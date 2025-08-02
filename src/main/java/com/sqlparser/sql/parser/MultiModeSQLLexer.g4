// Copyright 2021-present StarRocks, Inc. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


lexer grammar MultiModeSQLLexer;


ID: [a-zA-Z_][a-zA-Z_0-9]*;

RESOURCE_KEY: [a-zA-Z_][a-zA-Z0-9_]*;
RESOURCE_VALUE: [a-zA-Z0-9_./=]+;  // 따옴표 제외

COMMA : ',';
COLON : ':';
LPAREN : '(';
RPAREN : ')';

CREATE : 'CREATE';
STARROCKS : 'STARROCKS';
FE: 'FE';
CN: 'CN';

fragment EXPONENT
    : 'E' [+-]? DIGIT+
    ;

fragment DIGIT
    : [0-9]
    ;

fragment LETTER
    : [a-zA-Z_$\u0080-\uffff]
    ;

SIMPLE_COMMENT
    : '--' ~[\r\n]* '\r'? '\n'? -> channel(HIDDEN)
    ;

BRACKETED_COMMENT
    : '/*'([ \r\n\t\u3000]* | ~'+' .*?) '*/' -> channel(HIDDEN)
    ;

OPTIMIZER_HINT
    : '/*+' .*? '*/' -> channel(2)
    ;

SEMICOLON: ';';

DOTDOTDOT: '...';

WS
    : [ \r\n\t\u3000]+ -> channel(HIDDEN)
    ;

