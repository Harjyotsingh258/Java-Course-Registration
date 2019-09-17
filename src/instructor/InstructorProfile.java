package instructor;


import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author groot
 */
public class InstructorProfile {

    private final String email;

    public InstructorProfile(String email) {
        this.email = email;
    }
    
    public void start(){
        Stage stage = new Stage();
        
        GridPane sideMenu = new SideMenu().get(stage, email, 0);
        
        Image image = new Image(getClass().getResourceAsStream("avatar.png"));
        Label activeButton = new Label("");
        activeButton.setGraphic(new ImageView(image));
        
        Text name = new Text(Instructor.get(email, "firstName") + " " + Instructor.get(email, "secondName"));
        
        HBox hboxTop = new HBox();
        hboxTop.setAlignment(Pos.TOP_RIGHT);
        hboxTop.setSpacing(20);
        hboxTop.getChildren().addAll(activeButton, name);
        
        Label psi = new Label("Personal Information");
        psi.setStyle("-fx-font: normal bold 20px 'serif' ");
        Text emailText = new Text(email);
        
        Label depart = new Label("Department");
        depart.setStyle("-fx-font: normal bold 20px 'serif' ");
        Text departText = new Text(Instructor.get(email, "departiment"));
        
        Hyperlink editPSI = new Hyperlink("Edit Personal Information");
        editPSI.setOnAction((ActionEvent event) -> {
            stage.close();
            new EditPersonalInfo(email).start();
        });
        
        Hyperlink ChangePassword = new Hyperlink("Change Password");
        ChangePassword.setOnAction((ActionEvent event) -> {
            stage.close();
            new EditPersonalInfo(email).start();
        });
        
        VBox subVBox = new VBox();
        subVBox.setPadding(new Insets(20, 0, 20, 0));
        subVBox.setSpacing(20);
        subVBox.setAlignment(Pos.CENTER);
        subVBox.getChildren().addAll(psi, emailText, depart, departText);
        
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, 0, 10, 0));
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefWidth(400);
        vBox.getChildren().addAll(hboxTop,subVBox, editPSI, ChangePassword);
        
        
        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setStyle("-fx-background-color: BEIGE;");
        hbox.getChildren().addAll(sideMenu,vBox);
        
        Scene scene = new Scene(hbox);
        //Setting title to the Stage 
        stage.setTitle("Browse Courses");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }
    
}
