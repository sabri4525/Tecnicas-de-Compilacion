package com.compilador;

import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {
    private final Map<String, String> simbolos = new HashMap<>();

    public boolean declarar(String nombre, String tipo) {
        if (simbolos.containsKey(nombre)) {
            return false; // Ya declarado
        }
        simbolos.put(nombre, tipo);
        return true;
    }

    public boolean existe(String nombre) {
        return simbolos.containsKey(nombre);
    }

    public String obtenerTipo(String nombre) {
        return simbolos.get(nombre);
    }
}
