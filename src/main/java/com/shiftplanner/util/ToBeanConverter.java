package com.shiftplanner.util;

import com.shiftplanner.bean.EmployeeBean;

import com.shiftplanner.bean.ScheduleBean;
import com.shiftplanner.bean.ShiftBean;
import com.shiftplanner.exceptions.ConstraintViolationException;
import com.shiftplanner.exceptions.PeriodValidationException;
import com.shiftplanner.exceptions.ShiftPlannerException;
import com.shiftplanner.model.Employee;
import com.shiftplanner.model.Schedule;
import com.shiftplanner.model.Shift;

import java.util.ArrayList;
import java.util.List;

//Classe utility per la conversione da oggetti Model a oggetti Bean (DTO)
public final class ToBeanConverter {

    private ToBeanConverter() {
        throw new IllegalStateException("Classe utility — non istanziare.");
    }

    public static ScheduleBean fromSchedule(Schedule schedule) throws ShiftPlannerException {
        ScheduleBean bean = new ScheduleBean();
        bean.setScheduleId(schedule.getScheduleId());

        try {
            bean.setStartDate(schedule.getPeriodStartDate());
            bean.setEndDate(schedule.getPeriodEndDate());
        } catch (PeriodValidationException e) {
            throw new ShiftPlannerException("Errore nella conversione delle date dello schedule.", e);
        }

        bean.setStatus(schedule.getStatus().name());

        // Converte ogni Shift in ShiftBean e li aggiunge alla lista
        List<ShiftBean> shiftBeans = new ArrayList<>();
        for (Shift shift : schedule.getShifts()) {
            shiftBeans.add(fromShift(shift));
        }
        bean.setShifts(shiftBeans);

        return bean;
    }

    //Viene passato come parametro il turno da convertire(shift)
    //ritorna il ShiftBean popolato con data,orari e dipendente assegnato
    public static ShiftBean fromShift(Shift shift) throws ShiftPlannerException {
        ShiftBean bean = new ShiftBean();
        bean.setShiftId(shift.getShiftId());
        bean.setDate(shift.getDate());
        bean.setStartTime(shift.getStartTime());
        bean.setEndTime(shift.getEndTime());

        // Converte il dipendente assegnato al turno
        if (shift.getEmployee() != null) {
            bean.setEmployee(fromEmployee(shift.getEmployee()));
        }

        return bean;
    }

    //Converte un oggetto Employee in un EmployeeBEan
    //ritorna l'EmployeeBEan popolato
    public static EmployeeBean fromEmployee(Employee employee) throws ShiftPlannerException {
        EmployeeBean bean = new EmployeeBean();
        bean.setEmployeeId(employee.getEmployeeId());

        try {
            bean.setFullName(employee.getFullName());
            bean.setContractWeeklyHours(employee.getContractWeeklyHours());
        } catch (ConstraintViolationException e) {
            throw new ShiftPlannerException("Errore nella conversione dei dati del dipendente.", e);
        }

        bean.setRole(employee.getRole().name());
        return bean;
    }
}
