package com.cmput301.cia.utilities;

import com.cmput301.cia.models.ElasticSearchable;
import com.google.common.collect.Iterables;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.searchly.jestdroid.JestDroidClient;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * @author Adil Malik
 * @version 2
 * Date: Nov 2 2017
 *
 * Contains utilities for searching from the ElasticSearch database
 */

public class ElasticSearchUtilities {

    // The index used to access the database
    private static final String INDEX = "cmput301f17t15_cia";
    // Maximum number of search results per query
    private static final int MAX_RESULTS = 1000;
    private static JestDroidClient client;

    /**
     * Asynchronous Task for inserting/updating an object into the database
     */
    // TODO: test case where the insert fails
    private static class InsertTask extends AsyncTask<ElasticSearchable, Void, Boolean> {

        @Override
        protected Boolean doInBackground(ElasticSearchable... objects) {
            verifySettings();

            for (ElasticSearchable obj : objects) {
                Index.Builder builder = new Index.Builder(obj).index(INDEX).type(obj.getTypeId());

                // update instead if this object is already in the database
                if (obj.hasValidId()){
                    builder.id(obj.getId());
                }

                Index index = builder.build();

                try {
                    DocumentResult execute = client.execute(index);
                    if (execute.isSucceeded()){
                        obj.setId(execute.getId());
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the objects");
                    return Boolean.FALSE;
                }

            }
            return Boolean.TRUE;
        }
    }

    /**
     * Asynchronous Task for searching for an object in the database through parameters in the source
     */
    private static class SearchTask extends AsyncTask<String, Void, SearchResult> {
        @Override
        protected SearchResult doInBackground(String... search_parameters) {
            verifySettings();

            String typeId = search_parameters[0];
            String finalQuery = getCompleteQuery(search_parameters[1]);

            Search search = new Search.Builder(finalQuery).addIndex(INDEX).addType(typeId).build();

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
     * Asynchronous Task for searching for an object in the database using it's unique ID
     */
    private static class IDSearchTask extends AsyncTask<String, Void, SearchResult> {
        @Override
        protected SearchResult doInBackground(String... search_parameters) {
            verifySettings();

            String typeId = search_parameters[0];
            String objectId = search_parameters[1];
            String query = "{\"query\":{\"match\":{\"_id\":\"" + objectId + "\"}}}}";

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
     * Asynchronous Task for deleting an object from the database with it's unique ID
     */
    private static class DeleteTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... search_parameters) {
            verifySettings();

            String typeId = search_parameters[0];
            String objectId = search_parameters[1];
            String query = search_parameters[2];

            Delete delete = new Delete.Builder(query).index(INDEX).type(typeId).id(objectId).build();

            try {
                // TODO: test success
                client.execute(delete);
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return null;
        }
    }

    /**
     * Asynchronous task for deleting all records matching a query
     */
    // TODO: test
    private static class DeleteSearchTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... search_parameters) {
            verifySettings();

            String typeId = search_parameters[0];
            String query = getCompleteQuery(search_parameters[1]);

            Delete delete = new Delete.Builder(query).index(INDEX).type(typeId).build();

            try {
                // TODO: test success
                client.execute(delete);
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
     * Execute a search for a specific object with ElasticSearch
     * @param typeId the type template id all results must match
     * @param values map where key=parameter and value=required record value for that parameter
     * @return the search result from the query if it was found, or null otherwise
     */
    public static SearchResult search(String typeId, Map<String, String> values){
        return search(typeId, getQueryFromMap(values));
    }

    /**
     * Search for all records with the specified type parameter values
     * @param typeId the type template id that all results must match
     * @param tempClass the java class of the generic type T
     * @param values map where key=parameter and value=required record value for that parameter
     * @param <T> generic representing the java type corresponding to that type ID
     * @return the list of all records matching that type ID with the required parameter values
     */
    public static <T extends ElasticSearchable> List<T> getListOf(String typeId, Class<T> tempClass, Map<String, String> values){
        SearchResult result = search(typeId, getQueryFromMap(values));
        if (result != null && result.isSucceeded()) {

            List<T> found = result.getSourceAsObjectList(tempClass);

            // transform result into json
            JsonObject jo = result.getJsonObject();

            // get array of only the records in the database, and not the other parts of the result object
            JsonArray array = jo.get("hits").getAsJsonObject().get("hits").getAsJsonArray();

            // look at each record individually
            for (int i = 0; i < array.size(); ++i){
                JsonObject record = array.get(i).getAsJsonObject();
                // set the id of this object since jest does not do it automatically
                found.get(i).setId(record.get("_id").getAsString());
            }
            return found;
        }

        return new ArrayList<>();
    }

    /**
     * Search for the record with the specified ID
     * @param typeId the type template id of the result
     * @param tempClass the java class of the generic type T
     * @param id the id an object must have to be considered a match
     * @param <T> generic representing the java type corresponding to that type ID
     * @return the record if found, or null otherwise
     */
    public static <T extends ElasticSearchable> T getObject(String typeId, Class<T> tempClass, String id){
        SearchResult result = search(typeId, "", id);
        if (result != null && result.isSucceeded()) {
            T object = result.getSourceAsObject(tempClass);
            // TODO: recursive setId (ex: for profile, does not set the ids of it's habits)
            // appears to be fixed
            object.setId(id);
            return object;
        }
        return null;
    }

    /**
     * Search for the record with the specified parameter values
     * @param typeId the type template id of the result
     * @param tempClass the java class of the generic type T
     * @param values map where key=parameter and value=required record value for that parameter
     * @param <T> generic representing the java type corresponding to that type ID
     * @return the record if found, or null otherwise
     */
    public static <T extends ElasticSearchable> T getObject(String typeId, Class<T> tempClass, Map<String, String> values){
        SearchResult result = search(typeId, getQueryFromMap(values));
        if (result != null && result.isSucceeded()) {

            JsonObject jo = result.getJsonObject();

            // array of all of the records that match the search parameters
            JsonArray array = jo.get("hits").getAsJsonObject().get("hits").getAsJsonArray();
            if (array.size() == 0)
                return null;

            String foundId = array.get(array.size() - 1).getAsJsonObject().get("_id").getAsString();

            // use the last object as the source
            T obj = result.getSourceAsObjectList(tempClass).get(array.size() - 1);//result.getSourceAsObject(tempClass);
            // TODO: recursive setId (ex: for profile, does not set the ids of it's habits)
            // appears to be fixed
            obj.setId(foundId);
            return obj;
        }
        return null;
    }

    /**
     * Save an object into the database
     * @param object the object to save
     * @param <T> generic representing the java type corresponding to that object's type
     */
    public static <T extends ElasticSearchable> boolean save(T object){
        try {
            return new InsertTask().execute(object).get().booleanValue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete an object from the database
     * @param typeId the type template id of the result
     * @param objId the id of the object to search for
     */
    public static void delete(String typeId, String objId) {
        new DeleteTask().execute(typeId, objId, "");
    }

    /**
     * Delete an object from the database
     * @param object the object to delete
     */
    public static void delete(ElasticSearchable object) {
        new DeleteTask().execute(object.getTypeId(), object.getId(), "");
    }

    /**
     * Delete all records with the specified parameter values
     * @param typeId the type template id of the result
     * @param values map where key=parameter and value=required record value for that parameter
     */
    public static void delete(String typeId, Map<String, String> values){
        new DeleteSearchTask().execute(typeId, getQueryFromMap(values));
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

    /**
     * Convert a map of parameters to a query
     * @param values map where key=parameter and value=required record value for that parameter
     * @return the map as a string representing a query
     */
    private static String getQueryFromMap(Map<String, String> values){

        if (values.size() == 0)
            return "";

        StringBuilder query = new StringBuilder();

        Set<String> keyset = values.keySet();
        String last = Iterables.getLast(keyset);
        for (String key : keyset){
            // TODO: test when there are multiple parameters
            query.append("\""+key+"\"" + ":" + "\""+values.get(key)+"\"");
            if (key != last)
                query.append(",\n");
        }
        return query.toString();
    }

    /**
     * Get a complete ElasticSearch query
     * @param parameters the match parameters that results must have
     * @return the completed query
     */
    private static String getCompleteQuery(String parameters){
        StringBuilder builder = new StringBuilder();

        // TODO: handle case when there is more than max results
        builder.append("{\"size\": " + MAX_RESULTS + ",");

        // find all
        if (parameters.equals("")){
            builder.append("\n\"query\": {\"match_all\": {}");
            builder.append("}}\n}");
        } else {
            builder.append("\n\"query\": {\"term\":{");
            builder.append(parameters);
            builder.append("}}\n}");
        }

        return builder.toString();
    }

}
