package com.shiftplanner.model;

import java.time.LocalDate;

public class Absence {
	private long absenceId;
	private LocalDate startDate;
	private LocalDate endDate;
	private AbsenceType type;
	private Employee employee;
	
	public Absence(long absenceId, LocalDate startDate, LocalDate endDate, AbsenceType type, Employee employee) {
		this.absenceId = absenceId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = type;
		this.employee = employee;
	}
	
	public long getAbsenceId() { return absenceId; }
	public void setAbsenceId(long absenceId) {
		this.absenceId = absenceId;
	}
	
	public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { 
    	this.startDate = startDate; 
    }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { 
    	this.endDate = endDate; 
    }
    
    public AbsenceType getType() { return type; }
    public void setType(AbsenceType type) { 
    	this.type = type; 
    }
    
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { 
    	this.employee = employee; 
    }
    
    public boolean overlapsWith(LocalDate date) {
        return (date.equals(startDate) || date.isAfter(startDate)) && (date.equals(endDate) || date.isBefore(endDate));
    }
}
