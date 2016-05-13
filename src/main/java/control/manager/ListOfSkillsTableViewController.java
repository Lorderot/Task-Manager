package control.manager;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Skill;

import java.util.List;

public class ListOfSkillsTableViewController {
    private Stage stage;
    private ObservableList<Skill> observableList;
    @FXML
    private TableView<Skill> tableView;
    @FXML
    private TableColumn<Skill, String> nameColumn;
    @FXML
    private TextField filterField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getName()));
        descriptionTextArea.setEditable(false);
        tableView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue)
                        -> showDescription(newValue));
        descriptionTextArea.setWrapText(true);
    }

    public void handleExit() {
        stage.close();
    }

    public void setSkills(List<Skill> list) {
        observableList = FXCollections.observableArrayList(list);
        createFilter();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void createFilter() {
        FilteredList<Skill> filteredData =
                new FilteredList<>(observableList, p -> true);
        filterField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredData.setPredicate(skill -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCasedNewValue = newValue.toLowerCase();
                if (skill.getName().contains(lowerCasedNewValue)) {
                    return true;
                }
                return false;
            });
        }));
        SortedList<Skill> sortedData =
                new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

    private void showDescription(Skill skill) {
        if (skill != null) {
            descriptionTextArea.setText(skill.getDescription());
        } else {
            descriptionTextArea.setText("");
        }

    }
}
