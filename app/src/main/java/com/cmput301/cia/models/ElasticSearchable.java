/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import java.io.Serializable;

import io.searchbox.annotations.JestId;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 2 2017
 *
 * Represents an object that can be stored and retrieved from an ElasticSearch database
 */

// TODO: more testing to make sure serializable does not mess something up with ElasticSearch
public abstract class ElasticSearchable implements Serializable {

    @JestId
    private String id;

    /**
     * @return the object's template type id
     */
    public abstract String getTypeId();

    /**
     * @return the object's unique hash
     */
    public String getId() {
        return id;
    }

    /**
     * Set the object's unique hash
     * @param id the object's unique hash
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * @return whether this item has a valid ElasticSearch ID or not
     */

    public boolean hasValidId(){
        return id != null;
    }

    /**
     * Serialize this object to the ElasticSearch server
     */
    public abstract void save();

    /**
     * Synchronize this object from the ElasticSearch server
     */
    public abstract void load();

    /**
     * Delete this object from the ElasticSearch server
     */
    public abstract void delete();

    @Override
    public boolean equals(Object other){
        if (other instanceof ElasticSearchable && hasValidId() && ((ElasticSearchable) other).hasValidId()){
            return getId().equals(((ElasticSearchable) other).getId());
        }
        return super.equals(other);
    }

}
