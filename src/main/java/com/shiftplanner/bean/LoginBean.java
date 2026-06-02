package com.shiftplanner.bean;

import com.shiftplanner.exceptions.ConstraintViolationException;

//DTO per il trasporto delle credenziali di login
//tra la View e il LoginController.
public class LoginBean {

    private String username;
    private String password;

    public LoginBean() {}

    public String getUsername() { return username; }
    public void setUsername(String username) throws ConstraintViolationException {
        if (username == null || username.trim().isEmpty()) {
            throw new ConstraintViolationException("Lo username non può essere vuoto.");
        }
        this.username = username.trim();
    }

    public String getPassword() { return password; }
    public void setPassword(String password) throws ConstraintViolationException {
        if (password == null || password.isEmpty()) {
            throw new ConstraintViolationException("La password non può essere vuota.");
        }
        this.password = password;
    }
}
