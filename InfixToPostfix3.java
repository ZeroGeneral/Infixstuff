/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package infixtopostfix;

import java.util.Scanner;
import java.util.Stack;

public class InfixToPostfix {
    
    // Check if a character is an operator
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '^';
    }

    // Get precedence of an operator
    private static int getPrecedence(char op) {
        switch (op) {
            case '+': case '-': return 1;
            case '*': case '/': case '%': return 2;
            case '^': return 3; // Higher precedence
            default: return -1;
        }
    }

    // Convert Infix to Postfix
    private static String infixToPostfix(String infix) {
        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();
        
        System.out.println("\nTOKEN (X) \t STACK (Y) \t OUTPUT (POSTFIX)");
        System.out.println("---------------------------------------------------");

        for (char token : infix.toCharArray()) {
            if (Character.isLetterOrDigit(token)) { // If operand (letter or number), append to postfix
                postfix.append(token).append(" ");
            } 
            else if (token == '(') { // If opening bracket, push to stack
                stack.push(token);
            } 
            else if (token == ')') { // If closing bracket, pop until '('
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.pop(); // Remove '('
            } 
            else if (isOperator(token)) { // If operator, handle precedence
                while (!stack.isEmpty() && getPrecedence(stack.peek()) >= getPrecedence(token)) {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.push(token);
            }

            System.out.printf("%-10s \t %-15s \t %-15s%n", token, stack, postfix);
        }

        while (!stack.isEmpty()) { // Pop remaining operators
            postfix.append(stack.pop()).append(" ");
        }

        return postfix.toString().trim();
    }

    // Convert Postfix to Infix
    private static String postfixToInfix(String postfix) {
        Stack<String> stack = new Stack<>();

        System.out.println("\nTOKEN (X) \t STACK (Y)");
        System.out.println("---------------------------");

        for (char token : postfix.toCharArray()) {
            if (Character.isLetterOrDigit(token)) { // If operand, push onto stack
                stack.push(String.valueOf(token));
            } 
            else if (isOperator(token)) { // If operator, pop two operands and form an infix expression
                String b = stack.pop();
                String a = stack.pop();
                String newExpression = "(" + a + " " + token + " " + b + ")";
                stack.push(newExpression);
            }
            System.out.printf("%-10s \t %-15s%n", token, stack);
        }

        return stack.pop(); // Final infix expression
    }

    // Evaluate Postfix Expression (if numeric)
    private static int evaluatePostfix(String postfix) {
        Stack<Integer> stack = new Stack<>();

        for (String token : postfix.split(" ")) {
            if (token.matches("\\d+")) { // If operand (number), push onto stack
                stack.push(Integer.parseInt(token));
            } 
            else if (isOperator(token.charAt(0))) { // If operator, pop two numbers and apply operation
                int b = stack.pop();
                int a = stack.pop();
                switch (token.charAt(0)) {
                    case '+': stack.push(a + b); break;
                    case '-': stack.push(a - b); break;
                    case '*': stack.push(a * b); break;
                    case '/': stack.push(a / b); break;
                    case '%': stack.push(a % b); break;
                    case '^': stack.push((int) Math.pow(a, b)); break;
                }
            }
        }

        return stack.pop(); // Final evaluated result
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char choice;

        do {
            System.out.println("\n===== Expression Converter =====");
            System.out.println("1. Infix to Postfix");
            System.out.println("2. Postfix to Infix");
            System.out.println("3. Evaluate Postfix Expression (Numbers Only)");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    System.out.print("\nEnter an Infix expression (supports letters & numbers): ");
                    String infix = scanner.nextLine().replaceAll(" ", ""); // Remove spaces
                    String postfixResult = infixToPostfix(infix);
                    System.out.println("\nPostfix Expression: " + postfixResult);
                    break;

                case 2:
                    System.out.print("\nEnter a Postfix expression (supports letters & numbers): ");
                    String postfix = scanner.nextLine().replaceAll(" ", ""); // Remove spaces
                    String infixResult = postfixToInfix(postfix);
                    System.out.println("\nInfix Expression: " + infixResult);
                    break;

                case 3:
                    System.out.print("\nEnter a Postfix expression (numbers only): ");
                    String postfixEval = scanner.nextLine();
                    try {
                        int result = evaluatePostfix(postfixEval);
                        System.out.println("\nEvaluated Result: " + result);
                    } catch (Exception e) {
                        System.out.println("\nInvalid expression! Please enter numeric values.");
                    }
                    break;

                case 4:
                    System.out.println("Exiting program...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option! Try again.");
            }

            System.out.print("\nTry again? (y/n): ");
            choice = scanner.next().charAt(0);
            scanner.nextLine(); // Consume newline

        } while (choice == 'y' || choice == 'Y');

        System.out.println("Program terminated.");
        scanner.close();
    }
}
