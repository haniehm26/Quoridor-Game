package AI;

import logic.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class AI {
    private static final int DEPTH = 8;
    private Player[] players;
    private Board board;
    private Checker checker;
    private Connection connection;
    private final int numberOfPlayers;
    private int AIPlayerGoal, opponentGoal;
    private int[] AIPlayerGoals, opponentGoals;
    private Player AIPlayer, opponentPlayer;
    private Player[] AIPlayers, opponentPlayers;
    private ArrayList<Position> saveFirstValidPositionsAI;
    private ArrayList<Position>[] saveFirstValidPositionsAIs;
    private Position currAI;
    private Position[] currAIs;
    private int numberOfAIFences, numberOfOpponentFences;
    private int[] numberOfAIsFences, numberOfOpponentsFences;
    private int TURN;


    public AI(int numberOfPlayers, Player[] players) {
        this.numberOfPlayers = numberOfPlayers;
        this.players = players;
        setPlayers();
        setGoals();
    }

    public void setMinMax(Player[] players, Board board, Checker checker, Connection connection, int TURN) {
        this.players = players;
        setPlayers();
        this.board = board;
        this.checker = checker;
        this.connection = connection;
        setValidPositions(AIPlayer);
        setValidPositions(opponentPlayer);
        saveFirstValidPositionsAI = AIPlayer.getValidPositions();
        currAI = AIPlayer.getCurrPosition();
        numberOfAIFences = AIPlayer.getFences().size();
        numberOfOpponentFences = opponentPlayer.getFences().size();
        this.TURN = TURN;
    }

    private void setPlayers() {
        if (numberOfPlayers == 2) {
            AIPlayer = players[0];
            opponentPlayer = players[1];
        } else {

        }
    }

    private void setGoals() {
        if (numberOfPlayers == 2) {
            AIPlayerGoal = AIPlayer.getGoal()[0].getX() * 2;
            opponentGoal = opponentPlayer.getGoal()[0].getX() * 2;
        } else {

        }
    }

    private ArrayList<Fence> canMakeFences(int numberOfFences, Player player, Player opponent) {
        int startI = -1, endI = -1;
        int startJ = -1, endJ = -1;

        if (opponent.getCurrPosition().getX() - 1 >= 0 && opponent.getCurrPosition().getX() + 1 < 8) {
            startI = opponent.getCurrPosition().getX() - 1;
            endI = opponent.getCurrPosition().getX();
        } else if (opponent.getCurrPosition().getX() - 1 >= 0 && !(opponent.getCurrPosition().getX() + 1 < 8)) {
            startI = opponent.getCurrPosition().getX() - 1;
            endI = 7;
        } else if (!(opponent.getCurrPosition().getX() - 1 >= 0) && opponent.getCurrPosition().getX() + 1 < 8) {
            startI = 0;
            endI = opponent.getCurrPosition().getX();
        }

        if (opponent.getCurrPosition().getY() - 1 >= 0 && opponent.getCurrPosition().getY() + 1 < 8) {
            startJ = opponent.getCurrPosition().getY() - 1;
            endJ = opponent.getCurrPosition().getY();
        } else if (opponent.getCurrPosition().getY() - 1 >= 0 && !(opponent.getCurrPosition().getY() + 1 < 8)) {
            startJ = opponent.getCurrPosition().getY() - 1;
            endJ = 7;
        } else if (!(opponent.getCurrPosition().getY() - 1 >= 0) && opponent.getCurrPosition().getY() + 1 < 8) {
            startJ = 0;
            endJ = opponent.getCurrPosition().getY();
        }

        Position from, to;
        Fence fence = new Fence(null, null, null);
        ArrayList<Fence> result = new ArrayList<>();
        int counter = numberOfFences == 0 ? 0 : 1;
        while (counter != 0) {
            for (int i = startI; i <= endI; i++) {
                for (int j = startJ; j <= endJ; j++) {
                    from = new Position(i, j);
                    to = new Position(i, j + 1);
                    fence.setFrom(from);
                    fence.setTo(to);
                    if (placeFence(from, to)) {
                        fence.setDirection(FenceDirection.HORIZONTAL);
                        Fence newFence = new Fence(from, to, FenceDirection.HORIZONTAL);
                        if (!result.contains(newFence))
                            result.add(newFence);
                        board.removeHorizontalFence(fence);
                        connection.addConnection(from, to);
                    }
                    to = new Position(i + 1, j);
                    fence.setFrom(from);
                    fence.setTo(to);
                    if (placeFence(from, to)) {
                        fence.setDirection(FenceDirection.VERTICAL);
                        Fence newFence = new Fence(from, to, FenceDirection.VERTICAL);
                        if (!result.contains(newFence))
                            result.add(newFence);
                        board.removeVerticalFence(fence);
                        connection.addConnection(from, to);
                    }
                }
            }
            counter--;

            if (player.getCurrPosition().getX() - 1 >= 0 && player.getCurrPosition().getX() + 1 < 8) {
                startI = player.getCurrPosition().getX() - 1;
                endI = player.getCurrPosition().getX();
            } else if (player.getCurrPosition().getX() - 1 >= 0 && !(player.getCurrPosition().getX() + 1 < 8)) {
                startI = player.getCurrPosition().getX() - 1;
                endI = 7;
            } else if (!(player.getCurrPosition().getX() - 1 >= 0) && player.getCurrPosition().getX() + 1 < 8) {
                startI = 0;
                endI = player.getCurrPosition().getX();
            }


            if (player.getCurrPosition().getY() - 1 >= 0 && player.getCurrPosition().getY() + 1 < 8) {
                startJ = player.getCurrPosition().getY() - 1;
                endJ = player.getCurrPosition().getY();
            } else if (player.getCurrPosition().getY() - 1 >= 0 && !(player.getCurrPosition().getY() + 1 < 8)) {
                startJ = player.getCurrPosition().getY() - 1;
                endJ = 7;
            } else if (!(player.getCurrPosition().getY() - 1 >= 0) && player.getCurrPosition().getY() + 1 < 8) {
                startJ = 0;
                endJ = player.getCurrPosition().getY();
            }
        }
        return result;
    }


    public Object findBestMove(char[][] gameBoard) {
        int bestVal = Integer.MIN_VALUE;
        int moveVal;
        boolean changePawn = true;
        Position resultPosition = null;
        Fence resultFence = null;
        Queue<Object> validMoves = new LinkedList<>();
        validMoves.addAll(saveFirstValidPositionsAI);
        if (numberOfAIFences != 0) {
            validMoves.addAll(canMakeFences(numberOfAIFences, AIPlayer, opponentPlayer));
        }
        while (!validMoves.isEmpty()) {
            Object poll = validMoves.poll();
            if (poll instanceof Position) {
                Position position = (Position) poll;
                int x = position.getX() * 2;
                int y = position.getY() * 2;
                if (gameBoard[x][y] == '.') {
                    gameBoard[x][y] = 'X';
                    gameBoard[currAI.getX() * 2][currAI.getY() * 2] = '.';
                    AIPlayer.setCurrPosition(new Position(x / 2, y / 2));
                    setValidPositions(AIPlayer);
                    moveVal = miniMax(gameBoard, 1, opponentPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    if (moveVal > bestVal) {
                        resultPosition = position;
                        bestVal = moveVal;
                    }
                    gameBoard[x][y] = '.';
                    gameBoard[currAI.getX() * 2][currAI.getY() * 2] = 'X';
                    AIPlayer.setCurrPosition(currAI);
                    setValidPositions(AIPlayer);
                }
            } else {
                Fence fence = (Fence) poll;
                if (placeFence(fence.getFrom(), fence.getTo())) {
                    numberOfAIFences--;
                    moveVal = miniMax(gameBoard, 1, opponentPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    if (moveVal > bestVal) {
                        resultFence = fence;
                        bestVal = moveVal;
                        changePawn = false;
                    }
                    if (fence.getDirection() == FenceDirection.HORIZONTAL) {
                        board.removeHorizontalFence(fence);
                    } else {
                        board.removeVerticalFence(fence);
                    }
                    numberOfAIFences++;
                    connection.addConnection(fence.getFrom(), fence.getTo());
                }
            }
        }
        AIPlayer.setCurrPosition(currAI);
        setValidPositions(AIPlayer);
        if (changePawn) {
            return resultPosition;
        } else {
            return resultFence;
        }
    }

    private boolean winMax(Player curr, Player opponent) {
        PawnShape shape = curr.getPawn().getPawnShape();
        return ((shape == PawnShape.X && curr.getCurrPosition().getX() * 2 == AIPlayerGoal) ||
                (shape == PawnShape.O && opponent.getCurrPosition().getX() * 2 == AIPlayerGoal));
    }

    private boolean winMin(Player curr, Player opponent) {
        PawnShape shape = curr.getPawn().getPawnShape();
        return ((shape == PawnShape.X && opponent.getCurrPosition().getX() * 2 == opponentGoal) ||
                (shape == PawnShape.O && curr.getCurrPosition().getX() * 2 == opponentGoal));
    }

    private int miniMax(char[][] gameBoard, int depth, Player maximizer, int alpha, int beta) {
        Player opponent = maximizer.getPawn().getPawnShape() == PawnShape.X ? opponentPlayer : AIPlayer;
        if (depth == DEPTH) {
            int score;
            if (maximizer.getPawn().getPawnShape() == PawnShape.X) {
                score = evaluate(gameBoard, opponentPlayer, AIPlayer);
            } else {
                score = evaluate(gameBoard, AIPlayer, opponentPlayer);
            }
            return score;
        }

        if (winMax(maximizer, opponent))
            return Integer.MAX_VALUE;

        int value;
        Position curr = new Position(maximizer.getCurrPosition().getX(), maximizer.getCurrPosition().getY());
        Queue<Object> validMoves = new LinkedList<>();
        ArrayList<Position> beforeValid = maximizer.getValidPositions();
        validMoves.addAll(beforeValid);
        if (maximizer.getPawn().getPawnShape() == PawnShape.X) {
            if (numberOfAIFences != 0) {
                validMoves.addAll(canMakeFences(numberOfAIFences, AIPlayer, opponent));
            }
            while (!validMoves.isEmpty()) {
                Object poll = validMoves.poll();
                if (poll instanceof Position) {
                    Position position = (Position) poll;
                    int x = position.getX() * 2;
                    int y = position.getY() * 2;
                    if (gameBoard[x][y] == '.') {

                        gameBoard[x][y] = 'X';
                        gameBoard[curr.getX() * 2][curr.getY() * 2] = '.';
                        maximizer.setCurrPosition(new Position(x / 2, y / 2));
                        setValidPositions(maximizer);

                        value = miniMax(gameBoard, depth + 1, opponent, alpha, beta);

                        gameBoard[x][y] = '.';
                        gameBoard[curr.getX() * 2][curr.getY() * 2] = 'X';

                        maximizer.setCurrPosition(curr);
                        setValidPositions(maximizer);

                        if (value > alpha)
                            alpha = value;
                        if (alpha >= beta) {
                            break;  // beta cut-off
                        }
                    }
                } else {
                    Fence fence = (Fence) poll;
                    if (placeFence(fence.getFrom(), fence.getTo())) {
                        numberOfAIFences--;
                        value = miniMax(gameBoard, depth + 1, opponent, alpha, beta);
                        if (fence.getDirection() == FenceDirection.HORIZONTAL) {
                            board.removeHorizontalFence(fence);
                        } else {
                            board.removeVerticalFence(fence);
                        }
                        numberOfAIFences++;
                        connection.addConnection(fence.getFrom(), fence.getTo());

                        if (value > alpha)
                            alpha = value;
                        if (alpha >= beta) {
                            break;  // beta cut-off
                        }
                    }
                }
            }
            return alpha;

        } else {
            if (numberOfOpponentFences != 0) {
                validMoves.addAll(canMakeFences(numberOfOpponentFences, opponentPlayer, opponent));
            }
            while (!validMoves.isEmpty()) {
                Object poll = validMoves.poll();
                if (poll instanceof Position) {
                    Position position = (Position) poll;
                    int x = position.getX() * 2;
                    int y = position.getY() * 2;
                    if (gameBoard[x][y] == '.') {

                        gameBoard[x][y] = 'O';
                        gameBoard[curr.getX() * 2][curr.getY() * 2] = '.';
                        maximizer.setCurrPosition(new Position(x / 2, y / 2));
                        setValidPositions(maximizer);
                        value = miniMax(gameBoard, depth + 1, opponent, alpha, beta);

                        gameBoard[x][y] = '.';
                        gameBoard[curr.getX() * 2][curr.getY() * 2] = 'O';

                        maximizer.setCurrPosition(curr);
                        setValidPositions(maximizer);

                        if (value < beta)
                            beta = value;
                        if (alpha >= beta) {
                            break;  // alpha cut-off
                        }
                    }
                } else {
                    Fence fence = (Fence) poll;
                    if (placeFence(fence.getFrom(), fence.getTo())) {
                        numberOfOpponentFences--;
                        value = miniMax(gameBoard, depth + 1, opponent, alpha, beta);
                        if (fence.getDirection() == FenceDirection.HORIZONTAL) {
                            board.removeHorizontalFence(fence);
                        } else {
                            board.removeVerticalFence(fence);
                        }
                        numberOfOpponentFences++;
                        connection.addConnection(fence.getFrom(), fence.getTo());

                        if (value < beta)
                            beta = value;
                        if (alpha >= beta) {
                            break;  // alpha cut-off
                        }
                    }
                }
            }
            return beta;
        }
    }

    private int evaluate(char[][] gameBoard, Player curr, Player opponent) {
        int currSPWF = shortestPathWithFences(gameBoard, curr.getCurrPosition(), curr.getPawn().getPawnShape());
        int opponentSPWF = shortestPathWithFences(gameBoard, opponent.getCurrPosition(), opponent.getPawn().getPawnShape());
        int currSPIF = shortestPathIgnoreFences(gameBoard, curr.getCurrPosition(), curr.getPawn().getPawnShape());
        int opponentSPIF = shortestPathWithFences(gameBoard, opponent.getCurrPosition(), opponent.getPawn().getPawnShape());
        int numberOfFences = curr.getFences().size();
        int forwardFence = 0;
        int preferFence = 0;

        if (checkForwardFence(opponent)) {
            if (curr.getPawn().getPawnShape() == PawnShape.X)
                forwardFence = 10;
            else
                forwardFence = -10;
        }
        
        if (opponentSPIF >= currSPIF) {
            if (curr.getPawn().getPawnShape() == PawnShape.X)
                preferFence = 10;
            else
                preferFence = -10;
        }

        int m1 = 5;
        int m2 = 4;
        int m3 = 0;
        int m4 = 3;
        int m5 = 3;

        return (m1 * currSPWF) + (m2 * opponentSPWF) + (m3 * numberOfFences) + (m4 * forwardFence) + (m5 * preferFence);
    }

    private boolean checkForwardFence(Player player) {
        if (player.getPawn().getPawnShape() == PawnShape.X) {
            if (player.getCurrPosition().getX() + 1 < 8) {
                return !board.horizontalFenceCheckCell(player.getCurrPosition());
            }
        } else {
            if (player.getCurrPosition().getX() - 1 >= 0) {
                Position position = new Position(player.getCurrPosition().getX() - 1, player.getCurrPosition().getY());
                return !board.horizontalFenceCheckCell(position);
            }
        }
        return false;
    }

    private int shortestPathWithFences(char[][] grid, Position source, PawnShape pawnShape) {
        int goal = pawnShape == PawnShape.X ? AIPlayerGoal : opponentGoal;
        char opponent = pawnShape == PawnShape.X ? 'O' : 'X';
        int x, y, d;
        int length = grid.length;
        boolean[][] visited = new boolean[length][length];

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (grid[i][j] == '|') {
                    visited[i][j] = true;
                    if (i + 1 < length)
                        visited[i + 1][j] = true;
                    if (i - 1 >= 0)
                        visited[i - 1][j] = true;
                }
                if (grid[i][j] == '_') {
                    visited[i][j] = true;
                    if (j + 1 < length)
                        visited[i][j + 1] = true;
                    if (j - 1 >= 0)
                        visited[i][j - 1] = true;
                }
            }
        }

        Distance s = new Distance(source.getX() * 2, source.getY() * 2, 0);
        Queue<Distance> Q = new LinkedList<>();
        Q.add(s);
        visited[source.getX() * 2][source.getY() * 2] = true;

        while (!Q.isEmpty()) {
            Distance poll = Q.poll();
            x = poll.getX();
            y = poll.getY();
            d = poll.getDistance();

            if (x == goal && grid[x][y] == '.') {
                return -1 * d;
            }

            if (y - 1 >= 0 && !visited[x][y - 1]) {
                if (grid[x][y - 1] == opponent) {
                    if (y - 2 >= 0 && !visited[x][y - 2] && y - 3 >= 0 && !visited[x][y - 3]) {
                        addQ(Q, visited, x, y - 3, d + 1);
                    }
                } else
                    addQ(Q, visited, x, y - 1, d + 1);
            }

            if (x - 1 >= 0 && !visited[x - 1][y]) {
                if (grid[x - 1][y] == opponent) {
                    if (x - 2 >= 0 && !visited[x - 2][y] && x - 3 >= 0 && !visited[x - 3][y]) {
                        addQ(Q, visited, x - 3, y, d + 1);
                    }
                } else
                    addQ(Q, visited, x - 1, y, d + 1);
            }

            if (x + 1 < length && !visited[x + 1][y]) {
                if (grid[x + 1][y] == opponent) {
                    if (x + 2 < length && !visited[x + 2][y] && x + 3 < length && !visited[x + 3][y]) {
                        addQ(Q, visited, x + 3, y, d + 1);
                    }
                } else
                    addQ(Q, visited, x + 1, y, d + 1);
            }
            if (y + 1 < length && !visited[x][y + 1]) {
                if (grid[x][y + 1] == opponent) {
                    if (y + 2 < length && !visited[x][y + 2] && y + 3 < length && !visited[x][y + 3]) {
                        addQ(Q, visited, x, y + 3, d + 1);
                    }
                } else
                    addQ(Q, visited, x, y + 1, d + 1);
            }
        }
        return -1;
    }

    private int shortestPathIgnoreFences(char[][] grid, Position source, PawnShape pawnShape) {
        int goal = pawnShape == PawnShape.X ? AIPlayerGoal : opponentGoal;
        char opponent = pawnShape == PawnShape.X ? 'O' : 'X';
        int x, y, d;
        int length = grid.length;
        boolean[][] visited = new boolean[length][length];

        Distance s = new Distance(source.getX() * 2, source.getY() * 2, 0);
        Queue<Distance> Q = new LinkedList<>();
        Q.add(s);
        visited[source.getX() * 2][source.getY() * 2] = true;

        while (!Q.isEmpty()) {
            Distance poll = Q.poll();
            x = poll.getX();
            y = poll.getY();
            d = poll.getDistance();

            if (x == goal && grid[x][y] == '.') {
                return -1 * d;
            }

            if (y - 1 >= 0 && !visited[x][y - 1]) {
                if (grid[x][y - 1] == opponent) {
                    if (y - 2 >= 0 && !visited[x][y - 2] && y - 3 >= 0 && !visited[x][y - 3]) {
                        addQ(Q, visited, x, y - 3, d + 1);
                    }
                } else
                    addQ(Q, visited, x, y - 1, d + 1);
            }

            if (x - 1 >= 0 && !visited[x - 1][y]) {
                if (grid[x - 1][y] == opponent) {
                    if (x - 2 >= 0 && !visited[x - 2][y] && x - 3 >= 0 && !visited[x - 3][y]) {
                        addQ(Q, visited, x - 3, y, d + 1);
                    }
                } else
                    addQ(Q, visited, x - 1, y, d + 1);
            }

            if (x + 1 < length && !visited[x + 1][y]) {
                if (grid[x + 1][y] == opponent) {
                    if (x + 2 < length && !visited[x + 2][y] && x + 3 < length && !visited[x + 3][y]) {
                        addQ(Q, visited, x + 3, y, d + 1);
                    }
                } else
                    addQ(Q, visited, x + 1, y, d + 1);
            }
            if (y + 1 < length && !visited[x][y + 1]) {
                if (grid[x][y + 1] == opponent) {
                    if (y + 2 < length && !visited[x][y + 2] && y + 3 < length && !visited[x][y + 3]) {
                        addQ(Q, visited, x, y + 3, d + 1);
                    }
                } else
                    addQ(Q, visited, x, y + 1, d + 1);
            }
        }
        return -1;
    }


    private void addQ(Queue<Distance> Q, boolean[][] visited, int x, int y, int distance) {
        Q.add(new Distance(x, y, distance));
        visited[x][y] = true;
    }

    private void setValidPositions(Player currPlayer) {
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
        currPlayer.setValidPositions(list);
    }


    private boolean placeFence(Position from, Position to) {
        boolean isValidPlace = true;
        int fromX = from.getX();
        int fromY = from.getY();
        int toX = to.getX();
        int toY = to.getY();
        Position min = null;
        Position max = null;
        FenceDirection direction = FenceDirection.NULL;
        boolean checkHorizontalVertical = Math.abs(fromX - toX) == 0 && Math.abs(fromY - toY) == 1;
        boolean checkVerticalHorizontal = Math.abs(fromY - toY) == 0 && Math.abs(fromX - toX) == 1;
        if (checkHorizontalVertical) {
            min = new Position(fromX, Math.min(fromY, toY));
            max = new Position(fromX, Math.max(fromY, toY));
            if (board.horizontalFenceCheckCell(min) && board.horizontalFenceCheckCell(max)) {
                direction = FenceDirection.HORIZONTAL;
            }
        } else if (checkVerticalHorizontal) {
            min = new Position(Math.min(fromX, toX), fromY);
            max = new Position(Math.max(fromX, toX), fromY);
            if (board.verticalFenceCheckCell(min) && board.verticalFenceCheckCell(max)) {
                direction = FenceDirection.VERTICAL;
            }
        }

        if (direction != FenceDirection.NULL) {
            if (board.checkPlus(min, max)) {
                if (checkConnection(min, max)) {
                    board.fenceSetGameBoard(new Fence(min, max, direction));
                } else {
                    connection.addConnection(min, max);
                    isValidPlace = false;
                }
            } else {
                isValidPlace = false;
            }
        } else {
            isValidPlace = false;
        }
        return isValidPlace;
    }

    private boolean checkConnection(Position min, Position max) {
        connection.removeConnection(min, max);
        for (int i = 0; i < numberOfPlayers; i++) {
            Position curr = players[i].getCurrPosition();
            Position[] goal = players[i].getGoal();
            if (!connection.findPath(curr, goal))
                return false;
        }
        return true;
    }
}
