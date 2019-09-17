package students;

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





public class StudentProfile {

    private final String studentId;

    public StudentProfile(String id) {
        this.studentId = id;
    }

    public void start() {
        Stage stage = new Stage();

        
        GridPane gridPane = new SideMenu().get(stage, studentId, 0);        
        
        // the left pane
        Image image = new Image(getClass().getResourceAsStream("avatar.png"));
        Label activeButton = new Label("");
        activeButton.setGraphic(new ImageView(image));

        Text name = new Text(Student.get(studentId, "firstName") + " " + Student.get(studentId, "SecondName"));

        HBox hTopBox = new HBox();
        hTopBox.setAlignment(Pos.TOP_RIGHT);
        hTopBox.setSpacing(20);
        hTopBox.getChildren().addAll(activeButton, name);
        
        Text studentIdLabel = new Text("Student ID: ");
        studentIdLabel.setStyle("-fx-font: normal bold 20px 'serif'");
        Text studentIdText = new Text();
        studentIdText.setText(Student.get(studentId, "student_id"));
        
        Text personalDetailsLabel = new Text("Personal Details ");
        personalDetailsLabel.setStyle("-fx-font: normal bold 20px 'serif'");
        Text fullNamelText = new Text();
        fullNamelText.setText(Student.get(studentId, "firstName") + " " + Student.get(studentId, "SecondName") + " " + Student.get(studentId, "lastName"));
        Text emailText = new Text();
        emailText.setText(Student.get(studentId, "email"));
        
        Text departmentLabel = new Text("Department");
        departmentLabel.setStyle("-fx-font: normal bold 20px 'serif'");
        Text depText = new Text();
        depText.setText(Student.get(studentId, "departiment"));
        
        Text courseLabel = new Text("Courses");    
        courseLabel.setStyle("-fx-font: normal bold 20px 'serif'");
        Hyperlink courseHyperlink = new Hyperlink();
        courseHyperlink.setText("View your Courses");
        courseHyperlink.setOnAction((ActionEvent e) -> {
                    stage.close();
                    new ViewCourses(studentId).start();
                });
        
        Hyperlink editProfileHyperlink = new Hyperlink();        
        editProfileHyperlink.setText("Edit your personal Details");
        
        // when hyperlink is pressed 
        editProfileHyperlink.setOnAction((ActionEvent e) -> {
                    stage.close();
                    new StudentInfo(studentId).start();
                });
        
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(0, 10, 10, 0));
        vBox.setSpacing(20);
        vBox.setPrefWidth(400);        
        vBox.getChildren().addAll(hTopBox,studentIdLabel,studentIdText,personalDetailsLabel
                ,fullNamelText,emailText,departmentLabel,depText,courseLabel,courseHyperlink,editProfileHyperlink);
        
        
        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setStyle("-fx-padding: 10;" + 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + 
                "-fx-border-radius: 5;" + 
                "-fx-border-color: gray;"
                + "-fx-background-color: BEIGE;");
        hbox.getChildren().addAll(gridPane,vBox);
        
        Scene scene = new Scene(hbox);
        //Setting title to the Stage 
        stage.setTitle("Edit Page");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();

    }

}
