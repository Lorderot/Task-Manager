package control.manager;

import DAO.PersonDAO;
import app.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Person;
import org.hibernate.Session;
import util.HibernateUtil;

public class ManagerMainWindowController {
    private Session session = HibernateUtil.getSession();
    private PersonDAO personDAO;
    private Person person;
    private Stage mainStage;
    private MainApp mainApp;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label userTypeLabel;

    public ManagerMainWindowController() {
        personDAO = new PersonDAO(session);
    }

    @FXML
    public void initialize() {
    }

    public void handlePrimaryTasks() {
        mainApp.showPrimaryTask();
    }

    public void handlePlanes() {
        mainApp.showPlane();
    }

    public void handleChangePassword() {
        String newPassword = mainApp.showPasswordEditDialog(
                person.getPassword());
        if (newPassword != null && !newPassword.equals("")) {
            person.setPassword(newPassword);
            personDAO.updatePersonPassword(person);
        }
    }

    public void handleShowProfile() {
        mainApp.showProfile(person);
    }

    public void handleExit() {
        session.close();
        HibernateUtil.shutDown();
        mainStage.close();
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setPerson(Person person) {
        this.person = person;
        firstNameLabel.setText(person.getFirstName());
        lastNameLabel.setText(person.getLastName());
        userTypeLabel.setText(person.getUserType().toString());
    }
}
