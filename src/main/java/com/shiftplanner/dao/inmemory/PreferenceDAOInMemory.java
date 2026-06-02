package com.shiftplanner.dao.inmemory;

import com.shiftplanner.dao.PreferenceDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.EmployeePreference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PreferenceDAOInMemory implements PreferenceDAO {

    private static final List<EmployeePreference> DB = new ArrayList<>();

    @Override
    public List<EmployeePreference> getPreferencesByPeriod(LocalDate startDate, LocalDate endDate) throws DAOException {
    	// Filtra le preferenze la cui data specifica cade nel periodo richiesto (estremi inclusi)
    	return DB.stream()
                .filter(p -> {
                    LocalDate date = p.getPreferredDate();
                    return !date.isBefore(startDate) && !date.isAfter(endDate);
                })
                .collect(Collectors.toList());
    }
    
    //Aggiunge una preferenza in memoria.
    public static void addPreference(EmployeePreference preference) {
        DB.add(preference);
    }

    //Svuota il database. Utile nei test per garantire l'isolamento.
    public static void clear() {
        DB.clear();
    }
}