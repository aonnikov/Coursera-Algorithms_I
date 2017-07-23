import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int size;

    private final int trials;

    private final double[] percolationPercents;

    private double mean;

    private double stddev;

    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("Grid size must be greater than 0!");
        }

        if (trials <= 0) {
            throw new IllegalArgumentException("Grid size must be greater than 0!");
        }

        this.size = n;
        this.trials = trials;
        this.percolationPercents = new double[trials];

        run();
    }

    private void run() {
        for (int trial = 0; trial < trials; trial++) {
            Percolation p = new Percolation(size);
            do {
                int row = StdRandom.uniform(size) + 1;
                int col = StdRandom.uniform(size) + 1;
                p.open(row, col);
            } while (!p.percolates());
            this.percolationPercents[trial] = p.numberOfOpenSites() * 1.0 / (this.size * size);
        }

        this.mean = StdStats.mean(this.percolationPercents);
        if (this.size == 1) {
            this.stddev = Double.NaN;
        }
        else {
            this.stddev = StdStats.stddev(this.percolationPercents);
        }
    }

    /**
     * Returns the sample mean of percolation threshold
     * @return
     */
    public double mean() {
        return this.mean;
    }

    /**
     * Returns the sample standard deviation of percolation threshold
     * @return
     */
    public double stddev() {
        return this.stddev;
    }

    /**
     * Returns the low endpoint of 95% confidence interval
     * @return
     */
    public double confidenceLo() {
        return mean - 1.96 * stddev / Math.sqrt(this.trials);
    }

    /**
     * Returns the high endpoint of 95% confidence interval
     * @return
     */
    public double confidenceHi() {
        return mean + 1.96 * stddev / Math.sqrt(this.trials);
    }

    /**
     * The test client implementation
     * @param args the arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Wrong arguments count!");
        }

        int size = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(size, trials);
        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " +
                stats.confidenceHi() + "]");
    }
}