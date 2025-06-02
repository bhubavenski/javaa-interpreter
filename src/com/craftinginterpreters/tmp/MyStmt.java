package com.craftinginterpreters.tmp;

import java.util.List;

abstract class MyStmt {
  interface MyVisitor<R> {
    R visitMyExpressionMyStmt(MyExpression mystmt);
    R visitFunctionMyStmt(Function mystmt);
    R visitPrintMyStmt(Print mystmt);
    R visitReturnMyStmt(Return mystmt);
    R visitVarMyStmt(Var mystmt);
    R visitBlockMyStmt(Block mystmt);
    R visitIfMyStmt(If mystmt);
    R visitWhileMyStmt(While mystmt);
    R visitBreakMyStmt(Break mystmt);
  }
  static class MyExpression extends MyStmt {
    MyExpression(MyExpr expression) {
      this.expression = expression;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitMyExpressionMyStmt(this);
    }

    final MyExpr expression;
  }
  static class Function extends MyStmt {
    Function(MyToken name, List<MyToken> params, List<MyStmt> body) {
      this.name = name;
      this.params = params;
      this.body = body;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitFunctionMyStmt(this);
    }

    final MyToken name;
    final List<MyToken> params;
    final List<MyStmt> body;
  }
  static class Print extends MyStmt {
    Print(MyExpr expression) {
      this.expression = expression;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitPrintMyStmt(this);
    }

    final MyExpr expression;
  }
  static class Return extends MyStmt {
    Return(MyToken keyword, MyExpr value) {
      this.keyword = keyword;
      this.value = value;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitReturnMyStmt(this);
    }

    final MyToken keyword;
    final MyExpr value;
  }
  static class Var extends MyStmt {
    Var(MyToken name, MyExpr expression) {
      this.name = name;
      this.expression = expression;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitVarMyStmt(this);
    }

    final MyToken name;
    final MyExpr expression;
  }
  static class Block extends MyStmt {
    Block(List<MyStmt> statements) {
      this.statements = statements;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitBlockMyStmt(this);
    }

    final List<MyStmt> statements;
  }
  static class If extends MyStmt {
    If(MyExpr condition, MyStmt thenStatement, MyStmt elseBranch) {
      this.condition = condition;
      this.thenStatement = thenStatement;
      this.elseBranch = elseBranch;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitIfMyStmt(this);
    }

    final MyExpr condition;
    final MyStmt thenStatement;
    final MyStmt elseBranch;
  }
  static class While extends MyStmt {
    While(MyExpr condition, MyStmt body) {
      this.condition = condition;
      this.body = body;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitWhileMyStmt(this);
    }

    final MyExpr condition;
    final MyStmt body;
  }
  static class Break extends MyStmt {
    Break(MyToken token) {
      this.token = token;
    }
    @Override
    <R> R accept(MyVisitor<R> visitor) {
      return visitor.visitBreakMyStmt(this);
    }

    final MyToken token;
  }

  abstract <R> R accept(MyVisitor<R> visitor);
}
