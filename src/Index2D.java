import java.io.Serializable;

public class Index2D implements Pixel2D, Serializable{
    private int _x, _y;

    private int value;

    private Index2D prev;
    public Index2D() {this(0,0);}
    public Index2D(int x, int y) {_x=x;_y=y; prev = null;}
    public Index2D(int x, int y, Index2D p) {_x=x;_y=y; prev = p;}

    public Index2D(Pixel2D t) {this(t.getX(), t.getY());}


    public Index2D(int x, int y, int value) {
        _x = x;
        _y = y;
        this.value = value;
    }





    public Index2D(String pos) {
        String[] coordinates = pos.replaceAll("[()]", "").split(", ");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        _x = x;
        _y = y;

    }

    @Override
    public int getX() {
        return _x;
    }

    public Index2D getPrev(){
        return prev;
    }
    public void setPrev(Index2D p){
        prev= p;
    }
    @Override
    public int getY() {
        return _y;
    }
    public double distance2D(Pixel2D t) throws RuntimeException {
        double ans = -1;
        if (t== null) {
            throw new RuntimeException("RuntimeException, invalid input, t cannot be null");
        } else {
            double x = Math.pow(this._x - t.getX(),2);
            double y = Math.pow(this._y - t.getY(),2);
            ans = Math.sqrt(x+y);
        }

        return ans;
    }
    @Override
    public String toString() {
        return "[" +getX() + "," + getY() + "] ";
    }
    @Override
    public boolean equals(Object t) {
        boolean ans = false;
        if (t != null && t instanceof Pixel2D) {
            Pixel2D p3 = new Index2D(3,2 );
            Pixel2D p = (Pixel2D) t;
            ans = this.distance2D(p) == 0;
        }

        return ans;
    }

}
