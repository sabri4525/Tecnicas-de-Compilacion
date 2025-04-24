grammar MiniLenguaje;

// Punto de entrada de la gramática
program: token* EOF;

// Definición de tokens
token:
	ID
	| INTEGER
	| STRING
	| BOOLEAN
	| KEYWORD
	| OPERATOR
	| SEPARATOR;

// Definiciones de tokens
ID: [a-zA-Z][a-zA-Z0-9_]*;
INTEGER: [0-9]+;
STRING: '"' (~["\r\n] | '\\"')* '"';
BOOLEAN: 'true' | 'false';

// Palabras clave
KEYWORD:
	'var'
	| 'if'
	| 'else'
	| 'print'
	| 'while'
	| 'function'
	| 'return';

// Operadores
OPERATOR:
	'+'
	| '-'
	| '*'
	| '/'
	| '%'
	| '='
	| '=='
	| '!='
	| '<'
	| '>'
	| '<='
	| '>='
	| '&&'
	| '||'
	| '!';

// Separadores
SEPARATOR: ';' | '(' | ')' | '{' | '}' | ',' | '.';

// Ignorar espacios en blanco y saltos de línea
WS: [ \t\r\n]+ -> skip;

// Ignorar comentarios de una línea
COMMENT: '//' ~[\r\n]* -> skip;

// Ignorar comentarios de bloque
BLOCK_COMMENT: '/*' .*? '*/' -> skip;