package com.craftinginterpreters.lox;
import com.craftinginterpreters.console.ConsoleColors;

public class Token {
  final TokenType type;
  final String lexeme;
  final Object literal;
  final int line; 

  Token(TokenType type, String lexeme, Object literal, int line) {
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
