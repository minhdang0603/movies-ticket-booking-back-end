package com.dangtm.movie.util;

import com.dangtm.movie.entity.Movie;
import com.dangtm.movie.entity.TrieNode;
import com.dangtm.movie.entity.index.MovieIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AutoCompleteUtil {

    TrieNode root;

    public AutoCompleteUtil() {
        root = new TrieNode();
    }

    public void insert(Movie movie) {
        TrieNode curr = root;

        for (char c :movie.getTitle().toLowerCase().toCharArray()) {
            TrieNode node = curr.children.get(c);
            if (node == null) {
                node = new TrieNode(c);
                curr.children.put(c, node);
            }
            curr = node;
        }
        curr.isWord = true;
        curr.frequency = 0;
        curr.movieIndex = MovieIndex.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .build();
    }

    public TrieNode findExact(String prefix) {
        TrieNode lastNode = root;
        for (char c : prefix.toCharArray()) {
            lastNode = lastNode.children.get(c);
            if (lastNode == null)
                return null;
        }
        return lastNode;
    }

    public void increaseFrequency(TrieNode lastNode) {
        lastNode.frequency++;
    }

    public List<MovieIndex> autoComplete(String prefix) {
        List<MovieIndex> autoCompleteList = new ArrayList<>();
        TrieNode lastNode = root;
        StringBuffer current = new StringBuffer();
        for (char c : prefix.toCharArray()) {
            lastNode = lastNode.children.get(c);
            if (lastNode == null)
                return autoCompleteList;
            current.append(c);
        }
        findWords(lastNode, autoCompleteList, current);
        return autoCompleteList;
    }

    private void findWords(TrieNode lastNode, List<MovieIndex> autoCompleteList, StringBuffer word) {
        if (lastNode.isWord)
            autoCompleteList.add(lastNode.movieIndex);

        if (lastNode.children.isEmpty() || autoCompleteList.size() >= 10)
            return;

        for (TrieNode node : lastNode.children.values()) {
            findWords(node, autoCompleteList, word.append(node.c));
            word.setLength(word.length() - 1);  // remove last character
        }
    }

    private void dfs(TrieNode node, StringBuilder prefix, List<String> result) {
        if (node.isWord) {
            result.add(prefix.toString());
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            prefix.append(entry.getKey());
            dfs(entry.getValue(), prefix, result);
            prefix.deleteCharAt(prefix.length() - 1); // Backtrack
        }
    }

    public List<String> traverse() {
        List<String> result = new ArrayList<>();
        dfs(root, new StringBuilder(), result);
        return result;
    }

}
