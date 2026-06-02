package com.shiftplanner.model;

public class Employee {
	private long employeeId;
	private String fullName;
	private EmployeeRole role;
	private int contractWeeklyHours;
	
	public Employee(long employeeId, String fullName, EmployeeRole role, int contractWeeklyHours) {
		this.employeeId = employeeId;
		this.fullName = fullName;
		this.role = role;
		this.contractWeeklyHours = contractWeeklyHours;
	}
	
	public long getEmployeeId() { return employeeId; }
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}
	
	public String getFullName() { return fullName; }
	public void setFullName(String fullName) { 
		this.fullName = fullName; 
	}
	
	public EmployeeRole getRole() { return role; }
	public void setRole(EmployeeRole role) {
		this.role = role;
	}
	
	public int getContractWeeklyHours() { return contractWeeklyHours; }
	public void setContractWeeklyHours(int contractWeeklyHours) {
		this.contractWeeklyHours = contractWeeklyHours;
	}
}
