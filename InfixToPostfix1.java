

import java.util.Scanner;
import java.util.Stack;

public class InfixToPostfix1 {

    // Method to get precedence of operators
    private static int precedence(char operator) {
        switch (operator) {
            case '^': return 3;
            case '*': case '/': case '%': return 2;
            case '+': case '-': return 1;
            default: return 0;
        }
    }

    // Method to check if a character is an operator
    private static boolean isOperator(char c) {
        return "+-*/%^".indexOf(c) != -1;
    }

    // Method to convert infix expression to postfix and display the steps
    private static String infixToPostfix(String infix) {
        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();

        System.out.println("\nTOKEN (X) \t STACK (Y) \t OUTPUT (POSTFIX)");
        System.out.println("-----------------------------------------------------");

        for (char token : infix.toCharArray()) {
            if (Character.isLetterOrDigit(token)) { 
                postfix.append(token).append(" ");
            } 
            else if (token == '(') {
                stack.push(token);
            } 
            else if (token == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.pop(); // Remove '('
            } 
            else if (isOperator(token)) {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(token)) {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.push(token);
            }

            // Print step-by-step process
            System.out.printf("%-10s \t %-15s \t %s%n", token, stack, postfix);
        }

        // Pop remaining operators in stack
        while (!stack.isEmpty()) {
            postfix.append(stack.pop()).append(" ");
        }
        System.out.printf("# (end) \t %-15s \t %s%n", stack, postfix);

        return postfix.toString().trim();
    }

    // Method to evaluate postfix expression and show step-by-step simulation
    private static Integer evaluatePostfix(String postfix) {
        Stack<Integer> stack = new Stack<>();
        String[] tokens = postfix.split(" ");

        System.out.println("\nSimulation:");
        System.out.println("---------------------------------");

        for (String token : tokens) {
            if (token.matches("\\d+")) {  // If it's a number
                stack.push(Integer.parseInt(token));
            } else if (isOperator(token.charAt(0))) { // If it's an operator
                if (stack.size() < 2) {
                    System.out.println("Error: Not enough operands for operator '" + token + "'");
                    return null; // Return null for invalid expression
                }
                int b = stack.pop();
                int a = stack.pop();
                switch (token) {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "/": stack.push(a / b); break;
                    case "%": stack.push(a % b); break;
                    case "^": stack.push((int) Math.pow(a, b)); break;
                }
            } else {
                System.out.println("Error: Invalid token '" + token + "'");
                return null;
            }

            System.out.println(stack); // Show the stack evolution
        }

        return stack.isEmpty() ? null : stack.pop(); // Final result
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char choice;

        do {
            System.out.print("\nEnter an infix expression: ");
            String infix = scanner.nextLine().replaceAll(" ", ""); // Remove spaces

            // Convert infix to postfix
            String postfix = infixToPostfix(infix);
            System.out.println("\nPostfix Expression: " + postfix);

            // Check if the expression is evaluatable (only numbers allowed)
            if (postfix.matches(".*[A-Za-z].*")) {
                System.out.println("\nSkipping evaluation: Expression contains variables.");
            } else {
                // Evaluate the postfix expression
                Integer result = evaluatePostfix(postfix);
                if (result != null) {
                    System.out.println("\nFinal Answer: " + result);
                }
            }

            // Ask if user wants to try again
            System.out.print("\nTry again? (y/n): ");
            choice = scanner.next().charAt(0);
            scanner.nextLine(); // Consume newline

        } while (choice == 'y' || choice == 'Y');

        System.out.println("Program terminated.");
        scanner.close();
    }
}
