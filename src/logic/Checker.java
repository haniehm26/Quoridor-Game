package logic;

import java.util.ArrayList;

public class Checker {
    private final Board board;
    private final Position notValid;

    public Checker(Board board) {
        this.board = board;
        notValid = new Position(-1, -1);
    }

    public Position returnPosition(Position curr, PositionDirection positionDirection, int constant) {
        int x = curr.getX();
        int y = curr.getY();
        if (positionDirection == PositionDirection.DOWN && Position.boundValid(x + constant, y)) {
            return new Position(x + constant, y);
        } else if (positionDirection == PositionDirection.UP && Position.boundValid(x - constant, y)) {
            return new Position(x - constant, y);
        } else if (positionDirection == PositionDirection.RIGHT && Position.boundValid(x, y + constant)) {
            return new Position(x, y + constant);
        } else if (positionDirection == PositionDirection.LEFT && Position.boundValid(x, y - constant)) {
            return new Position(x, y - constant);
        }
        return notValid;
    }

    public boolean checkFence(Position curr, PositionDirection positionDirection) {
        if (PositionDirection.DOWN == positionDirection) {
            return board.pawnDownCheckFence(curr);
        } else if (PositionDirection.UP == positionDirection) {
            return board.pawnUpCheckFence(curr);
        } else if (PositionDirection.RIGHT == positionDirection) {
            return board.pawnRightCheckFence(curr);
        } else if (PositionDirection.LEFT == positionDirection) {
            return board.pawnLeftCheckFence(curr);
        }
        return false;
    }

    public PositionDirection returnDirection(Position x, Position y) {
        if (x.getY() == y.getY() && x.getX() + 1 == y.getX()) {
            return PositionDirection.DOWN;
        } else if (x.getY() == y.getY() && x.getX() - 1 == y.getX()) {
            return PositionDirection.UP;
        } else if (x.getX() == y.getX() && x.getY() + 1 == y.getY()) {
            return PositionDirection.RIGHT;
        } else if (x.getX() == y.getX() && x.getY() - 1 == y.getY()) {
            return PositionDirection.LEFT;
        }
        return PositionDirection.NULL;
    }

    public boolean checkPawnPosition(Position direction, Position curr, PositionDirection positionDirection) {
        return (direction.valueValid() && board.pawnCheckCell(direction) && checkFence(curr, positionDirection));
    }

    public boolean canJump(Position opponent, Position jump, PositionDirection positionDirection) {
        return ((board.pawnCheckCell(jump)) && (checkFence(opponent, positionDirection)));
    }

    public void returnJumpDirection(ArrayList<Position> list, Position curr, Position opponent, Position lastPosition, PositionDirection currDir) {
        Position directJump = returnPosition(curr, currDir, 2);
        if (currDir == PositionDirection.DOWN) {
            if (canJump(opponent, directJump, currDir)) {
                directJump.setPositionDirection(PositionDirection.JUMP_DOWN);
                list.add(directJump);
            } else {
                Position right = returnPosition(lastPosition, PositionDirection.RIGHT, 1);
                if (right.valueValid() && canJump(opponent, right, PositionDirection.RIGHT)) {
                    right.setPositionDirection(PositionDirection.DOWN_RIGHT);
                    list.add(right);
                }
                Position left = returnPosition(lastPosition, PositionDirection.LEFT, 1);
                if (left.valueValid() && canJump(opponent, left, PositionDirection.LEFT)) {
                    left.setPositionDirection(PositionDirection.DOWN_LEFT);
                    list.add(left);
                }
            }
        } else if (currDir == PositionDirection.UP) {
            if (canJump(opponent, directJump, currDir)) {
                directJump.setPositionDirection(PositionDirection.JUMP_UP);
                list.add(directJump);
            } else {
                Position left = returnPosition(lastPosition, PositionDirection.LEFT, 1);
                if (left.valueValid() && canJump(opponent, left, PositionDirection.LEFT)) {
                    left.setPositionDirection(PositionDirection.UP_LEFT);
                    list.add(left);
                }
                Position right = returnPosition(lastPosition, PositionDirection.RIGHT, 1);
                if (right.valueValid() && canJump(opponent, right, PositionDirection.RIGHT)) {
                    right.setPositionDirection(PositionDirection.UP_RIGHT);
                    list.add(right);
                }
            }
        } else if (currDir == PositionDirection.RIGHT) {
            if (canJump(opponent, directJump, currDir)) {
                directJump.setPositionDirection(PositionDirection.JUMP_RIGHT);
                list.add(directJump);
            } else {
                Position up = returnPosition(lastPosition, PositionDirection.UP, 1);
                if (up.valueValid() && canJump(opponent, up, PositionDirection.UP)) {
                    up.setPositionDirection(PositionDirection.RIGHT_UP);
                    list.add(up);
                }
                Position down = returnPosition(lastPosition, PositionDirection.DOWN, 1);
                if (down.valueValid() && canJump(opponent, down, PositionDirection.DOWN)) {
                    down.setPositionDirection(PositionDirection.RIGHT_DOWN);
                    list.add(down);
                }
            }
        } else if (currDir == PositionDirection.LEFT) {
            if (canJump(opponent, directJump, currDir)) {
                directJump.setPositionDirection(PositionDirection.JUMP_LEFT);
                list.add(directJump);
            } else {
                Position up = returnPosition(lastPosition, PositionDirection.UP, 1);
                if (up.valueValid() && canJump(opponent, up, PositionDirection.UP)) {
                    up.setPositionDirection(PositionDirection.LEFT_UP);
                    list.add(up);
                }
                Position down = returnPosition(lastPosition, PositionDirection.DOWN, 1);
                if (down.valueValid() && canJump(opponent, down, PositionDirection.DOWN)) {
                    down.setPositionDirection(PositionDirection.LEFT_DOWN);
                    list.add(down);
                }
            }
        }
    }
}
