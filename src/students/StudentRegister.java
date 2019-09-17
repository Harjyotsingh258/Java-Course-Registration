package students;

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





public class StudentRegister {

    private Text fNameLabel;
    private TextField fNameText;
    private Text sNameLabel;
    private TextField sNameText;
    private Text lNameLabel;
    private TextField lNameText;
    private Text emailLabel;
    private TextField emailText;
    private Text passLabel;
    private PasswordField passText;
    private Text passConfirmLabel;
    private PasswordField passConfirmText;
    private ListView<String> departmentListView;
    private ChoiceBox courseChoiceBox;
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
      
      //Label for name 
      lNameLabel = new Text("Last Name"); 
      
      lNameText = new TextField(); 
      
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
      
      //Label for course 
      Text courseLabel = new Text("Course"); 
      
      courseChoiceBox = new ChoiceBox(); 
      ObservableList<String> courses = FXCollections.observableArrayList( 
         Courses.loadSelectableCourses()); 
      courseChoiceBox.getItems().addAll(courses);
      
       
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
      
      gridPane.add(lNameLabel, 0, 2); 
      gridPane.add(lNameText, 1, 2);
      
      gridPane.add(emailLabel, 0, 3); 
      gridPane.add(emailText, 1, 3);        
       
      gridPane.add(departmentLabel, 0, 4); 
      gridPane.add(departmentListView, 1, 4);      
       
      gridPane.add(courseLabel, 0, 5); 
      gridPane.add(courseChoiceBox, 1, 5);  
      
      gridPane.add(passLabel, 0, 6);
      gridPane.add(passText, 1, 6);
      
      gridPane.add(passConfirmLabel, 0, 7);
      gridPane.add(passConfirmText, 1, 7);
       
      gridPane.add(buttonRegister, 2, 9);      
      
      //Styling nodes   
      buttonRegister.setStyle(
         "-fx-background-color: darkslateblue; -fx-text-fill: white;"); 
      String labelStyle = "-fx-font: normal bold 15px 'serif' ";
      fNameLabel.setStyle(labelStyle); 
      sNameLabel.setStyle(labelStyle);
      lNameLabel.setStyle(labelStyle);
      emailLabel.setStyle(labelStyle);
      departmentLabel.setStyle(labelStyle); 
      courseLabel.setStyle(labelStyle); 
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
        String lastName = lNameText.getText();
        String dep = departmentListView.getSelectionModel().getSelectedItem();
        String course = courseChoiceBox.getSelectionModel().getSelectedItem().toString();

        if (firstName.contentEquals(
                "") || secondName.contentEquals("") || email.contentEquals("") || lastName.contentEquals("") || password.contentEquals("")) {
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

        if (lastName.contains(
                " ")) {
            T.show("Error", "Last name should not contain a white space!", 0);
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

        if (course.contentEquals("")) {
            T.show("Error", "No courses under the departiment of " + dep + ". Please select another departiment or check back later");
            return;
        }

        if (!password.contentEquals(confirmPassword)) {
            T.show("Registration Error", "Your password did not match", 0);
            return;
        }

        Student.register(stage, firstName, secondName, lastName, email, dep, course, password);
//        T.show("Data", "First Name is "+firstName+"\nSecond Nam: "+secondName+"\nEmail: "+email+"\n"+"Last Name"+lastName+"\npassword: "+password+"", 1);
   }
    
}
