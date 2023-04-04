import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.Queue;

// Models a board in the 8-puzzle game or its generalization.
public class Board {
    private final int[][] tiles;
    private final int N;
    private int manhanttan;
    private int hamming;
    private int row;

    // Construct a board from an N-by-N array of tiles, where
    // tiles[i][j] = tile at row i and column j, and 0 represents the blank
    // square.
    public Board(int[][] tiles) {

        this.N = tiles.length;
        this.tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, N);
        }

        this.manhanttan = manhattan();
        this.hamming = hamming();

    }

    // Tile at row i and column j.
    public int tileAt(int i, int j) {
        return this.tiles[i][j];
    }

    // Size of this board.
    public int size() {
        return N;
    }

    // Number of tiles out of place.
    public int hamming() {
        hamming = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.tiles[i][j] != 0) {
                    if (this.tiles[i][j] != N * i + j + 1) {
                        hamming += 1;
                    }
                }
            }
        }

        return hamming;

    }

    // Sum of Manhattan distances between tiles and goal.
    public int manhattan() {
        manhanttan = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int Pos = tiles[i][j];
                if (Pos == 0) {
                    continue;
                } else {
                    int Row = (Pos - 1) / N;
                    int Col = (Pos - 1) % N;
                    manhanttan += (Math.abs(i - Row) + Math.abs(j - Col));
                }
            }
        }
        return manhanttan;

    }

    // Is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // Is this board solvable?
    public boolean isSolvable() {
        // updates the row variable to correct row
        blankPos();

        if (this.N % 2 != 0) {
            if (inversions() % 2 != 0)
                return false;
        }
        if (this.N % 2 == 0) {
            if ((inversions() + row) % 2 == 0)
                return false;
        }

        return true;
    }

    // Does this board equal that?
    public boolean equals(Board that) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // All neighboring boards.
    public Iterable<Board> neighbors() {

        int pos = blankPos();
        int i = pos / N;
        int j = pos % N;
        int[][] swap;
        Queue<Board> queue = new LinkedList<>();

        // moves tile up
        if (i > 0) {
            swap = cloneTiles();
            int tile = swap[i][j];
            swap[i][j] = swap[i - 1][j];
            swap[i - 1][j] = tile;
            queue.add(new Board(swap));

        }
        // moves tile down
        if (i < N - 1) {
            swap = cloneTiles();
            int tile = swap[i][j];
            swap[i][j] = swap[i + 1][j];
            swap[i + 1][j] = tile;
            queue.add(new Board(swap));

        }
        // moves tile left
        if (j > 0) {
            swap = cloneTiles();
            int tile = swap[i][j];
            swap[i][j] = swap[i][j - 1];
            swap[i][j - 1] = tile;
            queue.add(new Board(swap));


        }
        // moves tile right
        if (j < N - 1) {
            swap = cloneTiles();
            int tile = swap[i][j];
            swap[i][j] = swap[i][j + 1];
            swap[i][j + 1] = tile;
            queue.add(new Board(swap));


        }
        return queue;
    }

    // String representation of this board.
    public String toString() {
        String s = N + "\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s += String.format("%2d", tiles[i][j]);
                if (j < N - 1) {
                    s += " ";
                }
            }
            if (i < N - 1) {
                s += "\n";
            }
        }
        return s;
    }

    // Helper method that returns the position (in row-major order) of the
// blank (zero) tile.
    private int blankPos() {
        int BlanksPos = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) {
                    BlanksPos = i * N + j;
                    this.row = i;
                }
            }
        }
        return BlanksPos;
    }

    // Helper method that returns the number of inversions.
    private int inversions() {
        int Inversions = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < N; k++) {
                    for (int l = 0; l < N; l++) {
                        if (tiles[i][j] != 0 && tiles[k][l] != 0) {
                            if (tiles[i][j] > tiles[k][l]) {
                                if ((i * N + j) < (k * N + l)) {
                                    Inversions++;
                                }
                            }
                        }
                    }
                }
            }
        }
        return Inversions;
    }

    // Helper method that clones the tiles[][] array in this board and
// returns it.
    private int[][] cloneTiles() {

        int[][] clone = new int[N][N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(tiles[i], 0, clone[i], 0, N);
        }
        return clone;

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
        Board board = new Board(tiles);
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        StdOut.println(board.isGoal());
        StdOut.println(board.isSolvable());
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }

    }
}
