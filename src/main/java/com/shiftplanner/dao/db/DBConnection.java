package com.shiftplanner.dao.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DBConnection {
	
	private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    
    private static  String url;
    private static  String user;
    private static  String password;
    
    private static Connection instance = null;

    static {
    	try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")){
    		if (input == null) {
    			LOGGER.severe("Errore CRITICO: Impossibile trovare il file db.properties");
    		} else {
    			Properties prop = new Properties();
    			prop.load(input); //Carica tutte le chiavi dal file
    			
    			url = prop.getProperty("CONNECTION_URL");
    			user = prop.getProperty("USER");
    			password = prop.getProperty("PASS");
    		}
    	} catch(Exception e) {
    		LOGGER.log(Level.SEVERE, "Errore durante il caricamento di db.properties", e);
    	}
    }
    
    //Costruttore privato
    private DBConnection() {}
    
    public static synchronized Connection getInstance() throws SQLException {
    	try {
    		if (instance == null || instance.isClosed()) {
                instance = DriverManager.getConnection(url, user, password);
            }
    	} catch (SQLException e) {
            throw new SQLException("Impossibile stabilire la connessione al database.", e);
        }
        return instance;
    }
}