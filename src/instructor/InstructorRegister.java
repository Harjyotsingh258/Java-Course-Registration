/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instructor;

import admin.Courses;
import admin.Departiments;
import javafx.collections.FXCollections; 
import javafx.collections.ObservableList; 
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets; 
import javafx.geometry.Pos; 

import javafx.scene.Scene; 
import javafx.scene.control.Button; 
import javafx.scene.control.ChoiceBox; 
import javafx.scene.control.ListView; 
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane; 
import javafx.scene.text.Text; 
import javafx.scene.control.TextField; 
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage; 
import other_classes.T;
import students.Student;
import students.StudentLogin;

/**
 *
 * @author groot
 */
public class InstructorRegister {
    
    private Text fNameLabel;
    private TextField fNameText;
    private Text sNameLabel;
    private TextField sNameText;
    private Text emailLabel;
    private TextField emailText;
    private Text passLabel;
    private PasswordField passText;
    private Text passConfirmLabel;
    private PasswordField passConfirmText;
    private ListView<String> departmentListView;
    private Stage stage;
    
    public void start() {  
       stage = new Stage();
        
      fNameLabel = new Text("First Name"); 
      
      //Text field for name 
      fNameText = new TextField(); 
      
      //Label for name 
      sNameLabel = new Text("Second Name"); 
      
      //Text field for name 
      sNameText = new TextField(); 
           
      emailLabel = new Text("Email"); 
      
      emailText = new TextField(); 
      
      passLabel = new Text("Password"); 
      
      passText = new PasswordField(); 
      
      passConfirmLabel = new Text("Confirm Password"); 
      
      passConfirmText = new PasswordField(); 
      
                     
      Text departmentLabel = new Text("Department"); 
      
      
      ObservableList<String> names = FXCollections.observableArrayList( 
         Departiments.getDepartimentsCombobox()); 
      departmentListView = new ListView<String>(names); 
        
       
      //Label for register 
      Button buttonRegister = new Button("Register");  
      buttonRegister.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                register();
            }
          
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
       
      //Arranging all the nodes in the grid 
      gridPane.add(fNameLabel, 0, 0); 
      gridPane.add(fNameText, 1, 0); 
       
      gridPane.add(sNameLabel, 0, 1);       
      gridPane.add(sNameText, 1, 1); 
            
      gridPane.add(emailLabel, 0, 2); 
      gridPane.add(emailText, 1, 2);        
       
      gridPane.add(departmentLabel, 0, 3); 
      gridPane.add(departmentListView, 1, 3);      
      
      gridPane.add(passLabel, 0, 4);
      gridPane.add(passText, 1, 4);
      
      gridPane.add(passConfirmLabel, 0, 5);
      gridPane.add(passConfirmText, 1, 5);
       
      gridPane.add(buttonRegister, 2, 6);      
      
      //Styling nodes   
      buttonRegister.setStyle(
         "-fx-background-color: darkslateblue; -fx-text-fill: white;"); 
      String labelStyle = "-fx-font: normal bold 15px 'serif' ";
      fNameLabel.setStyle(labelStyle); 
      sNameLabel.setStyle(labelStyle);
      emailLabel.setStyle(labelStyle);
      departmentLabel.setStyle(labelStyle); 
      passLabel.setStyle(labelStyle);
      passConfirmLabel.setStyle(labelStyle);
       
      //Setting the back ground color 
      gridPane.setStyle("-fx-background-color: BEIGE;");       
       
      //Creating a scene object 
      BorderPane borderPane = new BorderPane();
      VBox vbox = new VBox();
      vbox.setSpacing(5);
      vbox.getChildren().addAll(borderPane, gridPane); 
      Scene scene = new Scene(vbox); 
      
      MenuBar menuBar = new MenuBar();
      menuBar.prefWidthProperty().bind(stage.widthProperty());
      borderPane.setTop(menuBar);
      
      // File menu - new, save, exit
      Menu mainMenu = new Menu("Navigate");
      
      MenuItem backItem = new MenuItem("Back to Login");
      
      backItem.setOnAction((ActionEvent e) -> {
          stage.close();
          new StudentLogin().start();
        });
      mainMenu.getItems().add(backItem);
      menuBar.getMenus().add(mainMenu);
      
      //Setting title to the Stage 
      stage.setTitle("Registration Form"); 
         
      //Adding scene to the stage 
      stage.setScene(scene);  
      
      //Displaying the contents of the stage 
      stage.show(); 
   } 
    
   private void register(){
       System.out.println("registering");
       String firstName = fNameText.getText();
        String secondName = sNameText.getText();
        String email = emailText.getText();
        String password = passText.getText();
        String confirmPassword = passConfirmText.getText();
        String dep = departmentListView.getSelectionModel().getSelectedItem();

        if (firstName.contentEquals(
                "") || secondName.contentEquals("") || email.contentEquals("") || password.contentEquals("")) {
            T.show("Registration Error", "You must fill in all the field", 0);
            return;
        }

        if (firstName.contains(
                " ")) {
            T.show("Registration Error", "Fisrt name should not contain a white space!", 0);
            return;
        }

        if (secondName.contains(
                " ")) {
            T.show("Registration Error", "Second name should not contain a white space!", 0);
            return;
        }

        if (email.contains(
                " ")) {
            T.show("Registration Error", "Email should not contain a white space!", 0);
            return;
        }

        if (!email.contains(
                "@") || !email.contains(".")) {
            // email should be inform of example@example.com
            T.show("Email Error", "Invalid Email address!", 0);
            return;
        }

        if (dep.contentEquals(
                "Select Your Departiment")) {
            T.show("Error", "You must select departiment, there there are no departiments, kindly contact the admin");
            return;
        }

        if (!password.contentEquals(confirmPassword)) {
            T.show("Registration Error", "Your password did not match", 0);
            return;
        }

        Instructor.register(stage, firstName, secondName, email, password, dep);
//        T.show("Data", "First Name is "+firstName+"\nSecond Nam: "+secondName+"\nEmail: "+email+"\n"+"Last Name"+lastName+"\npassword: "+password+"", 1);
   }
    
}
