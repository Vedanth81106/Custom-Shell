package org.waffles.commands;

public class MathCommand implements Command {

    @Override
    public void execute(String... args) {

        if (args.length < 4) {
            System.out.println("Usage: math [num1] [operator] [num2]");
            System.out.println("Example: math 5 + 5");
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
                        System.out.println("Error: Cannot divide by zero.");
                        return;
                    }
                    result = num1 / num2;
                    break;
                default:
                    System.out.println("Invalid operator. Use +, -, *, /");
                    return;
            }

            System.out.println("Result: " + result);

        } catch (NumberFormatException e) {
            System.out.println("Error: Please provide valid numbers.");
        }
    }
}