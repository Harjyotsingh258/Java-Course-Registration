
package admin;

import connection.Db;
import static connection.DbConnection.close;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import other_classes.T;


public class AssignCourse {

    private final String email;
    private ChoiceBox courseChoiceBox;
    private TableView tableView;

    public AssignCourse(String theEmail) {
        this.email = theEmail;
    }
    
    public void start(){
        Stage stage = new Stage();
        
        Button btnCancel = new Button("Cancel");
        btnCancel.setAlignment(Pos.TOP_RIGHT);
        btnCancel.setOnAction((ActionEvent event) -> {
            stage.close();
        });
        
        Label title = new Label("Assign Course to this Instructor");
        
        HBox topHBox = new HBox();
        topHBox.setSpacing(20);
        topHBox.getChildren().addAll(title,btnCancel);
        
        
        //Choice box for location 
        courseChoiceBox = new ChoiceBox();
        ObservableList<String> courses = FXCollections.observableArrayList(
                Courses.loadSelectableCourses());
        courseChoiceBox.getItems().addAll(courses);
        Button btnAssign = new Button("Assign Course");
        btnAssign.setOnAction((ActionEvent event) -> {
            String addCourse = courseChoiceBox.getSelectionModel().getSelectedItem().toString();
            if (addCourse.contentEquals("Select Course to Assign")) {
                T.show("Error", "Please select course first");
                return;
            }
            Courses.assignCourse(stage,email, addCourse);
        });
        
        tableView = new TableView();
        
        ResultSet rs = Db.loadTable("instructor_courses", new String[]{}, 
                "instructor = ? ", new String[]{email});
        
        ObservableList<course> dbData = FXCollections.observableArrayList(loadData(rs));

        TableColumn courseCol = new TableColumn("Course");
        courseCol.setMinWidth(300);
        courseCol.setCellValueFactory(
                new PropertyValueFactory<course, String>("course"));

        TableColumn instructorCol = new TableColumn("Instructor");
        instructorCol.setMinWidth(300);
        instructorCol.setCellValueFactory(
                new PropertyValueFactory<course, String>("instructor"));
        //Filling up tableView with data
        tableView.setItems(dbData);
        tableView.getColumns().addAll(instructorCol,courseCol);
        
        //main HBox
        VBox mainVBox = new VBox();
        mainVBox.setSpacing(20);
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.setPadding(new Insets(10, 10, 10, 10));
        mainVBox.setStyle("-fx-background-color: BEIGE;");
        mainVBox.getChildren().addAll(topHBox,courseChoiceBox,btnAssign,tableView);
        
        Scene scene = new Scene(mainVBox);
        //Setting title to the Stage 
        stage.setTitle("View Courses");
        stage.initModality(Modality.APPLICATION_MODAL);

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }
    
    
    private List<course> loadData(ResultSet rs){
        List<course> data = new ArrayList<>();
        try {
            if (rs.next()) {
                do {
                    String course = rs.getString("course");
                    String myId = rs.getString("instructor");
                    course myCourse = new course(course, myId);   
                    data.add(myCourse);

                } while (rs.next());
            }
            
        } catch (SQLException e) {
            T.show("Database Error", e + "", 0);
        } catch (Exception e){
            e.printStackTrace();
        }
        close(rs);
        return data;
        
    }  
    
    public static class course {
        
        private final SimpleStringProperty course = new SimpleStringProperty();
        private final SimpleStringProperty instructor = new SimpleStringProperty();

        private course(String course, String instructor) {
            this.course.set(course);
            this.instructor.set(instructor);
        }

        public String getCourse() {
            return course.get();
        }

        public void setCourse(String course) {
            this.course.set(course);
        }

        public String getInstructor() {
            return instructor.get();
        }

        public void setInstructor(String email) {
            this.instructor.set(email);
        }

    }
    
    
}
