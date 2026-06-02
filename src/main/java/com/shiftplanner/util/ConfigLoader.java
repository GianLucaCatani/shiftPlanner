package com.shiftplanner.util;

import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.dao.factory.DAOFactoryDB;
import com.shiftplanner.dao.factory.DAOFactoryDemo;
import com.shiftplanner.dao.factory.DAOFactoryFS;
import com.shiftplanner.exceptions.ShiftPlannerException;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    
    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Impossibile trovare config.properties, uso configurazione di default (DEMO).");
                properties.setProperty("app.mode.demo", "true");
            } else {
                properties.load(input);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Il cuore dell'Abstract Factory: ritorna la Factory concreta giusta in base alle properties.
    public static DAOFactory getDAOFactory() throws ShiftPlannerException {
        
        String isDemoStr = properties.getProperty("app.mode.demo", "true");
        boolean isDemo = Boolean.parseBoolean(isDemoStr);
        
        if (isDemo) {
            System.out.println("Avvio in modalità: DEMO (In-Memory)");
            return DAOFactoryDemo.getInstance();
        } else {
            String persistenceType = properties.getProperty("app.persistence.type", "db");//se non trova usa il valore di default "db"
            if ("fs".equalsIgnoreCase(persistenceType)) {
                System.out.println("Avvio in modalità: FULL (File System)");
                return DAOFactoryFS.getInstance();
            } else {
                System.out.println("Avvio in modalità: FULL (Database MySQL)");
                return DAOFactoryDB.getInstance();
            }
        }
    }
    
    //Restituisce tru se l'appicazione è configurata in modalità Demo
    //Usato dal Main per decidere se caricare i dati di esempio
    public static boolean isDemo() {
        String isDemoStr = properties.getProperty("app.mode.demo", "true");
        return Boolean.parseBoolean(isDemoStr);
    }
}