package com.shiftplanner.dao.db;

import com.shiftplanner.dao.EmployeeDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Employee;
import com.shiftplanner.model.EmployeeRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOEmployeeDB implements EmployeeDAO {

    @Override
    public List<Employee> getAllEmployees() throws DAOException {
        List<Employee> employees = new ArrayList<>();

        String query = "SELECT id, full_name, role, contract_hours FROM employees";
        
        Connection conn = null;
        try {
            conn = DBConnection.getInstance();
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    String name = rs.getString("full_name");
                    EmployeeRole role = EmployeeRole.valueOf(rs.getString("role").toUpperCase());
                    int hours = rs.getInt("contract_hours");
                    employees.add(new Employee(id, name, role, hours));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore nel recupero degli impiegati dal database.", e);
        }
        return employees;
    }

    @Override
    public Employee getEmployeeById(long id) throws DAOException {
        String query = "SELECT id, full_name, role, contract_hours FROM employees WHERE id = ?";
        
        Connection conn = null;
        try {
            conn = DBConnection.getInstance();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setLong(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String name = rs.getString("full_name");
                        EmployeeRole role = EmployeeRole.valueOf(rs.getString("role").toUpperCase());
                        int hours = rs.getInt("contract_hours");
                        return new Employee(id, name, role, hours);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore nel recupero dell'impiegato con ID " + id, e);
        }
        return null;
    }

    @Override
    public void saveEmployee(Employee employee) throws DAOException {
        String query = "INSERT INTO employees (full_name, role, contract_hours) VALUES (?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DBConnection.getInstance();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, employee.getFullName());
                stmt.setString(2, employee.getRole().name());
                stmt.setInt(3, employee.getContractWeeklyHours());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException("Errore durante il salvataggio dell'impiegato nel DB.", e);
        }
    }
}
