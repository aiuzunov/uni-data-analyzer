package utils.data.impl;

import utils.data.MultiValueMap;
import utils.data.Tuple;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MultiValueMapImpl<K, V1, V2> implements MultiValueMap<K, V1, V2> {

    private final Map<K, Tuple<V1, V2>> values;

    public MultiValueMapImpl() {
        values = new HashMap<>();
    }

    public void put(K key, V1 first, V2 second) {
        values.put(key, new TupleImpl<>(first, second));
    }

    public Collection<Tuple<V1, V2>> getValues() {
        return values.values();
    }

    public int size() {
        return values.size();
    }
}
