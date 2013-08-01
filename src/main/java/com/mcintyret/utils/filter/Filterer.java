package com.mcintyret.utils.filter;

import com.google.common.collect.AbstractIterator;

import java.util.AbstractCollection;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;

/**
 * User: mcintyret2
 * Date: 31/07/2013
 */
public class Filterer<T> {

    private final Collection<? extends T> collection;

    private final Filter<T>[] filters;

    private final BitSet[] bitSets;

    private int filteredOut;

    public Filterer(Collection<? extends T> collection, Filter<T>... filters) {
        this.collection = collection;
        this.filters = filters;
        this.bitSets = new BitSet[collection.size()];
        filteredOut = collection.size();
        initBitSets();
    }

    private void initBitSets() {
        for (int i = 0; i < bitSets.length; i++) {
            bitSets[i] = new BitSet(filters.length);
        }
    }

    private boolean isIncluded(int index) {
        return bitSets[index].cardinality() == filters.length;
    }

    public Collection<T> filter() {
        for (int i = 0; i < filters.length; i++) {
            Filter<T> filter = filters[i];
            int item = 0;
            for (T t : collection) {
                boolean wasIncluded = i == filters.length - 1 && isIncluded(item);
                BitSet bitSet = bitSets[item++];
                switch (filter.getState()) {
                    case NO_CHANGE:
                        break;
                    case EXPANDED:
                        if (!bitSet.get(i) && filter.apply(t)) {
                            bitSet.set(i);
                        }
                        break;
                    case RESTRICTED:
                        if (bitSet.get(i) && !filter.apply(t)) {
                            bitSet.set(i, false);
                        }
                        break;
                    case UNKNOWN:
                        bitSet.set(i, filter.apply(t));
                        break;
                    default:
                        throw new AssertionError();
                }
                boolean included = i == filters.length - 1 && isIncluded(item - 1);
                if (included != wasIncluded) {
                    filteredOut += included ? -1 : 1;
                }
            }
            filter.clearState();
        }
        return collectionView;
    }

    private final Collection<T> collectionView = new AbstractCollection<T>() {
        @Override
        public Iterator<T> iterator() {
            return new AbstractIterator<T>() {

                private Iterator<? extends T> iterator = collection.iterator();
                private int index = 0;

                @Override
                protected T computeNext() {
                    while (iterator.hasNext()) {
                        T t = iterator.next();
                        if (bitSets[index++].cardinality() == filters.length) {
                            return t;
                        }
                    }
                    return endOfData();
                }
            };
        }

        @Override
        public int size() {
            return collection.size() - filteredOut;
        }
    };

}
