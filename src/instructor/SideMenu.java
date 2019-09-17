
package instructor;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import students.StudentLogin;


public class SideMenu {
    public GridPane get(Stage stage, String email, int type){
        GridPane gridPane = new GridPane();
        gridPane.setVgap(20); 
        
        Button btnPSI = new Button("Personal Information");
        btnPSI.setPrefSize(200, 35);
        Button btnCourseTeaching = new Button("CourseTeaching");
        btnCourseTeaching.setPrefSize(200, 35);
         Button btnCourseSchedule = new Button("Course Schedule");
        btnCourseSchedule.setPrefSize(200, 35);
        Button btnLogout = new Button("Logout");
    
        btnLogout.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        
        btnLogout.setOnAction((ActionEvent e) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to logout?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.YES.equals(result)) {
                stage.close();
                new StudentLogin().start();
            } else if (ButtonType.NO.equals(result)) {
                alert.close();
            }

        });
        
        btnPSI.setOnAction((ActionEvent e) -> {
                    stage.close();
                    new InstructorProfile(email).start();
                });
        btnCourseTeaching.setOnAction((ActionEvent e) -> {
            stage.close();
            new CourseTeaching(email).start();
        });
        btnCourseSchedule.setOnAction((ActionEvent e) -> {
            stage.close();
            new CourseScheduleInstructor(email).start();
        });
        
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.add(btnPSI, 1,1);
        gridPane.add(btnCourseTeaching, 1,2);
        gridPane.add(btnCourseSchedule, 1,3);
        gridPane.add(btnLogout, 0, 4);
        
        
        gridPane.setStyle("-fx-padding: 10;" + 
                      "-fx-border-style: solid inside;" + 
                      "-fx-border-width: 2;" +
                      "-fx-border-insets: 5;" + 
                      "-fx-border-radius: 5;" + 
                      "-fx-border-color: black;");
        
        Image image = new Image(getClass().getResourceAsStream("dot.png"));
        Label activeButton = new Label("");
        
        
        activeButton.setGraphic(new ImageView(image));
        
        if (type == 0) {
            gridPane.add(activeButton, 0,1);
        }else if (type == 1) {
            gridPane.add(activeButton, 0,2);
        }else if (type == 2) {
            gridPane.add(activeButton, 0,3);
        }
        
        return gridPane;
        
    }
    
    
}
