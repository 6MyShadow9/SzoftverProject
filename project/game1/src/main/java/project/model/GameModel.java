package project.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.tinylog.Logger;



public class GameModel {

    
    public enum Player {
        PLAYER1(Field.RED), PLAYER2(Field.BLUE);

        Player(Field color) {
            this.color = color;
        }

        private Field color;


        public Player alter() {
            return switch (this) {
                case PLAYER1 -> PLAYER2;
                case PLAYER2 -> PLAYER1;
            };
        }

        public Field getColor(){
            return color;
        }
    }


    public static final int BOARD_NUMBER_OF_ROWS = 5;

    public static final int BOARD_NUMBER_OF_COLS = 4;

    private Player player = Player.PLAYER1;

    private ReadOnlyObjectWrapper<Field>[][] board = new ReadOnlyObjectWrapper[BOARD_NUMBER_OF_ROWS][BOARD_NUMBER_OF_COLS];

    /**
     * Creates a {@code DiskGameModel} object.
     */
    public GameModel() {
        for (var i = 0; i < BOARD_NUMBER_OF_ROWS; i++) {
            for (var j = 0; j < BOARD_NUMBER_OF_COLS; j++) {
                if (i == 0 && j % 2 == 0
                        || i == BOARD_NUMBER_OF_ROWS - 1 && j % 2 == 1) {
                    board[i][j] = new ReadOnlyObjectWrapper<Field>(Field.BLUE);
                } else if (i == 0 && j % 2 == 1
                        || i == BOARD_NUMBER_OF_ROWS - 1 && j % 2 == 0) {
                    board[i][j] = new ReadOnlyObjectWrapper<Field>(Field.RED);
                } else {
                    board[i][j] = new ReadOnlyObjectWrapper<Field>(Field.NONE);
                }
            }
        }
        Logger.info("Model created, with no argument constructor.");
    }

    /**
     * Creates a {@code DiskGameModel} object.
     *
     * @param player        the player to move first
     * @param bluePositions array of 4 positions of the blue disks
     * @param redPositions  array of 4 positions of the red disks
     */
    public GameModel(Player player, Position[] redPositions, Position[] bluePositions) {
        if (bluePositions.length != 4 || redPositions.length != 4) {
            throw new IllegalArgumentException();
        }
        this.player = player;

        for (var i = 0; i < BOARD_NUMBER_OF_ROWS; i++) {
            for (var j = 0; j < BOARD_NUMBER_OF_COLS; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<Field>(Field.NONE);
            }
        }

        for (var pos :
                bluePositions) {
            setField(pos, Field.BLUE);
        }
        for (var pos :
                redPositions) {
            setField(pos, Field.RED);
        }


    }

    public ReadOnlyObjectProperty<Field> fieldProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    /**
     * {@return the color of the Field in the given {@code Position}}
     *
     * @param p the position where the Field is.
     */
    public Field getField(Position p) {
        return board[p.row()][p.column()].get();
    }

    private void setField(Position p, Field field) {
        board[p.row()][p.column()].set(field);
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Make a move from a given position to another given position.
     *
     * @param from the starting {@code Position}.
     * @param to   the goal {@code Position}.
     */
    public void move(Position from, Position to) {
        setField(to, getField(from));
        setField(from, Field.NONE);
        Logger.info("Moved from " + from + " to " + to);
        if (!Victory()) {
            player = player.alter();
        }
    }

    /**
     * {@return if the {@code Position} is on the board}
     *
     * @param p the position in question.
     */
    public static boolean isOnBoard(Position p) {
        return 0 <= p.row() && p.row() < BOARD_NUMBER_OF_ROWS && 0 <= p.column()
                && p.column() < BOARD_NUMBER_OF_COLS;
    }

    /**
     * {@return if the given {@code Position} is empty}
     *
     * @param p the position in question.
     */
    public boolean isEmpty(Position p) {
        return board[p.row()][p.column()].get() == Field.NONE;
    }

    /**
     * {@return the state of the game} True if the game is in a state, where one player has won, false otherwise.
     */
    public boolean Victory() {
        if (isThreeInRow()
                || isThreeInCol()
                || isThreeDiag()) {
            Logger.info(player + " Has won!");
            return true;
        }
        return false;
    }

    private boolean isThreeInCol() {
        int diskCounter = 0;
        for (int i = 0; i < BOARD_NUMBER_OF_COLS; i++) {
            diskCounter = 0;
            for (int j = 0; j < BOARD_NUMBER_OF_ROWS; j++) {
                if (board[j][i].get() == player.color) {
                    diskCounter++;
                    if (diskCounter == 3) {
                        return true;
                    }
                } else {
                    diskCounter = 0;
                }
            }
        }
        return false;
    }

    private boolean isThreeInRow() {
        int diskCounter = 0;
        for (int i = 0; i < BOARD_NUMBER_OF_ROWS; i++) {
            diskCounter = 0;
            for (int j = 0; j < BOARD_NUMBER_OF_COLS; j++) {
                if (board[i][j].get() == player.color) {
                    diskCounter++;
                    if (diskCounter == 3) {
                        return true;
                    }
                } else {
                    diskCounter = 0;
                }
            }
        }
        return false;
    }

    private boolean isThreeDiag() {
        for (int i = 0; i < BOARD_NUMBER_OF_ROWS - 2; i++) {
            for (int j = 0; j < BOARD_NUMBER_OF_COLS - 2; j++) {
                if (checkLeftThreeDiagonal(i, j)) {
                    return true;
                }
            }
        }

        for (int i = 0; i < BOARD_NUMBER_OF_ROWS - 2; i++) {
            for (int j = 2; j < BOARD_NUMBER_OF_COLS; j++) {
                if (checkRightTreeDiagonal(i, j)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkRightTreeDiagonal(int i, int j) {
        int diskCounter = 0;
        for (int k = 0; k < 3; k++) {
            if (board[i + k][j - k].get() == player.color) {
                diskCounter++;
            }
        }
        if (diskCounter == 3) {
            return true;
        }

        return false;
    }

    private boolean checkLeftThreeDiagonal(int i, int j) {
        int diskCounter = 0;
        for (int k = 0; k < 3; k++) {
            if (board[i + k][j + k].get() == player.color) {
                diskCounter++;
            }
        }
        if (diskCounter == 3) {
            return true;
        }

        return false;
    }

    public static boolean isValid(Position from, Position to) {
        var dx = Math.abs(from.row() - to.row());
        var dy = Math.abs(from.column() - to.column());

        return (dx + dy == 1) || (dx * dy == 1);
    }

    public boolean canMove(Position from, Position to) {
        return isOnBoard(from) && isOnBoard(to) && !isEmpty(from)
                && isEmpty(to) && isValid(from, to) && player.color == getField(from);
    }



    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (var i = 0; i < BOARD_NUMBER_OF_ROWS; i++) {
            for (var j = 0; j < BOARD_NUMBER_OF_COLS; j++) {
                sb.append(board[i][j].get().ordinal()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        if(o instanceof GameModel other){
            if(player != other.player){
                return false;
            }
            for(int i = 0; i < BOARD_NUMBER_OF_ROWS; i++){
                for(int j = 0; j < BOARD_NUMBER_OF_COLS; j++){
                    if(board[i][j].get() != other.board[i][j].get()){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
