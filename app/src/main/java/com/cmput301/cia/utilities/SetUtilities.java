/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.utilities;

import java.util.Set;

/**
 * @author Adil Malik
 * @version 1
 * Date: Nov 11 2017
 *
 * Contains utilities for sets
 */

public class SetUtilities {

    /**
     * @param set the set to search through
     * @param index the index to the return thei tem at
     * @param <T> generic type of the set's elements
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index was out of bounds
     */
    public static <T> T getItemAtIndex(Set<T> set, int index){
        int i = 0;
        for (T value : set){
            if (index == i)
                return value;
            ++i;
        }

        throw new IndexOutOfBoundsException();
    }

}
