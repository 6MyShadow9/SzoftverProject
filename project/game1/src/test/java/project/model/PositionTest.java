package project.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {
    Position position1 = new Position(0,1);
    Position position2 = new Position(2,4);
    Position position3 = new Position(3,2);

    @Test
    void testToString(){

        assertEquals("(0,1)", position1.toString());
        assertEquals("(2,4)", position2.toString());
        assertEquals("(3,2)", position3.toString());
    }

    @Test
    void testEquals(){
        assertTrue(position1.equals(position1));
        assertTrue(position2.equals(new Position(2,4)));

        assertFalse(position1.equals(position2));
        assertFalse(position2.equals(position3));
        assertFalse(position3.equals(null));
        assertFalse(position3.equals(12));
    }
}
