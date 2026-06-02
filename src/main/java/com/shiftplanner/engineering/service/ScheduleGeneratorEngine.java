package com.shiftplanner.engineering.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shiftplanner.model.Absence;
import com.shiftplanner.model.Employee;
import com.shiftplanner.model.EmployeePreference;
import com.shiftplanner.model.Schedule;
import com.shiftplanner.model.Shift;
import com.shiftplanner.model.TimeSlot;

public class ScheduleGeneratorEngine {
	
	//Durata standard di un turno (es. 8 ore)
	private static final int SHIFT_DURATION_HOURS = 8;
	
	public void generateShifts(Schedule schedule, List<Employee> employees, List<Absence> absences, List<EmployeePreference> preferences) {
		
		// Mappa per tracciare le ore assegnate per dipendente PER SETTIMANA ISO.
		// Chiave esterna: employeeId — chiave interna: numero settimana ISO dell'anno.
		// Questo garantisce che il limite contractWeeklyHours venga rispettato settimana per settimana, anche su periodi multi-settimana.
		Map<Long, Map<Integer, Integer>> weeklyHoursMap = new HashMap<>();
		for (Employee emp : employees) {
			weeklyHoursMap.put(emp.getEmployeeId(), new HashMap<>());
		}
		
		// cyclicIndex locale — resettato a ogni chiamata
	    int cyclicIndex = 0;
		
		LocalDate currentDate = schedule.getPeriodStartDate();
		LocalDate endDate = schedule.getPeriodEndDate();
		
		//Ciclo su ogni giorno del calendario
		while (!currentDate.isAfter(endDate)) {
			
			//1. ELABORATE PREFERENCES (Gestione preferenze incomplete o specifiche)
			elaboratePreferences(schedule, currentDate, absences, preferences, weeklyHoursMap);
			
			//2. APPLY DEFAULT (Assegnazione di default per slot non coperti dalle preferenze)
			applyDefault(schedule, currentDate, employees, absences, weeklyHoursMap);
			
			//3. ASSIGN CYCLIC (Distribuzione equa e ciclica per gli slot rimanenti)
			cyclicIndex = assignCyclic(schedule, currentDate, employees, absences, weeklyHoursMap, cyclicIndex);
			
			// Passa al giorno successivo
			currentDate = currentDate.plusDays(1);
		}
	}
	
	// Restituisce il numero di settimana ISO dell'anno per una data
    private int isoWeekKey(LocalDate date) {
		return date.get(WeekFields.ISO.weekOfWeekBasedYear())
				+ date.get(WeekFields.ISO.weekBasedYear()) * 100;
	}
		
	// Restituisce le ore già assegnate al dipendente nella settimana della data indicata
	private int getWeeklyHours(Map<Long, Map<Integer, Integer>> weeklyHoursMap, long employeeId, LocalDate date) {
		return weeklyHoursMap.get(employeeId).getOrDefault(isoWeekKey(date), 0);
	}
		
	// Aggiorna le ore settimanali del dipendente per la settimana della data indicata
	private void addWeeklyHours(Map<Long, Map<Integer, Integer>> weeklyHoursMap, long employeeId, LocalDate date) {
		Map<Integer, Integer> empMap = weeklyHoursMap.get(employeeId);
		int key = isoWeekKey(date);
		empMap.put(key, empMap.getOrDefault(key, 0) + SHIFT_DURATION_HOURS);
	}
	
	/*Logica per esaminare le EmployeePreference.
    Se un dipendente ha chiesto quel giorno/slot, e canAssign() è true, si crea lo Shift.*/
	private void elaboratePreferences(Schedule schedule, LocalDate date, List<Absence> absences, List<EmployeePreference> preferences, Map<Long, Map<Integer, Integer>> weeklyHoursMap) {
		//Scorre tutti gli slot della giornata
		for (TimeSlot slot : TimeSlot.values()){
			if (isSlotFilled(schedule, date, slot)) continue;
			
			//Cerca una preferenza che combaci con il giorno e lo slot corrente
			for (EmployeePreference pref : preferences) {
				if (pref.getPreferredDate().equals(date) && pref.getTimeSlot() == slot) {
					Employee emp = pref.getEmployee();
					
					//Se può lavorare, gli assegniamo il turno
					if (canAssign(emp, date, absences, weeklyHoursMap)) {
						assignShift(schedule, date, slot, emp, weeklyHoursMap);
						break;
					}
				}
			}
		}
    }
	
	
    private void applyDefault(Schedule schedule, LocalDate date, List<Employee> employees, List<Absence> absences, Map<Long, Map<Integer, Integer>> weeklyHoursMap) {
        // Regola: sceglie chi ha lavorato meno ore nella settimana corrente
    	
    	//Ordina i dipendenti in base alle ore settimanali assegnate (crescente)
    	List<Employee> sortedEmployees = new ArrayList<>(employees);
    	sortedEmployees.sort(Comparator.comparingInt(emp -> getWeeklyHours(weeklyHoursMap, emp.getEmployeeId(), date)));
    	
    	for (TimeSlot slot : TimeSlot.values()) {
    		if (isSlotFilled(schedule, date, slot)) continue;
    		
    		for (Employee emp : sortedEmployees) {
    			if (canAssign(emp, date, absences, weeklyHoursMap)) {
    				assignShift(schedule, date, slot, emp, weeklyHoursMap);
    				
    				//riordiniamo la lista subito dopo l'assegnazione così il prossimo slot
    				//andrà a chi ora ha meno ore nella settimana corrente
    				sortedEmployees.sort(Comparator.comparingInt(e -> getWeeklyHours(weeklyHoursMap, e.getEmployeeId(), date)));
    				break;
    			}
    		}
    	}
    }
    
    //Logica per scorrere i dipendenti "a turno" e assegnare gli slot ancora vuoti.
    //Riceve e restituisce cyclicIndex per mantenerlo locale al chiamante.
    private int assignCyclic(Schedule schedule, LocalDate date, List<Employee> employees, List<Absence> absences, Map<Long, Map<Integer, Integer>> weeklyHoursMap, int cyclicIndex) {
        if (employees.isEmpty()) return cyclicIndex;
        
        for (TimeSlot slot : TimeSlot.values()) {
        	if (isSlotFilled(schedule, date, slot)) continue;
        	
        	//Prova tutti i dipendenti a rotazione partendo da cyclicIndex
        	int attempts = 0;
        	while (attempts < employees.size()) {
        		Employee emp = employees.get(cyclicIndex);
        		cyclicIndex = (cyclicIndex + 1) % employees.size();
        		
        		if (canAssign(emp, date, absences, weeklyHoursMap)) {
        			assignShift(schedule, date, slot, emp, weeklyHoursMap);
        			break;
        		}
        		attempts++;
        	}
        }
        return cyclicIndex;
    }
    
    /*Metodo Helper: verifica se un dipendente può fare un turno.
    Implementa i controlli obbligatori: Assenze e Ore Settimanali Contrattuali.*/
    private boolean canAssign(Employee emp, LocalDate date, List<Absence> absences, Map<Long, Map<Integer, Integer>> weeklyHoursMap) {
        
        // 1. Controllo Assenze
        for (Absence absence : absences) {
            if (absence.getEmployee().getEmployeeId() == emp.getEmployeeId() && absence.overlapsWith(date)) {
                return false; // Il dipendente è assente in questa data
            }
        }
        
        // 2. Controllo Ore Settimanali Contrattuali (per la settimana ISO della data)
        int currentWeekHours = getWeeklyHours(weeklyHoursMap, emp.getEmployeeId(), date);
        return (currentWeekHours + SHIFT_DURATION_HOURS) <= emp.getContractWeeklyHours();
    }
    
    //Controlla se uno slot in una certa data ha già un dipendente assegnato
    private boolean isSlotFilled(Schedule schedule, LocalDate date, TimeSlot slot) {
    	for (Shift shift : schedule.getShifts()) {
    		if (shift.getDate().equals(date) && getTimeSlotFromShift(shift) == slot) {
    			return true;
    		}
    	}
    	return false;
    }
    
    //Genera fisicamente l'oggetto Shift e aggiorna i dati
    private void assignShift(Schedule schedule, LocalDate date, TimeSlot slot, Employee emp, Map<Long, Map<Integer, Integer>> weeklyHoursMap) {
        LocalTime startTime = getStartTime(slot);
        LocalTime endTime = startTime.plusHours(SHIFT_DURATION_HOURS);
        
        Shift shift = new Shift(0, date, startTime, endTime, emp);
        schedule.addShift(shift);
        
        // Aggiorna le ore settimanali lavorate
        addWeeklyHours(weeklyHoursMap, emp.getEmployeeId(), date);
    }
	
    //Utility: converte la Enum TimeSlot in un orario vero e proprio
    private LocalTime getStartTime(TimeSlot slot) {
        switch (slot) {
            case MORNING: return LocalTime.of(6, 0); // Dalle 06:00
            case AFTERNOON: return LocalTime.of(14, 0); // Dalle 14:00
            case NIGHT: return LocalTime.of(22, 0); // Dalle 22:00
            default: return LocalTime.of(8, 0);
        }
    }
    
    //Utility: ricava la Enum TimeSlot dall'orario di uno Shift
    private TimeSlot getTimeSlotFromShift(Shift shift) {
        if (shift.getStartTime().equals(LocalTime.of(6, 0))) return TimeSlot.MORNING;
        if (shift.getStartTime().equals(LocalTime.of(14, 0))) return TimeSlot.AFTERNOON;
        return TimeSlot.NIGHT;
    }
}