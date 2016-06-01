package control.manager;

import DAO.ProblemDAO;
import DAO.TaskDAO;
import app.MainApp;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Problem;
import model.Task;
import util.DateUtil;
import util.TimeUtil;

import java.util.Date;
import java.util.List;

public class ProblemDetailsController {
    private Stage stage;
    private MainApp mainApp;
    private Problem problem;
    private TaskDAO taskDAO;
    private ProblemDAO problemDAO;
    private double progress;
    private long timeToFinish;
    private ObservableList<Task> observableList;
    @FXML
    private TableView<Task> taskTableView;
    @FXML
    private TableColumn<Task, String> nameTaskColumn;
    @FXML
    private TableColumn<Task, String> createDateColumn;
    @FXML
    private TableColumn<Task, String> deadlineColumn;
    @FXML
    private TableColumn<Task, Integer> priorityColumn;
    @FXML
    private TableColumn<Task, Integer> amountColumn;
    @FXML
    private TextArea problemDescriptionTextArea;
    @FXML
    private TextField problemNameField;
    @FXML
    private TextField createDateField;
    @FXML
    private TextField deadlineField;
    @FXML
    private Label priorityLabel;
    @FXML
    private TextField filterField;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TextField timeToFinishTextField;
    @FXML
    public void initialize() {
        nameTaskColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue()
                        .getPrimaryTask().getName()));
        createDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(DateUtil.toString(
                        cellData.getValue().getCreateDate())));
        deadlineColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(DateUtil.toString(
                        cellData.getValue().getDeadline())));
        priorityColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getPriority()));
        amountColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getAmount()));
        problemNameField.setEditable(false);
        createDateField.setEditable(false);
        deadlineField.setEditable(false);
        problemDescriptionTextArea.setEditable(false);
        problemDescriptionTextArea.setWrapText(true);
        timeToFinishTextField.setEditable(false);
    }

    public ProblemDetailsController() {
        taskDAO = new TaskDAO();
        problemDAO = new ProblemDAO();
    }

    public void updateData() {
        loadData();
        createFilter();
        String refresh = filterField.getText();
        filterField.setText("");
        filterField.setText(refresh);
    }

    public void handleExit() {
        stage.close();
    }

    public void handleCreateTask() {
        Task task = mainApp.createTaskDialog(stage);
        if (task != null) {
            task.setProblem(problem);
            task.setCreateDate(new Date());
            taskDAO.addTask(task);
            updateData();
        }
    }

    public void handleShowTaskDetails() {
        Task selectedTask = taskTableView.getSelectionModel()
                .selectedItemProperty().get();
        if (selectedTask == null) {
            MainApp.showAlert(Alert.AlertType.ERROR, "",
                    "Не вибрано завдання!",
                    "Виберіть, будь ласка, завдання!");
        } else {
            mainApp.showTaskDetails(selectedTask, stage);
        }
    }

    public void handleShowProfile() {
        mainApp.showProfile(problem.getCreator());
    }

    public void handleShowPlane() {
        mainApp.showPlane(problem.getPlane(), stage);
    }

    public void handleShowRequests() {
        mainApp.showRequestTable(stage, problem);
    }

    public void loadData() {
        loadDataFromDB();
        createFilter();
        progress = problemDAO.getProgress(problem.getIdentifier());
        progressBar.setProgress(progress);
        timeToFinish = problemDAO.getTime(problem.getIdentifier());
        timeToFinishTextField.setText(TimeUtil
                .toStringSpecialFormat(timeToFinish));
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
        priorityLabel.setText(problem.getPriority().toString());
        createDateField.setText(DateUtil.toString(problem.getCreateDate()));
        deadlineField.setText(DateUtil.toString(problem.getDeadline()));
        problemNameField.setText(problem.getName());
        problemDescriptionTextArea.setText(problem.getDescription());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private void loadDataFromDB() {
        List<Task> list = taskDAO.findTasksByProblem(problem.getIdentifier());
        observableList = FXCollections.observableArrayList(list);
    }

    private void createFilter() {
        FilteredList<Task> filteredList = new FilteredList<>(
                observableList, t -> true);
        filterField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate(task -> {
                if (newValue == null) {
                    return true;
                }
                String lowerCasedNewValue = newValue.toLowerCase();
                if (task.getPrimaryTask().getName().toLowerCase()
                        .contains(lowerCasedNewValue)) {
                    return true;
                }
                return false;
            });
        }));
        SortedList<Task> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(taskTableView.comparatorProperty());
        taskTableView.setItems(sortedList);
    }
}
