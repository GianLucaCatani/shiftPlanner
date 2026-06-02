package com.shiftplanner.dao.factory;

import com.shiftplanner.dao.*;
import com.shiftplanner.dao.fs.AbsenceDAOFileSystem;
import com.shiftplanner.dao.fs.EmployeeDAOFileSystem;
import com.shiftplanner.dao.fs.NotificationDAOFileSystem;
import com.shiftplanner.dao.fs.PreferenceDAOFileSystem;
import com.shiftplanner.dao.fs.ScheduleDAOFileSystem;
import com.shiftplanner.dao.fs.UserDAOFileSystem;
import com.shiftplanner.exceptions.dao.DAOException;

public class DAOFactoryFS implements DAOFactory {
    
    private static DAOFactoryFS instance = null;
    
    private DAOFactoryFS() {}
    
    public static synchronized DAOFactoryFS getInstance() {
        if (instance == null) instance = new DAOFactoryFS();
        return instance;
    }

    @Override
    public EmployeeDAO getEmployeeDAO() throws DAOException { 
    	return new EmployeeDAOFileSystem(); 
    }
    
    @Override
    public ScheduleDAO getScheduleDAO() throws DAOException { 
    	return new ScheduleDAOFileSystem(); 
    }
    
    @Override
    public AbsenceDAO getAbsenceDAO() throws DAOException { 
    	return new AbsenceDAOFileSystem(this.getEmployeeDAO()); 
    }
    
    @Override
    public PreferenceDAO getPreferenceDAO() throws DAOException { 
    	return new PreferenceDAOFileSystem(this.getEmployeeDAO()); 
    }
    
    @Override
    public NotificationDAO getNotificationDAO() throws DAOException { 
    	return new NotificationDAOFileSystem(); 
    }
    
    @Override
    public UserDAO getUserDAO() throws DAOException {
        return new UserDAOFileSystem();
    }
}