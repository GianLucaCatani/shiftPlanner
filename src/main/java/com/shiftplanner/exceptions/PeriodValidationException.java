package com.shiftplanner.exceptions;

public class PeriodValidationException extends ShiftPlannerException {

    private static final long serialVersionUID = 1L;

	public PeriodValidationException(String message) {
        super(message);
    }

    public PeriodValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
