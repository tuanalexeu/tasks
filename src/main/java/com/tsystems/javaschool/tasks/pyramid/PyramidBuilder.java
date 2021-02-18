package com.tsystems.javaschool.tasks.pyramid;

import java.util.Collections;
import java.util.List;

public class PyramidBuilder {
    /**
     * Builds a pyramid with sorted values (with minimum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        try {
            Collections.sort(inputNumbers);

            // Check if input array's length is appropriate to build the pyramid.
            int length = inputNumbers.size();
            int row = -1; // this variable is to be used to calculate height & width.
            while (length > 0) {
                length -= ++row;
            }

            // In case of the calculated length not being equal to zero - the pyramid build isn't possible.
            if(length != 0) {
                throw new CannotBuildPyramidException();
            } else {
                int[][] result = new int[row][(row * 2) - 1];
                int cur = 0; // Keep index of current element of input array

                for (int j = 0; j < result.length; j++) {
                    for (int k = 0; k < (j * 2) + 1; k += 2) {
                        result[j][k + (row - 1)] = inputNumbers.get(cur++);
                    }
                    row--;
                }
                return result;
            }
        } catch (NullPointerException | OutOfMemoryError e) {
            throw new CannotBuildPyramidException();
        }
    }
}
