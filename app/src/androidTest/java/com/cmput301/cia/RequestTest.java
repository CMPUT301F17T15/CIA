/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import android.test.ActivityInstrumentationTestCase2;

import com.cmput301.cia.models.Request;

/**
 * Created by WTH on 2017-10-22.
 */

public class RequestTest extends ActivityInstrumentationTestCase2 {
    public RequestTest(){
        super(Request.class);
    }

    public void testgetName(){
        Request request = new Request("name");
        request.setName("requestName");
        assertEquals(request.getName(), "requestName");
    }
}
