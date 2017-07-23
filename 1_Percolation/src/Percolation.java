import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF ufPercolation;

    private final WeightedQuickUnionUF ufFullness;

    private final int size;

    private final int inputIdx;

    private final int outputIdx;

    private final boolean[] openState;

    private int openCount = 0;

    /**
     * Creates n-by-n grid, with all sites blocked
     * @param n
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Percolation table size must be greater then 0!");
        }

        this.size = n;

        int lastIndex = arrayIndex(n, n);
        this.inputIdx = lastIndex + 1;
        this.outputIdx = lastIndex + 2;

        int ufSize = n * n;
        this.ufPercolation = new WeightedQuickUnionUF(ufSize + 2);
        this.ufFullness = new WeightedQuickUnionUF(ufSize + 2);

        this.openState = new boolean[ufSize];
    }

    /**
     * Open site (row, col) if it is not open already
     * @param row the row number
     * @param col the col number
     */
    public void open(int row, int col) {
        checkArguments(row, col);

        if (!isOpen(row, col)) {
            int idx = arrayIndex(row, col);

            // Connect left
            if (col > 1 && isOpen(row, col - 1)) {
                int leftIdx = idx - 1;
                this.ufPercolation.union(idx, leftIdx);
                this.ufFullness.union(idx, leftIdx);
            }
            // Connect left
            if (col < this.size && isOpen(row, col + 1)) {
                int rightIdx = idx + 1;
                this.ufPercolation.union(idx, rightIdx);
                this.ufFullness.union(idx, rightIdx);
            }
            // Connect top
            if (row > 1 && isOpen(row - 1, col)) {
                int topIdx = idx - this.size;
                this.ufPercolation.union(idx, topIdx);
                this.ufFullness.union(idx, topIdx);
            }
            // Connect bottom
            if (row < this.size && isOpen(row + 1, col)) {
                int bottomIdx = idx + this.size;
                this.ufPercolation.union(idx, bottomIdx);
                this.ufFullness.union(idx, bottomIdx);
            }

            if (row == 1) {
                this.ufPercolation.union(this.inputIdx, idx);
                this.ufFullness.union(this.inputIdx, idx);
            }
            if (row == this.size) {
                this.ufPercolation.union(this.outputIdx, idx);
            }

            this.openState[idx] = true;
            this.openCount++;
        }
    }

    /**
     * Is site (row, col) open?
     * @param row the row number
     * @param col the col number
     * @return
     */
    public boolean isOpen(int row, int col) {
        checkArguments(row, col);

        int idx = arrayIndex(row, col);
        return this.openState[idx];
    }

    /**
     * Is site (row, col) full?
     * @param row the row number
     * @param col the col number
     * @return
     */
    public boolean isFull(int row, int col) {
        checkArguments(row, col);

        int idx = arrayIndex(row, col);
        return this.ufFullness.connected(this.inputIdx, idx);
    }

    /**
     * Returns the number of open sites
     * @return
     */
    public int numberOfOpenSites() {
        return this.openCount;
    }

    /**
     * Returns true if the system percolates
     * @return
     */
    public boolean percolates() {
        return this.ufPercolation.connected(this.inputIdx, this.outputIdx);
    }

    private void checkArguments(int row, int col) {
        if (row < 1 || row > this.size || col < 1 || col > this.size) {
            throw new IllegalArgumentException("Row or column index is out of bounds!");
        }
    }

    /**
     * Translate the index of the two-dimensional array element to the one-dimensional array index
     * @param row the row number
     * @param col the col number
     * @return
     */
    private int arrayIndex(int row, int col) {
        return this.size * (row - 1) + col - 1;
    }

    public static void main(String[] args) {
        // 1. Test arrayIndex() method
        Percolation p = new Percolation(3);
        assert p.openState.length == 9;
        assert p.arrayIndex(1, 1) == 0;
        assert p.arrayIndex(3, 3) == 8;
        assert p.arrayIndex(1, 3) == 2;

        // 2. Test open() method
        assert !p.isOpen(1, 1);
        assert p.numberOfOpenSites() == 0;

        p.open(1, 1);
        assert !p.isOpen(1, 1);
        assert p.numberOfOpenSites() == 1;

        p.open(3, 3);
        assert !p.isOpen(3, 3);
        assert p.numberOfOpenSites() == 2;
    }

}