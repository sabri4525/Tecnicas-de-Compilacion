package com.compilador;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.gui.TreeViewer;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java -jar demo-1.0-jar-with-dependencies.jar <archivo.txt>");
            System.exit(1);
        }

        try {
            System.out.println("Analizando archivo: " + args[0]);

            CharStream inputLexico = CharStreams.fromFileName(args[0]);
            realizarAnalisisLexico(inputLexico);

            CharStream inputSintactico = CharStreams.fromFileName(args[0]);
            realizarAnalisisSintactico(inputSintactico);

        } catch (IOException e) {
            System.err.println("❌ Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void realizarAnalisisLexico(CharStream input) {
        MiLenguajeLexer lexer = new MiLenguajeLexer(input);

        List<String> errores = new ArrayList<>();
        lexer.removeErrorListeners();
        lexer.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                    int line, int charPositionInLine, String msg, RecognitionException e) {
                String errorMsg = "ERROR LÉXICO en línea " + line + ":" + charPositionInLine +
                        " - " + msg;
                errores.add(errorMsg);
                throw new RuntimeException(errorMsg);
            }
        });

        try {
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();

            System.out.println("\n=== ANALISIS LEXICO ===");
            System.out.printf("%-20s %-30s %-10s %-10s\n", "TIPO", "LEXEMA", "LÍNEA", "COLUMNA");
            System.out.println("-------------------------------------------------------------------");

            for (Token token : tokens.getTokens()) {
                if (token.getType() != Token.EOF) {
                    String tokenName = MiLenguajeLexer.VOCABULARY.getSymbolicName(token.getType());
                    System.out.printf("%-20s %-30s %-10d %-10d\n",
                            tokenName, token.getText(), token.getLine(),
                            token.getCharPositionInLine());
                }
            }

            System.out.println("\n Análisis léxico completado sin errores.");

        } catch (RuntimeException e) {
            System.out.println("\n " + e.getMessage());
        }
    }

    private static void realizarAnalisisSintactico(CharStream input) {
        MiLenguajeLexer lexer = new MiLenguajeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiLenguajeParser parser = new MiLenguajeParser(tokens);

        parser.removeErrorListeners();
        parser.addErrorListener(new MiErrorListener()); // Usa tu listener personalizado

        try {
            System.out.println("\n=== ANALISIS SINTACTICO ===");
            System.out.println("Intentando analizar el archivo como programa...");

            ParseTree tree = parser.programa(); // Asegúrate de que "programa" sea la regla inicial

            System.out.println("\n Analisis sintactico completado sin errores.");
            System.out.println("Representacion textual del arbol sintactico:");
            System.out.println(tree.toStringTree(parser));

            // Recorrido del árbol con visitor
            System.out.println("\nAnalizando árbol sintáctico...");
            System.out.println("Arbol sintáctico generado correctamente. Iniciando análisis semántico...");
            ExprVisitor visitor = new ExprVisitor();
            visitor.visit(tree);

            // Visualización gráfica del árbol
            generarImagenArbolSintactico(tree, parser);

            System.out.println("\n=== ANALISIS SEMANTICO ===");
            realizarAnalisisSemantico(tree); // Análisis semántico después del sintáctico

        } catch (RuntimeException e) {
            System.out.println("\n " + e.getMessage());
            System.out.println("El archivo no pudo ser analizado como programa valido.");
        }
    }

    private static void generarImagenArbolSintactico(ParseTree tree, MiLenguajeParser parser) {
        try {
            JFrame frame = new JFrame("Árbol Sintáctico");
            JPanel panel = new JPanel();

            TreeViewer viewer = new TreeViewer(Arrays.asList(parser.getRuleNames()), tree);
            viewer.setScale(1.5);
            panel.add(viewer);

            JScrollPane scrollPane = new JScrollPane(panel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            frame.add(scrollPane);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setVisible(true);

        } catch (Exception e) {
            System.out.println(" Error al generar la imagen del árbol: " + e.getMessage());
        }
    }

    public static void generarCodigoIntermedio(ParseTree arbol) {
        if (arbol == null) {
            System.err.println("No se puede generar código intermedio: árbol sintáctico nulo.");
            return;
        }
        System.out.println("Generando código intermedio...");
        GeneradorCodigo generador = new GeneradorCodigo();
        CodigoVisitor visitor = new CodigoVisitor(generador);
        visitor.visit(arbol);
        generador.imprimirCodigo();
        try {
            generador.guardarArchivo("codigo_intermedio.txt");
            System.out.println("Código intermedio generado correctamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar el código intermedio: " + e.getMessage());
        }
    }

    public static void realizarAnalisisSemantico(ParseTree tree) {
        AnalizadorSemanticoVisitor visitor = new AnalizadorSemanticoVisitor();
        visitor.visit(tree);

        // Verificar variables no usadas al final
        visitor.verificarVariablesNoUsadas();

        // Mostrar errores
        if (!visitor.getErroresSemanticos().isEmpty()) {
            System.out.println("\u001B[31mErrores semánticos encontrados:\u001B[0m");
            for (String error : visitor.getErroresSemanticos()) {
                System.out.println("\u001B[31m" + error + "\u001B[0m");
            }
        }

        // Mostrar warnings
        if (!visitor.getWarningsSemanticos().isEmpty()) {
            System.out.println("\u001B[33mWarnings semánticos encontrados:\u001B[0m");
            for (String warning : visitor.getWarningsSemanticos()) {
                System.out.println("\u001B[33m" + warning + "\u001B[0m");
            }
        }

        if (visitor.getErroresSemanticos().isEmpty() && visitor.getWarningsSemanticos().isEmpty()) {
            System.out.println("\u001B[32mAnálisis semántico completado sin errores ni warnings.\u001B[0m");
        }

        // *** SOLO GENERAR CÓDIGO SI NO HAY ERRORES ***
        if (visitor.getErroresSemanticos().isEmpty()) {
            System.out.println("\n=== GENERACION DE CODIGO INTERMEDIO ===");
            generarCodigoIntermedio(tree);
        } else {
            System.out.println("\n\u001B[31mNo se generó código intermedio debido a errores semánticos.\u001B[0m");
        }
    }
}

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
        indentLevel++;
        for (ParseTree child : ctx.children) {
            visit(child);
        }
        indentLevel--;
        return null;
    }
}
