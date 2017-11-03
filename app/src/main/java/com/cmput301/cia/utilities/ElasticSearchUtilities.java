package com.cmput301.cia.utilities;

import com.searchly.jestdroid.JestDroidClient;

import android.os.AsyncTask;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
                    //Log.i("Error", "the search query failed to find any tweets that matched");
                }
            }
            catch (Exception e) {
                //Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return null;
        }
    }

    /**
     * Execute a search with ElasticSearch
     * @param typeId the type template id all results must match
     * @param query the query to execute
     * @return the search result from the query
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
     * Search for all records with the specified type ID
     * @param typeId the type template id all results must match
     * @param tempClass the java class of the generic type T
     * @param <T> generic representing the java type corresponding to that type ID
     * @return list of all records matching that type ID
     */
    public static <T> List<T> getList(String typeId, Class<T> tempClass){
        SearchResult result = search(typeId, "");
        if (result.isSucceeded()) {
            // TODO: try get hits?
            List<T> sourceAsObjectList = result.getSourceAsObjectList(tempClass);
            return sourceAsObjectList;
        }
        return new ArrayList<>();
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
    }

}
