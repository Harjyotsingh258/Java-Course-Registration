/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package students;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author groot
 */
public class StudentHomePage {

    private final String studentId;
    private Text studentName;

    public StudentHomePage(String studentID) {
        this.studentId = studentID;
    }

    public void start() {
        Stage stage = new Stage();
        
        Image image = new Image(getClass().getResourceAsStream("avatar.png"));
        Label activeButton = new Label("");
        activeButton.setGraphic(new ImageView(image));
        
        studentName = new Text();
        studentName.setText(Student.get(studentId, "firstName") + " " + Student.get(studentId, "SecondName") + " " + Student.get(studentId, "lastName"));

        HBox hTopBox = new HBox();
        hTopBox.setSpacing(20);
        hTopBox.getChildren().addAll(activeButton, studentName);
        
        
        
        Button btnPSI = new Button("Personal Information");
        btnPSI.setPrefSize(250, 35);
        Button btnReg = new Button("Registration Information");
        btnReg.setPrefSize(250, 35);
        Button btnPayment = new Button("Payments");
        btnPayment.setPrefSize(250, 35);
        Button btnAcRe = new Button("Academic Record");
        btnAcRe.setPrefSize(250, 35);

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: red; -fx-text-fill: white;");

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

        //Creating a Grid Pane 
        GridPane gridPane = new GridPane();

        //Setting size for the pane 
        gridPane.setMinSize(500, 300);

        //Setting the padding    
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns 
        gridPane.setVgap(40);
        gridPane.setHgap(5);

        //Setting the Grid alignment 
        gridPane.setAlignment(Pos.TOP_CENTER);

        
        gridPane.add(hTopBox, 2, 0);
        gridPane.add(btnPSI, 1, 1);
        gridPane.add(btnReg, 1, 2);
        gridPane.add(btnPayment, 1, 3);
        gridPane.add(btnAcRe, 1, 4);
        gridPane.add(btnLogout, 0, 5);

        //Setting the back ground color 
        gridPane.setStyle("-fx-background-color: BEIGE;");
        
        Scene scene = new Scene(gridPane);
        //Setting title to the Stage 
        stage.setTitle("Home Page");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();

    }

}
