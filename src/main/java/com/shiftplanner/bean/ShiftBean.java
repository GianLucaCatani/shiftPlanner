package com.shiftplanner.bean;

import java.time.LocalDate;

import java.time.LocalTime;

public class ShiftBean {
	 private long shiftId;
	    private LocalDate date;
	    private LocalTime startTime;
	    private LocalTime endTime;
	    private EmployeeBean employee;
	    
	    public ShiftBean() {}
	    
	    public long getShiftId() { return shiftId; }
	    public void setShiftId(long shiftId) { 
	    	this.shiftId = shiftId; 
	    }
	    
	    public LocalDate getDate() { return date; }
	    public void setDate(LocalDate date) { 
	    	this.date = date; 
	    }
	    
	    public LocalTime getStartTime() { return startTime; }
	    public void setStartTime(LocalTime startTime) {
	    	this.startTime = startTime; 
	    }
	    
	    public LocalTime getEndTime() { return endTime; }
	    public void setEndTime(LocalTime endTime) {
	    	this.endTime = endTime; 
	    }
	    
	    public EmployeeBean getEmployee() { return employee; }
	    public void setEmployee(EmployeeBean employee) { 
	    	this.employee = employee; 
	    }
}
