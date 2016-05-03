package control.manager;

import DAO.PrimaryTaskDAO;
import app.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.PrimaryTask;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import util.HibernateUtil;
import view.ViewParameters;

import java.sql.Time;
import java.util.List;

public class PrimaryTaskTableViewController {
    private Stage primaryTaskStage;
    private Session session;
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
    private TableColumn<PrimaryTask, String> descriptionColumn;
    @FXML
    private TextField filterField;

    @FXML
    private TextArea descriptionTextArea;

    public PrimaryTaskTableViewController() {
        session = HibernateUtil.getSession();
        this.primaryTaskDAO = new PrimaryTaskDAO(session);
        loadDataFromDB();
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        costColumn.setCellValueFactory(
                new PropertyValueFactory<>("cost"));

        timeToCompleteColumn.setCellValueFactory(
                new PropertyValueFactory<>("timeToComplete"));

        descriptionColumn.setCellValueFactory(
                new PropertyValueFactory<>("description"));
        descriptionColumn.setCellFactory(new Callback<TableColumn<PrimaryTask, String>,
                        TableCell<PrimaryTask, String>>() {
            @Override
            public TableCell<PrimaryTask, String> call(TableColumn<PrimaryTask,
                    String> param) {
                final TableCell<PrimaryTask, String> cell =
                        new TableCell<PrimaryTask, String>(){
                            private Text text;
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (!isEmpty()) {
                                    text = new Text();
                                    text.setWrappingWidth(
                                            ViewParameters.descriptionCellWidth);
                                    String shortText;
                                    if (ViewParameters.descriptionLength < item.length()) {
                                        shortText = item.substring(0,
                                                ViewParameters.descriptionLength)
                                                + "...";
                                    } else {
                                        shortText = item;
                                    }
                                    text.setText(shortText);
                                    setGraphic(text);
                                } else {
                                    setGraphic(null);
                                }
                            }
                        };
                return cell;
            }
        });
        descriptionTextArea.setEditable(false);
        showDescription(null);
        primaryTaskTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDescription(newValue));
        createFilter();
    }

    public void handleAdd() {
        mainapp.showPrimaryTaskEditDialog(this);
    }

    public boolean addPrimaryTask(PrimaryTask primaryTask)
            throws ConstraintViolationException {
        if (primaryTaskDAO.add(primaryTask)) {
            observableList.add(primaryTask);
            return true;
        }
        return false;
    }

    public void handleExit() {
        session.close();
        primaryTaskStage.close();
    }

    public void setPrimaryTaskStage(Stage primaryTaskStage) {
        this.primaryTaskStage = primaryTaskStage;
        primaryTaskStage.setOnCloseRequest((windowEvent) -> session.close());
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
