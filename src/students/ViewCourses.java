package students;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import other_classes.T;





public class ViewCourses {

    private final String studentId;
    private Text studentName;
    private TableView tableView;

    public ViewCourses(String studentId) {
        this.studentId = studentId;
    }
    
    public void start(){
        Stage stage = new Stage();
        
        studentName = new Text();
        studentName.setText(Student.get(studentId, "firstName") + " "
                + Student.get(studentId, "SecondName") + " " + 
                Student.get(studentId, "lastName"));
        
        tableView = new TableView();
        
        ResultSet rs = Db.loadTable("student_courses", new String[]{},
                        "studentId = ?", new String[]{studentId});
        
        ObservableList<StudentCourse> dbData = FXCollections.observableArrayList(loadData(rs));

        TableColumn courseCol = new TableColumn("Course");
        courseCol.setMinWidth(300);
        courseCol.setCellValueFactory(
                new PropertyValueFactory<StudentCourse, String>("course"));

        TableColumn studentIDCol = new TableColumn("Student ID");
        studentIDCol.setMinWidth(100);
        studentIDCol.setCellValueFactory(
                new PropertyValueFactory<StudentCourse, String>("studentId"));
        //Filling up tableView with data
        tableView.setItems(dbData);
        tableView.getColumns().addAll(courseCol, studentIDCol);
        
        GridPane sideMenu = new SideMenu().get(stage, studentId, 0);
        
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, 10, 10, 0));
        vBox.setSpacing(20);
        vBox.setPrefWidth(400);
        vBox.getChildren().add(tableView);
        
        
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
        hbox.getChildren().addAll(sideMenu,vBox);
        
        Scene scene = new Scene(hbox);
        //Setting title to the Stage 
        stage.setTitle("View Courses");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }
    
    private List<StudentCourse> loadData(ResultSet rs){
        List<StudentCourse> data = new ArrayList<>();
        try {
            if (rs.next()) {
                int i = 0;
                do {
                    i++;
                    System.out.println("connection.Db.loadTable(): " + rs.getString("studentId"));
                    String course = rs.getString("course");
                    String myId = rs.getString("studentId");
                    StudentCourse myCourse = new StudentCourse(course, myId);   
                    data.add(myCourse);
                    System.out.println(i);

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
    
    public static class StudentCourse {
        
        private final SimpleStringProperty course = new SimpleStringProperty();
        private final SimpleStringProperty studentId = new SimpleStringProperty();

        private StudentCourse(String course, String studentId) {
            this.course.set(course);
            this.studentId.set(studentId);
        }

        public String getCourse() {
            return course.get();
        }

        public void setCourse(String course) {
            this.course.set(course);
        }

        public String getStudentId() {
            return studentId.get();
        }

        public void setStudentId(String studentId) {
            this.studentId.set(studentId);
        }

    }
    
}
