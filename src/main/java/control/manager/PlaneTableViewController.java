package control.manager;

import DAO.PlaneDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Plane;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.List;

public class PlaneTableViewController {
    private Stage planeStage;
    private ObservableList<Plane> observableList
            = FXCollections.observableArrayList();
    @FXML
    private TableView<Plane> planeTableView;
    @FXML
    private TableColumn<Plane, String> nameColumn;
    @FXML
    private TableColumn<Plane, String> typeColumn;
    @FXML
    private TableColumn<Plane, Integer> capacityColumn;
    @FXML
    private TableColumn<Plane, Boolean> technicalStatusColumn;
    @FXML
    private TableColumn<Plane, Boolean> fuelStatusColumn;
    @FXML
    private TableColumn<Plane, Boolean> availabilityColumn;
    @FXML
    private TableColumn<Plane, String> ownerColumn;
    @FXML
    private TextField filterField;

    public PlaneTableViewController() {
        Session session = HibernateUtil.getSession();
        List<Plane> list = new PlaneDAO(session).findAll();
        session.close();
        observableList.addAll(list);
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(
                new PropertyValueFactory<>("type"));
        capacityColumn.setCellValueFactory(
                new PropertyValueFactory<>("capacity"));
        technicalStatusColumn.setCellValueFactory(
                new PropertyValueFactory<>("technicalStatus"));
        fuelStatusColumn.setCellValueFactory(
                new PropertyValueFactory<>("fuelStatus"));
        availabilityColumn.setCellValueFactory(
                new PropertyValueFactory<>("availability"));
        ownerColumn.setCellValueFactory(
                new PropertyValueFactory<>("owner"));
        FilteredList<Plane> filteredList = new FilteredList<>(
                observableList, plane -> true);
        filterField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate(plane -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                if (plane.getName().toLowerCase()
                        .contains(newValue.toLowerCase())) {
                    return true;
                }
                if (plane.getOwner().toLowerCase()
                        .contains(newValue.toLowerCase())) {
                    return true;
                }
                if (plane.getType().toLowerCase()
                        .contains(newValue.toLowerCase())) {
                    return true;
                }
                return false;
            });
        }));
        SortedList<Plane> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(planeTableView.comparatorProperty());
        planeTableView.setItems(sortedList);
        observableList = sortedList;
    }

    public void handleOk() {
        planeStage.close();
    }

    public void setPlaneStage(Stage planeStage) {
        this.planeStage = planeStage;
        planeStage.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER
                    || event.getCode() == KeyCode.ESCAPE) {
                handleOk();
            }
        });
    }
}
