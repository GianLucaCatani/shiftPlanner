package com.shiftplanner.engineering.service;

import com.shiftplanner.dao.NotificationDAO; 
import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.engineering.observer.Observer;
import com.shiftplanner.engineering.observer.Subject;
import com.shiftplanner.model.Notification;
import com.shiftplanner.model.Schedule;
import com.shiftplanner.model.Shift;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationManager implements Observer {
	
	private static final Logger LOGGER = Logger.getLogger(NotificationManager.class.getName());
	
	private final DAOFactory daoFactory; 
    
    public NotificationManager(DAOFactory daoFactory) {
    	this.daoFactory = daoFactory;
    }
    
    @Override 
    public void update(Subject subject) {
        if (subject instanceof Schedule schedule) {
            sendNotificationsForSchedule(schedule);
        }
    }
    
    private void sendNotificationsForSchedule(Schedule schedule) {
        try {
            NotificationDAO notificationDAO = daoFactory.getNotificationDAO();
            
            // Per ogni turno nel calendario, genera e salva una notifica
            for (Shift shift : schedule.getShifts()) {
                String message = "Nuovo turno assegnato per il: " + shift.getDate();
                Notification notification = new Notification(0, shift.getEmployee().getEmployeeId(), message);
                notificationDAO.saveNotification(notification);
            }
            
            LOGGER.info("Notifiche generate e salvate correttamente!");
            
        } catch (Exception e) {
            // Logghiamo l'errore senza interrompere il flusso principale:
            // un fallimento nelle notifiche non deve bloccare la pubblicazione dello schedule
            LOGGER.log(Level.WARNING, "Errore durante la generazione delle notifiche", e);
        }
    }
}
