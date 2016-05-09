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
import model.UserType;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import util.HibernateUtil;

public class LoginWindowController {
    private Session session;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;

    private Stage loginStage;
    private Person person;

    public LoginWindowController() {
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
        Person person = authentication();
        if (person != null) {
            session.close();
            HibernateUtil.shutDown();
            HibernateUtil.buildSessionFactory(person.getUserType());
            this.person = person;
            loginStage.close();
        }
    }

    public void handleExit() {
        if (session != null) {
            session.close();
        }
        loginStage.close();
    }

    private Person authentication() {
        try {
            if (HibernateUtil.isFactoryClosed()) {
                HibernateUtil.buildSessionFactory(UserType.ADMIN);
            }
        } catch (JDBCConnectionException e) {
            e.printStackTrace();
            MainApp.showAlert(Alert.AlertType.ERROR,
                    "Доступ до інтернет ресурсів", "Проблеми зі з'єднанням",
                    "Підключіться, будь ласка, до інтернету");
            return null;
        }
        if (session == null) {
            session = HibernateUtil.getSession();
        }
        PersonDAO personDAO = new PersonDAO(session);
        Person person = personDAO.findPersonByLogin(userNameField.getText());
        if (person != null) {
            if (person.getPassword().equals(passwordField.getText())) {
                return person;
            }
        }
        MainApp.showAlert(Alert.AlertType.ERROR,
                "Авторизація",
                "Будь ласка, введіть вірні ім'я користувача та пароль",
                "Невірні ім'я користувача або пароль");
        return null;
    }
}
