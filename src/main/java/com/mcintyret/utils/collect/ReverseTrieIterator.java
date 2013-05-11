package com.mcintyret.utils.collect;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import static com.google.common.collect.Iterators.emptyIterator;
import static com.google.common.collect.Iterators.singletonIterator;

/**
 * User: mcintyret2
 * Date: 09/04/2013
 */
class ReverseTrieIterator<V> extends AbstractTrieIterator<V> {

    private final Stack<CharacterAndNode<V>> valueStack = new Stack<>();

    ReverseTrieIterator(AbstractTrie<V> trie) {
        this(trie, trie.getRoot(), "");
    }

    ReverseTrieIterator(AbstractTrie<V> trie, TrieNode<V> baseNode, String prefix) {
        super(trie, prefix);
        index = prefix.length();
        iterator = baseNode == null ? Iterators.<CharacterAndNode<V>>emptyIterator() :
                singletonIterator(new CharacterAndNode<>('\0', baseNode));
    }

    ReverseTrieIterator(AbstractTrie<V> trie, String prefix) {
        super(trie, prefix);
        TrieNode<V> node = trie.getRoot();
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            PeekingIterator<CharacterAndNode<V>> it = node.reverseIterator(c);
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

    @Override
    protected Map.Entry<String, V> computeNext() {
        while (iterator.hasNext()) {
            final CharacterAndNode<V> charAndNode = iterator.next();
            Iterator<CharacterAndNode<V>> it = charAndNode.getNode().reverseIterator();
            updateKey(charAndNode.getChar());
            if (it.hasNext()) {
                iteratorStack.push(iterator);
                valueStack.push(charAndNode);
                iterator = it;
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
            CharacterAndNode<V> popped = valueStack.pop();
            if (popped.getNode().getValue() != null) {
                return MapUtils.trieNodeEntry(popped.getNode(), new String(key, 0, index + 1));
            } else {
                return computeNext();
            }
        }
    }

}
