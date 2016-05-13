package control.manager;

import DAO.AssignedTaskDAO;
import DAO.PersonDAO;
import DAO.TaskDAO;
import app.MainApp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.AssignedTask;
import model.Person;
import model.Task;
import util.DateUtil;
import util.TimeUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class TaskDetailsController {
    private final int labelTimeToHide = 5000;
    private MainApp mainApp;
    private Task task;
    private AssignedTask assignedTask;
    private Stage stage;
    private AssignedTaskDAO assignedTaskDAO;
    private TaskDAO taskDAO;
    private PersonDAO personDAO;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField assignmentDateField;
    @FXML
    private TextField updateField;
    @FXML
    private TextField finishDateField;
    @FXML
    private TextField deadlineField;
    @FXML
    private TextField taskNameField;
    @FXML
    private TextField costField;
    @FXML
    private TextField timeToCompleteField;
    @FXML
    private TextArea additionalDescriptionTextArea;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private CheckBox isFinishedCheckBox;
    @FXML
    private Label confirmation;

    public TaskDetailsController() {
        assignedTaskDAO = new AssignedTaskDAO();
        taskDAO = new TaskDAO();
        personDAO = new PersonDAO();
    }

    @FXML
    public void initialize() {
        firstNameField.setEditable(false);
        lastNameField.setEditable(false);
        assignmentDateField.setEditable(false);
        updateField.setEditable(false);
        finishDateField.setEditable(false);
        deadlineField.setEditable(true);
        deadlineField.setPromptText(DateUtil.DATE_FORMAT);
        taskNameField.setEditable(false);
        costField.setEditable(false);
        timeToCompleteField.setEditable(false);
        descriptionTextArea.setEditable(false);
        additionalDescriptionTextArea.setEditable(true);
        deadlineField.focusedProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!newValue) {
                    Date date = null;
                        try {
                            date = DateUtil.fromString(deadlineField.getText());
                        } catch (ParseException e) {
                            deadlineField.setPromptText(DateUtil.DATE_FORMAT);
                            MainApp.showAlert(Alert.AlertType.ERROR,
                                    "","Невірний формат дати",
                                    "Будь ласка, введіть дату корректно");
                        }
                        deadlineField.setText(DateUtil.toString(date));
                    }
                });
    }

    public void update() {
        setTask(taskDAO.findTaskByIdentifier(task.getIdentifier()));
    }

    public void handleExit() {
        stage.close();
    }

    public void handleAssignWorker() {
        updateAssignedTask();
        if (assignedTask != null) {
            MainApp.showAlert(Alert.AlertType.ERROR,"",
                    "Робітника уже призначено!",
                    "Звільніть, будь ласка, робітника, щоб призначити іншого!");
            return;
        }
        List<Person> persons = personDAO.findAvailableWorkers();
        Person person = mainApp.showChoosePersonDialog(persons, stage);
        if (person != null) {
            assignedTask = new AssignedTask();
            assignedTask.setPerson(person);
            assignedTask.setFinished(false);
            assignedTask.setTask(task);
            assignedTask.setAssignmentDate(new Date());
            assignedTask.setProgress(0.0);
            assignedTaskDAO.addAssignedTask(assignedTask);
            updateAssignedTask();
        }
    }

    public void handleRemoveWorker() {
        Date currentDate = new Date();
        updateAssignedTask();
        if (assignedTask != null) {
            assignedTask.setFinishDate(currentDate);
            assignedTask.setFinished(false);
            assignedTaskDAO.updateAssignedTask(assignedTask);
            updateAssignedTask();
        } else {
            MainApp.showAlert(Alert.AlertType.ERROR, "",
                    "На дану роботу не призначено робітника!",
                    "");
        }

    }

    public void handleShowProfile() {
        if (assignedTask == null) {
            MainApp.showAlert(Alert.AlertType.ERROR,
                    "Неможливо показати профіль!",
                    "Завдання ніким не виконується!",
                    "");
        } else {
            mainApp.showProfile(assignedTask.getPerson());
        }
    }

    public void handleConfirmChanges() {
        boolean updateAssignedTask = false;
        boolean updateTask = false;
        if (isFinishedCheckBox.isSelected()) {
            if (!canTaskBeFinished()) {
                return;
            } else {
                if (!assignedTask.getFinished()) {
                    assignedTask.setFinished(true);
                    Date currentDate = new Date();
                    assignedTask.setFinishDate(currentDate);
                    finishDateField.setText(DateUtil.toString(currentDate));
                    updateAssignedTask = true;
                }
            }
        } else {
            if (assignedTask != null && assignedTask.getFinished()) {
                assignedTask.setFinished(false);
                assignedTask.setFinishDate(null);
                finishDateField.setText("");
                updateAssignedTask = true;
            }
        }

        if (!additionalDescriptionTextArea.getText()
                .equals(task.getDescription())) {
            task.setDescription(additionalDescriptionTextArea.getText());
            updateTask = true;
        }
        Date deadline;
        try {
            deadline = DateUtil.fromString(deadlineField.getText());
            if ((deadline != null && !deadline.equals(task.getDeadline()))
                    || (deadline == null && task.getDeadline() != null) ) {
                updateTask = true;
                task.setDeadline(deadline);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (updateTask) {
            taskDAO.updateTask(task);
        }
        if (updateAssignedTask) {
            assignedTaskDAO.updateAssignedTask(assignedTask);
        }
        new Thread(() -> {
            try {
                Thread.sleep(labelTimeToHide);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() ->
                    confirmation.setText(""));
        }).start();
        if (!updateAssignedTask && !updateTask) {
            confirmation.setText("Змін не виявлено!");
            return;
        }
        confirmation.setText("Зміни вступили в силу!");
    }

    public void setTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("set not null task");
        }
        this.task = task;
        updateAssignedTask();
        taskNameField.setText(task.getPrimaryTask().getName());
        costField.setText(task.getPrimaryTask().getCost().toString());
        timeToCompleteField.setText(TimeUtil.toString(
                task.getPrimaryTask().getTimeToComplete()));
        descriptionTextArea.setText(task.getPrimaryTask().getDescription());
        additionalDescriptionTextArea.setText(task.getDescription());
        deadlineField.setText(DateUtil.toString(task.getDeadline()));
    }

    private void updateAssignedTask() {
        assignedTask = assignedTaskDAO
                .findCurrentTaskAssignedToPerson(task.getIdentifier());
        if (assignedTask != null) {
            firstNameField.setText(assignedTask.getPerson().getFirstName());
            lastNameField.setText(assignedTask.getPerson().getLastName());
            assignmentDateField.setText(DateUtil.toString(assignedTask
                    .getAssignmentDate()));
            updateField.setText(DateUtil.toString(assignedTask.getUpdate()));
            finishDateField.setText(DateUtil.toString(assignedTask.getFinishDate()));
            progressBar.setProgress(assignedTask.getProgress());
            isFinishedCheckBox.setSelected(assignedTask.getFinished());
        } else {
            firstNameField.setText("");
            lastNameField.setText("");
            assignmentDateField.setText("");
            updateField.setText("");
            finishDateField.setText("");
            progressBar.setProgress(0);
            isFinishedCheckBox.setSelected(false);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private boolean canTaskBeFinished() {
        if (assignedTask == null) {
            MainApp.showAlert(Alert.AlertType.ERROR,
                    "Помилка!","Завдання ніхто не виконував!",
                    "Завдання, не призначене" +
                    " робітнику, не може бути зарахованим!");
            return false;
        }
        if (assignedTask.getProgress() != 1) {
            MainApp.showAlert(Alert.AlertType.ERROR,
                    "Помилка!","Завдання не може бути зарахованим",
                    "Робітник ще не закінчив" +
                            " виконання завдання!");
            return false;
        }
        return true;
    }
}
