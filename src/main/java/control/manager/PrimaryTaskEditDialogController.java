package control.manager;

import DAO.PrimaryTaskDAO;
import DAO.SkillDAO;
import app.MainApp;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.PrimaryTask;
import model.Skill;
import org.hibernate.exception.ConstraintViolationException;
import util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class PrimaryTaskEditDialogController {
    private PrimaryTask primaryTask;
    private Stage primaryTaskEditDialogStage;
    private ObservableList<Skill> observableList;
    private PrimaryTaskDAO primaryTaskDAO;
    private SkillDAO skillDAO;
    private List<Skill> chosenSkills;

    @FXML
    private TableView<Skill> skillTableView;
    @FXML
    private TableColumn<Skill, String> nameColumn;
    @FXML
    private TableColumn<Skill, Boolean> checkBoxColumn;
    @FXML
    private TextArea skillDescriptionTextArea;
    @FXML
    private TextField nameField;
    @FXML
    private TextField costField;
    @FXML
    private TextField timeToCompleteField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextField filterField;

    public PrimaryTaskEditDialogController() {
        primaryTaskDAO = new PrimaryTaskDAO();
        skillDAO = new SkillDAO();
        chosenSkills = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        timeToCompleteField.setPromptText(TimeUtil.TIME_FORMAT);
        primaryTask = new PrimaryTask();
        descriptionTextArea.setWrapText(true);
        skillDescriptionTextArea.setWrapText(true);
        descriptionTextArea.setEditable(true);
        skillDescriptionTextArea.setEditable(false);
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        checkBoxColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().getChecked()));
        checkBoxColumn.setCellFactory(skill -> new CheckBoxCell());
        checkBoxColumn.setEditable(true);
        skillTableView.setEditable(true);
        skillTableView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                        showDescription(newValue));

    }

    public void handleOk() {
        if (isInputValid()) {
            chosenSkills.clear();
            observableList.forEach(skill -> {
                if (skill.getChecked()) {
                    chosenSkills.add(skill);
                }
            });
            if (chosenSkills.size() == 0) {
                MainApp.showAlert(Alert.AlertType.ERROR, "",
                        "Не вибрано жодного вміння! ",
                        "Виберіть, будь ласка, необхідні для виконання роботи вміння");
                return;
            }
            primaryTask.setName(nameField.getText());
            primaryTask.setCost(Integer.parseInt(costField.getText()));
            primaryTask.setTimeToComplete(TimeUtil.fromString(
                    timeToCompleteField.getText()).getTime());
            primaryTask.setDescription(descriptionTextArea.getText());
            try {
                if (addPrimaryTask()) {
                    primaryTaskEditDialogStage.close();
                } else {
                    MainApp.showAlert(Alert.AlertType.ERROR,
                            "Запис не було додано! ",
                            "Запис з пустими полями або недодатньою оплатою" +
                                    " неможливо додати! ",
                            "Заповніть, будь ласка, пусті поля");
                }
            } catch (ConstraintViolationException e) {
                MainApp.showAlert(Alert.AlertType.ERROR,
                        "Немождиво додати запис",
                        "Запис з даним іменем існує",
                        "Будь ласка, зробіть ім'я унікальним");
            } catch (Exception e) {
                e.printStackTrace();
                MainApp.showAlert(Alert.AlertType.INFORMATION,
                        "Невідома проблема",
                        "Запис не було додано",
                        "");
            }
        }
    }

    public void handleCancel() {
        primaryTask = null;
        primaryTaskEditDialogStage.close();
    }

    public void setPrimaryTaskEditDialogStage(Stage primaryTaskEditDialogStage) {
        this.primaryTaskEditDialogStage = primaryTaskEditDialogStage;
        primaryTaskEditDialogStage.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleOk();
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                handleCancel();
            }
        });
    }

    public void loadData() {
        List<Skill> list = skillDAO.findAllSkills();
        observableList = FXCollections.observableList(list);
        createFilter();
    }

    public PrimaryTask getPrimaryTask() {
        return primaryTask;
    }

    private boolean addPrimaryTask()
            throws ConstraintViolationException{
        if (primaryTaskDAO.add(primaryTask)) {
            primaryTask = primaryTaskDAO.findPrimaryTaskByName(primaryTask.getName());
            chosenSkills.forEach(skill ->  skillDAO.addSkillsToPrimaryTasks(
                    skill.getIdentifier(), primaryTask.getIdentifier()));
            return true;
        }
        return false;

    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (timeToCompleteField == null) {
            errorMessage += "Час виконання не задано! ";
        } else {
            try {
                TimeUtil.fromString(timeToCompleteField.getText());
            } catch (Exception e) {
                errorMessage += "Не вірний формат задання часу! (hh:mm:ss) ";
                e.printStackTrace();
            }
        }
        if (costField == null) {
            errorMessage += "Оплата не задана! ";
        } else {
            try {
                Integer.parseInt((costField.getText()));
            } catch (Exception e) {
                errorMessage += "Не вірний формат задання оплати ";
                e.printStackTrace();
            }
        }
        if (errorMessage.equals("")) {
            return true;
        }
        MainApp.showAlert(Alert.AlertType.ERROR, "Помилка",
                "Не коректні дані", errorMessage);
        return false;
    }

    private void createFilter() {
        FilteredList<Skill> filteredData =
                new FilteredList<>(observableList, p -> true);
        filterField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredData.setPredicate(skill -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCasedNewValue = newValue.toLowerCase();
                if (skill.getName().toLowerCase().contains(lowerCasedNewValue)) {
                    return true;
                }
                return false;
            });
        }));
        SortedList<Skill> sortedData =
                new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(skillTableView.comparatorProperty());
        skillTableView.setItems(sortedData);
    }

    private void showDescription(Skill skill) {
        if (skill == null) {
            skillDescriptionTextArea.setText("");
        } else {
            skillDescriptionTextArea.setText(skill.getDescription());
        }
    }
}

class CheckBoxCell extends TableCell<Skill, Boolean> {
    CheckBox checkbox;

    @Override
    protected void updateItem(Boolean arg0, boolean arg1) {
        super.updateItem(arg0, arg1);
        if (!isEmpty()) {
            paintCell();
        }
    }

    private void paintCell() {
        if (checkbox == null) {
            checkbox = new CheckBox();
            checkbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov,
                                    Boolean old_val, Boolean new_val) {
                    setItem(new_val);
                    (getTableView().getItems().get(getTableRow()
                            .getIndex())).setChecked(new_val);
                }
            });
        }
        checkbox.setSelected(getValue());
        setText(null);
        setGraphic(checkbox);
    }

    private Boolean getValue() {
        return getItem() == null ? false : getItem();
    }
}