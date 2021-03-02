package logic;

public class Board {
    private final int SIZE = 9 * 2 - 1;
    private char[][] gameBoard;
    private final char freePawn = '.';
    private final char freeFence = ' ';
    private final char horizontalFence = '_';
    private final char verticalFence = '|';

    public Board() {
        gameBoard = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (j % 2 == 0 && i % 2 == 0)
                    gameBoard[i][j] = freePawn;
                else
                    gameBoard[i][j] = freeFence;
            }
        }
    }

    public int getSIZE() {
        return SIZE;
    }

    public void fenceSetGameBoard(Fence fence) {
        if (fence.getDirection() == FenceDirection.HORIZONTAL) {
            drawHorizontalFence(fence);
        } else if (fence.getDirection() == FenceDirection.VERTICAL) {
            drawVerticalFence(fence);
        }
    }
    public void removeHorizontalFence(Fence fence) {
        gameBoard[fence.getFrom().getX() * 2 + 1][fence.getFrom().getY() * 2] = freeFence;
        gameBoard[fence.getTo().getX() * 2 + 1][fence.getTo().getY() * 2] = freeFence;
        if (fence.getTo().getY() != 0)
            gameBoard[fence.getTo().getX() * 2 + 1][fence.getTo().getY() * 2 - 1] = freeFence;
    }

    public void removeVerticalFence(Fence fence) {
        gameBoard[fence.getFrom().getX() * 2][fence.getFrom().getY() * 2 + 1] = freeFence;
        gameBoard[fence.getTo().getX() * 2][fence.getTo().getY() * 2 + 1] = freeFence;
        if (fence.getTo().getX() != 0)
            gameBoard[fence.getTo().getX() * 2 - 1][fence.getTo().getY() * 2 + 1] = freeFence;
    }

    private void drawHorizontalFence(Fence fence) {
        gameBoard[fence.getFrom().getX() * 2 + 1][fence.getFrom().getY() * 2] = horizontalFence;
        gameBoard[fence.getTo().getX() * 2 + 1][fence.getTo().getY() * 2] = horizontalFence;
        if (fence.getTo().getY() != 0)
            gameBoard[fence.getTo().getX() * 2 + 1][fence.getTo().getY() * 2 - 1] = horizontalFence;
    }

    private void drawVerticalFence(Fence fence) {
        gameBoard[fence.getFrom().getX() * 2][fence.getFrom().getY() * 2 + 1] = verticalFence;
        gameBoard[fence.getTo().getX() * 2][fence.getTo().getY() * 2 + 1] = verticalFence;
        if (fence.getTo().getX() != 0)
            gameBoard[fence.getTo().getX() * 2 - 1][fence.getTo().getY() * 2 + 1] = verticalFence;
    }

    public boolean horizontalFenceCheckCell(Position position) {
        return (fenceCheckCellBound(position) && horizontalFenceCheckCellEmpty(position));
    }

    public boolean verticalFenceCheckCell(Position position) {
        return (fenceCheckCellBound(position) && verticalFenceCheckCellEmpty(position));
    }

    private boolean horizontalFenceCheckCellEmpty(Position position) {
        return gameBoard[position.getX() * 2 + 1][position.getY() * 2] == freeFence;
    }

    private boolean verticalFenceCheckCellEmpty(Position position) {
        return gameBoard[position.getX() * 2][position.getY() * 2 + 1] == freeFence;
    }

    public boolean checkPlus(Position from, Position to) {
        return (((from.getY() - to.getY() == 0) && (gameBoard[from.getX() * 2 + 1][from.getY() * 2 + 1] == freeFence)) ||
                ((from.getX() - to.getX() == 0) && (gameBoard[from.getX() * 2 + 1][from.getY() * 2 + 1] == freeFence)));
    }

    private boolean fenceCheckCellBound(Position position) {
        return (position.getX() * 2 - 1 < SIZE && position.getX() * 2 + 1 <= SIZE &&
                position.getY() * 2 - 1 < SIZE && position.getY() * 2 + 1 <= SIZE);
    }

    public boolean pawnDownCheckFence(Position x) {
        return ((x.getX() * 2 + 1 < SIZE) && (x.getX() * 2 + 1 > -1) &&
                (gameBoard[x.getX() * 2 + 1][x.getY() * 2] == freeFence));
    }

    public boolean pawnUpCheckFence(Position x) {
        return ((x.getX() * 2 - 1 > -1) && (x.getX() * 2 - 1 < SIZE) &&
                (gameBoard[x.getX() * 2 - 1][x.getY() * 2] == freeFence));
    }

    public boolean pawnLeftCheckFence(Position x) {
        return ((x.getY() * 2 - 1 > -1) && (x.getY() * 2 - 1 < SIZE) &&
                (gameBoard[x.getX() * 2][x.getY() * 2 - 1] == freeFence));
    }

    public boolean pawnRightCheckFence(Position x) {
        return ((x.getY() * 2 + 1 > -1) && (x.getY() * 2 + 1 < SIZE) &&
                (gameBoard[x.getX() * 2][x.getY() * 2 + 1] == freeFence));
    }

    public boolean pawnCheckCell(Position position) {
        return (pawnCheckCellBound(position) && pawnCheckCellEmpty(position));
    }

    private boolean pawnCheckCellEmpty(Position position) {
        return (gameBoard[position.getX() * 2][position.getY() * 2] == freePawn);
    }

    private boolean pawnCheckCellBound(Position position) {
        return (position.getX() * 2 > -1 && position.getY() * 2 > -1 && position.getX() * 2 < SIZE && position.getY() * 2 < SIZE);
    }

    public boolean pawnCheckJump(Position x, Position y) {
        return (((x.getX() - y.getX() == 1) && (x.getY() - y.getY() == 0) && horizontalFenceCheckCellEmpty(y)) ||
                ((y.getX() - x.getX() == 1) && (x.getY() - y.getY() == 0) && horizontalFenceCheckCellEmpty(x)) ||
                ((x.getY() - y.getY() == 1) && (x.getX() - y.getX() == 0) && verticalFenceCheckCellEmpty(y)) ||
                ((y.getY() - x.getY() == 1) && (x.getX() - y.getX() == 0) && verticalFenceCheckCellEmpty(x)));
    }

    public void pawnSetGameBoard(Position curr, int x, int y, Pawn pawn) {
        gameBoard[x * 2][y * 2] = freePawn;
        gameBoard[curr.getX() * 2][curr.getY() * 2] = pawn.getPawnShape().toString().charAt(0);
    }

    public char[][] getGameBoard() {
        return gameBoard;
    }

    public void printBoard() {
        System.out.print("  ");
        for (int i = 0; i < SIZE; i++) {
            if (i % 2 == 0)
                System.out.print(i / 2 + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (i % 2 == 0 && j == 0)
                    System.out.print(i / 2 + " ");
                else if (i % 2 == 1 && j == 0)
                    System.out.print("  ");

                System.out.print(gameBoard[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}