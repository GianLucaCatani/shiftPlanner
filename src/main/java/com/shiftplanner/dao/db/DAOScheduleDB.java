package com.shiftplanner.dao.db;

import com.shiftplanner.dao.ScheduleDAO;

import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Schedule;
import com.shiftplanner.model.Shift;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DAOScheduleDB implements ScheduleDAO {

    @Override
    public void saveSchedule(Schedule schedule) throws DAOException {
        String insertScheduleQuery = "INSERT INTO schedules (start_date, end_date) VALUES (?, ?)";
        String insertShiftQuery = "INSERT INTO shifts (schedule_id, shift_date, start_time, end_time, employee_id) VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DBConnection.getInstance();
            
            // Disabilitiamo l'autocommit per gestire manualmente la TRANSAZIONE
            conn.setAutoCommit(false);
            
            // 1. Salviamo i dati generali dello Schedule e recupera l'ID generato da MySQL
            long generatedScheduleId;
            try (PreparedStatement scheduleStmt = conn.prepareStatement(insertScheduleQuery, Statement.RETURN_GENERATED_KEYS)) {
            	
            	scheduleStmt.setDate(1, java.sql.Date.valueOf(schedule.getPeriodStartDate()));
                scheduleStmt.setDate(2, java.sql.Date.valueOf(schedule.getPeriodEndDate()));
                scheduleStmt.executeUpdate();
                
                // Legge l'ID AUTO_INCREMENT assegnato da MySQL
                try (ResultSet keys = scheduleStmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        generatedScheduleId = keys.getLong(1);
                        // Aggiorna l'oggetto Schedule con l'ID reale del DB
                        schedule.setScheduleId(generatedScheduleId);
                    } else {
                        throw new DAOException("Impossibile recuperare l'ID generato per lo schedule.");
                    }
                }
            }
            
            // 2. Salviamo la lista dei Turni (Shifts) collegati a questo Schedule
            // Inserisce i turni usando l'ID reale recuperato
            try (PreparedStatement shiftStmt = conn.prepareStatement(insertShiftQuery)) {
            	shiftStmt.setLong(1, generatedScheduleId); // Chiave esterna verso schedules.id (è costante per tutti i turni di questo schedule)
            	for (Shift shift : schedule.getShifts()) {
                    shiftStmt.setDate(2, java.sql.Date.valueOf(shift.getDate()));
                    shiftStmt.setTime(3, java.sql.Time.valueOf(shift.getStartTime()));
                    shiftStmt.setTime(4, java.sql.Time.valueOf(shift.getEndTime()));
                    shiftStmt.setLong(5, shift.getEmployee().getEmployeeId());
                    
                    // addBatch mette l'istruzione in "coda" anziché eseguirla subito
                    shiftStmt.addBatch(); 
                }
                shiftStmt.executeBatch(); // Invia l'intera coda al DB in un colpo solo
            }
            
            // 3. Se non ci sono state eccezioni fino a qui, salviamo definitivamente sul DB
            conn.commit();
            
        } catch (SQLException e) {
            // Se c'è stato un errore annulliamo le scritture per evitare tabelle a metà.
            if (conn != null) {
                try { 
                	conn.rollback(); 
                } catch (SQLException ex) { 
                	ex.printStackTrace(); 
                }
            }
            
            throw new DAOException("Errore durante il salvataggio dello Schedule e dei Turni: Rollback effettuato", e);
        } finally {
            // Ripristiniamo il comportamento standard del DB
            if (conn != null) {
                try { 
                	conn.setAutoCommit(true); 
                } catch (SQLException e) { 
                	e.printStackTrace(); 
                }
            }
        }
    }

    @Override
    public Schedule getScheduleById(long id) throws DAOException {
        // Come per il FileSystem, un'implementazione reale farebbe due SELECT con le JOIN, 
        // ma per il caso d'uso principale basta il salvataggio
        return null; 
    }
    
    @Override
    public List<Shift> getShiftsByEmployeeId(long employeeId) throws DAOException {
        String query = "SELECT s.id, s.shift_date, s.start_time, s.end_time, " +
                       "e.id AS emp_id, e.full_name, e.role, e.contract_hours " +
                       "FROM shifts s " +
                       "JOIN employees e ON s.employee_id = e.id " +
                       "WHERE s.employee_id = ? " +
                       "ORDER BY s.shift_date, s.start_time";

        List<Shift> result = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBConnection.getInstance();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setLong(1, employeeId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        long shiftId = rs.getLong("id");
                        java.time.LocalDate date = rs.getDate("shift_date").toLocalDate();
                        java.time.LocalTime start = rs.getTime("start_time").toLocalTime();
                        java.time.LocalTime end   = rs.getTime("end_time").toLocalTime();
                        long empId   = rs.getLong("emp_id");
                        String name  = rs.getString("full_name");
                        com.shiftplanner.model.EmployeeRole role =
                                com.shiftplanner.model.EmployeeRole.valueOf(rs.getString("role").toUpperCase());
                        int hours    = rs.getInt("contract_hours");
                        com.shiftplanner.model.Employee emp =
                                new com.shiftplanner.model.Employee(empId, name, role, hours);
                        result.add(new Shift(shiftId, date, start, end, emp));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore nel recupero dei turni per il dipendente ID " + employeeId, e);
        }
        return result;
    }
}