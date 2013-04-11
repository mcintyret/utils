//package com.mcintyret.utils.collect;
//
//import com.google.common.base.Function;
//import com.google.common.collect.Iterators;
//import com.google.common.collect.PeekingIterator;
//
//import java.util.Iterator;
//import java.util.Map;
//import java.util.NavigableMap;
//
///**
//* User: mcintyret2
//* Date: 04/04/2013
//*/
//class MapTrieNode<V> extends AbstractTrieNode<V> {
//
//    private final NavigableMap<Character, TrieNode<V>> children;
//
//    MapTrieNode(NavigableMap<Character, TrieNode<V>> children, V val) {
//        super(val);
//        this.children = children;
//    }
//
//    @Override
//    public TrieNode<V> getChild(char c) {
//        return children.get(c);
//    }
//
//    @Override
//    public void setChild(char c, TrieNode<V> child) {
//        children.put(c, child);
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return children.isEmpty();
//    }
//
//    @Override
//    public void clear() {
//        children.clear();
//    }
//
//    @Override
//    public PeekingIterator<CharacterAndNode<V>> iterator() {
//        return Iterators.transform(children.entrySet().iterator(), new Function<Map.Entry<Character, TrieNode<V>>, CharacterAndNode<V>>() {
//            @Override
//            public CharacterAndNode<V> apply(Map.Entry<Character, TrieNode<V>> input) {
//                return new CharacterAndNode<>(input.getKey(), input.getValue());
//            }
//        });
//    }
//
//    @Override
//    public Iterator<CharacterAndNode<V>> iterator(char c) {
//        return Iterators.transform(children.tailMap(c).entrySet().iterator(), new Function<Map.Entry<Character, TrieNode<V>>, CharacterAndNode<V>>() {
//            @Override
//            public CharacterAndNode<V> apply(Map.Entry<Character, TrieNode<V>> input) {
//                return new CharacterAndNode<>(input.getKey(), input.getValue());
//            }
//        });
//    }
//
//    @Override
//    public Iterator<CharacterAndNode<V>> reverseIterator() {
//        return Iterators.transform(children.descendingMap().entrySet().iterator(), new Function<Map.Entry<Character, TrieNode<V>>, CharacterAndNode<V>>() {
//            @Override
//            public CharacterAndNode<V> apply(Map.Entry<Character, TrieNode<V>> input) {
//                return new CharacterAndNode<>(input.getKey(), input.getValue());
//            }
//        });
//    }
//
//    @Override
//    public Iterator<CharacterAndNode<V>> reverseIterator(char c) {
//        return Iterators.transform(children.descendingMap().tailMap(c).entrySet().iterator(), new Function<Map.Entry<Character, TrieNode<V>>, CharacterAndNode<V>>() {
//            @Override
//            public CharacterAndNode<V> apply(Map.Entry<Character, TrieNode<V>> input) {
//                return new CharacterAndNode<>(input.getKey(), input.getValue());
//            }
//        });
//    }
//}
