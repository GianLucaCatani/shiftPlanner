package com.shiftplanner.util;

import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.dao.factory.DAOFactoryDB;
import com.shiftplanner.dao.factory.DAOFactoryDemo;
import com.shiftplanner.dao.factory.DAOFactoryFS;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigLoader {

    private static final Logger LOGGER = Logger.getLogger(ConfigLoader.class.getName());
    private static final String APP_MODE_DEMO = "app.mode.demo";

    private ConfigLoader() {
        // utility class — no instances
    }

    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                LOGGER.warning("Impossibile trovare config.properties, uso configurazione di default (DEMO).");
                properties.setProperty(APP_MODE_DEMO, "true");
            } else {
                properties.load(input);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Errore durante il caricamento di config.properties", ex);
        }
    }

    //Il cuore dell'Abstract Factory: ritorna la Factory concreta giusta in base alle properties.
    public static DAOFactory getDAOFactory() {

        String isDemoStr = properties.getProperty(APP_MODE_DEMO, "true");
        boolean isDemo = Boolean.parseBoolean(isDemoStr);

        if (isDemo) {
            LOGGER.info("Avvio in modalità: DEMO (In-Memory)");
            return DAOFactoryDemo.getInstance();
        } else {
            String persistenceType = properties.getProperty("app.persistence.type", "db");//se non trova usa il valore di default "db"
            if ("fs".equalsIgnoreCase(persistenceType)) {
                LOGGER.info("Avvio in modalità: FULL (File System)");
                return DAOFactoryFS.getInstance();
            } else {
                LOGGER.info("Avvio in modalità: FULL (Database MySQL)");
                return DAOFactoryDB.getInstance();
            }
        }
    }

    //Restituisce true se l'applicazione è configurata in modalità Demo
    //Usato dal Main per decidere se caricare i dati di esempio
    public static boolean isDemo() {
        String isDemoStr = properties.getProperty(APP_MODE_DEMO, "true");
        return Boolean.parseBoolean(isDemoStr);
    }
}