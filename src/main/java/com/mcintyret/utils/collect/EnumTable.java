package com.mcintyret.utils.collect;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * User: mcintyret2
 * Date: 12/08/2013
 */
public class EnumTable<R extends Enum<R>, C extends Enum<C>, V> {

    private final Map<R, Map<C, V>> table;

    public EnumTable(Class<R> rowClass, Class<C> columnClass) {
        table = new EnumMap<>(rowClass);
        for (R r : rowClass.getEnumConstants()) {
            table.put(r, new EnumMap<C, V>(columnClass));
        }
    }

    public V put(R row, C col, V val) {
        return table.get(row).put(col, val);
    }

    public V get(R row, C col) {
        return table.get(row).get(col);
    }

    public boolean contains(R row, C col) {
        return get(row, col) != null;
    }

    public V remove(R row, C col) {
        return table.get(row).remove(col);
    }

    public void clear() {
        for (Map<C, V> col : table.values()) {
            col.clear();
        }
    }

    public Collection<V> getRow(R row) {
        return table.get(row).values();
    }

}