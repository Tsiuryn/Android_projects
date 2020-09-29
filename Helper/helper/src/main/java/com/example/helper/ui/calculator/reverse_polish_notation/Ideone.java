package com.example.helper.ui.calculator.reverse_polish_notation;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Ideone {
    public static BigDecimal calc(List<String> postfix) {
        Deque<BigDecimal> stack = new ArrayDeque<BigDecimal>();
        String x = "";
        for (int i = 0; i < postfix.size(); i++) {
            x = postfix.get(i);
            if (x.equals("^")) {
                int a = stack.pop().intValue();
                BigDecimal b = stack.pop();

                stack.push(b.pow(a, MathContext.DECIMAL64));
            } else if (x.equals("√")) {
                double a = stack.pop().doubleValue();
                double b = stack.pop().doubleValue();
                stack.push(new BigDecimal(Math.pow(a, 1 / b), MathContext.DECIMAL64));
            } else if (x.equals("%")){
                double a = stack.pop().doubleValue();
                double b = stack.pop().doubleValue();
                if(postfix.get(i+1).equals("+")){
                    stack.push(new BigDecimal(b + (a * b / 100)));
                    postfix.remove(i+1);
                    continue;
                } else if(postfix.get(i+1).equals("-")){
                    stack.push(new BigDecimal(b - (a * b / 100)));
                    postfix.remove(i+1);
                    continue;
                } else if(postfix.get(i+1).equals("×")){
                    stack.push(new BigDecimal(a * b / 100));
                    postfix.remove(i+1);
                    continue;
                } else if(postfix.get(i+1).equals("÷")){
                    stack.push(new BigDecimal(b * 100 / a));
                    postfix.remove(i+1);
                    continue;
                }

            } else if (x.equals("c")) {
                double a = stack.pop().doubleValue();
                if (a == 90) {
                    stack.push(new BigDecimal(0));
                } else {
                    stack.push(new BigDecimal(Math.cos(degToRad(a)), MathContext.DECIMAL64));
                }
            }
//
            else if (x.equals("s")) {
                double a = stack.pop().doubleValue();
                stack.push(new BigDecimal(Math.sin(degToRad(a)), MathContext.DECIMAL64));
            } else if (x.equals("t")) {
                double a = stack.pop().doubleValue();
                stack.push(new BigDecimal(Math.tan(degToRad(a)), MathContext.DECIMAL64));
            } else if (x.equals("+")) {
                double b = stack.pop().doubleValue();
                double a = stack.pop().doubleValue();
                stack.push(new BigDecimal(a + b, MathContext.DECIMAL64));
            }
            else if (x.equals("-")) {
                double b = stack.pop().doubleValue();
                double a = stack.pop().doubleValue();
                stack.push(new BigDecimal(a - b, MathContext.DECIMAL64));
            } else if (x.equals("×")) {
                double b = stack.pop().doubleValue();
                double a = stack.pop().doubleValue();
                stack.push(new BigDecimal(a * b, MathContext.DECIMAL64));
            }
            else if (x.equals("÷")) {
                double b = stack.pop().doubleValue();
                double a = stack.pop().doubleValue();
                stack.push(new BigDecimal(a / b, MathContext.DECIMAL64));

            } else if (x.equals("u-")){
                double zero = 0.0;
                stack.push(new BigDecimal(zero - stack.pop().doubleValue()));
            }
            else stack.push(new BigDecimal(x));
        }
        return stack.pop();
    }

    private static double degToRad(double number) {
        return number * Math.PI / 180;
    }
}
