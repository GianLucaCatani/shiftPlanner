package com.shiftplanner.dao;

import com.shiftplanner.model.Notification;

import java.util.List;

import com.shiftplanner.exceptions.dao.DAOException;

public interface NotificationDAO {
	void saveNotification(Notification notification) throws DAOException;
	
	//Restituisce tutte le notifiche di un dipendente, ordinate dalla più recente.
	List<Notification> getNotificationsByEmployeeId(long employeeId) throws DAOException;
}
