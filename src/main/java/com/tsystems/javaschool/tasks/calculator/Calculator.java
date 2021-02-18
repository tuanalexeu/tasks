package com.tsystems.javaschool.tasks.calculator;

import java.util.*;

public class Calculator {
    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        if(statement == null) return null;
        try {
            double result = calculateExpression(statement.replaceAll(" ", ""));

            // Here we also check if the final result is integer value. If so, round it.
            return result == Math.floor(result)
                    ? String.valueOf((int) result)
                    : String.valueOf(result);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method divides expression by + or - and add each member to the final sum.
     * In case of a member being a nested expression, we call calculate term to continue calculating it.
     * @param expression - Input math expression
     * @return - final sum
     */
    private double calculateExpression(String expression) {
        double sum = 0.0;
        for (String s : splitBy(expression, OpType.ADD_SUB)) {
            try {
                // If we member is a number, we can just add it
                sum += Double.parseDouble(s);
            } catch (Exception e) {
                // Otherwise, it's an expression - we need to calculate it
                sum += calculateMultipliers(s);
            }
        }
        return sum;
    }

    /**
     * Method divides expression by * or / (into multipliers or dividers) and calculates the final result.
     * A member can also be a nested expression (If it's inside parentheses)
     * @param expression - Input math expression, contains only * and / at the top level (Not in parentheses)
     * @return - final sum
     */
    private double calculateMultipliers(String expression) {
        List<String> elTerms = splitBy(expression, OpType.DIV_MUL);

        // The first member cannot follow by * or / sign, so we can just parse it into double
        // or calculate it (if it's an expression)
        String first = elTerms.get(0);

        // Your unit test didn't cover the case when we have expression like "1 + (2 + 3)"
        // where we have + or - sign before parentheses.
        // Without the checking below, I would pass all the tests, but get null as a result to the example above
        double sum;
        switch (first.charAt(0)) {
            case '+':
                sum = isExpression(first.substring(1))
                        ? calculateExpression(first.substring(2, first.length() - 1))
                        : Double.parseDouble(first);
                break;
            case '-':
                sum = isExpression(first.substring(1))
                        ? -calculateExpression(first.substring(2, first.length() - 1))
                        : Double.parseDouble(first);
                break;
            default:
                sum = isExpression(first)
                        ? calculateExpression(first.substring(1, first.length() - 1))
                        : Double.parseDouble(first);
        }



        // Then we consistently calculate the final result
        for (int i = 1; i < elTerms.size(); i++) {

            String cur = elTerms.get(i);

            switch (cur.charAt(0)) {
                case '/':
                    if(isExpression(cur.substring(1))) {
                        sum /= calculateExpression(cur.substring(2, cur.length() - 1));
                    } else {
                        sum /= Double.parseDouble(cur.substring(1));
                    }
                    break;
                case '*':
                    if(isExpression(cur.substring(1))) {
                        sum *= calculateExpression(cur.substring(2, cur.length() - 1));
                    } else {
                        sum *= Double.parseDouble(cur.substring(1));
                    }
                    break;
            }
        }

        // Handle dividing by zero (Double type doesn't throws RuntimeException itself, we get infinity instead)
        if(sum == Double.POSITIVE_INFINITY || sum == Double.NEGATIVE_INFINITY) {
            throw new RuntimeException();
        }

        return sum;
    }

    /**
     * Checks if a given string is expression inside brackets
     * @param s - string that needs checking
     * @return - true, if so
     */
    private boolean isExpression(String s) {
        return s.startsWith("(") && s.endsWith(")");
    }


    /**
     * Method splits a string by given delimiter (Depends on OpType) into members.
     * Each member (except, maybe, the first one) contains operator in the first position.
     * @param exp - given math expression
     * @param opType - Operator type
     * @return - list of members
     */
    private List<String> splitBy(String exp, OpType opType) {

        char[] delimiter = opType.getDelimiters();

        List<String> elExp = new ArrayList<>(); // List of math members.

        // With level variable we indicate the depth of current operator.
        // We only need to split expression by top operators (the ones that aren't inside parentheses)
        int level = 0;

        // We use start to simultaneously extract member from given string
        // We know the current index of an operator and we just get a substring from
        // the position of the previous operator up to the current index.
        int start = 0;

        for (int i = 0; i < exp.length(); i++) {
            if((exp.charAt(i) == delimiter[0] || exp.charAt(i) == delimiter[1]) && level == 0) {
                // By (i - start != 0) we check if we found the first + or - operator
                // Expression might start with + or - (e.g "-1 + (5 * 3)").
                // In that case we don't need to touch this operator and just calculate expression as it is.
                if(i - start != 0) {
                    String s = exp.substring(start, start = i);

                    // We need to check if we can continue calculation the member
                    if(!checkString(s)) {
                        throw new RuntimeException();
                    }
                    elExp.add(s);
                }
            } else if(exp.charAt(i) == '(') {
                level++;
            } else if(exp.charAt(i) == ')') {
                level--;
            }
        }

        // Indicates if unequal number of open & closing parentheses found
        if(level != 0) throw new RuntimeException();

        // We need to add the last member
        String s = exp.substring(start);
        if(checkString(s)) elExp.add(s);

        return elExp;
    }

    /**
     * Checks if given string is appropriate math member.
     * Basically, it checks if we use more than one operator in row
     * (If original string contains two operators in row (e.g "++5"),
     * after splitting we will get [+, +5] array, this method is to find such elements)
     * @param s - given math member
     * @return - false, if it's not.
     */
    private boolean checkString(String s) {
        Set<Character> set = new HashSet<>(Arrays.asList('*','/','-','+'));
        return !(s.length() == 1 && set.contains(s.charAt(0)));
    }
}
