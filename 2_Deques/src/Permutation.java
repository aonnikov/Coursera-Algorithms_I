import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Wrong arguments count!");
        }

        RandomizedQueue<String> queue = new RandomizedQueue<>();

        final int k = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }

        Iterator<String> iterator = queue.iterator();
        for (int i = 0; i < k; i++) {
            StdOut.println(iterator.next());
        }
    }
}
