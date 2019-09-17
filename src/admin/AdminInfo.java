
package admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import other_classes.T;

/**
 *
 * @author groot
 */
public class AdminInfo {

    private final String email;

    public AdminInfo(String email) {
        this.email = email;
    }
    
    public void start(){
        Stage stage = new Stage();
        
        Image image = new Image(getClass().getResourceAsStream("smalllogo.png"));
        Label logo = new Label("");
        logo.setGraphic(new ImageView(image));
        
        Label adminNameLabel = new Label(Admin.get(email, "firstName") + " " + Admin.get(email, "secondName"));
        adminNameLabel.setStyle("-fx-font: normal bold 20px 'serif'");
        Label priviledgeLabel = new Label("System Admin");
        priviledgeLabel.setStyle("-fx-font: normal bold 'serif'");
        Hyperlink editInfoLink = new Hyperlink("Edit Profile");
        editInfoLink.setOnAction((ActionEvent event) -> {
            stage.close();
            new EditProfile(email).start();
        });
        
        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(10, 0, 10, 0));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(logo, adminNameLabel, priviledgeLabel, editInfoLink);
        
        HBox logoutLayout = new HBox();
        logoutLayout.setAlignment(Pos.BOTTOM_LEFT);
        Button btnLogout = new Button("Logout");
        btnLogout.setPrefSize(100, 50);
        btnLogout.setStyle("-fx-background-color: red; -fx-text-fill: white;");

        btnLogout.setOnAction((ActionEvent e) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to logout?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.YES.equals(result)) {
                stage.close();
                new AdminLogin().start();
            } else if (ButtonType.NO.equals(result)) {
                alert.close();
            }

        });
        
        logoutLayout.getChildren().add(btnLogout);        
        
        //left window
        VBox leftBox = new VBox();
        leftBox.setPadding(new Insets(0, 10, 10, 50));
        leftBox.setSpacing(20);
        leftBox.getChildren().addAll(vbox, logoutLayout);   
        
        EventHandler<ActionEvent> viewCourses = (ActionEvent event) -> {
            stage.close();
            new CoursesUi(email).start();
        };
        EventHandler<ActionEvent> viewDepartment = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                new DepartimenstUi(email).start();
            }
        };
        EventHandler<ActionEvent> viewInstructors = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                new InstructorsUi(email).start();
            }
        };
        EventHandler<ActionEvent> viewStudents = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                new StudentsUi(email).start();
            }
        };
        EventHandler<ActionEvent> viewAdmins = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                new AdminUi(email).start();
            }
        };
        EventHandler<ActionEvent> viewMessages = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                T.show("You crrently have no messages in the system");
            }
        };
        
        VBox courseView = createBox("Courses", Admin.getCoursesCount(null) + "", viewCourses);
        VBox departmentView = createBox("Departments", Admin.getDepartimentCount() + "", viewDepartment);
        VBox instructorView = createBox("Instructors", Admin.getInstructorsCount(null) + "", viewInstructors);
        VBox studentsView = createBox("Students", Admin.getStudentsCount(null) + "", viewStudents);
        VBox adminsView = createBox("Admins", Admin.getAdminsCount() + "", viewAdmins);
        VBox msgView = createBox("Messages", "0", viewMessages);
        
        GridPane gridPane = new GridPane();
        gridPane.setVgap(20); 
        gridPane.setHgap(20);
        gridPane.add(courseView, 0, 0);
        gridPane.add(departmentView, 1, 0);
        gridPane.add(instructorView, 0, 1);
        gridPane.add(studentsView, 1, 1);
        gridPane.add(adminsView, 0, 2);
        gridPane.add(msgView, 1, 2);
        
        Button btnChangePass = new Button("Change Password");
        btnChangePass.setPrefSize(300, 35);
        btnChangePass.setAlignment(Pos.BOTTOM_CENTER);
        btnChangePass.setOnAction((ActionEvent event) -> {
            new ChangePasswordDialog(email).start();
        });
                       
        //right window
        VBox rightBox = new VBox();
        rightBox.setSpacing(20);
        rightBox.getChildren().addAll(gridPane, btnChangePass);
        
        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setStyle("-fx-background-color: BEIGE;");
        hbox.getChildren().addAll(leftBox,rightBox);
        
        Scene scene = new Scene(hbox);
        //Setting title to the Stage 
        stage.setTitle("Browse Courses");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }
    
    private VBox createBox(String title, String count, EventHandler<ActionEvent> event){
        Hyperlink view = new Hyperlink("View");
        view.setOnAction(event);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font: normal bold 20px 'serif'; -fx-text-fill: blue;");      
        Label counterLabel = new Label(count);
        counterLabel.setStyle("-fx-font: normal bold 20px 'serif'; -fx-text-fill: red;");
        
        VBox coursesBox = new VBox();
        coursesBox.setAlignment(Pos.CENTER);
        coursesBox.setPadding(new Insets(0, 10, 0, 10));
        coursesBox.setSpacing(20);
        coursesBox.setPrefWidth(200);
        coursesBox.setStyle("-fx-padding: 10;" + 
                      "-fx-border-style: solid inside;" + 
                      "-fx-border-width: 2;" +
                      "-fx-border-insets: 5;" + 
                      "-fx-border-radius: 5;" + 
                      "-fx-border-color: black;");
        coursesBox.getChildren().addAll(titleLabel, counterLabel, view);
        return coursesBox;
    }
    
}
