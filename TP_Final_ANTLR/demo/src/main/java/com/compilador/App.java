package com.compilador;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.IOException;
import javax.swing.*;
import org.antlr.v4.gui.TreeViewer;

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: java -jar demo.jar <archivo>");
            return;
        }

        String nombreArchivo = args[0];

        try {
            // FASE 1: ANÁLISIS LÉXICO
            System.out.println("=== ANALISIS LEXICO ===");
            CharStream input = CharStreams.fromFileName(nombreArchivo);
            realizarAnalisisLexico(input);

            // FASE 2: ANÁLISIS SINTÁCTICO
            System.out.println("\n=== ANALISIS SINTACTICO ===");
            ParseTree tree = realizarAnalisisSintactico(CharStreams.fromFileName(nombreArchivo));

            if (tree != null) {
                // FASE 3: ANÁLISIS SEMÁNTICO
                System.out.println("\n=== ANALISIS SEMANTICO ===");
                realizarAnalisisSemantico(tree);
            }

        } catch (Exception e) {
            System.err.println("Error al procesar el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * FASE 1: Realiza el análisis léxico y muestra los tokens
     */
    public static void realizarAnalisisLexico(CharStream input) {
        MiLenguajeLexer lexer = new MiLenguajeLexer(input);

        // Agregar listener personalizado para errores léxicos
        final boolean[] hayErroresLexicos = { false };
        lexer.removeErrorListeners();
        lexer.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                    int line, int charPositionInLine, String msg, RecognitionException e) {
                hayErroresLexicos[0] = true;
                System.err.println("\u001B[31m¡Error léxico encontrado!\u001B[0m");
                System.err.println("Línea " + line + ", columna " + charPositionInLine + ": " + msg);
            }
        });

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill(); // Llenar el stream de tokens

        System.out.println("Tokens encontrados:");
        System.out.println("┌──────────────────────┬──────────────────────┬─────────┬─────────┐");
        System.out.println("│ TIPO                 │ TEXTO                │ LÍNEA   │ COLUMNA │");
        System.out.println("├──────────────────────┼──────────────────────┼─────────┼─────────┤");

        for (Token token : tokens.getTokens()) {
            if (token.getType() != Token.EOF) {
                String tipoToken = lexer.getVocabulary().getSymbolicName(token.getType());
                if (tipoToken == null) {
                    tipoToken = "'" + token.getText() + "'";
                }

                System.out.printf("│ %-20s │ %-20s │ %-7d │ %-7d │\n",
                        tipoToken,
                        token.getText().length() > 20 ? token.getText().substring(0, 17) + "..." : token.getText(),
                        token.getLine(),
                        token.getCharPositionInLine());
            }
        }

        System.out.println("└──────────────────────┴──────────────────────┴─────────┴─────────┘");
        System.out.println("Total de tokens: " + (tokens.getTokens().size() - 1)); // -1 para no contar EOF

        // Verificar errores léxicos
        if (hayErroresLexicos[0]) {
            System.err.println("\u001B[31m¡Errores léxicos encontrados!\u001B[0m");
        } else {
            System.out.println("\u001B[32mAnálisis léxico completado sin errores.\u001B[0m");
        }
    }

    /**
     * FASE 2: Realiza el análisis sintáctico y construye el árbol
     */
    public static ParseTree realizarAnalisisSintactico(CharStream input) {
        MiLenguajeLexer lexer = new MiLenguajeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiLenguajeParser parser = new MiLenguajeParser(tokens);

        // Agregar listener personalizado para errores sintácticos
        parser.removeErrorListeners(); // Remover el listener por defecto
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                    int line, int charPositionInLine, String msg, RecognitionException e) {
                System.err.println("\u001B[31m¡Error sintáctico encontrado!\u001B[0m");
                System.err.println("Línea " + line + ", columna " + charPositionInLine + ":");
                System.err.println("Símbolo inesperado '" + offendingSymbol + "' " + msg);
            }
        });

        // Construir el árbol sintáctico
        ParseTree tree = parser.programa();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            System.err.println("\u001B[31mSe encontraron errores sintácticos. No se puede continuar.\u001B[0m");
            return null;
        }

        if (tree == null) {
            System.err.println("\u001B[31mError: No se pudo construir el arbol sintactico.\u001B[0m");
            return null;
        }

        System.out.println("\u001B[32mAnálisis sintáctico completado exitosamente.\u001B[0m");
        System.out.println("Arbol sintactico construido correctamente.");

        // Mostrar representación textual del árbol
        mostrarDiagramaArbol(tree, parser);

        return tree;
    }

    /**
     * FASE 3: Ejecuta el análisis semántico verificando errores y warnings
     */
    public static void realizarAnalisisSemantico(ParseTree tree) {
        AnalizadorSemanticoVisitor visitor = new AnalizadorSemanticoVisitor();
        visitor.visit(tree);

        // Verificar variables no usadas
        visitor.verificarVariablesNoUsadas();

        // Mostrar errores semánticos
        if (!visitor.getErroresSemanticos().isEmpty()) {
            System.out.println("\n=== ERRORES SEMÁNTICOS ===");
            for (String error : visitor.getErroresSemanticos()) {
                System.err.println("\u001B[31m" + error + "\u001B[0m");
            }
        }

        // Mostrar warnings semánticos
        if (!visitor.getWarningsSemanticos().isEmpty()) {
            System.out.println("\n=== WARNINGS SEMANTICOS ===");
            for (String warning : visitor.getWarningsSemanticos()) {
                System.out.println("\u001B[33m" + warning + "\u001B[0m");
            }
        }

        // Resumen del análisis
        if (visitor.getErroresSemanticos().isEmpty() && visitor.getWarningsSemanticos().isEmpty()) {
            System.out.println("\u001B[32mAnálisis semántico completado sin errores ni warnings.\u001B[0m");
        } else if (visitor.getErroresSemanticos().isEmpty()) {
            System.out.println("\u001B[32mAnálisis semántico completado sin errores.\u001B[0m");
            System.out.println(
                    "\u001B[33mSe encontraron " + visitor.getWarningsSemanticos().size() + " warnings.\u001B[0m");
        } else {
            System.out.println("\u001B[31mSe encontraron " + visitor.getErroresSemanticos().size()
                    + " errores semánticos.\u001B[0m");
            if (!visitor.getWarningsSemanticos().isEmpty()) {
                System.out.println("\u001B[33mTambién se encontraron " + visitor.getWarningsSemanticos().size()
                        + " warnings.\u001B[0m");
            }
        }

        // Mostrar y guardar tabla de símbolos
        TablaSimbolos tablaSimbolos = visitor.getTablaSimbolos();
        tablaSimbolos.imprimirTabla();

        try {
            tablaSimbolos.guardarArchivo("tabla_simbolos.txt");
            System.out.println("Tabla de simbolos guardada en 'tabla_simbolos.txt'");
        } catch (IOException e) {
            System.err.println("Error al guardar la tabla de simbolos: " + e.getMessage());
        }

        // FASE 4: Solo genera código intermedio si no hay errores
        if (visitor.getErroresSemanticos().isEmpty()) {
            System.out.println("\n=== GENERACION DE CODIGO INTERMEDIO ===");
            generarCodigoIntermedio(tree);
        } else {
            System.out.println("\n\u001B[31mNo se genero codigo intermedio debido a errores semanticos.\u001B[0m");
        }
    }

    /**
     * FASE 4: Coordina la generación de código intermedio y su almacenamiento
     */
    public static void generarCodigoIntermedio(ParseTree arbol) {
        // Verificación adicional de seguridad
        if (arbol == null) {
            System.err.println("No se puede generar codigo intermedio: arbol sintactico nulo.");
            return;
        }

        System.out.println("Generando codigo intermedio...");
        GeneradorCodigo generador = new GeneradorCodigo();
        CodigoVisitor visitor = new CodigoVisitor(generador);
        visitor.visit(arbol);
        generador.imprimirCodigo();

        try {
            generador.guardarArchivo("codigo_intermedio.txt");
            System.out.println("Codigo intermedio guardado en 'codigo_intermedio.txt'");
            System.out.println("\u001B[32mCodigo intermedio generado correctamente.\u001B[0m");
        } catch (IOException e) {
            System.err.println("Error al guardar el codigo intermedio: " + e.getMessage());
        }
    }

    /**
     * Muestra el diagrama visual del árbol sintáctico en una ventana
     */
    private static void mostrarDiagramaArbol(ParseTree tree, MiLenguajeParser parser) {
        try {
            // Crear el visor del árbol
            TreeViewer viewer = new TreeViewer(java.util.Arrays.asList(parser.getRuleNames()), tree);
            viewer.setScale(1.5);

            // Crear la ventana
            JFrame frame = new JFrame("Arbol Sintactico");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            // Agregar el visor con scroll
            JScrollPane scrollPane = new JScrollPane(viewer);
            frame.add(scrollPane);

            // Mostrar la ventana
            frame.setVisible(true);

            System.out.println("Diagrama del arbol sintactico abierto en nueva ventana.");

        } catch (Exception e) {
            System.err.println("Error al mostrar el diagrama del arbol: " + e.getMessage());
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
