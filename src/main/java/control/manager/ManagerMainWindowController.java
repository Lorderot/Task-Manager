package control.manager;

import DAO.PersonDAO;
import DAO.ProblemDAO;
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
import model.Person;
import model.Problem;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.Date;
import java.util.List;

public class ManagerMainWindowController {
    private Session session;
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
    private TableColumn<Problem, Date> updateField;
    @FXML
    private TableColumn<Problem, Date> deadlineField;
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
        session = HibernateUtil.getSession();
        personDAO = new PersonDAO(session);
        problemDAO = new ProblemDAO(session);
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
                new SimpleObjectProperty<>(cellData.getValue().getUpdateDate()));
        deadlineField.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDeadline()));
        isFinishedField.setCellValueFactory(cellData -> {
            if (cellData.getValue().getSolved()) {
                return new SimpleStringProperty("Так");
            } else {
                return new SimpleStringProperty("Ні");
            }
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
        session.close();
        HibernateUtil.shutDown();
        mainStage.close();
    }

    public void loadData() {
        loadProblemsByPersonFromDB();
        createFilter();
    }

    public void updateData() {
        session.close();
        session = HibernateUtil.getSession();
        problemDAO.setSession(session);
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
        problemTableView.setItems(sortedList);
    }

}
