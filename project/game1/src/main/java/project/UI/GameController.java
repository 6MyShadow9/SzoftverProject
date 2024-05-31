package project.UI;

import project.model.GameModel;
import project.model.Position;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

/**
 * The controller for the JavaFx application.
 */
public class GameController {


    @FXML
    private GridPane board;

    @FXML
    private TextField currentPlayerText;

    @FXML
    private TextField turnCounterText;

    @FXML
    private Button restartButton;

    @FXML
    private Button returnButton;

    private int turnCounter = 1;

    private String playerOneName;

    private String playerTwoName;

    private int firstTurn = 1;

    private GameModel model = new GameModel();
    private MoveManager selector = new MoveManager(model);

    private GameModel.Player firstPlayer = model.getPlayer();

    @FXML
    private void initialize() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }
        selector.phaseProperty().addListener(this::updateSelection);
        turnCounterText.setText("Turn: " + turnCounter);
        Platform.runLater(() -> swapPlayerLabel());
    }

    private void updateSelection(ObservableValue value,
                                 MoveManager.Phase oldPhase,
                                 MoveManager.Phase newPhase) {
        switch (newPhase) {
            case SELECT_FROM, READY_TO_MOVE -> hideSelection(selector.getFrom());
            case SELECT_TO -> showSelection(selector.getFrom());
        }
    }

    private StackPane createSquare(int i, int j) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        var piece = new Circle(50);

        piece.fillProperty().bind(
                new ObjectBinding<Paint>() {
                    {
                        super.bind(model.fieldProperty(i, j));
                    }

                    @Override
                    protected Paint computeValue() {
                        return switch (model.fieldProperty(i, j).get()) {
                            case NONE -> Color.TRANSPARENT;
                            case RED -> Color.RED;
                            case BLUE -> Color.BLUE;
                        };
                    }
                }
        );
        square.getChildren().add(piece);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        if (!model.Victory()) {
            var square = (StackPane) event.getSource();
            var row = GridPane.getRowIndex(square);
            var col = GridPane.getColumnIndex(square);
            var p = new Position(row, col);
            Logger.info("Clicked on square " + p);
            selector.select(p);
            if (selector.isReadyToMove()) {
                selector.makeMove();
                swapPlayerLabel();
                if(firstTurn == 1){
                    firstTurn++;
                }
                if(model.getPlayer() == firstPlayer && firstTurn != 1){
                    turnCounter++;
                    turnCounterText.setText("Turn: " + turnCounter);
                }
            }
            if (model.Victory()) {
                gameOver();
            }
        }
    }

    private void gameOver() {
        Logger.info("The winner is: ", getCurrentPlayersName());
        Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION, getCurrentPlayersName() + " won!");
        gameOverAlert.show();
        restartButton.setVisible(true);
        returnButton.setVisible(true);
        saveGame();
    }

    private void saveGame() {
        Logger.info("Saving...");

        var gameSaver = new Saver();
        gameSaver.saveGame(playerOneName, playerTwoName, getCurrentPlayersName(), turnCounter);

        Logger.info("Game saved!");

    }

    private void swapPlayerLabel() {
            currentPlayerText.setText("Current player: " + getCurrentPlayersName());
    }

    public void setPlayerNames(String playerOneName, String playerTwoName) {
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
    }


    private String getCurrentPlayersName() {
        if (model.getPlayer() == firstPlayer) {
            return playerOneName;
        } else
            return playerTwoName;
    }

    private void showSelection(Position p) {
        getSquare(p).getStyleClass().add("selected");
    }

    private void hideSelection(Position p) {
        getSquare(p).getStyleClass().remove("selected");
    }

    private StackPane getSquare(Position p) {
        for (var child : board.getChildren()) {
            var row = GridPane.getRowIndex(child);
            var col = GridPane.getColumnIndex(child);
            if (row == p.row() && col == p.column()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }

    public void handleRestart(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Game.fxml"));
        Parent root = fxmlLoader.load();
        GameController controller = fxmlLoader.getController();
        controller.setPlayerNames(playerTwoName, playerOneName);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleReturn(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GameStart.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleExit(ActionEvent event) {
        Logger.info("Exiting...");
        Platform.exit();
    }
}
