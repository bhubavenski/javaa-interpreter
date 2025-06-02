package com.craftinginterpreters.tmp;

class MyReturn extends RuntimeException {
    final Object value;

    public MyReturn(Object value) {
        super(null, null, false, false);
        this.value = value;
    }
}