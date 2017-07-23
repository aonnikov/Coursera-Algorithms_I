import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node head;
    private Node tail;
    private int size;

    /**
     * Construct an empty Deque
     */
    public Deque() {
        this.size = 0;
    }

    /**
     * Is the deque is empty?
     * @return
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Return the size of the deque
     * @return
     */
    public int size() {
        return this.size;
    }

    /**
     * Add the item to the front
     * @param item the Item
     */
    public void addFirst(Item item) {
        checkItemNotNull(item);

        Node node = new Node(null, item, this.head);
        if (isEmpty()) {
            this.head = node;
            this.tail = node;
        }
        else {
            this.head.prev = node;
            this.head = node;
        }
        this.size++;
    }

    /**
     * Add the item to the end
     * @param item the Item
     */
    public void addLast(Item item) {
        checkItemNotNull(item);

        Node node = new Node(this.tail, item, null);
        if (isEmpty()) {
            this.head = node;
            this.tail = node;
        }
        else {
            this.tail.next = node;
            this.tail = node;
        }

        this.size++;
    }

    /**
     * Remove and return the item from the front
     * @return the removed item
     */
    public Item removeFirst() {
        checkNotEmpty();

        Node removed = this.head;
        this.head = removed.next;
        if (this.head == null) {
            this.tail = null;
        }
        else {
            this.head.prev = null;
        }
        this.size--;

        return removed.item;
    }

    /**
     * Remove and return the item from the end
     * @return the removed item
     */
    public Item removeLast() {
        checkNotEmpty();

        Node removed = this.tail;
        this.tail = removed.prev;
        if (this.tail == null) {
            this.head = null;
        }
        else {
            this.tail.next = null;
        }
        this.size--;

        return removed.item;
    }

    /**
     * Construct new {@link Iterator} over the deque
     * @return
     */
    public Iterator<Item> iterator() {
        return new DequeIterator(this.head);
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

    private class DequeIterator implements Iterator<Item> {

        private Node nextNode;

        private DequeIterator(Node nextNode) {
            this.nextNode = nextNode;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public Item next()  {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node node = this.nextNode;
            this.nextNode = this.nextNode.next;
            return node.item;
        }
    }

    private class Node {

        private final Item item;
        private Node next;
        private Node prev;

        Node(Node prev, Item item, Node next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        assert deque.size() == 0;
        assert deque.isEmpty();
        deque.addFirst("Hello");
        deque.addLast("stupid");
        deque.addLast("world");
        assert deque.size() == 3;
        assert !deque.isEmpty();

        Iterator<String> iterator = deque.iterator();
        assert iterator.hasNext();
        String next = iterator.next();
        assert "Hello".equals(next);

        assert iterator.hasNext();
        next = iterator.next();
        assert "stupid".equals(next);

        assert iterator.hasNext();
        next = iterator.next();
        assert "world".equals(next);

        assert !iterator.hasNext();

        next = deque.removeFirst();
        assert "Hello".equals(next);
        next = deque.removeLast();
        assert "world".equals(next);
        next = deque.removeFirst();
        assert "stupid".equals(next);

        assert deque.size() == 0;
        assert deque.isEmpty();
    }
}