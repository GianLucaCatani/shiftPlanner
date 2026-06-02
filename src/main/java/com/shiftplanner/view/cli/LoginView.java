package com.shiftplanner.view.cli;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.shiftplanner.bean.UserBean;
import com.shiftplanner.controller.gui.cli.LoginGUIControllerCLI;
import com.shiftplanner.exceptions.ShiftPlannerException;

//View CLI per il login.
//Chiede username e password, poi instrada l'utente alla view corretta in base al ruolo
public class LoginView {

    private static final Logger LOGGER = Logger.getLogger(LoginView.class.getName());
    private static final String ROLE_COORDINATOR = "COORDINATOR";

    private final LoginGUIControllerCLI loginController;
    private final Scanner scanner;

    private final GenerateScheduleView coordinatorView;
    private final EmployeeDashboardView employeeView;

    public LoginView(LoginGUIControllerCLI loginController,
                     GenerateScheduleView coordinatorView,
                     EmployeeDashboardView employeeView,
                     Scanner scanner) {
        this.loginController  = loginController;
        this.coordinatorView  = coordinatorView;
        this.employeeView     = employeeView;
        this.scanner          = scanner;
    }

    public void start() {
        LOGGER.info("========================================");
        LOGGER.info("       SHIFT PLANNER — LOGIN            ");
        LOGGER.info("========================================");

        while (true) {
            LOGGER.info("Username: ");
            String username = scanner.nextLine().trim();

            LOGGER.info("Password: ");
            String password = scanner.nextLine();

            try {
                UserBean user = loginController.login(username, password);
                LOGGER.info("\nBenvenuto, " + user.getUsername() + "! [" + user.getRole() + "]");

                //instrada alla view corretta
                if (ROLE_COORDINATOR.equals(user.getRole())) {
                    coordinatorView.start();
                } else {
                    employeeView.start(user.getEmployeeId());
                }
                // Dopo il logout la view ritorna qui: il loop ripresenta il login
                LOGGER.info("\n--- Sessione terminata. Effettua di nuovo il login. ---\n");

            } catch (ShiftPlannerException e) {
                LOGGER.log(Level.WARNING, "Errore di login", e);
                LOGGER.info("Vuoi riprovare? (s/n): ");
                String retry = scanner.nextLine().trim();
                if (!"s".equalsIgnoreCase(retry)) {
                    LOGGER.info("Arrivederci.");
                    break;
                }
                LOGGER.info("");
            }
        }
    }
}
