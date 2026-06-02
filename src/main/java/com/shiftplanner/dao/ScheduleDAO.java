package com.shiftplanner.dao;

import com.shiftplanner.model.Schedule;
import com.shiftplanner.model.Shift;
import java.util.List;
import com.shiftplanner.exceptions.dao.DAOException;

public interface ScheduleDAO {
	void saveSchedule(Schedule schedule) throws DAOException;
	
	Schedule getScheduleById(long id) throws DAOException;
	
	//Restituisce tutti i turni assegnati a un dipendente specifico tra tutti gli schedule pubblicati
	List<Shift> getShiftsByEmployeeId(long employeeId) throws DAOException;
}
