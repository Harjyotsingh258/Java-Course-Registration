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
import javafx.scene.control.Hyperlink;
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

public class DepartimenstUi {

    private final String email;
    private TableView rightTable;
    private ObservableList<Department> dbData;
    private String depId;
    private TextField depText;
    private Label depName;

    public DepartimenstUi(String email) {
        this.email = email;
    }
    
    public void start(){
        Stage stage = new Stage();
        
        
        Label title = new Label("Actions");
        
        Label departmentNameLabel = new Label("Department Name");
        depText = new TextField();
        depText.textProperty().addListener((ObservableValue<? extends String> observable,
                String oldValue, String newValue) -> {
            depName.setText(newValue);
        });
        
        Button btnAdd = new Button("Add New");
        btnAdd.setOnAction((ActionEvent event) -> {
            // TODO add your handling code here:
            String dep = depText.getText();
            if (dep.contentEquals("")) {
                T.show("Error", "You must fill in departiment field", 0);
                return;
            }
            Departiments.add(stage, email, dep);
        });
        
        Button btnEdit = new Button("Edit");
        btnEdit.setOnAction((ActionEvent event) -> {
            if (depText.getText().contentEquals("")) {
                T.show("Error", "Nothing to edit", 0);
                return;
            }
            Departiments.edit(stage, depId, depText.getText(), email);
        });
        
        Button btnDelete = new Button("Delete");
        btnDelete.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        btnDelete.setOnAction((ActionEvent event) -> {
            if (depText.getText().contentEquals("")) {
                T.show("Error", "Nothing to delete", 0);
                return;
            }
            Departiments.delete(stage, depId, email);
        });
        
        HBox leftHBox = new HBox();
        leftHBox.setSpacing(20);
        leftHBox.setPadding(new Insets(0, 20, 0, 20));
        leftHBox.getChildren().addAll(btnAdd, btnEdit, btnDelete);
        
        Hyperlink viewCourses = new Hyperlink("View Courses");
        depName = new Label("Department Name");
        Hyperlink viewInstructors = new Hyperlink("View Instructor");
        Hyperlink viewStudent = new Hyperlink("View Students");
        
        Button btnBack = new Button("Back to Dashboard");
        btnBack.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        btnBack.setOnAction((ActionEvent event) -> {
            stage.close();
            new AdminInfo(email).start();
        });
        
        HBox openViewBox = new HBox();
        openViewBox.autosize();
        openViewBox.getChildren().addAll(viewCourses,viewStudent,viewInstructors);
        
        VBox box = new VBox();
        box.autosize();
        box.getChildren().addAll(depName,openViewBox);
        box.setStyle("-fx-padding: 10;" + 
                      "-fx-border-style: solid inside;" + 
                      "-fx-border-width: 2;" +
                      "-fx-border-insets: 5;" + 
                      "-fx-border-radius: 5;" + 
                      "-fx-border-color: black;");
        
        VBox leftBox = new VBox();
        leftBox.setStyle("-fx-padding: 10;" + 
                      "-fx-border-style: solid inside;" + 
                      "-fx-border-width: 2;" +
                      "-fx-border-insets: 5;" + 
                      "-fx-border-radius: 5;" + 
                      "-fx-border-color: black;");
        leftBox.setSpacing(20);
        leftBox.getChildren().addAll(title, departmentNameLabel, depText,
                leftHBox,box,btnBack);
        
        //right view
        rightTable = new TableView();
 
        Label rightTitle = new Label("Available Departments");
        Label rightSubTitle = new Label("Click on specific row to view, edit or delete");
        rightSubTitle.setStyle("-fx-text-fill: red;");
        
        ResultSet rs = Db.loadTable("departiments", new String[]{}, "", new String[]{});
        
        Callback<TableColumn, TableCell> stringCellFactory =
                new Callback<TableColumn, TableCell>() {
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
        stage.setTitle("Department");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }
    private List<Department> loadData(ResultSet rs){
        List<Department> data = new ArrayList<>();
        try {
            if (rs.next()) {
                int i = 0;
                do {
                    String departmentId = rs.getString("depId");
                    String _Id = rs.getString("_id");
                    String department = rs.getString("departiment");
                    Department object = new Department(_Id, departmentId, department); 
                    data.add(object);

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
    
    public static class Department {
        
        private final SimpleStringProperty departmentID = new SimpleStringProperty();
        private final SimpleStringProperty department = new SimpleStringProperty();
        private final SimpleStringProperty id = new SimpleStringProperty();

        private Department(String id, String depId, String department) {
            this.departmentID.set(depId);
            this.department.set(department);
            this.id.set(id);
        }

        public String getDepartmentId() {
            return departmentID.get();
        }

        public void setDepartmentId(String course) {
            this.departmentID.set(course);
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
    
        class MyStringTableCell extends TableCell<Department, String> {
 
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
            depId = dbData.get(index).getDepartmentId();
            String department = dbData.get(index).getDepartment();
            depText.setText(department);
            depName.setText(department);
        }
    }
}
