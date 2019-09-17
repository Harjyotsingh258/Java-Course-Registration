
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import other_classes.T;

/**
 *
 * @author groot
 */
public class AdminUi {

    private final String myEmail;
    private TableView table;
    private ObservableList<AdminObject> dbData;
    private Stage stage;

    public AdminUi(String email) {
        this.myEmail = email;
    }

    public void start() {
        stage = new Stage();

        Label title = new Label("LIST OF AVAILABLE ADMINS"
                + "(" + Db.count("admin", "firstName", "", new String[]{}) + ")");
        title.setStyle("-fx-font: normal bold 20px 'serif'");

        Button btnBack = new Button("Back to Dashboard");
        btnBack.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        btnBack.setOnAction((ActionEvent event) -> {
            stage.close();
            new AdminInfo(myEmail).start();
        });
        
        //table
        table = new TableView();

        ResultSet rs = Db.loadTable("admin", new String[]{}, "", new String[]{});
        
        Callback<TableColumn, TableCell> stringCellFactory =
                (TableColumn p) -> {
                    MyStringTableCell cell = new MyStringTableCell();
                    cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                    return cell;
        };

        dbData = FXCollections.observableArrayList(loadData(rs));

        TableColumn idCol = new TableColumn("ID");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(
                new PropertyValueFactory<AdminObject, String>("id"));
        idCol.setCellFactory(stringCellFactory);

        TableColumn fNameCol = new TableColumn("First Name");
        fNameCol.setMinWidth(300);
        fNameCol.setCellValueFactory(
                new PropertyValueFactory<AdminObject, String>("firstName"));
        fNameCol.setCellFactory(stringCellFactory);

        TableColumn sNameCol = new TableColumn("Second Name");
        sNameCol.setMinWidth(200);
        sNameCol.setCellValueFactory(
                new PropertyValueFactory<AdminObject, String>("secondName"));
        sNameCol.setCellFactory(stringCellFactory);

        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(50);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<AdminObject, String>("email"));
        emailCol.setCellFactory(stringCellFactory);
        
        TableColumn passCol = new TableColumn("Password");
        passCol.setMinWidth(200);
        passCol.setCellValueFactory(
                new PropertyValueFactory<AdminObject, String>("password"));
        passCol.setCellFactory(stringCellFactory);

        TableColumn tokenCol = new TableColumn("Token");
        tokenCol.setMinWidth(300);
        tokenCol.setCellValueFactory(
                new PropertyValueFactory<AdminObject, String>("token"));
        tokenCol.setCellFactory(stringCellFactory);


        //Filling up tableView with data
        table.setItems(dbData);
        table.getColumns().addAll(idCol, fNameCol, sNameCol,
                emailCol, passCol, tokenCol);

        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setStyle("-fx-background-color: BEIGE;");
        vBox.getChildren().addAll(title, table,btnBack);

        Scene scene = new Scene(vBox);
        //Setting title to the Stage 
        stage.setTitle("Admins");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }

    private List<AdminObject> loadData(ResultSet rs) {
        List<AdminObject> data = new ArrayList<>();
        try {
            if (rs.next()) {
                int i = 0;
                do {
                    String id = rs.getString("_id");
                    String firstName = rs.getString("firstName");
                    String secondName = rs.getString("secondName");
                    String email = rs.getString("email");
                    String token = rs.getString("token");
                    String password = rs.getString("password");

                    AdminObject object = new AdminObject(id, firstName, secondName, email,
                            token, password);
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

    public static class AdminObject {

        private final SimpleStringProperty id = new SimpleStringProperty();
        private final SimpleStringProperty firstName = new SimpleStringProperty();
        private final SimpleStringProperty secondName = new SimpleStringProperty();
        private final SimpleStringProperty email = new SimpleStringProperty();
        private final SimpleStringProperty token = new SimpleStringProperty();
        private final SimpleStringProperty password = new SimpleStringProperty();

        private AdminObject(String id, String firstName, String secondName, String email,
                String token, String password) {
            this.id.set(id);
            this.firstName.set(firstName);
            this.secondName.set(secondName);
            this.email.set(email);
            this.token.set(token);
            this.password.set(password);

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
        
        public String getToken(){
            return token.get();
        }
        
        public String getPassword(){
            return password.get();
        }
    }

    class MyStringTableCell extends TableCell<AdminObject, String> {

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
            //Todo delete Admin
//            String id = dbData.get(index).getId();
//            Admin.delete(stage, id, myEmail);
           
        }
    }
}
