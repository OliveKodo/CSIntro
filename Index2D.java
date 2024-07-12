
//id = 208040261
public class Index2D implements Pixel2D {
    private int _x, _y;
    private int value;

    public Pixel2D prev;

    public Index2D() {
        this(0, 0);
    }

    public Index2D(int x, int y) {
        _x = x;
        _y = y;
        prev = null;
    }
    public Index2D(int x, int y, int value) {
        _x = x;
        _y = y;
        this.value = value;
    }
    public Index2D(Pixel2D t) {
        this(t.getX(), t.getY());
    }
    public int get_value(){
        return this.value;
    }
    @Override
    public int getX() {
        return _x;
    }

    @Override
    public int getY() {
        return _y;
    }

    public double distance2D(Pixel2D t) throws RuntimeException {
        double ans = 0;
        if (t == null) {
            // Check if the input is null and throw a RuntimeException if it is
            throw new RuntimeException("RuntimeException, Invalid input, t cannot be null");
        } else {
            // Calculate the squared differences in x and y coordinates
            double x = Math.pow(this._x - t.getX(), 2);
            double y = Math.pow(this._y - t.getY(), 2);

            // Calculate the square root of the sum of squared differences to get the Euclidean distance
            ans = Math.sqrt(x + y);
        }
        return ans;
    }

    @Override
    public String toString () {
        return getX() + "," + getY();
    }
    @Override
    public boolean equals (Object t){
        boolean ans = false;
        if (t != null && t instanceof Pixel2D) {
            Pixel2D p3 = new Index2D(3, 2);
            Pixel2D p = (Pixel2D) t;
            ans = this.distance2D(p) == 0;
        }

        return ans;
    }

}

