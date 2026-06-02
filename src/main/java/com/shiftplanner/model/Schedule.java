package com.shiftplanner.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.shiftplanner.engineering.observer.Subject;

public class Schedule extends Subject {
	private long scheduleId;
    private LocalDate periodStartDate;
    private LocalDate periodEndDate;
    private ScheduleStatus status;
    
    private final List<Shift> shifts = new ArrayList<>();
    
    public Schedule(long scheduleId, LocalDate periodStartDate, LocalDate periodEndDate) {
    	this.scheduleId = scheduleId;
    	this.periodStartDate = periodStartDate;
    	this.periodEndDate = periodEndDate;
    	this.status = ScheduleStatus.DRAFT;
    }
    
    public long getScheduleId() { return scheduleId; }
    public void setScheduleId(long scheduleId) { 
    	this.scheduleId = scheduleId; 
    }
    
    public LocalDate getPeriodStartDate() { return periodStartDate; }
    public void setPeriodStartDate(LocalDate periodStartDate) { 
    	this.periodStartDate = periodStartDate; 
    }
    
    public LocalDate getPeriodEndDate() { return periodEndDate; }
    public void setPeriodEndDate(LocalDate periodEndDate) { 
    	this.periodEndDate = periodEndDate; 
    }
    
    public ScheduleStatus getStatus() { return status; }
    public void setStatus(ScheduleStatus status) { 
    	this.status = status; 
    }
    
    public List<Shift> getShifts() { 
    	return Collections.unmodifiableList(shifts); 
    }
    
    public void addShift(Shift shift) {
        this.shifts.add(shift);
    }
    public void removeShift(Shift shift) {
        this.shifts.remove(shift);
    }
    
    //Sostituisce il dipendente assegnato al turno all'indice specificato.
    //newEmployee è il nuovo dipendente da assegnare
    public void reassignShift(int shiftIndex, Employee newEmployee) {
    	if (this.status == ScheduleStatus.PUBLISHED) {
            throw new IllegalStateException("Non è possibile modificare uno schedule già pubblicato.");
        }
        if (shiftIndex < 0 || shiftIndex >= shifts.size()) {
            throw new IndexOutOfBoundsException("Indice turno non valido: " + shiftIndex);
        }
        shifts.get(shiftIndex).setEmployee(newEmployee);
    }
    
    public void publish() {
    	if (this.status == ScheduleStatus.PUBLISHED) {
    		throw new IllegalStateException("Lo schedule è già stato pubblicato.");
    	}
        this.status = ScheduleStatus.PUBLISHED;
        notifyObservers();
    }
}
