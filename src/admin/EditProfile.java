
package admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import other_classes.T;


public class EditProfile {

    private final String myEmail;

    public EditProfile(String email) {
        this.myEmail = email;
    }

    public void start() {
        Stage stage = new Stage();

        Label title = new Label("Change your System Info");
        title.setStyle("-fx-font: normal bold 20px 'serif'");

        Label fNameLabel = new Label("First Name");
        TextField fNameText = new TextField(Admin.get(myEmail, "firstName"));

        Label sNameLabel = new Label("Second Name");
        TextField sNameText = new TextField(Admin.get(myEmail, "secondName"));

        Label emailLabel = new Label("Email");
        TextField emailText = new TextField(Admin.get(myEmail, "email"));

        Button btnSave = new Button("Save Changes");
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String firstName = fNameText.getText();
                String secondName = sNameText.getText();
                String email = emailText.getText();

                if (firstName.contentEquals("")) {
                    T.show("Error", "First name field is required", 0);
                    return;
                }

                if (secondName.contentEquals("")) {
                    T.show("Error", "second name field is required", 0);
                    return;
                }

                if (!email.contains("@") || !email.contains(".") || email.contains(" ")) {
                    T.show("Error", "Invalid email. Email must be inform of example@domainname.com, also white spaces are not allowed", 0);
                    return;
                }

                if (firstName.contains(" ")) {
                    T.show("Error", "White spaces are not allowed for the first name field", 0);
                    return;
                }

                if (secondName.contains(" ")) {
                    T.show("Error", "White spaces are not allowed for the second name field", 0);
                    return;
                }

                Admin.edit(stage, myEmail, firstName, secondName, email);
                stage.close();
                
            }
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
        hBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setStyle("-fx-background-color: BEIGE;");
        vBox.getChildren().addAll(title, fNameLabel, fNameText, sNameLabel, sNameText, emailLabel, emailText, hBox);

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
