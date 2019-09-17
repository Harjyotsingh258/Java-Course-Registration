package other_classes;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class T {

    public static void show(String title, String message, int status) {
        Alert alert = null;
        switch (status) {
            case 0:
                // an error occured
                alert = new Alert(AlertType.ERROR);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);

                alert.showAndWait();
                break;

            case 1:
                // a nice message
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);

                alert.showAndWait();
                break;
            default:
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);

                alert.showAndWait();
                break;
        }

    }

    public static void show(String success) {
        // defined success
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(success);

        alert.showAndWait();
    }

    public static void show(String title, String error) {
        // defined error message
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(error);

        alert.showAndWait();
    }

    public static void show(String warning, int anything) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(warning);

        alert.showAndWait();
    }

}
