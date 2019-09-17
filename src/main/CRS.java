
package main;

import students.StudentLogin;

import javafx.application.Application;
import javafx.stage.Stage;



public class CRS extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        new StudentLogin().start();
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
