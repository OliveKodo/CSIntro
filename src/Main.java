import javax.swing.*;
import java.util.Random;

public class Main {
    private static int[][] selectedPoints = new int[2][2];
    public static double sizeR = 0.13476562;
    public static double startX = 0.03515625;
    public static double startY= 0.0390625;

    private static int pointsCounter = 0;

    public static void main(String[] args) {
        Map map = new Map(randomInt(10,10));
        drawMat(map);
        Mouse(map);
    }

    public static void drawMat(Map map) {
        double r = 1 / 15.0;
        StdDraw.setScale(0, 1); // Adjust scale to match matrix size
        StdDraw.clear();

        int[][] mat = map.getMap();
        for (int y = 0; y < mat.length; y++) {
            for (int x = 0; x < mat[0].length; x++) {
                int v = mat[y][x];
                if (v == 0) {
                    StdDraw.setPenColor(StdDraw.WHITE);
                } else if(v == 1) {
                    StdDraw.setPenColor(StdDraw.GRAY);
                }
                else{
                    StdDraw.setPenColor(StdDraw.PINK);

                }
                double x1 = 0.1 + 2 * r * x;
                double y1 = 1- (0.1 + 2 * r * y);

                StdDraw.filledSquare(x1, y1, r);
            }

        }
    }

    public static void Mouse(Map map) {
        int[][] mat = map.getMap();
        boolean mouseClicked = false;
        while (true) {
            if (StdDraw.mousePressed()) {
                if (!mouseClicked) {
                    onMouseClick(StdDraw.mouseX(), StdDraw.mouseY(), map);
                    mouseClicked = true;
                }
            } else {
                mouseClicked = false;
            }
        }
    }

    public static void onMouseClick(double x, double y, Map map) {

        int[][] mat = map.getMap();

        int matrixX = (int) ((x - startX) /sizeR)  ;
        int matrixY = (int) ( (1-(y+startY))/ sizeR);
        if(matrixY >= mat.length || matrixX >= mat[0].length ||
                x < startX || 1- y < startY ||
                mat[matrixY][matrixX] == 0) {
            map.showMsg("Choose only gray");
            return;
        }
        mat[matrixY][matrixX] = 2;
        map.setMap(mat);
        drawMat(map);
        if (matrixX >= 0 && matrixX < mat[0].length && matrixY >= 0 && matrixY < mat.length) {
            System.out.println("Selected position: " + matrixX + ", " + matrixY);
            // אם נבחרו פחות משתי נקודות, הוסף את הנקודה הנוכחית למערך
            if (pointsCounter < 2) {
                selectedPoints[pointsCounter][0] = matrixX;
                selectedPoints[pointsCounter][1] = matrixY;
                pointsCounter++;
            }
            // כאשר שתי נקודות נבחרו, עבור על המערך והדפס את הנקודות
            if (pointsCounter == 2) {
                System.out.println("Selected points:");
                for (int i = 0; i < selectedPoints.length; i++) {
                    System.out.println(selectedPoints[i][0] + ", " + selectedPoints[i][1]);

                }
                Pixel2D p1 = new Index2D(selectedPoints[0][0],selectedPoints[0][1]);
                Pixel2D p2 = new Index2D(selectedPoints[1][0],selectedPoints[1][1]);
                shortestDistanceUsingShortestPath(map, p1, p2 );

            }
        } else {
            System.out.println("Clicked outside of matrix bounds.");
        }

    }
    public static void shortestDistanceUsingShortestPath(Map map, Pixel2D p1, Pixel2D p2) {
        // קבל את המסלול הקצר ביותר בין שתי הנקודות
        String Msg ="No path";
        Pixel2D[] way =  map.shortestPath(p1, p2, 0);
        int[][] mat= map.getMap();
        if(way != null) {
            Msg = "";
            for (int i = 0; i < way.length; i++) {
                mat[way[i].getY()][way[i].getX()] = 2;
                Msg += way[i].toString();
            }
            Msg = Msg + " length: " + Integer.toString(way.length);


            map.setMap(mat);
            drawMat(map);

        }
        map.showMsg(Msg);
        pointsCounter = 0;
        if(way!= null) {
            for (int i = 0; i < way.length; i++) {
                mat[way[i].getY()][way[i].getX()] = 1;
                Msg += way[i].toString();
            }
        }
        else{
            mat[p1.getY()][p1.getX()] = 1;
            mat[p2.getY()][p2.getX()] = 1;
        }
        map.setMap(mat);
        drawMat(map);
    }
    public static int [][] randomInt(int x, int y){

        int[][]  newM = new int[x][y];
        Random rand = new Random();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                newM[i][j] = rand.nextInt(2);
            }
        }
        return newM;
    }



}
