// Instructor:          Professor Cronin
// Student:             Yenchi Kuo
// Description:         Solving Fibonacci Sequence with difference approaches
//                      more stack-efficient solution for solving Fibonacci Sequences. The solution includes:
//                      1.For-loop method
//                      2.Plain recursion method
//                      3.Tail recursion method
//                      4.Memorization recursion method
// Last Modified:       02/06/2022 1923 PM

import java.math.BigInteger;
import java.util.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        boolean continueProgram = true;
        HashMap<BigInteger, BigInteger> memo = new HashMap<>();

        printCommandList();
        System.out.println("--------------------------------------------------------\n");

        // This Do-While Loop allows user to choose different approaches to
        // find the number of Fibonacci Sequence with the assigned index
        // Algorithms included:
        // 1.For-Loop
        // 2.Plain Recursion
        // 3.Tail Recursion
        // 4.Dynamic Programming Recursion
        do {
            char command = getCommand();

            switch (command) {
                // Case A uses FOR-LOOP to find the number corresponding to Fibonacci Sequence's order
                // Handles stack overflow exception and prompts the user again for commands
                case 'a':
                    try {
                        System.out.println("Tabulation method (bottom-up) selected.");
                        BigInteger testNum = getNum();
                        System.out.println("Calling FOR-LOOP function...");
                        long start = System.currentTimeMillis();
                        for (int i = 0; i <= testNum.intValue(); i++) {
                            System.out.println("Fib(" + i + ") = " + iterateFib(BigInteger.valueOf(i)));
                        }
                        long finish = System.currentTimeMillis();
                        System.out.println("Total time used: " + (finish - start) + " milliseconds.\n");
                    } catch (StackOverflowError t) {
                        System.out.println("Warning! Stack overflow exception thrown.");
                        System.out.println("The number of Fibonacci Sequence's order is too high.");
                    }
                    break;

                // Case B uses PLAIN RECURSION to find the number corresponding to Fibonacci Sequence's order
                // Handles stack overflow exception and prompts the user again for commands
                case 'b':
                    try {
                        System.out.println("Plain Recursion method selected.");
                        BigInteger testNum = getNum();
                        long start = System.currentTimeMillis();
                        System.out.println("Calling the PLAIN RECURSION function...");
                        for (int i = 0; i <= testNum.intValue(); i++) {
                            System.out.println("Fib(" + i + ") = " + recurFib(BigInteger.valueOf(i)));
                        }
                        long finish = System.currentTimeMillis();

                        System.out.println("Total time used: " + (finish - start) + " milliseconds.\n");
                    } catch (StackOverflowError t) {
                        System.out.println("Warning! Stack overflow exception thrown.");
                        System.out.println("The number of Fibonacci Sequence's order is too high.");
                    }
                    break;

                // Case C uses TAIL RECURSION to find the number corresponding to Fibonacci Sequence's order
                // Handles stack overflow exception and prompts the user again for commands
                case 'c':
                    try {
                        System.out.println("Tail Recursion method selected.");
                        BigInteger testNum = getNum();
                        long start = System.currentTimeMillis();
                        System.out.println("Calling the TAIL RECURSION function...");

                        // Printing out all the numbers correspond to the nth order of Fibonacci Sequence
                        for (int i = 0; i <= testNum.intValue(); i++) {
                            System.out.println("Fib(" + i + ") = " + tailRecurFib(BigInteger.valueOf(i), BigInteger.ZERO, BigInteger.ONE));
                        }
                        long finish = System.currentTimeMillis();
                        System.out.println("Total time used: " + (finish - start) + " milliseconds.\n");
                    } catch (StackOverflowError t) {
                        System.out.println("Warning! Stack overflow exception thrown.");
                        System.out.println("The number of Fibonacci Sequence's order is too high.");
                    }
                    break;

                // Case D uses MEMORIZATION RECURSION to find the number corresponding to Fibonacci Sequence's order
                // Handles stack overflow exception and prompts the user again for commands
                case 'd':
                    try {
                        System.out.println("Memoization Recursion selected.");
                        BigInteger testNum = getNum();
                        long start = System.currentTimeMillis();
                        System.out.println("Calling MEMORIZATION RECURSION function...");

                        for (int i = 0; i <= testNum.intValue(); i++) {
                            System.out.println("Fib(" + i + ") = " + memorizeFib(BigInteger.valueOf(i), memo));
                        }
                        long finish = System.currentTimeMillis();
                        System.out.println("Total time used: " + (finish - start) + " milliseconds.\n");
                    } catch (StackOverflowError t) {
                        System.out.println("Warning! Stack overflow exception thrown.");
                        System.out.println("The number of Fibonacci Sequence's order is too high.");
                    }
                    break;

                // Case H will print out the command list
                case 'h':
                    printCommandList();
                    break;
                case 'x':
                    continueProgram = false;
                    System.out.print("Thank you for using Aurelius' Product, exiting now...");
                    Thread.sleep(2000);
            }
        } while (continueProgram);
    }

    /**
     * This method endeavors to solve Fibonacci Sequence using for-loop
     * Tabulation Method (Bottom-up Approach)
     * @param n BigInteger
     * @return Biginteger
     */
    public static BigInteger iterateFib(BigInteger n) {
        BigInteger a = BigInteger.ZERO;
        BigInteger b = BigInteger.ONE;
        BigInteger c, i;

        // if the input number is 0, return the base case.
        if (n.equals(BigInteger.ZERO))
            return a;

        // Starting with 2 because the first two cases were covered
        // by integers a and b's initial values
        // The three integers serve in a way similar to a linked list (a-b-c)
        // where in each iteration each of the three integers update its value
        for (i = BigInteger.TWO; i.compareTo(n) < 0 || i.compareTo(n) == 0; i = i.add(BigInteger.ONE)) {
            c = a.add(b);
            a = b;
            b = c;
        }
        return b;
    }

    /**
     * This function endeavors to solve the Fibonacci Sequence
     * with the given order using plain recursive method
     * @param inputNum BigInteger
     * @return BigInteger
     */
    public static BigInteger recurFib(BigInteger inputNum) {
        // Base case
        if (inputNum.compareTo(BigInteger.ONE) < 0 || inputNum.compareTo(BigInteger.ONE) == 0)
            return inputNum;

        // Recursively calls itself with the two positions (previous number + current number)
        // Which results in repeating the same calculations
        return recurFib(inputNum.subtract(BigInteger.ONE)).add(recurFib(inputNum.subtract(BigInteger.TWO)));
    }

    /**
     * This function bendeavors to solve the designated order of Fibonacci Sequence using tail recursion approach
     * Reference: https://www.youtube.com/watch?v=_JtPhF8MshA
     *
     * @param inputNum int user designated order
     * @param a        BigInteger
     * @param b        BigInteger
     * @return BigInteger
     */
    public static BigInteger tailRecurFib(BigInteger inputNum, BigInteger a, BigInteger b) {
        if (inputNum.equals(BigInteger.ZERO))
            return a;
        if (inputNum.equals(BigInteger.ONE))
            return b;

        // Tail Recursive call where integer b is the current number and a + b is the next number
        return tailRecurFib(inputNum.subtract(BigInteger.ONE), b, a.add(b));
    }

    /**
     * This function endeavors to solve the designated order of Fibonacci Sequence
     * Using the combination of dynamic programming approach and mappings.
     * For the map object, its keys will be the argument to the function,
     * whereas the value will be the return value
     *
     * @return BigInteger
     */
    public static BigInteger memorizeFib(BigInteger n, HashMap<BigInteger, BigInteger> memo) {
        // Base case
        if (n.compareTo(BigInteger.TWO) < 0 || n.compareTo(BigInteger.TWO) == 0) return BigInteger.ONE;

        // Search existing results from the HashMap memo, if it contains the nth order,
        // Return the value of the order(key)
        if (memo.containsKey(n)) return memo.get(n);

        // Putting new Keys and Values into the HashMap, namely Memorization
        else {
            BigInteger result = memorizeFib(n.subtract(BigInteger.ONE), memo).add(memorizeFib(n.subtract(BigInteger.TWO), memo));
            memo.put(n, result);
            return result;
        }
    }

    /**
     * This method will print out a list of commands
     */
    public static void printCommandList() {
        System.out.println("""
                1.For-loop. Command: A
                2.Plain Recursion. Command: B.
                3.Tail Recursion. Command: C.
                4.Dynamic Programming Recursion. Command: D.
                5.Print Command List. Command: H.
                6.Exit Program. Command: X.""");
    }

    /**
     * This method will get the command from the user, also validates input
     *
     * @return char
     */
    public static char getCommand() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter command (enter H for command list):");
        String inputCommand = scanner.next();
        inputCommand = inputCommand.toLowerCase();
        char command = inputCommand.charAt(0);

        while (command != 'a' && command != 'b' && command != 'c' && command != 'd' && command != 'h' && command != 'x') {
            System.out.println("Invalid input. Please enter the command to see the specified method, or hit 'H' for command list.");
            inputCommand = scanner.next();
            inputCommand = inputCommand.toLowerCase();
            command = inputCommand.charAt(0);
        }
        return command;
    }

    /**
     * This method will get the designated index of Fibonacci Sequence from the user, also validates input
     *
     * @return BigInteger
     */
    public static BigInteger getNum() {

        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        boolean validInput = true;

        do {
            System.out.println("Please enter an index number of Fibonacci Sequence:");
            String userInput = scanner.next();
            try {
                char[] chInput = userInput.toCharArray();
                for (char c : chInput) {
                    if (Character.isDigit(c)) {
                        sb.append(c);
                    } else throw new NumberFormatException("Invalid Input. Please enter a number!");
                }

                validInput = false;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                sb.setLength(0);
            }
        } while (validInput);
        return new BigInteger(sb.toString());
    }
}
