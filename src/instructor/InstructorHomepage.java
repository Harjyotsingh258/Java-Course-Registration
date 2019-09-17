
package instructor;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class InstructorHomepage {

    private final String email;

    public InstructorHomepage(String email) {
        this.email = email;
    }
    
    public void start(){
        Stage stage = new Stage();
        
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
                new InstructorLogin().start();
            } else if (ButtonType.NO.equals(result)) {
                alert.close();
            }

        });
        logoutLayout.getChildren().add(btnLogout);
        
        
        //image and th ename of the user on the top right corner.
        Image image = new Image(getClass().getResourceAsStream("avatar.png"));
        Label activeButton = new Label("");
        activeButton.setGraphic(new ImageView(image));
        
        Text name = new Text(Instructor.get(email, "firstName") + " " + Instructor.get(email, "secondName"));
        
        HBox hboxTop = new HBox();
        hboxTop.setAlignment(Pos.TOP_RIGHT);
        hboxTop.setSpacing(20);
        hboxTop.getChildren().addAll(activeButton, name);
        
        Button btnPSI = new Button("Personal Information");
        btnPSI.setPrefSize(300, 40);
        btnPSI.setOnAction((ActionEvent event) -> {
            stage.close();
            new InstructorProfile(email).start();
        });
        
        Button btnCourseTeaching = new Button("Course Teaching");
        btnCourseTeaching.setPrefSize(300, 40);
        btnCourseTeaching.setOnAction((ActionEvent event) -> {
            stage.close();
            new CourseTeaching(email).start();
        });
        
        
        Button btnCourseSchedule = new Button("Course Schedule");
        btnCourseSchedule.setPrefSize(300, 40);
        btnCourseSchedule.setOnAction((ActionEvent event) -> {
            stage.close();
            new CourseScheduleInstructor(email).start();
        });
        
        
        
        
        
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, 10, 10, 0));
        vBox.setSpacing(20);
        vBox.setPrefWidth(600);
        vBox.getChildren().addAll(hboxTop,btnPSI, btnCourseTeaching, btnCourseSchedule);
        
        
        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setStyle("-fx-background-color: BEIGE;");
        hbox.getChildren().addAll(logoutLayout,vBox);
        
        Scene scene = new Scene(hbox);
        //Setting title to the Stage 
        stage.setTitle("Browse Courses");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }
    
}
