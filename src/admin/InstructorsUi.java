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

public class InstructorsUi {

    private final String myEmail;
    private TableView table;
    private ObservableList<Instructor> dbData;

    public InstructorsUi(String email) {
        this.myEmail = email;
    }

    public void start() {
        Stage stage = new Stage();

        Label title = new Label("LIST OF AVAILABLE INSTRUCTORS "
                + "(" + Db.count("instructors", "firstName", "", new String[]{}) + ")");
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

        ResultSet rs = Db.loadTable("instructors", new String[]{}, "", new String[]{});

        Callback<TableColumn, TableCell> stringCellFactory
                = (TableColumn p) -> {
                    MyStringTableCell cell = new MyStringTableCell();
                    cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                    return cell;
        };

        dbData = FXCollections.observableArrayList(loadData(rs));

        TableColumn idCol = new TableColumn("ID");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Instructor, String>("id"));
        idCol.setCellFactory(stringCellFactory);

        TableColumn fNameCol = new TableColumn("First Name");
        fNameCol.setMinWidth(100);
        fNameCol.setCellValueFactory(
                new PropertyValueFactory<Instructor, String>("firstName"));
        fNameCol.setCellFactory(stringCellFactory);

        TableColumn sNameCol = new TableColumn("Second Name");
        sNameCol.setMinWidth(100);
        sNameCol.setCellValueFactory(
                new PropertyValueFactory<Instructor, String>("secondName"));
        sNameCol.setCellFactory(stringCellFactory);

        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Instructor, String>("email"));
        emailCol.setCellFactory(stringCellFactory);

        TableColumn depCol = new TableColumn("Department");
        depCol.setMinWidth(300);
        depCol.setCellValueFactory(
                new PropertyValueFactory<Instructor, String>("department"));
        depCol.setCellFactory(stringCellFactory);

        TableColumn passCol = new TableColumn("Password");
        passCol.setMinWidth(100);
        passCol.setCellValueFactory(
                new PropertyValueFactory<Instructor, String>("password"));
        passCol.setCellFactory(stringCellFactory);

        TableColumn addRoleCol = new TableColumn("Additional Role");
        addRoleCol.setMinWidth(50);
        addRoleCol.setCellValueFactory(
                new PropertyValueFactory<Instructor, String>("addRole"));
        addRoleCol.setCellFactory(stringCellFactory);

        //Filling up tableView with data
        table.setItems(dbData);
        table.getColumns().addAll(idCol, fNameCol, sNameCol, emailCol, depCol, passCol, addRoleCol);

        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setStyle("-fx-background-color: BEIGE;");
        vBox.getChildren().addAll(title, subTitle, table,btnBack);

        Scene scene = new Scene(vBox);
        //Setting title to the Stage 
        stage.setTitle("Instuctors");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }

    private List<Instructor> loadData(ResultSet rs) {
        List<Instructor> data = new ArrayList<>();
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
                    String addRole = rs.getString("additionalRole");
                    Instructor object = new Instructor(id, firstName, secondName, email,
                            department, password, addRole);
                    data.add(object);

                } while (rs.next());
            }

        } catch (SQLException e) {
            T.show("Database Error", e + "", 0);
        } catch (Exception e) {
            System.out.println(e);
        }

        close(rs);
        return data;

    }

    public static class Instructor {

        private final SimpleStringProperty id = new SimpleStringProperty();
        private final SimpleStringProperty firstName = new SimpleStringProperty();
        private final SimpleStringProperty secondName = new SimpleStringProperty();
        private final SimpleStringProperty email = new SimpleStringProperty();
        private final SimpleStringProperty department = new SimpleStringProperty();
        private final SimpleStringProperty password = new SimpleStringProperty();
        private final SimpleStringProperty addRole = new SimpleStringProperty();

        private Instructor(String id, String firstName, String secondName, String email,
                String department, String password, String addRole) {
            this.id.set(id);
            this.firstName.set(firstName);
            this.secondName.set(secondName);
            this.email.set(email);
            this.department.set(department);
            this.password.set(password);
            this.addRole.set(addRole);
        }

        public String getSecondName() {
            return secondName.get();
        }

        public String getId() {
            return id.get();
        }

        public String getFirstName() {
            return firstName.get();
        }

        public String getDepartment() {
            return department.get();
        }

        public String getPassword() {
            return password.get();
        }

        public String getAddRole() {
            return addRole.get();
        }

        public String getEmail() {
            return email.get();
        }

        public void setEmail(String email) {
            this.email.set(email);
        }

    }

    class MyStringTableCell extends TableCell<Instructor, String> {

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
            String instEmail = dbData.get(index).getEmail();
            new AssignCourse(instEmail).start();
        }
    }

}
