package com.shiftplanner.engineering;

import com.shiftplanner.engineering.service.ScheduleGeneratorEngine;
import com.shiftplanner.model.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/*Test Logica di Business dell'Engine.
Verifica che per UN giorno con dipendenti disponibili,
vengano generati esattamente 3 turni (mattina, pomeriggio, notte).*/
class ScheduleGeneratorEngineTest {

    @Test
    void generateShifts_withEmployees_creates3Shift() {
        // GIVEN: un calendario di 1 giorno con 3 dipendenti disponibili
        LocalDate giorno = LocalDate.of(2026, 6, 1);
        Schedule schedule = new Schedule(1L, giorno, giorno);

        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "Mario Rossi",   EmployeeRole.EMPLOYEE, 40));
        employees.add(new Employee(2, "Anna Bianchi",  EmployeeRole.EMPLOYEE, 40));
        employees.add(new Employee(3, "Luca Verdi",    EmployeeRole.EMPLOYEE, 40));

        List<com.shiftplanner.model.Absence>           absences    = new ArrayList<>();
        List<com.shiftplanner.model.EmployeePreference> preferences = new ArrayList<>();

        // WHEN: eseguiamo l'algoritmo
        ScheduleGeneratorEngine engine = new ScheduleGeneratorEngine();
        engine.generateShifts(schedule, employees, absences, preferences);

        // THEN: il calendario deve contenere esattamente 3 turni per quel giorno
        assertEquals(3, schedule.getShifts().size(),
                "Per un giorno con 3 dipendenti disponibili devono essere generati 3 turni");
    }

    @Test
    void generateShifts_noEmployees_createNoShifts() {
        // GIVEN: lista dipendenti vuota
        LocalDate giorno = LocalDate.of(2026, 6, 1);
        Schedule schedule = new Schedule(1L, giorno, giorno);
        List<Employee> employees = new ArrayList<>();

        // WHEN
        ScheduleGeneratorEngine engine = new ScheduleGeneratorEngine();
        engine.generateShifts(schedule, employees, new ArrayList<>(), new ArrayList<>());

        // THEN: zero turni generati
        assertEquals(0, schedule.getShifts().size(),
                "Senza dipendenti non devono essere generati turni");
    }
}