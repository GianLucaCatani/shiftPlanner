package com.shiftplanner.dao;

import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Employee;

import java.util.List;

public interface EmployeeDAO {
	List<Employee> getAllEmployees() throws DAOException;
	
	Employee getEmployeeById(long id) throws DAOException;
	
	public void saveEmployee(Employee employee) throws DAOException;
}
