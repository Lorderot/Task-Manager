package control;

import DAO.PlaneDAO;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Plane;
import org.hibernate.Session;
import util.HibernateUtil;

public class PlaneController {
    private Plane plane;
    private Stage planeStage;
    private Session session;
    private PlaneDAO planeDAO;
    @FXML
    private TextField nameField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField capacityField;
    @FXML
    private CheckBox availabilityCheckBox;
    @FXML
    private CheckBox technicalStatusCheckBox;
    @FXML
    private CheckBox fuelStatusCheckBox;
    @FXML
    private TextField ownerField;

    @FXML
    public void initialize() {
        nameField.setEditable(false);
        typeField.setEditable(false);
        capacityField.setEditable(false);
        ownerField.setEditable(false);
    }

    public PlaneController() {
        session = HibernateUtil.getSession();
        planeDAO = new PlaneDAO(session);
    }

    public void handleExit() {
        plane.setAvailability(availabilityCheckBox.isSelected());
        plane.setFuelStatus(fuelStatusCheckBox.isSelected());
        plane.setTechnicalStatus(technicalStatusCheckBox.isSelected());
        planeDAO.update(plane);
        planeStage.close();
    }

    public void setPlaneStage(Stage planeStage) {
        this.planeStage = planeStage;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
        nameField.setText(plane.getName());
        typeField.setText(plane.getType());
        ownerField.setText(plane.getOwner());
        capacityField.setText(plane.getCapacity().toString());
        availabilityCheckBox.setSelected(plane.getAvailability());
        technicalStatusCheckBox.setSelected(plane.getTechnicalStatus());
        fuelStatusCheckBox.setSelected(plane.getFuelStatus());
    }
}
