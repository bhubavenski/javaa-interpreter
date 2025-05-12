package com.craftinginterpreters.tmp;

import com.craftinginterpreters.tmp.MyExpr.*;

public class MyASTPrinter implements MyVisitor<String> {

    public String print(MyExpr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(MyExpr.Binary expr) {
        return parenthesize("binary", expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Grouping expr) {
        return parenthesize("group",expr.expression);
    }

    @Override
    public String visitLiteralExpr(Literal expr) {
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Unary expr) {
        return parenthesize("unary", expr.right);
    }

    private String parenthesize(String name, MyExpr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (MyExpr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();

    }

}
