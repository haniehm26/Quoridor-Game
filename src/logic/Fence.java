package logic;

public class Fence {
    private Position from;
    private Position to;
    private FenceDirection direction;

    public Fence(Position from, Position to, FenceDirection direction) {
        this.from = from;
        this.to = to;
        this.direction = direction;
    }

    public void setFrom(Position from) {
        this.from = from;
    }

    public void setTo(Position to) {
        this.to = to;
    }

    public void setDirection(FenceDirection direction) {
        this.direction = direction;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public FenceDirection getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "logic.Fence{" +
                "from=" + from +
                ", to=" + to +
                ", direction=" + direction +
                '}';
    }
}
