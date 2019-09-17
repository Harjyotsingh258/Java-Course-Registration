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
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import other_classes.T;
import students.Student;


public class StudentsUi {

    private final String myEmail;
    private TableView table;
    private ObservableList<StudentObject> dbData;
    private Stage stage;

    public StudentsUi(String email) {
        this.myEmail = email;
    }
    
    public void start(){
        stage = new Stage();
        
        Label title = new Label("LIST OF AVAILABLE STUDENTS "
                + "(" + Db.count("students", "firstName", "", new String[]{}) + ")");
        title.setStyle("-fx-font: normal bold 20px 'serif'");
        
        Button btnBack = new Button("Back to Dashboard");
        btnBack.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        btnBack.setOnAction((ActionEvent event) -> {
            stage.close();
            new AdminInfo(myEmail).start();
        });
        
        Label subTitle = new Label("Click on specific row for more options");
  
        //table
        table = new TableView();
        
        ResultSet rs = Db.loadTable("students", new String[]{}, "", new String[]{});
        
        Callback<TableColumn, TableCell> stringCellFactory
                = new Callback<TableColumn, TableCell>() {
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
                new PropertyValueFactory<StudentObject, String>("id"));
        idCol.setCellFactory(stringCellFactory);

        TableColumn fNameCol = new TableColumn("First Name");
        fNameCol.setMinWidth(100);
        fNameCol.setCellValueFactory(
                new PropertyValueFactory<StudentObject, String>("firstName"));
        fNameCol.setCellFactory(stringCellFactory);
        
        TableColumn sNameCol = new TableColumn("Second Name");
        sNameCol.setMinWidth(100);
        sNameCol.setCellValueFactory(
                new PropertyValueFactory<StudentObject, String>("secondName"));
        sNameCol.setCellFactory(stringCellFactory);
        
        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<StudentObject, String>("email"));
        emailCol.setCellFactory(stringCellFactory);

        TableColumn depCol = new TableColumn("Department");
        depCol.setMinWidth(300);
        depCol.setCellValueFactory(
                new PropertyValueFactory<StudentObject, String>("department"));
        depCol.setCellFactory(stringCellFactory);
        
        TableColumn passCol = new TableColumn("Password");
        passCol.setMinWidth(200);
        passCol.setCellValueFactory(
                new PropertyValueFactory<StudentObject, String>("password"));
        passCol.setCellFactory(stringCellFactory);
        
        TableColumn courseCol = new TableColumn("Course");
        courseCol.setMinWidth(300);
        courseCol.setCellValueFactory(
                new PropertyValueFactory<StudentObject, String>("course"));
        courseCol.setCellFactory(stringCellFactory);
        
        
        TableColumn lNameCol = new TableColumn("Last Name");
        lNameCol.setMinWidth(100);
        lNameCol.setCellValueFactory(
                new PropertyValueFactory<StudentObject, String>("lastName"));
        lNameCol.setCellFactory(stringCellFactory);
        
        
        TableColumn studentIdCol = new TableColumn("Studnet ID");
        studentIdCol.setMinWidth(100);
        studentIdCol.setCellValueFactory(
                new PropertyValueFactory<StudentObject, String>("studentId"));
        studentIdCol.setCellFactory(stringCellFactory);
        
        
        TableColumn regDateCol = new TableColumn("Registration Date");
        regDateCol.setMinWidth(100);
        regDateCol.setCellValueFactory(
                new PropertyValueFactory<StudentObject, String>("regDate"));
        regDateCol.setCellFactory(stringCellFactory);
        
        //Filling up tableView with data
        table.setItems(dbData);
        table.getColumns().addAll(idCol,studentIdCol,fNameCol,sNameCol,lNameCol,
                emailCol,passCol,courseCol,depCol,regDateCol);
        
        
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setStyle("-fx-background-color: BEIGE;");
        vBox.getChildren().addAll(title,subTitle,table,btnBack);

        Scene scene = new Scene(vBox);
        //Setting title to the Stage 
        stage.setTitle("Instuctor Courses");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }
    
    private List<StudentObject> loadData(ResultSet rs){
        List<StudentObject> data = new ArrayList<>();
        try {
            if (rs.next()) {
                int i = 0;
                do {
                    String id = rs.getString("_id");
                    String firstName = rs.getString("firstName");
                    String secondName = rs.getString("secondName");
                    String email = rs.getString("email");
                    String department = rs.getString("departiment");
                    String password = rs.getString("password");
                    String lastName = rs.getString("lastName");
                    String studentId = rs.getString("student_id");
                    String regDate = rs.getString("regDate");
                    String course = rs.getString("course");
                    
                    StudentObject object = new StudentObject(id,  firstName,  secondName,  email, 
                 department,  password, lastName,  studentId,  regDate,course); 
                    data.add(object);

                } while (rs.next());
            }
            
        } catch (SQLException e) {
            T.show("Database Error", e + "", 0);
        } catch (Exception e){
            System.out.println(e);
        }
        
        close(rs);
        return data;
        
    }    
    
    public static class StudentObject {
        
        private final SimpleStringProperty id = new SimpleStringProperty();
        private final SimpleStringProperty firstName = new SimpleStringProperty();
        private final SimpleStringProperty secondName = new SimpleStringProperty();
        private final SimpleStringProperty email = new SimpleStringProperty();
        private final SimpleStringProperty department = new SimpleStringProperty();
        private final SimpleStringProperty password = new SimpleStringProperty();
        private final SimpleStringProperty lastName = new SimpleStringProperty();
        private final SimpleStringProperty studentId = new SimpleStringProperty();
        private final SimpleStringProperty regDate = new SimpleStringProperty();
        private final SimpleStringProperty course = new SimpleStringProperty();

        private StudentObject(String id, String firstName, String secondName, String email,
                String department, String password, String lastName,
                String studentId, String regDate,String course) {
            this.id.set(id);
            this.firstName.set(firstName);
            this.secondName.set(secondName);
            this.email.set(email);
            this.department.set(department);
            this.password.set(password);           
            this.lastName.set(lastName);
            this.studentId.set(studentId);
            this.regDate.set(regDate);
            this.course.set(course);
            
        }
        public String getId(){
            return id.get();
        }
        
        public String getFirstName(){
            return firstName.get();
            
        }
        
        public String getSecondName(){
            return secondName.get();
        }
        
        public String getEmail(){
            return email.get();
        }
        
        public String getDepartment(){
            return department.get();
        }
        
        public String getLastName(){
            return lastName.get();
        }
        
        
        public String getStudentId() {
            return studentId.get();
        }
        
        public String getRegDate(){
            return regDate.get();
        }
        
        public String getCourse(){
            return course.get();
        }
        
        public String getPassword(){
            return password.get();
        }
    }
    
        class MyStringTableCell extends TableCell<StudentObject, String> {
 
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? null : getString());
            setGraphic(null);
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }
    
    class MyEventHandler implements EventHandler<MouseEvent> {
 
        @Override
        public void handle(MouseEvent t) {
            TableCell c = (TableCell) t.getSource();
            int index = c.getIndex();
            String studentId = dbData.get(index).getStudentId();
            Student.delete(stage, studentId, myEmail);
        }
    }
}
