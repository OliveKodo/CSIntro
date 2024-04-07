import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a very basic Testing class for Map - please note that this JUnit
 * contains only a very limited testing method and should be added many other
 * methods for testing all the functionality of Map2D - both in correctness and in runtime.
 */
class MapTest {
    /**
     * _m_3_3 =
     * 0,1,0
     * 1,0,1
     * 0,1,0
     * <p>
     * _m0 =
     * 1,1,1,1,1
     * 1,0,1,0,1
     * 1,0,0,0,1
     * 1,0,1,0,1
     * 1,1,1,1,1
     * 1,0,1,0,1
     * <p>
     * 1, 1, 1, 1, 1
     * 1,-1, 1,-1, 1
     * 1,-1,-1,-1, 1
     * 1,-1, 1,-1, 1
     * 1, 1, 1, 1, 1
     * 1,-1, 1,-1, 1
     * <p>
     * m2[3][2] = 0, m2[1][2] = 10, |sp|=11 (isCiclic = false;}
     * =============
     * 7, 8, 9, 1, 7
     * 6,-1,10,-1, 6
     * 5,-1,-1,-1, 5
     * 4,-1, 0,-1, 4
     * 3, 2, 1, 2, 3
     * 4,-1, 2,-1, 4
     * <p>
     * m[3][2] = 0, m2[1][2] = 5, |sp|=5 (isCiclic = true;}
     * 5, 4, 3, 4, 5
     * 6,-1, 4,-1, 6
     * 5,-1,-1,-1, 5
     * 4,-1, 0,-1, 4
     * 3, 2, 1, 2, 3
     * 4,-1, 2,-1, 4
     */
    private int[][] _map = {{1, 1, 1, 1, 1}, {1, 0, 1, 0, 1},
            {1, 0, 0, 0, 1}, {1, 0, 1, 0, 1}, {1, 1, 1, 1, 1}, {1, 0, 1, 0, 1}};
    private int[][] _map_3_3 = {{0, 1, 0}, {1, 0, 1}, {0, 1, 0}};
    private Map2D _m0, _m1, _m2, _m3, _m3_3;

    public static final int[][] map1 = {{-2, -2, -1, -2},
            {-2, -2, -2, -2},
            {-1, -2, -1, -2}};

    public static final int[][] pixels = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}};

    static final int[][] map2 = {{-1, -1, -1, -1, -1, -1}, {-1, -2, -2, -2, -2, -1}, {-1, -2, -2, -1, -2, -1}, {-1, -2, -1, -2, -2, -1}, {-1, -2, -1, -1, -1, -1},
            {-1, -2, -2, -2, -2, -2}, {-1, -1, -1, -1, -1, -1}};
    static final int[][] map4 = {
            {0, 1, 0, 0, 0},
            {0, 1, 0, 1, 0},
            {0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0},
            {0, 0, 0, 0, 0}};


    @BeforeEach
    public void setuo() {
        _m0 = new Map(_map);
        _m1 = new Map(_map);
        _m1.setCyclic(true);
        _m2 = new Map(_map);
        _m2.setCyclic(false);
        _m3 = new Map(_map);
        _m3_3 = new Map(_map_3_3);
    }

    @Test
    @Timeout(value = 1, unit = SECONDS)
    void init() {
        int[][] bigarr = new int[500][500];
        _m1.init(bigarr);
        assertEquals(bigarr.length, _m1.getWidth());
        assertEquals(bigarr[0].length, _m1.getHeight());
        Pixel2D p1 = new Index2D(3, 2);
        _m1.fill(p1, 1);
    }

    @Test
    void testEquals() {
        assertEquals(_m0, _m1);
        assertEquals(_m0, _m3);
        assertNotEquals(_m1, _m2);
        _m3.setPixel(2, 2, 17);
        assertNotEquals(_m0, _m3);
    }

    @Test
    void getMap() {
        int[][] m0 = _m0.getMap();
        _m1.init(m0);
        assertEquals(_m0, _m1);
    }


    @Test
    void testFill() {
        Pixel2D p1 = new Index2D(0, 0);
        int f0 = _m0.fill(p1, 2);
        assertEquals(f0, 21);
    }

    @Test
    void testFill1() {
        Pixel2D p1 = new Index2D(0, 1);
        _m0.setPixel(p1, 0);
        int f0 = _m0.fill(p1, 2);
        assertEquals(f0, 8);
        _m0.setCyclic(false);
        int f2 = _m0.fill(p1, 3);
        assertEquals(f2, 8);
    }

    @Test
    void testAllDistance() {
        Pixel2D p1 = new Index2D(3, 2);
        Pixel2D p2 = new Index2D(1, 0);
        Map2D m00 = _m0.allDistance(p1, 0);
        assertEquals(6, m00.getPixel(p2));
    }

    @Test
    void testShortestPath() {
        Pixel2D p1 = new Index2D(3, 2);
        Pixel2D p2 = new Index2D(1, 2);
        Pixel2D[] path = _m0.shortestPath(p1, p2, 0);
        assertEquals(5, path.length);
        path = _m2.shortestPath(p1, p2, 0);
        assertEquals(11, path.length);
    }

    @Test
    void allDistance2() {
        int[][] data = {
                {0, -1, -2},
                {-2, -2, -1},
                {-2, -1, -2}};

        Map map = new Map(data);
        Pixel2D start = new Index2D(0, 0);
        int obsColor = -1;

        Map2D result = map.allDistance(start, obsColor);
        int[][] resultArray = result.getMap();

        int[][] expected = {
                {0, 0, 1},
                {1, 2, 0},
                {1, 0, 2}
        };

        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[0].length; j++) {
                assertEquals(expected[i][j], resultArray[i][j]);
            }
        }
    }

    @Test
    void allDistance3() {
        Map map = new Map(map4);
        Pixel2D start = new Index2D(2, 2);
        int obsColor = -1;
        Map2D result = map.allDistance(start, obsColor);
        Assertions.assertEquals(map.getWidth(), map.getHeight());
        Assertions.assertEquals(map.getHeight(), map.getWidth());

        Assertions.assertEquals(0, result.getPixel(start));

        Assertions.assertEquals(1, result.getPixel(new Index2D(2, 1)));
        Assertions.assertEquals(1, result.getPixel(new Index2D(3, 2)));
        Assertions.assertEquals(1, result.getPixel(new Index2D(2, 3)));

        Assertions.assertEquals(2, result.getPixel(new Index2D(1, 1)));
        Assertions.assertEquals(2, result.getPixel(new Index2D(1, 3)));
        Assertions.assertEquals(2, result.getPixel(new Index2D(3, 1)));
        Assertions.assertEquals(2, result.getPixel(new Index2D(3, 3)));


    }

    @Test
    public void testShortestPath2() {
        Map2D map3 = new Map(map1);
        map3.setCyclic(false);
        Pixel2D zero = new Index2D(2, 1);
        Pixel2D step1 = new Index2D(1, 1);
        Pixel2D step2 = new Index2D(1, 2);
        Pixel2D step3 = new Index2D(1, 3);
        Pixel2D aim = new Index2D(2, 3);
        Pixel2D[] exp = {zero, step1, step2, step3, aim};
        assertArrayEquals(map3.shortestPath(zero, aim, -1), exp);

    }

    @Test

    public void testShortestPath3() {
        Map2D map3 = new Map(map1);
        map3.setCyclic(true);
        Pixel2D zero = new Index2D(2, 1);
        Pixel2D step1 = new Index2D(0, 1);
        Pixel2D step2 = new Index2D(0, 0);
        Pixel2D aim = new Index2D(0, 3);
        Pixel2D[] exp = {zero, step1, step2, aim};
        assertArrayEquals(map3.shortestPath(zero, aim, -1), exp);


    }

    @Test
    void testFill0() {
        Pixel2D p1 = new Index2D(0, 0);
        int f0 = _m0.fill(p1, 2);

        // צפייה מדויקת בכמות הפיקסלים שמומלאים (12 פיקסלים בסביבת נקודת ההתחלה ו-9 פיקסלים נוספים)
        assertEquals(f0, 21);
    }

    @Test
    void testFill12() {
        Pixel2D p1 = new Index2D(0, 1);
        _m0.setPixel(p1, 0);
        // צפייה מדויקת בכמות הפיקסלים שמומלאים לאחר עדכון הערך של הפיקסל
        int f0 = _m0.fill(p1, 2);
        assertEquals(f0, 8);

        // כיבוי מצב המחזוריות
        _m0.setCyclic(false);
        // בדיקה של כמות הפיקסלים שמומלאים לאחר שינוי במצב המחזוריות
        int f2 = _m0.fill(p1, 3);
        assertEquals(f2, 8);
    }


    @Test
    public void testShortest1(){
        int[][] arr = {{-2, -1, -1, -1, -2},
                {-2, -1, -1, -2, -2},
                {-2, -2, -2, -2, -1},
                {-1, -2, -2, -2, -1} };
        Map2D map = new Map(arr);
        map.setCyclic(false);
        Index2D index1 = new Index2D(0,0);
        Index2D index2 = new Index2D(2,2);
        Index2D index3 = new Index2D(0,4);
        Pixel2D [] points = {index1,index2,index3,null};
        int obscolor = -1;
        int [] exp = {0,4,1,4,1,3,2,3,2,2,2,1,2,3,2,0,1,0,0,0};
        Pixel2D[] ans = map.shortestPath(points, obscolor);
        int[] comp = new int[exp.length];
        for (int i = 0; i < exp.length; i += 2) {
            comp[i] = ans[i / 2].getX();
            comp[i + 1] = ans[i / 2].getY();
        }
        assertArrayEquals(exp, comp);

    }



    @Test
    void testTime() {
        Map map = new Map(new int[500][500]);
        Pixel2D p1 = new Index2D(0, 0);
        Pixel2D p2 = new Index2D(50, 50);
        long startTime = System.nanoTime();

        map.getMap();
        long endTime = System.nanoTime();
        long timer = (endTime - startTime) / 1000000;
        System.out.println("Time taken " + timer);
    }

}





