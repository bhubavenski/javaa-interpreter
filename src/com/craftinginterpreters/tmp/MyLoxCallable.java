package com.craftinginterpreters.tmp;

import java.util.List;

public interface MyLoxCallable {
    int arity();
    Object call(MyInterpreter interpreter, List<Object> args);
}