grammar MiLenguaje;

// Regla inicial
programa: (declaracion | funcion | sentencia)* EOF;

// Declaraciones
declaracion: tipo ID (IGUAL expresion)? PYC;

// Declaración de función
funcion: tipo ID PIZQ parametros? PDER bloque;

// Parámetros de función
parametros: parametro (COMA parametro)*;
parametro: tipo ID;

// Bloque de sentencias
bloque: CIZQ sentencia* CDER;

// Sentencias
sentencia:
	declaracion
	| asignacion
	| llamada PYC
	| ifStmt
	| whileStmt
	| forStmt
	| BREAK PYC
	| CONTINUE PYC
	| RETURN expresion? PYC
	| bloque;

// Asignación
asignacion: ID IGUAL expresion PYC;

// If-Else
ifStmt: IF PIZQ expresion PDER sentencia (ELSE sentencia)?;

// While
whileStmt: WHILE PIZQ expresion PDER sentencia;

// For
forStmt:
	FOR PIZQ (declaracion | asignacion | PYC) expresion? PYC asignacion? PDER sentencia;

// Llamada a función
llamada: ID PIZQ argumentos? PDER;
argumentos: expresion (COMA expresion)*;

// Expresiones
expresion:
    expresion op = (MUL | DIV) expresion                        # MulDiv
    | expresion op = (MAS | MENOS) expresion                    # AddSub
    | expresion op = (MENOR | MAYOR | MENOR_IGUAL | MAYOR_IGUAL | IGUAL_IGUAL | DISTINTO) expresion # Relacional
    | expresion op = (AND | OR) expresion                       # Logica
    | PIZQ expresion PDER                                       # Parentesis
    | ID                                                        # IdExpr
    | NUM                                                       # NumExpr
    | CHAR                                                      # CharExpr
    | llamada                                                   # LlamadaExpr;

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