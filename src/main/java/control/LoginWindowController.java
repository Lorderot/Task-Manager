package control;

import DAO.PersonDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Person;
import model.UserType;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import util.HibernateUtil;

public class LoginWindowController {
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
    }

    public Person getPerson() {
        return person;
    }

    public void handleOk() {
        Person person = authentication();
        if (person != null) {
            HibernateUtil.buildSessionFactory(person.getUserType());
            this.person = person;
            loginStage.close();
        }
    }

    public void handleCancel() {
        loginStage.close();
    }

    private Person authentication() {
        try {
            HibernateUtil.buildSessionFactory(UserType.ADMIN);
        } catch (JDBCConnectionException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Доступ до інтернет ресурсів");
            alert.setHeaderText("Проблеми зі з'єднанням");
            alert.setContentText("Підключіться, будь ласка, до інтернету");
            alert.showAndWait();
            return null;
        }
        Session session = HibernateUtil.getSession();
        PersonDAO personDAO = new PersonDAO(session);
        Person person = personDAO.findPersonByLogin(userNameField.getText());
        session.close();
        if (person != null) {
            if (person.getPassword().equals(passwordField.getText())) {
                return person;
            }
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Авторизація");
        alert.setHeaderText("Будь ласка, введіть вірні ім'я користувача та пароль");
        alert.setContentText("Невірні ім'я користувача або пароль");

        alert.showAndWait();
        return null;
    }
}
