package com.mcintyret.utils.filter;

/**
 * User: mcintyret2
 * Date: 31/07/2013
 */
public class ComparableFilter<C extends Comparable<C>> extends AbstractFilter<C> {

    private C min;

    private boolean minInclusive;

    private C max;

    private boolean maxInclusive;

    public ComparableFilter(C min, boolean minInclusive, C max, boolean maxInclusive) {
        this.min = min;
        this.minInclusive = minInclusive;
        this.max = max;
        this.maxInclusive = maxInclusive;
    }

    @Override
    public boolean apply(C c) {
        if (min != null) {
            int comp = min.compareTo(c);
            if (comp > 0 || (!minInclusive && comp == 0)) {
                return false;
            }
        }
        if (max != null) {
            int comp = max.compareTo(c);
            if (comp < 0 || (!maxInclusive && comp == 0)) {
                return false;
            }
        }
        return true;
    }

    public void setMin(C min) {
        int comp = min.compareTo(this.min);
        if (comp > 0) {
            // the new min is smaller
            expand();
        } else if (comp < 0) {
            restrict();
        }
        this.min = min;
    }

    public void setMinInclusive(boolean minInclusive) {
        changeInclusive(this.minInclusive, minInclusive);
        this.minInclusive = minInclusive;
    }

    public void setMax(C max) {
        int comp = max.compareTo(this.max);
        if (comp > 0) {
            // the new max is smaller
            restrict();
        } else if (comp < 0) {
            expand();
        }
        this.max = max;
    }

    public void setMaxInclusive(boolean maxInclusive) {
        changeInclusive(this.maxInclusive, maxInclusive);
        this.maxInclusive = maxInclusive;
    }

    private void changeInclusive(boolean old, boolean noo) {
        if (old != noo) {
            if (noo) {
                expand();
            } else {
                restrict();
            }
        }
    }
}