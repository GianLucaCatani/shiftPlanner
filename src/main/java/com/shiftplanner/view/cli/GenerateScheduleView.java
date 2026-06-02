 package com.shiftplanner.view.cli;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.shiftplanner.bean.ScheduleBean;
import com.shiftplanner.bean.ShiftBean;
import com.shiftplanner.controller.gui.cli.GenerateScheduleGUIControllerCLI;
import com.shiftplanner.exceptions.ShiftPlannerException;

public class GenerateScheduleView {

    private static final Logger LOGGER = Logger.getLogger(GenerateScheduleView.class.getName());

    private final GenerateScheduleGUIControllerCLI graphicController;

    public GenerateScheduleView(GenerateScheduleGUIControllerCLI graphicController) {
        this.graphicController = graphicController;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        LOGGER.info("   SHIFT PLANNER - GENERAZIONE TURNI    ");

        try {
            LOGGER.info("Inserisci la Data di Inizio (YYYY-MM-DD): ");
            String startDate = scanner.nextLine();

            LOGGER.info("Inserisci la Data di Fine   (YYYY-MM-DD): ");
            String endDate = scanner.nextLine();

            LOGGER.info("\nElaborazione in corso... Attendi...");

            // 1. Genera la Bozza
            ScheduleBean draft = graphicController.generateSchedule(startDate, endDate);
            long draftId = draft.getScheduleId();
            displaySchedule(draft);
            LOGGER.info("\n[BOZZA GENERATA CON SUCCESSO]");

            // 2. Loop di Modifica Manuale (Opzionale)
            while (true) {
                LOGGER.info("\nVuoi modificare l'assegnazione di un turno? (s/n): ");
                String choice = scanner.nextLine().trim();

                if ("n".equalsIgnoreCase(choice)) {
                    break;
                } else if ("s".equalsIgnoreCase(choice)) {
                    LOGGER.info("Inserisci l'indice del turno (es. 0 per la prima riga): ");
                    int shiftIndex = Integer.parseInt(scanner.nextLine().trim());

                    LOGGER.info("Inserisci l'ID del NUOVO dipendente da assegnare: ");
                    long newEmployeeId = Long.parseLong(scanner.nextLine().trim());

                    draft = graphicController.modifyShift(draftId, shiftIndex, newEmployeeId);
                    displaySchedule(draft);
                    LOGGER.info("\n[TURNO MODIFICATO CON SUCCESSO]");
                }
            }

            // 3. Pubblicazione
            LOGGER.info("\nVuoi PUBBLICARE il calendario definitivo? (s/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                ScheduleBean published = graphicController.publishSchedule(draftId);
                displaySchedule(published);
                LOGGER.info("\n========================================");
                LOGGER.info("  Calendario pubblicato con successo!");
                LOGGER.info("  I dipendenti sono stati notificati.");
                LOGGER.info("========================================");
            } else {
                LOGGER.info("\n[Operazione annullata. La bozza è stata scartata.]");
            }

            // Logout
            LOGGER.info("\nVuoi fare logout? (s/n): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("s")) {
                // L'utente vuole restare: ricomincia il flusso di generazione
                start();
            }
            // Se risponde "s" il metodo termina e LoginView ripresenta il login

        } catch (ShiftPlannerException e) {
            LOGGER.log(Level.SEVERE, "Errore ShiftPlanner", e);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Inserisci un numero valido.", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "ERRORE CRITICO: Si è verificato un errore di sistema.", e);
        }
    }

    //Stampa il calendario generato in formato tabellare
    private void displaySchedule(ScheduleBean schedule) {

        LOGGER.info(String.format("  Periodo : %s → %s%n",
                schedule.getStartDate(), schedule.getEndDate()));
        LOGGER.info(String.format("  Stato   : %s%n", schedule.getStatus()));
        LOGGER.info(String.format("  Turni   : %d%n", schedule.getShifts().size()));
        LOGGER.info("----------------------------------------");
        LOGGER.info(String.format("  %-12s %-10s %-8s %-8s %-20s%n",
                "DATA", "SLOT", "INIZIO", "FINE", "DIPENDENTE"));
        LOGGER.info("----------------------------------------");

        for (ShiftBean shift : schedule.getShifts()) {
            String employeeName = (shift.getEmployee() != null)
                    ? shift.getEmployee().getFullName()
                    : "*** NON ASSEGNATO ***";

            LOGGER.info(String.format("  %-12s %-10s %-8s %-8s %-20s%n",
                    shift.getDate(),
                    getSlotLabel(shift),
                    shift.getStartTime(),
                    shift.getEndTime(),
                    employeeName));
        }

        LOGGER.info("========================================");
    }


    private String getSlotLabel(ShiftBean shift) {
        if (shift.getStartTime() == null) return "-";
        int hour = shift.getStartTime().getHour();
        if (hour == 6)  return "MORNING";
        if (hour == 14) return "AFTERNOON";
        if (hour == 22) return "NIGHT";
        return shift.getStartTime().toString();
    }
}