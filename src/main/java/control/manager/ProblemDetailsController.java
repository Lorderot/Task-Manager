package control.manager;

import DAO.TaskDAO;
import app.MainApp;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
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
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.Date;
import java.util.List;

public class ProblemDetailsController {
    private Stage stage;
    private MainApp mainApp;
    private Problem problem;
    private Session session;
    private TaskDAO taskDAO;
    private ObservableList<Task> observableList;
    @FXML
    private TableView<Task> taskTableView;
    @FXML
    private TableColumn<Task, String> nameTaskColumn;
    @FXML
    private TableColumn<Task, Date> createDateColumn;
    @FXML
    private TableColumn<Task, Date> deadlineColumn;
    @FXML
    private TableColumn<Task, Integer> priorityColumn;
    @FXML
    private TableColumn<Task, Integer> amountColumn;
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    @FXML
    private TextArea problemDescriptionTextArea;
    @FXML
    private TextArea taskDescriptionTextArea;
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
    public void initialize() {
        nameTaskColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue()
                        .getPrimaryTask().getName()));
        createDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getCreateDate()));
        deadlineColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDeadline()));
        priorityColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getPriority()));
        amountColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getAmount()));
        descriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));
        problemNameField.setEditable(false);
        createDateField.setEditable(false);
        deadlineField.setEditable(false);
        taskDescriptionTextArea.setEditable(false);
        problemDescriptionTextArea.setEditable(false);
        showDescription(null);
        taskTableView.getSelectionModel().selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> {
                    showDescription(newValue);
                }));
    }

    public ProblemDetailsController() {
        session = HibernateUtil.getSession();
        taskDAO = new TaskDAO(session);
    }

    public void updateData() {
        session.close();
        session = HibernateUtil.getSession();
        taskDAO.setSession(session);
        loadData();
        createFilter();
        String refresh = filterField.getText();
        filterField.setText("");
        filterField.setText(refresh);
    }

    public void handleExit() {
        session.close();
        stage.close();
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
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
        priorityLabel.setText(problem.getPriority().toString());
        createDateField.setText(problem.getCreateDate().toString());
        deadlineField.setText(problem.getDeadline().toString());
        problemNameField.setText(problem.getName());
        problemDescriptionTextArea.setText(problem.getDescription());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private void showDescription(Task task) {
        if (task == null) {
            taskDescriptionTextArea.setText("");
        } else {
            taskDescriptionTextArea.setText(task.getPrimaryTask()
                    .getDescription());
        }
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
        taskTableView.setItems(sortedList);
    }
}
