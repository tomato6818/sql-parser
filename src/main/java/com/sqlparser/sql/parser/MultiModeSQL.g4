grammar MultiModeSQL;

script: (systemBlock | starrocksBlock | pysparkBlock | queryBlock | createStarrocksBlock)* EOF;

systemBlock: SYSTEM_MARK WS;
starrocksBlock: STARROCKS_MARK WS;
pysparkBlock: PYSPARK_MARK WS;
createStarrocksBlock: (systemStatement SEMICOLON?)+;
queryBlock: (queryStatement)+;

systemStatement
    : CREATE STARROCKS ID (SIZE_SPEC)? (resourceSpec (resourceSpec)*)?
    ;

queryStatement
  : ( . )+? SEMICOLON    // 모든 문자 하나 이상 (non-greedy) + 세미콜론
  ;

pysparkStatement
  : ( . )+? SEMICOLON    // 모든 문자 하나 이상 (non-greedy) + 세미콜론
  ;

resourceSpec
    : FE LPAREN resourceConfig RPAREN
    | CN LPAREN resourceConfig RPAREN
    ;

resourceConfig
    : (RESOURCE_KEY COLON RESOURCE_VALUE) (COMMA RESOURCE_KEY COLON RESOURCE_VALUE)*
    ;

SIZE_SPEC: 'size' LPAREN ID RPAREN;

SYSTEM_MARK: '%' 'system';
STARROCKS_MARK: '%' 'starrocks';
PYSPARK_MARK: '%' 'pyspark';




