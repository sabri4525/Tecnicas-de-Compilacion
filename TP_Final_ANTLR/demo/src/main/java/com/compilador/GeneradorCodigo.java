package com.compilador;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeneradorCodigo {
    private final List<String> instrucciones = new ArrayList<>();
    private int contadorTemporal = 1;
    private int contadorEtiqueta = 1;
    private String ultimoTemporal = "";

    /**
     * Genera un nuevo temporal único
     */
    public String nuevoTemporal() {
        ultimoTemporal = "t" + contadorTemporal++;
        return ultimoTemporal;
    }

    /**
     * Obtiene el último temporal generado
     */
    public String getUltimoTemporal() {
        return ultimoTemporal;
    }

    /**
     * Genera una nueva etiqueta única
     */
    public String nuevaEtiqueta() {
        return "L" + contadorEtiqueta++;
    }

    /**
     * Genera código de asignación
     */
    public void generarAsignacion(String destino, String origen) {
        instrucciones.add(destino + " = " + origen);
    }

    /**
     * Genera código de declaración
     */
    public void generarDeclaracion(String variable, String tipo) {
        instrucciones.add("DECLARE " + tipo + " " + variable);
    }

    /**
     * Genera código de operación aritmética o lógica
     */
    public void generarOperacion(String resultado, String operando1, String operador, String operando2) {
        instrucciones.add(resultado + " = " + operando1 + " " + operador + " " + operando2);
    }

    /**
     * Genera código de salto incondicional
     */
    public void generarSalto(String etiqueta) {
        instrucciones.add("GOTO " + etiqueta);
    }

    /**
     * Genera código de salto condicional
     */
    public void generarSaltoCondicional(String condicion, String operando, String etiqueta) {
        if (condicion.equals("ifFalse")) {
            instrucciones.add("IF_FALSE " + operando + " GOTO " + etiqueta);
        } else {
            instrucciones.add("IF_TRUE " + operando + " GOTO " + etiqueta);
        }
    }

    /**
     * Genera una etiqueta
     */
    public void generarEtiqueta(String etiqueta) {
        instrucciones.add(etiqueta + ":");
    }

    /**
     * Genera código de return
     */
    public void generarReturn(String valor) {
        if (valor != null) {
            instrucciones.add("RETURN " + valor);
        } else {
            instrucciones.add("RETURN");
        }
    }

    /**
     * Genera código para pasar parámetros
     */
    public void generarParametro(String valor) {
        instrucciones.add("PARAM " + valor);
    }

    /**
     * Genera código de llamada a función
     */
    public void generarLlamada(String resultado, String nombreFuncion) {
        instrucciones.add(resultado + " = CALL " + nombreFuncion);
    }

    /**
     * Imprime todo el código generado por consola
     */
    public void imprimirCodigo() {
        System.out.println("\n=== CODIGO INTERMEDIO GENERADO ===");
        for (String instruccion : instrucciones) {
            System.out.println(instruccion);
        }
        System.out.println("=== FIN CODIGO INTERMEDIO ===\n");
    }

    /**
     * Guarda el código generado en un archivo
     */
    public void guardarArchivo(String nombreArchivo) throws IOException {
        try (PrintWriter out = new PrintWriter(nombreArchivo)) {
            out.println("=== CODIGO INTERMEDIO GENERADO ===");
            for (String instruccion : instrucciones) {
                out.println(instruccion);
            }
            out.println("=== FIN CODIGO INTERMEDIO ===");
        }
    }

    /**
     * Obtiene todas las instrucciones generadas
     */
    public List<String> getInstrucciones() {
        return new ArrayList<>(instrucciones);
    }

    /**
     * Limpia todas las instrucciones generadas
     */
    public void limpiar() {
        instrucciones.clear();
        contadorTemporal = 1;
        contadorEtiqueta = 1;
        ultimoTemporal = "";
    }
}