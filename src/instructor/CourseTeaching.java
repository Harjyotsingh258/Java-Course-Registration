package instructor;

import connection.Db;
import static connection.DbConnection.close;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import other_classes.T;
import students.ViewCourses;


public class CourseTeaching {

    private final String email;
    private TableView tableView;

    public CourseTeaching(String email) {
        this.email = email;
    }
    
    public void start(){
        Stage stage = new Stage();
        
        GridPane sideMenu = new SideMenu().get(stage, email, 1);
        
        Image image = new Image(getClass().getResourceAsStream("avatar.png"));
        Label activeButton = new Label("");
        activeButton.setGraphic(new ImageView(image));
        
        Text name = new Text(Instructor.get(email, "firstName") + " " + Instructor.get(email, "secondName"));
        
        HBox hboxTop = new HBox();
        hboxTop.setAlignment(Pos.TOP_RIGHT);
        hboxTop.setSpacing(20);
        hboxTop.getChildren().addAll(activeButton, name);
        
        Label title = new Label("Course Teaching");
        title.setStyle("-fx-font: normal bold 20px 'serif' ");
        Text subTitle = new Text("Course teaching will be added by the admin");
        
        tableView = new TableView();
        
        ResultSet rs = Db.loadTable("instructor_courses", new String[]{}, 
                "instructor = ? ", new String[]{email});
        
        ObservableList<Courses> dbData = FXCollections.observableArrayList(loadData(rs));

        TableColumn courseCol = new TableColumn("Course");
        courseCol.setMinWidth(300);
        courseCol.setCellValueFactory(
                new PropertyValueFactory<Courses, String>("course"));

        TableColumn instructorCol = new TableColumn("Instructor");
        instructorCol.setMinWidth(300);
        instructorCol.setCellValueFactory(
                new PropertyValueFactory<Courses, String>("instructor"));
        //Filling up tableView with data
        tableView.setItems(dbData);
        tableView.getColumns().addAll(courseCol, instructorCol);
        
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, 10, 10, 0));
        vBox.setSpacing(20);
        vBox.setPrefWidth(600);
        vBox.getChildren().addAll(hboxTop, title, subTitle, tableView);
        
        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setStyle("-fx-background-color: BEIGE;");
        hbox.getChildren().addAll(sideMenu,vBox);
        
        Scene scene = new Scene(hbox);
        //Setting title to the Stage 
        stage.setTitle("View Courses");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }
    
    
    private List<Courses> loadData(ResultSet rs){
        List<Courses> data = new ArrayList<>();
        try {
            if (rs.next()) {
                do {
                    String course = rs.getString("course");
                    String myId = rs.getString("instructor");
                    Courses myCourse = new Courses(course, myId);   
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
    
    public static class Courses {
        
        private final SimpleStringProperty course = new SimpleStringProperty();
        private final SimpleStringProperty instructor = new SimpleStringProperty();

        private Courses(String course, String instructor) {
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
