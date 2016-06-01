package control.manager;

import DAO.PrimaryTaskDAO;
import app.MainApp;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.PrimaryTask;
import model.Task;
import util.DateUtil;

import java.sql.Time;
import java.text.ParseException;
import java.util.List;

public class CreateTaskDialogController {
    private Stage stage;
    private Task createdTask;
    private ObservableList<PrimaryTask> observableList;
    @FXML
    private TableView<PrimaryTask> primaryTaskTableView;
    @FXML
    private TableColumn<PrimaryTask, String> nameColumn;
    @FXML
    private TableColumn<PrimaryTask, Integer> costColumn;
    @FXML
    private TableColumn<PrimaryTask, Time> timeToCompleteColumn;
    @FXML
    private TextField amountField;
    @FXML
    private TextField deadlineField;
    @FXML
    private TextField priorityField;
    @FXML
    private TextArea primaryTaskDescriptionTextArea;
    @FXML
    private TextField filterField;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        costColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getCost()));
        timeToCompleteColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getTimeToComplete()));
        primaryTaskDescriptionTextArea.setEditable(false);
        primaryTaskDescriptionTextArea.setWrapText(true);
        amountField.setEditable(true);
        deadlineField.setEditable(true);
        deadlineField.setPromptText(DateUtil.DATE_FORMAT);
        deadlineField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    String[] splitted = newValue.split("-");
                    for (String digit : splitted) {
                        if (!digit.matches("\\d*")) {
                            deadlineField.setText("");
                            MainApp.showAlert(Alert.AlertType.ERROR,
                                    "",
                                    "Введіть коректно дату!",
                                    "");
                        }
                    }
                    if (newValue.length() == 4
                            || newValue.length() == 7) {
                        deadlineField.setText(newValue + "-");
                    }
                });
        priorityField.setEditable(true);
        priorityField.setPromptText("> 0");
        primaryTaskTableView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                showDescription(newValue));
    }

    public void handleCancel() {
        stage.close();
    }

    public void handleCreateTask() {
        if (valid()) {
            createdTask = new Task();
            createdTask.setPrimaryTask(primaryTaskTableView
                    .getSelectionModel().getSelectedItem());
            try {
                createdTask.setDeadline(DateUtil.fromString(deadlineField.getText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            createdTask.setPriority(Integer.parseInt(priorityField.getText()));
            createdTask.setAmount(Integer.parseInt(amountField.getText()));
            handleCancel();
        }

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Task getCreatedTask() {
        return createdTask;
    }

    public void loadData() {
        PrimaryTaskDAO primaryTaskDAO = new PrimaryTaskDAO();
        List<PrimaryTask> list = primaryTaskDAO.findAll();
        observableList = FXCollections.observableArrayList(list);
        createFilter();
    }

    private void createFilter() {
        FilteredList<PrimaryTask> filteredData =
                new FilteredList<>(observableList, p -> true);
        filterField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredData.setPredicate(primaryTask -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCasedNewValue = newValue.toLowerCase();
                if (primaryTask.getName().toLowerCase()
                        .contains(lowerCasedNewValue)) {
                    return true;
                }
                return false;
            });
        }));
        SortedList<PrimaryTask> sortedData =
                new SortedList<>(filteredData);
        sortedData.comparatorProperty()
                .bind(primaryTaskTableView.comparatorProperty());
        primaryTaskTableView.setItems(sortedData);
    }

    private boolean valid() {
        PrimaryTask primaryTask = primaryTaskTableView
                .getSelectionModel().getSelectedItem();
        String errorMessage = "";
        if (primaryTask == null) {
            errorMessage += "Виберіть, будь ласка, завдання! ";
        }
        int amount;
        try {
            amount = Integer.parseInt(amountField.getText());
            if (amount <= 0) {
                errorMessage += "Задайте додатнє значення! ";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Задайте коректно кількість завдань! ";
        }
        int priority;
        try {
            priority = Integer.parseInt(priorityField.getText());
            if (priority <= 0) {
                errorMessage += "Задайте додатнє значення! ";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Задайте коректно пріорітет! ";
        }
        if (deadlineField.getText().equals("")) {
            errorMessage += "Задайте коректно дедлайн! ";
        }
        try {
            DateUtil.fromString(deadlineField.getText());
        } catch (ParseException e) {
            errorMessage += "Задайте коректно дедлайн! ";
        }
        if (errorMessage.equals("")) {
            return true;
        }
        MainApp.showAlert(Alert.AlertType.ERROR,
                "",
                "Неможливо створити завдання!",
                errorMessage);
        return false;
    }

    private void showDescription(PrimaryTask primaryTask) {
        if (primaryTask != null) {
            primaryTaskDescriptionTextArea.setText(primaryTask.getDescription());
        } else {
            primaryTaskDescriptionTextArea.setText("");
        }
    }
}
