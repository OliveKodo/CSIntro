
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Index2DTest {

    @Test
    void getX() {
    }

    @Test
    void getY() {
    }



    @Test
    void distance2D() {
        Index2D p1 = new Index2D(0, 0);
        Index2D p2 = new Index2D(3, 4);

        double expected = 5.0;
        double actual = p1.distance2D(p2);
        assertEquals(expected,actual, 0.0001);

    }



    @Test
    void testToString() {
    }

    @Test
    void testEquals() {
        Index2D p1 = new Index2D(0, 0);
        Index2D p2 = new Index2D(3, 4);

        double expected = 5.0;
        double actual = p1.distance2D(p2);
        assertEquals(expected,actual, 0.0001);

    }
    }
