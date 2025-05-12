package com.craftinginterpreters.tmp;

import java.util.List;

import com.craftinginterpreters.lox.Lox;
import com.craftinginterpreters.lox.Token;
import com.craftinginterpreters.tmp.MyExpr.Binary;
import com.craftinginterpreters.tmp.MyExpr.Grouping;
import com.craftinginterpreters.tmp.MyExpr.Literal;
import com.craftinginterpreters.tmp.MyExpr.Unary;

public class MyParser {
    private static class ParseError extends RuntimeException {
    }

    private final List<Token> tokens;
    private int current = 0;

    public MyParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public MyExpr parse() {
        try {
            return expression();
        } catch (ParseError e) {
            return null;
        }
    }

    private MyExpr expression() {
        return equality();
    }

    private MyExpr equality() {
        MyExpr expr = comparison();
        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            MyExpr right = comparison();
            expr = new MyExpr.Binary(expr, operator, right);
        }
        return expr;
    }

    private MyExpr comparison() {
        MyExpr expr = term();
        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            MyExpr right = term();
            expr = new MyExpr.Binary(expr, operator, right);
        }
        return expr;

    }

    private MyExpr term() {
        MyExpr expr = factor();
        while (match(PLUS, MINUS)) {
            Token operator = previous();
            MyExpr right = factor();
            expr = new MyExpr.Binary(expr, operator, right);
        }
        return expr;
    }

    private MyExpr factor() {
        MyExpr expr = unary();
        while (match(SLASH, STAR)) {
            Token operator = previous();
            MyExpr right = unary();
            expr = new MyExpr.Binary(expr, operator, right);
        }
        return expr;
    }

    private MyExpr unary() {
        if (match(BANG, MINUS)) {
            Token operator = previous();
            MyExpr right = unary();
            return new MyExpr.Unary(operator, right);
        }

        return primary();
    }

    private MyExpr primary() {
        if (match(FALSE))
            return new MyExpr.Literal(false);
        if (match(TRUE))
            return new MyExpr.Literal(true);
        if (match(NIL))
            return new MyExpr.Literal(null);

        if (match(NUMBER, STRING)) {
            return new MyExpr.Literal(previous().literal);
        }
        if (match(LEFT_PAREN)) {
            MyExpr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new MyExpr.Grouping(expr);
        }

        throw error(peek(), "Expect expression.");

    }

    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        Lox.error(token, message);
        return new ParseError();
    }

    private Token advance() {
        if (!isAtEnd())
            current++;
        return previous();
    }

    private Boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Boolean check(TokenType type) {
        if (isAtEnd()) {
            return false;
        }
        return peek().type == type;
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }
}
