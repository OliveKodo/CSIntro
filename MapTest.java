
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class MapTest {
    public static final int[][] map1 = {{-2,-2,-1,-2},
                                        {-2,-2,-2,-2},
                                        {-1,-2,-1,-2}};

   public static final int[][] pixels = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}};

    static final int[][] map2= {{-1,-1,-1,-1,-1,-1},{-1,-2,-2,-2,-2,-1},{-1,-2,-2,-1,-2,-1},{-1,-2,-1,-2,-2,-1},{-1,-2,-1,-1,-1,-1},
            {-1,-2,-2,-2,-2,-2},{-1,-1,-1,-1,-1,-1}};
    static final int[][] map4 = {
            {0,1,0,0,0},
            {0,1,0,1,0},
            {0,0,0,0,0},
            {0,1,0,1,0},
            {0,0,0,0,0}};
    @Test
    void init() {
        // Create a sample map
        Map sampleMap = new Map(3, 3, 1);
        // Initialize the map
        sampleMap.init(3, 3, 1);

        // Get the map using getMap
        int[][] result = sampleMap.getMap();

        // Assert that all elements in the map are equal to the specified value
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(1, result[i][j]);
            }
        }


    }

    @Test
    void testInit() {
        int[][] input = {
                {0, 1, 0},
                {1, 0, 1},
                {0, 1, 0}
        };

        // Create a sample map
        Map sampleMap = new Map(0, 1, 0);
        sampleMap.init(input);

        // Get the map using getMap
        int[][] result = sampleMap.getMap();

        // Assert that the result matches the input array
        assertArrayEquals(input, result);

    }

    @Test
    void getMap() {
        int[][] map = {
                {0, 0, 0},
                {1, 1, 1},
                {2, 2, 2}
        };
        Map sampleMap = new Map(map);

        // Get the map using getMap
        int[][] result = sampleMap.getMap();

        // Assert that the result matches the original map
        Assert.assertArrayEquals(map, result);
    }
    @Test
    void testGetMap() {
        Map2D map = new Map(3, 4, 7);
        int[][] test = map.getMap();
        Assertions.assertEquals(test.length, map.getWidth());
        Assertions.assertEquals(test[0].length, map.getHeight());
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < test[0].length; j++) {
                Assertions.assertEquals(test[i][j], map.getPixel(i, j));

            }

        }
    }


        @Test
        void fill () {
            Map2D map = new Map(pixels);

            int filledPixels = map.fill(new Index2D(1, 1), 2);

             Assertions.assertEquals(3, filledPixels);
            Assertions.assertEquals(1, map.getPixel(1, 1));
            Assertions.assertEquals(2, map.getPixel(0, 0));
            Assertions.assertEquals(1, map.getPixel(0, 1));
            Assertions.assertEquals(1, map.getPixel(0, 2));
            Assertions.assertEquals(2, map.getPixel(1, 0));
            Assertions.assertEquals(1, map.getPixel(1, 2));
            Assertions.assertEquals(2, map.getPixel(2, 0));
            Assertions.assertEquals(1, map.getPixel(2, 1));
            Assertions.assertEquals(1, map.getPixel(2, 2));


        }


        @Test
        public void testShortestPath () {
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

        public void testShortestPath2 () {
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
        void isInside () {
            // Create a map with dimensions 3x3
            Map map = new Map(3, 3, 0);

            // Test for pixels within the map boundaries
            assertTrue(map.isInside(new Index2D(0, 0)));
            assertTrue(map.isInside(new Index2D(1, 1)));
            assertTrue(map.isInside(new Index2D(2, 2)));

            // Test for a pixel outside the map boundaries
            assertFalse(map.isInside(new Index2D(3, 3)));
        }



    @Test
    void allDistance() {
        Map map = new Map(map4);
        Pixel2D start = new Index2D(2,2);
        int obsColor = -1;
        Map2D result = map.allDistance(start,obsColor);
        Assertions.assertEquals(map.getWidth(), map.getHeight());
        Assertions.assertEquals(map.getHeight(), map.getWidth());

        Assertions.assertEquals(0,result.getPixel(start));

        Assertions.assertEquals(1,result.getPixel(new Index2D(2,1)));
        Assertions.assertEquals(1,result.getPixel(new Index2D(3,2)));
        Assertions.assertEquals(1,result.getPixel(new Index2D(2,3)));

        Assertions.assertEquals(2,result.getPixel(new Index2D(1,1)));
        Assertions.assertEquals(2,result.getPixel(new Index2D(1,3)));
        Assertions.assertEquals(2,result.getPixel(new Index2D(3,1)));
        Assertions.assertEquals(2,result.getPixel(new Index2D(3,3)));



    }
    @Test
    void TestisInside2() {
        Map2D map = new Map(48, 76, 2);
        Pixel2D first = new Index2D(46, 24);
        Pixel2D second = new Index2D(48, 76);
        Assertions.assertTrue(map.isInside(first));
        Assertions.assertFalse(map.isInside(second));
    }

    @Test
    void testisCyclic() {
        Map2D map = new Map(4, 7, 13);
        Assertions.assertTrue(map.isCyclic());
        map.setCyclic(false);
        Assertions.assertFalse(map.isCyclic());
    }
    @Test
    void allDistance4() {
        int[][] data = {
                {0, -1, -2},
                {-2, -2, -1},
                {-2, -1, -2} };

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



}
