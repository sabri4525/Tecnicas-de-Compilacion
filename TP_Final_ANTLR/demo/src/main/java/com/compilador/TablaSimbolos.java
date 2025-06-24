package com.compilador;

import java.util.*;
import java.io.PrintWriter;
import java.io.IOException;

public class TablaSimbolos {
    private final Deque<Map<String, String>> pilaScopes = new ArrayDeque<>();
    private final List<EntradaTabla> todasLasEntradas = new ArrayList<>();

    // Para almacenar información de funciones
    public final Map<String, FuncionInfo> funciones = new HashMap<>();

    private String ambitoActual = "global";

    public TablaSimbolos() {
        pilaScopes.push(new HashMap<>()); // Scope global
    }

    public void entrarScope() {
        pilaScopes.push(new HashMap<>());
    }

    public void salirScope() {
        pilaScopes.pop();
    }

    /**
     * Verifica si una variable existe en cualquier scope
     */
    public boolean existe(String nombre) {
        for (Map<String, String> scope : pilaScopes) {
            if (scope.containsKey(nombre))
                return true;
        }
        return false;
    }

    /**
     * Obtiene el tipo de una variable
     */
    public String obtenerTipo(String nombre) {
        for (Map<String, String> scope : pilaScopes) {
            if (scope.containsKey(nombre))
                return scope.get(nombre);
        }
        return null;
    }

    /**
     * Declara un parámetro con información de posición
     */
    public boolean declararParametro(String nombre, String tipo, int linea, int columna) {
        Map<String, String> actual = pilaScopes.peek();
        if (actual.containsKey(nombre)) {
            return false;
        }
        actual.put(nombre, tipo);

        todasLasEntradas.add(new EntradaTabla(nombre, tipo, "Parámetro", linea, columna, getAmbitoActual(), "N/A"));
        return true;
    }

    /**
     * Declara una variable con información de posición
     */
    public boolean declarar(String nombre, String tipo, int linea, int columna) {
        Map<String, String> actual = pilaScopes.peek();
        if (actual.containsKey(nombre)) {
            return false; // Ya existe en este scope
        }
        actual.put(nombre, tipo);

        // Guarda entrada para el reporte final CON línea y columna
        todasLasEntradas.add(new EntradaTabla(nombre, tipo, "Variable", linea, columna, getAmbitoActual(), "N/A"));

        return true;
    }

    /**
     * Obtiene información completa de una función
     */
    public FuncionInfo obtenerInfoFuncion(String nombre) {
        return funciones.get(nombre);
    }

    /**
     * Verifica si existe una función con el nombre dado
     */
    public boolean existeFuncion(String nombre) {
        return funciones.containsKey(nombre);
    }

    /**
     * Imprime la tabla de símbolos con columnas que se adaptan al contenido
     */
    public void imprimirTabla() {
        if (todasLasEntradas.isEmpty()) {
            System.out.println("\n=== TABLA DE SIMBOLOS VACÍA ===");
            return;
        }

        // Calcular anchos máximos para cada columna
        int maxNombre = Math.max(6, "NOMBRE".length());
        int maxTipo = Math.max(4, "TIPO".length());
        int maxCategoria = Math.max(9, "CATEGORIA".length());
        int maxLinea = Math.max(5, "LINEA".length());
        int maxColumna = Math.max(7, "COLUMNA".length());
        int maxAmbito = Math.max(6, "AMBITO".length());
        int maxParametros = Math.max(10, "PARAMETROS".length());

        for (EntradaTabla entrada : todasLasEntradas) {
            maxNombre = Math.max(maxNombre, entrada.getNombre().length());
            maxTipo = Math.max(maxTipo, entrada.getTipo().length());
            maxCategoria = Math.max(maxCategoria, entrada.getCategoria().length());
            maxLinea = Math.max(maxLinea, String.valueOf(entrada.getLinea()).length());
            maxColumna = Math.max(maxColumna, String.valueOf(entrada.getColumna()).length());
            maxAmbito = Math.max(maxAmbito, entrada.getAmbito().length());
            maxParametros = Math.max(maxParametros, entrada.getParametros().length());
        }

        System.out.println("\n=== TABLA DE SIMBOLOS ===");
        imprimirLineaHorizontal(maxNombre, maxTipo, maxCategoria, maxLinea, maxColumna, maxAmbito, maxParametros, "┌",
                "┬", "┐");
        imprimirLineaEncabezado(maxNombre, maxTipo, maxCategoria, maxLinea, maxColumna, maxAmbito, maxParametros);
        imprimirLineaHorizontal(maxNombre, maxTipo, maxCategoria, maxLinea, maxColumna, maxAmbito, maxParametros, "├",
                "┼", "┤");

        for (EntradaTabla entrada : todasLasEntradas) {
            System.out.printf(
                    "│ %-" + maxNombre + "s │ %-" + maxTipo + "s │ %-" + maxCategoria + "s │ %" + maxLinea + "d │ %"
                            + maxColumna + "d │ %-" + maxAmbito + "s │ %-" + maxParametros + "s │\n",
                    entrada.getNombre(),
                    entrada.getTipo(),
                    entrada.getCategoria(),
                    entrada.getLinea(),
                    entrada.getColumna(),
                    entrada.getAmbito(),
                    entrada.getParametros());
        }

        imprimirLineaHorizontal(maxNombre, maxTipo, maxCategoria, maxLinea, maxColumna, maxAmbito, maxParametros, "└",
                "┴", "┘");
        System.out.println("Total de símbolos: " + todasLasEntradas.size());
    }

    /**
     * Guarda la tabla de símbolos en un archivo con el mismo formato que la consola
     */
    public void guardarArchivo(String nombreArchivo) throws IOException {
        try (PrintWriter out = new PrintWriter(nombreArchivo)) {
            if (todasLasEntradas.isEmpty()) {
                out.println("=== TABLA DE SIMBOLOS VACÍA ===");
                return;
            }

            // Calcular anchos máximos (mismo código que imprimirTabla)
            int maxNombre = Math.max(6, "NOMBRE".length());
            int maxTipo = Math.max(4, "TIPO".length());
            int maxCategoria = Math.max(9, "CATEGORIA".length());
            int maxLinea = Math.max(5, "LINEA".length());
            int maxColumna = Math.max(7, "COLUMNA".length());
            int maxAmbito = Math.max(6, "AMBITO".length());
            int maxParametros = Math.max(10, "PARAMETROS".length());

            for (EntradaTabla entrada : todasLasEntradas) {
                maxNombre = Math.max(maxNombre, entrada.getNombre().length());
                maxTipo = Math.max(maxTipo, entrada.getTipo().length());
                maxCategoria = Math.max(maxCategoria, entrada.getCategoria().length());
                maxLinea = Math.max(maxLinea, String.valueOf(entrada.getLinea()).length());
                maxColumna = Math.max(maxColumna, String.valueOf(entrada.getColumna()).length());
                maxAmbito = Math.max(maxAmbito, entrada.getAmbito().length());
                maxParametros = Math.max(maxParametros, entrada.getParametros().length());
            }

            out.println("=== TABLA DE SIMBOLOS ===");
            guardarLineaHorizontal(out, maxNombre, maxTipo, maxCategoria, maxLinea, maxColumna, maxAmbito,
                    maxParametros, "┌", "┬", "┐");
            guardarLineaEncabezado(out, maxNombre, maxTipo, maxCategoria, maxLinea, maxColumna, maxAmbito,
                    maxParametros);
            guardarLineaHorizontal(out, maxNombre, maxTipo, maxCategoria, maxLinea, maxColumna, maxAmbito,
                    maxParametros, "├", "┼", "┤");

            for (EntradaTabla entrada : todasLasEntradas) {
                out.printf(
                        "│ %-" + maxNombre + "s │ %-" + maxTipo + "s │ %-" + maxCategoria + "s │ %" + maxLinea + "d │ %"
                                + maxColumna + "d │ %-" + maxAmbito + "s │ %-" + maxParametros + "s │\n",
                        entrada.getNombre(),
                        entrada.getTipo(),
                        entrada.getCategoria(),
                        entrada.getLinea(),
                        entrada.getColumna(),
                        entrada.getAmbito(),
                        entrada.getParametros());
            }

            guardarLineaHorizontal(out, maxNombre, maxTipo, maxCategoria, maxLinea, maxColumna, maxAmbito,
                    maxParametros, "└", "┴", "┘");
            out.println("Total de símbolos: " + todasLasEntradas.size());
        }
    }

    /**
     * Clase para almacenar información de funciones
     */
    public static class FuncionInfo {
        public final String tipoRetorno;
        public final List<String> tiposParametros;

        public FuncionInfo(String tipoRetorno, List<String> tiposParametros) {
            this.tipoRetorno = tipoRetorno;
            this.tiposParametros = new ArrayList<>(tiposParametros);
        }

        public int getNumeroParametros() {
            return tiposParametros.size();
        }

        public String getTipoParametro(int indice) {
            if (indice >= 0 && indice < tiposParametros.size()) {
                return tiposParametros.get(indice);
            }
            return null;
        }
    }

    /**
     * Clase interna para representar entradas en la tabla de símbolos
     */
    /**
     * Clase que representa una entrada en la tabla de símbolos con todos los campos
     * requeridos
     */
    static class EntradaTabla {
        private final String nombre;
        private final String tipo;
        private final String categoria;
        private final int linea;
        private final int columna;
        private final String ambito;
        private final String parametros;
        private boolean usado = false;

        public EntradaTabla(String nombre, String tipo, String categoria, int linea, int columna, String ambito,
                String parametros) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.categoria = categoria;
            this.linea = linea;
            this.columna = columna;
            this.ambito = ambito != null ? ambito : "global";
            this.parametros = parametros != null ? parametros : "N/A";
        }

        // Getters
        public String getNombre() {
            return nombre;
        }

        public String getTipo() {
            return tipo;
        }

        public String getCategoria() {
            return categoria;
        }

        public int getLinea() {
            return linea;
        }

        public int getColumna() {
            return columna;
        }

        public String getAmbito() {
            return ambito;
        }

        public String getParametros() {
            return parametros;
        }

        public boolean isUsado() {
            return usado;
        }

        public void marcarComoUsado() {
            this.usado = true;
        }

        @Override
        public String toString() {
            return nombre + " (" + tipo + ")";
        }
    }

    /**
     * Establece el ámbito actual
     */
    public void setAmbitoActual(String ambito) {
        this.ambitoActual = ambito;
    }

    /**
     * Obtiene el ámbito actual
     */
    public String getAmbitoActual() {
        return ambitoActual;
    }

    /**
     * Entra a un nuevo ámbito de función
     */
    public void entrarAmbitoFuncion(String nombreFuncion) {
        entrarScope();
        this.ambitoActual = nombreFuncion;
    }

    /**
     * Sale del ámbito actual y vuelve al anterior
     */
    public void salirAmbitoFuncion() {
        salirScope();
        this.ambitoActual = "global";
    }

    /**
     * Agrega un símbolo a la tabla con todos los campos requeridos
     */
    public boolean agregarSimbolo(String nombre, String tipo, String categoria, int linea, int columna, String ambito,
            String parametros) {
        if (nombre == null || tipo == null || categoria == null) {
            return false;
        }

        // Verificar si ya existe en el scope actual
        for (EntradaTabla entrada : todasLasEntradas) {
            if (entrada.getNombre().equals(nombre) && entrada.getAmbito().equals(ambito)) {
                return false; // Ya existe
            }
        }

        EntradaTabla nuevaEntrada = new EntradaTabla(nombre, tipo, categoria, linea, columna, ambito, parametros);
        todasLasEntradas.add(nuevaEntrada);
        return true;
    }

    /**
     * Sobrecarga para compatibilidad con código que no especifica todos los campos
     */
    public boolean agregarSimbolo(String nombre, String tipo, String categoria) {
        if (nombre == null || tipo == null || categoria == null) {
            return false;
        }

        // Verificar si ya existe
        for (EntradaTabla entrada : todasLasEntradas) {
            if (entrada.getNombre().equals(nombre)) {
                return false;
            }
        }

        // Crear entrada con valores por defecto para los campos nuevos
        EntradaTabla nuevaEntrada = new EntradaTabla(nombre, tipo, categoria, 0, 0, "global", "N/A");
        todasLasEntradas.add(nuevaEntrada);
        return true;
    }

    /**
     * Agrega información de función después de agregarla como símbolo
     */
    public void agregarInfoFuncion(String nombre, String tipoRetorno, List<String> tiposParametros) {
        funciones.put(nombre, new FuncionInfo(tipoRetorno, tiposParametros));
    }

    /**
     * Imprime una línea horizontal de la tabla
     */
    private void imprimirLineaHorizontal(int maxNombre, int maxTipo, int maxCategoria, int maxLinea, int maxColumna,
            int maxAmbito, int maxParametros, String inicio, String separador, String fin) {
        System.out.print(inicio);
        System.out.print("─".repeat(maxNombre + 2));
        System.out.print(separador);
        System.out.print("─".repeat(maxTipo + 2));
        System.out.print(separador);
        System.out.print("─".repeat(maxCategoria + 2));
        System.out.print(separador);
        System.out.print("─".repeat(maxLinea + 2));
        System.out.print(separador);
        System.out.print("─".repeat(maxColumna + 2));
        System.out.print(separador);
        System.out.print("─".repeat(maxAmbito + 2));
        System.out.print(separador);
        System.out.print("─".repeat(maxParametros + 2));
        System.out.println(fin);
    }

    /**
     * Imprime la línea de encabezados
     */
    private void imprimirLineaEncabezado(int maxNombre, int maxTipo, int maxCategoria, int maxLinea, int maxColumna,
            int maxAmbito, int maxParametros) {
        System.out.printf(
                "│ %-" + maxNombre + "s │ %-" + maxTipo + "s │ %-" + maxCategoria + "s │ %" + maxLinea + "s │ %"
                        + maxColumna + "s │ %-" + maxAmbito + "s │ %-" + maxParametros + "s │\n",
                "NOMBRE", "TIPO", "CATEGORIA", "LINEA", "COLUMNA", "AMBITO", "PARAMETROS");
    }

    /**
     * Guarda una línea horizontal en el archivo
     */
    private void guardarLineaHorizontal(PrintWriter out, int maxNombre, int maxTipo, int maxCategoria, int maxLinea,
            int maxColumna, int maxAmbito, int maxParametros, String inicio, String separador, String fin) {
        out.print(inicio);
        out.print("─".repeat(maxNombre + 2));
        out.print(separador);
        out.print("─".repeat(maxTipo + 2));
        out.print(separador);
        out.print("─".repeat(maxCategoria + 2));
        out.print(separador);
        out.print("─".repeat(maxLinea + 2));
        out.print(separador);
        out.print("─".repeat(maxColumna + 2));
        out.print(separador);
        out.print("─".repeat(maxAmbito + 2));
        out.print(separador);
        out.print("─".repeat(maxParametros + 2));
        out.println(fin);
    }

    /**
     * Guarda la línea de encabezados en el archivo
     */
    private void guardarLineaEncabezado(PrintWriter out, int maxNombre, int maxTipo, int maxCategoria, int maxLinea,
            int maxColumna, int maxAmbito, int maxParametros) {
        out.printf("│ %-" + maxNombre + "s │ %-" + maxTipo + "s │ %-" + maxCategoria + "s │ %" + maxLinea + "s │ %"
                + maxColumna + "s │ %-" + maxAmbito + "s │ %-" + maxParametros + "s │\n",
                "NOMBRE", "TIPO", "CATEGORIA", "LINEA", "COLUMNA", "AMBITO", "PARAMETROS");
    }
}
