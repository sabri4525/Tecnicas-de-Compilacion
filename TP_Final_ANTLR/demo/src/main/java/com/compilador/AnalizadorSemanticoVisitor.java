package com.compilador;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorSemanticoVisitor extends MiLenguajeBaseVisitor<Void> {
    private final TablaSimbolos tablaSimbolos = new TablaSimbolos();
    private final List<String> erroresSemanticos = new ArrayList<>();

    public List<String> getErroresSemanticos() {
        return erroresSemanticos;
    }

    @Override
    public Void visitDeclaracion(MiLenguajeParser.DeclaracionContext ctx) {
        String tipo = ctx.tipo().getText();
        MiLenguajeParser.ListaDeclaracionContext lista = ctx.listaDeclaracion();
        for (MiLenguajeParser.DeclaradorContext declaradorCtx : lista.declarador()) {
            String nombre = declaradorCtx.ID().getText();
            if (!tablaSimbolos.declarar(nombre, tipo)) {
                erroresSemanticos.add("Error semántico: Variable '" + nombre + "' ya declarada (línea "
                        + declaradorCtx.start.getLine() + ")");
            }
            // Verifica tipo en inicialización
            if (declaradorCtx.expresion() != null) {
                String tipoExpr = obtenerTipoExpresion(declaradorCtx.expresion());
                if (tipoExpr != null && !tipoExpr.equals(tipo)) {
                    erroresSemanticos.add("Error semántico: Asignación incompatible para '" + nombre + "' (línea "
                            + declaradorCtx.start.getLine() + "). Se esperaba " + tipo + " pero se obtuvo " + tipoExpr);
                }
            }
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitAsignacion(MiLenguajeParser.AsignacionContext ctx) {
        String nombre = ctx.ID().getText();
        if (!tablaSimbolos.existe(nombre)) {
            erroresSemanticos
                    .add("Error semántico: Variable '" + nombre + "' no declarada (línea " + ctx.start.getLine() + ")");
        } else {
            // Verifica tipo en asignación
            String tipoVar = tablaSimbolos.obtenerTipo(nombre);
            String tipoExpr = obtenerTipoExpresion(ctx.expresion());
            if (tipoExpr != null && !tipoExpr.equals(tipoVar)) {
                erroresSemanticos.add("Error semántico: Asignación incompatible para '" + nombre + "' (línea "
                        + ctx.start.getLine() + "). Se esperaba " + tipoVar + " pero se obtuvo " + tipoExpr);
            }
        }
        return visitChildren(ctx);
    }

    // Verifica que los identificadores usados en expresiones estén declarados
    @Override
    public Void visitIdExpr(MiLenguajeParser.IdExprContext ctx) {
        String nombre = ctx.ID().getText();
        if (!tablaSimbolos.existe(nombre)) {
            erroresSemanticos.add("Error semántico: Variable '" + nombre + "' no declarada (línea "
                    + ctx.start.getLine() + ")");
        }
        return null;
    }

    // Método auxiliar para obtener el tipo de una expresión simple
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
        // Para expresiones más complejas, podrías expandir esta lógica
        return null;
    }

    @Override
    public Void visitChildren(org.antlr.v4.runtime.tree.RuleNode node) {
        return super.visitChildren(node);
    }

    @Override
    public Void visitBloque(MiLenguajeParser.BloqueContext ctx) {
        tablaSimbolos.entrarScope();
        super.visitChildren(ctx);
        tablaSimbolos.salirScope();
        return null;
    }

    @Override
    public Void visitFuncion(MiLenguajeParser.FuncionContext ctx) {
        tablaSimbolos.entrarScope();
        super.visitChildren(ctx);
        tablaSimbolos.salirScope();
        return null;
    }

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

    @Override
    public Void visitForInit(MiLenguajeParser.ForInitContext ctx) {
        if (ctx.tipo() != null && ctx.listaDeclaracion() != null) {
            // Simula una declaración: tipo listaDeclaracion
            String tipo = ctx.tipo().getText();
            MiLenguajeParser.ListaDeclaracionContext lista = ctx.listaDeclaracion();
            for (MiLenguajeParser.DeclaradorContext declaradorCtx : lista.declarador()) {
                String nombre = declaradorCtx.ID().getText();
                if (!tablaSimbolos.declarar(nombre, tipo)) {
                    erroresSemanticos.add("Error semántico: Variable '" + nombre + "' ya declarada (línea "
                            + declaradorCtx.start.getLine() + ")");
                }
                // Verifica tipo en inicialización
                if (declaradorCtx.expresion() != null) {
                    String tipoExpr = obtenerTipoExpresion(declaradorCtx.expresion());
                    if (tipoExpr != null && !tipoExpr.equals(tipo)) {
                        erroresSemanticos.add("Error semántico: Asignación incompatible para '" + nombre + "' (línea "
                                + declaradorCtx.start.getLine() + "). Se esperaba " + tipo + " pero se obtuvo "
                                + tipoExpr);
                    }
                }
            }
        } else if (ctx.asignacion() != null) {
            visitAsignacion(ctx.asignacion());
        }
        return null;
    }
}
