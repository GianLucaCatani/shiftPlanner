package com.shiftplanner.bean;

import java.time.LocalDateTime;

public class NotificationBean {
    private long notificationId;
    private String message;
    private long employeeId;
    private LocalDateTime createAt;

    public NotificationBean() {}

    public long getNotificationId() { return notificationId; }
    public void setNotificationId(long notificationId) { 
    	this.notificationId = notificationId; 
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { 
    	this.message = message; 
    }

    public long getEmployeeId() { return employeeId; }
    public void setEmployeeId(long employeeId) { 
    	this.employeeId = employeeId; 
    }

    public LocalDateTime getCreateAt() { return createAt; }
    public void setCreateAt(LocalDateTime createAt) { 
    	this.createAt = createAt; 
    }
}