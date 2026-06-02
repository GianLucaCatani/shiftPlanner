package com.shiftplanner.dao;

import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.User;

public interface UserDAO {

    //Cerca un utente per username e password (login).
    //Restituisce l'oggetto User se le credenziali sono corrette, null altrimenti.
    User getUserByCredentials(String username, String password) throws DAOException;

    //Salva un nuovo utente nel sistema.
    void saveUser(User user) throws DAOException;
}
