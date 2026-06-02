package com.shiftplanner.dao;

import com.shiftplanner.model.Absence;
import java.time.LocalDate;
import java.util.List;
import com.shiftplanner.exceptions.dao.DAOException;

public interface AbsenceDAO {
	//Recupera solo le assenze che si sovrappongono al periodo indicato.
	List<Absence> getAbsencesByPeriod(LocalDate startDate, LocalDate endDate) throws DAOException;
}
