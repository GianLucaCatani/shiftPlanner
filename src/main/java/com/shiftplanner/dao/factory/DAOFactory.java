package com.shiftplanner.dao.factory;

import com.shiftplanner.dao.AbsenceDAO;
import com.shiftplanner.dao.EmployeeDAO;
import com.shiftplanner.dao.NotificationDAO;
import com.shiftplanner.dao.PreferenceDAO;
import com.shiftplanner.dao.ScheduleDAO;
import com.shiftplanner.dao.UserDAO;
import com.shiftplanner.exceptions.dao.DAOException;

public interface DAOFactory {
	EmployeeDAO getEmployeeDAO() throws DAOException;
    ScheduleDAO getScheduleDAO() throws DAOException;
    AbsenceDAO getAbsenceDAO() throws DAOException;
    PreferenceDAO getPreferenceDAO() throws DAOException;
    NotificationDAO getNotificationDAO() throws DAOException;
    UserDAO getUserDAO() throws DAOException;
}
