package org.waffles.commands;

import org.waffles.Colors;

public class MathCommand implements Command {

    @Override
    public void execute(String... args) {

        if (args.length < 4) {
            System.out.println(Colors.RED + "Usage: math [num1] [operator] [num2]" + Colors.RESET);
            System.out.println(Colors.RED + "Example: math 5 + 5" + Colors.RESET);
            return;
        }

        try {
            // args[0] is "math", so we start at 1
            double num1 = Double.parseDouble(args[1]);
            String operator = args[2];
            double num2 = Double.parseDouble(args[3]);
            double result = 0;

            switch (operator) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "*":
                    result = num1 * num2;
                    break;
                case "/":
                    if (num2 == 0) {
                        System.out.println(Colors.RED + "Error: Cannot divide by zero." + Colors.RESET);
                        return;
                    }
                    result = num1 / num2;
                    break;
                default:
                    System.out.println(Colors.RED + "Invalid operator. Use +, -, *, /" + Colors.RESET);
                    return;
            }

            System.out.println("Result: " + result);

        } catch (NumberFormatException e) {
            System.out.println(Colors.RED + "Error: Please provide valid numbers." + Colors.RESET);
        }
    }
}