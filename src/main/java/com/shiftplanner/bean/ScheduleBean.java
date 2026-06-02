package com.shiftplanner.bean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.shiftplanner.exceptions.PeriodValidationException;

public class ScheduleBean {
	private long scheduleId;
	private LocalDate startDate;
	private LocalDate endDate;
	private String status;
	
	private List<ShiftBean> shifts; //Lista di Bean
	
	public ScheduleBean() {
		this.shifts = new ArrayList<>();
	}
	
	public ScheduleBean(LocalDate startDate, LocalDate endDate) throws PeriodValidationException {
		if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
			throw new PeriodValidationException("La data di inizio non può essere successiva alla data di fine.");
		}
		this.startDate = startDate;
		this.endDate = endDate;
		this.shifts = new ArrayList<>();
	}
	
	//Getter e Setter
	public long getScheduleId() { return scheduleId; }
    public void setScheduleId(long scheduleId) { 
    	this.scheduleId = scheduleId; 
    }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) throws PeriodValidationException { 
    	if (startDate != null && this.endDate != null && startDate.isAfter(this.endDate)) {
    		throw new PeriodValidationException("La data di inizio non può essere successiva alla data di fine.");
    	}
    	this.startDate = startDate; 
    }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) throws PeriodValidationException {
        if (this.startDate != null && endDate != null && this.startDate.isAfter(endDate)) {
            throw new PeriodValidationException("La data di fine non può precedere la data di inizio.");
        }
        this.endDate = endDate;
    }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
    	this.status = status; 
    }
    
    public List<ShiftBean> getShifts() { 
    	return Collections.unmodifiableList(shifts); //Lista non modificabile
    }
    
    public void setShifts(List<ShiftBean> shifts) { 
    	this.shifts = new ArrayList<>(shifts); //Copia difensiva
    }
}
