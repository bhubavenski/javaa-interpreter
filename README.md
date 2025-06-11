# Lox — Simple Programming Language

Lox is a small, easy-to-learn scripting language created as a project based on the [Crafting Interpreters](https://craftinginterpreters.com/) book by Robert Nystrom.

## About the Project

This project is implemented following a step-by-step tutorial that shows how to build an interpreter for a custom language from scratch. Lox supports core programming concepts such as variables, functions, control flow, classes, inheritance, and more.

## Features

- Interpreted language with lexical scoping  
- Support for variables, conditional statements, loops  
- Functions and methods  
- Classes and inheritance  
- Detailed error handling  

## Grammar

Here is the core grammar of Lox, defined in parsing rules (EBNF-like format):

```ebnf
program        → declaration* EOF ;

declaration    → classDecl
               | funDecl
               | varDecl
               | statement ;

classDecl      → "class" IDENTIFIER ( "<" IDENTIFIER )?
                 "{" function* "}" ;
funDecl        → "fun" function ;
varDecl        → "var" IDENTIFIER ( "=" expression )? ";" ;
function       → IDENTIFIER "(" parameters? ")" block ;
parameters     → IDENTIFIER ( "," IDENTIFIER )* ;
statement      → exprStmt
               | forStmt
               | ifStmt
               | printStmt
               | returnStmt     
               | whileStmt
               | block ;

returnStmt     → "return" expression? ";" ;
forStmt        → "for" "(" ( varDecl | exprStmt | ";" )
                 expression? ";"
                 expression? ")" statement ;
block          → "{" declaration* "}" ;
exprStmt       → expression ";" ;
printStmt      → "print" expression ";" ;
whileStmt      → "while" "(" expression ")" statement ;
ifStmt         → "if" "(" expression ")" statement
               ( "else" statement )? ;

expression     → assignment ;

assignment     → ( call "." )? IDENTIFIER "=" assignment
               | logic_or ;

logic_or       → logic_and ( "or" logic_and )* ;
logic_and      → equality ( "and" equality )* ;

equality       → comparison ( ( "!=" | "==" ) comparison )* ;
comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
term           → factor ( ( "-" | "+" ) factor )* ;
factor         → unary ( ( "/" | "*" ) unary )* ;
unary          → ( "!" | "-" ) unary | call ;
call           → primary ( "(" arguments? ")" | "." IDENTIFIER )* ;

arguments      → expression ( "," expression )* ;
primary        → "true" | "false" | "nil" | "this"
               | NUMBER | STRING | IDENTIFIER | "(" expression ")"
               | "super" "." IDENTIFIER ;

## How to Use

### Setup and Running

This Lox interpreter is implemented in Java.

1. **Clone the repository:**

```bash
git clone https://github.com/yourusername/lox.git
cd lox
```

2. **Compile the Java sources:**

If you have `javac` installed, run:

```bash
javac -d bin src/com/craftinginterpreters/lox/*.java src/com/craftinginterpreters/lox/*/*.java
```

*(Adjust the source path if needed based on the project structure.)*

3. **Run the interpreter:**

- To run a Lox script file:

```bash
java -cp bin com.craftinginterpreters.lox.Lox path/to/script.lox
```

- To start the interactive REPL prompt:

```bash
java -cp bin com.craftinginterpreters.lox.Lox
```

---

### How It Works

- The main class is `com.craftinginterpreters.lox.Lox`.
- If you provide a script file as an argument, it will execute it.
- If no arguments are provided, it starts an interactive prompt.
- The interpreter performs scanning, parsing, resolving, and interpreting of Lox code.

---

### Sample Lox Code

```lox
// Function to sum two numbers
fun sum(a, b) {
  return a + b;
}

print sum(3, 4); // Output: 7
```

---

### Resources

- [Crafting Interpreters - Official Website](https://craftinginterpreters.com/)

---

### License

MIT License © 2025 Boris Hubavenski

