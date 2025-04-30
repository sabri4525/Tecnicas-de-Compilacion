grammar MiLenguaje;

// Regla inicial
programa: s EOF;

s:
	ID IGUAL e PYC	# asignacion
	| IF PA e PC s	# sentenciaIf
	|				# sentenciaVacia;

e: e SUM t # suma | t # termino;

t: ID # identificador | NUM # numero | PA e PC # parens;

PA: '(';
PC: ')';
PYC: ';';
IGUAL: '=';
SUM: '+';
IF: 'if';

// Otros tokens
ID: [a-zA-Z][a-zA-Z_0-9]*;
NUM: [0-9]+;
WS: [\t\r\n]+ -> skip;