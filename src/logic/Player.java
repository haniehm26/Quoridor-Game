package logic;

import java.util.ArrayList;
import java.util.Arrays;

public class Player {
    private Pawn pawn;
    private boolean turn;
    private ArrayList<Fence> fences;
    private Position[] goal;
    private Position currPosition;
    private ArrayList<Position> validPositions;

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void setFences(ArrayList<Fence> fences) {
        this.fences = fences;
    }

    public void setGoal(Position[] goal) {
        this.goal = goal;
    }

    public void setValidPositions(ArrayList<Position> validPositions) {
        this.validPositions = validPositions;
    }

    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }

    public void setCurrPosition(Position currPosition) {
        this.currPosition = currPosition;
    }

    public ArrayList<Fence> getFences() {
        return fences;
    }

    public Position[] getGoal() {
        return goal;
    }

    public boolean isTurn() {
        return turn;
    }

    public ArrayList<Position> getValidPositions() {
        return validPositions;
    }

    public Pawn getPawn() {
        return pawn;
    }

    public Position getCurrPosition() {
        return currPosition;
    }

    @Override
    public String toString() {
        return "logic.Player{" +
                "pawn=" + pawn +
                ", turn=" + turn +
                ", fences=" + fences +
                ", goal=" + Arrays.toString(goal) +
                ", curr=" + currPosition +
                ", validPositions=" + validPositions +
                '}';
    }

    public int restFences() {
        int counter = 0;
        for (Fence fence : fences) {
            if (fence.getDirection() == FenceDirection.NULL)
                counter++;
        }
        return counter;
    }

    public Position makeFence(int x, int y) {
        return new Position(x, y);
    }

    public PositionDirection makeMove(String direction) {
        if (direction.equals("U") || direction.equals("u"))
            return PositionDirection.UP;
        else if (direction.equals("D") || direction.equals("d"))
            return PositionDirection.DOWN;
        else if (direction.equals("L") || direction.equals("l"))
            return PositionDirection.LEFT;
        else if (direction.equals("R") || direction.equals("r"))
            return PositionDirection.RIGHT;
        else if (direction.equals("JU") || direction.equals("ju"))
            return PositionDirection.JUMP_UP;
        else if (direction.equals("JD") || direction.equals("jd"))
            return PositionDirection.JUMP_DOWN;
        else if (direction.equals("JL") || direction.equals("jl"))
            return PositionDirection.JUMP_LEFT;
        else if (direction.equals("JR") || direction.equals("jr"))
            return PositionDirection.JUMP_RIGHT;
        else if (direction.equals("UR") || direction.equals("ur"))
            return PositionDirection.UP_RIGHT;
        else if (direction.equals("UL") || direction.equals("ul"))
            return PositionDirection.UP_LEFT;
        else if (direction.equals("DR") || direction.equals("dr"))
            return PositionDirection.DOWN_RIGHT;
        else if (direction.equals("DL") || direction.equals("dl"))
            return PositionDirection.DOWN_LEFT;
        else if (direction.equals("RD") || direction.equals("rd"))
            return PositionDirection.RIGHT_DOWN;
        else if (direction.equals("LD") || direction.equals("ld"))
            return PositionDirection.LEFT_DOWN;
        else if (direction.equals("RU") || direction.equals("ru"))
            return PositionDirection.RIGHT_UP;
        else if (direction.equals("LU") || direction.equals("lu"))
            return PositionDirection.LEFT_UP;
        else {
            System.out.println("Invalid input!\n");
            System.out.println("Choose U/u for UP,\n" + "D/d for DOWN,\n" + "L/l for LEFT,\n" +
                    "R/r for RIGHT,\n" + "JU/ju for JUMP_UP,\n" + "JD/jd for JUMP_DOWN,\n" +
                    "JL/jl for JUMP_LEFT,\n" + "JR/jr for JUMP_RIGHT\n");
            return PositionDirection.NULL;
        }
    }
}
