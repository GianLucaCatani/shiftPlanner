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

import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
	
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	
	static {
        // Carica la configurazione del logger da logging.properties (classpath)
        try (InputStream config = Main.class.getClassLoader()
                .getResourceAsStream("logging.properties")) {
            if (config != null) {
                LogManager.getLogManager().readConfiguration(config);
            }
        } catch (Exception e) {
            // Se fallisce usiamo la configurazione di default della JVM
        }
    }

    public static void main(String[] args) {

    	LOGGER.info("   SHIFT PLANNER - Avvio Applicazione   ");
      
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
            LOGGER.info("Scegli l'interfaccia da avviare:");
            LOGGER.info("  [1] JavaFX (Grafica)");
            LOGGER.info("  [2] CLI (Riga di Comando)");
            LOGGER.info("\nScelta: ");
            String choice = scanner.nextLine().trim();

            LOGGER.info("");

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
        	LOGGER.log(Level.SEVERE, "ERRORE FATALE all'avvio: " + e.getMessage(), e);
        }
    }
}