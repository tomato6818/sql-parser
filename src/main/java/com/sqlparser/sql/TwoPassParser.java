package com.sqlparser.sql;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import com.sqlparser.sql.parser.ContextBlockLexer;
import com.sqlparser.sql.parser.ContextBlockParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;





public class TwoPassParser {

    public static void main(String[] args) throws IOException {
        String inputWithContext =
                "%system\n" +
                        "CREATE STARROCKS mycluster size(large) FE(cpu:4,mem:16) CN(cpu:8,mem:32);\n" +
                        "select * from default_cluster_test;\n" +
                        "\n" +
                        "%starrocks\n" +
                        "select * from anotherCluster_test;\n" +
                        "insert into some_table values (1, 2);\n" +
                        "\n" +
                        "%pyspark\n" +
                        "df = spark.read.csv(\"file.csv\");\n" +
                        "df.show();";

        String inputWithoutContext =
                "CREATE STARROCKS default_cluster size(small);";

        System.out.println("--- 컨텍스트 선언이 있는 입력 처리 ---");
        processInput(inputWithContext);

        System.out.println("\n\n" + "--- 컨텍스트 선언이 없는 입력 처리 (기본값: %system) ---");
        processInput(inputWithoutContext);
    }

    private static void processInput(String input) throws IOException {
        CommonTokenStream tokens = createTokenStream(input);

        System.out.println("--- 1차 파싱 시작 ---");
        List<ContextBlockInfo> blocks = firstPass(tokens);

        System.out.println("\n--- 2차 파싱 시작 ---");
        secondPass(tokens, blocks);
    }

    private static CommonTokenStream createTokenStream(String input) throws IOException {
        InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        CharStream charStream = CharStreams.fromStream(stream, StandardCharsets.UTF_8);
        return new CommonTokenStream(new ContextBlockLexer(charStream));
    }

    private static List<ContextBlockInfo> firstPass(CommonTokenStream tokens) {
        System.out.println("  -> 모든 입력에 대해 파싱 트리를 생성합니다.");

        ContextBlockParser parser = new ContextBlockParser(tokens);
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.file();

        ContextBlockListener listener = new ContextBlockListener();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, tree);

        List<ContextBlockInfo> blocks = listener.getBlocks();

        // 블록이 하나도 없는 경우, 전체를 SYSTEM 블록으로 처리
        if (blocks.isEmpty() && tokens.size() > 0) {
            System.out.println("  -> 컨텍스트 블록이 없습니다. 전체 입력을 '%system'으로 간주합니다.");
            blocks.add(new ContextBlockInfo("SYSTEM", 0, tokens.size() - 1));
        }

        return blocks;
    }

    private static void secondPass(CommonTokenStream tokens, List<ContextBlockInfo> blocks) {
        for (ContextBlockInfo block : blocks) {
            if (block.startTokenIndex > block.endTokenIndex) {
                System.out.println("  -> WARNING: 블록 [" + block.type + "]의 토큰 범위가 유효하지 않습니다. 스킵합니다.");
                System.out.println();
                continue;
            }

            String blockText = getBlockText(tokens, block);
            System.out.println("  -> 처리 중인 블록: " + block.type);
            System.out.println("  -> 쿼리 내용:\n\"\"\"\n" + blockText.trim() + "\n\"\"\"");

            try {
                switch (block.type) {
                    case "SYSTEM":
                        parseSystem(tokens, block);
                        break;
                    case "STARROCKS":
                        parseStarrocks(tokens, block);
                        break;
                    case "PYSPARK":
                        parsePyspark(tokens, block);
                        break;
                    default:
                        System.out.println("  -> 알 수 없는 블록 타입입니다.");
                }
            } catch (ParseCancellationException e) {
                System.out.println("  -> 파싱 실패: " + e.getMessage());
            }
            System.out.println();
        }
    }

    // System 전용 파싱 메서드 (새로 추가)
    private static void parseSystem(CommonTokenStream tokens, ContextBlockInfo block) {
        // 여기에 'System.g4'로 생성된 SystemParser를 사용합니다.
        // SystemParser parser = new SystemParser(tokens);
        // ...
        System.out.println("  -> System 파서로 재파싱을 시도합니다.");
        System.out.println("  -> System 파싱 성공! (예제이므로 실제 파싱은 생략)");
    }

    // Starrocks 전용 파싱 메서드 (기존과 동일)
    private static void parseStarrocks(CommonTokenStream tokens, ContextBlockInfo block) {


        System.out.println("  -> Starrocks 파서로 재파싱을 시도합니다.");
        System.out.println("  -> Starrocks 파싱 성공!");
    }

    private static void parsePyspark(CommonTokenStream tokens, ContextBlockInfo block) {
        System.out.println("  -> Pyspark 파서로 재파싱을 시도합니다.");
        System.out.println("  -> Pyspark 파싱 성공! (예제이므로 실제 파싱은 생략)");
    }

    private static String getBlockText(CommonTokenStream tokens, ContextBlockInfo block) {
        List<Token> sublist = tokens.getTokens(block.startTokenIndex, block.endTokenIndex);
        StringBuilder sb = new StringBuilder();
        for (Token token : sublist) {
            sb.append(token.getText());
        }
        return sb.toString();
    }
}