package control.manager;

import DAO.TaskDAO;
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
    private TextArea primaryTaskDescriptionTextArea;
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

    }

    public ProblemDetailsController() {
        Session session = HibernateUtil.getSession();
        taskDAO = new TaskDAO(session);
    }

    public void updateData() {
        session.close();
        session = HibernateUtil.getSession();
        taskDAO.changeSession(session);
        loadData();
    }

    public void loadData() {
        loadDataFromDB();
        createFilter();
    }

    private void loadDataFromDB() {
        List<Task> list = taskDAO.findTasks(problem);
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
                if (task.getPrimaryTask().getName().contains(lowerCasedNewValue)) {
                    return true;
                }
                return false;
            });
        }));
        SortedList<Task> sortedList = new SortedList<>(filteredList);
        taskTableView.setItems(sortedList);
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
