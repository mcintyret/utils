package com.mcintyret.utils.collect;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import static com.google.common.collect.Iterators.singletonIterator;

/**
* User: mcintyret2
* Date: 09/04/2013
*/
class LexicographicTrieIterator<T> extends AbstractIterator<Map.Entry<String, T>> {

    private final Stack<Iterator<CharacterAndNode<T>>> iteratorStack = new Stack<>();
    private Iterator<CharacterAndNode<T>> iterator;
    private final AbstractTrie<T> trie;

    LexicographicTrieIterator(AbstractTrie<T> trie) {
        this(trie, "", true);
    }

    LexicographicTrieIterator(AbstractTrie<T> trie, String prefix, boolean subtrie) {
        this(trie, trie.getNode(prefix, false), prefix, subtrie);
    }

    LexicographicTrieIterator(AbstractTrie<T> trie, TrieNode<T> baseNode, String prefix, boolean subtrie) {
        this.trie = trie;
        key = Arrays.copyOf(prefix.toCharArray(), 10);
        if (subtrie) {
            index = prefix.length();
            iterator = baseNode == null ? Iterators.<CharacterAndNode<T>>emptyIterator() :
                    singletonIterator(new CharacterAndNode<>('\0', baseNode));
        } else {
            TrieNode<T> node = trie.getRoot();
            for (int i = 0; i < prefix.length(); i++) {
                PeekingIterator<CharacterAndNode<T>> it = node.iterator(prefix.charAt(i));
                iteratorStack.push(it);
                if (it.hasNext() && it.peek().getChar() == prefix.charAt(i)) {
                    if (i != prefix.length() - 1) {
                        index++;
                        node = it.next().getNode();
                    }
                } else {
                    break;
                }
            }
            iterator = iteratorStack.pop();
        }
    }

    private char[] key;
    private int index;

    @Override
    protected Map.Entry<String, T> computeNext() {
        while (iterator.hasNext()) {
            final CharacterAndNode<T> charAndNode = iterator.next();
            updateKey(charAndNode.getChar());
            iteratorStack.push(iterator);
            iterator = charAndNode.getNode().iterator();
            if (charAndNode.getNode().getValue() != null) {
                // This is an actual entry of interest
                return MapUtils.trieNodeEntry(charAndNode.getNode(), new String(key, 0, index));
            }
        }
        if (!iteratorStack.isEmpty()) {
            iterator = iteratorStack.pop();
            index--;
            return computeNext();
        } else {
            return endOfData();
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
