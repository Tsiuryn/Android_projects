package com.example.helper.ui.calculator.reverse_polish_notation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.StringTokenizer;

public class ExpressionParser {

    private static String OPERATORS = "+-×÷^%cst√";
    private static String DELIMETERS = "() " + OPERATORS;

    private static boolean isDelimiter(String token) {
        if (token.length() != 1) return false;
        for (int i = 0; i < DELIMETERS.length(); i++) {
            if (token.charAt(0) == DELIMETERS.charAt(i)) return true;
        }
        return false;
    }

    private static boolean isFunction(String token) {
        return token.equals("^") || token.equals("%")
                || token.equals("pow10") || token.equals("c")
                || token.equals("s") || token.equals("t")
                || token.equals("√");
    }

    private static int priority(String token) {
        if (token.equals("(")) return 1;
        if (token.equals("+") || token.equals("-")) return 2;
        if (token.equals("×") || token.equals("÷")) return 3;
        if (token.equals("c")) return 4;
        return 5;
    }

    public static List<String> parse(String infix) {
        List<String> postfix = new ArrayList<String>();
        Deque<String> stack = new ArrayDeque<String>();
        StringTokenizer tokenizer = new StringTokenizer(infix, DELIMETERS, true);
        String prev = "";
        String curr = "";
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            if (curr.equals(" ")) continue;
            if (isFunction(curr)) stack.push(curr);
            else if (isDelimiter(curr)) {
                if (curr.equals("(")) stack.push(curr);
                else if (curr.equals(")")) {
                    while (!stack.peek().equals("(")) {
                        postfix.add(stack.pop());
                    }
                    stack.pop();
                    if (!stack.isEmpty() && isFunction(stack.peek())) {
                        postfix.add(stack.pop());
                    }
                } else {
                    if (curr.equals("-") && (prev.equals("") || (isDelimiter(prev) && !prev.equals(")")))) {
                        // унарный минус
                        curr = "u-";
                    } else {
                        while (!stack.isEmpty() && (priority(curr) <= priority(stack.peek()))) {
                            postfix.add(stack.pop());
                        }

                    }
                    stack.push(curr);
                }

            } else {
                postfix.add(curr);
            }
            prev = curr;
        }

        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }
        return postfix;
    }
}
