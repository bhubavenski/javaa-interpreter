package com.craftinginterpreters.lox;

import java.util.List;

class LoxFunction implements LoxCallable {
  private final Stmt.Function declaration;
  private final Environment closure;
  private final boolean isInitializer;

  LoxFunction(Stmt.Function declaration, Environment closure,
      boolean isInitializer) {
    this.isInitializer = isInitializer;
    this.declaration = declaration;
    this.closure = closure;
  }

  LoxFunction bind(LoxInstance instance) {
    Environment environment = new Environment(closure);
    environment.define("this", instance);
    return new LoxFunction(declaration, environment,
        isInitializer);
  }

  @Override
  public int arity() {
    return declaration.params.size();
  }

  @Override
  public Object call(Interpreter interpreter,
      List<Object> arguments) {
    Environment environment = new Environment(closure);

    for (int i = 0; i < declaration.params.size(); i++) {
      environment.define(declaration.params.get(i).lexeme,
          arguments.get(i));
    }
    try {
      interpreter.executeBlock(declaration.body, environment);
    } catch (Return returnValue) {
      if (isInitializer)
        return closure.getAt(0, "this");
      return returnValue.value;
    }
    if (isInitializer)
      return closure.getAt(0, "this");
    return null;
  }

  @Override
  public String toString() {
    return "<fn " + declaration.name.lexeme + ">";
  }
}

// Lambda support
// package com.craftinginterpreters.lox;

// import java.util.List;

// class LoxFunction implements LoxCallable {
// private final String name;
// private final List<Token> params;
// private final List<Stmt> body;
// private final Environment closure;

// // `Expr.Lambda`
// LoxFunction(String name, List<Token> params, List<Stmt> body, Environment
// closure) {
// this.name = name;
// this.params = params;
// this.body = body;
// this.closure = closure;
// }

// // `Stmt.Function`
// LoxFunction(Stmt.Function declaration, Environment closure) {
// this(
// declaration.name.lexeme,
// declaration.params,
// declaration.body,
// closure
// );
// }

// @Override
// public int arity() {
// return params.size();
// }

// @Override
// public Object call(Interpreter interpreter, List<Object> arguments) {
// Environment environment = new Environment(closure);
// for (int i = 0; i < params.size(); i++) {
// environment.define(params.get(i).lexeme, arguments.get(i));
// }

// try {
// interpreter.executeBlock(body, environment);
// } catch (Return returnValue) {
// return returnValue.value;
// }

// return null;
// }

// @Override
// public String toString() {
// if (name == null) return "<fn lambda>";
// return "<fn " + name + ">";
// }
// }
