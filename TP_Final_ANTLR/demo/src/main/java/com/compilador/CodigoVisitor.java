package com.compilador;

public class CodigoVisitor extends MiLenguajeBaseVisitor<String> {
    private final GeneradorCodigo gen;

    public CodigoVisitor(GeneradorCodigo gen) {
        this.gen = gen;
    }

    // Asignación
    @Override
    public String visitAsignacion(MiLenguajeParser.AsignacionContext ctx) {
        String id = ctx.ID().getText();
        String valor = visit(ctx.expresion());
        gen.agregar(id + " = " + valor);
        return null;
    }

    // Declaración
    @Override
    public String visitDeclarador(MiLenguajeParser.DeclaradorContext ctx) {
        String id = ctx.ID().getText();
        if (ctx.expresion() != null) {
            String valor = visit(ctx.expresion());
            gen.agregar(id + " = " + valor);
        }
        return null;
    }

    // Expresiones binarias: *, /
    @Override
    public String visitMulDivExpr(MiLenguajeParser.MulDivExprContext ctx) {
        String izq = visit(ctx.expresion(0));
        String der = visit(ctx.expresion(1));
        String op = ctx.op.getText();
        String temp = gen.nuevoTemporal();
        gen.agregar(temp + " = " + izq + " " + op + " " + der);
        return temp;
    }

    // Expresiones binarias: +, -
    @Override
    public String visitMasMenosExpr(MiLenguajeParser.MasMenosExprContext ctx) {
        String izq = visit(ctx.expresion(0));
        String der = visit(ctx.expresion(1));
        String op = ctx.op.getText();
        String temp = gen.nuevoTemporal();
        gen.agregar(temp + " = " + izq + " " + op + " " + der);
        return temp;
    }

    // Expresiones relacionales: <, >, <=, >=, ==, !=
    @Override
    public String visitRelacional(MiLenguajeParser.RelacionalContext ctx) {
        String izq = visit(ctx.expresion(0));
        String der = visit(ctx.expresion(1));
        String op = ctx.op.getText();
        String temp = gen.nuevoTemporal();
        gen.agregar(temp + " = " + izq + " " + op + " " + der);
        return temp;
    }

    // Expresiones lógicas: &&, ||
    @Override
    public String visitLogica(MiLenguajeParser.LogicaContext ctx) {
        String izq = visit(ctx.expresion(0));
        String der = visit(ctx.expresion(1));
        String op = ctx.op.getText();
        String temp = gen.nuevoTemporal();
        gen.agregar(temp + " = " + izq + " " + op + " " + der);
        return temp;
    }

    // Expresión de igualdad (=)
    @Override
    public String visitIgualExpr(MiLenguajeParser.IgualExprContext ctx) {
        String izq = visit(ctx.expresion(0));
        String der = visit(ctx.expresion(1));
        String temp = gen.nuevoTemporal();
        gen.agregar(temp + " = " + izq + " = " + der);
        return temp;
    }

    // Paréntesis
    @Override
    public String visitParentesis(MiLenguajeParser.ParentesisContext ctx) {
        return visit(ctx.expresion());
    }

    // Identificador
    @Override
    public String visitIdExpr(MiLenguajeParser.IdExprContext ctx) {
        return ctx.ID().getText();
    }

    // Número
    @Override
    public String visitNumExpr(MiLenguajeParser.NumExprContext ctx) {
        return ctx.NUM().getText();
    }

    // Carácter
    @Override
    public String visitCharExpr(MiLenguajeParser.CharExprContext ctx) {
        return ctx.CHAR().getText();
    }

    // Llamada a función
    @Override
    public String visitLlamadaExpr(MiLenguajeParser.LlamadaExprContext ctx) {
        // Puedes adaptar según tu convención de llamadas
        String nombre = ctx.llamada().ID().getText();
        String args = "";
        if (ctx.llamada().argumentos() != null) {
            for (int i = 0; i < ctx.llamada().argumentos().expresion().size(); i++) {
                if (i > 0)
                    args += ", ";
                args += visit(ctx.llamada().argumentos().expresion(i));
            }
        }
        String temp = gen.nuevoTemporal();
        gen.agregar(temp + " = call " + nombre + "(" + args + ")");
        return temp;
    }

    // If
    @Override
    public String visitIfStmt(MiLenguajeParser.IfStmtContext ctx) {
        String cond = visit(ctx.expresion());
        String etiquetaElse = gen.nuevaEtiqueta();
        String etiquetaFin = ctx.ELSE() != null ? gen.nuevaEtiqueta() : etiquetaElse;

        gen.agregar("if !" + cond + " goto " + etiquetaElse);
        visit(ctx.sentencia(0));
        if (ctx.ELSE() != null) {
            gen.agregar("goto " + etiquetaFin);
            gen.agregarEtiqueta(etiquetaElse);
            visit(ctx.sentencia(1));
            gen.agregarEtiqueta(etiquetaFin);
        } else {
            gen.agregarEtiqueta(etiquetaElse);
        }
        return null;
    }

    // While
    @Override
    public String visitWhileStmt(MiLenguajeParser.WhileStmtContext ctx) {
        String etiquetaInicio = gen.nuevaEtiqueta();
        String etiquetaFin = gen.nuevaEtiqueta();
        gen.agregarEtiqueta(etiquetaInicio);
        String cond = visit(ctx.expresion());
        gen.agregar("if !" + cond + " goto " + etiquetaFin);
        gen.pushBreak(etiquetaFin);
        gen.pushContinue(etiquetaInicio);
        visit(ctx.sentencia());
        gen.agregar("goto " + etiquetaInicio);
        gen.agregarEtiqueta(etiquetaFin);
        gen.popBreak();
        gen.popContinue();
        return null;
    }

    // For
    @Override
    public String visitForStmt(MiLenguajeParser.ForStmtContext ctx) {
        String etiquetaCond = gen.nuevaEtiqueta();
        String etiquetaFin = gen.nuevaEtiqueta();

        if (ctx.forInit() != null)
            visit(ctx.forInit());
        gen.agregarEtiqueta(etiquetaCond);
        String cond = ctx.expresion() != null ? visit(ctx.expresion()) : "1";
        gen.agregar("if !" + cond + " goto " + etiquetaFin);
        gen.pushBreak(etiquetaFin);
        gen.pushContinue(etiquetaCond);
        visit(ctx.sentencia());
        if (ctx.asignacion() != null)
            visit(ctx.asignacion());
        gen.agregar("goto " + etiquetaCond);
        gen.agregarEtiqueta(etiquetaFin);
        gen.popBreak();
        gen.popContinue();
        return null;
    }

    // Break
    @Override
    public String visitSentencia(MiLenguajeParser.SentenciaContext ctx) {
        if (ctx.BREAK() != null) {
            gen.agregar("goto " + gen.topBreak());
            return null;
        }
        if (ctx.CONTINUE() != null) {
            gen.agregar("goto " + gen.topContinue());
            return null;
        }
        if (ctx.RETURN() != null) {
            String valor = ctx.expresion() != null ? visit(ctx.expresion()) : "";
            gen.agregar("return" + (valor.isEmpty() ? "" : " " + valor));
            return null;
        }
        // Otros casos: bloque, expresion, etc.
        return super.visitSentencia(ctx);
    }

    // Puedes agregar más métodos visit según lo que necesites...
}