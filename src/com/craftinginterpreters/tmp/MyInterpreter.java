package com.craftinginterpreters.tmp;

import static com.craftinginterpreters.tmp.MyTokenType.OR;

import java.util.ArrayList;
import java.util.List;

import com.craftinginterpreters.tmp.MyExpr.Assign;
import com.craftinginterpreters.tmp.MyExpr.Binary;
import com.craftinginterpreters.tmp.MyExpr.Call;
import com.craftinginterpreters.tmp.MyExpr.Grouping;
import com.craftinginterpreters.tmp.MyExpr.Literal;
import com.craftinginterpreters.tmp.MyExpr.Logical;
import com.craftinginterpreters.tmp.MyExpr.MyVisitor;
import com.craftinginterpreters.tmp.MyExpr.Unary;
import com.craftinginterpreters.tmp.MyExpr.Variable;
import com.craftinginterpreters.tmp.MyStmt.Block;
import com.craftinginterpreters.tmp.MyStmt.Break;
import com.craftinginterpreters.tmp.MyStmt.Function;
import com.craftinginterpreters.tmp.MyStmt.If;
import com.craftinginterpreters.tmp.MyStmt.MyExpression;
import com.craftinginterpreters.tmp.MyStmt.Print;
import com.craftinginterpreters.tmp.MyStmt.Return;
import com.craftinginterpreters.tmp.MyStmt.Var;
import com.craftinginterpreters.tmp.MyStmt.While;

public class MyInterpreter implements MyVisitor<Object>, MyStmt.MyVisitor<Void> {
    // private MyEnv env = new MyEnv();
    final MyEnv globals = new MyEnv();
    private MyEnv env = globals;

    MyInterpreter() {
        globals.define("clock", new MyLoxCallable() {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Object call(MyInterpreter interpreter, List<Object> args) {
                return (double) System.currentTimeMillis() / 1000.0;
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        });
    }

    public void interprate(List<MyStmt> stmts) {
        try {
            for (MyStmt stmt : stmts) {
                execute(stmt);
            }
            // Object value = evaluate(expr);
            // System.out.println(stringify(value));
        } catch (MyRuntimeError e) {
            MyLox.error(e.token.line, e.getMessage());
        }
    }

    @Override
    public Object visitBinaryMyExpr(Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);
        switch (expr.operator.type) {
            case BANG_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left, right);
            case GREATER:
                checkNumberOperands(expr.operator, left, right);
                return (double) left > (double) right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double) left >= (double) right;
            case LESS:
                checkNumberOperands(expr.operator, left, right);
                return (double) left < (double) right;
            case LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double) left <= (double) right;
            case MINUS:
                checkNumberOperands(expr.operator, left, right);
                return (double) left - (double) right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }

                if (left instanceof String || right instanceof String) {
                    return stringify(left) + stringify(right);
                }

                throw new MyRuntimeError(expr.operator,
                        "Operands must be of type number or string.");
            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                if ((double) right == 0) {
                    throw new MyRuntimeError(expr.operator,
                            "Can not devide by zero in.");
                }
                return (double) left / (double) right;
            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double) left * (double) right;
            default:
                return null;
        }

    }

    @Override
    public Object visitGroupingMyExpr(Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitLiteralMyExpr(Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryMyExpr(Unary expr) {
        Object right = evaluate(expr.right);
        switch (expr.operator.type) {
            case MINUS:
                return -(double) right;
            case BANG:
                return !isTruthy(right);
            default:
                return null;
        }

    }

    @Override
    public Void visitMyExpressionMyStmt(MyExpression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visitPrintMyStmt(Print stmt) {
        Object result = evaluate(stmt.expression);
        System.out.println(result);
        return null;
    }

    @Override
    public Void visitVarMyStmt(Var stmt) {
        Object val = null;

        if (stmt.expression != null) {
            val = evaluate(stmt.expression);
        }

        env.define(stmt.name.lexeme, val);
        return null;
    }

    @Override
    public Object visitVariableMyExpr(Variable expr) {
        Object res = env.get(expr.name);
        // * |Statements and state| ex. 2
        // if(res == null) {
        // throw new MyRuntimeError(expr.name, "You tried to access uninitialized
        // variable");
        // }
        return res;
    }

    @Override
    public Object visitAssignMyExpr(Assign expr) {
        Object val = evaluate(expr.expression);
        env.assign(expr.name, val);
        return val;
    }

    @Override
    public Void visitBlockMyStmt(Block stmt) {
        executeBlock(stmt.statements, new MyEnv(env));
        return null;
    }

    @Override
    public Void visitIfMyStmt(If stmt) {
        Object condition = evaluate(stmt.condition);

        if (isTruthy(condition)) {
            execute(stmt.thenStatement);
        } else if (stmt.elseBranch != null) {
            execute(stmt.elseBranch);
        }

        return null;
    }

    @Override
    public Object visitLogicalMyExpr(Logical myexpr) {
        Object left = evaluate(myexpr.left);

        if (myexpr.operator.type == OR) {
            if (isTruthy(left))
                return left;
        } else {
            if (!isTruthy(left))
                return left;
        }

        return evaluate(myexpr.right);

    }

    @Override
    public Void visitWhileMyStmt(While stmt) {
        while (isTruthy(evaluate(stmt.condition))) {
            try {
                execute(stmt.body);
            } catch (BreakError e) {
                break;
            }
        }

        return null;
    }

    @Override
    public Void visitBreakMyStmt(Break stmt) {
        throw new BreakError(stmt.token);
    }

    @Override
    public Object visitCallMyExpr(Call myexpr) {
        Object callee = evaluate(myexpr.callee);
        List<Object> args = new ArrayList<>();

        for (MyExpr arg : myexpr.arguments) {
            args.add(evaluate(arg));
        }

        if (!(callee instanceof MyLoxCallable)) {
            throw new MyRuntimeError(myexpr.paren, "Can only call functions and classes.");
        }

        MyLoxCallable function = (MyLoxCallable) callee;

        if (args.size() != function.arity()) {
            throw new MyRuntimeError(myexpr.paren,
                    "Expected " + function.arity() + " arguments but got " + args.size() + ".");
        }

        return function.call(this, args);
    }

    // private
    public Object evaluate(MyExpr expr) {
        return expr.accept(this);
    }

    // private
    public void execute(MyStmt stmt) {
        stmt.accept(this);
    }

    private Boolean isTruthy(Object object) {
        if (object == null)
            return false;
        if (object instanceof Boolean)
            return (boolean) object;
        return true;
    }

    private Boolean isEqual(Object left, Object right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null) {
            return false;
        }
        return left.equals(right);
    }

    private String stringify(Object object) {
        if (object == null)
            return "nil";

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }

    private void checkNumberOperands(MyToken operator,
            Object left, Object right) {
        if (left instanceof Double && right instanceof Double)
            return;

        throw new MyRuntimeError(operator, "Operands must be numbers.");
    }

    public void executeBlock(List<MyStmt> stmts, MyEnv newEnv) {
        MyEnv prevEnv = env;
        try {
            env = newEnv;
            for (MyStmt myStmt : stmts) {
                execute(myStmt);
            }
        } finally {
            env = prevEnv;
        }

    }

    @Override
    public Void visitFunctionMyStmt(Function mystmt) {
        MyLoxFunction func = new MyLoxFunction(mystmt, env);
        env.define(mystmt.name.lexeme, func);
        return null;
    }

    @Override
    public Void visitReturnMyStmt(Return mystmt) {
        Object value = null;

        if (mystmt.value != null)
            value = evaluate(mystmt.value);
        
        throw new MyReturn(value);
    }

}
