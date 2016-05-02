package control;

import app.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class PasswordEditDialogController {
    private Stage passwordStage;
    private String oldPassword;
    private String password;
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    public void handleOk() {
        String errorMessage = "";
        if (!oldPasswordField.getText().equals(oldPassword)) {
            errorMessage += "Невірний пароль! ";
        }
        if (newPasswordField.getText().equals("")) {
            errorMessage += "Некоректний новий пароль!";
        }
        if (!newPasswordField.getText().equals(
                confirmPasswordField.getText())) {
            errorMessage += "Паролі не співпадають! ";
        }
        if (errorMessage.equals("")) {
            password = newPasswordField.getText();
            passwordStage.close();
        } else {
            MainApp.showAlert(Alert.AlertType.ERROR, "", errorMessage, "");
        }
    }

    public void handleCancel() {
        passwordStage.close();
    }

    public void setPasswordStage(Stage passwordStage) {
        this.passwordStage = passwordStage;
        passwordStage.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleOk();
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                handleCancel();
            }
        });
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }
}
