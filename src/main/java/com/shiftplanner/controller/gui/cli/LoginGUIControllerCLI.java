package com.shiftplanner.controller.gui.cli;

import com.shiftplanner.bean.LoginBean;
import com.shiftplanner.bean.UserBean;
import com.shiftplanner.controller.app.LoginController;
import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.exceptions.ConstraintViolationException;
import com.shiftplanner.exceptions.ShiftPlannerException;


//Controller GUI CLI per il caso d'uso Login.
//Riceve username e password come stringhe grezze dalla view CLI,le valida nel bean e delega al LoginController applicativo.
public class LoginGUIControllerCLI {

    private final LoginController loginController;

    public LoginGUIControllerCLI(DAOFactory daoFactory) {
        this.loginController = new LoginController(daoFactory);
    }

    //Esegue il login con le credenziali fornite.
    public UserBean login(String username, String password) throws ShiftPlannerException {
        LoginBean bean = new LoginBean();
        try {
            bean.setUsername(username);
            bean.setPassword(password);
        } catch (ConstraintViolationException e) {
            throw new ShiftPlannerException(e.getMessage());
        }
        return loginController.login(bean);
    }
}
