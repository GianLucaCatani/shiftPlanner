package com.shiftplanner.util;

import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Employee;
import com.shiftplanner.model.EmployeeRole;
import com.shiftplanner.model.User;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 Carica dati di esempio nel database in-memory per la modalità Demo.
 Viene chiamato dal Main solo quando app.mode.demo=true.
 Senza questi dati l'algoritmo di generazione non produce turni perché la lista dipendenti è vuota.*/
public final class DemoDataLoader {

    private static final Logger LOGGER = Logger.getLogger(DemoDataLoader.class.getName());

    private DemoDataLoader() {
        throw new IllegalStateException("Utility class — non istanziare.");
    }

    public static void load(DAOFactory daoFactory) {
        try {
            // 5 dipendenti con ruoli e ore contrattuali diversi
            daoFactory.getEmployeeDAO().saveEmployee(
                    new Employee(1, "Mario Rossi",    EmployeeRole.EMPLOYEE, 40));
            daoFactory.getEmployeeDAO().saveEmployee(
                    new Employee(2, "Anna Bianchi",   EmployeeRole.EMPLOYEE, 36));
            daoFactory.getEmployeeDAO().saveEmployee(
                    new Employee(3, "Luca Verdi",     EmployeeRole.EMPLOYEE, 40));
            daoFactory.getEmployeeDAO().saveEmployee(
                    new Employee(4, "Sara Neri",      EmployeeRole.EMPLOYEE, 32));
            daoFactory.getEmployeeDAO().saveEmployee(
                    new Employee(5, "Paolo Gialli",   EmployeeRole.EMPLOYEE, 40));
            // Coordinatore (non partecipa ai turni, ma ha un profilo Employee per il login)
            daoFactory.getEmployeeDAO().saveEmployee(
                    new Employee(6, "Admin Coord",    EmployeeRole.COORDINATOR, 0));

            LOGGER.info("[DEMO] Caricati 6 dipendenti di esempio.");

            // Account utente per il login
            // Coordinatore: username=admin, password=admin
            daoFactory.getUserDAO().saveUser(
                    new User(1, "admin", "admin", 6, EmployeeRole.COORDINATOR));
            // Dipendenti: username=mario, anna, luca, sara, paolo — password uguale allo username
            daoFactory.getUserDAO().saveUser(
                    new User(2, "mario", "mario", 1, EmployeeRole.EMPLOYEE));
            daoFactory.getUserDAO().saveUser(
                    new User(3, "anna",  "anna",  2, EmployeeRole.EMPLOYEE));
            daoFactory.getUserDAO().saveUser(
                    new User(4, "luca",  "luca",  3, EmployeeRole.EMPLOYEE));
            daoFactory.getUserDAO().saveUser(
                    new User(5, "sara",  "sara",  4, EmployeeRole.EMPLOYEE));
            daoFactory.getUserDAO().saveUser(
                    new User(6, "paolo", "paolo", 5, EmployeeRole.EMPLOYEE));

            LOGGER.info("[DEMO] Caricati 6 account utente di esempio.");
            LOGGER.info("[DEMO] Coordinatore → username: admin  password: admin");
            LOGGER.info("[DEMO] Dipendente   → username: mario  password: mario (e così via)");

        } catch (DAOException e) {
            LOGGER.log(Level.WARNING, "[DEMO] Avviso: impossibile caricare i dati di esempio.", e);
        }
    }
}
