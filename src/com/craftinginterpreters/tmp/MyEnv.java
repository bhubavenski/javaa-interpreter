package com.craftinginterpreters.tmp;

import java.util.HashMap;
import java.util.Map;

public class MyEnv {
    private final Map<String, Object> values = new HashMap<>();
    final MyEnv enclosing;

    MyEnv() {
        this.enclosing = null;
    }

    MyEnv(MyEnv enclosing) {
        this.enclosing = enclosing;
    }

    public void define(String name, Object value) {
        values.put(name, value);
    }

    public Object get(MyToken token) {
        if (values.containsKey(token.lexeme))
            return values.get(token.lexeme);
        if (enclosing != null) {
            return enclosing.get(token);
        }
        throw new MyRuntimeError(token, "Undefined variable '" + token.lexeme + "'.");
    }

    public void assign(MyToken token, Object val) {
        if (values.containsKey(token.lexeme)) {
            values.put(token.lexeme, val);
            return;
        }
        if (enclosing != null) {
            enclosing.assign(token, val);
            return;
        }
        throw new MyRuntimeError(token,
                "Undefined variable '" + token.lexeme + "'.");
    }
}
