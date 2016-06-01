package control.manager;

import DAO.RequestDAO;
import DAO.SkillDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Request;
import model.Skill;
import model.Task;
import util.DateUtil;

import java.util.Date;
import java.util.List;

public class CreateRequestDialogController {
    private Stage stage;
    private ObservableList<Skill> observableList;
    private Task task;
    @FXML
    private TableView<Skill> skillTableView;
    @FXML
    private TableColumn<Skill, String> nameColumn;
    @FXML
    private TextArea skillDescriptionTextArea;
    @FXML
    private TextField taskNameField;
    @FXML
    private TextField requestDateField;
    @FXML
    private TextArea requestDescriptionTextArea;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        skillDescriptionTextArea.setWrapText(true);
        skillDescriptionTextArea.setEditable(false);
        requestDescriptionTextArea.setEditable(true);
        requestDescriptionTextArea.setWrapText(true);
        taskNameField.setEditable(false);
        requestDateField.setEditable(false);
        skillTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDescription(newValue));
    }

    public void handleMakeRequest() {
        RequestDAO requestDAO = new RequestDAO();
        Request request = new Request();
        request.setDescription(requestDescriptionTextArea.getText());
        request.setTask(task);
        request.setDateOfRequest(new Date());
        request.setCanceled(false);
        requestDAO.addRequest(request);
        handleCancel();
    }

    public void handleCancel() {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setTask(Task task) {
        this.task = task;
        taskNameField.setText(task.getPrimaryTask().getName());
        requestDateField.setText(DateUtil.toString(new Date()));
        loadData();
    }

    private void loadData() {
        SkillDAO skillDAO = new SkillDAO();
        List<Skill> skills = skillDAO.getSkillsByPrimaryTask(
                task.getPrimaryTask().getIdentifier());
        observableList = FXCollections.observableArrayList(skills);
        skillTableView.setItems(observableList);
    }

    private void showDescription(Skill skill) {
        if (skill != null) {
            skillDescriptionTextArea.setText(skill.getDescription());
        } else {
            skillDescriptionTextArea.setText("");
        }
    }
}
