package control.manager;

import DAO.PrimaryTaskDAO;
import app.MainApp;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.PrimaryTask;

import java.sql.Time;
import java.util.List;

public class PrimaryTaskTableViewController {
    private Stage primaryTaskStage;
    private MainApp mainapp;
    private ObservableList<PrimaryTask> observableList;
    private PrimaryTaskDAO primaryTaskDAO;

    @FXML
    private TableView<PrimaryTask> primaryTaskTable;
    @FXML
    private TableColumn<PrimaryTask, String> nameColumn;
    @FXML
    private TableColumn<PrimaryTask, Integer> costColumn;
    @FXML
    private TableColumn<PrimaryTask, Time> timeToCompleteColumn;
    @FXML
    private TextField filterField;

    @FXML
    private TextArea descriptionTextArea;

    public PrimaryTaskTableViewController() {
        this.primaryTaskDAO = new PrimaryTaskDAO();
        loadDataFromDB();
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        costColumn.setCellValueFactory(
                new PropertyValueFactory<>("cost"));

        timeToCompleteColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getTime()));
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setWrapText(true);
        showDescription(null);
        primaryTaskTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDescription(newValue));
        createFilter();
    }

    public void handleAdd() {
        PrimaryTask primaryTask =
                mainapp.showPrimaryTaskEditDialog(this);
        if (primaryTask != null) {
            observableList.add(primaryTask);
        }
    }

    public void handleExit() {
        primaryTaskStage.close();
    }

    public void setPrimaryTaskStage(Stage primaryTaskStage) {
        this.primaryTaskStage = primaryTaskStage;
    }

    public Stage getPrimaryTaskStage() {
        return primaryTaskStage;
    }

    public void setMainApp(MainApp mainapp) {
        this.mainapp = mainapp;
    }


    public void loadDataFromDB() {
        List<PrimaryTask> list = primaryTaskDAO.findAll();
        observableList = FXCollections.observableArrayList(list);
    }

    public void updateData() {
        loadDataFromDB();
        String refresh = filterField.getText();
        createFilter();
        filterField.setText("");
        filterField.setText(refresh);
    }

    private void createFilter() {
        FilteredList<PrimaryTask> filteredData =
                new FilteredList<>(observableList, p -> true);
        filterField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredData.setPredicate(primaryTask -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                if (primaryTask.getName().toLowerCase()
                        .contains(newValue.toLowerCase())) {
                    return true;
                }
                return false;
            });
        }));
        SortedList<PrimaryTask> sortedData =
                new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(primaryTaskTable.comparatorProperty());
        primaryTaskTable.setItems(sortedData);
    }

    private void showDescription(PrimaryTask primaryTask) {
        if (primaryTask != null) {
            descriptionTextArea.setText(primaryTask.getDescription());
        } else {
            descriptionTextArea.setText("");
        }
    }

}
