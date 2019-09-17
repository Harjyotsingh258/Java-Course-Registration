
package students;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class AcadamicRecords {

    private final String studentId;

    public AcadamicRecords(String studentID) {
        this.studentId = studentID;
    }
    
    public void start(){
        Stage stage = new Stage();
        
        GridPane sideMenu = new SideMenu().get(stage, studentId, 3);
        
        Image image = new Image(getClass().getResourceAsStream("avatar.png"));
        Label activeButton = new Label("");
        activeButton.setGraphic(new ImageView(image));
        
        Text name = new Text(Student.get(studentId, "firstName") + " " + Student.get(studentId, "SecondName"));
        
        HBox hboxTop = new HBox();
        hboxTop.setAlignment(Pos.TOP_RIGHT);
        hboxTop.setSpacing(20);
        hboxTop.getChildren().addAll(activeButton, name);
        
        Label titleLabel = new Label("Acadamic Records");
        titleLabel.setAlignment(Pos.TOP_CENTER);
        titleLabel.setStyle("-fx-font: normal bold 20px 'serif' ");
        
        VBox subVbox = new VBox();
        subVbox.setAlignment(Pos.CENTER);        
        
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, 10, 10, 50));
        vBox.setSpacing(20);
        vBox.setPrefWidth(400);
        vBox.getChildren().addAll(hboxTop, titleLabel, subVbox);
        
        
        HBox mainHBox = new HBox();
        mainHBox.setSpacing(20);
        mainHBox.setAlignment(Pos.CENTER);
        mainHBox.setPadding(new Insets(10, 10, 10, 10));
        mainHBox.setStyle("-fx-padding: 10;" + 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + 
                "-fx-border-radius: 5;" + 
                "-fx-border-color: gray;"
                + "-fx-background-color: BEIGE;");
        mainHBox.getChildren().addAll(sideMenu,vBox);
        
        Scene scene = new Scene(mainHBox);
        //Setting title to the Stage 
        stage.setTitle("Edit Page");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
        
    }
    
}
