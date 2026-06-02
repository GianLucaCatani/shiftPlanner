package com.shiftplanner.controller.app;

import com.shiftplanner.bean.LoginBean;
import com.shiftplanner.bean.UserBean;
import com.shiftplanner.dao.UserDAO;
import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.exceptions.ShiftPlannerException;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.User;

import java.util.logging.Level;
import java.util.logging.Logger;


//Controller applicativo per il caso d'uso Login.
//Riceve le credenziali dal layer GUI, le verifica tramite UserDAO e restituisce un UserBean con il ruolo dell'utente autenticato.
public class LoginController {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    private final DAOFactory daoFactory;

    public LoginController(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /*Verifica le credenziali e restituisce il profilo dell'utente autenticato.
    loginBean -> bean con username e password inseriti dall'utente
    return UserBean con userId, employeeId e role se le credenziali sono corrette
    */
    public UserBean login(LoginBean loginBean) throws ShiftPlannerException {
        try {
            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.getUserByCredentials(
                    loginBean.getUsername(), loginBean.getPassword());

            if (user == null) {
                throw new ShiftPlannerException("Credenziali non valide. Riprova.");
            }

            // Costruisce il bean di risposta
            UserBean userBean = new UserBean();
            userBean.setUserId(user.getUserId());
            userBean.setUsername(user.getUsername());
            userBean.setEmployeeId(user.getEmployeeId());
            userBean.setRole(user.getRole().name());
            return userBean;

        } catch (DAOException e) {
            LOGGER.log(Level.WARNING, "Errore DAO durante il login", e);
            throw new ShiftPlannerException("Errore di sistema durante il login. Riprovare.", e);
        }
    }
}
