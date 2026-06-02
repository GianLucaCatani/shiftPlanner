package com.shiftplanner.dao.factory;

import com.shiftplanner.dao.*;
import com.shiftplanner.dao.inmemory.AbsenceDAOInMemory;
import com.shiftplanner.dao.inmemory.EmployeeDAOInMemory;
import com.shiftplanner.dao.inmemory.NotificationDAOInMemory;
import com.shiftplanner.dao.inmemory.PreferenceDAOInMemory;
import com.shiftplanner.dao.inmemory.ScheduleDAOInMemory;
import com.shiftplanner.dao.inmemory.UserDAOInMemory;
import com.shiftplanner.exceptions.dao.DAOException;

public class DAOFactoryDemo implements DAOFactory {
    
    private static DAOFactoryDemo instance = null;
    
    private DAOFactoryDemo() {}
    
    public static synchronized DAOFactoryDemo getInstance() {
        if (instance == null) instance = new DAOFactoryDemo();
        return instance;
    }

    @Override
    public EmployeeDAO getEmployeeDAO() throws DAOException { 
    	return new EmployeeDAOInMemory(); 
    } 
    
    @Override
    public ScheduleDAO getScheduleDAO() throws DAOException { 
    	return new ScheduleDAOInMemory(); 
    }
    
    @Override
    public AbsenceDAO getAbsenceDAO() throws DAOException { 
    	return new AbsenceDAOInMemory(); 
    }
    
    @Override
    public PreferenceDAO getPreferenceDAO() throws DAOException { 
    	return new PreferenceDAOInMemory(); 
    }
    
    @Override
    public NotificationDAO getNotificationDAO() throws DAOException { 
    	return new NotificationDAOInMemory(); 
    }
    
    @Override
    public UserDAO getUserDAO() throws DAOException {
        return new UserDAOInMemory();
    }
}