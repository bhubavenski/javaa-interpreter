package com.craftinginterpreters.tmp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import com.craftinginterpreters.console.ConsoleColors;

public class MyLox {
  private static final MyInterpreter interpreter = new MyInterpreter();
  static boolean hadError = false;
  static boolean hadRuntimeError = false;

  public static void main(String[] args) throws IOException {
      runFile("src\\com\\craftinginterpreters\\tmp\\file");

    // if (args.length > 1) {
    //   System.out.println("Usage: jlox [script]");
    //   System.exit(64);
    // } else if (args.length == 1) {
    //   runFile("");
    // } else {
    //   runPrompt();
    // }
  }

  private static void runFile(String path) throws IOException {
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    run(new String(bytes, Charset.defaultCharset()));
    if (hadError)
      System.exit(65);
    if (hadRuntimeError)
      System.exit(70);
  }

  private static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    MyInterpreter interpreter = new MyInterpreter();

    while (true) {
      System.out.print("> ");
      String line = reader.readLine();
      if (line == null)
        break;

      MyScanner scanner = new MyScanner(line);
      List<MyToken> tokens = scanner.scanMyTokens();

      MyParser parser = new MyParser(tokens);

      List<MyStmt> statements = parser.parse();

      if (statements.size() == 1 && statements.get(0) instanceof MyStmt.MyExpression) {
        Object value = interpreter.evaluate(((MyStmt.MyExpression) statements.get(0)).expression);
        if (value == null)
          value = "nil";
        System.out.println(value);
      } else {
        interpreter.interprate(statements);
      }
    }

  }

  private static void run(String source) {
    MyScanner scanner = new MyScanner(source);

    List<MyToken> tokens = scanner.scanMyTokens();

    MyParser parser = new MyParser(tokens);
    List<MyStmt> stmts = parser.parse();

    // MyInterpreter interpreter = new MyInterpreter();
    interpreter.interprate(stmts);
    // List<Stmt> statements = parser.parse();
    // MyParser parser = new MyParser(tokens);
    // MyExpr expression = parser.parse();
    if (hadError)
      return;

    // interpreter.interpret(statements);
    // System.out.println(new AstPrinter().print(expression));
    // System.out.println(new MyASTPrinter().print(expression));

  }

  static void error(int line, String message) {
    report(line, "", message);
  }

  private static void report(int line, String where, String message) {
    System.err.println(ConsoleColors.RED + "[line " + line + "] Error" + where + ": " + message + ConsoleColors.RESET);
    hadError = true;
  }

  static void error(MyToken token, String message) {
    if (token.type == MyTokenType.EOF) {
      report(token.line, " at end", message);
    } else {
      report(token.line, " at '" + token.lexeme + "'", message);
    }
  }

  // static void runtimeError(RuntimeError error) {
  // System.err.println(error.getMessage() +
  // "\n[line " + error.token.line + "]");
  // hadRuntimeError = true;
  // }
}