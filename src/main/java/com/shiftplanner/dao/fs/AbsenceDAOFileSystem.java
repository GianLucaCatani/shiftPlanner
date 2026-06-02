package com.shiftplanner.dao.fs;

import com.shiftplanner.dao.AbsenceDAO;

import com.shiftplanner.dao.EmployeeDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Absence;
import com.shiftplanner.model.AbsenceType;
import com.shiftplanner.model.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AbsenceDAOFileSystem implements AbsenceDAO {

    private static final String FILE_PATH = "data/absences.csv";
    
    private final EmployeeDAO employeeDAO;
    
    public AbsenceDAOFileSystem(EmployeeDAO employeeDAO) {
    	this.employeeDAO = employeeDAO;
    }

    @Override
    public List<Absence> getAbsencesByPeriod(LocalDate startDate, LocalDate endDate) throws DAOException {
        List<Absence> absences = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                String[] data = line.split(",");
                if (data.length == 5) {
                    long absenceId = Long.parseLong(data[0].trim());
                    long employeeId = Long.parseLong(data[1].trim());
                    LocalDate start = LocalDate.parse(data[2].trim());
                    LocalDate end = LocalDate.parse(data[3].trim());
                    String typeString = data[4].trim();
                    AbsenceType type = AbsenceType.valueOf(typeString.toUpperCase());
                    
                    // Se l'assenza si sovrappone al periodo richiesto
                    if (!(end.isBefore(startDate) || start.isAfter(endDate))) {
                        
                        // Recuperiamo il vero dipendente tramite il suo ID
                        Employee emp = employeeDAO.getEmployeeById(employeeId);
                        
                        if (emp != null) {
                            absences.add(new Absence(absenceId, start, end, type, emp));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new DAOException("Errore nella lettura del file " + FILE_PATH, e);
        }
        
        return absences;
    }
}