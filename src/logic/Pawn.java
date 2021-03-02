package logic;

public class Pawn {
    private PawnShape pawnShape;

    public Pawn(PawnShape pawnShape) {
        this.pawnShape = pawnShape;
    }

    public void setPawnShape(PawnShape pawnShape) {
        this.pawnShape = pawnShape;
    }

    public PawnShape getPawnShape() {
        return pawnShape;
    }

    @Override
    public String toString() {
        return "logic.Pawn{" +
                ", pawnShape=" + pawnShape +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pawn pawn = (Pawn) o;
        return pawnShape == pawn.pawnShape;
    }
}
