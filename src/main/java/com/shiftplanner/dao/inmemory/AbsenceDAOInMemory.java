package com.shiftplanner.dao.inmemory;

import com.shiftplanner.dao.AbsenceDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Absence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AbsenceDAOInMemory implements AbsenceDAO {

    private static final List<Absence> DB = new ArrayList<>();

    @Override
    public List<Absence> getAbsencesByPeriod(LocalDate startDate, LocalDate endDate) throws DAOException {
        // Usa gli stream di Java per filtrare solo le assenze che ricadono nel periodo
        return DB.stream()
                .filter(a -> !(a.getEndDate().isBefore(startDate) || a.getStartDate().isAfter(endDate)))
                .toList();
    }
}