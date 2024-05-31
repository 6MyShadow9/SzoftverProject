package project.UI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.URL;

public class GameStartScreenController {
    @FXML
    private TextField playerOneField;

    @FXML
    private TextField playerTwoField;

    @FXML
    private Alert emptyNamesAlert = new Alert(Alert.AlertType.WARNING,"Please give names to the players!");

    @FXML
    private void handleStartGame(ActionEvent event) throws IOException {
        Logger.debug(playerOneField.getText(), playerTwoField.getText());
        if (playerOneField.getText().isBlank() || playerTwoField.getText().isBlank()) {
            Logger.warn("Please add a name!");
            emptyNamesAlert.show();
        }
        else{
            Logger.info("Player one name: {}", playerOneField.getText());
            Logger.info("Player two name: {}", playerTwoField.getText());

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Game.fxml"));
            Parent root = fxmlLoader.load();
            GameController controller = fxmlLoader.getController();
            controller.setPlayerNames(playerOneField.getText(), playerTwoField.getText());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }


    @FXML
    private void handleExit(ActionEvent event) {
        Logger.debug("Exiting...");
        Platform.exit();
    }

    @FXML
    public void handleHistory(ActionEvent event) throws IOException {
        Logger.info("Switching to history...");

        URL resource = getClass().getResource("/history.fxml");
        if (resource == null) {
            System.out.println("Resource not found");
        }
        else{
            System.out.println("Resource found " + resource );
        }

        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}