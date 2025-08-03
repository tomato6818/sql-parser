package com.sqlparser.sql;

public class ContextBlockInfo {
    String type;
    int startTokenIndex;
    int endTokenIndex;
    public ContextBlockInfo(String type, int startTokenIndex, int endTokenIndex) {
        this.type = type;
        this.startTokenIndex = startTokenIndex;
        this.endTokenIndex = endTokenIndex;
    }
}