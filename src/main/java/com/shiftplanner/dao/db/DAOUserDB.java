package com.shiftplanner.dao.db;

import com.shiftplanner.dao.UserDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.EmployeeRole;
import com.shiftplanner.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOUserDB implements UserDAO {

    @Override
    public User getUserByCredentials(String username, String password) throws DAOException {
        String query = "SELECT id, username, password, employee_id, role FROM users " +
                       "WHERE username = ? AND password = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getInstance();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        long userId     = rs.getLong("id");
                        String uname    = rs.getString("username");
                        String pwd      = rs.getString("password");
                        long employeeId = rs.getLong("employee_id");
                        EmployeeRole role = EmployeeRole.valueOf(rs.getString("role").toUpperCase());
                        return new User(userId, uname, pwd, employeeId, role);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore durante il login dal database.", e);
        }
        return null;
    }

    @Override
    public void saveUser(User user) throws DAOException {
        String query = "INSERT INTO users (username, password, employee_id, role) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getInstance();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setLong(3, user.getEmployeeId());
                stmt.setString(4, user.getRole().name());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException("Errore durante il salvataggio dell'utente nel DB.", e);
        }
    }
}
