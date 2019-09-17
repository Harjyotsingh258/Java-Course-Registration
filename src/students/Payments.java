/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
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



public class Payments {

    int i = 0;
    
    private final String studentId;
    private TableView tableView;

    public Payments(String studenntID) {
        this.studentId = studenntID;
    }
    
    public void start(){
        Stage stage = new Stage();
        
        GridPane sideMenu = new SideMenu().get(stage, studentId, 2);
        
        Image image = new Image(getClass().getResourceAsStream("avatar.png"));
        Label activeButton = new Label("");
        activeButton.setGraphic(new ImageView(image));
        
        Text name = new Text(Student.get(studentId, "firstName") + " " + Student.get(studentId, "SecondName"));
        
        HBox hboxTop = new HBox();
        hboxTop.setAlignment(Pos.TOP_RIGHT);
        hboxTop.setSpacing(20);
        hboxTop.getChildren().addAll(activeButton, name);
        
        Hyperlink Registration = new Hyperlink();
        Registration.setText("Registration");
        Registration.setOnAction((ActionEvent e) -> {
                    stage.close();
                    new ViewCourses(studentId).start();
                });
        
        
        
        Label titleLabel = new Label("Payments");
        titleLabel.setAlignment(Pos.TOP_CENTER);
        titleLabel.setStyle("-fx-font: normal bold 20px 'serif' ");
        
       
    
        
        
        
        tableView = new TableView();
        tableView.maxHeight(400);
        ResultSet rs = Db.loadTable("student_courses", new String[]{},
                        "studentId = ?", new String[]{studentId});
        
        ObservableList<StudentCourses> dbData = FXCollections.observableArrayList(loadData(rs));

        TableColumn courseCol = new TableColumn("Course");
        courseCol.setMinWidth(300);
        courseCol.setCellValueFactory(
                new PropertyValueFactory<ViewCourses.StudentCourse, String>("course"));

        
        tableView.setItems(dbData);
        tableView.getColumns().addAll(courseCol);
        
        
         Label Description = new Label("The courses are to be announced as on the\n "
                + "price of 2000 CAD for each course.\n"
                + "you are taking the following " + i + " courses.");
        
         Label Description2 = new Label("The total will be : " + i +" * 2000 CAD = " + (i*2000));
        
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, 10, 10, 50));
        vBox.setSpacing(20);
        vBox.setPrefWidth(400);
        vBox.getChildren().addAll(hboxTop, titleLabel, Description, tableView, Description2);
        
        
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
        mainHBox.getChildren().addAll(sideMenu, vBox);
        
        Scene scene = new Scene(mainHBox);
        //Setting title to the Stage 
        stage.setTitle("Edit Page");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
        
    }
   
    
    
        private List<StudentCourses> loadData(ResultSet rs){
            List<StudentCourses> data = new ArrayList<>();
            try {
                if (rs.next()) {

                    do {
                        i++;
                        System.out.println("connection.Db.loadTable(): " + rs.getString("studentId"));
                        String course = rs.getString("course");
                        String myId = rs.getString("studentId");
                        StudentCourses myCourse = new StudentCourses(course, myId);   
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
        
         public static class StudentCourses {
        
            private final SimpleStringProperty course = new SimpleStringProperty();
            private final SimpleStringProperty studentId = new SimpleStringProperty();

                private StudentCourses(String course, String studentId) {
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
