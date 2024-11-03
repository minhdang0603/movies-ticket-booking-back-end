package com.dangtm.movie.entity;

import com.dangtm.movie.entity.index.MovieIndex;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    public Map<Character, TrieNode> children;
    public char c;
    public boolean isWord;
    public MovieIndex movieIndex;
    public int frequency;

    public TrieNode(char c) {
        this.c = c;
        children = new HashMap<>();
    }

    public TrieNode() {
        children = new HashMap<>();
    }
}
