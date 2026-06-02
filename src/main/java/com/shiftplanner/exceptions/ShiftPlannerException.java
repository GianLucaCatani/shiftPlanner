package com.shiftplanner.exceptions;

public class ShiftPlannerException extends Exception {

    private static final long serialVersionUID = 1L;

	public ShiftPlannerException(String message) {
        super(message);
    }

    public ShiftPlannerException(String message, Throwable cause) {
        super(message, cause);
    }
}
