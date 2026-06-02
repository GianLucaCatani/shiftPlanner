package com.shiftplanner.dao.fs;

import com.shiftplanner.dao.NotificationDAO;
import com.shiftplanner.exceptions.dao.DAOException;
import com.shiftplanner.model.Notification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAOFileSystem implements NotificationDAO {

    private static final String FILE_PATH = "data/notifications.csv";

    @Override
    public void saveNotification(Notification notification) throws DAOException {
        // Usiamo 'true' per accodare le notifiche senza sovrascriverle
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            
            // Creiamo la riga: ID_Notifica, ID_Impiegato, Messaggio
            String csvLine = notification.getNotificationId() + "," +
                             notification.getEmployeeId() + "," +
                             notification.getMessage();
                             
            pw.println(csvLine);
            
        } catch (IOException e) {
            throw new DAOException("Errore durante il salvataggio nel file " + FILE_PATH, e);
        }     
    }
    
    @Override
    public List<Notification> getNotificationsByEmployeeId(long employeeId) throws DAOException {
        List<Notification> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",", 3);
                if (data.length == 3) {
                    long notifId = Long.parseLong(data[0].trim());
                    long empId   = Long.parseLong(data[1].trim());
                    String msg   = data[2].trim();
                    if (empId == employeeId) {
                        result.add(new Notification(notifId, empId, msg));
                    }
                }
            }
        } catch (IOException e) {
            throw new DAOException("Errore nella lettura del file " + FILE_PATH, e);
        }
        return result;
    }
}