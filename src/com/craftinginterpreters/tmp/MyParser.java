package com.craftinginterpreters.tmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.craftinginterpreters.tmp.MyExpr.Binary;
import com.craftinginterpreters.tmp.MyExpr.Grouping;
import com.craftinginterpreters.tmp.MyExpr.Literal;
import com.craftinginterpreters.tmp.MyExpr.Logical;
import com.craftinginterpreters.tmp.MyExpr.Unary;
import static com.craftinginterpreters.tmp.MyTokenType.*;

public class MyParser {
    private static class ParseError extends RuntimeException {
    }

    private final List<MyToken> MyTokens;
    private int current = 0;

    public MyParser(List<MyToken> MyTokens) {
        this.MyTokens = MyTokens;
    }

    public List<MyStmt> parse() {
        List<MyStmt> stmts = new ArrayList<>();
        while (!isAtEnd()) {
            stmts.add(declaration());
        }
        return stmts;
    }

    private MyStmt declaration() {
        try {
            if (match(VAR)) {
                return varDeclaration();
            }
            return statement();

        } catch (ParseError e) {
            synchronize();
            return null;
        }

    }

    private MyStmt varDeclaration() {
        MyToken name = consume(IDENTIFIER, "Expected var name");
        MyExpr initializer = null;

        if (match(EQUAL)) {
            initializer = expression();
        }

        consume(SEMICOLON, "Expected semicolon after var decl");
        return new MyStmt.Var(name, initializer);
    }

    private MyStmt statement() {
        if (match(PRINT)) {
            return printStatement();
        } else if (match(LEFT_BRACE)) {
            return new MyStmt.Block(block());
        } else if (match(IF)) {
            return ifStatement();
        } else if (match(WHILE)) {
            return whileStatement();
        } else if (match(FOR)) {
            return forStatement();
        } else if (match(BREAK)) {
            return breakStatement();
        } else {
            return expressionStatement();
        }
    }

    private MyStmt forStatement() {
        consume(LEFT_PAREN, "Expect '(' after for");

        MyStmt initializer;
        if (match(SEMICOLON)) {
            initializer = null;
        } else if (match(VAR)) {
            initializer = varDeclaration();
        } else {
            initializer = expressionStatement();
        }

        MyExpr condition = new Literal(true);
        if (!check(SEMICOLON)) {
            condition = expression();
        }
        consume(SEMICOLON, "Expect ';' after for condition");

        MyExpr increment = null;
        if (!check(RIGHT_PAREN)) {
            increment = expression();
        }
        consume(RIGHT_PAREN, "Expect ')' after for increment");

        MyStmt body = statement();

        if (increment != null) {
            body = new MyStmt.Block(Arrays.asList(body, new MyStmt.MyExpression(increment)));
        }

        body = new MyStmt.While(condition, body);

        if (initializer != null) {
            body = new MyStmt.Block(Arrays.asList(initializer, body));
        }

        return body;
    }

    private MyStmt whileStatement() {
        consume(LEFT_PAREN, "Expect '(' after while condition");

        MyExpr condition = expression();

        consume(RIGHT_PAREN, "Expect ')' after while condition");

        MyStmt body = statement();

        return new MyStmt.While(condition, body);

    }

    private MyStmt ifStatement() {
        consume(LEFT_PAREN, "Expect '(' after if");
        MyExpr condition = expression();

        consume(RIGHT_PAREN, "Expect ')' after if condition");
        MyStmt thenBranch = statement();

        MyStmt elseBranch = null;

        if (match(ELSE)) {
            elseBranch = statement();
        }
        return new MyStmt.If(condition, thenBranch, elseBranch);
    }

    private MyStmt breakStatement() {
        MyToken token = previous();
        consume(SEMICOLON, "Expect ';' after 'break'.");
        return new MyStmt.Break(token);
    }

    private List<MyStmt> block() {
        List<MyStmt> statements = new ArrayList<>();
        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            MyStmt decl = declaration();
            statements.add(decl);
        }
        consume(RIGHT_BRACE, "Expect '}' after block");
        return statements;
    }

    private MyStmt printStatement() {
        MyExpr expr = expression();
        consume(SEMICOLON, "Expected ';' after expr");
        return new MyStmt.Print(expr);
    }

    private MyStmt expressionStatement() {
        MyExpr expr = expression();
        consume(SEMICOLON, "Expected ';' after expr");
        return new MyStmt.MyExpression(expr);
    }

    private MyExpr expression() {
        return assignment();
    }

    private MyExpr assignment() {
        MyExpr expr = or();

        if (match(EQUAL)) {
            MyToken equals = previous();
            MyExpr value = assignment();

            if (expr instanceof MyExpr.Variable) {
                MyToken name = ((MyExpr.Variable) expr).name;
                return new MyExpr.Assign(name, value);
            }

            throw error(equals, "Invalid assignment target.");
        }

        return expr;
    }

    private MyExpr or() {
        MyExpr expr = and();
        while (match(OR)) {
            MyToken operator = previous();
            MyExpr right = and();
            expr = new MyExpr.Logical(expr, operator, right);
        }
        return expr;
    }

    private MyExpr and() {
        MyExpr expr = equality();
        while (match(AND)) {
            MyToken operator = previous();
            MyExpr right = equality();
            expr = new MyExpr.Logical(expr, operator, right);
        }
        return expr;
    }

    private MyExpr equality() {
        MyExpr expr = comparison();
        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            MyToken operator = previous();
            MyExpr right = comparison();
            expr = new MyExpr.Binary(expr, operator, right);
        }
        return expr;
    }

    private MyExpr comparison() {
        MyExpr expr = term();
        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            MyToken operator = previous();
            MyExpr right = term();
            expr = new MyExpr.Binary(expr, operator, right);
        }
        return expr;

    }

    private MyExpr term() {
        MyExpr expr = factor();
        while (match(PLUS, MINUS)) {
            MyToken operator = previous();
            MyExpr right = factor();
            expr = new MyExpr.Binary(expr, operator, right);
        }
        return expr;
    }

    private MyExpr factor() {
        MyExpr expr = unary();
        while (match(SLASH, STAR)) {
            MyToken operator = previous();
            MyExpr right = unary();
            expr = new MyExpr.Binary(expr, operator, right);
        }
        return expr;
    }

    private MyExpr unary() {
        if (match(BANG, MINUS)) {
            MyToken operator = previous();
            MyExpr right = unary();
            return new MyExpr.Unary(operator, right);
        }

        return primary();
    }

    private MyExpr primary() {
        if (match(IDENTIFIER)) {
            return new MyExpr.Variable(previous());
        }
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

    private MyToken consume(MyTokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        throw error(peek(), message);
    }

    private ParseError error(MyToken MyToken, String message) {
        MyLox.error(MyToken, message);
        return new ParseError();
    }

    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().type == SEMICOLON)
                return;

            switch (peek().type) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
                default:
            }

            advance();
        }
    }

    private MyToken advance() {
        if (!isAtEnd())
            current++;
        return previous();
    }

    /**
     * Checks the current token if it's from the given type and if so - consumes it
     */
    private Boolean match(MyTokenType... types) {
        for (MyTokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Boolean check(MyTokenType type) {
        if (isAtEnd()) {
            return false;
        }
        return peek().type == type;
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private MyToken peek() {
        return MyTokens.get(current);
    }

    private MyToken previous() {
        return MyTokens.get(current - 1);
    }
}
