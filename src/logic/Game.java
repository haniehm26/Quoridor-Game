package logic;

//import AI.MinMax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


public class Game {
    private final int numberOfPlayers;
    private final Player[] players;
    private final int SIZE = 9;
    private final int TOTAL_FENCES = 20;
    private final int eachPlayerFences;
    private final Position initialFence;
    private int TURN;
    private boolean END;
    private Board board;
    private Checker checker;
    private Connection connection;
//    private MinMax minMax;

    public Game(Player[] players) {
        setBoard();
        this.players = players;
        numberOfPlayers = this.players.length;
        eachPlayerFences = TOTAL_FENCES / numberOfPlayers;
        initialFence = new Position(-1, -1);
        setPlayers();
//        minMax = new MinMax(this.players);
    }

    private void setBoard() {
        board = new Board();
        connection = new Connection();
        checker = new Checker(board);
    }

    private void setPlayers() {
        if (numberOfPlayers == 2 || numberOfPlayers == 4) {
            setPlayersFences();
            setPlayersFirstTurn();
            setPlayersStartPosition();
            setPlayersGoalRow();
        } else {
            System.out.println("Illegal number of players");
        }
    }

    private void setPlayersFences() {
        for (int i = 0; i < numberOfPlayers; i++) {
            ArrayList<Fence> fences = new ArrayList<>(eachPlayerFences);
            for (int j = 0; j < eachPlayerFences; j++) {
                fences.add(new Fence(initialFence, initialFence, FenceDirection.NULL));
            }
            players[i].setFences(fences);
        }
    }

    private void setPlayersFirstTurn() {
        Random random = new Random();
        int turn = random.nextInt(numberOfPlayers);
        players[turn].setTurn(true);
        TURN = turn;
    }

    private void setPlayersStartPosition() {
        int size = SIZE - 1;
        Position[] positions = new Position[numberOfPlayers];
        Pawn pawn;
        for (int i = 0; i < numberOfPlayers; i++) {
            positions[i] = new Position();
        }
        for (int i = 0; i < numberOfPlayers; i++) {
            if (i == 0) {
                positions[i].setX(i);
                positions[i].setY(size / 2);
                pawn = new Pawn(PawnShape.X);
            } else if (i == 1) {
                positions[i].setX(size);
                positions[i].setY(size / 2);
                pawn = new Pawn(PawnShape.O);
            } else if (i == 2) {
                positions[i].setX(size / 2);
                positions[i].setY(0);
                pawn = new Pawn(PawnShape.S);
            } else {
                positions[i].setX(size / 2);
                positions[i].setY(size);
                pawn = new Pawn(PawnShape.T);
            }
            players[i].setPawn(pawn);
            players[i].setCurrPosition(positions[i]);
            board.pawnSetGameBoard(positions[i], positions[i].getX(), positions[i].getY(), pawn);
        }
    }

    private void setPlayersGoalRow() {
        for (int i = 0; i < numberOfPlayers; i++) {
            Position[] goal = new Position[SIZE];
            for (int j = 0; j < SIZE; j++) {
                if (i == 0) {
                    goal[j] = new Position(8, j);
                } else if (i == 1) {
                    goal[j] = new Position(0, j);
                } else if (i == 2) {
                    goal[j] = new Position(j, 8);
                } else {
                    goal[j] = new Position(j, 0);
                }
            }
            players[i].setGoal(goal);
        }
    }

    private void setTurn() {
        TURN++;
        if (numberOfPlayers == 4) {
            if (TURN > 3) {
                TURN = 0;
            }
        } else if (numberOfPlayers == 2) {
            if (TURN > 1) {
                TURN = 0;
            }
        } else {
            System.out.println("Illegal number of players");
        }
        players[TURN].setTurn(true);
        for (int i = 0; i < numberOfPlayers; i++) {
            if (i != TURN) {
                players[i].setTurn(false);
            }
        }
    }

    private int getTurn() {
        return TURN;
    }

    private void setCurrPlayerPosition(Position position) {
        players[getTurn()].setCurrPosition(position);
    }

    private void setValidPositions() {
        Player currPlayer = players[getTurn()];
        ArrayList<Position> list = new ArrayList<>();
        Position curr = currPlayer.getCurrPosition();

        Position p_down = checker.returnPosition(curr, PositionDirection.DOWN, 1);
        if (checker.checkPawnPosition(p_down, curr, PositionDirection.DOWN)) {
            p_down.setPositionDirection(PositionDirection.DOWN);
            list.add(p_down);
        }
        Position p_up = checker.returnPosition(curr, PositionDirection.UP, 1);
        if (checker.checkPawnPosition(p_up, curr, PositionDirection.UP)) {
            p_up.setPositionDirection(PositionDirection.UP);
            list.add(p_up);
        }
        Position p_right = checker.returnPosition(curr, PositionDirection.RIGHT, 1);
        if (checker.checkPawnPosition(p_right, curr, PositionDirection.RIGHT)) {
            p_right.setPositionDirection(PositionDirection.RIGHT);
            list.add(p_right);
        }
        Position p_left = checker.returnPosition(curr, PositionDirection.LEFT, 1);
        if (checker.checkPawnPosition(p_left, curr, PositionDirection.LEFT)) {
            p_left.setPositionDirection(PositionDirection.LEFT);
            list.add(p_left);
        }

        for (int i = 0; i < numberOfPlayers; i++) {
            if (players[i] != currPlayer) {
                Position opponent = players[i].getCurrPosition();
                if (board.pawnCheckJump(opponent, curr)) {
                    if (checker.returnDirection(curr, opponent) == PositionDirection.DOWN) {
                        checker.returnJumpDirection(list, curr, opponent, p_down, PositionDirection.DOWN);
                    } else if (checker.returnDirection(curr, opponent) == PositionDirection.UP) {
                        checker.returnJumpDirection(list, curr, opponent, p_up, PositionDirection.UP);
                    } else if (checker.returnDirection(curr, opponent) == PositionDirection.RIGHT) {
                        checker.returnJumpDirection(list, curr, opponent, p_right, PositionDirection.RIGHT);
                    } else if (checker.returnDirection(curr, opponent) == PositionDirection.LEFT) {
                        checker.returnJumpDirection(list, curr, opponent, p_left, PositionDirection.LEFT);
                    }
                }
            }
        }
        players[getTurn()].setValidPositions(list);
    }

    private boolean checkWinner(Pawn pawn, Position currPosition) {
        if (pawn.getPawnShape() == PawnShape.X) {
            return currPosition.getX() == 8;
        } else if (pawn.getPawnShape() == PawnShape.O) {
            return currPosition.getX() == 0;
        } else if (pawn.getPawnShape() == PawnShape.S) {
            return currPosition.getY() == 8;
        } else {
            return currPosition.getY() == 0;
        }
    }

    boolean changePawn(PositionDirection positionDirection) {
        boolean isValidMove = true;
        Player currPlayer = players[getTurn()];
        ArrayList<Position> validPositions = currPlayer.getValidPositions();
        Pawn pawn = currPlayer.getPawn();
        Position currPosition = currPlayer.getCurrPosition();
        int x = currPosition.getX();
        int y = currPosition.getY();
        for (int i = 0; i < validPositions.size(); i++) {
            Position curr = currPlayer.getValidPositions().get(i);
            if (positionDirection == curr.getPositionDirection() && currPlayer.isTurn()) {
                currPosition = new Position(curr.getX(), curr.getY(), curr.getPositionDirection());
                board.pawnSetGameBoard(currPosition, x, y, pawn);
                break;
            }
        }
        if (!currPosition.equals(currPlayer.getCurrPosition())) {
            setCurrPlayerPosition(currPosition);
            setTurn();
        } else {
            setCurrPlayerPosition(currPosition);
            System.out.println("Invalid Movement!\n");
            System.out.println("You can do:");
            for (Position validPosition : validPositions) {
                System.out.println(validPosition.getPositionDirection());
            }
            isValidMove = false;
        }
        if (checkWinner(pawn, currPosition)) {
            System.out.println("logic.Player " + pawn.getPawnShape() + " is winner!");
            printValidPositions(players[getTurn()].getValidPositions());
            board.printBoard();
            END = true;
        }
        return isValidMove;
    }

    boolean placeFence(Position from, Position to) {
        boolean isValidPlace = true;
        Player currPlayer = players[getTurn()];
        if (currPlayer.restFences() != 0) {
            int fromX = from.getX();
            int fromY = from.getY();
            int toX = to.getX();
            int toY = to.getY();
            Position min = null;
            Position max = null;
            boolean checkHorizontalVertical = Math.abs(fromX - toX) == 0 && Math.abs(fromY - toY) == 1;
            boolean checkVerticalHorizontal = Math.abs(fromY - toY) == 0 && Math.abs(fromX - toX) == 1;
            if (checkHorizontalVertical) {
                min = new Position(fromX, Math.min(fromY, toY));
                max = new Position(fromX, Math.max(fromY, toY));
                if (board.horizontalFenceCheckCell(min) && board.horizontalFenceCheckCell(max)) {
                    currPlayer.getFences().get(0).setDirection(FenceDirection.HORIZONTAL);
                }
            } else if (checkVerticalHorizontal) {
                min = new Position(Math.min(fromX, toX), fromY);
                max = new Position(Math.max(fromX, toX), fromY);
                if (board.verticalFenceCheckCell(min) && board.verticalFenceCheckCell(max)) {
                    currPlayer.getFences().get(0).setDirection(FenceDirection.VERTICAL);
                }
            }
            if (currPlayer.getFences().get(0).getDirection() != FenceDirection.NULL) {
                if (board.checkPlus(min, max)) {
                    currPlayer.getFences().get(0).setFrom(min);
                    currPlayer.getFences().get(0).setTo(max);
                    if ((checkConnection(min, max))) {
                        board.fenceSetGameBoard(currPlayer.getFences().get(0));
                        players[getTurn()].getFences().remove(0);
                        setTurn();
                    } else {
                        connection.addConnection(min, max);
                        isValidPlace = false;
                        System.out.println("logic.Fence can't be there!!");
                    }
                } else {
                    isValidPlace = false;
                    System.out.println("Plus shape fence!");
                }
            } else {
                isValidPlace = false;
                System.out.println("Wrong position for fence!");
            }
        } else {
            isValidPlace = false;
            System.out.println("Out of fence!");
        }
        return isValidPlace;
    }

    private boolean checkConnection(Position min, Position max) {
        connection.removeConnection(min, max);
        boolean checkConnection = false;
        for (int i = 0; i < numberOfPlayers; i++) {
            Position curr = players[i].getCurrPosition();
            Position[] goal = players[i].getGoal();
            checkConnection = connection.findPath(curr, goal);
        }
        return checkConnection;
    }

    void printValidPositions(ArrayList<Position> validPositions) {
        char[][] boardGame = new char[board.getSIZE()][board.getSIZE()];
        for (int i = 0; i < board.getSIZE(); i++) {
            for (int j = 0; j < board.getSIZE(); j++) {
                boardGame[i][j] = board.getGameBoard()[i][j];
            }
        }
        for (Position positions : validPositions) {
            int x = positions.getX() * 2;
            int y = positions.getY() * 2;
            boardGame[x][y] = '*';
        }
        System.out.print("  ");
        for (int i = 0; i < board.getSIZE(); i++) {
            if (i % 2 == 0)
                System.out.print(i / 2 + " ");
        }
        System.out.println();
        for (int i = 0; i < board.getSIZE(); i++) {
            for (int j = 0; j < board.getSIZE(); j++) {
                if (i % 2 == 0 && j == 0)
                    System.out.print(i / 2 + " ");
                else if (i % 2 == 1 && j == 0)
                    System.out.print("  ");
                System.out.print(boardGame[i][j]);
            }
            System.out.println();
        }
        System.out.println("\nnumber of available fences:");
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.println(players[i].getPawn().getPawnShape() + ": " + players[i].restFences());
        }
    }

    void startGame() {
        Scanner scanner = new Scanner(System.in);
        boolean changeTurn = true;
        while (!END) {
            if (changeTurn) {
                setValidPositions();
                printValidPositions(players[getTurn()].getValidPositions());
            }
            System.out.println("\nIt's " + players[getTurn()].getPawn().getPawnShape() + " turn:\n");
            System.out.println("\nChoose an action:\nP/p for set pawn, \nF/f for set fence\n");
            String line = scanner.next();
            if (line.equals("P") || line.equals("p")) {
                changeTurn = false;
                System.out.println("Enter pawn direction:");
                line = scanner.next();
                PositionDirection positionDirection = players[getTurn()].makeMove(line);
                if (positionDirection != PositionDirection.NULL) {
                    changeTurn = changePawn(players[getTurn()].makeMove(line));
                }
            } else if (line.equals("F") || line.equals("f")) {
                changeTurn = false;
                Position from = null;
                Position to = null;
                System.out.println("fromX [0,8]:");
                int fromX = scanner.nextInt();
                System.out.println("fromY [0,8]:");
                int fromY = scanner.nextInt();
                if (fromX >= 0 && fromX <= 8 && fromY >= 0 && fromY <= 8) {
                    from = players[getTurn()].makeFence(fromX, fromY);
                }
                System.out.println("toX [0,8]:");
                int toX = scanner.nextInt();
                System.out.println("toY [0,8]:");
                int toY = scanner.nextInt();
                if (toX >= 0 && toX <= 8 && toY >= 0 && toY <= 8) {
                    to = players[getTurn()].makeFence(toX, toY);
                } else {
                    System.out.println("Wrong input!");
                }
                if (from != null && to != null) {
                    changeTurn = placeFence(from, to);
                }
            }
        }
    }
}