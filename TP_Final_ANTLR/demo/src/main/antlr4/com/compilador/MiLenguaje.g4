grammar MiLenguaje;

// Regla inicial
programa: (inicio)* EOF;

// Inicio del programa
inicio: sentenciaInicial | funcion;

// Sentencia inicial 
sentenciaInicial: declaracion | asignacion PYC | llamada PYC;

// Declaraciones
declaracion: tipo listaDeclaracion PYC;
listaDeclaracion: declarador (COMA declarador)*;
declarador: ID (IGUAL expresion)?;

// Declaración de función
funcion: tipo ID PIZQ parametros? PDER bloque;

// Parámetros de función
parametros: parametro (COMA parametro)*;
parametro: tipo ID;

// Bloque de sentencias
bloque: CIZQ sentencia* CDER;

// Sentencias
sentencia:
	ifStmt
	| whileStmt
	| forStmt
	| BREAK PYC
	| CONTINUE PYC
	| RETURN expresion? PYC
	| bloque
	| expresion PYC;

// Asignación
asignacion: ID IGUAL expresion;

// If-Else
ifStmt:
	IF PIZQ expresion PDER CIZQ sentencia CDER (ELSE sentencia)?;

// While
whileStmt: WHILE PIZQ expresion PDER sentencia;

// For
forStmt:
	FOR PIZQ forInit? PYC expresion? PYC asignacion? PDER sentencia;

forInit: tipo listaDeclaracion | asignacion;

// Llamada a función
llamada: ID PIZQ argumentos? PDER;
argumentos: expresion (COMA expresion)*;

// Expresiones
expresion:
	expresion IGUAL expresion					# IgualExpr
	| expresion op = (MUL | DIV) expresion		# MulDivExpr
	| expresion op = (MAS | MENOS) expresion	# MasMenosExpr
	| expresion op = (
		MENOR
		| MAYOR
		| MENOR_IGUAL
		| MAYOR_IGUAL
		| IGUAL_IGUAL
		| DISTINTO
	) expresion								# Relacional
	| expresion op = (AND | OR) expresion	# Logica
	| PIZQ expresion PDER					# Parentesis
	| ID									# IdExpr
	| NUM									# NumExpr
	| CHAR									# CharExpr
	| llamada								# LlamadaExpr;

// Tipos de datos
tipo: INT | CHAR_TYPE | DOUBLE | VOID;

// Tokens para símbolos y operadores
PYC: ';';
COMA: ',';
IGUAL: '=';
PIZQ: '(';
PDER: ')';
CIZQ: '{';
CDER: '}';
MAS: '+';
MENOS: '-';
MUL: '*';
DIV: '/';
MENOR: '<';
MAYOR: '>';
MENOR_IGUAL: '<=';
MAYOR_IGUAL: '>=';
IGUAL_IGUAL: '==';
DISTINTO: '!=';
AND: '&&';
OR: '||';

// Palabras reservadas
IF: 'if';
ELSE: 'else';
WHILE: 'while';
FOR: 'for';
BREAK: 'break';
CONTINUE: 'continue';
RETURN: 'return';
INT: 'int';
CHAR_TYPE: 'char';
DOUBLE: 'double';
VOID: 'void';

// Tokens para identificadores y literales
ID: [a-zA-Z_][a-zA-Z_0-9]*;
NUM: [0-9]+ ('.' [0-9]+)?;
CHAR: '\'' . '\''; // Para el literal carácter

// Ignorar espacios y comentarios
WS: [ \t\r\n]+ -> skip;
COMMENT: '//' ~[\r\n]* -> skip;
LINE_COMMENT: '/*' .*? '*/' -> skip;