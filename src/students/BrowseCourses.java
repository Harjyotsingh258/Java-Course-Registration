
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

public class BrowseCourses {

    private final String studentId;
    private Text studentName;
    private TableView tableView;

    public BrowseCourses(String studentId) {
        this.studentId = studentId;
    }
    
    public void start(){
        Stage stage = new Stage();
        
        Image image = new Image(getClass().getResourceAsStream("avatar.png"));
        Label activeButton = new Label("");
        activeButton.setGraphic(new ImageView(image));
        
        Text name = new Text(Student.get(studentId, "firstName") + " " + Student.get(studentId, "SecondName"));
        
        HBox hboxTop = new HBox();
        hboxTop.setAlignment(Pos.TOP_RIGHT);
        hboxTop.setSpacing(20);
        hboxTop.getChildren().addAll(activeButton, name);
        
        tableView = new TableView();
        
        Label title = new Label("Registration > Browse Course");
        title.setStyle("-fx-font: normal bold 20px 'serif' ");
        Label subTitle = new Label("This table displays all the courses available on different departments.\n"
                + "To add or drop courses, kindly click registration on the left panel");
        
        ResultSet rs = Db.loadTable("courses", new String[]{}, "", new String[]{});
        
        ObservableList<Course> dbData = FXCollections.observableArrayList(loadData(rs));

        TableColumn idCol = new TableColumn("ID");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Course, String>("id"));

        TableColumn courseCol = new TableColumn("Course");
        courseCol.setMinWidth(300);
        courseCol.setCellValueFactory(
                new PropertyValueFactory<Course, String>("course"));
        
        TableColumn departmentCol = new TableColumn("Department");
        departmentCol.setMinWidth(200);
        departmentCol.setCellValueFactory(
                new PropertyValueFactory<Course, String>("department"));
        //Filling up tableView with data
        tableView.setItems(dbData);
        tableView.getColumns().addAll(idCol, courseCol,departmentCol);
        
        GridPane sideMenu = new SideMenu().get(stage, studentId, 1);
        
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, 10, 10, 0));
        vBox.setSpacing(20);
        vBox.setPrefWidth(600);
        vBox.getChildren().addAll(hboxTop,title,subTitle,tableView);
        
        
        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setStyle("-fx-background-color: BEIGE;");
        hbox.getChildren().addAll(sideMenu,vBox);
        
        Scene scene = new Scene(hbox);
        //Setting title to the Stage 
        stage.setTitle("Browse Courses");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }
    
    private List<Course> loadData(ResultSet rs){
        List<Course> data = new ArrayList<>();
        try {
            if (rs.next()) {
//              int i = 0;
                do {
                    System.out.println("connection.Db.loadTable(): " + rs.getString("_id"));
                    String course = rs.getString("course");
                    String myId = rs.getString("_id");
                    String department = rs.getString("departiment");
                    Course myCourse = new Course(course,department, myId); 
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
    
    public static class Course {
        
        private final SimpleStringProperty course = new SimpleStringProperty();
        private final SimpleStringProperty department = new SimpleStringProperty();
        private final SimpleStringProperty id = new SimpleStringProperty();

        private Course(String course, String department,String id) {
            this.course.set(course);
            this.department.set(department);
            this.id.set(id);
        }

        public String getCourse() {
            return course.get();
        }

        public void setCourse(String course) {
            this.course.set(course);
        }

        public String getId() {
            return id.get();
        }

        public void setId(String id) {
            this.id.set(id);
        }
        
        public void setDepartment(String department){
            this.department.set(department);
        }
        
        public String getDepartment(){
            return department.get();
        }

    }
    
}
