package com.shiftplanner.controller.app;

import com.shiftplanner.bean.NotificationBean;
import com.shiftplanner.bean.ShiftBean;
import com.shiftplanner.dao.NotificationDAO;
import com.shiftplanner.dao.ScheduleDAO;
import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.exceptions.ShiftPlannerException;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Notification;
import com.shiftplanner.model.Shift;
import com.shiftplanner.util.ToBeanConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//Controller applicativo per la dashboard del dipendente.
//Recupera i turni assegnati e le notifiche dell'utente autenticato.
public class EmployeeDashboardController {

    private static final Logger LOGGER = Logger.getLogger(EmployeeDashboardController.class.getName());

    private final DAOFactory daoFactory;

    public EmployeeDashboardController(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    //Restituisce tutti i turni assegnati al dipendente.
    public List<ShiftBean> getMyShifts(long employeeId) throws ShiftPlannerException {
        try {
            ScheduleDAO scheduleDAO = daoFactory.getScheduleDAO();
            List<Shift> shifts = scheduleDAO.getShiftsByEmployeeId(employeeId);
            List<ShiftBean> beans = new ArrayList<>();
            for (Shift shift : shifts) {
                beans.add(ToBeanConverter.fromShift(shift));
            }
            return beans;
        } catch (DAOException e) {
            LOGGER.log(Level.WARNING, "Errore DAO nel recupero dei turni del dipendente", e);
            throw new ShiftPlannerException("Errore nel recupero dei tuoi turni. Riprovare.", e);
        }
    }

    //Restituisce tutte le notifiche del dipendente.
    public List<NotificationBean> getMyNotifications(long employeeId) throws ShiftPlannerException {
        try {
            NotificationDAO notificationDAO = daoFactory.getNotificationDAO();
            List<Notification> notifications = notificationDAO.getNotificationsByEmployeeId(employeeId);
            List<NotificationBean> beans = new ArrayList<>();
            for (Notification n : notifications) {
                NotificationBean bean = new NotificationBean();
                bean.setNotificationId(n.getNotificationId());
                bean.setEmployeeId(n.getEmployeeId());
                bean.setMessage(n.getMessage());
                bean.setCreateAt(n.getCreateAt());
                beans.add(bean);
            }
            return beans;
        } catch (DAOException e) {
            LOGGER.log(Level.WARNING, "Errore DAO nel recupero delle notifiche del dipendente", e);
            throw new ShiftPlannerException("Errore nel recupero delle tue notifiche. Riprovare.", e);
        }
    }
}
