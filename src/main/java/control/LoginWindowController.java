package control;

import DAO.PersonDAO;
import app.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Person;
import org.hibernate.exception.JDBCConnectionException;
import util.HibernateUtil;

public class LoginWindowController {
    private PersonDAO personDAO;
    private Stage loginStage;
    private Person person;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;

    public LoginWindowController() {
        personDAO = new PersonDAO();
    }

    @FXML
    private void initialize() {
        userNameField.setPromptText("Ваш логін");
        passwordField.setPromptText("Ваш пароль");
    }

    public void setLoginStage(Stage loginStage) {
        this.loginStage = loginStage;
        loginStage.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                handleExit();
            }
        });
    }

    public Person getPerson() {
        return person;
    }

    public void handleLogin() {
        Person person;
        try {
            HibernateUtil.buildSessionFactory(userNameField.getText().toLowerCase(),
                    passwordField.getText());
            person = personDAO.findPersonByLogin(userNameField.getText());
        } catch (JDBCConnectionException e) {
            e.printStackTrace();
            MainApp.showAlert(Alert.AlertType.ERROR,
                    "Авторизація",
                    "Будь ласка, введіть вірні ім'я користувача та пароль",
                    "Невірні ім'я користувача або пароль");
            return;
        }
        if (person != null) {
            this.person = person;
            person.setPassword(passwordField.getText());
            loginStage.close();
        } else {
            MainApp.showAlert(Alert.AlertType.ERROR,
                    "Авторизація",
                    "Такого користувача не знайдено в базі даних!",
                    "");
            HibernateUtil.shutDown();
        }
    }

    public void handleExit() {
        HibernateUtil.shutDown();
        loginStage.close();
    }
}
