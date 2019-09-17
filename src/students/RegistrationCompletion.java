/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package students;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author groot
 */
public class RegistrationCompletion {

    private final String studentId;

    public RegistrationCompletion(String id) {
        this.studentId = id;
    }

    public void start() {
        Stage stage = new Stage();

        String info = "Thanks very much for Registering. \nThis is your student Id, you will use it when logging in along with your password. \nPlease keep this Student Id safe. \nIn case you forget it, kindly contact the system admin.\n";
        Text infoText = new Text(info);

        Text idText = new Text(studentId);

        Hyperlink login = new Hyperlink();
        login.setText("Go to login");
        login.setOnAction((ActionEvent event) -> {
            stage.close();
            new StudentLogin().start();
        });

        //Creating a Grid Pane 
        GridPane gridPane = new GridPane();

        //Setting size for the pane 
        gridPane.setMinSize(500, 500);

        //Setting the padding    
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns 
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        //Setting the Grid alignment 
        gridPane.setAlignment(Pos.CENTER);

        //Setting the back ground color 
        gridPane.setStyle("-fx-background-color: BEIGE;");
        gridPane.add(infoText, 1, 0);
        gridPane.add(idText, 1, 1);
        gridPane.add(login, 1, 2);

        Scene scene = new Scene(gridPane);
        //Setting title to the Stage 
        stage.setTitle("Registration Complete");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }

}
