package com.shiftplanner.view.cli;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.shiftplanner.bean.NotificationBean;
import com.shiftplanner.bean.ShiftBean;
import com.shiftplanner.controller.app.EmployeeDashboardController;
import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.exceptions.ShiftPlannerException;

//View CLI per la dashboard del dipendente.
//Mostra i turni assegnati e le notifiche dell'utente autenticato.
public class EmployeeDashboardView {

    private static final Logger LOGGER = Logger.getLogger(EmployeeDashboardView.class.getName());

    private final EmployeeDashboardController dashboardController;

    public EmployeeDashboardView(DAOFactory daoFactory) {
        this.dashboardController = new EmployeeDashboardController(daoFactory);
    }

    public void start(long employeeId) {
        LOGGER.info("\n========================================");
        LOGGER.info("     SHIFT PLANNER — DASHBOARD          ");
        LOGGER.info("========================================");

        showMyShifts(employeeId);
        showMyNotifications(employeeId);

        LOGGER.info("\n========================================");
        LOGGER.info("  Premi INVIO per fare logout...");
        LOGGER.info("========================================");
        // Il metodo termina: LoginView rileva il ritorno e ripresenta il login
    }

    private void showMyShifts(long employeeId) {
        LOGGER.info("\n--- I TUOI TURNI ---");
        try {
            List<ShiftBean> shifts = dashboardController.getMyShifts(employeeId);
            if (shifts.isEmpty()) {
                LOGGER.info("  Nessun turno assegnato al momento.");
                return;
            }
            LOGGER.info(() -> String.format("  %-12s %-8s %-8s%n", "DATA", "INIZIO", "FINE"));
            LOGGER.info("  --------------------------------");
            for (ShiftBean shift : shifts) {
                LOGGER.info(() -> String.format("  %-12s %-8s %-8s%n",
                        shift.getDate(),
                        shift.getStartTime(),
                        shift.getEndTime()));
            }
        } catch (ShiftPlannerException e) {
            LOGGER.log(Level.SEVERE, "Errore nel caricamento dei turni", e);
        }
    }

    private void showMyNotifications(long employeeId) {
        LOGGER.info("\n--- LE TUE NOTIFICHE ---");
        try {
            List<NotificationBean> notifications = dashboardController.getMyNotifications(employeeId);
            if (notifications.isEmpty()) {
                LOGGER.info("  Nessuna notifica.");
                return;
            }
            for (NotificationBean n : notifications) {
                LOGGER.info(() -> "  • " + n.getMessage());
            }
        } catch (ShiftPlannerException e) {
            LOGGER.log(Level.SEVERE, "Errore nel caricamento delle notifiche", e);
        }
    }
}
