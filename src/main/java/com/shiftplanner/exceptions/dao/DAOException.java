package com.shiftplanner.exceptions.dao;

import com.shiftplanner.exceptions.ShiftPlannerException;

public class DAOException extends ShiftPlannerException {

    private static final long serialVersionUID = 1L;

	public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
