package com.shiftplanner.controller.gui.fx;

import com.shiftplanner.bean.NotificationBean;
import com.shiftplanner.bean.ShiftBean;
import com.shiftplanner.bean.UserBean;
import com.shiftplanner.controller.app.EmployeeDashboardController;
import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.engineering.service.NotificationManager;
import com.shiftplanner.exceptions.ShiftPlannerException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controller GUI JavaFX per la dashboard del dipendente.
 * Mostra i turni assegnati e le notifiche dell'utente autenticato.
 */
public class EmployeeDashboardGUIControllerFX {
	
	private static final String FXML_LOGIN = "/com/shiftplanner/view/fx/LoginView.fxml";

    @FXML private Label welcomeLabel;

    @FXML private TableView<ShiftRow> shiftsTable;
    @FXML private TableColumn<ShiftRow, String> dateColumn;
    @FXML private TableColumn<ShiftRow, String> startTimeColumn;
    @FXML private TableColumn<ShiftRow, String> endTimeColumn;
    @FXML private TableColumn<ShiftRow, String> slotColumn;

    @FXML private ListView<String> notificationsList;

    @FXML private Label errorLabel;

    private EmployeeDashboardController dashboardController;
    private DAOFactory daoFactory;
    private NotificationManager notificationManager;

    public void initDependencies(DAOFactory daoFactory, NotificationManager notificationManager, UserBean user) {
    	this.daoFactory = daoFactory;
        this.notificationManager = notificationManager;
        this.dashboardController = new EmployeeDashboardController(daoFactory);
        welcomeLabel.setText("Benvenuto, " + user.getUsername() + "!");
        initTableColumns();
        loadData(user.getEmployeeId());
    }

    private void initTableColumns() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        slotColumn.setCellValueFactory(new PropertyValueFactory<>("slot"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    }

    private void loadData(long employeeId) {
        loadShifts(employeeId);
        loadNotifications(employeeId);
    }

    private void loadShifts(long employeeId) {
        try {
            List<ShiftBean> shifts = dashboardController.getMyShifts(employeeId);
            ObservableList<ShiftRow> rows = FXCollections.observableArrayList();
            for (ShiftBean shift : shifts) {
                rows.add(new ShiftRow(
                        shift.getDate() != null ? shift.getDate().toString() : "-",
                        getSlotLabel(shift),
                        shift.getStartTime() != null ? shift.getStartTime().toString() : "-",
                        shift.getEndTime()   != null ? shift.getEndTime().toString()   : "-"
                ));
            }
            shiftsTable.setItems(rows);
        } catch (ShiftPlannerException e) {
            showError("Errore nel caricamento dei turni: " + e.getMessage());
        }
    }

    private void loadNotifications(long employeeId) {
        try {
            List<NotificationBean> notifications = dashboardController.getMyNotifications(employeeId);
            ObservableList<String> items = FXCollections.observableArrayList();
            for (NotificationBean n : notifications) {
                items.add(n.getMessage());
            }
            notificationsList.setItems(items);
        } catch (ShiftPlannerException e) {
            showError("Errore nel caricamento delle notifiche: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    
    //Chiamato dal bottone "Logout".
    //Ricarica il LoginView sulla stessa finestra.
    @FXML
    private void onLogoutClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(FXML_LOGIN));
            Parent root = loader.load();

            LoginGUIControllerFX loginCtrl = loader.getController();
            loginCtrl.initDependencies(daoFactory, notificationManager);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setTitle("ShiftPlanner — Login");
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            showError("Errore durante il logout: " + e.getMessage());
        }
    }

    private String getSlotLabel(ShiftBean shift) {
        if (shift.getStartTime() == null) return "-";
        int hour = shift.getStartTime().getHour();
        if (hour == 6)  return "MORNING";
        if (hour == 14) return "AFTERNOON";
        if (hour == 22) return "NIGHT";
        return shift.getStartTime().toString();
    }

    // Classe di supporto per la TableView
    public static class ShiftRow {
        private final String date;
        private final String slot;
        private final String startTime;
        private final String endTime;

        public ShiftRow(String date, String slot, String startTime, String endTime) {
            this.date      = date;
            this.slot      = slot;
            this.startTime = startTime;
            this.endTime   = endTime;
        }

        public String getDate()      { return date; }
        public String getSlot()      { return slot; }
        public String getStartTime() { return startTime; }
        public String getEndTime()   { return endTime; }
    }
}