package app;

import control.LoginWindowController;
import control.PasswordEditDialogController;
import control.PlaneController;
import control.ProfileController;
import control.manager.*;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
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
                break;
            }
            case WORKER: {
                showWorkerMainWindow(person);
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
            mainStage.setResizable(false);
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
            loginStage.setResizable(false);
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
            passwordStage.setResizable(false);
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
            profileStage.setResizable(false);
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
            primaryTasksStage.setResizable(false);
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

    public PrimaryTask showPrimaryTaskEditDialog(
            PrimaryTaskTableViewController primaryTaskTableViewController) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/manager/PrimaryTaskEditDialog.fxml"));
            AnchorPane root = loader.load();

            Stage primaryTaskEditDialogStage = new Stage();
            primaryTaskEditDialogStage.setResizable(false);
            primaryTaskEditDialogStage.setTitle("Атомарна робота");
            primaryTaskEditDialogStage.setScene(new Scene(root));
            primaryTaskEditDialogStage.initOwner(
                    primaryTaskTableViewController.getPrimaryTaskStage());
            primaryTaskEditDialogStage.initModality(Modality.WINDOW_MODAL);
            PrimaryTaskEditDialogController controller = loader.getController();
            controller.setPrimaryTaskEditDialogStage(primaryTaskEditDialogStage);
            controller.loadData();
            primaryTaskEditDialogStage.showAndWait();
            return controller.getPrimaryTask();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
            requestStage.setResizable(false);
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
            controller.loadData();
            taskStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AssignedTask showAssignTaskDialog(List<Person> persons,
                                             int maxAmount, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/manager/AssignTaskDialog.fxml"));
            AnchorPane root = loader.load();

            Stage personStage = new Stage();
            personStage.setResizable(false);
            personStage.setTitle("Вибір робітника");
            personStage.setScene(new Scene(root));
            personStage.initOwner(stage);
            personStage.initModality(Modality.WINDOW_MODAL);
            AssignTaskDialogController controller = loader.getController();
            controller.setStage(personStage);
            controller.setMainApp(this);
            controller.setMaxAmount(maxAmount);
            controller.loadData(persons);
            personStage.showAndWait();
            return controller.getAssignedTask();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showJasperViewer(JasperPrint jasperPrint, Stage stage) {
        Stage jasperStage = new Stage();
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(new JRViewer(jasperPrint));
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(swingNode);
        Scene scene = new Scene(stackPane);
        jasperStage.setScene(scene);
        jasperStage.setFullScreen(false);
        jasperStage.setTitle("Звіт");
        jasperStage.initOwner(stage);
        jasperStage.setResizable(false);
        jasperStage.initModality(Modality.WINDOW_MODAL);
        jasperStage.showAndWait();
    }

    public Task createTaskDialog(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(this.getClass()
                    .getResource("/view/manager/CreateTaskDialog.fxml"));
            AnchorPane root = fxmlLoader.load();

            Stage taskStage = new Stage();
            taskStage.setResizable(false);
            taskStage.setScene(new Scene(root));
            taskStage.initOwner(stage);
            taskStage.initModality(Modality.WINDOW_MODAL);
            taskStage.setResizable(false);
            taskStage.setTitle("Нове завдання");
            CreateTaskDialogController controller = fxmlLoader.getController();
            controller.setStage(taskStage);
            controller.loadData();
            taskStage.showAndWait();
            return controller.getCreatedTask();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createRequestDialog(Stage stage, Task task) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(this.getClass()
                    .getResource("/view/manager/CreateRequestDialog.fxml"));
            AnchorPane root = fxmlLoader.load();

            Stage taskStage = new Stage();
            taskStage.setScene(new Scene(root));
            taskStage.initOwner(stage);
            taskStage.initModality(Modality.WINDOW_MODAL);
            taskStage.setResizable(false);
            taskStage.setTitle("Нове завдання");
            CreateRequestDialogController controller = fxmlLoader.getController();
            controller.setStage(taskStage);
            controller.setTask(task);
            taskStage.showAndWait();
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
