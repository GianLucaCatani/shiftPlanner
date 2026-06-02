package com.shiftplanner.dao.fs;

import com.shiftplanner.dao.PreferenceDAO;

import com.shiftplanner.dao.EmployeeDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.EmployeePreference;
import com.shiftplanner.model.Employee;
import com.shiftplanner.model.TimeSlot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PreferenceDAOFileSystem implements PreferenceDAO {

    private static final String FILE_PATH = "data/preferences.csv";
    
    private final EmployeeDAO employeeDAO;
    
    public PreferenceDAOFileSystem(EmployeeDAO employeeDAO) {
    	this.employeeDAO = employeeDAO;
    }

    @Override
    public List<EmployeePreference> getPreferencesByPeriod(LocalDate startDate, LocalDate endDate) throws DAOException {
        List<EmployeePreference> preferences = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                String[] data = line.split(",");
                if (data.length == 4) {
                    long prefId = Long.parseLong(data[0].trim());
                    long employeeId = Long.parseLong(data[1].trim());
                    LocalDate date = LocalDate.parse(data[2].trim());
                    TimeSlot slot = TimeSlot.valueOf(data[3].trim().toUpperCase());
                    
                    //Filtra solo le preferenze che cadono nel periodo richiesto
                    if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                    	// Anche qui ricreiamo il collegamento reale con il Dipendente
                    	Employee emp = employeeDAO.getEmployeeById(employeeId);
                        if (emp != null) {
                            preferences.add(new EmployeePreference(prefId, date, slot, emp));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new DAOException("Errore nella lettura del file " + FILE_PATH, e);
        }
        
        return preferences;
    }
}