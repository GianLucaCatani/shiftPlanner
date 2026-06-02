package com.shiftplanner.view.cli;

import java.util.Scanner;

import com.shiftplanner.bean.UserBean;
import com.shiftplanner.controller.gui.cli.LoginGUIControllerCLI;
import com.shiftplanner.exceptions.ShiftPlannerException;

//Chiede username e password, poi instrada l'utente alla view corretta in base al ruolo
public class LoginView {

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
        System.out.println("========================================");
        System.out.println("       SHIFT PLANNER — LOGIN            ");
        System.out.println("========================================");

        while (true) {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            try {
                UserBean user = loginController.login(username, password);
                System.out.println("\nBenvenuto, " + user.getUsername() + "! [" + user.getRole() + "]");

                //instrada alla view corretta
                if ("COORDINATOR".equals(user.getRole())) {
                    coordinatorView.start();
                } else {
                    employeeView.start(user.getEmployeeId());
                }
                // Dopo il logout la view ritorna qui: il loop ripresenta il login
                System.out.println("\n--- Sessione terminata. Effettua di nuovo il login. ---\n");

            } catch (ShiftPlannerException e) {
                System.out.println("\nERRORE: " + e.getMessage());
                System.out.print("Vuoi riprovare? (s/n): ");
                String retry = scanner.nextLine().trim().toLowerCase();
                if (!retry.equals("s")) {
                    System.out.println("Arrivederci.");
                    break;
                }
                System.out.println();
            }
        }
    }
}
