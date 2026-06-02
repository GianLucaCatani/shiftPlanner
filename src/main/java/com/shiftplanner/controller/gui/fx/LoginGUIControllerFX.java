package com.shiftplanner.controller.gui.fx;

import com.shiftplanner.bean.LoginBean;
import com.shiftplanner.bean.UserBean;
import com.shiftplanner.controller.app.LoginController;
import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.engineering.service.NotificationManager;
import com.shiftplanner.exceptions.ConstraintViolationException;
import com.shiftplanner.exceptions.ShiftPlannerException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

//Controller GUI JavaFX per il login.
//Dopo l'autenticazione instrada l'utente alla view corretta.
public class LoginGUIControllerFX {
	
	private static final String ROLE_COORDINATOR = "COORDINATOR";
	private static final String FXML_COORDINATOR  = "/com/shiftplanner/view/fx/GenerateScheduleView.fxml";
    private static final String FXML_EMPLOYEE     = "/com/shiftplanner/view/fx/EmployeeDashboardView.fxml";

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private LoginController loginController;
    private DAOFactory daoFactory;
    private NotificationManager notificationManager;

    public void initDependencies(DAOFactory daoFactory, NotificationManager notificationManager) {
        this.daoFactory = daoFactory;
        this.notificationManager = notificationManager;
        this.loginController = new LoginController(daoFactory);
    }

    @FXML
    private void onLoginClicked() {
        errorLabel.setVisible(false);

        String username = usernameField.getText();
        String password = passwordField.getText();

        LoginBean bean = new LoginBean();
        try {
            bean.setUsername(username);
            bean.setPassword(password);
        } catch (ConstraintViolationException e) {
            showError(e.getMessage());
            return;
        }

        try {
            UserBean user = loginController.login(bean);
            openDashboard(user);
        } catch (ShiftPlannerException e) {
            showError(e.getMessage());
        }
    }

    private void openDashboard(UserBean user) {
        try {
            String fxmlPath;
            if (ROLE_COORDINATOR.equals(user.getRole())) {
                fxmlPath = FXML_COORDINATOR;
            } else {
                fxmlPath = FXML_EMPLOYEE;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            if (ROLE_COORDINATOR.equals(user.getRole())) {
                GenerateScheduleGUIControllerFX ctrl = loader.getController();
                ctrl.initDependencies(daoFactory, notificationManager);
            } else {
                EmployeeDashboardGUIControllerFX ctrl = loader.getController();
                ctrl.initDependencies(daoFactory, notificationManager, user);
            }

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("ShiftPlanner — " +
                    (ROLE_COORDINATOR.equals(user.getRole()) ? "Generazione Turni" : "Dashboard Dipendente"));
            stage.setScene(new Scene(root));
            stage.setResizable(false);

        } catch (Exception e) {
            showError("Errore nell'apertura della dashboard: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}