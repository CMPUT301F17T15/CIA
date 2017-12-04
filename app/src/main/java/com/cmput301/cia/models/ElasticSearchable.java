/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;

import io.searchbox.annotations.JestId;

/**
 * @author Adil Malik
 * @version 1
 * Date: Nov 2 2017
 *
 * Represents an object that can be stored and retrieved from an ElasticSearch database
 */

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
        return id != null && !NumberUtils.isNumber(id);
    }

    /**
     * Serialize this object to the ElasticSearch server
     * @return whether the object was saved successfully or not
     */
    public abstract boolean save();

    /**
     * Synchronize this object from the ElasticSearch server
     */
    public abstract void load();

    /**
     * Delete this object from the ElasticSearch server
     */
    public abstract void delete();

    /**
     *
     * @param other the object to check whether this one equals or not
     * @return whether this object equals the specified one
     */
    @Override
    public boolean equals(Object other){
        if (other instanceof ElasticSearchable && hasValidId() && ((ElasticSearchable) other).hasValidId()){
            return getId().equals(((ElasticSearchable) other).getId());
        }
        return super.equals(other);
    }

}
