grammar MiLenguaje;

programa: (token)* EOF;

token:
	PA
	| PC
	| CA
	| CC
	| LA
	| LC
	| PYC
	| COMA
	| IGUAL
	| MAYOR
	| MAYOR_IGUAL
	| MENOR
	| MENOR_IGUAL
	| EQL
	| DISTINTO
	| SUM
	| RES
	| MUL
	| DIV
	| MOD
	| OR
	| AND
	| NOT
	| FOR
	| WHILE
	| IF
	| ELSE
	| INT
	| CHAR
	| DOUBLE
	| VOID
	| RETURN
	| ID
	| INTEGER
	| DECIMAL
	| CHARACTER
	| OTRO;

fragment LETRA: [A-Za-z];
fragment DIGITO: [0-9];

PA: '(';
PC: ')';
CA: '[';
CC: ']';
LA: '{';
LC: '}';

PYC: ';';
COMA: ',';

IGUAL: '=';

MAYOR: '>';
MAYOR_IGUAL: '>=';
MENOR: '<';
MENOR_IGUAL: '<=';
EQL: '==';
DISTINTO: '!=';

SUM: '+';
RES: '-';
MUL: '*';
DIV: '/';
MOD: '%';

OR: '||';
AND: '&&';
NOT: '!';

FOR: 'for';
WHILE: 'while';

IF: 'if';
ELSE: 'else';

INT: 'int';
CHAR: 'char';
DOUBLE: 'double';
VOID: 'void';

RETURN: 'return';

ID: (LETRA | '_') (LETRA | DIGITO | '_')*;

INTEGER: DIGITO+;
DECIMAL: INTEGER '.' INTEGER;
CHARACTER: '\'' (~['\r\n] | '\\' .) '\'';

COMENTARIO_LINEA: '//' ~[\r\n]* -> skip;
COMENTARIO_BLOQUE: '/*' .*? '*/' -> skip;

WS: [ \r\n\t] -> skip;
OTRO: .;