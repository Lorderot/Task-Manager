package control;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Person;

public class ProfileController {
    private Stage profileStage;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField birthdayField;
    @FXML
    private TextArea educationTextArea;
    @FXML
    private TextField employmentDateField;
    @FXML
    private TextField telephoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField skypeField;

    @FXML
    public void initialize() {
        firstNameField.setEditable(false);
        lastNameField.setEditable(false);
        birthdayField.setEditable(false);
        educationTextArea.setEditable(false);
        employmentDateField.setEditable(false);
        telephoneNumberField.setEditable(false);
        emailField.setEditable(false);
        skypeField.setEditable(false);
    }

    public void handleOk() {
        profileStage.close();
    }

    public void setPerson(Person person) {
        firstNameField.setText(person.getFirstName());
        lastNameField.setText(person.getLastName());
        birthdayField.setText(person.getBirthday().toString());
        educationTextArea.setText(person.getEducation());
        employmentDateField.setText(person.getDateIn().toString());
        telephoneNumberField.setText(person.getPhone_number());
        emailField.setText(person.getEmail());
        skypeField.setText(person.getSkype());
    }

    public void setProfileStage(Stage profileStage) {
        this.profileStage = profileStage;
        profileStage.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleOk();
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                handleOk();
            }
        });
    }
}
