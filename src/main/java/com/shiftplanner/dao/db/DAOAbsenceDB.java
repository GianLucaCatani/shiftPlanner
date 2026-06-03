package com.shiftplanner.dao.db;

import com.shiftplanner.dao.AbsenceDAO;
import com.shiftplanner.dao.EmployeeDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Absence;
import com.shiftplanner.model.AbsenceType;
import com.shiftplanner.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DAOAbsenceDB implements AbsenceDAO {
    
    private final EmployeeDAO employeeDAO;

    public DAOAbsenceDB(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public List<Absence> getAbsencesByPeriod(LocalDate startDate, LocalDate endDate) throws DAOException {
        List<Absence> absences = new ArrayList<>();
        
        String query = "SELECT id, employee_id, start_date, end_date, type FROM absences " +
                       "WHERE NOT (end_date < ? OR start_date > ?)";
           
        Connection conn = null;
        try {
            conn = DBConnection.getInstance();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
            	// JDBC richiede java.sql.Date, quindi convertiamo i nostri LocalDate
                stmt.setDate(1, java.sql.Date.valueOf(startDate));
                stmt.setDate(2, java.sql.Date.valueOf(endDate));
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        long id = rs.getLong("id");
                        long employeeId = rs.getLong("employee_id");
                        LocalDate start = rs.getDate("start_date").toLocalDate();
                        LocalDate end = rs.getDate("end_date").toLocalDate();
                        AbsenceType type = AbsenceType.valueOf(rs.getString("type").toUpperCase());
                        
                        // Ricostruiamo l'Employee delegando al DAO apposito
                        Employee emp = this.employeeDAO.getEmployeeById(employeeId);
                        
                        if (emp != null) {
                            absences.add(new Absence(id, start, end, type, emp));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore nel recupero delle assenze dal DB", e);
        }
        
        return absences;
    }
}