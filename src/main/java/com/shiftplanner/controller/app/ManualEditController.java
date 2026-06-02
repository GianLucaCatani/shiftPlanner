package com.shiftplanner.controller.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.shiftplanner.bean.ScheduleBean;
import com.shiftplanner.dao.EmployeeDAO;
import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.exceptions.ShiftPlannerException;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Employee;
import com.shiftplanner.model.Schedule;
import com.shiftplanner.util.ToBeanConverter;

/*Controller applicativo per la modifica manuale dei turni.
Permette al coordinatore di modificare i turni della bozza prima della pubblicazione definitiva*/
public class ManualEditController {

    private static final Logger LOGGER = Logger.getLogger(ManualEditController.class.getName());

    private final DAOFactory daoFactory;
    private final GenerateScheduleController generateScheduleController;

    public ManualEditController(DAOFactory daoFactory,
                                GenerateScheduleController generateScheduleController) {
        this.daoFactory = daoFactory;
        this.generateScheduleController = generateScheduleController;
    }

    //Modifica il dipendente assegnato a un turno specifico nella bozza.
    //shiftIndex -> indice del turno da modificare
    //employeeId -> ID del nuovo dipendente da assegnare
    public ScheduleBean modifyShift(long scheduleId, int shiftIndex, long employeeId)
            throws ShiftPlannerException {

        // Recupera il dipendente dal DAO
        Employee newEmployee = validateManualChange(employeeId);

        // Recupera la bozza dalla cache del GenerateScheduleController
        Schedule draft = generateScheduleController.getDraftFromCache(scheduleId);
        if (draft == null) {
            throw new ShiftPlannerException(
                    "Bozza non trovata per scheduleId=" + scheduleId +
                    ". Genera prima uno schedule.");
        }

        // Applica la modifica sul model
        try {
            draft.reassignShift(shiftIndex, newEmployee);
        } catch (IndexOutOfBoundsException e) {
            throw new ShiftPlannerException(
                    "Indice turno non valido: " + shiftIndex +
                    ". Lo schedule ha " + draft.getShifts().size() + " turni.");
        } catch (IllegalStateException e) {
            throw new ShiftPlannerException(e.getMessage());
        }

        // Restituisce il bean aggiornato per la visualizzazione
        return ToBeanConverter.fromSchedule(draft);
    }

    //Verifica che il dipendente esista nel sistema.
    //EmployeeId -> ID del dipendente da verificare
    public Employee validateManualChange(long employeeId) throws ShiftPlannerException {
        try {
            EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            if (employee == null) {
                throw new ShiftPlannerException(
                        "Dipendente con ID " + employeeId + " non trovato.");
            }
            return employee;
        } catch (DAOException e) {
            LOGGER.log(Level.WARNING, "Errore DAO durante la validazione del dipendente", e);
            throw new ShiftPlannerException(
                    "Errore nel recupero del dipendente. Riprovare.", e);
        }
    }
}
