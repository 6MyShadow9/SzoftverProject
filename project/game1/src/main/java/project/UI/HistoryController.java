package project.UI;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class HistoryController {


    @FXML
    private TableView<SavedData> table;

    @FXML
    private TableColumn<SavedData, String> Player1Name;

    @FXML
    private TableColumn<SavedData, String> Player2Name;

    @FXML
    private TableColumn<SavedData, String> WinnerName;

    @FXML
    private TableColumn<SavedData, Long> AmountOfMoves;

    @FXML
    private TableColumn<SavedData, Integer> DateOfGame;

    @FXML
    private void initialize() throws IOException {
        try {
            System.out.println("Initializing HistoryController...");

            if (Player1Name == null) {
                System.out.println("Player1Name column is null");
            } else {
                Player1Name.setCellValueFactory(new PropertyValueFactory<>("Player1Name"));
            }

            if (Player2Name == null) {
                System.out.println("Player2Name column is null");
            } else {
                Player2Name.setCellValueFactory(new PropertyValueFactory<>("Player2Name"));
            }

            if (WinnerName == null) {
                System.out.println("WinnerName column is null");
            } else {
                WinnerName.setCellValueFactory(new PropertyValueFactory<>("WinnerName"));
            }

            if (AmountOfMoves == null) {
                System.out.println("AmountOfMoves column is null");
            } else {
                AmountOfMoves.setCellValueFactory(new PropertyValueFactory<>("AmountOfMoves"));
            }

            if (DateOfGame == null) {
                System.out.println("DateOfGame column is null");
            } else {
                DateOfGame.setCellValueFactory(new PropertyValueFactory<>("DateOfGame"));
            }

            ObservableList<SavedData> observableList = FXCollections.observableArrayList();
            observableList.addAll(readData());
            table.setItems(observableList);
            System.out.println("Initialization successful.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Initialization failed.");
        }
    }

    private List<SavedData> readData() throws IOException {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .readValue(this.getClass().getResourceAsStream("/Data/history.json"),
                        new TypeReference<List<SavedData>>() {
                        });
    }

    @FXML
    public void handleReturn(ActionEvent event) throws IOException {
        Logger.info("Switching to main screen...");

        URL resource = getClass().getResource("/GameStart.fxml");
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