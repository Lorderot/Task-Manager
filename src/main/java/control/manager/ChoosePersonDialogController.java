package control.manager;

import app.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Person;

import java.util.Date;
import java.util.List;

public class ChoosePersonDialogController {
    private MainApp mainApp;
    private Stage stage;
    private Person chosenPerson;
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
    private TextField filterField;

    public ChoosePersonDialogController() {
    }

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));
        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("dateIn"));
    }

    public void handleExit() {
        stage.close();
    }

    public void handleAssignWorker() {
        Person person = personTableView.getSelectionModel()
                .selectedItemProperty().get();
        if (person == null) {
            MainApp.showAlert(Alert.AlertType.ERROR, "",
                    "Не вибрано робітника!",
                    "Виберіть, будь ласка, робітника!");
        } else {
            chosenPerson = person;
            handleExit();
        }
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

    public Person getChosenPerson() {
        return chosenPerson;
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
