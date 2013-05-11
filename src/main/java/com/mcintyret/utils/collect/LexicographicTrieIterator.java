package com.mcintyret.utils.collect;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import java.util.Map;

import static com.google.common.collect.Iterators.singletonIterator;

/**
 * User: mcintyret2
 * Date: 09/04/2013
 */
class LexicographicTrieIterator<V> extends AbstractTrieIterator<V> {

    LexicographicTrieIterator(AbstractTrie<V> trie) {
        this(trie, trie.getRoot(), "");
    }

    LexicographicTrieIterator(AbstractTrie<V> trie, TrieNode<V> baseNode, String prefix) {
        super(trie, prefix);
        index = prefix.length();
        iterator = baseNode == null ? Iterators.<CharacterAndNode<V>>emptyIterator() :
                singletonIterator(new CharacterAndNode<>('\0', baseNode));
    }

    LexicographicTrieIterator(AbstractTrie<V> trie, String prefix) {
        super(trie, prefix);
        TrieNode<V> node = trie.getRoot();
        for (int i = 0; i < prefix.length(); i++) {
            PeekingIterator<CharacterAndNode<V>> it = node.iterator(prefix.charAt(i));
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

    @Override
    protected Map.Entry<String, V> computeNext() {
        while (iterator.hasNext()) {
            final CharacterAndNode<V> charAndNode = iterator.next();
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

}
