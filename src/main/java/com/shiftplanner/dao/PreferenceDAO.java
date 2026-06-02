package com.shiftplanner.dao;

import java.time.LocalDate;
import java.util.List;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.EmployeePreference;

public interface PreferenceDAO {
	//Recupera solo le preferenze che ricadono nel periodo indicato.
	List<EmployeePreference> getPreferencesByPeriod(LocalDate startDate, LocalDate endDate) throws DAOException;
}
