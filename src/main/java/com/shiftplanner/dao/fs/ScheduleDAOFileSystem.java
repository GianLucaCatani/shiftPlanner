package com.shiftplanner.dao.fs;

import com.shiftplanner.dao.ScheduleDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Schedule;
import com.shiftplanner.model.Shift;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ScheduleDAOFileSystem implements ScheduleDAO {

    private static final String SCHEDULES_FILE = "data/schedules.csv";
    private static final String SHIFTS_FILE = "data/shifts.csv";

    @Override
    public void saveSchedule(Schedule schedule) throws DAOException {
        
        // 1. Salviamo le informazioni generali del calendario
        try (PrintWriter pw = new PrintWriter(new FileWriter(SCHEDULES_FILE, true))) {
            String scheduleLine = schedule.getScheduleId() + "," +
                                  schedule.getPeriodStartDate() + "," +
                                  schedule.getPeriodEndDate();
            pw.println(scheduleLine);
        } catch (IOException e) {
            throw new DAOException("Errore salvataggio schedule nel file " + SCHEDULES_FILE, e);
        }
        
        // 2. Salviamo tutti i singoli turni generati dall'algoritmo
        try (PrintWriter pw = new PrintWriter(new FileWriter(SHIFTS_FILE, true))) {
            for (Shift shift : schedule.getShifts()) {
                
                // Formato: idTurno, idSchedule, data, oraInizio, oraFine, idDipendente
                String shiftLine = shift.getShiftId() + "," +
                                   schedule.getScheduleId() + "," +
                                   shift.getDate() + "," +
                                   shift.getStartTime() + "," +
                                   shift.getEndTime() + "," +
                                   shift.getEmployee().getEmployeeId();
                                   
                pw.println(shiftLine);
            }
        } catch (IOException e) {
            throw new DAOException("Errore salvataggio turni nel file " + SHIFTS_FILE, e);
        }
    }

    @Override
    public Schedule getScheduleById(long id) throws DAOException {
        // Il nostro caso d'uso si ferma alla generazione e al salvataggio.
        // Un'implementazione reale di questo metodo dovrebbe leggere da entrambi i file CSV
        // e ricostruire l'oggetto. Per ora non è bloccante, e non è necessario pe ril caso d'uso principale
        return null;
    }
    
    @Override
    public List<Shift> getShiftsByEmployeeId(long employeeId) throws DAOException {
        // Lettura non implementata per il FS 
        return new java.util.ArrayList<>();
    }
}