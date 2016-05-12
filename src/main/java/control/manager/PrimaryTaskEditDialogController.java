package control.manager;

import app.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.PrimaryTask;
import org.hibernate.exception.ConstraintViolationException;
import util.TimeUtil;

public class PrimaryTaskEditDialogController {
    private PrimaryTask primaryTask;
    private Stage primaryTaskEditDialogStage;
    private PrimaryTaskTableViewController controller;
    private boolean okClicked;

    @FXML
    private TextField nameField;
    @FXML
    private TextField costField;
    @FXML
    private TextField timeToCompleteField;
    @FXML
    private TextArea descriptionTextArea;

    @FXML
    public void initialize() {
        timeToCompleteField.setPromptText(TimeUtil.TIME_FORMAT);
        primaryTask = new PrimaryTask();
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setController(PrimaryTaskTableViewController controller) {
        this.controller = controller;
    }

    public void handleOk() {
        if (isInputValid()) {
            primaryTask.setName(nameField.getText());
            primaryTask.setCost(Integer.parseInt(costField.getText()));
            primaryTask.setTimeToComplete(TimeUtil.fromString(
                    timeToCompleteField.getText()).getTime());
            primaryTask.setDescription(descriptionTextArea.getText());
            try {
                if (controller.addPrimaryTask(primaryTask)) {
                    okClicked = true;
                    primaryTaskEditDialogStage.close();
                } else {
                    MainApp.showAlert(Alert.AlertType.ERROR,
                            "Запис не було додано",
                            "Запис з пустими полями неможливо додати",
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
}
