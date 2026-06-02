package com.shiftplanner.main;

import com.shiftplanner.controller.gui.fx.LoginGUIControllerFX;
import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.engineering.service.NotificationManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

    // Variabili statiche per passare le dipendenze a JavaFX
    private static DAOFactory daoFactory;
    private static NotificationManager notificationManager;

    //Chiamato dal Main prima di launch() per iniettare le dipendenze.
    public static void setDependencies(DAOFactory factory, NotificationManager manager) {
        daoFactory = factory;
        notificationManager = manager;
    }

    //Carica il file FXML, inietta le dipendenze nel controller FX e mostra la finestra.
    @Override
    public void start(Stage primaryStage) throws Exception {

        //Carica il file FXML (layout della finestra)
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/shiftplanner/view/fx/LoginView.fxml"));
        Parent root = loader.load();

        //Recupera il controller FX creato da FXMLLoader
        LoginGUIControllerFX controller = loader.getController();

        //Inietta le dipendenze nel controller FX
        controller.initDependencies(daoFactory, notificationManager);

        //Configura e mostra la finestra
        Scene scene = new Scene(root);
        primaryStage.setTitle("ShiftPlanner — Generazione Turni");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
