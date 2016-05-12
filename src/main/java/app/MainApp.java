package app;

import control.LoginWindowController;
import control.PasswordEditDialogController;
import control.PlaneController;
import control.ProfileController;
import control.manager.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import util.HibernateUtil;

import java.io.IOException;
import java.util.List;

public class MainApp extends Application {
    private Stage mainStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.mainStage = primaryStage;
        mainStage.setOnCloseRequest((windowEvent) -> HibernateUtil.shutDown());
        mainStage.setTitle("Менеджер робіт");
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


    public void showPrimaryTaskTable() {
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

    public void showPlaneTable() {
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

    public void showProblemDetails(Problem problem) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/manager/ProblemDetails.fxml"));
            AnchorPane root = loader.load();

            Stage problemDetails = new Stage();
            problemDetails.setTitle("Проект");
            problemDetails.setScene(new Scene(root));
            problemDetails.initOwner(mainStage);
            problemDetails.initModality(Modality.WINDOW_MODAL);
            ProblemDetailsController controller = loader.getController();
            controller.setStage(problemDetails);
            controller.setProblem(problem);
            controller.loadData();
            controller.setMainApp(this);
            problemDetails.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPlane(Plane plane, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Plane.fxml"));
            AnchorPane root = loader.load();

            Stage planeStage = new Stage();
            planeStage.setTitle("Літак");
            planeStage.setScene(new Scene(root));
            planeStage.initOwner(stage);
            planeStage.initModality(Modality.WINDOW_MODAL);
            PlaneController controller = loader.getController();
            controller.setPlaneStage(planeStage);
            controller.setPlane(plane);
            planeStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRequestTable(Stage stage, Problem problem) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/manager/RequestPersonTableView.fxml"));
            AnchorPane root = loader.load();

            Stage requestStage = new Stage();
            requestStage.setTitle("Запити");
            requestStage.setScene(new Scene(root));
            requestStage.initOwner(stage);
            requestStage.initModality(Modality.WINDOW_MODAL);
            RequestTableViewController controller = loader.getController();
            controller.setRequestStage(requestStage);
            controller.setParentStage(stage);
            controller.setProblem(problem);
            controller.loadData();
            controller.setMainApp(this);
            requestStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showListOfSKills(List<Skill> list, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/manager/ListOfSkillsTableView.fxml"));
            AnchorPane root = loader.load();

            Stage skillStage = new Stage();
            skillStage.setTitle("Вміння");
            skillStage.setScene(new Scene(root));
            skillStage.initOwner(stage);
            skillStage.initModality(Modality.WINDOW_MODAL);
            ListOfSkillsTableViewController controller = loader.getController();
            controller.setStage(skillStage);
            controller.setSkills(list);
            skillStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showTaskDetails(Task task, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/manager/TaskDetails.fxml"));
            AnchorPane root = loader.load();

            Stage taskStage = new Stage();
            taskStage.setResizable(false);
            taskStage.setTitle("Деталі завдання");
            taskStage.setScene(new Scene(root));
            taskStage.initOwner(stage);
            taskStage.initModality(Modality.WINDOW_MODAL);
            TaskDetailsController controller = loader.getController();
            controller.setStage(taskStage);
            controller.setMainApp(this);
            controller.setTask(task);
            taskStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Person showChoosePersonDialog(List<Person> persons, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/manager/ChoosePersonDialog.fxml"));
            AnchorPane root = loader.load();

            Stage personStage = new Stage();
            personStage.setResizable(false);
            personStage.setTitle("Вибір робітника");
            personStage.setScene(new Scene(root));
            personStage.initOwner(stage);
            personStage.initModality(Modality.WINDOW_MODAL);
            ChoosePersonDialogController controller = loader.getController();
            controller.setStage(personStage);
            controller.setMainApp(this);
            controller.loadData(persons);
            personStage.showAndWait();
            return controller.getChosenPerson();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
