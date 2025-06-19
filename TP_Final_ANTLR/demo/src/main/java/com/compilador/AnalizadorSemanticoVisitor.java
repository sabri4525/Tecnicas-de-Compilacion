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
        }
        return null;
    }

    @Override
    public Void visitAsignacion(MiLenguajeParser.AsignacionContext ctx) {
        String nombre = ctx.ID().getText();
        if (!tablaSimbolos.existe(nombre)) {
            erroresSemanticos
                    .add("Error semántico: Variable '" + nombre + "' no declarada (línea " + ctx.start.getLine() + ")");
        }
        return null;
    }

    @Override
    public Void visitChildren(org.antlr.v4.runtime.tree.RuleNode node) {
        return super.visitChildren(node);
    }
}
