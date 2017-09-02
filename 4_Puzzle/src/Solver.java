import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

/**
 * Created by aonnikov on 03/08/2017.
 */
public class Solver {

    private final Board initial;
    private boolean solvable = false;
    private Stack<Board> moves;

    /**
     * Find a solution to the initial board (using the A* algorithm)
     * @param initial
     */
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Initial board is null!");
        }

        this.initial = initial;
        solve();
    }

    private void solve() {
        AStarBoardSolver mainSolver = new AStarBoardSolver(this.initial, new ManhattanOrder());
        AStarBoardSolver twinSolver = new AStarBoardSolver(this.initial.twin(), new ManhattanOrder());

        while (true) {
            boolean mainGoal = mainSolver.doStep();
            boolean twinGoal = twinSolver.doStep();

            if (mainGoal) {
                this.moves = mainSolver.moves();
                this.solvable = true;
                break;
            }
            else if (twinGoal) {
                this.solvable = false;
                break;
            }
        }
    }

    /**
     * Is the initial board solvable?
     * @return true if the initial board is solvable
     */
    public boolean isSolvable() {
        return this.solvable;
    }

    /**
     * Return min number of moves to solve initial board; -1 if unsolvable
     * @return min number of moves to solve initial board
     */
    public int moves() {
        if (isSolvable()) {
            return this.moves.size() - 1;
        }
        return -1;
    }

    /**
     * Return sequence of boards in a shortest solution; null if unsolvable
     * @return sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        if (isSolvable()) {
            return this.moves;
        }
        return null;
    }

    private static class SearchNode {

        private final Board board;

        private final int step;

        private final SearchNode parent;

        public SearchNode(Board board, int step, SearchNode parent) {
            this.board = board;
            this.step = step;
            this.parent = parent;
        }

        public SearchNode next(Board next) {
            return new SearchNode(next, this.step + 1, this);
        }

        public Board board() {
            return this.board;
        }

        public SearchNode parent() {
            return this.parent;
        }

        public int hammingPriority() {
            return this.board.hamming() + this.step;
        }

        public int manhattanPriority() {
            return this.board.manhattan() + this.step;
        }

    }

    private static class AStarBoardSolver {

        private final MinPQ<SearchNode> queue;

        private SearchNode lastNode;

        public AStarBoardSolver(Board initial, Comparator<SearchNode> minFunction) {

            SearchNode node = new SearchNode(initial, 0, null);
            this.queue = new MinPQ<>(1, minFunction);

            this.queue.insert(node);
        }

        public boolean doStep() {
            if (this.queue.isEmpty()) {
                throw new IllegalStateException();
            }

            lastNode = queue.delMin();
            Board board = lastNode.board();

            if (board.isGoal()) {
                return true;
            }
            else {
                // Add neighbors to the queue
                for (Board neighbor : board.neighbors()) {
                    boolean visited = isVisited(lastNode, neighbor);
                    if (!visited) {
                        queue.insert(lastNode.next(neighbor));
                    }
                }
            }

            return false;
        }

        public Stack<Board> moves() {
            Stack<Board> result = new Stack<>();

            SearchNode node = this.lastNode;
            while (node != null) {
                result.push(node.board());
                node = node.parent();
            }

            return result;
        }

        /**
         * Return true if the candidate board has been already visited
         * @param parent the parent node
         * @param candidate the candidate board to check
         * @return has the board been already visited?
         */
        private boolean isVisited(SearchNode parent, Board candidate) {
            if (parent != null && parent.parent() != null) {
                return parent.parent().board().equals(candidate);
            }
            return false;
        }

    }

    /**
     * Comparator which compares two {@link SearchNode} by their hamming priority.
     */
    private class HammingOrder implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode b1, SearchNode b2) {
            return Integer.compare(b1.hammingPriority(), b2.hammingPriority());
        }
    }

    /**
     * Comparator which compares two {@link Board} by their manhattan priority.
     */
    private class ManhattanOrder implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode b1, SearchNode b2) {
            return Integer.compare(b1.manhattanPriority(), b2.manhattanPriority());
        }
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }

        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        }
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}