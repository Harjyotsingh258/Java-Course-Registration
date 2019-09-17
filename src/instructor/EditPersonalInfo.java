/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instructor;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import other_classes.T;

/**
 *
 * @author groot
 */
public class EditPersonalInfo {

    private final String myEmail;

    public EditPersonalInfo(String email) {
        this.myEmail = email;
    }

    public void start() {
        Stage stage = new Stage();

        GridPane sideMenu = new SideMenu().get(stage, myEmail, 0);

        Image image = new Image(getClass().getResourceAsStream("avatar.png"));
        Label activeButton = new Label("");
        activeButton.setGraphic(new ImageView(image));

        Text name = new Text(Instructor.get(myEmail, "firstName") + " " + Instructor.get(myEmail, "secondName"));

        HBox hboxTop = new HBox();
        hboxTop.setAlignment(Pos.TOP_RIGHT);
        hboxTop.setSpacing(20);
        hboxTop.getChildren().addAll(activeButton, name);

        Label title = new Label("Personal Details");
        title.setStyle("-fx-font: normal bold 20px 'serif' ");
        
        Label fNameLabel = new Label("First Name");
        fNameLabel.setStyle("-fx-font: normal bold 'serif' ");
        TextField fNameText = new TextField(Instructor.get(myEmail, "firstName"));

        Label sNameLabel = new Label("Second Name");
        sNameLabel.setStyle("-fx-font: normal bold 'serif' ");
        TextField sNameText = new TextField(Instructor.get(myEmail, "secondName"));
        
        Label emailLabel = new Label("Email");
        emailLabel.setStyle("-fx-font: normal bold 'serif' ");
        TextField emailText = new TextField(Instructor.get(myEmail, "email"));

        Button btnSave = new Button("Save Changes");
        btnSave.setOnAction((ActionEvent event) -> {
            String fn = fNameText.getText();
            String sn = sNameText.getText();
            String email = emailText.getText();
            if (fn.contentEquals("") || sn.contentEquals("") || email.contentEquals("")) {
                T.show("Error", "You must fill in all the fields");
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                T.show("Error", email + " is not a valid email");
                return;
            }
            Instructor.edit(stage,myEmail, fn, sn, email);
        });

        Hyperlink changePass = new Hyperlink("Change Your Password");
        changePass.setOnAction((ActionEvent event) -> {
            new ChangePassword(myEmail).start();
        });
        changePass.setAlignment(Pos.BOTTOM_LEFT);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, 10, 10, 0));
        vBox.setSpacing(20);
        vBox.setPrefWidth(400);
        vBox.getChildren().addAll(hboxTop, title,fNameLabel,fNameText, sNameLabel,
                sNameText,emailLabel,emailText, btnSave, changePass);

        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setStyle("-fx-background-color: BEIGE;");
        hbox.getChildren().addAll(sideMenu, vBox);

        Scene scene = new Scene(hbox);
        //Setting title to the Stage 
        stage.setTitle("View Courses");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }

}
