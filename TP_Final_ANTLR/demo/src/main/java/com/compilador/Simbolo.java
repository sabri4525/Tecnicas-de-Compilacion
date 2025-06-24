package com.compilador;

import java.util.ArrayList;
import java.util.List;

public class Simbolo {
    private final String nombre;
    private final String tipo;
    private final String categoria;
    private final int linea;
    private final int columna;
    private final String ambito;
    private final List<String> parametros;
    private boolean usado = false;

    // Constructor original (mantener compatibilidad)
    public Simbolo(String nombre, String tipo, String categoria) {
        this(nombre, tipo, categoria, 0, 0, "global");
    }

    // Nuevo constructor con todos los campos
    public Simbolo(String nombre, String tipo, String categoria, int linea, int columna, String ambito) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.categoria = categoria;
        this.linea = linea;
        this.columna = columna;
        this.ambito = ambito;
        this.parametros = new ArrayList<>();
    }

    // Getters existentes (NO TOCAR)
    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public boolean isUsado() {
        return usado;
    }

    public void marcarComoUsado() {
        this.usado = true;
    }

    // NUEVOS getters para los campos adicionales
    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    public String getAmbito() {
        return ambito;
    }

    public String getParametrosString() {
        if (parametros.isEmpty()) {
            return "N/A";
        }
        return String.join(", ", parametros);
    }

    // Método para agregar parámetros (solo para funciones)
    public void agregarParametro(String parametro) {
        if (categoria.equals("Función")) {
            parametros.add(parametro);
        }
    }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ")";
    }
}