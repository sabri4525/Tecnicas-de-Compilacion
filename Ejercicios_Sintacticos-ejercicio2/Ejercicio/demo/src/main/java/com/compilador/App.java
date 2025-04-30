package com.compilador;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.gui.TreeViewer;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java -jar demo-1.0-jar-with-dependencies.jar <archivo.txt>");
            System.exit(1);
        }

        try {
            // Cargar el archivo de entrada
            System.out.println("Analizando archivo: " + args[0]);

            CharStream input = CharStreams.fromFileName(args[0]);
            realizarAnalisis(input);
        } catch (IOException e) {
            System.err.println("❌ Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void realizarAnalisis(CharStream input) {
        MiLenguajeLexer lexer = new MiLenguajeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiLenguajeParser parser = new MiLenguajeParser(tokens);

        try {
            System.out.println("\n=== ANÁLISIS SINTÁCTICO ===");
            ParseTree tree = parser.programa(); // Analizar usando la regla inicial 'programa'

            System.out.println("\n✅ Análisis sintáctico completado sin errores.");
            System.out.println("Árbol sintáctico:");
            System.out.println(tree.toStringTree(parser));

            // Usar el visitor para recorrer el árbol
            ExprVisitor visitor = new ExprVisitor();
            visitor.visit(tree);

            // Mostrar el árbol gráficamente
            mostrarArbol(tree, parser);
        } catch (Exception e) {
            System.err.println("❌ Error durante el análisis: " + e.getMessage());
        }
    }

    private static void mostrarArbol(ParseTree tree, MiLenguajeParser parser) {
        JFrame frame = new JFrame("Árbol Sintáctico");
        JPanel panel = new JPanel();
        TreeViewer viewer = new TreeViewer(Arrays.asList(parser.getRuleNames()), tree);
        viewer.setScale(1.5);
        panel.add(viewer);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}

// Visitor para recorrer el árbol sintáctico
class ExprVisitor extends MiLenguajeBaseVisitor<Void> {
    private int indentLevel = 0;

    private void indent() {
        for (int i = 0; i < indentLevel; i++) {
            System.out.print("  ");
        }
    }

    @Override
    public Void visitPrograma(MiLenguajeParser.ProgramaContext ctx) {
        indent();
        System.out.println("Programa:");
        return visit(ctx.s());
    }

    @Override
    public Void visitAsignacion(MiLenguajeParser.AsignacionContext ctx) {
        indent();
        System.out.println("Asignación: " + ctx.ID().getText() + " = ");
        indentLevel++;
        visit(ctx.e());
        indentLevel--;
        return null;
    }

    @Override
    public Void visitSentenciaIf(MiLenguajeParser.SentenciaIfContext ctx) {
        indent();
        System.out.println("Sentencia If:");
        indentLevel++;
        visit(ctx.e());
        visit(ctx.s());
        indentLevel--;
        return null;
    }

    @Override
    public Void visitSentenciaVacia(MiLenguajeParser.SentenciaVaciaContext ctx) {
        indent();
        System.out.println("Sentencia vacía");
        return null;
    }

    @Override
    public Void visitSuma(MiLenguajeParser.SumaContext ctx) {
        indent();
        System.out.println("Suma:");
        indentLevel++;
        visit(ctx.e());
        visit(ctx.t());
        indentLevel--;
        return null;
    }

    @Override
    public Void visitTermino(MiLenguajeParser.TerminoContext ctx) {
        return visit(ctx.t());
    }

    @Override
    public Void visitIdentificador(MiLenguajeParser.IdentificadorContext ctx) {
        indent();
        System.out.println("Identificador: " + ctx.ID().getText());
        return null;
    }

    @Override
    public Void visitNumero(MiLenguajeParser.NumeroContext ctx) {
        indent();
        System.out.println("Número: " + ctx.NUM().getText());
        return null;
    }

    @Override
    public Void visitParens(MiLenguajeParser.ParensContext ctx) {
        indent();
        System.out.println("Paréntesis:");
        indentLevel++;
        visit(ctx.e());
        indentLevel--;
        return null;
    }
}
