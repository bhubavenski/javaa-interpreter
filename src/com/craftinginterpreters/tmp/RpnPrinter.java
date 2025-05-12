package com.craftinginterpreters.tmp;

import com.craftinginterpreters.lox.Expr;
import com.craftinginterpreters.lox.Expr.*;

public class RpnPrinter implements Visitor<String> {
  @Override
  public String visitBinaryExpr(Binary expr) {
    return expr.left.accept(this) + " " + expr.right.accept(this) + expr.operator.lexeme;
  }

  @Override
  public String visitGroupingExpr(Grouping expr) {
    return expr.expression.accept(this);
  }

  @Override
  public String visitLiteralExpr(Literal expr) {
    return expr.value.toString();
  }

  @Override
  public String visitUnaryExpr(Unary expr) {
    return expr.right.accept(this) + " " + expr.operator.lexeme;
  }

  public String print(Expr expr) {
    return expr.accept(this);
  }

  // public static void main(String[] args) {
  //   // Създай синтактичното дърво
  //   Expr expression = new Expr.Unary(
  //       new Token(TokenType.MINUS, "-", null, 1),
  //       new Expr.Grouping(
  //           new Expr.Binary(
  //               new Expr.Literal(123),
  //               new Token(TokenType.PLUS, "+", null, 1),
  //               new Expr.Literal(45.67))));

  //   RpnPrinter printer = new RpnPrinter();
  //   String result = printer.print(expression);

  //   System.out.println(result); 
  // }
}
