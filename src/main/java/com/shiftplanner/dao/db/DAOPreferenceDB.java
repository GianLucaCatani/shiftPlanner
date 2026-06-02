package com.shiftplanner.dao.db;

import com.shiftplanner.dao.PreferenceDAO;
import com.shiftplanner.dao.EmployeeDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.EmployeePreference;
import com.shiftplanner.model.Employee;
import com.shiftplanner.model.TimeSlot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DAOPreferenceDB implements PreferenceDAO {
    
    private final EmployeeDAO employeeDAO;

    public DAOPreferenceDB(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public List<EmployeePreference> getPreferencesByPeriod(LocalDate startDate, LocalDate endDate) throws DAOException {
        List<EmployeePreference> preferences = new ArrayList<>();
        
        String query = "SELECT id, employee_id, pref_date, time_slot FROM preferences " +
                       "WHERE pref_date >= ? AND pref_date <= ?";
                       
        try (Connection conn = DBConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    long employeeId = rs.getLong("employee_id");
                    LocalDate prefDate = rs.getDate("pref_date").toLocalDate();
                    TimeSlot slot = TimeSlot.valueOf(rs.getString("time_slot").toUpperCase());
                    
                    Employee emp = this.employeeDAO.getEmployeeById(employeeId);
                    
                    if (emp != null) {
                        preferences.add(new EmployeePreference(id, prefDate, slot, emp));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore nel recupero delle preferenze dal DB", e);
        }
        
        return preferences;
    }
}
