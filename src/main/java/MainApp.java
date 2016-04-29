import control.LoginWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Person;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Person person = showLoginWindow();
        switch (person.getUserType()) {
            case MANAGER: case ADMIN: {

                break;
            }
            case WORKER: {

                break;
            }
        }
        primaryStage.close();
    }

    public Person showLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("LoginWindow.fxml"));
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

    public static void main(String[] args) {
        launch(args);
    }
}
