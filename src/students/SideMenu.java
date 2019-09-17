package students;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import students.AcadamicRecords;
import students.Payments;
import students.Registration;
import students.StudentLogin;
import students.StudentProfile;


public class SideMenu {
    
    public GridPane get(Stage stage, String studentId, int type){
        GridPane gridPane = new GridPane();
        gridPane.setVgap(20); 
        
        Button btnPSI = new Button("Personal Information");
        btnPSI.setPrefSize(200, 35);
        Button btnReg = new Button("Registration Information");
        btnReg.setPrefSize(200, 35);
        Button btnPayment = new Button("Payments");
        btnPayment.setPrefSize(200, 35);
        Button btnAcRe = new Button("Academic Record");
        btnAcRe.setPrefSize(200, 35);
        Button btnLogout = new Button("Logout");
    
        btnLogout.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        
        EventHandler<ActionEvent> logoutClickHandler = (ActionEvent e) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to logout?", ButtonType.YES, ButtonType.NO);
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.YES.equals(result)) {
                stage.close();
                new StudentLogin().start();
            } else if (ButtonType.NO.equals(result)) {
                alert.close();
            }

        };
        btnLogout.setOnAction(logoutClickHandler);
        // when hyperlink is pressed 
        btnPSI.setOnAction((ActionEvent e) -> {
                    stage.close();
                    new StudentProfile(studentId).start();
                });
        btnReg.setOnAction((ActionEvent e) -> {
            stage.close();
            new Registration(studentId).start();
        });
        btnPayment.setOnAction((ActionEvent e) -> {
            stage.close();
            new Payments(studentId).start();
        });
        btnAcRe.setOnAction((ActionEvent e) -> {
            stage.close();
            new AcadamicRecords(studentId).start();
        });
        
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.add(btnPSI, 1,1);
        gridPane.add(btnReg, 1,2);
        gridPane.add(btnPayment,1,3);
        gridPane.add(btnAcRe, 1, 4);
        gridPane.add(btnLogout, 0, 5);
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
        }else if (type == 3){
            gridPane.add(activeButton, 0,4);
        }
        
        return gridPane;
        
    }
    
}
