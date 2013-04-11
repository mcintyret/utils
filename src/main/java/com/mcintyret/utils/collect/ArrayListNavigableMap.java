//package com.mcintyret.utils.collect;
//
//import java.util.*;
//
///**
// * User: mcintyret2
// * Date: 10/04/2013
// */
//public class ArrayListNavigableMap<V> extends AbstractNavigableMap<Integer, V> {
//
//    private final ArrayList<V> list;
//
//    public ArrayListNavigableMap(ArrayList<V> list) {
//        this.list = list;
//    }
//
//    @Override
//    public V get(Object key) {
//        return list.get((Integer) key);
//    }
//
//    @Override
//    Iterator<Entry<Integer, V>> entryIterator() {
//        return new Iterator<Entry<Integer, V>>() {
//
//            ListIterator<V> it = list.listIterator();
//
//            @Override
//            public boolean hasNext() {
//                return it.hasNext();
//            }
//
//            @Override
//            public Entry<Integer, V> next() {
//                return new Entry<Integer, V>() {
//                    private final int key = it.nextIndex();
//                    private V val = it.next();
//
//                    @Override
//                    public Integer getKey() {
//                        return key;
//                    }
//
//                    @Override
//                    public V getValue() {
//                        return val;
//                    }
//
//                    @Override
//                    public V setValue(V value) {
//                        return null;  //To change body of implemented methods use File | Settings | File Templates.
//                    }
//                }
//            }
//
//            @Override
//            public void remove() {
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        }
//    }
//
//    @Override
//    Iterator<Entry<Integer, V>> descendingEntryIterator() {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public NavigableMap<Integer, V> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public Comparator<? super Integer> comparator() {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//}
