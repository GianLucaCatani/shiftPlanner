package com.shiftplanner.bean;

import java.time.LocalDate;

import com.shiftplanner.exceptions.ConstraintViolationException;
import com.shiftplanner.exceptions.PeriodValidationException;

public class AbsenceBean {
    private long absenceId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type; // String al posto di AbsenceType
    private EmployeeBean employee;

    public long getAbsenceId() { return absenceId; }
    public void setAbsenceId(long absenceId) { 
    	this.absenceId = absenceId; 
    }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) throws PeriodValidationException {
    	if (startDate != null && this.endDate != null && startDate.isAfter(this.endDate)) {
    		throw new PeriodValidationException("La data di inizio assenza non può superare quella di fine.");
    	}
    	this.startDate = startDate; 
    }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) throws PeriodValidationException {
    	if (this.startDate != null && endDate != null && this.startDate.isAfter(endDate)) {
    		throw new PeriodValidationException("La data di fine assenza non piò precedere quella di inizio.");
    	}
    	this.endDate = endDate; 
    }

    public String getType() { return type; }
    public void setType(String type) throws ConstraintViolationException {
    	if (type == null || type.trim().isEmpty()) {
    		throw new ConstraintViolationException("Il tipo di assenza non può essere vuoto.");
    	}
    	this.type = type; 
    }

    public EmployeeBean getEmployee() { return employee; }
    public void setEmployee(EmployeeBean employee) { 
    	this.employee = employee; 
    }
}