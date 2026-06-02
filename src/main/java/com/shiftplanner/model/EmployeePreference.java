package com.shiftplanner.model;

import java.time.LocalDate;

/*Rappresenta la preferenza di turno di un dipendente per una data specifica
 * Un dipendente può indicare che per un certo giorno prefereisce un determinato
 * slot orario (MORNING, AFTERNOON, NIGHT)
 */
public class EmployeePreference {
	private long preferenceId;
	private LocalDate preferredDate;
	private TimeSlot timeSlot;
	private Employee employee;
	
	public EmployeePreference(long preferenceId, LocalDate preferredDate, TimeSlot timeSlot, Employee employee) {
		this.preferenceId = preferenceId;
        this.preferredDate = preferredDate;
        this.timeSlot = timeSlot;
        this.employee = employee;
	}
	
	public long getPreferenceId() { return preferenceId; }
    public void setPreferenceId(long preferenceId) { 
    	this.preferenceId = preferenceId; 
    }
    
    public LocalDate getPreferredDate() { return preferredDate; }
    public void setPreferredDate(LocalDate preferredDate) { 
    	this.preferredDate = preferredDate; 
    }
    
    public TimeSlot getTimeSlot() { return timeSlot; }
    public void setTimeSlot(TimeSlot timeSlot) { 
    	this.timeSlot = timeSlot; 
    }
    
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { 
    	this.employee = employee; 
    }
}
