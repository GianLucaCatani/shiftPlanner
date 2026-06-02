package com.shiftplanner.view.cli;

import java.util.List;

import com.shiftplanner.bean.NotificationBean;
import com.shiftplanner.bean.ShiftBean;
import com.shiftplanner.controller.app.EmployeeDashboardController;
import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.exceptions.ShiftPlannerException;

//View CLI per la dashboard del dipendente.
//Mostra i turni assegnati e le notifiche dell'utente autenticato.
public class EmployeeDashboardView {

    private final EmployeeDashboardController dashboardController;

    public EmployeeDashboardView(DAOFactory daoFactory) {
        this.dashboardController = new EmployeeDashboardController(daoFactory);
    }

    public void start(long employeeId) {
        System.out.println("\n========================================");
        System.out.println("     SHIFT PLANNER — DASHBOARD          ");
        System.out.println("========================================");

        showMyShifts(employeeId);
        showMyNotifications(employeeId);

        System.out.println("\n========================================");
        System.out.println("  Premi INVIO per fare logout...");
        System.out.println("========================================");
    }

    private void showMyShifts(long employeeId) {
        System.out.println("\n--- I TUOI TURNI ---");
        try {
            List<ShiftBean> shifts = dashboardController.getMyShifts(employeeId);
            if (shifts.isEmpty()) {
                System.out.println("  Nessun turno assegnato al momento.");
                return;
            }
            System.out.printf("  %-12s %-8s %-8s%n", "DATA", "INIZIO", "FINE");
            System.out.println("  --------------------------------");
            for (ShiftBean shift : shifts) {
                System.out.printf("  %-12s %-8s %-8s%n",
                        shift.getDate(),
                        shift.getStartTime(),
                        shift.getEndTime());
            }
        } catch (ShiftPlannerException e) {
            System.out.println("  ERRORE: " + e.getMessage());
        }
    }

    private void showMyNotifications(long employeeId) {
        System.out.println("\n--- LE TUE NOTIFICHE ---");
        try {
            List<NotificationBean> notifications = dashboardController.getMyNotifications(employeeId);
            if (notifications.isEmpty()) {
                System.out.println("  Nessuna notifica.");
                return;
            }
            for (NotificationBean n : notifications) {
                System.out.println("  • " + n.getMessage());
            }
        } catch (ShiftPlannerException e) {
            System.out.println("  ERRORE: " + e.getMessage());
        }
    }
}
