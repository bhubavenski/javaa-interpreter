package com.craftinginterpreters.tmp;

public class BreakError extends RuntimeException {
    MyToken token;

    BreakError(MyToken token) {
        this.token = token;
    }
}
