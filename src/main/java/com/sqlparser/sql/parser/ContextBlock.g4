grammar ContextBlock;

// 1차 파싱: 컨텍스트 블록 식별을 위한 문법
file: block+ EOF?;

block:
    (STARROCKS_CTX | PYSPARK_CTX | SYSTEM_CTX)? statement_list
;

statement_list:
    statement+
;

statement:
    (any_token)+ SEMI
;

any_token: ID | INT | STRING | WS | EQ | COMMA | DOT | STAR | LPAREN | RPAREN | OTHER;


// --- Lexer Rules ---
// Lexer 규칙들은 파일의 맨 아래에 위치해야 합니다.
STARROCKS_CTX: '%starrocks';
PYSPARK_CTX: '%pyspark';
SYSTEM_CTX: '%system';

ID: [a-zA-Z_][a-zA-Z0-9_]*;
INT: [0-9]+;
STRING: '"' (~'"')* '"' | '\'' (~'\'')* '\'';
SEMI: ';';
EQ: '=';
COMMA: ',';
DOT: '.';
STAR: '*';
LPAREN: '(';
RPAREN: ')';
WS: [ \t\r\n]+ -> skip;
OTHER: .;