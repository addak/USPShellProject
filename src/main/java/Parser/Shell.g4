grammar Shell;

SPACE : [ \t]+ -> skip;

input: PARAMS* ARGUMENTS*;
PARAMS: [+-][rwx]VALUES | '-'[a-z]+;
VALUES : [rwx]VALUES | [+-][rwx]VALUES | ;

ARGUMENTS: (STRING | WORD)*;
STRING : '"' (~[\\"] | '\\' [\\"])* '"';
WORD: ~[ \t\r\n"*]+;




