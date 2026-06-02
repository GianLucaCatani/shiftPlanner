package com.shiftplanner.dao.inmemory;

import com.shiftplanner.dao.EmployeeDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOInMemory implements EmployeeDAO {

    // Simuliamo un database in RAM
    private static final List<Employee> DB = new ArrayList<>();

    @Override
    public List<Employee> getAllEmployees() throws DAOException {
        // Restituiamo una copia della lista per evitare modifiche accidentali
        return new ArrayList<>(DB);
    }
    
    @Override
    public Employee getEmployeeById(long id) throws DAOException {
        // Cerca l'impiegato nella lista in memoria e lo restituisce (oppure null se non esiste)
        for (Employee emp : DB) {
            if (emp.getEmployeeId() == id) {
                return emp;
            }
        }
        return null;
    }
    
    @Override
    public void saveEmployee(Employee employee) throws DAOException {
        DB.add(employee); // Aggiunge il dipendente creato dalla GUI alla memoria RAM
    }
}