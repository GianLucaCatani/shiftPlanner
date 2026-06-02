package com.shiftplanner.bean;

//DTO che rappresenta l'utente autenticato.
//Restituito da LoginController dopo un login riuscito.
public class UserBean {

    private long userId;
    private String username;
    private long employeeId;
    private String role;   // "COORDINATOR" o "EMPLOYEE"

    public long getUserId() { return userId; }
    public void setUserId(long userId) { 
    	this.userId = userId; 
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { 
    	this.username = username; 
    }

    public long getEmployeeId() { return employeeId; }
    public void setEmployeeId(long employeeId) { 
    	this.employeeId = employeeId; 
    }

    public String getRole() { return role; }
    public void setRole(String role) { 
    	this.role = role; 
    }
}
