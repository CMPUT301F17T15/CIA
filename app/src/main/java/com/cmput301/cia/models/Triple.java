/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import java.io.Serializable;

/**
 * @author Adil Malik
 * @version 1
 * Date: Nov 29 2017
 *
 * A storage class that stores three generic objects for easy returning of 3 values
 */

public class Triple<X extends Serializable, Y extends Serializable, Z extends Serializable> implements Serializable{

    public X first;
    public Y second;
    public Z third;

    public Triple(X one, Y two, Z three){
        first = one;
        second = two;
        third = three;
    }

}
