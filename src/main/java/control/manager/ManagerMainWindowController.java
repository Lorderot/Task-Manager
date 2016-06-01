package control.manager;

import DAO.PersonDAO;
import DAO.ProblemDAO;
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
import model.Person;
import model.Problem;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.hibernate.Session;
import util.DateUtil;
import util.HibernateUtil;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerMainWindowController {
    private PersonDAO personDAO;
    private ProblemDAO problemDAO;
    private Person person;
    private Stage mainStage;
    private MainApp mainApp;
    private ObservableList<Problem> observableList;

    @FXML
    private TableView<Problem> problemTableView;
    @FXML
    private TableColumn<Problem, String> firstNameCreatorField;
    @FXML
    private TableColumn<Problem, String> lastNameCreatorField;
    @FXML
    private TableColumn<Problem, String> planeNameField;
    @FXML
    private TableColumn<Problem, String> problemNameField;
    @FXML
    private TableColumn<Problem, Integer> priorityField;
    @FXML
    private TableColumn<Problem, String> updateField;
    @FXML
    private TableColumn<Problem, String> deadlineField;
    @FXML
    private TableColumn<Problem, String> isFinishedField;
    @FXML
    private TextField filterField;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label userTypeLabel;

    public ManagerMainWindowController() {
        personDAO = new PersonDAO();
        problemDAO = new ProblemDAO();
    }

    @FXML
    public void initialize() {
        firstNameCreatorField.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue()
                        .getCreator().getFirstName()));
        lastNameCreatorField.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue()
                        .getCreator().getLastName()));
        planeNameField.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue()
                    .getPlane().getName()));
        problemNameField.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        priorityField.setCellValueFactory(cellData ->
               new ReadOnlyObjectWrapper<>(cellData.getValue().getPriority()));
        updateField.setCellValueFactory(cellData ->
                new SimpleStringProperty(DateUtil.toString(
                        cellData.getValue().getUpdateDate())));
        deadlineField.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        DateUtil.toString(cellData.getValue().getDeadline())));
        isFinishedField.setCellValueFactory(cellData -> {
            Boolean isSolved = cellData.getValue().getSolved();
            if (isSolved == null) {
                return new SimpleStringProperty("");
            }
            String solved;
            if (isSolved) {
                solved = "Так";
            } else {
                solved = "Ні";
            }
            return new SimpleStringProperty(solved);
        });
    }

    public void handlePrimaryTasks() {
        mainApp.showPrimaryTaskTable();
    }

    public void handlePlanes() {
        mainApp.showPlaneTable();
    }

    public void handleChangePassword() {
        String newPassword = mainApp.showPasswordEditDialog(
                person.getPassword());
        if (newPassword != null && !newPassword.equals("")) {
            person.setPassword(newPassword);
            personDAO.updatePersonPassword(person);
        }
    }

    public void handleShowProfile() {
        mainApp.showProfile(person);
    }

    public void handleChooseProblem() {
        Problem selectedProblem = problemTableView.getSelectionModel()
                .selectedItemProperty().get();
        if (selectedProblem == null) {
            MainApp.showAlert(Alert.AlertType.ERROR, "",
                    "Не вибрано проект!",
                    "Виберіть, будь ласка, проект!");
        } else {
            mainApp.showProblemDetails(selectedProblem);
        }
    }

    public void handleExit() {
        HibernateUtil.shutDown();
        mainStage.close();
    }

    public void handleCreateProblemReport() {
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
                    this.getClass().getResource("/reports/reportProblems.jasper"));
            Session session = HibernateUtil.getSession();
            Connection connection = HibernateUtil.getConnection(session);
            Map parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, parameters, connection);
            mainApp.showJasperViewer(jasperPrint, mainStage);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        loadProblemsByPersonFromDB();
        createFilter();
    }

    public void updateData() {
        loadData();
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setPerson(Person person) {
        this.person = person;
        firstNameLabel.setText(person.getFirstName());
        lastNameLabel.setText(person.getLastName());
        userTypeLabel.setText(person.getUserType().toString());
    }

    private void loadProblemsByPersonFromDB() {
        List<Problem> list = problemDAO
                .findProblemsByAssignedPerson(person.getIdentifier());
        observableList = FXCollections.observableArrayList(list);
    }

    private void createFilter() {
        FilteredList<Problem> filteredList = new FilteredList<>(observableList, p->true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(problem -> {
                if (newValue == null) {
                    return true;
                }
                String lowerCasedNewValue = newValue.toLowerCase();
                if (problem.getName().toLowerCase()
                        .contains(lowerCasedNewValue)) {
                    return true;
                }
                if (problem.getCreator().getFirstName().toLowerCase()
                        .contains(lowerCasedNewValue)) {
                    return true;
                }
                if (problem.getCreator().getLastName().toLowerCase()
                        .contains(lowerCasedNewValue)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Problem> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(problemTableView.comparatorProperty());
        problemTableView.setItems(sortedList);
    }

}
