package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Environment {
    private final Map<String, Object> valuesMap = new HashMap<>();
    private final List<Object> values = new ArrayList<>();
    private final Environment enclosing;

    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    Object get(Token name) {
        if (valuesMap.containsKey(name.lexeme)) {
            return valuesMap.get(name.lexeme);
        }
        if (enclosing != null)
            return enclosing.get(name);
        throw new RuntimeError(name,
                "Undefined variable '" + name.lexeme + "'.");
    }

    void define(String name, Object value) {
        if(enclosing == null) {
            valuesMap.put(name, value);
        } else {
            values.add(value);
        }
    }

    Object getAt(int distance, int index) {
        return ancestor(distance).values.get(index);
    }

    void assignAt(Integer distance, Integer index, Object value) {
        ancestor(distance).values.add(index, value);
    }

    Environment ancestor(int distance) {
        Environment environment = this;
        for (int i = 0; i < distance; i++) {
            environment = environment.enclosing;
        }

        return environment;
    }

    void assign(Token name, Object value) {
        if (valuesMap.get(name.lexeme) != null) {
            valuesMap.put(name.lexeme, value);
            return;
        }
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }
        throw new RuntimeError(name,
                "Undefined variable '" + name.lexeme + "'.");
    }
}