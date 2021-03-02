package logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Connection {
    private final int N = 9;
    private Graph graph;
    private final int V = N * N + 2;

    public Connection() {
        graph = new Graph(V);
        createConnection();
    }

    private boolean checkBound(int i, int j) {
        return (i >= 0 && i < N) && (j >= 0 && j < N);
    }

    private void createConnection() {
        int k = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (checkBound(i, j + 1))
                    graph.addEdge(k, k + 1);
                if (checkBound(i, j - 1))
                    graph.addEdge(k, k - 1);
                if (j < N - 1 && checkBound(i + 1, j))
                    graph.addEdge(k, k + N);
                if (i > 0 && checkBound(i - 1, j))
                    graph.addEdge(k, k - N);
                k++;
            }
        }
    }

    public boolean findPath(Position curr, Position[] goal) {
        int s = curr.getX() * 9 + curr.getY();
        int[] d = new int[goal.length];
        for (int i = 0; i < goal.length; i++) {
            d[i] = goal[i].getX() * 9 + goal[i].getY();
            if (graph.BFS(s, d[i]))
                return true;
        }
        return false;
    }

    //TODO

    public void removeConnection(Position from, Position to) {
        boolean vertical = (from.getY() - to.getY() == 0);
        boolean horizontal = (from.getX() - to.getX() == 0);
        int fromS = from.getX() * 9 + from.getY();
        int toS = to.getX() * 9 + to.getY();
        int fromD = -1, toD = -1;
        if (vertical) {
            fromD = fromS + 1;
            toD = toS + 1;
        } else if (horizontal) {
            fromD = fromS + 9;
            toD = toS + 9;
        }
        graph.removeEdge(fromS, fromD);
        graph.removeEdge(toS, toD);
    }

    public void addConnection(Position from, Position to) {
        boolean vertical = (from.getY() - to.getY() == 0);
        boolean horizontal = (from.getX() - to.getX() == 0);
        int fromS = from.getX() * 9 + from.getY();
        int toS = to.getX() * 9 + to.getY();
        int fromD = -1, toD = -1;
        if (vertical) {
            fromD = fromS + 1;
            toD = toS + 1;
        } else if (horizontal) {
            fromD = fromS + 9;
            toD = toS + 9;
        }
        graph.addEdge(fromS, fromD);
        graph.addEdge(toS, toD);
    }

    class Graph {
        private final int V;
        private List<List<Integer>> adj;

        Graph(int V) {
            this.V = V;
            adj = new ArrayList<>(V);
            for (int i = 0; i < V; i++) {
                adj.add(i, new ArrayList<>());
            }
        }

        void addEdge(int s, int d) {
            if (!adj.get(s).contains(d))
                adj.get(s).add(d);
            if (!adj.get(d).contains(s))
                adj.get(d).add(s);
        }

        void removeEdge(int s, int d) {
            Integer D = d;
            Integer S = s;
            adj.get(s).remove(D);
            adj.get(d).remove(S);
        }

        boolean BFS(int s, int d) {
            if (s == d)
                return true;
            boolean[] visited = new boolean[V];
            Queue<Integer> queue = new LinkedList<>();
            visited[s] = true;
            queue.offer(s);
            List<Integer> edges;
            while (!queue.isEmpty()) {
                s = queue.poll();
                edges = adj.get(s);
                for (int curr : edges) {
                    if (curr == d)
                        return true;
                    if (!visited[curr]) {
                        visited[curr] = true;
                        queue.offer(curr);
                    }
                }
            }
            return false;
        }
    }
}