package app;

import control.LoginWindowController;
import control.PasswordEditDialogController;
import control.ProfileController;
import control.manager.ManagerMainWindowController;
import control.manager.PlaneTableViewController;
import control.manager.PrimaryTaskEditDialogController;
import control.manager.PrimaryTaskTableViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Person;
import util.HibernateUtil;

import java.io.IOException;

public class MainApp extends Application {
    private Stage mainStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.mainStage = primaryStage;
        mainStage.setOnCloseRequest((windowEvent) -> HibernateUtil.shutDown());
        mainStage.setTitle("Task manager");
        Person person = showLoginWindow();
        if (person == null) {
            mainStage.close();
            return;
        }
        switch (person.getUserType()) {
            case MANAGER: case ADMIN: {
                showManagerMainWindow(person);
                return;
            }
            case WORKER: {
                showWorkerMainWindow(person);
                return;
            }
        }
    }

    public void showManagerMainWindow(Person person) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/manager/ManagerRootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            mainStage.setScene(scene);
            ManagerMainWindowController controller = loader.getController();
            controller.setPerson(person);
            controller.loadData();
            controller.setMainStage(mainStage);
            controller.setMainApp(this);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showWorkerMainWindow(Person person) {
        /*try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view.worker/WorkerRootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            WorkerMainWindowController controller = loader.getController();
            controller.setPerson(person);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public Person showLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/LoginWindow.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Авторизація");
            loginStage.setScene(new Scene(root));
            LoginWindowController controller = loader.getController();
            controller.setLoginStage(loginStage);
            loginStage.showAndWait();
            return controller.getPerson();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String showPasswordEditDialog(String oldPassword) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/PasswordEditDialog.fxml"));
            AnchorPane root = loader.load();
            Stage passwordStage = new Stage();
            passwordStage.setTitle("Зміна паролю");
            passwordStage.initOwner(mainStage);
            passwordStage.initModality(Modality.WINDOW_MODAL);
            passwordStage.setScene(new Scene(root));
            PasswordEditDialogController controller = loader.getController();
            controller.setPasswordStage(passwordStage);
            controller.setOldPassword(oldPassword);
            passwordStage.showAndWait();
            return controller.getPassword();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void showProfile(Person person) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Profile.fxml"));
            AnchorPane root = loader.load();
            Stage profileStage = new Stage();
            profileStage.setTitle("Профіль");
            profileStage.initOwner(mainStage);
            profileStage.initModality(Modality.WINDOW_MODAL);
            profileStage.setScene(new Scene(root));
            ProfileController controller = loader.getController();
            controller.setProfileStage(profileStage);
            controller.setPerson(person);
            profileStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showPrimaryTask() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/manager/PrimaryTaskTableView.fxml"));
            AnchorPane root = loader.load();

            Stage primaryTasksStage = new Stage();
            primaryTasksStage.initOwner(mainStage);
            primaryTasksStage.initModality(Modality.WINDOW_MODAL);
            primaryTasksStage.setTitle("Атомарні роботи");
            primaryTasksStage.setScene(new Scene(root));
            PrimaryTaskTableViewController controller = loader.getController();
            controller.setPrimaryTaskStage(primaryTasksStage);
            controller.setMainApp(this);
            primaryTasksStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPrimaryTaskEditDialog(
            PrimaryTaskTableViewController primaryTaskTableViewController) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/manager/PrimaryTaskEditDialog.fxml"));
            AnchorPane root = loader.load();

            Stage primaryTaskEditDialogStage = new Stage();
            primaryTaskEditDialogStage.setTitle("Атомарна робота");
            primaryTaskEditDialogStage.setScene(new Scene(root));
            primaryTaskEditDialogStage.initOwner(
                    primaryTaskTableViewController.getPrimaryTaskStage());
            primaryTaskEditDialogStage.initModality(Modality.WINDOW_MODAL);
            PrimaryTaskEditDialogController controller = loader.getController();
            controller.setPrimaryTaskEditDialogStage(primaryTaskEditDialogStage);
            controller.setController(primaryTaskTableViewController);
            primaryTaskEditDialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPlane() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/manager/PlaneTableView.fxml"));
            AnchorPane root = loader.load();

            Stage planeStage = new Stage();
            planeStage.setTitle("Літаки");
            planeStage.setScene(new Scene(root));
            planeStage.initOwner(mainStage);
            planeStage.initModality(Modality.WINDOW_MODAL);
            PlaneTableViewController controller = loader.getController();
            controller.setPlaneStage(planeStage);
            planeStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showAlert(Alert.AlertType type, String title,
                                 String header, String context) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
