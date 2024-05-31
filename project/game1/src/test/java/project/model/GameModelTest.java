package project.model;

import org.junit.jupiter.api.Test;
import project.model.Field;
import project.model.GameModel;
import project.model.Position;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GameModelTest {
    GameModel gameModel1 = new GameModel(); //starting state

    GameModel gameModel2 = new GameModel(GameModel.Player.PLAYER1,
            new Position[]{new Position(0, 0), new Position(0, 1),
                    new Position(0, 2), new Position(2, 3)},
            new Position[]{new Position(1, 2), new Position(1, 3),
                    new Position(4, 0), new Position(4, 1)}); //goal state for red in row

    GameModel gameModel3 = new GameModel(GameModel.Player.PLAYER1,
            new Position[]{new Position(0, 0), new Position(1, 0),
                    new Position(2, 3), new Position(2, 0)},
            new Position[]{new Position(1, 2), new Position(1, 3),
                    new Position(4, 0), new Position(4, 1)}); //goal state for red in column

    GameModel gameModel4 = new GameModel(GameModel.Player.PLAYER1,
            new Position[]{new Position(2, 1), new Position(3, 2),
                    new Position(1, 0), new Position(4, 3)},
            new Position[]{new Position(1, 2), new Position(1, 3),
                    new Position(4, 0), new Position(4, 1)}); //goal state for red in diagonal

    GameModel gameModel5 = new GameModel(GameModel.Player.PLAYER2,
            new Position[]{new Position(2, 1), new Position(2, 2),
                    new Position(1, 0), new Position(4, 3)},
            new Position[]{new Position(3, 2), new Position(3, 3),
                    new Position(4, 0), new Position(3, 1)}); //goal state for blue in row

    GameModel gameModel6 = new GameModel(GameModel.Player.PLAYER2,
            new Position[]{new Position(2, 0), new Position(2, 2),
                    new Position(1, 0), new Position(4, 3)},
            new Position[]{new Position(0, 3), new Position(1, 2),
                    new Position(2, 3), new Position(2, 1)}); //goal state for blue in diagonal

    GameModel gameModel7 = new GameModel(GameModel.Player.PLAYER2,
            new Position[]{new Position(2, 0), new Position(2, 2),
                    new Position(1, 0), new Position(4, 3)},
            new Position[]{new Position(0, 3), new Position(1, 2),
                    new Position(2, 3), new Position(2, 0)});//random state

    Position position1 = new Position(0, 0);
    Position position2 = new Position(1, 1);
    Position position3 = new Position(3, 2);
    Position position4 = new Position(2, 3);
    Position position5 = new Position(4, 0);
    Position position6 = new Position(0, 3);
    Position position7 = new Position(4, 3);
    Position position8 = new Position(-1, 1);
    Position position9 = new Position(5, 4);
    Position position10 = new Position(10, 10);
    Position position11 = new Position(3, 0);
    Position position12 = new Position(0, 1);

    @Test
    void testGameModelConstructor_shouldThrowIllegalArgumentException(){
        assertThrows(IllegalArgumentException.class, () -> new GameModel(GameModel.Player.PLAYER1,
                new Position[]{new Position(2, 0), new Position(2, 2),
                        new Position(1, 0), new Position(4, 3)},
                new Position[]{new Position(0, 3), new Position(1, 2),
                        new Position(2, 3)}));

        assertThrows(IllegalArgumentException.class, () -> new GameModel(GameModel.Player.PLAYER1,
                new Position[]{new Position(2, 2),
                        new Position(1, 0), new Position(4, 3)},
                new Position[]{new Position(0, 3), new Position(1, 2),
                        new Position(2, 3), new Position(1,1)}));
    }

    @Test
    void testMove() {
        gameModel1.move(position12, position2);
        assertEquals(
                new GameModel(GameModel.Player.PLAYER2,
                        new Position[]{
                                new Position(0, 3), new Position(4, 0),
                                new Position(4, 2), new Position(1, 1)
                        },
                        new Position[]{
                                new Position(0, 0), new Position(0, 2),
                                new Position(4, 1), new Position(4, 3)
                        }
                ), gameModel1);
    }

    @Test
    void testIsOnBoard() {
        assertTrue(gameModel1.isOnBoard(position1));
        assertTrue(gameModel1.isOnBoard(position2));
        assertTrue(gameModel1.isOnBoard(position3));
        assertTrue(gameModel1.isOnBoard(position4));
        assertTrue(gameModel1.isOnBoard(position5));
        assertTrue(gameModel1.isOnBoard(position6));
        assertTrue(gameModel1.isOnBoard(position7));

        assertFalse(gameModel1.isOnBoard(position8));
        assertFalse(gameModel1.isOnBoard(position9));
        assertFalse(gameModel1.isOnBoard(position10));
    }

    @Test
    void testIsEmpty() {
        assertTrue(gameModel1.isEmpty(position2));
        assertTrue(gameModel1.isEmpty(position3));
        assertTrue(gameModel1.isEmpty(position4));

        assertFalse(gameModel1.isEmpty(position1));
        assertFalse(gameModel1.isEmpty(position6));
        assertFalse(gameModel1.isEmpty(position5));
        assertFalse(gameModel1.isEmpty(position7));
    }

    @Test
    void testVictory() {
        assertFalse(gameModel1.Victory());
        assertFalse(gameModel7.Victory());

        assertTrue(gameModel2.Victory());
        assertTrue(gameModel3.Victory());
    }

    @Test
    void testCanMove_False() {
        assertFalse(gameModel1.canMove(position3, position4)); //Empty the from position
        assertFalse(gameModel1.canMove(position12, position1)); //Not empty the to position
        assertFalse(gameModel1.canMove(position12, position3)); //Positions too far
        assertFalse(gameModel1.canMove(position12, position12)); //The same position
        assertFalse(gameModel1.canMove(position12, position8)); //Invalid to position
        assertFalse(gameModel1.canMove(position9, position7)); //Invalid from position
        assertFalse(gameModel1.canMove(position1, position2)); //Not current player
    }

    @Test
    void testCanMove_True() {
        assertTrue(gameModel1.canMove(position12, position2));
    }

    @Test
    void testisValid() {
        assertTrue(gameModel1.isValid(position1, position2));
        assertTrue(gameModel1.isValid(position2, position1));
        assertTrue(gameModel1.isValid(position3, position4));
        assertTrue(gameModel1.isValid(position4, position3));
        assertTrue(gameModel1.isValid(position7, position9));
        assertTrue(gameModel1.isValid(position11, position5));

        assertFalse(gameModel1.isValid(position1, position10));
        assertFalse(gameModel1.isValid(position2, position3));
    }

    @Test
    void testGetColor() {
        assertEquals(Field.RED, gameModel1.getPlayer().getColor());
        assertEquals(Field.BLUE, gameModel6.getPlayer().getColor());
    }

    @Test
    void testToString() {
        assertEquals("2 1 2 1 " + '\n'
                        + "0 0 0 0 " + '\n'
                        + "0 0 0 0 " + '\n'
                        + "0 0 0 0 " + '\n'
                        + "1 2 1 2 " + '\n',
                gameModel1.toString());

        assertEquals("0 0 0 2 " + '\n'
                        + "1 0 2 0 " + '\n'
                        + "1 2 1 2 " + '\n'
                        + "0 0 0 0 " + '\n'
                        + "0 0 0 1 " + '\n',
                gameModel6.toString());
    }

    @Test
    void testEquals() {
        assertTrue(gameModel1.equals(gameModel1));
        assertTrue(gameModel1.equals(new GameModel(GameModel.Player.PLAYER1,
                new Position[]{
                        new Position(0, 3), new Position(4, 0),
                        new Position(4, 2), new Position(0, 1)
                },
                new Position[]{
                        new Position(0, 0), new Position(0, 2),
                        new Position(4, 1), new Position(4, 3)
                })));
        assertTrue(gameModel6.equals(new GameModel(GameModel.Player.PLAYER2,
                new Position[]{new Position(2, 0), new Position(2, 2),
                        new Position(1, 0), new Position(4, 3)},
                new Position[]{new Position(0, 3), new Position(1, 2),
                        new Position(2, 3), new Position(2, 1)})));

        assertFalse(gameModel1.equals(null));
        assertFalse(gameModel1.equals(gameModel5)); //Different player sate
        assertFalse(gameModel1.equals(gameModel2)); //Different positions
        assertFalse(gameModel1.equals(3));
    }
}
