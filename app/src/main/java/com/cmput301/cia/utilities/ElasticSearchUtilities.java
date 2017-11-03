package com.cmput301.cia.utilities;

import com.cmput301.cia.models.ElasticSearchable;
import com.searchly.jestdroid.JestDroidClient;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Version 2
 * Author: Adil Malik
 * Date: Nov 2 2017
 *
 * Contains utilities for searching from the ElasticSearch database
 */


public class ElasticSearchUtilities {

    // The index used to access the database
    private static final String INDEX = "CMPUT301F17T15";

    private static JestDroidClient client;

    // Adds tweets to elastic search
    // Get=NormalTweet, Progress=Void, Result=Void
    /*public static class AddTweetsTask extends AsyncTask<NormalTweet, Void, Void> {

        @Override
        protected Void doInBackground(NormalTweet... tweets) {
            verifySettings();

            for (NormalTweet tweet : tweets) {
                Index index = new Index.Builder(tweet).index("testing").type("tweet").build();

                try {
                    // where is the client?
                    DocumentResult execute = client.execute(index);
                    if (execute.isSucceeded()){
                        tweet.setId(execute.getId());
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the tweets");
                }

            }
            return null;
        }
    }

    // Gets tweets from elastic search
    public static class GetTweetsTask extends AsyncTask<String, Void, ArrayList<NormalTweet>> {
        @Override
        protected ArrayList<NormalTweet> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<NormalTweet> tweets = new ArrayList<NormalTweet>();

            // Build the query

            String query = "{\n" +
                    "    \"query\": {\"term\": {\"message\":\"" + search_parameters[0] + "\"}}\n" + "}";


            Search search = new Search.Builder(query).addIndex("testing").addType("tweet").build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<NormalTweet> foundTweets = result.getSourceAsObjectList(NormalTweet.class);
                    tweets.addAll(foundTweets);
                } else {
                    Log.i("Error", "the search query failed to find any tweets that matched");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return tweets;
        }
    }*/

    private static class InsertTask extends AsyncTask<ElasticSearchable, Void, Void> {

        @Override
        protected Void doInBackground(ElasticSearchable... objects) {
            verifySettings();

            for (ElasticSearchable obj : objects) {
                Index index = new Index.Builder(obj).index(INDEX).type(obj.getTypeId()).build();

                try {
                    DocumentResult execute = client.execute(index);
                    if (execute.isSucceeded()){
                        obj.setId(execute.getId());
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the objects");
                }

            }
            return null;
        }
    }

    private static class SearchTask extends AsyncTask<String, Void, SearchResult> {
        @Override
        protected SearchResult doInBackground(String... search_parameters) {
            verifySettings();

            String typeId = search_parameters[0];
            String query = search_parameters[1];

            // Build the query
            // TODO
            //String query = "{\n" +
            //        "    \"query\": {\"term\": {\"message\":\"" + search_parameters[0] + "\"}}\n" + "}";


            Search search = new Search.Builder(query).addIndex(INDEX).addType(typeId).build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    return result;
                } else {
                    Log.i("Error", "the search query failed to find any objects that matched");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return null;
        }
    }

    private static class IDSearchTask extends AsyncTask<String, Void, SearchResult> {
        @Override
        protected SearchResult doInBackground(String... search_parameters) {
            verifySettings();

            String typeId = search_parameters[0];
            String objectId = search_parameters[1];
            String query = search_parameters[2];

            // Build the query
            // TODO
            //String query = "{\n" +
            //        "    \"query\": {\"term\": {\"message\":\"" + search_parameters[0] + "\"}}\n" + "}";


            Search search = new Search.Builder(query).addIndex(INDEX).addType(typeId).build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    return result;
                } else {
                    Log.i("Error", "the search query failed to find any objects that matched");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return null;
        }
    }

    /**
     * Execute a search with ElasticSearch
     * @param typeId the type template id all results must match
     * @param query the query to execute
     * @return the search result from the query if results were found, or null otherwise
     */
    public static SearchResult search(String typeId, String query){
        try {
            return new SearchTask().execute(typeId, query).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Execute a search for a specific object with ElasticSearch
     * @param typeId the type template id all results must match
     * @param query the query to execute
     * @param id the id of the object to search for
     * @return the search result from the query if it was found, or null otherwise
     */
    public static SearchResult search(String typeId, String query, String id){
        try {
            return new IDSearchTask().execute(typeId, id, query).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Search for all records with the specified type ID
     * @param typeId the type template id that all results must match
     * @param tempClass the java class of the generic type T
     * @param <T> generic representing the java type corresponding to that type ID
     * @return the list of all records matching that type ID
     */
    public static <T extends ElasticSearchable> List<T> getListOf(String typeId, Class<T> tempClass){
        SearchResult result = search(typeId, "");   // TODO: query
        if (result != null && result.isSucceeded()) {
            // TODO: try get hits?
            return result.getSourceAsObjectList(tempClass);
        }
        return new ArrayList<>();
    }

    /**
     * Search for the record with the specified ID
     * @param typeId the type template id of the result
     * @param tempClass the java class of the generic type T
     * @param <T> generic representing the java type corresponding to that type ID
     * @return the record if found, or null otherwise
     */
    public static <T extends ElasticSearchable> T getObject(String typeId, Class<T> tempClass, String id){
        SearchResult result = search(typeId, "", id);   // TODO: query
        if (result != null && result.isSucceeded()) {
            return result.getSourceAsObject(tempClass);
        }
        return null;
    }

    /**
     * Save an object into the database
     * @param object the object to save
     * @param <T> generic representing the java type corresponding to that object's type
     */
    public static <T extends ElasticSearchable> void save(T object){
        new InsertTask().execute(object);
    }

    /**
     * Validate that the client is initialized
     */
    private static void verifySettings() {
        if (client == null) {

            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
        // TODO: if index has not been built
        // create it and also create the templates for Habit, HabitEvent, Profile
    }

}
