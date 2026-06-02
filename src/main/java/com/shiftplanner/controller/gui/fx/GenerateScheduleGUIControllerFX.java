package com.shiftplanner.controller.gui.fx;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import com.shiftplanner.bean.ScheduleBean;
import com.shiftplanner.bean.ShiftBean;
import com.shiftplanner.controller.app.GenerateScheduleController;
import com.shiftplanner.controller.app.ManualEditController;
import com.shiftplanner.dao.factory.DAOFactory;
import com.shiftplanner.engineering.service.NotificationManager;
import com.shiftplanner.exceptions.PeriodValidationException;
import com.shiftplanner.exceptions.ShiftPlannerException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/*Controller GUI JavaFX per il caso d'uso Generate Work Schedule.
1. Coordinatore inserisce le date e clicca "Genera Bozza"
    → chiama generateDraft() → mostra i turni in tabella
2. Coordinatore seleziona una riga, inserisce l'ID del nuovo dipendente
    e clicca "Modifica Turno" → chiama ManualEditController.modifyShift()
3. Coordinatore clicca "Pubblica" → chiama publishSchedule()
    → notifiche inviate, stato PUBLISHED
*/
public class GenerateScheduleGUIControllerFX {
	
	private static final String FXML_LOGIN = "/com/shiftplanner/view/fx/LoginView.fxml";

    @FXML private TextField startDateField;
    @FXML private TextField endDateField;
    
    @FXML private TextField employeeIdField;
    
    @FXML private Label errorLabel;
    @FXML private Label successLabel;
    @FXML private Label summaryLabel;

    @FXML private TableView<ShiftRow> shiftsTable;
    @FXML private TableColumn<ShiftRow, String> dateColumn;
    @FXML private TableColumn<ShiftRow, String> slotColumn;
    @FXML private TableColumn<ShiftRow, String> startTimeColumn;
    @FXML private TableColumn<ShiftRow, String> endTimeColumn;
    @FXML private TableColumn<ShiftRow, String> employeeColumn;

    private GenerateScheduleController generateController;
    private ManualEditController manualEditController;
    
    // ID della bozza corrente — serve per le operazioni di modifica e pubblicazione
    private long currentDraftId = -1;
    
    // Dipendenze conservate per il re-login dopo logout
    private DAOFactory daoFactory;
    private NotificationManager notificationManager;
    
    public void initDependencies(DAOFactory daoFactory, NotificationManager notificationManager) {
    	this.daoFactory = daoFactory;
        this.notificationManager = notificationManager;
    	this.generateController = new GenerateScheduleController(daoFactory, notificationManager);
        this.manualEditController = new ManualEditController(daoFactory, generateController);
        initTableColumns();
    }

    //Collega ogni colonna della TableView alla proprietà corrispondente di ShiftRow.
    //PropertyValueFactory usa il nome del getter: "date" → getDate(), ecc.
    private void initTableColumns() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        slotColumn.setCellValueFactory(new PropertyValueFactory<>("slot"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
    }

    /*Chiamato dal bottone "Genera Bozza".
    Genera lo schedule in stato DRAFT e lo mostra in tabella.
    Non pubblica ancora — il coordinatore può modificare i turni.*/
    @FXML
    private void onGenerateClicked() {
        // Resetta i messaggi precedenti
        hideMessages();
        shiftsTable.getItems().clear();
        currentDraftId = -1;

        String startDateStr = startDateField.getText();
        String endDateStr = endDateField.getText();

        //Controllo campi vuoti
        if (startDateStr == null || startDateStr.trim().isEmpty()
                || endDateStr == null || endDateStr.trim().isEmpty()) {
            showError("Le date non possono essere vuote.");
            return;
        }

        //Parsing delle date
        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(startDateStr.trim());
            endDate = LocalDate.parse(endDateStr.trim());
        } catch (DateTimeParseException e) {
            showError("Formato data non valido. Usa YYYY-MM-DD (es. 2026-06-01).");
            return;
        }

        //Creazione del Bean con validazione
        com.shiftplanner.bean.ScheduleBean bean = new com.shiftplanner.bean.ScheduleBean();
        try {
            bean.setStartDate(startDate);
            bean.setEndDate(endDate);
        } catch (PeriodValidationException e) {
            showError(e.getMessage());
            return;
        }

        try {
            ScheduleBean draft = generateController.generateDraft(bean);
            currentDraftId = draft.getScheduleId();
            displaySchedule(draft);
            showSuccess("Bozza generata. Puoi modificare i turni o pubblicare direttamente.");
        } catch (ShiftPlannerException e) {
            showError("Errore: " + e.getMessage());
        }
    }
    
    /*Chiamato dal bottone "Modifica Turno".
     Prende la riga selezionata nella tabella e l'ID del nuovo dipendente
     dal campo di testo, poi chiama ManualEditController.modifyShift().
     */
    @FXML
    private void onModifyShiftClicked() {
        hideMessages();

        if (currentDraftId < 0) {
            showError("Genera prima una bozza.");
            return;
        }

        // Recupera la riga selezionata nella tabella
        int selectedIndex = shiftsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            showError("Seleziona un turno dalla tabella da modificare.");
            return;
        }

        // Recupera l'ID del nuovo dipendente dal campo di testo
        String employeeIdStr = employeeIdField.getText();
        if (employeeIdStr == null || employeeIdStr.trim().isEmpty()) {
            showError("Inserisci l'ID del nuovo dipendente.");
            return;
        }

        long newEmployeeId;
        try {
            newEmployeeId = Long.parseLong(employeeIdStr.trim());
        } catch (NumberFormatException e) {
            showError("ID dipendente non valido. Inserisci un numero intero.");
            return;
        }

        try {
            // Delega la modifica al ManualEditController
            ScheduleBean updated = manualEditController.modifyShift(
                    currentDraftId, selectedIndex, newEmployeeId);
            displaySchedule(updated);
            showSuccess("Turno modificato correttamente.");
        } catch (ShiftPlannerException e) {
            showError("Errore modifica: " + e.getMessage());
        }
    }
    
    /*Chiamato dal bottone "Pubblica Calendario".
    Pubblica la bozza corrente: cambia stato a PUBLISHED
    e attiva il pattern Observer che invia le notifiche.
    */
    @FXML
    private void onPublishClicked() {
        hideMessages();

        if (currentDraftId < 0) {
            showError("Genera prima una bozza da pubblicare.");
            return;
        }

        try {
            ScheduleBean published = generateController.publishSchedule(currentDraftId);
            currentDraftId = -1; // bozza pubblicata, reset
            displaySchedule(published);
            showSuccess("Calendario pubblicato con successo! I dipendenti sono stati notificati.");
        } catch (ShiftPlannerException e) {
            showError("Errore pubblicazione: " + e.getMessage());
        }
    }

    //Popola la TableView con i turni del calendario generato.
    private void displaySchedule(ScheduleBean schedule) {
        ObservableList<ShiftRow> rows = FXCollections.observableArrayList();

        for (ShiftBean shift : schedule.getShifts()) {
            String employeeName = (shift.getEmployee() != null)
                    ? shift.getEmployee().getFullName()
                    : "*** NON ASSEGNATO ***";

            rows.add(new ShiftRow(
                    shift.getDate() != null ? shift.getDate().toString() : "-",
                    getSlotLabel(shift.getStartTime()),
                    shift.getStartTime() != null ? shift.getStartTime().toString() : "-",
                    shift.getEndTime() != null ? shift.getEndTime().toString() : "-",
                    employeeName
            ));
        }

        shiftsTable.setItems(rows);

        summaryLabel.setText(String.format(
                "Periodo: %s \u2192 %s  |  Stato: %s  |  Turni generati: %d",
                schedule.getStartDate(), schedule.getEndDate(),
                schedule.getStatus(), schedule.getShifts().size()));
        summaryLabel.setVisible(true);
    }
   
    // Gestione messaggi
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        successLabel.setVisible(false);
    }

    private void showSuccess(String message) {
        successLabel.setText(message);
        successLabel.setVisible(true);
        errorLabel.setVisible(false);
    }

    private void hideMessages() {
        errorLabel.setVisible(false);
        successLabel.setVisible(false);
        summaryLabel.setVisible(false);
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

            Stage stage = (Stage) startDateField.getScene().getWindow();
            stage.setTitle("ShiftPlanner — Login");
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            showError("Errore durante il logout: " + e.getMessage());
        }
    }


    private String getSlotLabel(LocalTime startTime) {
        if (startTime == null) return "-";
        int hour = startTime.getHour();
        if (hour == 6)  return "MORNING";
        if (hour == 14) return "AFTERNOON";
        if (hour == 22) return "NIGHT";
        return startTime.toString();
    }

    /*Classe di supporto per la TableView di JavaFX.
    JavaFX richiede che ogni riga della tabella sia un oggetto con getter
    che corrispondono ai nomi usati in PropertyValueFactory.
    Questa classe converte i dati di ShiftBean in stringhe semplici pronte per la visualizzazione.*/
    public static class ShiftRow {

        private final String date;
        private final String slot;
        private final String startTime;
        private final String endTime;
        private final String employeeName;

        public ShiftRow(String date, String slot, String startTime,
                        String endTime, String employeeName) {
            this.date = date;
            this.slot = slot;
            this.startTime = startTime;
            this.endTime = endTime;
            this.employeeName = employeeName;
        }

        public String getDate() { return date; }
        public String getSlot() { return slot; }
        public String getStartTime() { return startTime; }
        public String getEndTime() { return endTime; }
        public String getEmployeeName() { return employeeName; }
    }
}
