package com.shiftplanner.dao.inmemory;

import com.shiftplanner.dao.NotificationDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class NotificationDAOInMemory implements NotificationDAO {

    private static final Logger LOGGER = Logger.getLogger(NotificationDAOInMemory.class.getName());
    private static final List<Notification> DB = new ArrayList<>();

    @Override
    public void saveNotification(Notification notification) throws DAOException {
        DB.add(notification);
        LOGGER.info("(DEMO) Notifica inviata all'impiegato ID " + notification.getEmployeeId() + ": " + notification.getMessage());
    }

    @Override
    public List<Notification> getNotificationsByEmployeeId(long employeeId) throws DAOException {
        List<Notification> result = new ArrayList<>();
        for (Notification n : DB) {
            if (n.getEmployeeId() == employeeId) {
                result.add(n);
            }
        }
        return result;
    }
}