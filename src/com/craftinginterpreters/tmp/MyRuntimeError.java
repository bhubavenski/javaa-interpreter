package com.craftinginterpreters.tmp;

public class MyRuntimeError extends RuntimeException {
    final MyToken token;

  public MyRuntimeError(MyToken token, String message) {
    super(message);
    this.token = token;
  }
}
