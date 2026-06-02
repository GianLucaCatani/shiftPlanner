package com.shiftplanner.dao.fs;

import com.shiftplanner.dao.UserDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.EmployeeRole;
import com.shiftplanner.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class UserDAOFileSystem implements UserDAO {

    private static final String FILE_PATH = "data/users.csv";

    @Override
    public User getUserByCredentials(String username, String password) throws DAOException {
    	
        List<User> users = loadAll();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void saveUser(User user) throws DAOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            // formato: id,username,password,employeeId,role
            String line = user.getUserId() + "," +
                          user.getUsername() + "," +
                          user.getPassword() + "," +
                          user.getEmployeeId() + "," +
                          user.getRole().name();
            pw.println(line);
        } catch (IOException e) {
            throw new DAOException("Errore durante il salvataggio dell'utente nel file " + FILE_PATH, e);
        }
    }

    private List<User> loadAll() throws DAOException {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) { isFirstLine = false; continue; }
                String[] data = line.split(",");
                if (data.length == 5) {
                    long id         = Long.parseLong(data[0].trim());
                    String uname    = data[1].trim();
                    String pwd      = data[2].trim();
                    long employeeId = Long.parseLong(data[3].trim());
                    EmployeeRole role = EmployeeRole.valueOf(data[4].trim().toUpperCase());
                    users.add(new User(id, uname, pwd, employeeId, role));
                }
            }
        } catch (IOException e) {
            throw new DAOException("Errore nella lettura del file " + FILE_PATH, e);
        }
        return users;
    }
}
