import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size = 0;

    private int capacity = 1;

    private Object[] items;

    private int head = 0;

    private int tail = 0;

    /**
     * Construct an empty randomized queue
     */
    public RandomizedQueue() {
        items = new Object[capacity];
    }

    /**
     * Return true if the queue is empty
     * @return true if the queue is empty
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Return the number of items on the queue
     * @return the number of items on the queue
     */
    public int size() {
        return this.size;
    }

    /**
     * Add the item
     * @param item the Item
     */
    public void enqueue(Item item) {
        checkItemNotNull(item);

        // Grow the array when it is full
        if (this.size == this.capacity) {
            resize(2 * this.capacity);
        }

        this.items[this.tail % this.capacity] = item;
        this.tail++;
        this.size++;
    }

    /**
     * Remove and return a random item
     * @return
     */
    public Item dequeue() {
        checkNotEmpty();

        int index = randomItemIndex() % this.capacity;
        Item item = (Item) this.items[index];

        // Swap selected item and hea item
        int headIndex = this.head % this.capacity;
        this.items[index] = this.items[headIndex];
        this.items[headIndex] = null;

        this.head++;
        this.size--;

        // Shrink the array when it is one-quarter full
        if (this.size > 0 && this.size == this.capacity / 4) {
            resize(this.capacity / 2);
        }

        return item;
    }

    /**
     * Return (but do not remove) a random item
     * @return
     */
    public Item sample() {
        checkNotEmpty();

        int index = randomItemIndex();
        return (Item) this.items[index % this.capacity];
    }

    /**
     * Returns the random item index between head and tail
     * @return
     */
    private int randomItemIndex() {
        return StdRandom.uniform(this.head, this.tail);
    }

    private void resize(int newCapacity) {
        Object[] newItems = new Object[newCapacity];
        for (int i = 0; i < this.size; i++) {
            newItems[i] = this.items[(this.head + i) % this.capacity];
        }

        this.capacity = newCapacity;
        this.items = newItems;
        this.head = 0;
        this.tail = this.size;
    }

    /**
     * Return an independent iterator over items in random order
     * @return
     */
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private void checkItemNotNull(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
    }

    private void checkNotEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private final Object[] items;
        private final int size;
        private int index;

        public RandomizedQueueIterator() {
            this.size = RandomizedQueue.this.size;
            this.items = new Object[this.size];
            this.index = 0;

            int[] permutation = StdRandom.permutation(this.size);
            for (int i = 0; i < this.size; i++) {
                this.items[i] = RandomizedQueue.this.items[(head + permutation[i]) % capacity];
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return this.index < this.size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = (Item) this.items[this.index];
            this.index++;
            return item;
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        assert queue.isEmpty();
        assert queue.size() == 0;
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        assert !queue.isEmpty();
        assert queue.size() == 3;

        for (int i = 0; i < queue.size(); i++) {
            System.out.println("Sample: " + queue.sample());
        }

        Iterator<String> iterator = queue.iterator();
        assert iterator.hasNext();
        System.out.println("Next: " + iterator.next());
        assert iterator.hasNext();
        System.out.println("Next: " + iterator.next());
        assert iterator.hasNext();
        System.out.println("Next: " + iterator.next());
        assert !iterator.hasNext();

        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
        assert queue.isEmpty();
        assert queue.size == 0;

        queue.enqueue("40");
        System.out.println(queue.dequeue());
        assert !queue.isEmpty();
        queue.enqueue("335");
        System.out.println(queue.dequeue());
        assert !queue.isEmpty();
    }
}