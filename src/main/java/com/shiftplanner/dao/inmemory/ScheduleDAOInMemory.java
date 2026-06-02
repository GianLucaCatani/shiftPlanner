package com.shiftplanner.dao.inmemory;

import com.shiftplanner.dao.ScheduleDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Schedule;
import com.shiftplanner.model.Shift;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScheduleDAOInMemory implements ScheduleDAO {

    private static final Logger LOGGER = Logger.getLogger(ScheduleDAOInMemory.class.getName());
    private static final List<Schedule> DB = new ArrayList<>();

    @Override
    public void saveSchedule(Schedule schedule) throws DAOException {
        DB.add(schedule);
        LOGGER.log(Level.INFO, "(DEMO) Schedule salvato in memoria RAM. Totale turni salvati: {0}",
                schedule.getShifts().size());
    }
    
    @Override
    public Schedule getScheduleById(long id) throws DAOException {
        // Cerca lo schedule nella memoria RAM
        for (Schedule s : DB) {
            if (s.getScheduleId() == id) {
                return s;
            }
        }
        return null;
    }

    @Override
    public List<Shift> getShiftsByEmployeeId(long employeeId) throws DAOException {
        List<Shift> result = new ArrayList<>();
        for (Schedule s : DB) {
            for (Shift shift : s.getShifts()) {
                if (shift.getEmployee() != null &&
                        shift.getEmployee().getEmployeeId() == employeeId) {
                    result.add(shift);
                }
            }
        }
        return result;
    }
}