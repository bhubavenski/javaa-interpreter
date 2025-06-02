package com.craftinginterpreters.tmp;

import java.util.List;

public class MyLoxFunction implements MyLoxCallable {
    private final MyStmt.Function declaration;
    private final MyEnv closure;

    public MyLoxFunction(MyStmt.Function declaration,  MyEnv closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public Object call(MyInterpreter interpreter, List<Object> args) {
        MyEnv env = new MyEnv(closure);

        for (int i = 0; i < declaration.params.size(); i++) {
            env.define(declaration.params.get(i).lexeme, args.get(i));
        }

        try {
            interpreter.executeBlock(declaration.body, env);
        } catch (MyReturn e) {
            return e.value;
        }
        return null;

    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }

}
