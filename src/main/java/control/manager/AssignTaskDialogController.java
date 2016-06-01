package control.manager;

import app.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.AssignedTask;
import model.Person;
import util.DateUtil;

import java.util.Date;
import java.util.List;

public class AssignTaskDialogController {
    private MainApp mainApp;
    private int maxAmount;
    private Stage stage;
    private AssignedTask assignedTask;
    private ObservableList<Person> observableList;
    @FXML
    private TableView<Person> personTableView;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;
    @FXML
    private TableColumn<Person, Date> dateColumn;
    @FXML
    private TextField assignmentDateTextField;
    @FXML
    private TextField amountTextField;
    @FXML
    private TextField filterField;
    @FXML
    private TextArea descriptionTextArea;

    public AssignTaskDialogController() {
        assignedTask = new AssignedTask();
        assignedTask.setAssignmentDate(new Date());
        assignedTask.setFinished(false);
    }

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));
        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("dateIn"));
        descriptionTextArea.setWrapText(true);
        descriptionTextArea.setEditable(true);
        amountTextField.setEditable(true);
        assignmentDateTextField.setEditable(false);
        assignmentDateTextField.setText(DateUtil
                .toString(assignedTask.getAssignmentDate()));
    }

    public void handleExit() {
        assignedTask = null;
        stage.close();
    }

    public void handleAssignWorker() {
        Person person = personTableView.getSelectionModel()
                .selectedItemProperty().get();
        if (person == null) {
            MainApp.showAlert(Alert.AlertType.ERROR, "",
                    "Не вибрано робітника!",
                    "Виберіть, будь ласка, робітника!");
            return;
        }
        assignedTask.setPerson(person);
        int amount;
        try {
            amount = Integer.parseInt(amountTextField.getText());
            if (amount <= 0) {
                throw  new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            MainApp.showAlert(Alert.AlertType.ERROR,
                    "",
                    "Некоректне задання числа",
                    "Введіть, будь ласка, корректно кількість завдань");
            return;
        }

        if (amount > maxAmount) {
            MainApp.showAlert(Alert.AlertType.ERROR,
                    "",
                    "Ви можете призначити не більше ніж " + maxAmount
                            + " завдань!",
                    "");
            return;
        }
        assignedTask.setAmount(amount);
        assignedTask.setDescription(descriptionTextArea.getText());
        stage.close();
    }

    public void handleShowProfile() {
        Person person = personTableView.getSelectionModel()
                .selectedItemProperty().get();
        if (person == null) {
            MainApp.showAlert(Alert.AlertType.ERROR, "",
                    "Не вибрано робітника!",
                    "Виберіть, будь ласка, робітника!");
        } else {
            mainApp.showProfile(person);
        }
    }

    public void loadData(List<Person> list) {
        observableList = FXCollections.observableArrayList(list);
        createFilter();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public AssignedTask getAssignedTask() {
        return assignedTask;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
        amountTextField.setPromptText(Integer.toString(maxAmount));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void createFilter() {
        FilteredList<Person> filteredList = new FilteredList<>(
                observableList, p -> true);
        filterField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate(person -> {
                if (newValue == null) {
                    return true;
                }
                String lowerCasedNewValue = newValue.toLowerCase();
                if (person.getFirstName().contains(lowerCasedNewValue)) {
                    return true;
                }
                if (person.getLastName().contains(lowerCasedNewValue)) {
                    return true;
                }
                return false;
            });
        }));
        SortedList<Person> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(personTableView.comparatorProperty());
        personTableView.setItems(sortedList);
    }
}
