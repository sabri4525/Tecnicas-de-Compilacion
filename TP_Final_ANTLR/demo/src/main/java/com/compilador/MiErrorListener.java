package com.compilador;

import org.antlr.v4.runtime.*;

public class MiErrorListener extends BaseErrorListener {
    // Código ANSI para rojo
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
            Object offendingSymbol,
            int line, int charPositionInLine,
            String msg,
            RecognitionException e) {
        String mensaje = limpiarAcentos(msg);

        // Traducciones básicas de errores comunes
        if (mensaje.contains("missing ';'")) {
            mensaje = mensaje.replace("missing ';'", "Falta ';'");
        }
        if (mensaje.contains("mismatched input")) {
            mensaje = mensaje.replace("mismatched input", "Simbolo inesperado");
        }
        if (mensaje.contains("expecting ')'")) {
            mensaje = mensaje.replace("expecting ')'", "se esperaba ')'");
        }
        if (mensaje.contains("expecting ';'")) {
            mensaje = mensaje.replace("expecting ';'", "se esperaba ';'");
        }
        if (mensaje.contains("at '{'")) {
            mensaje = mensaje.replace("at '{'", "en '{'");
        }

        System.err.println(ANSI_RED +
                "¡Error sintactico encontrado!\n" +
                "Linea " + line + ", columna " + (charPositionInLine + 1) + ":\n" +
                mensaje +
                ANSI_RESET);
    }

    // Método para quitar acentos
    private String limpiarAcentos(String texto) {
        return texto
                .replace("á", "a").replace("é", "e").replace("í", "i")
                .replace("ó", "o").replace("ú", "u")
                .replace("Á", "A").replace("É", "E").replace("Í", "I")
                .replace("Ó", "O").replace("Ú", "U")
                .replace("ñ", "n").replace("Ñ", "N");
    }
}
