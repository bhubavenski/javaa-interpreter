package com.craftinginterpreters.tmp;

import com.craftinginterpreters.lox.Token;

abstract class MyExpr {
    interface MyVisitor<R> {
        R visitBinaryExpr(MyExpr.Binary expr);

        R visitGroupingExpr(Grouping expr);

        R visitLiteralExpr(Literal expr);

        R visitUnaryExpr(Unary expr);
    }

    static class Binary extends MyExpr {
        Binary(MyExpr left, Token operator, MyExpr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(MyVisitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }

        final MyExpr left;
        final Token operator;
        final MyExpr right;
    }

    static class Grouping extends MyExpr {
        Grouping(MyExpr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(MyVisitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }

        final MyExpr expression;
    }

    static class Literal extends MyExpr {
        Literal(Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(MyVisitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }

        final Object value;
    }

    static class Unary extends MyExpr {
        Unary(Token operator, MyExpr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(MyVisitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }

        final Token operator;
        final MyExpr right;
    }

    abstract <R> R accept(MyVisitor<R> visitor);

}
