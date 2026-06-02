package com.shiftplanner.model;

/*Entità che rappresenta un account utente nel sistema.
 Separata da Employee: un User è l'account di accesso,
 Employee è il profilo lavorativo. Sono collegati tramite employeeId.
*/
public class User {

    private long userId;
    private String username;
    private String password;       
    private long employeeId;       
    private EmployeeRole role;    

    public User(long userId, String username, String password, long employeeId, EmployeeRole role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.employeeId = employeeId;
        this.role = role;
    }

    public long getUserId() { return userId; }
    public void setUserId(long userId){ 
    	this.userId = userId; 
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { 
    	this.username = username; 
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { 
    	this.password = password; 
    }

    public long getEmployeeId() { return employeeId; }
    public void setEmployeeId(long id) { 
    	this.employeeId = id; 
    }

    public EmployeeRole getRole() { return role; }
    public void setRole(EmployeeRole role) { 
    	this.role = role; 
    }
}