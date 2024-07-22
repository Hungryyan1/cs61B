package bstmap;


import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B<K, V> {

    private class BSTNode {
        private K key;
        private V value;
        private BSTNode left, right;
    }

    private BSTNode root;

    private int size;

    public BSTMap() {
        root = null;
        size = 0;
    }
    /* Set size = 0, root = null
     **/
    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }

        BSTNode node = root;
        while (node != null) {
            if (key.equals(node.key)) {
                return true;
            } else if (key.compareTo(node.key) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return false;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (root == null) {
            return null;
        }

        BSTNode node = root;
        while (node != null) {
            if (key.equals(node.key)) {
                return node.value;
            } else if (key.compareTo(node.key) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    /* Associates the specified value with the specified key in this map.
    *  If the key already exists, reassign the value.
    **/
    @Override
    public void put(K key, V value) {
        size += 1;git 
        if (root == null) {
            root = new BSTNode();
            root.key = key;
            root.value = value;
            return;
        }
        BSTNode node = root;
        while (true) {
            if (key.equals(node.key)) {
                node.value = value;
                return;
            } else if (key.compareTo(node.key) < 0) {
                if (node.left == null) {
                    node.left = new BSTNode();
                    node.left.key = key;
                    node.left.value = value;
                    return;
                } else {
                    node = node.left;
                }
            } else {
                if (node.right == null) {
                    node.right = new BSTNode();
                    node.right.key = key;
                    node.right.value = value;
                    return;
                } else {
                    node = node.right;
                }
            }
        }

    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

}
