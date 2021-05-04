package utils.data.impl;

import utils.data.Tuple;

public class TupleImpl<F, S> implements Tuple<F, S> {

    private final F first;
    private final S second;

    public TupleImpl(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public F getFirst() {
        return first;
    }

    @Override
    public S getSecond() {
        return second;
    }
}
