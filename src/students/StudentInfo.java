package students;

import connection.Db;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import other_classes.T;


public class StudentInfo {

    private final String studentId;
    private Text fNameLabel;
    private TextField fNameText;
    private Text sNameLabel;
    private TextField sNameText;
    private Text lNameLabel;
    private TextField lNameText;
    private Text emailLabel;
    private TextField emailText;
    private Stage stage;

    public StudentInfo(String id) {
        this.studentId = id;
    }

    public void start() {
        stage = new Stage();

        GridPane sideMenu = new SideMenu().get(stage, studentId, 0);

        fNameLabel = new Text("First Name");

        //Text field for name 
        fNameText = new TextField(Db.get("students", "firstName", "student_id = ? ", new String[]{studentId}));

        //Label for name 
        sNameLabel = new Text("Second Name");

        //Text field for name 
        sNameText = new TextField(Student.get(studentId, "secondName"));

        //Label for name 
        lNameLabel = new Text("Last Name");

        lNameText = new TextField(Student.get(studentId, "lastName"));

        emailLabel = new Text("Email");

        emailText = new TextField(Student.get(studentId, "email"));

        Button btnSaveButton = new Button("Save Changes");
        btnSaveButton.setOnAction((ActionEvent event) -> {
            saveChanges();
        });

        Hyperlink hyperlink = new Hyperlink();
        hyperlink.setText("Change your password");
        hyperlink.setOnAction((ActionEvent event) -> {
            new ChangePassword(studentId).start();
        });

        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setPrefWidth(400);
        vbox.getChildren().addAll(fNameLabel, fNameText, sNameLabel,
                sNameText, lNameLabel, lNameText, emailLabel, emailText, btnSaveButton, hyperlink);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(20);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setStyle("-fx-padding: 10;" + 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + 
                "-fx-border-radius: 5;" + 
                "-fx-border-color: gray;"
                + "-fx-background-color: BEIGE;");
        hbox.getChildren().addAll(sideMenu, vbox);

        Scene scene = new Scene(hbox);
        //Setting title to the Stage 
        stage.setTitle("Edit Page");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }

    private void saveChanges() {
        String fn = fNameText.getText();
        String sn = sNameText.getText();
        String email = emailText.getText();
        String ln = lNameText.getText();
        if (fn.contentEquals("") || sn.contentEquals("") || email.contentEquals("")) {
            T.show("Error", "You must fill in all the fields");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            T.show("Error", "You entered invalid email address");
            return;
        }

        Student.edit(stage, studentId, fn, sn, ln, email);
    }
}
