
package students;

import admin.Courses;
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
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import other_classes.T;


public class AddDropCourses {

    private final String studentId;
    private ChoiceBox courseChoiceBox;
    private VBox delCourse;
    private Label delTitle;
    private TableView tableView = new TableView();
    private ObservableList<StudentCourse> dbData;
    private String tableSelectedCourse;
   
    public AddDropCourses(String studentID){
        this.studentId = studentID;
    }
    
    public void start(){
        Stage stage = new Stage();
        
        GridPane sideMenu = new SideMenu().get(stage, studentId, 1);
        
        
        Image image = new Image(getClass().getResourceAsStream("avatar.png"));
        Label activeButton = new Label("");
        activeButton.setGraphic(new ImageView(image));
        
        Text name = new Text(Student.get(studentId, "firstName") + " " + Student.get(studentId, "SecondName"));
        
        HBox hboxTop = new HBox();
        hboxTop.setAlignment(Pos.TOP_RIGHT);
        hboxTop.setSpacing(20);
        hboxTop.getChildren().addAll(activeButton, name);
        
        Label title = new Label("Registration > Add/Drop Course");
        title.setStyle("-fx-font: normal bold 20px 'serif' ");
        
        //center view
        Label title1 = new Label("Add or delete course. "
                + "\nPlease select course from the table on the right");
        //Choice box for location 
        courseChoiceBox = new ChoiceBox(); 
        ObservableList<String> courses = FXCollections.observableArrayList( 
           Courses.loadSelectableCourses()); 
        courseChoiceBox.getItems().addAll(courses);
        Button btnAdd = new Button("Add");
        btnAdd.setOnAction((ActionEvent event) -> {
            String addCourse = courseChoiceBox.getSelectionModel().getSelectedItem().toString();
            if (addCourse.contentEquals("Select Course from the Available Courses in the System")) {
                T.show("", "You must select the course you want to add");
                return;
            }
            Courses.add(stage, studentId, addCourse);
        });
        
        Button btnDel = new Button("Delete Course");
        btnDel.setPadding(new Insets(5, 0, 10, 5));
        btnDel.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        
        btnDel.setOnAction((ActionEvent event) -> {
            String theCourse = tableView.getSelectionModel().getSelectedItem().toString();
            showDelView(theCourse);
            Courses.drop(stage, studentId, tableSelectedCourse);
        });
        
        delTitle = new Label("Bachelor of bla bla bla");
        delTitle.setPadding(new Insets(5, 0, 10, 5));
        
        delCourse = new VBox();
        delCourse.setVisible(false);
        delCourse.getChildren().addAll(delTitle, btnDel);
        
        VBox centerViewBox = new VBox();
        centerViewBox.setPadding(new Insets(0, 10, 10, 0));
        centerViewBox.setSpacing(20);
        centerViewBox.setPrefWidth(400);
        centerViewBox.getChildren().addAll(title1, courseChoiceBox, btnAdd, delCourse);
        
        //right view
        ResultSet rs = Db.loadTable("student_courses", new String[]{},
                        "studentId = ?", new String[]{studentId});
        
        dbData = FXCollections.observableArrayList(loadData(rs));
        
        Callback<TableColumn, TableCell> stringCellFactory =
                new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                MyStringTableCell cell = new MyStringTableCell();
                cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                return cell;
            }
        };

        TableColumn courseCol = new TableColumn("Course");
        courseCol.setMinWidth(300);
        courseCol.setCellValueFactory(
                new PropertyValueFactory<ViewCourses.StudentCourse, String>("course"));
        courseCol.setCellFactory(stringCellFactory);

        TableColumn studentIDCol = new TableColumn("Student ID");
        studentIDCol.setMinWidth(100);
        studentIDCol.setCellValueFactory(
                new PropertyValueFactory<ViewCourses.StudentCourse, String>("studentId"));
        studentIDCol.setCellFactory(stringCellFactory);
        
        //Filling up tableView with data
        tableView.setItems(dbData);
        tableView.getColumns().addAll(courseCol, studentIDCol);
        
        VBox rightView = new VBox();
        rightView.setPadding(new Insets(0, 10, 10, 0));
        rightView.setSpacing(20);
        rightView.setPrefWidth(400);
        rightView.getChildren().add(tableView);
        
        //
        HBox subHBox = new HBox();
        subHBox.getChildren().addAll(centerViewBox, rightView);
        
        VBox subVBox = new VBox();
        subVBox.getChildren().addAll(hboxTop, title, subHBox);
        
        //main HBox
        HBox mainHBox = new HBox();
        mainHBox.setSpacing(20);
        mainHBox.setAlignment(Pos.CENTER);
        mainHBox.setPadding(new Insets(10, 10, 10, 10));
        mainHBox.setStyle("-fx-background-color: BEIGE;");
        mainHBox.getChildren().addAll(sideMenu,subVBox);
        
        Scene scene = new Scene(mainHBox);
        //Setting title to the Stage 
        stage.setTitle("View Courses");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }
    
    private void showDelView(String title){
        delCourse.setVisible(true);
        delTitle.setText(title);
        
    }
    
    private List<StudentCourse> loadData(ResultSet rs){
        List<StudentCourse> data = new ArrayList<>();
        try {
            if (rs.next()) {
                int i = 0;
                do {
                    System.out.println("connection.Db.loadTable(): " + rs.getString("studentId"));
                    String course = rs.getString("course");
                    String myId = rs.getString("studentId");
                    StudentCourse myCourse = new StudentCourse(course, myId);   
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
    
    class MyStringTableCell extends TableCell<StudentCourse, String> {
 
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? null : getString());
            setGraphic(null);
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
    
    class MyEventHandler implements EventHandler<MouseEvent> {
 
        @Override
        public void handle(MouseEvent t) {
            TableCell c = (TableCell) t.getSource();
            int index = c.getIndex();
            tableSelectedCourse = dbData.get(index).getCourse();
            System.out.println("id = " + tableSelectedCourse);
            showDelView(tableSelectedCourse);
        }
    }
    
}
