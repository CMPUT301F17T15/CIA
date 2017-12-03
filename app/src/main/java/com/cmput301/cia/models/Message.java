/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import java.util.List;

/**
 * Created by guanfang on 2017/12/2.
 */

public class Message {

    private String viewer;
    private String displayed;
    private List<String> messageList;

    public void setViewer(String name){
        this.viewer = name;
    }

    public void setDisplayed(String name){
        this.displayed = name;
    }

    public String getViewer(){
        return viewer;
    }

    public String getDisplayed(){
        return displayed;
    }

    public void addMessage(String message){
        this.messageList.add(message);
    }

    public List<String> getMessageList(){
        return messageList;
    }
}
