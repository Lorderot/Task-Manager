package control.manager;

import DAO.AssignedTaskDAO;
import DAO.PersonDAO;
import DAO.RequestDAO;
import app.MainApp;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.AssignedTask;
import model.Person;
import model.Request;
import model.Task;
import util.DateUtil;
import util.TimeUtil;

import java.util.Date;
import java.util.List;

public class TaskDetailsController {
    private MainApp mainApp;
    private Request request;
    private Integer tasksToAssign;
    private ObservableList<AssignedTask> taskList;
    private Task task;
    private Stage stage;
    private AssignedTaskDAO assignedTaskDAO;
    private PersonDAO personDAO;
    @FXML
    private TableView<AssignedTask> taskTableView;
    @FXML
    private TableColumn<AssignedTask, String> firstNameColumn;
    @FXML
    private TableColumn<AssignedTask, String> lastNameColumn;
    @FXML
    private TableColumn<AssignedTask, Integer> amountOfTasksColumn;
    @FXML
    private TableColumn<AssignedTask, String> isFinishedColumn;
    @FXML
    private TextField creationDateField;
    @FXML
    private TextField deadlineField;
    @FXML
    private TextArea commentsTextArea;
    @FXML
    private TextField nameOfJobTextField;
    @FXML
    private TextField costField;
    @FXML
    private TextField timeToCompleteField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextField tasksToAssignField;

    public TaskDetailsController() {
        assignedTaskDAO = new AssignedTaskDAO();
        personDAO = new PersonDAO();
    }

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue()
                        .getPerson().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue()
                        .getPerson().getLastName()));
        amountOfTasksColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue()
                        .getAmount()));
        isFinishedColumn.setCellValueFactory(cellData -> {
            String isFinished;
            if (cellData.getValue().getFinished()) {
                isFinished = "Так";
            } else {
                isFinished = "Ні";
            }
            return new SimpleStringProperty(isFinished);
        });
        creationDateField.setEditable(false);
        deadlineField.setEditable(false);
        nameOfJobTextField.setEditable(false);
        costField.setEditable(false);
        timeToCompleteField.setEditable(false);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setWrapText(true);
        commentsTextArea.setEditable(false);
        tasksToAssignField.setEditable(false);
        showDescription(null);
        taskTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    showDescription(newValue);
                });
    }

    public void handleExit() {
        stage.close();
    }

    public void handleAssignWorker() {
        if (tasksToAssign == 0) {
            MainApp.showAlert(Alert.AlertType.ERROR,
                    "Немає необхідності робити запит! ",
                    "Дане завдання уже розподілено між робітниками!",
                    "");
            return;
        }
        List<Person> persons = personDAO.findAvailableWorkers();
        AssignedTask task = mainApp.showAssignTaskDialog(persons,
                tasksToAssign, stage);
        if (task != null) {
            tasksToAssign -= task.getAmount();
            updateTaskToAssignField();
            task.setTask(this.task);
            assignedTaskDAO.addAssignedTask(task);
            taskList.add(task);
            loadData();
        }
    }

    public void handleRemoveWorker() {
        AssignedTask task =
                taskTableView.getSelectionModel().getSelectedItem();
        if (task == null) {
            MainApp.showAlert(Alert.AlertType.ERROR, "",
                    "Не вибрано жодного робітника",
                    "Виберіть, будь ласка, робітника!");
        } else {
            Date currentDate = new Date();
            if (!task.getFinished()) {
                task.setFinishDate(currentDate);
                taskList.remove(task);
                assignedTaskDAO.updateAssignedTask(task);
                tasksToAssign += task.getAmount();
            } else {
                MainApp.showAlert(Alert.AlertType.ERROR,
                        "",
                        "",
                        "Неможливо звільнити робітника із завдання," +
                                "яке вже зараховано!");
            }
            updateTaskToAssignField();
        }
    }

    public void handleSetFinished() {
        AssignedTask task =
                taskTableView.getSelectionModel().getSelectedItem();
        if (task == null) {
            MainApp.showAlert(Alert.AlertType.ERROR, "",
                    "Не вибрано жодного робітника",
                    "Виберіть, будь ласка, робітника");
        } else {
            if (!task.getFinished()) {
                task.setFinished(true);
                task.setFinishDate(new Date());
                assignedTaskDAO.updateAssignedTask(task);
                loadData();
            }
        }
    }

    public void handleShowProfile() {
        AssignedTask task =
                taskTableView.getSelectionModel().getSelectedItem();
        if (task == null) {
            MainApp.showAlert(Alert.AlertType.ERROR, "",
                    "Не вибрано жодного робітника",
                    "Виберіть, будь ласка, робітників");
            return;
        }
        mainApp.showProfile(task.getPerson());
    }

    public void handleMakeRequest() {
        updateRequest();
        if (request != null) {
            MainApp.showAlert(Alert.AlertType.ERROR,"",
                    "Запит вже зроблено!",
                    "Відмініть, будь ласка, запит, щоб зробити новий");
            return;
        }
        if (tasksToAssign == 0) {
            MainApp.showAlert(Alert.AlertType.ERROR,
                    "Немає необхідності робити запит! ",
                    "Дане завдання уже розподілено між робітниками!",
                    "");
            return;
        }
        mainApp.createRequestDialog(stage, task);
    }

    public void handleCancelRequest() {
        updateRequest();
        if (request == null) {
            MainApp.showAlert(Alert.AlertType.ERROR, "",
                    "Запиту не зроблено!", "");
            return;
        }
        request.setCanceled(true);
        RequestDAO requestDAO = new RequestDAO();
        requestDAO.update(request);
        request = null;
    }

    public void updateRequest() {
        RequestDAO requestDAO = new RequestDAO();
        request = requestDAO.findCurrentRequestByTaskID(task.getIdentifier());
    }

    public void loadData() {
        List<AssignedTask> list = assignedTaskDAO.findAssignedTasks(
                task.getIdentifier());
        tasksToAssign = task.getAmount();
        for (AssignedTask task : list) {
            tasksToAssign -= task.getAmount();
        }
        updateTaskToAssignField();
        taskList = FXCollections.observableList(list);
        taskTableView.setItems(taskList);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("set not null task");
        }
        this.task = task;
        updateRequest();
        creationDateField.setText(DateUtil.toString(task.getCreateDate()));
        deadlineField.setText(DateUtil.toString(task.getDeadline()));
        nameOfJobTextField.setText(task.getPrimaryTask().getName());
        costField.setText(task.getPrimaryTask().getCost().toString());
        timeToCompleteField.setText(TimeUtil.toString(
                task.getPrimaryTask().getTimeToComplete()));
        descriptionTextArea.setText(task.getPrimaryTask().getDescription());
    }

    private void showDescription(AssignedTask task) {
        if (task == null) {
            commentsTextArea.setText("");
        } else {
            commentsTextArea.setText(task.getDescription());
        }
    }

    private void updateTaskToAssignField() {
        tasksToAssignField.setText(Integer.toString(tasksToAssign));
    }
}
