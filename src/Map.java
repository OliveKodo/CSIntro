import javax.swing.*;
import java.io.Serializable;
import java.util.*;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 *
 * @author boaz.benmoshe
 */
public class Map extends JFrame implements Map2D, Serializable {
    private int[][] _map;
    private boolean _cyclicFlag = false;

    public Map() {
        this.setLayout(null);
    }

    /**
     * Constructs a w*h 2D raster map with an init value v.
     *
     * @param w
     * @param h
     * @param v
     */
    public Map(int w, int h, int v) {
        init(w, h, v);
    }

    /**
     * Constructs a square map (size*size).
     *
     * @param size
     */
    public Map(int size) {
        this(size, size, 0);
    }

    /**
     * Constructs a map from a given 2D array.
     *
     * @param data
     */
    public Map(int[][] data) {
        init(data);
    }


    @Override
    public void init(int w, int h, int v) {
        // בדיקת תקינות הקלט
        if (w <= 0 || h <= 0) {
            throw new RuntimeException("RuntimeException, Invalid input, arr cant be null");
        }
        // השמה של הערכים למערך באמצעות fill
        this._map = new int[w][h];
        for (int i = 0; i < w; i++) {
            Arrays.fill(this._map[i], v);
        }
    }


    @Override
    public void init(int[][] arr) {
        // בדיקת תקינות הקלט
        if (arr == null) {
            throw new RuntimeException("RuntimeException, Invalid input, arr cant be null");
        }

        // השמת הערכים מהמערך הקיים למערך הפנימי
        int rows = arr.length;
        int cols = arr[0].length;
        this._map = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            if (arr[i].length != cols) {
                throw new RuntimeException("RuntimeException, Invalid input, arr cant be null");
            }
            System.arraycopy(arr[i], 0, this._map[i], 0, cols);
        }
    }


    @Override
    public int[][] getMap() {
        int[][] copyMap = new int[this._map.length][this._map[0].length];
        for (int i = 0; i < this._map.length; i++) {
            for (int j = 0; j < this._map[0].length; j++) {
                copyMap[i][j] = this._map[i][j];
            }
        }
        return copyMap;
    }


    @Override
    public int getWidth() {
        return _map.length;
    }

    @Override
    public int getHeight() {
        return _map[0].length;
    }

    @Override
    public int getPixel(int x, int y) {
        return _map[x][y];
    }

    @Override
    public int getPixel(Pixel2D p) {
        return this.getPixel(p.getX(), p.getY());
    }

    @Override
    public void setPixel(int x, int y, int v) {
        _map[x][y] = v;
    }

    @Override
    public void setPixel(Pixel2D p, int v) {
        setPixel(p.getX(), p.getY(), v);
    }


    @Override
    /**
     * Fills this map with the new color (new_v) starting from p.
     * https://en.wikipedia.org/wiki/Flood_fill
     */
    public int fill(Pixel2D xy, int new_v) {
        int targetColor = getPixel(xy);
        int width = getWidth();
        int height = getHeight();

        // Create a stack to store pixels to be processed
        Deque<Pixel2D> tor = new ArrayDeque<>();
        tor.push(xy);

        // Flood fill algorithm
        int pixelsChanged = 0;
        while (!tor.isEmpty()) {
            Pixel2D currentPixel = tor.pop();
            int x = currentPixel.getX();
            int y = currentPixel.getY();

            // Check if the current pixel is within bounds and has the target color
            if (x >= 0 && x < width && y >= 0 && y < height && getPixel(x, y) == targetColor) {
                // Change the color of the current pixel
                setPixel(x, y, new_v);
                pixelsChanged++;

                // Add neighboring pixels to the stack
                tor.push(new Index2D(x + 1, y, targetColor)); // Right
                tor.push(new Index2D(x - 1, y, targetColor)); // Left
                tor.push(new Index2D(x, y + 1, targetColor)); // Down
                tor.push(new Index2D(x, y - 1, targetColor)); // Up
            }
        }

        return pixelsChanged;
    }


    /**
     * Computes the distance of the shortest path (minimal number of consecutive neighbors) from p1 to p2.
     * Notes: the distance is using computing the shortest path and returns its length-1, as the distance fro  a point
     * to itself is 0, while the path contains a single point.
     */
    public int shortestPathDist(Pixel2D p1, Pixel2D p2, int obsColor) {
        // בדיקה אם p1 או p2 הם null
        if (p1 == null || p2 == null) {
            return -1;
        }

        // קריאה לפונקציה shortestPath עם הארגומנטים שנמסרו
        Pixel2D[] path = shortestPath(p1, p2, obsColor);

        // בדיקה אם לא נמצא מסלול בין הנקודות
        if (path == null) {
            return -1;
        }

        // החזרת אורך המסלול פחות 1 (כיוון שאורך המסלול מוצג כמספר הפיקסלים בו ולא כמספר הקפיצות בו)
        return path.length - 1;
    }


    /**
     * BFS like shortest the computation based on iterative raster implementation of BFS, see:
     * https://en.wikipedia.org/wiki/Breadth-first_search
     */
    @Override
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
        // Get the dimensions of the map
        boolean dontNeedSteps = false;
        int width = _map.length;
        int height = _map[0].length;
        int pathLength = 1;
        Pixel2D[] ans;  // The result array containing the shortest path.
        PixelProperties[][] visited = new PixelProperties[width][height];
        initPixelPropMap(visited, width, height);

        Queue<Index2D> queue = new LinkedList<>();

        queue.add((Index2D) p1); // Enqueue the first pixel
        visited[p1.getX()][p1.getY()].setState(PixelProperties.PixelState.Explored);

        // Perform breadth-first search to find the shortest path
        while (!queue.isEmpty()) {
            Index2D currentPixel = queue.poll();


            // Check if the current pixel is the destination
            if (currentPixel.getX() == p2.getX() && currentPixel.getY() == p2.getY()) {

                // Backtrack to reconstruct the path
                ArrayList<Index2D> backwardArray = new ArrayList<>();
                backwardArray.add(currentPixel);
                while (currentPixel.getPrev() != null) {
                    backwardArray.add(currentPixel.getPrev());
                    currentPixel = currentPixel.getPrev();
                    pathLength++;
                }

                ans = new Pixel2D[pathLength];

                // Reverse the path and store it in the result array
                for (int i = 0; i < pathLength; i++) {
                    ans[pathLength - 1 - i] = backwardArray.get(i);
                }

                return ans; // Return the shortest path
            }
            Index2D[] allN = getNeighbors(0, currentPixel);
            for (int i = 0; i < allN.length; i++) {
                if(!(p1.getX() == allN[i].getX() &&
                        p1.getY() == allN[i].getY()))
                    queue.add(allN[i]);
            }

        }

        // If no path is found, return null
        return null;
    }


    public Pixel2D[] shortestPath(Pixel2D[] points, int obsColor) {
        if (points == null || points.length < 2) {
            return null; // Cannot find paths with less than two points
        }

        List<Pixel2D> pathList = new ArrayList<>();

        // Iterate over consecutive pairs of non-null points and find shortest path between them
        for (int i = 0; i < points.length - 1; i++) {
            Pixel2D p1 = points[i];
            Pixel2D p2 = points[i + 1];

            // Skip null points
            if (p1 == null || p2 == null) {
                continue;
            }

            // Check if both points are non-null
            Pixel2D[] shortestPath = shortestPath(p1, p2, obsColor); // Use Dijkstra's algorithm for finding shortest path

            // If a shortest path is found, add it to the list of paths
            if (shortestPath != null) {
                Collections.addAll(pathList, shortestPath);
            }
        }

        // Convert list of paths to array
        return pathList.toArray(new Pixel2D[0]);
    }


    @Override
    public boolean isInside(Pixel2D p) {
        return isInside(p.getX(), p.getY());
    }
    public void setMap(int[][] newM) {
         _map = newM;
    }
    @Override
    public boolean isCyclic() {
        return _cyclicFlag;
    }

    @Override
    public void setCyclic(boolean cy) {
        _cyclicFlag = cy;
    }

    private boolean isInside(int x, int y) {
        return x >= 0 && y >= 0 && x < this.getWidth() && y < this.getHeight();
    }

    public Map2D allDistance(Pixel2D start, int obsColor) {
        int w = _map.length;
        int h = _map[0].length;
        Map2D disMap = new Map(w, h, 0);

        // חשב מספר רכיבים קשורים
        int componentCount = numberOfConnectedComponents(obsColor);

        // עבור כל פיקסל במפה
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Pixel2D currentPixel = new Index2D(i, j, _map[i][j]);
                int shortestDist = 0;

                // אם הפיקסל אינו חסר ואינו חלק מרכיב, חשב את המרחק
                if (_map[i][j] != obsColor) {
                    shortestDist = shortestPathDist(start, currentPixel, obsColor);
                }

                // שמור את המרחק במפה החדשה
                disMap.setPixel(i, j, shortestDist);
            }
        }

        return disMap;
    }


    @Override
    public int numberOfConnectedComponents(int obsColor) {
        int componentCount = 0;
        boolean[][] visited = new boolean[getWidth()][getHeight()]; // מערך המסמן האם פיקסל נבדק או לא

        // לכל פיקסל במפה
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (_map[i][j] != obsColor && !visited[i][j]) { // אם הפיקסל חופשי ולא נבדק עדיין
                    Pixel2D startPixel = new Index2D(i, j, _map[i][j]); // הגדרת הפיקסל הנוכחי כנקודת ההתחלה
                    Pixel2D[] path = shortestPath(startPixel, new Index2D(-1, -1, -1), obsColor);

                    // אם נמצא מסלול לנקודות חופשיות אז ישנה קומפוננטה חדשה
                    if (path != null) {
                        componentCount++;

                        // מסמן את כל הפיקסלים בקומפוננטה הנוכחית כנבדקים
                        for (Pixel2D pixel : path) {
                            visited[pixel.getX()][pixel.getY()] = true;
                        }
                    }
                }
            }
        }

        return componentCount;
    }


    @Override
    public boolean equals(Object ob) {
        boolean ans = false;
        if (this == ob) return true; // אם הפרמטר הוא המפה עצמה, הן שוות
        if (ob == null || getClass() != ob.getClass())
            return false; // אם הפרמטר הוא null או שהטיפוס שלו שונה מטיפוס המפה, הן לא שוות

        Map otherMap = (Map) ob; // עבור עבודת השוואה נוחה יותר, נמיר את האובייקט שהתקבל לסוג Map

        // משווה את המפות לפי התוכן שלהן
        return Arrays.deepEquals(this._map, otherMap._map) &&
                this._cyclicFlag == otherMap._cyclicFlag;
    }
    public  Index2D[]  getNeighbors(int obsColor, Index2D p1) {
        Index2D[] neig = new Index2D[4];
        int count = 0;
        if (p1.getY() > 0 && _map[p1.getY() - 1][p1.getX() ] != obsColor ) {
                neig[count] = new Index2D(p1.getX(), p1.getY() - 1, p1);
                count++;

        }
        if (p1.getY() < _map.length - 1 && _map[p1.getY() +1][p1.getX() ] != obsColor){
            neig[count] = new Index2D(p1.getX(), p1.getY() +1, p1);
                    count++;

        }
        if (p1.getX() > 0 && _map[p1.getY()][p1.getX() - 1] != obsColor ){
                 neig[count] = new Index2D(p1.getX() - 1, p1.getY(), p1);
                    count++;
            }

        if (p1.getX() < _map[0].length - 1 && _map[p1.getY()][p1.getX() +1] != obsColor ){
                    neig[count] = new Index2D(p1.getX() +1, p1.getY(), p1);
                    count++;
            }

        Index2D[] all = new Index2D[count];
        for(int i = 0; i<count; i++)
            all[i] = neig[i];

        return all;


    }


    ////////////////////// Private Methods ///////////////////////
    public static class PixelProperties {

        private Pixel2D prev;
        private PixelState state;

        private int steps;

        public PixelProperties() {
            this.prev = null;
            this.state = PixelState.Free;
            this.steps = 0;
        }

        private void increaseStep(int stepsSoFar) {
            this.steps = stepsSoFar + 1;
        }

        private int getSteps() {
            return this.steps;
        }

        public Pixel2D getPrev() {
            return this.prev;
        }


        void setPrev(Pixel2D prev) {
            this.prev = prev;
        }


        public PixelState getState() {
            return this.state;

        }


        void setState(PixelState state) {
            this.state = state;
        }

        public enum PixelState {
            Free,
            Explored,
        }
    }

    public void initPixelPropMap(PixelProperties[][] pixelProp, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixelProp[i][j] = new PixelProperties();
            }
        }
    }





    public void showMsg(String msg) {

        JOptionPane.showMessageDialog(this, msg);
    }
}

class MessageDialogs1 {
    public static void main(String args[]) {
        Map f = new Map();
        f.setBounds(200, 200, 400, 300);
        f.setResizable(false);
        f.setVisible(true);
    }
}










