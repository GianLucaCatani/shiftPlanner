package com.shiftplanner.exceptions;

public class ConstraintViolationException extends ShiftPlannerException {

    private static final long serialVersionUID = 1L;

	public ConstraintViolationException(String message) {
        super(message);
    }

    public ConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
