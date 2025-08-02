package com.sqlparser.sql;

import com.sqlparser.sql.parser.MultiModeSQLBaseListener;
import com.sqlparser.sql.parser.MultiModeSQLParser;

public class SqlPrintListener extends MultiModeSQLBaseListener {

    @Override
    public void enterSystemBlock(MultiModeSQLParser.SystemBlockContext ctx) {
        System.out.println("=== SYSTEM BLOCK START ===");
    }

    @Override
    public void exitSystemBlock(MultiModeSQLParser.SystemBlockContext ctx) {
        System.out.println("=== SYSTEM BLOCK END ===");
    }

    @Override
    public void enterStarrocksBlock(MultiModeSQLParser.StarrocksBlockContext ctx) {
        System.out.println("=== STARROCKS BLOCK START ===");
    }

    @Override
    public void exitStarrocksBlock(MultiModeSQLParser.StarrocksBlockContext ctx) {
        System.out.println("=== STARROCKS BLOCK END ===");
    }

    @Override
    public void enterPysparkBlock(MultiModeSQLParser.PysparkBlockContext ctx) {
        System.out.println("=== PYSPARK BLOCK START ===");
    }

    @Override
    public void exitPysparkBlock(MultiModeSQLParser.PysparkBlockContext ctx) {
        System.out.println("=== PYSPARK BLOCK END ===");
    }

    @Override
    public void enterCreateStarrocksBlock(MultiModeSQLParser.CreateStarrocksBlockContext ctx) {
        for (MultiModeSQLParser.SystemStatementContext stmt : ctx.systemStatement()) {
            System.out.println("System Statement: " + stmt.getText());
        }
    }

    @Override
    public void enterQueryBlock(MultiModeSQLParser.QueryBlockContext ctx) {
        for (MultiModeSQLParser.QueryStatementContext stmt : ctx.queryStatement()) {
            System.out.println("Query Statement: " + stmt.getText());
        }
    }
}