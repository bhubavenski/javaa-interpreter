package com.craftinginterpreters.tmp;
import com.craftinginterpreters.console.ConsoleColors;

public class MyToken {
  final MyTokenType type;
  final String lexeme;
  final Object literal;
  final int line; 

  MyToken(MyTokenType type, String lexeme, Object literal, int line) {
      this.type = type;
      this.lexeme = lexeme;
      this.literal = literal;
      this.line = line;
  }

  @Override
  public String toString() {
      return String.format(
          "%s: %-15s %s %s",
          ConsoleColors.green("Line " + line),
          ConsoleColors.blue(type),
          ConsoleColors.yellow(lexeme),
          ConsoleColors.cyan(literal)
      );
  }
  
}
