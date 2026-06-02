package com.shiftplanner.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Shift {
	private long shiftId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Employee employee;
    
    public Shift(long shiftId, LocalDate date, LocalTime startTime, LocalTime endTime, Employee employee) {
        this.shiftId = shiftId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.employee = employee;
    }
    
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
    
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { 
    	this.employee = employee; 
    }
}
