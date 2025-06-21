package com.compilador;

import java.util.*;

public class AnalizadorSemanticoVisitor extends MiLenguajeBaseVisitor<Void> {
    private final TablaSimbolos tablaSimbolos = new TablaSimbolos();
    private final List<String> erroresSemanticos = new ArrayList<>();
    private final List<String> warningsSemanticos = new ArrayList<>();

    // Conjuntos para rastrear el ciclo de vida de las variables
    private final Set<String> variablesDeclaradas = new HashSet<>();
    private final Set<String> variablesUsadas = new HashSet<>();
    private final Set<String> variablesInicializadas = new HashSet<>();

    // Mapa para almacenar las líneas donde se declaran las variables (para
    // warnings)
    private final Map<String, Integer> lineasDeclaracion = new HashMap<>();

    // Flag para detectar código inalcanzable después de statements de return
    private boolean despuesDeReturn = false;

    public List<String> getErroresSemanticos() {
        return erroresSemanticos;
    }

    public List<String> getWarningsSemanticos() {
        return warningsSemanticos;
    }

    /**
     * Visita declaraciones de variables, registra en tabla de símbolos y verifica
     * tipos
     */
    @Override
    public Void visitDeclaracion(MiLenguajeParser.DeclaracionContext ctx) {
        String tipo = ctx.tipo().getText();
        MiLenguajeParser.ListaDeclaracionContext lista = ctx.listaDeclaracion();

        for (MiLenguajeParser.DeclaradorContext declaradorCtx : lista.declarador()) {
            String nombre = declaradorCtx.ID().getText();
            variablesDeclaradas.add(nombre);
            lineasDeclaracion.put(nombre, declaradorCtx.start.getLine());

            // Intenta registrar la variable en la tabla de símbolos del scope actual
            if (!tablaSimbolos.declarar(nombre, tipo)) {
                erroresSemanticos.add("Error semántico: Variable '" + nombre + "' ya declarada (línea "
                        + declaradorCtx.start.getLine() + ")");
            }

            // Procesa inicialización si existe
            if (declaradorCtx.expresion() != null) {
                variablesInicializadas.add(nombre);
                variablesUsadas.add(nombre); // La variable se usa al ser inicializada

                String tipoExpr = obtenerTipoExpresion(declaradorCtx.expresion());
                if (tipoExpr != null && !tipoExpr.equals(tipo)) {
                    // Verifica si la conversión de tipos es segura
                    if (esConversionSegura(tipoExpr, tipo)) {
                        warningsSemanticos.add("Warning: Conversión implícita de " + tipoExpr + " a " + tipo +
                                " para variable '" + nombre + "' (línea " + declaradorCtx.start.getLine() + ")");
                    } else {
                        erroresSemanticos.add("Error semántico: Asignación incompatible para '" + nombre + "' (línea "
                                + declaradorCtx.start.getLine() + "). Se esperaba " + tipo + " pero se obtuvo "
                                + tipoExpr);
                    }
                }
            }

            // Detecta código inalcanzable después de return
            if (despuesDeReturn) {
                warningsSemanticos.add("Warning: Código inalcanzable - declaración después de return (línea "
                        + declaradorCtx.start.getLine() + ")");
            }
        }
        return visitChildren(ctx);
    }

    /**
     * Visita asignaciones, verifica que la variable exista y los tipos sean
     * compatibles
     */
    @Override
    public Void visitAsignacion(MiLenguajeParser.AsignacionContext ctx) {
        String nombre = ctx.ID().getText();

        // Verifica que la variable esté declarada antes de usarla
        if (!tablaSimbolos.existe(nombre)) {
            erroresSemanticos.add("Error semántico: Variable '" + nombre + "' no declarada (línea " +
                    ctx.start.getLine() + ")");
        } else {
            variablesInicializadas.add(nombre);

            // Verifica compatibilidad de tipos en la asignación
            String tipoVar = tablaSimbolos.obtenerTipo(nombre);
            String tipoExpr = obtenerTipoExpresion(ctx.expresion());
            if (tipoExpr != null && !tipoExpr.equals(tipoVar)) {
                if (esConversionSegura(tipoExpr, tipoVar)) {
                    warningsSemanticos.add("Warning: Conversión implícita de " + tipoExpr + " a " + tipoVar +
                            " para variable '" + nombre + "' (línea " + ctx.start.getLine() + ")");
                } else {
                    erroresSemanticos.add("Error semántico: Asignación incompatible para '" + nombre + "' (línea "
                            + ctx.start.getLine() + "). Se esperaba " + tipoVar + " pero se obtuvo " + tipoExpr);
                }
            }

            // Detecta posible error de asignación en lugar de comparación
            if (ctx.expresion() instanceof MiLenguajeParser.IgualExprContext) {
                warningsSemanticos
                        .add("Warning: ¿Quisiste usar '==' en lugar de '='? Posible asignación en condición (línea "
                                + ctx.start.getLine() + ")");
            }
        }

        // Detecta código inalcanzable
        if (despuesDeReturn) {
            warningsSemanticos.add("Warning: Código inalcanzable - asignación después de return (línea "
                    + ctx.start.getLine() + ")");
        }

        return visitChildren(ctx);
    }

    /**
     * Visita uso de identificadores en expresiones, verifica que estén declarados e
     * inicializados
     */
    @Override
    public Void visitIdExpr(MiLenguajeParser.IdExprContext ctx) {
        String nombre = ctx.ID().getText();
        variablesUsadas.add(nombre);

        if (!tablaSimbolos.existe(nombre)) {
            erroresSemanticos.add("Error semántico: Variable '" + nombre + "' no declarada (línea "
                    + ctx.start.getLine() + ")");
        } else {
            // Verifica si la variable fue inicializada antes de usarla
            if (!variablesInicializadas.contains(nombre)) {
                warningsSemanticos.add("Warning: Variable '" + nombre + "' puede no estar inicializada (línea "
                        + ctx.start.getLine() + ")");
            }
        }
        return visitChildren(ctx);
    }

    /**
     * Visita operaciones de multiplicación y división, detecta divisiones por cero
     */
    @Override
    public Void visitMulDivExpr(MiLenguajeParser.MulDivExprContext ctx) {
        if (ctx.op.getText().equals("/")) {
            // Detecta divisiones por cero con literales numéricos
            if (ctx.expresion(1) instanceof MiLenguajeParser.NumExprContext) {
                String valor = ctx.expresion(1).getText();
                if (valor.equals("0")) {
                    warningsSemanticos.add("Warning: División por cero detectada (línea " +
                            ctx.start.getLine() + ")");
                }
            }
        }
        return visitChildren(ctx);
    }

    /**
     * Visita sentencias, detecta returns para marcar código posterior como
     * inalcanzable
     */
    @Override
    public Void visitSentencia(MiLenguajeParser.SentenciaContext ctx) {
        if (ctx.RETURN() != null) {
            despuesDeReturn = true;
        }
        return visitChildren(ctx);
    }

    /**
     * Verifica al final del análisis qué variables fueron declaradas pero nunca
     * usadas
     */
    public void verificarVariablesNoUsadas() {
        for (String variable : variablesDeclaradas) {
            if (!variablesUsadas.contains(variable)) {
                Integer linea = lineasDeclaracion.get(variable);
                if (linea != null) {
                    warningsSemanticos.add(
                            "Warning: Variable '" + variable + "' declarada pero nunca usada (línea " + linea + ")");
                } else {
                    warningsSemanticos.add("Warning: Variable '" + variable + "' declarada pero nunca usada");
                }
            }
        }
    }

    /**
     * Determina si una conversión de tipos es segura (sin pérdida de datos)
     */
    private boolean esConversionSegura(String tipoOrigen, String tipoDestino) {
        if (tipoOrigen.equals("int") && tipoDestino.equals("double")) {
            return true;
        }
        if (tipoOrigen.equals("char") && tipoDestino.equals("int")) {
            return true;
        }
        return false;
    }

    /**
     * Obtiene el tipo de una expresión analizando su contexto
     */
    private String obtenerTipoExpresion(MiLenguajeParser.ExpresionContext ctx) {
        if (ctx instanceof MiLenguajeParser.NumExprContext) {
            String texto = ctx.getText();
            if (texto.contains("."))
                return "double";
            else
                return "int";
        }
        if (ctx instanceof MiLenguajeParser.CharExprContext) {
            return "char";
        }
        if (ctx instanceof MiLenguajeParser.IdExprContext) {
            String nombre = ((MiLenguajeParser.IdExprContext) ctx).ID().getText();
            return tablaSimbolos.obtenerTipo(nombre);
        }
        return null;
    }

    /**
     * Maneja scopes de bloques creando nuevos contextos para variables locales
     */
    @Override
    public Void visitBloque(MiLenguajeParser.BloqueContext ctx) {
        tablaSimbolos.entrarScope();
        super.visitChildren(ctx);
        tablaSimbolos.salirScope();
        return null;
    }

    /**
     * Maneja scopes de funciones y reinicia el flag de código inalcanzable
     */
    @Override
    public Void visitFuncion(MiLenguajeParser.FuncionContext ctx) {
        tablaSimbolos.entrarScope();
        despuesDeReturn = false; // Reinicia para cada función

        // Procesa parámetros antes del cuerpo de la función
        if (ctx.parametros() != null) {
            visit(ctx.parametros());
        }

        visit(ctx.bloque());
        tablaSimbolos.salirScope();
        return null;
    }

    /**
     * Maneja scopes de bucles for con variables locales
     */
    @Override
    public Void visitForStmt(MiLenguajeParser.ForStmtContext ctx) {
        tablaSimbolos.entrarScope();
        if (ctx.forInit() != null)
            visit(ctx.forInit());
        if (ctx.expresion() != null)
            visit(ctx.expresion());
        if (ctx.asignacion() != null)
            visit(ctx.asignacion());
        visit(ctx.sentencia());
        tablaSimbolos.salirScope();
        return null;
    }

    /**
     * Procesa inicializaciones en bucles for, manejando tanto declaraciones como
     * asignaciones
     */
    @Override
    public Void visitForInit(MiLenguajeParser.ForInitContext ctx) {
        if (ctx.tipo() != null && ctx.listaDeclaracion() != null) {
            // Es una declaración en el for
            String tipo = ctx.tipo().getText();
            MiLenguajeParser.ListaDeclaracionContext lista = ctx.listaDeclaracion();
            for (MiLenguajeParser.DeclaradorContext declaradorCtx : lista.declarador()) {
                String nombre = declaradorCtx.ID().getText();
                variablesDeclaradas.add(nombre);
                lineasDeclaracion.put(nombre, declaradorCtx.start.getLine());

                if (!tablaSimbolos.declarar(nombre, tipo)) {
                    erroresSemanticos.add("Error semántico: Variable '" + nombre + "' ya declarada (línea "
                            + declaradorCtx.start.getLine() + ")");
                }
                if (declaradorCtx.expresion() != null) {
                    variablesInicializadas.add(nombre);
                    variablesUsadas.add(nombre);
                    String tipoExpr = obtenerTipoExpresion(declaradorCtx.expresion());
                    if (tipoExpr != null && !tipoExpr.equals(tipo)) {
                        if (esConversionSegura(tipoExpr, tipo)) {
                            warningsSemanticos.add("Warning: Conversión implícita de " + tipoExpr + " a " + tipo +
                                    " para variable '" + nombre + "' (línea " + declaradorCtx.start.getLine() + ")");
                        } else {
                            erroresSemanticos
                                    .add("Error semántico: Asignación incompatible para '" + nombre + "' (línea "
                                            + declaradorCtx.start.getLine() + "). Se esperaba " + tipo
                                            + " pero se obtuvo " + tipoExpr);
                        }
                    }
                }
            }
        } else if (ctx.asignacion() != null) {
            // Es una asignación en el for
            visitAsignacion(ctx.asignacion());
        }
        return null;
    }

    /**
     * Registra parámetros de función en la tabla de símbolos del scope de la
     * función
     */
    @Override
    public Void visitParametro(MiLenguajeParser.ParametroContext ctx) {
        String tipo = ctx.tipo().getText();
        String nombre = ctx.ID().getText();

        variablesDeclaradas.add(nombre);
        lineasDeclaracion.put(nombre, ctx.start.getLine());
        variablesInicializadas.add(nombre); // Los parámetros se consideran inicializados

        if (!tablaSimbolos.declarar(nombre, tipo)) {
            erroresSemanticos.add("Error semántico: Parámetro '" + nombre + "' ya declarado (línea "
                    + ctx.start.getLine() + ")");
        }

        return null;
    }

    @Override
    public Void visitChildren(org.antlr.v4.runtime.tree.RuleNode node) {
        return super.visitChildren(node);
    }
}
