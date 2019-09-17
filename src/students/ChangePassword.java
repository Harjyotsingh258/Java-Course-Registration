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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import other_classes.T;

/**
 *
 * @author groot
 */
public class ChangePassword {

    private final String studentId;

    public ChangePassword(String studentID) {
        this.studentId = studentID;
    }

    public void start() {

        Stage stage = new Stage();

        Label title = new Label("Change your System Password");
        title.setStyle("-fx-font: normal bold 20px 'serif'");

        Label oldPassLabel = new Label("Old Password");
        PasswordField oldPassText = new PasswordField();

        Label newPassLabel = new Label("New Password");
        PasswordField newPassText = new PasswordField();

        Label confirmPassLabel = new Label("Confirm New Password");
        PasswordField confrimPassText = new PasswordField();

        Button btnSave = new Button("Save Changes");
        btnSave.setOnAction((ActionEvent event) -> {
            String op = oldPassText.getText();
            String np = newPassText.getText();
            String cp = confrimPassText.getText();

            if (np.contentEquals("")) {
                T.show("Error", "You must provide a new password", 0);
                return;
            }

            if (!np.contentEquals(cp)) {
                T.show("Error", "Your Password did not match", 0);
                return;
            }

            Student.changePassword(studentId, op, np);
        });

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction((ActionEvent event) -> {
            stage.close();
        });

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);
        hBox.getChildren().addAll(btnCancel, btnSave);

        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setStyle("-fx-background-color: BEIGE;");
        vBox.getChildren().addAll(title, oldPassLabel, oldPassText, newPassLabel,
                newPassText, confirmPassLabel, confrimPassText, hBox);

        Scene scene = new Scene(vBox);
        //Setting title to the Stage 
        stage.setTitle("Browse Courses");
        stage.initModality(Modality.APPLICATION_MODAL);

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }
}
