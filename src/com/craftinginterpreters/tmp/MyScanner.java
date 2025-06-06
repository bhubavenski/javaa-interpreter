// package com.craftinginterpreters.tmp;

// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import com.craftinginterpreters.lox.Lox;
// import com.craftinginterpreters.lox.Token;
// import com.craftinginterpreters.lox.TokenType;

// import static com.craftinginterpreters.lox.TokenType.*;

// public class MyScanner {
//     // the sourse we are scanning
//     private final String source;
//     // the list of tokens we are going to return
//     private final List<Token> tokens = new ArrayList<>();
//     // the start of the current lexeme
//     private int start = 0;
//     // the current position in the source string
//     private int current = 0;
//     // the current line number
//     private int line = 1;

//     private static final Map<String, TokenType> keywords;

//     static {
//         keywords = new HashMap<>();
//         keywords.put("and", AND);
//         keywords.put("class", CLASS);
//         keywords.put("else", ELSE);
//         keywords.put("false", FALSE);
//         keywords.put("for", FOR);
//         keywords.put("fun", FUN);
//         keywords.put("if", IF);
//         keywords.put("nil", NIL);
//         keywords.put("or", OR);
//         keywords.put("print", PRINT);
//         keywords.put("return", RETURN);
//         keywords.put("super", SUPER);
//         keywords.put("this", THIS);
//         keywords.put("true", TRUE);
//         keywords.put("var", VAR);
//         keywords.put("while", WHILE);
//     }

//     MyScanner(String source) {
//         this.source = source;
//     }

//     List<Token> scanTokens() {
//         while (!isAtEnd()) {
//             start = current;
//             scanToken();
//         }
//         tokens.add(new Token(EOF, "", null, line));
//         return tokens;
//     }

//     private void scanToken() {
//         // char c = advance();
//         char c = advance();

//         switch (c) {
//             case '(':
//                 addToken(LEFT_PAREN);
//                 break;
//             case ')':
//                 addToken(RIGHT_PAREN);
//                 break;
//             case '{':
//                 addToken(LEFT_BRACE);
//                 break;
//             case '}':
//                 addToken(RIGHT_BRACE);
//                 break;
//             case ',':
//                 addToken(COMMA);
//                 break;
//             case '.':
//                 addToken(DOT);
//                 break;
//             case '-':
//                 addToken(MINUS);
//                 break;
//             case '+':
//                 addToken(PLUS);
//                 break;
//             case ';':
//                 addToken(SEMICOLON);
//                 break;
//             case '*':
//                 addToken(STAR);
//                 break;
//             case '!':
//                 addToken(match('=') ? BANG_EQUAL : BANG);
//                 break;
//             case '=':
//                 addToken(match('=') ? EQUAL_EQUAL : EQUAL);
//                 break;
//             case '<':
//                 addToken(match('=') ? LESS_EQUAL : LESS);
//                 break;
//             case '>':
//                 addToken(match('=') ? GREATER_EQUAL : GREATER);
//                 break;
//             case ' ':
//             case '\r':
//             case '\t':
//                 // Ignore whitespace.
//                 break;

//             case '\n':
//                 line++;
//                 break;

//             default:
//                 if (isDigit(c)) {
//                     number();
//                 } else if (isAlpha(c)) {
//                     identifier();
//                 } else {
//                     Lox.error(line, "Unexpected character: " + "'" + c + "'");
//                 }
//         }
//     }

//     // var i = 123.798; 123.
//     private void number() {
//         while (isDigit(peek()))
//             advance();

//         if (peek() == '.' && isDigit(peekNext())) {
//             advance();

//             while (isDigit(peek()))
//                 advance();
//         }

//         Double number = Double.parseDouble(source.substring(start, current));

//         addToken(NUMBER, number);
//     }

//     private char peekNext() {
//         if (current + 1 >= source.length())
//             return '\0';
//         return source.charAt(current + 1);
//     }

//     private void identifier() {
//         while (isAlphaNumeric(peek()))
//             advance();
//         String lexem = source.substring(start, current);
//         TokenType type = keywords.get(lexem);
//         if (type == null) {
//             addToken(TokenType.IDENTIFIER);
//         } else {
//             addToken(type);
//         }
//     }

//     private boolean match(char expected) {
//         if (isAtEnd())
//             return false;
//         if (peek() != expected)
//             return false;

//         current++;
//         return true;
//     }

//     private boolean isAlpha(char c) {
//         return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
//     }

//     private boolean isAlphaNumeric(char c) {
//         return (isAlpha(c) || isDigit(c));
//     }

//     private boolean isDigit(char c) {
//         return c >= '0' && c <= '9';
//     }

//     private boolean isAtEnd() {
//         return current >= source.length();
//     }

//     private char peek() {
//         return source.charAt(current);
//     }

//     private char advance() {
//         return source.charAt(current++);
//     }

//     private void addToken(TokenType type, Object literal) {
//         String text = source.substring(start, current);
//         tokens.add(new Token(type, text, literal, line));
//     }

//     private void addToken(TokenType type) {
//         addToken(type, null);
//     }
// }
