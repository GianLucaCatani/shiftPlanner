package com.shiftplanner.bean;

import com.shiftplanner.exceptions.ConstraintViolationException;

public class EmployeeBean {
	private long employeeId;
    private String fullName;
    private String role; // String al posto di EmployeeRole
    private int contractWeeklyHours;
    
    public EmployeeBean() {}
    
    public long getEmployeeId() { return employeeId; }
    public void setEmployeeId(long employeeId) { 
    	this.employeeId = employeeId; 
    }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) throws ConstraintViolationException {
    	if (fullName == null || fullName.trim().isEmpty()) {
    		throw new ConstraintViolationException("Il nome del dipendente non può essere vuoto.");
    	}
    	this.fullName = fullName; 
    }
    
    public String getRole() { return role; }
    public void setRole(String role) { 
    	this.role = role; 
    }
    
    public int getContractWeeklyHours() { return contractWeeklyHours; }
    public void setContractWeeklyHours(int contractWeeklyHours) throws ConstraintViolationException {
    	if (contractWeeklyHours <= 0) {
    		throw new ConstraintViolationException("Le ore contrattuali devono essere maggiori di zero.");
    	}
    	this.contractWeeklyHours = contractWeeklyHours; 
    }
}
