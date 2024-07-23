package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    private int bucketSize;

    private int size;

    private double loadFactor;

    /** Constructors */
    public MyHashMap() {
        size = 0;
        bucketSize = 16;
        loadFactor = 0.75;
        buckets = new Collection[bucketSize];
    }

    public MyHashMap(int initialSize) {
        size = 0;
        bucketSize = initialSize;
        loadFactor = 0.75;
        buckets = new Collection[bucketSize];
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        size = 0;
        bucketSize = initialSize;
        loadFactor = maxLoad;

        buckets = new Collection[bucketSize];
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return null;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    @Override
    public void clear() {
        size = 0;
        bucketSize = 16;
        buckets = new Collection[bucketSize];
    }

    @Override
    public boolean containsKey(K key) {
        if (size == 0) {
            return false;
        }
        int hash = key.hashCode();
        int index = Math.floorMod(hash, bucketSize);
        if (buckets[index] == null) {
            return false;
        }
        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        if (size == 0) {
            return null;
        }
        int hash = key.hashCode();
        int index = Math.floorMod(hash, bucketSize);
        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (!containsKey(key)) {
            size += 1;
            if ((double) size / bucketSize > loadFactor) {
                resize();
            }
            int hash = key.hashCode();
            int index = Math.floorMod(hash, bucketSize);
            if (buckets[index] == null) {
                buckets[index] = createBucket();
            }
            buckets[index].add(createNode(key, value));
        } else {
            int hash = key.hashCode();
            int index = Math.floorMod(hash, bucketSize);
            for (Node node : buckets[index]) {
                if (node.key.equals(key)) {
                    node.value = value;
                    return;
                }
            }
        }
    }

    private void resize() {
        bucketSize *= 2;
        Collection<Node>[] tempBuckets = new Collection[bucketSize];
        for (int i = 0; i < bucketSize / 2; i++) {
            if (buckets[i] != null) {
                for (Node node : buckets[i]) {
                    int hash = node.key.hashCode();
                    int index = Math.floorMod(hash, bucketSize);
                    if (tempBuckets[index] == null) {
                        tempBuckets[index] = createBucket();
                    }
                    tempBuckets[index].add(node);
                }
            }
        }
        buckets = tempBuckets;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (int i = 0; i < bucketSize; i++) {
            if (buckets[i] != null) {
                for (Node node : buckets[i]) {
                    keySet.add(node.key);
                }
            }
        }
        return keySet;
    }

    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            return null;
        }
        int hash = key.hashCode();
        int index = Math.floorMod(hash, bucketSize);
        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                V returnValue = node.value;
                buckets[index].remove(node);
                return returnValue;
            }
        }
        return null;
    }

    @Override
    public V remove(K key, V value) {
        if (!containsKey(key)) {
            return null;
        }
        int hash = key.hashCode();
        int index = Math.floorMod(hash, bucketSize);
        for (Node node : buckets[index]) {
            if (node.key.equals(key) && node.value == value) {
                V returnValue = node.value;
                buckets[index].remove(node);
                return returnValue;
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

}
