import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Index2DTest {

    @Test
    void distance2D() {
            Index2D p1 = new Index2D(0, 0);
            Index2D p2 = new Index2D(3, 4);

            double expected = 5.0;
            double actual = p1.distance2D(p2);
            assertEquals(expected,actual, 0.0001);

        }



    @Test
    public void testToString() {
        Index2D index = new Index2D(4, 9);
        assertEquals("4,9", index.toString());
    }

    @Test
    public void testEquals() {
        Index2D index1 = new Index2D(3, 6);
        Index2D index2 = new Index2D(3, 6);
        assertTrue(index1.equals(index2));
    }

    @Test
    public void testNotEquals() {
        Index2D index1 = new Index2D(2, 5);
        Index2D index2 = new Index2D(3, 6);
        assertFalse(index1.equals(index2));
    }
}