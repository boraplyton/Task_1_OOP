import java.lang.reflect.Array;
import java.util.*;

public class SimpleHashMap<K, V> implements DefaultNotSupportedMap<K, V> {

    private static class HashNode<K, V> implements Map.Entry<K, V> {
        final K key;
        V value;
        HashNode<K, V> next;

        public HashNode(K key, V value, HashNode<K, V> next ) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public final K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }

    protected HashNode<K, V>[] table;
    protected int size = 0;

    public SimpleHashMap(int capacity) {
        table = (HashNode<K, V>[])  Array.newInstance(HashNode.class, capacity);
    }

    private int getIndex(Object key) {
        int index = key.hashCode() % table.length;
        if (index < 0) {
            index += table.length;
        }
        return index;
    }
    private HashNode<K, V> getEntry(Object key, int index) {
        if (index < 0) {
            index = getIndex(key);
        }
        for (HashNode<K, V> curr = table[index]; curr != null; curr = curr.next) {
            if (key.equals(curr.key)) {
                return curr;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() <= 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getEntry(key, -1) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        HashNode<K, V>[] t;
        V v;
        if ((t = table) != null && size > 0) {
            for (HashNode<K,V> e : t) {
                for (; e != null; e = e.next) {
                    if ((v = e.value) == value || (value != null && value.equals(v)))
                        return true;
                }

            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        HashNode<K, V> item = getEntry(key, -1);
        return (item == null) ? null : item.value;
    }

    @Override
    public V put(K key, V value) {
        int index = getIndex(key);
        HashNode<K, V> item = getEntry(key, index);
        if (item != null) {
            V oldValue = item.value;
            item.value = value;
            return oldValue;
        }
        table[index] = new HashNode<K, V>(key, value, table[index]);
        size++;
        return null;
    }

    @Override
    public V remove(Object key) {
        int index = getIndex(key);
        HashNode<K, V> parent = null;
        for (HashNode<K, V> curr = table[index]; curr != null; curr = curr.next) {
            if (key.equals(curr.key)) {
                if (parent == null) {
                    table[index] = curr.next;
                } else {
                    parent.next = curr.next;
                }
                size--;
                return curr.value;
            }
            parent = curr;
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            K key = e.getKey();
            V value = e.getValue();
            this.put(key, value);
        }
    }
    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }
    @Override
    public Set<K> keySet() {
        Set<K> result = new HashSet<>();
        HashNode<K, V>[] t;

        if ((t = table) != null && size > 0) {
            for (HashNode<K,V> e : t) {
                for (; e != null; e = e.next) {
                   result.add(e.key);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<V> values() {
        Set<V> result = new HashSet<>();
        HashNode<K, V>[] t;

        if ((t = table) != null && size > 0) {
            for (HashNode<K,V> e : t) {
                for (; e != null; e = e.next) {
                    result.add(e.value);
                }
            }
        }
        return result;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new HashSet<Map.Entry<K, V>>() {
            @Override
            public int size() {
                return SimpleHashMap.this.size();
            }

            @Override
            public Iterator<Map.Entry<K, V>> iterator() {
                return new Iterator<Map.Entry<K, V>>() {
                    int tableIndex = -1;
                    HashNode<K, V> curr = null;

                    {
                        findNext();
                    }

                    private void findNext() {
                        if (tableIndex >= table.length) {
                            return;
                        }
                        if (curr != null) {
                            curr = curr.next;
                        }
                        if (curr == null) {
                            for (tableIndex = tableIndex + 1; tableIndex < table.length; tableIndex++) {
                                curr = table[tableIndex];
                                if (curr != null) {
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public boolean hasNext() {
                        return curr != null;
                    }

                    @Override
                    public Map.Entry<K, V> next() {
                        Map.Entry<K, V> temp = curr;
                        findNext();
                        return temp;
                    }
                };
            }
        };
    }
}



