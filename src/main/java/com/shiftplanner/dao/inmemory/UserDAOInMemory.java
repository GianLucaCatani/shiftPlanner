package com.shiftplanner.dao.inmemory;

import com.shiftplanner.dao.UserDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAOInMemory implements UserDAO {

    // Database in RAM condiviso tra tutte le istanze 
    private static final List<User> DB = new ArrayList<>();

    @Override
    public User getUserByCredentials(String username, String password) throws DAOException {
        for (User user : DB) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void saveUser(User user) throws DAOException {
        DB.add(user);
    }
}