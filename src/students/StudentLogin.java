package students;

import admin.AdminLogin;
import instructor.InstructorLogin;
import instructor.InstructorRegister;



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
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;





public class StudentLogin {

    public void start() {
        Stage stage = new Stage();
        //creating label for student ID
        Text txtStudentID = new Text("Student id");

        //creating label password 
        Text txtPass = new Text("Password");

        //Creating Text Field for student id        
        TextField studentID = new TextField();

        //Creating Password Feild for password        
        PasswordField pass = new PasswordField();

        //Creating Buttons 
        Button btnSubmit = new Button("Submit");

        
        
        
        EventHandler<ActionEvent> submitClickHandler = (ActionEvent e) -> {
            Student.login(stage, studentID.getText(), String.valueOf(pass.getText()) );
        };
        btnSubmit.setOnAction(submitClickHandler);

        
        
        
        
        //Creating a Grid Pane 
        GridPane gridPane = new GridPane();

        //Setting size for the pane 
        gridPane.setMinSize(900, 900);

        //Setting the padding  
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        //Setting the vertical and horizontal gaps between the columns 
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        //Setting the Grid alignment 
        gridPane.setAlignment(Pos.CENTER);

        //Arranging all the nodes in the grid 
        gridPane.add(txtStudentID, 0, 0);
        gridPane.add(studentID, 1, 0);
        gridPane.add(txtPass, 0, 1);
        gridPane.add(pass, 1, 1);
        gridPane.add(btnSubmit, 1, 2);

        //Styling nodes  
        btnSubmit.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        txtStudentID.setStyle("-fx-font: normal bold 20px 'serif' ");
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
        Menu mainMenu = new Menu("Menu");

        MenuItem adminLogin = new MenuItem("Login as an Admin");
        MenuItem studentRegister = new MenuItem("Register as a Student");
        MenuItem instRegister = new MenuItem("Register as an Instructor");
        MenuItem instLogin = new MenuItem("Instructor Login");

        adminLogin.setOnAction((ActionEvent e) -> {
            stage.close();
            new AdminLogin().start();
        });

        instLogin.setOnAction((ActionEvent e) -> {
            stage.close();
            new InstructorLogin().start();
        });

        instRegister.setOnAction((ActionEvent e) -> {
            stage.close();
            new InstructorRegister().start();
        });

        studentRegister.setOnAction((ActionEvent e) -> {
            stage.close();
            new StudentRegister().start();
        });

        mainMenu.getItems().addAll(adminLogin, studentRegister,
                instRegister, instLogin);
        menuBar.getMenus().add(mainMenu);

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }

}
