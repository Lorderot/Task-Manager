package control.manager;

import DAO.RequestDAO;
import app.MainApp;
import javafx.application.Platform;
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
import model.Request;
import model.Skill;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestTableViewController {
    private Stage requestStage;
    private MainApp mainApp;
    private Stage parentStage;
    private RequestDAO requestDAO;
    private Problem problem;
    private ObservableList<Request> observableList;
    @FXML
    private TableView<Request> requestTableView;
    @FXML
    private TableColumn<Request, String> firstNameColumn;
    @FXML
    private TableColumn<Request, String> lastNameColumn;
    @FXML
    private TableColumn<Request, Date> requestDateColumn;
    @FXML
    private TableColumn<Request, Date> respondDateColumn;
    @FXML
    private TableColumn<Request, String> taskNameColumn;
    @FXML
    private TextField filterField;
    @FXML
    private TextArea requestDescriptionTextArea;
    @FXML
    private TextArea respondDescriptionTextArea;

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(cellData -> {
            Person person = cellData.getValue().getPerson();
            if (person != null) {
                return new SimpleStringProperty(cellData.getValue()
                        .getPerson().getFirstName());
            } else {
                return new SimpleStringProperty("");
            }
        });
        lastNameColumn.setCellValueFactory(cellData -> {
            Person person = cellData.getValue().getPerson();
            if (person != null) {
                return new SimpleStringProperty(cellData.getValue()
                        .getPerson().getLastName());
            } else {
                return new SimpleStringProperty("");
            }
        });
        requestDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue()
                        .getDateOfRequest()));
        respondDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue()
                        .getDateOfRespond()));
        taskNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue()
                        .getTask().getPrimaryTask().getName()));
        requestDescriptionTextArea.setEditable(false);
        respondDescriptionTextArea.setEditable(false);
    }

    public RequestTableViewController() {
        requestDAO = new RequestDAO();
    }

    public void handleExit() {
        requestStage.close();
    }

    public void handleMoveToTask() {
        Request request = requestTableView.getSelectionModel()
                .selectedItemProperty().get();
        if (request == null) {
            MainApp.showAlert(Alert.AlertType.ERROR,"",
                    "Запит не було вибрано!",
                    "Виберіть, будь ласка, запит!");
        } else {
            new Thread(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(()-> handleExit());
            }).start();
            mainApp.showTaskDetails(request.getTask(), parentStage);
        }
    }

    public void handleShowProfile() {
        Request request = requestTableView.getSelectionModel()
                .selectedItemProperty().get();
        if (request != null) {
            Person person = request.getPerson();
            if (person == null) {
                MainApp.showAlert(Alert.AlertType.ERROR, "Пусте поле!",
                        "Не знайдено робітника!",
                        "На даний запит остаточної відповіді ще не дано! " +
                                "Виберіть, будь ласка, запит з непустими полями!");
            } else {
                mainApp.showProfile(person);
            }
        } else {
            MainApp.showAlert(Alert.AlertType.ERROR, "",
                    "Не вибрано запит!",
                    "Виберіть, будь ласка, запит!");
        }
    }

    public void updateData() {
        loadRequestsByProblemFromDB();
        createFilter();
        String refresh = filterField.getText();
        filterField.setText("");
        filterField.setText(refresh);
    }

    public void loadData() {
        loadRequestsByProblemFromDB();
        createFilter();
    }

    public void handleShowSkills() {
        Request request = requestTableView.getSelectionModel()
                .selectedItemProperty().get();
        if (request != null) {
            List<Skill> skillList = request.getSkills();
            if (skillList == null) {
                skillList = new ArrayList<>();
            }
            mainApp.showListOfSKills(skillList, requestStage);
        } else {
            MainApp.showAlert(Alert.AlertType.ERROR, "",
                    "Не вибрано запит!",
                    "Виберіть, будь ласка, запит!");
        }
    }

    public void setRequestStage(Stage requestStage) {
        this.requestStage = requestStage;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    private void loadRequestsByProblemFromDB() {
        List<Request> list = requestDAO
                .findRequestsByProblem(problem.getIdentifier());
        observableList = FXCollections.observableArrayList(list);
    }

    private void createFilter() {
        FilteredList<Request> filteredData =
                new FilteredList<>(observableList, p -> true);
        filterField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredData.setPredicate(request -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCasedNewValue = newValue.toLowerCase();
                if (request.getTask().getPrimaryTask()
                        .getName().contains(lowerCasedNewValue)) {
                    return true;
                }
                if (request.getDateOfRequest()
                        .toString().contains(lowerCasedNewValue)) {
                    return true;
                }
                return false;
            });
        }));
        SortedList<Request> sortedData =
                new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(requestTableView.comparatorProperty());
        requestTableView.setItems(sortedData);
    }
}
