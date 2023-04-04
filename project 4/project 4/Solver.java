import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

// A solver based on the A* algorithm for the 8-puzzle and its generalizations.

public class Solver {
    private LinkedStack<Board> solution;
    private MinPQ<SearchNode> pq;
    private Board initialB;


    // Helper search node class.
    private class SearchNode {
        private final Board board;
        private final int moves;
        private SearchNode previous;

        SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }
    }

    // Find a solution to the initial board (using the A* algorithm).

    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException();
        if (!initial.isSolvable()) throw new IllegalArgumentException();

        this.initialB = initial;
        Comparator<SearchNode> compare = new ManhattanOrder();
        pq = new MinPQ<>(compare);
        solution = new LinkedStack<>();

        SearchNode first = new SearchNode(initial, 0, null);
        pq.insert(first);

        // finds the goal board and assigns it to goal variable
        int[][] correctBoard = new int[initial.size()][initial.size()];
        int count = 1;

        for (int i = 0; i < initial.size(); i++) {
            for (int j = 0; j < initial.size(); j++) {
                correctBoard[i][j] = count;
                count++;
            }
        }

        correctBoard[initial.size() - 1][initial.size() - 1] = 0;
        Board goal = new Board(correctBoard);

        // traverses the boards until the board is the goal board
        while (!pq.min().board.equals(goal)) {
            SearchNode node = pq.delMin();

            for (Board x : node.board.neighbors()) {
                if (node.moves == 0) {
                    SearchNode next = new SearchNode(x, node.moves + 1, node);
                    pq.insert(next);

                } else if (!x.equals(node.previous.board)) {
                    SearchNode next = new SearchNode(x, node.moves + 1, node);
                    pq.insert(next);

                }
            }

        }

    }

    // The minimum number of moves to solve the initial board.
    public int moves() {
        return pq.min().moves;
    }

    // Sequence of boards in a shortest solution.
    public Iterable<Board> solution() {

        solution = new LinkedStack<>();
        if (initialB.manhattan() == 0) {
            solution.push(initialB);
            return solution;
        }

        SearchNode curr = pq.min();
        while (curr.previous != null) {
            solution.push(curr.board);
            curr = curr.previous;
        }

        return solution;

    }

    // Helper hamming priority function comparator.
    private static class HammingOrder implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            return Integer.compare(a.board.hamming() + a.moves, b.board.hamming() + b.moves);
        }
    }

    // Helper manhattan priority function comparator.
    private static class ManhattanOrder implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            return Integer.compare(a.board.manhattan() + a.moves, b.board.manhattan() + b.moves);
        }

    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        } else {
            StdOut.println("Unsolvable puzzle");
        }
    }
}
