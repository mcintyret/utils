package com.mcintyret.utils.collect;

/**
* User: mcintyret2
* Date: 04/04/2013
*/
final class CharacterAndNode<V> {

    private final char c;
    private final TrieNode<V> node;

    public CharacterAndNode(char c, TrieNode<V> node) {
        this.c = c;
        this.node = node;
    }

    public char getChar() {
        return c;
    }

    public TrieNode<V> getNode() {
        return node;
    }

    @Override
    public String toString() {
        return c + "->" + String.valueOf(node.getValue());
    }
}
