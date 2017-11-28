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
import java.util.List;

/**
 * @author Adil Malik
 * @version 1
 * Date: Nov 3 2017
 *
 * Contains utilities for saving and loading Serializable objects locally
 */

public class SerializableUtilities {

    private static String DIR;

    /**
     * Initialize the directory files are stored in
     * Call this once when the program starts
     * @param dir the directory where files will be stored
     */
    public static void initializeFilesDir(String dir){
        if (DIR == null)
            DIR = dir;
    }

    /**
     * Save a serializable object
     * @param fileName the name of the file containing the object
     * @param s the object to save
     * @return whether the object was successfully saved or not
     */
    public static <T extends Serializable> boolean save(String fileName, List<T> s){
        return save(new File(DIR, fileName), s);
    }

    /**
     * Load a serializable object
     * @param fileName the name of the file containing the object
     * @return the object if successful, or null otherwise
     */
    public static <T extends Serializable> T load(String fileName){
        Serializable value = load(new File(DIR, fileName));
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
    private static <T extends Serializable> boolean save(File file, List<T> s){
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }


}
