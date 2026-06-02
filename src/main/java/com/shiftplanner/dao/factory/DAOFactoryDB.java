package com.shiftplanner.dao.factory;

import com.shiftplanner.dao.AbsenceDAO;

import com.shiftplanner.dao.EmployeeDAO;
import com.shiftplanner.dao.NotificationDAO;
import com.shiftplanner.dao.PreferenceDAO;
import com.shiftplanner.dao.ScheduleDAO;
import com.shiftplanner.dao.UserDAO;
import com.shiftplanner.dao.db.DAOAbsenceDB;
import com.shiftplanner.dao.db.DAOEmployeeDB;
import com.shiftplanner.dao.db.DAONotificationDB;
import com.shiftplanner.dao.db.DAOPreferenceDB;
import com.shiftplanner.dao.db.DAOScheduleDB;
import com.shiftplanner.dao.db.DAOUserDB;
import com.shiftplanner.exceptions.dao.DAOException;

public class DAOFactoryDB implements DAOFactory {
	
	// Pattern Singleton per la Factory concreta
    private static DAOFactoryDB instance = null;
    
    private DAOFactoryDB() {}
    
    public static synchronized DAOFactoryDB getInstance() {
        if (instance == null) {
            instance = new DAOFactoryDB();
        }
        return instance;
    }
    @Override
    public EmployeeDAO getEmployeeDAO() throws DAOException {
        return new DAOEmployeeDB();
    }
    @Override
    public ScheduleDAO getScheduleDAO() throws DAOException {
        return new DAOScheduleDB(); 
    }
    @Override
    public AbsenceDAO getAbsenceDAO() throws DAOException {
        return new DAOAbsenceDB(this.getEmployeeDAO());
    }
    @Override
    public PreferenceDAO getPreferenceDAO() throws DAOException {
        return new DAOPreferenceDB(this.getEmployeeDAO());
    }
    @Override
    public NotificationDAO getNotificationDAO() throws DAOException {
        return new DAONotificationDB();
    }
    
    @Override
    public UserDAO getUserDAO() throws DAOException {
        return new DAOUserDB();
    }
}
