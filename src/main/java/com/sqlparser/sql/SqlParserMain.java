package com.sqlparser.sql;

import com.sqlparser.sql.parser.MultiModeSQLLexer;
import com.sqlparser.sql.parser.MultiModeSQLListener;
import com.sqlparser.sql.parser.MultiModeSQLParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.IOException;

public class SqlParserMain {
    public static void main(String[] args) throws IOException {
        String sql = """
            %system
            create starrocks mycluster size(large) Fe(cpu:4,mem:16) Cn(cpu:8,mem:32);

            %starrocks
            select * from test;
            insert into test values (1, 2);

            %pyspark
            df = spark.read.csv("file.csv");
            df.show();
            """;

        CharStream input = CharStreams.fromString(sql);
        MultiModeSQLLexer lexer = new MultiModeSQLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MultiModeSQLParser parser = new MultiModeSQLParser(tokens);
        ParseTree tree = parser.script();

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new SqlPrintListener(), tree);
    }
}
