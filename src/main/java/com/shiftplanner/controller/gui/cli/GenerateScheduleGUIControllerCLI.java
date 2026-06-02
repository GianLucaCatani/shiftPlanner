package com.shiftplanner.controller.gui.cli;

import java.time.LocalDate;

import java.time.format.DateTimeParseException;

import com.shiftplanner.bean.ScheduleBean;
import com.shiftplanner.controller.app.GenerateScheduleController;
import com.shiftplanner.controller.app.ManualEditController;
import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.engineering.service.NotificationManager;
import com.shiftplanner.exceptions.PeriodValidationException;
import com.shiftplanner.exceptions.ShiftPlannerException;

/*Controller GUI CLI per il caso d'uso Generate Work Schedule.
1. generateSchedule()  → genera la bozza (DRAFT) e la restituisce per la visualizzazione
2. modifyShift()       → modifica un turno nella bozza (opzionale, ripetibile)
3. publishSchedule()   → pubblica la bozza e invia le notifiche
*/
public class GenerateScheduleGUIControllerCLI {
	
	private final GenerateScheduleController appController;
	private final ManualEditController manualEditController;
	
	public GenerateScheduleGUIControllerCLI(DAOFactory daoFactory, NotificationManager notificationManager) {
		this.appController = new GenerateScheduleController(daoFactory, notificationManager);
		this.manualEditController = new ManualEditController(daoFactory, appController);
	}
	
	/*FASE 1 — Genera la bozza dello schedule.
	 Non pubblica ancora — il coordinatore può modificare i turni prima di pubblicare.*/
	public ScheduleBean generateSchedule(String startDateStr, String endDateStr) throws ShiftPlannerException {
		if (startDateStr == null || startDateStr.trim().isEmpty()
				|| endDateStr == null || endDateStr.trim().isEmpty()) {
			throw new ShiftPlannerException("Le date non possono essere vuote.");
		}
		
		LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(startDateStr.trim());
            endDate = LocalDate.parse(endDateStr.trim());
        } catch (DateTimeParseException e) {
            throw new ShiftPlannerException("Formato data non valido. Usa il formato YYYY-MM-DD (es. 2026-06-01).");
        }
        
        ScheduleBean bean = new ScheduleBean();
        try {
            bean.setStartDate(startDate);
            bean.setEndDate(endDate);
        } catch (PeriodValidationException e) {
            throw new ShiftPlannerException(e.getMessage());
        }
        
        // Genera la bozza — NON pubblica ancora
        return appController.generateDraft(bean);
    }

	/*FASE 2 — Modifica un turno nella bozza (opzionale).
	scheduleId -> ID della bozza da modificare
	shiftIndex -> indice del turno (0-based, come mostrato nella lista)
    newEmployeeId -> ID del nuovo dipendente da assegnare
	*/
	public ScheduleBean modifyShift(long scheduleId, int shiftIndex, long newEmployeeId)
			throws ShiftPlannerException {
		return manualEditController.modifyShift(scheduleId, shiftIndex, newEmployeeId);
	}

	/*FASE 3 — Pubblica la bozza.
	Cambia lo stato a PUBLISHED e invia le notifiche ai dipendenti.*/
	public ScheduleBean publishSchedule(long scheduleId) throws ShiftPlannerException {
		return appController.publishSchedule(scheduleId);
	}
}
