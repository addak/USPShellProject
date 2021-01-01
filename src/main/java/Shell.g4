grammar Shell;

SPACE : [ \t]+ -> skip;

input: PARAMS* ARGUMENTS*;
PARAMS: '-'[a-z]+;

ARGUMENTS: (STRING | WORD)*;
STRING : '"' (~[\\"] | '\\' [\\"])* '"';
WORD: ~[ \t\r\n"*]+;




