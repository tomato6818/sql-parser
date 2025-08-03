package com.sqlparser.sql;


import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import com.sqlparser.sql.parser.ContextBlockBaseListener;
import com.sqlparser.sql.parser.ContextBlockParser;
import java.util.ArrayList;
import java.util.List;

public class ContextBlockListener extends ContextBlockBaseListener {

    private final List<ContextBlockInfo> blocks = new ArrayList<>();

    public List<ContextBlockInfo> getBlocks() {
        return blocks;
    }

    /**
     * ANTLR이 'block' 규칙을 파싱할 때 호출됩니다.
     * 이 메서드는 블록의 타입과 토큰 범위를 추출하여 저장합니다.
     */
    @Override
    public void enterBlock(ContextBlockParser.BlockContext ctx) {
        String type;

        // 블록의 시작 토큰이 컨텍스트 선언인지 확인
        if (ctx.STARROCKS_CTX() != null) {
            type = "STARROCKS";
        } else if (ctx.PYSPARK_CTX() != null) {
            type = "PYSPARK";
        } else if (ctx.SYSTEM_CTX() != null) {
            type = "SYSTEM";
        } else {
            // 컨텍스트 선언이 없는 경우, 기본값인 "SYSTEM"으로 간주
            type = "SYSTEM";
        }

        // 블록의 시작 토큰 인덱스와 끝 토큰 인덱스를 가져옵니다.
        int startTokenIndex = ctx.start.getTokenIndex();
        int endTokenIndex = ctx.stop.getTokenIndex();

        // ContextBlockInfo 객체를 생성하여 리스트에 추가합니다.
        blocks.add(new ContextBlockInfo(type, startTokenIndex, endTokenIndex));
    }
}