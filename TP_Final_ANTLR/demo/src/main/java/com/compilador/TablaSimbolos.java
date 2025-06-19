package com.compilador;

import java.util.*;

public class TablaSimbolos {
    private final Deque<Map<String, String>> pilaScopes = new ArrayDeque<>();

    public TablaSimbolos() {
        pilaScopes.push(new HashMap<>()); // Scope global
    }

    public void entrarScope() {
        pilaScopes.push(new HashMap<>());
    }

    public void salirScope() {
        pilaScopes.pop();
    }

    public boolean declarar(String nombre, String tipo) {
        Map<String, String> actual = pilaScopes.peek();
        if (actual.containsKey(nombre)) {
            return false; // Ya existe en este scope
        }
        actual.put(nombre, tipo);
        return true;
    }

    public boolean existe(String nombre) {
        for (Map<String, String> scope : pilaScopes) {
            if (scope.containsKey(nombre))
                return true;
        }
        return false;
    }

    public String obtenerTipo(String nombre) {
        for (Map<String, String> scope : pilaScopes) {
            if (scope.containsKey(nombre))
                return scope.get(nombre);
        }
        return null;
    }
}
