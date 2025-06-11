package com.craftinginterpreters.tmp;

import java.util.List;

abstract class MyExpr {
  interface MyVisitor<R> {
    R visitBinaryMyExpr(Binary myexpr);
    R visitCallMyExpr(Call myexpr);
    R visitGroupingMyExpr(Grouping myexpr);
    R visitLiteralMyExpr(Literal myexpr);
    R visitLogicalMyExpr(Logical myexpr);
    R visitUnaryMyExpr(Unary myexpr);
    R visitVariableMyExpr(Variable myexpr);
    R visitAssignMyExpr(Assign myexpr);
  }
  static class Binary extends MyExpr {
    Binary(MyExpr left, MyToken operator, MyExpr right) {
      this.left = left;
      this.operator = operator;
      this.right = right;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitBinaryMyExpr(this);
    }

    final MyExpr left;
    final MyToken operator;
    final MyExpr right;
  }
  static class Call extends MyExpr {
    Call(MyExpr callee, MyToken paren, List<MyExpr> arguments) {
      this.callee = callee;
      this.paren = paren;
      this.arguments = arguments;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitCallMyExpr(this);
    }

    final MyExpr callee;
    final MyToken paren;
    final List<MyExpr> arguments;
  }
  static class Grouping extends MyExpr {
    Grouping(MyExpr expression) {
      this.expression = expression;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitGroupingMyExpr(this);
    }

    final MyExpr expression;
  }
  static class Literal extends MyExpr {
    Literal(Object value) {
      this.value = value;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitLiteralMyExpr(this);
    }

    final Object value;
  }
  static class Logical extends MyExpr {
    Logical(MyExpr left, MyToken operator, MyExpr right) {
      this.left = left;
      this.operator = operator;
      this.right = right;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitLogicalMyExpr(this);
    }

    final MyExpr left;
    final MyToken operator;
    final MyExpr right;
  }
  static class Unary extends MyExpr {
    Unary(MyToken operator, MyExpr right) {
      this.operator = operator;
      this.right = right;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitUnaryMyExpr(this);
    }

    final MyToken operator;
    final MyExpr right;
  }
  static class Variable extends MyExpr {
    Variable(MyToken name) {
      this.name = name;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitVariableMyExpr(this);
    }

    final MyToken name;
  }
  static class Assign extends MyExpr {
    Assign(MyToken name, MyExpr expression) {
      this.name = name;
      this.expression = expression;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitAssignMyExpr(this);
    }

    final MyToken name;
    final MyExpr expression;
  }

  abstract <R> R accept(MyVisitor<R> visitor);
}
