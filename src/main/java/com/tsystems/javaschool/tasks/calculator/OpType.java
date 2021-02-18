package com.tsystems.javaschool.tasks.calculator;

/**
 * Enum keeps delimiters for addition/subtraction or multiplication/division
 */
public enum OpType {
    ADD_SUB("+-".toCharArray()),
    DIV_MUL("*/".toCharArray());

    private final char[] delimiters;

    OpType(char[] delimiters) {
        this.delimiters = delimiters;
    }

    public char[] getDelimiters() {
        return delimiters;
    }
}
