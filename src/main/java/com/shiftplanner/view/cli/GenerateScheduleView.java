package com.shiftplanner.view.cli;

import java.util.Scanner;

import com.shiftplanner.bean.ScheduleBean;
import com.shiftplanner.bean.ShiftBean;
import com.shiftplanner.controller.gui.cli.GenerateScheduleGUIControllerCLI;
import com.shiftplanner.exceptions.ShiftPlannerException;

public class GenerateScheduleView {
	
	private final GenerateScheduleGUIControllerCLI graphicController;
	
	public GenerateScheduleView(GenerateScheduleGUIControllerCLI graphicController) {
		this.graphicController = graphicController;
	}
	
	public void start() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("   SHIFT PLANNER - GENERAZIONE TURNI    ");
        
        try {
            System.out.print("Inserisci la Data di Inizio (YYYY-MM-DD): ");
            String startDate = scanner.nextLine();
            
            System.out.print("Inserisci la Data di Fine   (YYYY-MM-DD): ");
            String endDate = scanner.nextLine();
            
            System.out.println("\nElaborazione in corso... Attendi...");
            
            // 1. Genera la Bozza
            ScheduleBean draft = graphicController.generateSchedule(startDate, endDate);
            long draftId = draft.getScheduleId();
            displaySchedule(draft);
            System.out.println("\n[BOZZA GENERATA CON SUCCESSO]");
            
            // 2. Loop di Modifica Manuale (Opzionale)
            while (true) {
                System.out.print("\nVuoi modificare l'assegnazione di un turno? (s/n): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                
                if (choice.equals("n")) {
                    break;
                } else if (choice.equals("s")) {
                    System.out.print("Inserisci l'indice del turno (es. 0 per la prima riga): ");
                    int shiftIndex = Integer.parseInt(scanner.nextLine().trim());
                    
                    System.out.print("Inserisci l'ID del NUOVO dipendente da assegnare: ");
                    long newEmployeeId = Long.parseLong(scanner.nextLine().trim());
                    
                    draft = graphicController.modifyShift(draftId, shiftIndex, newEmployeeId);
                    displaySchedule(draft);
                    System.out.println("\n[TURNO MODIFICATO CON SUCCESSO]");
                }
            }
            
            // 3. Pubblicazione
            System.out.print("\nVuoi PUBBLICARE il calendario definitivo? (s/n): ");
            if (scanner.nextLine().trim().toLowerCase().equals("s")) {
                ScheduleBean published = graphicController.publishSchedule(draftId);
                displaySchedule(published);
                System.out.println("\n========================================");
                System.out.println("  Calendario pubblicato con successo!");
                System.out.println("  I dipendenti sono stati notificati.");
                System.out.println("========================================");
            } else {
                System.out.println("\n[Operazione annullata. La bozza è stata scartata.]");
            }
            
            // Logout
            System.out.print("\nVuoi fare logout? (s/n): ");
            if (!scanner.nextLine().trim().toLowerCase().equals("s")) {
                // L'utente vuole restare: ricomincia il flusso di generazione
                start();
            }
            // Se risponde "s" il metodo termina e LoginView ripresenta il login
            
        } catch (ShiftPlannerException e) {
            System.out.println("\nERRORE: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("\nERRORE: Inserisci un numero valido.");
        } catch (Exception e) {
            System.out.println("\nERRORE CRITICO: Si è verificato un errore di sistema.");
            e.printStackTrace();
        }
	}
	
	//Stampa il calendario generato in formato tabellare
    private void displaySchedule(ScheduleBean schedule) {
      
        System.out.printf("  Periodo : %s → %s%n",
                schedule.getStartDate(), schedule.getEndDate());
        System.out.printf("  Stato   : %s%n", schedule.getStatus());
        System.out.printf("  Turni   : %d%n", schedule.getShifts().size());
        System.out.println("----------------------------------------");
        System.out.printf("  %-12s %-10s %-8s %-8s %-20s%n",
                "DATA", "SLOT", "INIZIO", "FINE", "DIPENDENTE");
        System.out.println("----------------------------------------");

        for (ShiftBean shift : schedule.getShifts()) {
            String employeeName = (shift.getEmployee() != null)
                    ? shift.getEmployee().getFullName()
                    : "*** NON ASSEGNATO ***";

            System.out.printf("  %-12s %-10s %-8s %-8s %-20s%n",
                    shift.getDate(),
                    getSlotLabel(shift),
                    shift.getStartTime(),
                    shift.getEndTime(),
                    employeeName);
        }
        System.out.println("========================================");
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
