package students;

import connection.Db;
import connection.DbConnection;

import static connection.DbConnection.close;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

public class DetailedSchedule {

    private final String studentId;
    private TableView tableView;

    public DetailedSchedule(String studentID) {
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
        
        Label titleLabel = new Label("Registration > Detailed Schedule");
        titleLabel.setAlignment(Pos.TOP_CENTER);
        titleLabel.setStyle("-fx-font: normal bold 20px 'serif' ");
        
        
//        ------------------Table-----------------------

        tableView = new TableView();
        
        ResultSet rs = Db.loadTable("CourseSchedule", new String[]{},"", new String[]{});
        
        ObservableList<Schedule> dbData = FXCollections.observableArrayList(loadData(rs));

        TableColumn idCol = new TableColumn("ID");
        idCol.setMinWidth(150);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Schedule, String>("id"));
        
        
        TableColumn ScheduleCol = new TableColumn("Schedule");
        ScheduleCol.setMinWidth(200);
        ScheduleCol.setCellValueFactory(
                new PropertyValueFactory<Schedule, String>("Schedule"));
        
        TableColumn RoomCol = new TableColumn("RoomNumber");
        RoomCol.setMinWidth(200);
        RoomCol.setCellValueFactory(
                new PropertyValueFactory<Schedule, String>("RoomNumber"));
        
        
        tableView.setItems(dbData);
        tableView.getColumns().addAll(idCol, RoomCol, ScheduleCol);
        
        
        
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, 10, 10, 50));
        vBox.setSpacing(20);
        vBox.setPrefWidth(600);
        vBox.getChildren().addAll(hboxTop, titleLabel, tableView);
        
        
        HBox mainHBox = new HBox();
        mainHBox.setSpacing(20);
        mainHBox.setAlignment(Pos.CENTER);
        mainHBox.setPadding(new Insets(10, 10, 10, 10));
        mainHBox.setStyle("-fx-padding: 10;" + 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + 
                "-fx-border-radius: 10;" + 
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
    
    
    private List<Schedule> loadData(ResultSet rs){
        List<Schedule> data = new ArrayList<>();
        try {
            if (rs.next()) {
                do {
//                    String id = rs.getString("CourseId");
                    String schedule = rs.getString("Schedule");
                    String RoomNumber = rs.getString("Room");
                    Schedule mySchedule = new Schedule(schedule, rs.getString("CourseId"), RoomNumber); 
                    data.add(mySchedule);

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
     
    public static class Schedule {
        
        private final SimpleStringProperty schedule = new SimpleStringProperty();
        private final SimpleStringProperty id = new SimpleStringProperty();
        private final SimpleStringProperty RoomNumber = new SimpleStringProperty();

        private Schedule(String schedule, String id, String RoomNumber) {
            this.schedule.set(schedule);
            this.id.set(id);
            this.RoomNumber.set(RoomNumber);
        }

        public String getSchedule() {
            return schedule.get();
        }

        public void setSchedule(String course) {
            this.schedule.set(course);
        }

        public String getId() {
            return id.get();
        }

        public void setId(String id) {
            this.id.set(id);
        }
        
        public String getRoomNumber() {
            return this.RoomNumber.get();
        }
        
        public void setRoomNumber(String RoomNumber) {
            this.RoomNumber.set(RoomNumber);
        }

    }
    
}
    

