package com.shiftplanner.dao.db;

import com.shiftplanner.dao.NotificationDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAONotificationDB implements NotificationDAO {

    @Override
    public void saveNotification(Notification notification) throws DAOException {
       
        String query = "INSERT INTO notifications (employee_id, message) VALUES (?, ?)";
        
        Connection conn = null;
        try {
        	conn = DBConnection.getInstance();
        	try (PreparedStatement stmt = conn.prepareStatement(query)) {
        		stmt.setLong(1, notification.getEmployeeId());
                stmt.setString(2, notification.getMessage());
                stmt.executeUpdate(); 
        	}
        } catch (SQLException e) {
            throw new DAOException("Errore durante il salvataggio della notifica nel DB", e);
        }
    }
    
    @Override
    public List<Notification> getNotificationsByEmployeeId(long employeeId) throws DAOException {
        String query = "SELECT id, employee_id, message FROM notifications " +
                       "WHERE employee_id = ? ORDER BY id DESC";
        List<Notification> result = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBConnection.getInstance();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setLong(1, employeeId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        long id  = rs.getLong("id");
                        long emp = rs.getLong("employee_id");
                        String msg = rs.getString("message");
                        result.add(new Notification(id, emp, msg));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore nel recupero delle notifiche per il dipendente ID " + employeeId, e);
        }
        return result;
    }
}