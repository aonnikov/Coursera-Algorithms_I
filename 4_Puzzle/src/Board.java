import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by aonnikov on 03/08/2017.
 */
public class Board {

    private final int n;
    private final int blockCount;
    private final char[] blocks;
    private int pos;

    /**
     * Construct a board from an n-by-n array of blocks
     * where blocks[i][j] = block in row i, column j
     * @param blocks the blocks
     */
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException("Blocks are null!");
        }

        this.n = blocks.length;
        this.blockCount = this.n * this.n;
        this.blocks = new char[this.blockCount];

        int idx = 0;
        for (int i = 0; i < this.n; i++) {
            if (blocks[i].length != this.n) {
                throw new IllegalArgumentException("Incorrect board size!");
            }
            for (int j = 0; j < this.n; j++) {
                int value = blocks[i][j];
                this.blocks[idx] = (char) value;
                if (0 == value) {
                    this.pos = idx;
                }

                idx++;
            }
        }
    }

    private Board(int n, char[] blocks, int pos) {
        this.n = n;
        this.blockCount = this.n * this.n;
        this.blocks = blocks;
        this.pos = pos;
    }

    /**
     * Return the board dimension
     * @return the board dimension
     */
    public int dimension() {
        return this.n;
    }

    /**
     * Calculate the Hamming priority for the board (number of blocks out of place)
     * @return
     */
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < this.blockCount; i++) {
            int value = this.blocks[i];
            if (value != 0 && value != i + 1) {
                hamming++;
            }
        }
        return hamming;
    }

    /**
     * Calculate sum of Manhattan distances between blocks and goal
     * @return
     */
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < this.blockCount; i++) {
            int value = this.blocks[i];
            if (value != 0) {
                int hD = (i / this.n) - ((value - 1) / this.n);
                int vD = (i % this.n) - ((value - 1) % this.n);
                manhattan += Math.abs(hD) + Math.abs(vD);
            }
        }
        return manhattan;
    }

    /**
     * Is this board the goal board?
     * @return true if the board is the goal board
     */
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    private char[] copyBlocks() {
        char[] blocksBopy = new char[this.blockCount];
        System.arraycopy(this.blocks, 0, blocksBopy, 0, this.blockCount);
        return blocksBopy;
    }
    /**
     * Return a board that is obtained by exchanging any pair of blocks
     * @return
     */
    public Board twin() {
        char[] twinBlocks = copyBlocks();

        if (this.getRow() == 0) {
            swap(twinBlocks, this.blockCount - 2, this.blockCount - 1);
        }
        else {
            swap(twinBlocks, 0, 1);
        }
        return new Board(this.n, twinBlocks, this.pos);
    }

    private void move(char[] board, int src, int dst) {
        assert  board[src] == 0;
        board[src] = board[dst];
        board[dst] = 0;
    }

    private void swap(char[] board, int src, int dst) {
        char c = board[src];
        board[src] = board[dst];
        board[dst] = c;
    }

    /**
     * Does this board equal y?
     * @param y the {@link Object}
     * @return true if the board equal y
     */
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }

        if (this == y) {
            return true;
        }

        if (!this.getClass().equals(y.getClass())) {
            return false;
        }

        Board that = (Board) y;
        return Arrays.equals(this.blocks, that.blocks);
    }

    private int getRow() {
        return this.pos / this.n;
    }

    private int getCol() {
        return this.pos % this.n;
    }

    private Board moveLeft() {
        int col = getCol();
        assert col > 0;

        char[] newBlocks = copyBlocks();
        int newPos = this.pos - 1;
        move(newBlocks, this.pos, newPos);
        return new Board(this.n, newBlocks, newPos);
    }

    private Board moveRight() {
        int col = getCol();
        assert col < this.n - 1;

        char[] newBlocks = copyBlocks();
        int newPos = this.pos + 1;
        move(newBlocks, this.pos, newPos);
        return new Board(this.n, newBlocks, newPos);
    }

    private Board moveUp() {
        int row = getRow();
        assert row > 0;

        char[] newBlocks = copyBlocks();
        int newPos = this.pos - this.n;
        move(newBlocks, this.pos, newPos);
        return new Board(this.n, newBlocks, newPos);
    }

    private Board moveDown() {
        int row = getRow();
        assert row < this.n - 1;

        char[] newBlocks = copyBlocks();
        int newPos = this.pos + this.n;
        move(newBlocks, this.pos, newPos);
        return new Board(this.n, newBlocks, newPos);
    }

    /**
     * Return {@link Iterable} of all neighboring boards
     * @return the {@link Iterable}
     */
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new LinkedList<>();

        int row = getRow();
        int col = getCol();

        // top
        if (row > 0) {
            neighbors.add(moveUp());
        }
        // bottom
        if (row < n - 1) {
            neighbors.add(moveDown());
        }
        // left
        if (col > 0) {
            neighbors.add(moveLeft());
        }
        // right
        if (col < this.n - 1) {
            neighbors.add(moveRight());
        }
        return neighbors;
    }

    /**
     * Return string representation of this board (in the output format specified below)
     * @return the {@link String}
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.n + "\n");
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                s.append(String.format("%2d ", (int) this.blocks[i * this.n + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        int[][] blocks = new int[][] {
                { 8, 1, 3 },
                { 4, 0, 2 },
                { 7, 6, 5 }
        };

        Board board = new Board(blocks);
        int manhattan =  board.manhattan();
        int hamming = board.hamming();

        System.out.println("Manhattan: " + manhattan);
        System.out.println("Hamming: " + hamming);
        assert manhattan == 10;
        assert hamming == 5;

        // Testing twin board
        Board twin = board.twin();
        System.out.println("Original:");
        System.out.println(board);
        System.out.println("Twin:");
        System.out.println(twin);
        assert twin.twin().equals(board);

        // Testing neighbors
        int[][] blocksLeft = new int[][] {
                { 8, 1, 3 },
                { 0, 4, 2 },
                { 7, 6, 5 }
        };
        Board left = new Board(blocksLeft);
        assert left.equals(board.moveLeft());

        int[][] blocksDown = new int[][] {
                { 8, 1, 3 },
                { 4, 6, 2 },
                { 7, 0, 5 }
        };
        Board down = new Board(blocksDown);
        assert down.equals(board.moveDown());

        assert board.moveUp().moveDown().equals(board);
        assert board.moveLeft().moveRight().equals(board);

        int[][] blocks1 = new int[][] {
                { 1, 0 },
                { 2, 3 }
        };
        Board board1 = new Board(blocks1);
        System.out.println("Original:");
        System.out.println(board);
        System.out.println("Neighbors:");
        for (Board neighbor : board1.neighbors()) {
            System.out.println(neighbor);
        }

    }
}
