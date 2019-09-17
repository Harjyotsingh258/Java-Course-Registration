/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instructor;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import students.StudentLogin;

/**
 *
 * @author groot
 */
public class InstructorLogin {
    
    public void start() {
      Stage stage = new Stage();
        //creating label email 
      Text emailLable = new Text("Email");       
      
      //creating label password 
      Text txtPass = new Text("Password"); 
       
      //Creating Text Filed for student id        
      TextField emailText = new TextField();       
      
      //Creating Text Filed for password        
      PasswordField pass = new PasswordField();  
       
      //Creating Buttons 
      Button btnSubmit = new Button("Submit"); 
      
      EventHandler<ActionEvent> submitClickHandler = (ActionEvent e) -> {
          String email = emailText.getText();
          String password = String.valueOf(pass.getText());
          System.out.println(email+", "+password);
          Instructor.login(stage, email, password);
        };
      btnSubmit.setOnAction(submitClickHandler);
      
      //Creating a Grid Pane 
      GridPane gridPane = new GridPane();   
      
      //Setting size for the pane 
      gridPane.setMinSize(400, 200); 
      
      //Setting the padding  
      gridPane.setPadding(new Insets(10, 10, 10, 10)); 
      
      //Setting the vertical and horizontal gaps between the columns 
      gridPane.setVgap(5); 
      gridPane.setHgap(5);       
      
      //Setting the Grid alignment 
      gridPane.setAlignment(Pos.CENTER); 
       
      //Arranging all the nodes in the grid 
      gridPane.add(emailLable, 0, 0); 
      gridPane.add(emailText, 1, 0); 
      gridPane.add(txtPass, 0, 1);       
      gridPane.add(pass, 1, 1); 
      gridPane.add(btnSubmit, 1, 2); 
       
      //Styling nodes  
      btnSubmit.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;"); 
       
      emailLable.setStyle("-fx-font: normal bold 20px 'serif' "); 
      txtPass.setStyle("-fx-font: normal bold 20px 'serif' ");  
      gridPane.setStyle("-fx-background-color: BEIGE;"); 
     
      //Creating a scene object 
      BorderPane borderPane = new BorderPane();
      VBox vbox = new VBox();
      vbox.setSpacing(5);
      vbox.getChildren().addAll(borderPane, gridPane); 
      Scene scene = new Scene(vbox); 
       
      //Setting title to the Stage 
      stage.setTitle("Login"); 
      
      MenuBar menuBar = new MenuBar();
      menuBar.prefWidthProperty().bind(stage.widthProperty());
      borderPane.setTop(menuBar);
      
      // File menu - new, save, exit
      Menu mainMenu = new Menu("Navigate");
      
      MenuItem adminLogin = new MenuItem("back to Student Login");
      
      adminLogin.setOnAction((ActionEvent e) -> {
          stage.close();
          new StudentLogin().start();
        });
      
      mainMenu.getItems().add(adminLogin);
      menuBar.getMenus().add(mainMenu);

      //Adding scene to the stage 
      stage.setScene(scene);
      
      //Displaying the contents of the stage 
      stage.show(); 
    }
    
}
