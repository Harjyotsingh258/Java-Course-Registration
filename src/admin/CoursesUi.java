package admin;

import connection.Db;
import static connection.DbConnection.close;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import other_classes.T;
import students.BrowseCourses;


public class CoursesUi {

    private final String email;
    private TableView rightTable;
    private String tableSelectedCourse;
    private ObservableList<Course> dbData;
    private boolean isTableClicked = false;
    private Label courseName;
    private TextField courseText;

    public CoursesUi(String email) {
        this.email = email;
    }
    
    public void start(){
        Stage stage = new Stage();
        
        Label title = new Label("Actions");
        
        Label courseNameLabel = new Label("Course Name");
        courseText = new TextField();
        courseText.textProperty().addListener((ObservableValue<? extends String> observable,
                String oldValue, String newValue) -> {
            courseName.setText(newValue);
        });
        
        Label departmentLabel = new Label("Department in which the course is offered");
        ChoiceBox departmentChoiceBox = new ChoiceBox(); 
        ObservableList<String> departments = FXCollections.observableArrayList( 
           Departiments.getDepartimentsCombobox()); 
        departmentChoiceBox.getItems().addAll(departments);
        
        Label instructionLabel = new Label("Clicking edit button will edit course along selected department");
        
        Button btnAdd = new Button("Add New");
        btnAdd.setOnAction((ActionEvent event) -> {
            // TODO add your handling code here:
            String course = courseText.getText();
            String dep = departmentChoiceBox.getSelectionModel().getSelectedItem().toString();
            if (course.contentEquals("")) {
                T.show("Error", "You must fill in the course field", 0);
                return;
            }
            if (dep.contentEquals("")) {
                T.show("Error", "You must fill in the course field", 0);
                return;
                
            }
            
            if (dep.contentEquals("Select Departiment")) {
                T.show("Error", "You must select the departiment");
                return;
            }
            Courses.add(stage, email, course, dep);
        });
        
        Button btnEdit = new Button("Edit");
        btnEdit.setOnAction((ActionEvent event) -> {
            if (!isTableClicked) {
                T.show("", "You must select the course to delte from the table");
                return;
            }   if (courseText.getText().contentEquals("") && departmentChoiceBox.getSelectionModel().getSelectedItem().toString()
                            .contentEquals("")) {
                T.show("Error", "Either the coursse or department must be filled", 0);
                return;
            }   Courses.edit(stage, email, tableSelectedCourse, courseText.getText(), 
                    departmentChoiceBox.getSelectionModel().getSelectedItem().toString());
        });
        
        Button btnDelete = new Button("Delete");
        btnDelete.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        btnDelete.setOnAction((ActionEvent event) -> {
            if (!isTableClicked) {
                T.show("", "You must select the course to delete from the table");
                return;
            }

            Courses.delete(stage, email, tableSelectedCourse);
        });
        
        HBox leftHBox = new HBox();
        leftHBox.setSpacing(20);
        leftHBox.setPadding(new Insets(0, 20, 0, 20));
        leftHBox.getChildren().addAll(btnAdd, btnEdit, btnDelete);
        
        //labels
//        Label viewStudents = new Label("View Courses");
//        courseName = new Label("Course Name");
//        Label viewInstructors = new Label("View Instructor");
        
        Button btnBack = new Button("Back to Dashboard");
        btnBack.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                new AdminInfo(email).start();
            }
        });
        
//        HBox openViewBox = new HBox();
//        openViewBox.autosize();
//        openViewBox.getChildren().addAll(viewStudents,viewInstructors);
        
//        VBox box = new VBox();
//        box.autosize();
////      box.getChildren().addAll(courseName,openViewBox);
//        box.setStyle("-fx-padding: 10;" + 
//                      "-fx-border-style: solid inside;" + 
//                      "-fx-border-width: 2;" +
//                      "-fx-border-insets: 5;" + 
//                      "-fx-border-radius: 5;" + 
//                      "-fx-border-color: black;");
        
        VBox leftBox = new VBox();
        leftBox.setStyle("-fx-padding: 10;" + 
                      "-fx-border-style: solid inside;" + 
                      "-fx-border-width: 2;" +
                      "-fx-border-insets: 5;" + 
                      "-fx-border-radius: 5;" + 
                      "-fx-border-color: black;");
        leftBox.setSpacing(20);
        leftBox.getChildren().addAll(title, courseNameLabel, courseText, departmentLabel,
                departmentChoiceBox, instructionLabel, leftHBox,btnBack);
        
        //right view
        rightTable = new TableView();
 
        Label rightTitle = new Label("Available Courses");
        Label rightSubTitle = new Label("Click on specific row to view, edit or delete");
        
        
        
        ResultSet rs = Db.loadTable("courses", new String[]{}, "", new String[]{});
        
        Callback<TableColumn, TableCell> stringCellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                MyStringTableCell cell = new MyStringTableCell();
                cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                return cell;
            }
        };
        
        dbData = FXCollections.observableArrayList(loadData(rs));

        TableColumn idCol = new TableColumn("ID");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(
                new PropertyValueFactory<BrowseCourses.Course, String>("id"));
        idCol.setCellFactory(stringCellFactory);

        TableColumn courseCol = new TableColumn("Course");
        courseCol.setMinWidth(300);
        courseCol.setCellValueFactory(
                new PropertyValueFactory<BrowseCourses.Course, String>("course"));
        courseCol.setCellFactory(stringCellFactory);
        
        TableColumn departmentCol = new TableColumn("Department");
        departmentCol.setMinWidth(200);
        departmentCol.setCellValueFactory(
                new PropertyValueFactory<BrowseCourses.Course, String>("department"));
        departmentCol.setCellFactory(stringCellFactory);
        
        //Filling up tableView with data
        rightTable.setItems(dbData);
        rightTable.getColumns().addAll(idCol, courseCol,departmentCol);
        
        VBox rightVBox = new VBox();
        rightVBox.getChildren().addAll(rightTitle,rightSubTitle,rightTable);
        
        HBox mainHBox = new HBox();
        mainHBox.setAlignment(Pos.CENTER);
        mainHBox.setSpacing(20);
        mainHBox.setStyle("-fx-padding: 10;" + 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + 
                "-fx-border-radius: 5;" + 
                "-fx-border-color: gray;"
                + "-fx-background-color: BEIGE;");
        mainHBox.getChildren().addAll(leftBox,rightVBox);
        
        Scene scene = new Scene(mainHBox);
        //Setting title to the Stage 
        stage.setTitle("Courses");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }
    private List<Course> loadData(ResultSet rs){
        List<Course> data = new ArrayList<>();
        try {
            if (rs.next()) {
                int i = 0;
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
    
        class MyStringTableCell extends TableCell<Course, String> {
 
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
            isTableClicked = true;
            courseName.setText(tableSelectedCourse);
            courseText.setText(tableSelectedCourse);
            
        }
    }
    
}
