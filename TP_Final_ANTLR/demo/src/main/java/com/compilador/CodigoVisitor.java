package com.compilador;

public class CodigoVisitor extends MiLenguajeBaseVisitor<Void> {
    private final GeneradorCodigo generador;

    public CodigoVisitor(GeneradorCodigo generador) {
        this.generador = generador;
    }

    @Override
    public Void visitAsignacion(MiLenguajeParser.AsignacionContext ctx) {
        String variable = ctx.ID().getText();

        // Visitar la expresión del lado derecho primero
        if (ctx.expresion() != null) {
            visit(ctx.expresion());
            // Generar código de asignación
            generador.generarAsignacion(variable, generador.getUltimoTemporal());
        }

        return null;
    }

    @Override
    public Void visitDeclaracion(MiLenguajeParser.DeclaracionContext ctx) {
        String tipo = ctx.tipo().getText();

        for (MiLenguajeParser.DeclaradorContext declarador : ctx.listaDeclaracion().declarador()) {
            String variable = declarador.ID().getText();

            if (declarador.expresion() != null) {
                // Si hay inicialización, visitar la expresión
                visit(declarador.expresion());
                generador.generarAsignacion(variable, generador.getUltimoTemporal());
            } else {
                // Solo declaración sin inicialización
                generador.generarDeclaracion(variable, tipo);
            }
        }

        return null;
    }

    @Override
    public Void visitIdExpr(MiLenguajeParser.IdExprContext ctx) {
        String variable = ctx.ID().getText();
        String temporal = generador.nuevoTemporal();
        generador.generarAsignacion(temporal, variable);
        return null;
    }

    @Override
    public Void visitNumExpr(MiLenguajeParser.NumExprContext ctx) {
        String valor = ctx.NUM().getText();
        String temporal = generador.nuevoTemporal();
        generador.generarAsignacion(temporal, valor);
        return null;
    }

    @Override
    public Void visitCharExpr(MiLenguajeParser.CharExprContext ctx) {
        String valor = ctx.CHAR().getText();
        String temporal = generador.nuevoTemporal();
        generador.generarAsignacion(temporal, valor);
        return null;
    }

    // Este método maneja todas las expresiones binarias de forma genérica
    @Override
    public Void visitChildren(org.antlr.v4.runtime.tree.RuleNode node) {
        // Si es una expresión con exactamente 3 hijos (operando operador operando)
        if (node instanceof MiLenguajeParser.ExpresionContext && node.getChildCount() == 3) {
            MiLenguajeParser.ExpresionContext ctx = (MiLenguajeParser.ExpresionContext) node;

            // Verificar que el hijo del medio sea un operador (no es un ID o NUM)
            String operador = ctx.getChild(1).getText();
            if (esOperador(operador)) {
                // Visitar primer operando
                visit(ctx.getChild(0));
                String temp1 = generador.getUltimoTemporal();

                // Visitar segundo operando
                visit(ctx.getChild(2));
                String temp2 = generador.getUltimoTemporal();

                // Generar operación
                String resultado = generador.nuevoTemporal();
                generador.generarOperacion(resultado, temp1, operador, temp2);

                return null;
            }
        }

        // Para otros casos, usar comportamiento por defecto
        return super.visitChildren(node);
    }

    // Método auxiliar para verificar si un texto es un operador
    private boolean esOperador(String texto) {
        return texto.equals("+") || texto.equals("-") || texto.equals("*") || texto.equals("/") ||
                texto.equals("<") || texto.equals(">") || texto.equals("<=") || texto.equals(">=") ||
                texto.equals("==") || texto.equals("!=") || texto.equals("&&") || texto.equals("||");
    }

    @Override
    public Void visitIfStmt(MiLenguajeParser.IfStmtContext ctx) {
        // Evaluar condición
        if (ctx.expresion() != null) {
            visit(ctx.expresion());
            String condicion = generador.getUltimoTemporal();

            // Generar etiquetas
            String etiquetaElse = generador.nuevaEtiqueta();
            String etiquetaFin = generador.nuevaEtiqueta();

            // Salto condicional
            generador.generarSaltoCondicional("ifFalse", condicion, etiquetaElse);

            // Bloque then
            if (ctx.sentencia(0) != null) {
                visit(ctx.sentencia(0));
            }

            if (ctx.sentencia().size() > 1) {
                // Hay bloque else
                generador.generarSalto(etiquetaFin);
                generador.generarEtiqueta(etiquetaElse);
                visit(ctx.sentencia(1));
                generador.generarEtiqueta(etiquetaFin);
            } else {
                generador.generarEtiqueta(etiquetaElse);
            }
        }

        return null;
    }

    @Override
    public Void visitWhileStmt(MiLenguajeParser.WhileStmtContext ctx) {
        String etiquetaInicio = generador.nuevaEtiqueta();
        String etiquetaFin = generador.nuevaEtiqueta();

        // Etiqueta de inicio del bucle
        generador.generarEtiqueta(etiquetaInicio);

        // Evaluar condición
        if (ctx.expresion() != null) {
            visit(ctx.expresion());
            String condicion = generador.getUltimoTemporal();

            // Salto condicional para salir del bucle
            generador.generarSaltoCondicional("ifFalse", condicion, etiquetaFin);
        }

        // Cuerpo del bucle
        if (ctx.sentencia() != null) {
            visit(ctx.sentencia());
        }

        // Salto de vuelta al inicio
        generador.generarSalto(etiquetaInicio);

        // Etiqueta de fin
        generador.generarEtiqueta(etiquetaFin);

        return null;
    }

    @Override
    public Void visitForStmt(MiLenguajeParser.ForStmtContext ctx) {
        // Inicialización
        if (ctx.forInit() != null) {
            visit(ctx.forInit());
        }

        String etiquetaInicio = generador.nuevaEtiqueta();
        String etiquetaFin = generador.nuevaEtiqueta();

        // Etiqueta de inicio del bucle
        generador.generarEtiqueta(etiquetaInicio);

        // Condición
        if (ctx.expresion() != null) {
            visit(ctx.expresion());
            String condicion = generador.getUltimoTemporal();
            generador.generarSaltoCondicional("ifFalse", condicion, etiquetaFin);
        }

        // Cuerpo del bucle
        if (ctx.sentencia() != null) {
            visit(ctx.sentencia());
        }

        // Incremento
        if (ctx.asignacion() != null) {
            visit(ctx.asignacion());
        }

        // Salto de vuelta al inicio
        generador.generarSalto(etiquetaInicio);

        // Etiqueta de fin
        generador.generarEtiqueta(etiquetaFin);

        return null;
    }

    @Override
    public Void visitSentencia(MiLenguajeParser.SentenciaContext ctx) {
        if (ctx.RETURN() != null && ctx.expresion() != null) {
            visit(ctx.expresion());
            String valor = generador.getUltimoTemporal();
            generador.generarReturn(valor);
            return null;
        } else if (ctx.RETURN() != null) {
            generador.generarReturn(null);
            return null;
        }

        // Para otros tipos de sentencias, usar comportamiento por defecto
        return super.visitChildren(ctx);
    }

    @Override
    public Void visitLlamadaExpr(MiLenguajeParser.LlamadaExprContext ctx) {
        String nombreFuncion = ctx.llamada().ID().getText();

        // Visitar argumentos si existen
        if (ctx.llamada().argumentos() != null) {
            for (MiLenguajeParser.ExpresionContext arg : ctx.llamada().argumentos().expresion()) {
                visit(arg);
                generador.generarParametro(generador.getUltimoTemporal());
            }
        }

        // Generar llamada
        String resultado = generador.nuevoTemporal();
        generador.generarLlamada(resultado, nombreFuncion);

        return null;
    }
}