package com.craftinginterpreters.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyGenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];

        defineAst(outputDir, "MyExpr", Arrays.asList(
                "Binary   : MyExpr left, MyToken operator, MyExpr right",
                "Grouping : MyExpr expression",
                "Literal  : Object value",
                "Logical: MyExpr left, MyToken operator, MyExpr right",
                "Unary    : MyToken operator, MyExpr right",
                "Variable: MyToken name",
                "Assign: MyToken name, MyExpr expression"
                ));
        defineAst(outputDir, "MyStmt", Arrays.asList(
                "MyExpression: MyExpr expression",
                "Print : MyExpr expression",
                "Var : MyToken name, MyExpr expression",
                "Block: List<MyStmt> statements",
                "If : MyExpr condition, MyStmt thenStatement, MyStmt elseBranch",
                "While : MyExpr condition, MyStmt body",
                "Break: MyToken token"));
    }

    private static void defineAst(
            String outputDir, String baseName, List<String> types)
            throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package com.craftinginterpreters.tmp;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");
        defineVisitor(writer, baseName, types);
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }
        writer.println();
        writer.println("  abstract <R> R accept(MyVisitor<R> visitor);");
        writer.println("}");
        writer.close();
    }

    private static void defineVisitor(
            PrintWriter writer, String baseName, List<String> types) {
        writer.println("  interface MyVisitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("    R visit" + typeName + baseName + "(" +
                    typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println("  }");
    }

    private static void defineType(
            PrintWriter writer, String baseName,
            String className, String fieldList) {
        writer.println("  static class " + className + " extends " +
                baseName + " {");

        // Constructor.
        writer.println("    " + className + "(" + fieldList + ") {");

        // Store parameters in fields.
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("      this." + name + " = " + name + ";");
        }

        writer.println("    }");
        writer.println("    @Override");
        writer.println("    <R> R accept(MyVisitor<R> visitor) {");
        writer.println("      return visitor.visit" +
                className + baseName + "(this);");
        writer.println("    }");

        // Fields.
        writer.println();
        for (String field : fields) {
            writer.println("    final " + field + ";");
        }

        writer.println("  }");
    }
}