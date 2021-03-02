package logic;

public class Position {
    private int X;
    private int Y;
    private PositionDirection positionDirection;

    public Position() {
    }

    public Position(int x, int y) {
        X = x;
        Y = y;
    }

    public Position(int x, int y, PositionDirection positionDirection) {
        X = x;
        Y = y;
        this.positionDirection = positionDirection;
    }

    public void setX(int x) {
        X = x;
    }

    public void setY(int y) {
        Y = y;
    }

    public void setPositionDirection(PositionDirection positionDirection) {
        this.positionDirection = positionDirection;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public PositionDirection getPositionDirection() {
        return positionDirection;
    }

    public boolean valueValid() {
        return X != -1 && Y != -1;
    }

    public static boolean boundValid(int x, int y) {
        return x >= 0 && y >= 0 && x <= 8 && y <= 8;
    }

    @Override
    public String toString() {
        return "logic.Position{" +
                "X=" + X +
                ", Y=" + Y +
                ", positionDirection=" + positionDirection +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return X == position.X && Y == position.Y;
    }
}
