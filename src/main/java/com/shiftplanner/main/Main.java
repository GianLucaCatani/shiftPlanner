package com.shiftplanner.main;

import com.shiftplanner.controller.gui.cli.GenerateScheduleGUIControllerCLI;
import com.shiftplanner.controller.gui.cli.LoginGUIControllerCLI;
import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.engineering.service.NotificationManager;
import com.shiftplanner.util.ConfigLoader;
import com.shiftplanner.util.DemoDataLoader;
import com.shiftplanner.view.cli.EmployeeDashboardView;
import com.shiftplanner.view.cli.GenerateScheduleView;
import com.shiftplanner.view.cli.LoginView;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("   SHIFT PLANNER - Avvio Applicazione   ");
      
        try {
            //Legge config.properties e crea la Factory corretta
            DAOFactory daoFactory = ConfigLoader.getDAOFactory();
            
            //Se siamo in Demo Mode, pre-popola il database in-memory con dati di esempio
            //Senza questo passo la lista dipendenti è vuota e l'algoritmo non genera turni
            if (ConfigLoader.isDemo()) {
                DemoDataLoader.load(daoFactory);
            }

            // Crea il NotificationManager iniettandogli la factory
            NotificationManager notificationManager = new NotificationManager(daoFactory);

            //Chiede all'utente quale interfaccia avviare
            Scanner scanner = new Scanner(System.in);
            System.out.println("Scegli l'interfaccia da avviare:");
            System.out.println("  [1] JavaFX (Grafica)");
            System.out.println("  [2] CLI (Riga di Comando)");
            System.out.print("\nScelta: ");
            String choice = scanner.nextLine().trim();

            System.out.println();

            if ("1".equals(choice)) {
            	//Avvio JavaFX
            	MainFX.setDependencies(daoFactory, notificationManager);
                MainFX.launch(MainFX.class, args);
            } else {
            	// Costruisce le view di destinazione post-login
                GenerateScheduleGUIControllerCLI guiController = new GenerateScheduleGUIControllerCLI(daoFactory,notificationManager);
                GenerateScheduleView coordinatorView = new GenerateScheduleView(guiController);
                EmployeeDashboardView employeeView   = new EmployeeDashboardView(daoFactory);

                // Avvia il login CLI — instrada automaticamente in base al ruolo
                LoginGUIControllerCLI loginController = new LoginGUIControllerCLI(daoFactory);
                LoginView loginView = new LoginView(loginController, coordinatorView, employeeView, scanner);
                loginView.start();
            }

        } catch (Exception e) {
            System.err.println("\n ERRORE FATALE all'avvio: " + e.getMessage());
            e.printStackTrace();
        }
    }
}