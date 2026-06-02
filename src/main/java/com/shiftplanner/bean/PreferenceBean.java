package com.shiftplanner.bean;

import java.time.LocalDate;

import com.shiftplanner.exceptions.PeriodValidationException;

public class PreferenceBean {
    private long preferenceId;
    private LocalDate preferredDate;
    private String timeSlot;     // String al posto dell'Enum TimeSlot
    private EmployeeBean employee;

    public PreferenceBean() {}

    public long getPreferenceId() { return preferenceId; }
    public void setPreferenceId(long preferenceId) { 
    	this.preferenceId = preferenceId; 
    }

    public LocalDate getPreferredDate() { return preferredDate; }
    public void setPreferredDate(LocalDate preferredDate) throws PeriodValidationException { 
    	if (preferredDate == null) {
            throw new PeriodValidationException("La data della preferenza non può essere nulla.");
        }
        this.preferredDate = preferredDate;
    }


    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { 
    	this.timeSlot = timeSlot; 
    }

    public EmployeeBean getEmployee() { return employee; }
    public void setEmployee(EmployeeBean employee) { 
    	this.employee = employee; 
    }
}