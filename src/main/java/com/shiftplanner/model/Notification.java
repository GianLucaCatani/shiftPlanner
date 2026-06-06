package com.shiftplanner.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Notification {
	private long notificationId;
    private String message;
    private long employeeId;
    private LocalDateTime createAt;
    
    public Notification(long notificationId, long employeeId, String message) {
        this.notificationId = notificationId;
        this.employeeId = employeeId;
        this.message = message;
        this.createAt = LocalDateTime.now(ZoneId.systemDefault()); //ZoneId.systemDefault() dice esplicitamente "usa il fuso orario del sistema dove gira l'app"
    }
    
    public long getNotificationId() { return notificationId; }
    public void setNotificationId(long notificationId) { 
    	this.notificationId = notificationId; 
    }
    
    public long getEmployeeId() { return employeeId; }
    public void setEmployeeId(long employeeId) { 
    	this.employeeId = employeeId; 
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { 
    	this.message = message; 
    }
    
    public LocalDateTime getCreateAt() { return createAt; }
}
