package com.compilador;

import java.util.*;

public class GeneradorCodigo {
    private List<String> instrucciones = new ArrayList<>();
    private int tempCount = 0;
    private int labelCount = 0;
    private Stack<String> pilaBreak = new Stack<>();
    private Stack<String> pilaContinue = new Stack<>();

    public String nuevoTemporal() {
        return "t" + (tempCount++);
    }

    public String nuevaEtiqueta() {
        return "L" + (labelCount++);
    }

    public void agregar(String instruccion) {
        instrucciones.add(instruccion);
    }

    public void agregarEtiqueta(String etiqueta) {
        String etiquetaFormateada = "    " + etiqueta + ":";
        // No agregar si la última instrucción ya es esa etiqueta
        if (!instrucciones.isEmpty() && instrucciones.get(instrucciones.size() - 1).trim().equals(etiqueta + ":")) {
            return;
        }
        // No agregar si la última instrucción también es una etiqueta (evita etiquetas
        // seguidas)
        if (!instrucciones.isEmpty() && instrucciones.get(instrucciones.size() - 1).trim().endsWith(":")) {
            return;
        }
        instrucciones.add(etiquetaFormateada);
    }

    public void pushBreak(String etiqueta) {
        pilaBreak.push(etiqueta);
    }

    public void popBreak() {
        pilaBreak.pop();
    }

    public String topBreak() {
        return pilaBreak.peek();
    }

    public void pushContinue(String etiqueta) {
        pilaContinue.push(etiqueta);
    }

    public void popContinue() {
        pilaContinue.pop();
    }

    public String topContinue() {
        return pilaContinue.peek();
    }

    public void limpiarTemporales() {
        tempCount = 0;
    }

    public void imprimirCodigo() {
        for (int i = 0; i < instrucciones.size(); i++) {
            String instr = instrucciones.get(i);
            if (!instr.trim().endsWith(":")) {
                System.out.printf("%3d: %s\n", i, instr);
            } else {
                System.out.println(instr);
            }
        }
    }

    public void guardarArchivo(String nombreArchivo) throws java.io.IOException {
        try (java.io.PrintWriter out = new java.io.PrintWriter(nombreArchivo)) {
            for (int i = 0; i < instrucciones.size(); i++) {
                String instr = instrucciones.get(i);
                if (!instr.trim().endsWith(":")) {
                    out.printf("%3d: %s\n", i, instr);
                } else {
                    out.println(instr);
                }
            }
        }
    }

    public List<String> getInstrucciones() {
        return instrucciones;
    }
}