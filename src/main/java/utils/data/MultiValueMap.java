package utils.data;

import java.util.Collection;

public interface MultiValueMap<K, V1, V2> {

    void put(K key, V1 first, V2 second);

    Collection<Tuple<V1, V2>> getValues();

    int size();
}