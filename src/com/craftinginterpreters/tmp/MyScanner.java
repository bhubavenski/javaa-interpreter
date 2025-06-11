package com.craftinginterpreters.tmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.craftinginterpreters.lox.Lox;

import static com.craftinginterpreters.tmp.MyTokenType.*;

public class MyScanner {
    // the sourse we are scanning
    private final String source;
    // the list of MyTokens we are going to return
    private final List<MyToken> MyTokens = new ArrayList<>();
    // the start of the current lexeme
    private int start = 0;
    // the current position in the source string
    private int current = 0;
    // the current line number
    private int line = 1;

    private static final Map<String, MyTokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
        keywords.put("break", BREAK);

    }

    MyScanner(String source) {
        this.source = source;
    }

    public List<MyToken> scanMyTokens() {
        while (!isAtEnd()) {
            start = current;
            scanMyToken();
        }
        MyTokens.add(new MyToken(EOF, "", null, line));
        return MyTokens;
    }

    private void scanMyToken() {
        // char c = advance();
        char c = advance();

        switch (c) {
            case '(':
                addMyToken(LEFT_PAREN);
                break;
            case ')':
                addMyToken(RIGHT_PAREN);
                break;
            case '{':
                addMyToken(LEFT_BRACE);
                break;
            case '}':
                addMyToken(RIGHT_BRACE);
                break;
            case ',':
                addMyToken(COMMA);
                break;
            case '.':
                addMyToken(DOT);
                break;
            case '-':
                addMyToken(MINUS);
                break;
            case '+':
                addMyToken(PLUS);
                break;
            case '/':
                // 3213213

                if (match('/')) {
                    // A comment goes until the end of the line
                    while (peek() != '\n' && !isAtEnd())
                        advance();
                } else if (match('*')) {
                    int nesting = 1;
                    while (nesting != 0 && !isAtEnd()) {
                        if (peek() == '/' && peekNext() == '*') {
                            nesting++;
                            advance();
                            advance();
                        } else if (peek() == '*' && peekNext() == '/') {
                            nesting--;
                            advance();
                            advance();
                        } else {
                            if (peek() == '\n')
                                line++;
                            advance();
                        }

                    }

                } else {
                    addMyToken(SLASH);
                }
                break;
            case ';':
                addMyToken(SEMICOLON);
                break;
            case '*':
                addMyToken(STAR);
                break;
            case '!':
                addMyToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addMyToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addMyToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addMyToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;

            case '\n':
                line++;
                break;
            case '"':
                string();
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    MyLox.error(line, "Unexpected character: " + "'" + c + "'");
                }
        }
    }

    // var i = 123.798; 123.
    private void number() {
        while (isDigit(peek()))
            advance();

        if (peek() == '.' && isDigit(peekNext())) {
            advance();

            while (isDigit(peek()))
                advance();
        }

        Double number = Double.parseDouble(source.substring(start, current));

        addMyToken(NUMBER, number);
    }

    private char peekNext() {
        if (current + 1 >= source.length())
            return '\0';
        return source.charAt(current + 1);
    }

    private void identifier() {
        while (isAlphaNumeric(peek()))
            advance();
        String lexem = source.substring(start, current);
        MyTokenType type = keywords.get(lexem);
        if (type == null) {
            addMyToken(MyTokenType.IDENTIFIER);
        } else {
            addMyToken(type);
        }
    }

    private boolean match(char expected) {
        if (isAtEnd())
            return false;
        if (peek() != expected)
            return false;

        current++;
        return true;
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isAlphaNumeric(char c) {
        return (isAlpha(c) || isDigit(c));
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void string() {

        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n')
                line++;
            advance();
        }

        if (isAtEnd()) {
            MyLox.error(line, "Unterminated string.");
            return;
        }

        // The closing ".
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addMyToken(STRING, value);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char peek() {
        if (isAtEnd())
            return '\0';
        return source.charAt(current);
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addMyToken(MyTokenType type, Object literal) {
        String text = source.substring(start, current);
        MyTokens.add(new MyToken(type, text, literal, line));
    }

    private void addMyToken(MyTokenType type) {
        addMyToken(type, null);
    }
}
