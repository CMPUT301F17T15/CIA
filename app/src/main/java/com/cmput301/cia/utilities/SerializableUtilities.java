/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 3 2017
 *
 * Contains utilities for saving and loading Serializable objects locally
 */

public class SerializableUtilities {

    /**
     * Save a serializable object
     * @param fileName the name of the file containing the object
     * @param filesDir the directory of the file (use this.getFilesDir() when calling from an Activity class)
     * @param s the object to save
     * @return whether the object was successfully saved or not
     */
    public static boolean save(String fileName, String filesDir, Serializable s){
        return save(new File(filesDir, fileName), s);
    }

    /**
     * Load a serializable object
     * @param fileName the name of the file containing the object
     * @param filesDir the directory of the file (use this.getFilesDir() when calling from an Activity class)
     * @return the object if successful, or null otherwise
     */
    public static <T extends Serializable> T load(String fileName, String filesDir){
        Serializable value = load(new File(filesDir, fileName));
        if (value != null)
            return (T)value;
        return null;
    }

    /**
     * Save a serializable object
     * @param file the file to save the object in
     * @param s the object to save
     * @return whether the object was saved successfully
     */
    private static boolean save(File file, Serializable s){
        boolean success = false;
        try {
            FileOutputStream os = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
            objectOutputStream.writeObject(s);
            success = true;
            objectOutputStream.close();
            os.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

        return success;
    }

    /**
     * Load a serializable object
     * @param file the file the object is saved in
     * @return the object if successful, or null otherwise
     */
    private static Serializable load(File file){
        Serializable value = null;
        try {
            FileInputStream is = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(is);
            value = (Serializable) objectInputStream.readObject();
            objectInputStream.close();
            is.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        }
        return value;
    }


}
