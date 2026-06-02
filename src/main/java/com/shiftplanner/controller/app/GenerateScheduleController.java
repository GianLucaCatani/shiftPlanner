package com.shiftplanner.controller.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.shiftplanner.bean.ScheduleBean;
import com.shiftplanner.dao.AbsenceDAO;
import com.shiftplanner.dao.EmployeeDAO;
import com.shiftplanner.dao.PreferenceDAO;
import com.shiftplanner.dao.ScheduleDAO;
import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.engineering.service.NotificationManager;
import com.shiftplanner.engineering.service.ScheduleGeneratorEngine;
import com.shiftplanner.exceptions.ShiftPlannerException;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Absence;
import com.shiftplanner.model.Employee;
import com.shiftplanner.model.EmployeePreference;
import com.shiftplanner.model.Schedule;
import com.shiftplanner.util.ToBeanConverter;

public class GenerateScheduleController {
	
	private static final Logger LOGGER = Logger.getLogger(GenerateScheduleController.class.getName());
    
    private final DAOFactory daoFactory;
    private final NotificationManager notificationManager;
    
    // Cache in memoria degli schedule in bozza, indicizzati per scheduleId.
    // Serve per recuperare lo Schedule tra generateDraft() e publishSchedule().
    private final Map<Long, Schedule> draftCache = new HashMap<>();
    
    
    public GenerateScheduleController(DAOFactory daoFactory, NotificationManager notificationManager) {
        this.daoFactory = daoFactory;
        this.notificationManager = notificationManager;
    }
    
    /*FASE 1 — Genera la bozza dello schedule e la salva in stato DRAFT.
    Non pubblica e non invia notifiche. La bozza viene tenuta in cache per permettere modifiche manuali.*/
    public ScheduleBean generateDraft(ScheduleBean requestBean) throws ShiftPlannerException {
    	
        Schedule newSchedule = new Schedule(0, requestBean.getStartDate(), requestBean.getEndDate());
        // L'observer viene registrato ora ma verrà notificato solo al publish()
        newSchedule.attach(notificationManager);
        
        try {
        	EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();
        	AbsenceDAO absenceDAO = daoFactory.getAbsenceDAO();
        	PreferenceDAO preferenceDAO = daoFactory.getPreferenceDAO();
        	
        	List<Employee> employees = employeeDAO.getAllEmployees();
            List<Absence> absences = absenceDAO.getAbsencesByPeriod(
                    requestBean.getStartDate(), requestBean.getEndDate());
            List<EmployeePreference> preferences = preferenceDAO.getPreferencesByPeriod(
                    requestBean.getStartDate(), requestBean.getEndDate());
            
            ScheduleGeneratorEngine engine = new ScheduleGeneratorEngine();
            engine.generateShifts(newSchedule, employees, absences, preferences);
            
            //Salvataggio in cache
            //Assegniamo un ID temporaneo solo per far funzionare la cache della GUI
            long tempDraftId = System.currentTimeMillis();
            newSchedule.setScheduleId(tempDraftId);
            draftCache.put(tempDraftId, newSchedule);
            
            return ToBeanConverter.fromSchedule(newSchedule);
            
        } catch (DAOException e) {
            LOGGER.log(Level.WARNING, "Errore DAO durante la generazione della bozza", e);
            throw new ShiftPlannerException("Errore durante il salvataggio della bozza. Riprovare.", e);
        }
    }
    
    /*Restituisce lo schedule in bozza dalla cache, o null se non trovato.
    Usato da ManualEditController per accedere alla bozza da modificare.*/
    public Schedule getDraftFromCache(long scheduleId) {
        return draftCache.get(scheduleId);
    }

    /*FASE 2 — Pubblica lo schedule in bozza. 
    Cambia lo stato da DRAFT a PUBLISHED e attiva il pattern Observer che genera e salva le notifiche per i dipendenti.*/
    public ScheduleBean publishSchedule(long scheduleId) throws ShiftPlannerException {
        Schedule schedule = draftCache.get(scheduleId);
        if (schedule == null) {
            throw new ShiftPlannerException("Bozza non trovata. Genera prima uno schedule.");
        }
        
        try {
        	//Salva nel database ora che la bozza è definitiva
        	//Il DAO assegnerà l'ID definitivo di MySQL allo schedule sostituendo quello temporaneo
        	ScheduleDAO scheduleDAO = daoFactory.getScheduleDAO();
        	scheduleDAO.saveSchedule(schedule);
        	
            // Pubblica: cambia stato e notifica gli observer (NotificationManager)
            schedule.publish();
            
            // Rimuove dalla cache — non serve più
            draftCache.remove(scheduleId);
            
            return ToBeanConverter.fromSchedule(schedule);
            
        } catch (DAOException e) {
        	LOGGER.log(Level.WARNING, "Errore di persistenza durante la pubblicazione", e);
            throw new ShiftPlannerException("Errore durante la pubblicazione definitiva. Riprovare.", e);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Errore durante la pubblicazione dello schedule", e);
            throw new ShiftPlannerException("Errore durante la pubblicazione. Riprovare.", e);
        }
    }
}
