package com.mcintyret.utils.collect;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import static com.google.common.collect.Iterators.emptyIterator;
import static com.google.common.collect.Iterators.singletonIterator;

/**
 * User: mcintyret2
 * Date: 09/04/2013
 */
class ReverseTrieIterator<T> extends AbstractIterator<Map.Entry<String, T>> {

    private final AbstractTrie<T> trie;

    private final Stack<Iterator<CharacterAndNode<T>>> iteratorStack = new Stack<>();
    private Iterator<CharacterAndNode<T>> iterator;
    private final Stack<CharacterAndNode<T>> valueStack = new Stack<>();

    ReverseTrieIterator(AbstractTrie<T> trie) {
        this(trie, "", true);
    }

    ReverseTrieIterator(AbstractTrie<T> trie, String prefix, boolean subtrie) {
        this(trie, trie.getNode(prefix, false), prefix, subtrie);
    }

    ReverseTrieIterator(AbstractTrie<T> trie, TrieNode<T> baseNode, String prefix, boolean subtrie) {
        this.trie = trie;
        key = Arrays.copyOf(prefix.toCharArray(), 10);
        if (subtrie) {
            index = prefix.length();
            iterator = baseNode == null ? Iterators.<CharacterAndNode<T>>emptyIterator() :
                    singletonIterator(new CharacterAndNode<>('\0', baseNode));
        } else {
            TrieNode<T> node = trie.getRoot();
            for (int i = 0; i < prefix.length(); i++) {
                char c = prefix.charAt(i);
                PeekingIterator<CharacterAndNode<T>> it = node.reverseIterator(c);
                if (it.hasNext() && it.peek().getChar() == c) {
                    index++;
                    node = it.next().getNode();
                    valueStack.push(new CharacterAndNode<>(c, node));
                    iteratorStack.push(it);
                } else {
                    break;
                }
            }
            iterator = emptyIterator();
        }
    }

    private char[] key = new char[10];
    private int index = 0;

    @Override
    protected Map.Entry<String, T> computeNext() {
        while (iterator.hasNext()) {
            final CharacterAndNode<T> charAndNode = iterator.next();
            Iterator<CharacterAndNode<T>> it = charAndNode.getNode().reverseIterator();
            updateKey(charAndNode.getChar());
            if (it.hasNext()) {
                iteratorStack.push(iterator);
                valueStack.push(charAndNode);
                iterator = it;
                return computeNext();
            } else {
                index--;
                if (charAndNode.getNode().getValue() != null) {
                    // This is an actual entry of interest
                    return MapUtils.trieNodeEntry(charAndNode.getNode(), new String(key, 0, index + 1));
                }
            }
        }
        if (iteratorStack.isEmpty()) {
            return endOfData();
        } else {
            index--;
            iterator = iteratorStack.pop();
            CharacterAndNode<T> popped = valueStack.pop();
            if (popped.getNode().getValue() != null) {
                return MapUtils.trieNodeEntry(popped.getNode(), new String(key, 0, index + 1));
            } else {
                return computeNext();
            }
        }
    }

    private void updateKey(char c) {
        if (c != '\0') {
            if (index == key.length) {
                key = Arrays.copyOf(key, key.length * 2);
            }
            key[index++] = c;
        }
    }


    @Override
    protected void doRemove(Map.Entry<String, T> removed) {
        trie.remove(removed.getKey());
    }

}
