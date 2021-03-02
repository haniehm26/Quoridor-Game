package AI;

public class Distance {
    private int x;
    private int y;
    private int distance;

    public Distance(int x, int y, int distance) {
        this.x = x;
        this.y = y;
        this.distance = distance;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Distance{" +
                "x=" + x +
                ", y=" + y +
                ", distance=" + distance +
                '}';
    }
}
