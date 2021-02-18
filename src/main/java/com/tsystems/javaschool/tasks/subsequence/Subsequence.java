package com.tsystems.javaschool.tasks.subsequence;

import java.util.List;

public class Subsequence {

    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    public boolean find(List x, List y) {
        if(x == null || y == null) throw new IllegalArgumentException();

        int curY = 0;
        for (Object curX: x) {
            try {
                while (!curX.equals(y.get(curY))) {
                    curY++;
                }
            } catch (IndexOutOfBoundsException e) {
                // IndexOutOfBoundsException means that we went out of bounds before we found all the equal Y elements
                return false;
            }
        }
        return true;
    }
}
