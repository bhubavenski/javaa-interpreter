package com.craftinginterpreters.lox;

import com.craftinginterpreters.console.ConsoleColors;

import java.util.List;

public class TokenTablePrinter {
    private static final int LINE_WIDTH = 6;
    private static final int TYPE_WIDTH = 12;
    private static final int LEXEME_WIDTH = 20;
    private static final int LITERAL_WIDTH = 20;

    public static void print(List<Token> tokens) {
        // Таблица — рамка
        String border = "+" +
                repeat("-", LINE_WIDTH + 2) + "+" +
                repeat("-", TYPE_WIDTH + 2) + "+" +
                repeat("-", LEXEME_WIDTH + 2) + "+" +
                repeat("-", LITERAL_WIDTH + 2) + "+";

        // Заглавия (оцветяваме след подравняване)
        String header = String.format("| %-" + LINE_WIDTH + "s | %-" + TYPE_WIDTH + "s | %-" + LEXEME_WIDTH + "s | %-" + LITERAL_WIDTH + "s |",
                "line", "type", "lexeme", "literal");
        header = header.replace("line", ConsoleColors.cyan("line"))
                       .replace("type", ConsoleColors.blue("type"))
                       .replace("lexeme", ConsoleColors.yellow("lexeme"))
                       .replace("literal", ConsoleColors.purple("literal"));

        System.out.println(border);
        System.out.println(header);
        System.out.println(border);

        int prevLine = -1;
        for (Token token : tokens) {
            String lineDisplay = token.line == prevLine ? "" : String.valueOf(token.line);
            prevLine = token.line;

            // Подравняваме преди да оцветим
            String lineStr    = String.format("%-" + LINE_WIDTH + "s", lineDisplay);
            String typeStr    = String.format("%-" + TYPE_WIDTH + "s", token.type);
            String lexemeStr  = String.format("%-" + LEXEME_WIDTH + "s", token.lexeme);
            String literalStr = String.format("%-" + LITERAL_WIDTH + "s", token.literal);

            // Цветове
            lineStr    = lineDisplay.isEmpty() ? repeat(" ", LINE_WIDTH) : ConsoleColors.cyan(lineStr);
            typeStr    = ConsoleColors.blue(typeStr);
            lexemeStr  = ConsoleColors.yellow(lexemeStr);
            literalStr = ConsoleColors.purple(literalStr);

            System.out.printf("| %s | %s | %s | %s |\n", lineStr, typeStr, lexemeStr, literalStr);
        }

        System.out.println(border);
    }

    private static String repeat(String s, int count) {
        return s.repeat(Math.max(0, count));
    }
}
