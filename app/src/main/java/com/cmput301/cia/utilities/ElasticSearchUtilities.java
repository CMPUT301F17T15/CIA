package com.cmput301.cia.utilities;

import com.cmput301.cia.models.ElasticSearchable;
import com.google.common.collect.Iterables;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.searchly.jestdroid.JestDroidClient;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.searchbox.action.BulkableAction;
import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * @author Adil Malik
 * @version 4
 * Date: Nov 24 2017
 *
 * Contains utilities for searching for, inserting into, and deleting from the ElasticSearch database
 */

public class ElasticSearchUtilities {

    // The index used to access the database
    private static final String INDEX = "cmput301f17t15_cia";

    // Maximum number of search results per query
    private static final int MAX_RESULTS = 10000;

    // The maximum time in milliseconds before a query is cancelled
    private static final long MAX_QUERY_TIME = 5000;

    private static JestDroidClient client;

    /**
     * Asynchronous Task for inserting/updating an object into the database
     *
     * Takes in a list of objects to insert
     * @return true if all of the objects were successfully inserted, false otherwise
     *
     */
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
     *
     * Takes in 2 string parameters:
     * - The first one is the type id of the object to search for
     * - The second one is the parameter values any result must have (ex: "name":"xyz")
     *
     * @return {@nullable result, success} as a pair
     */
    private static class SearchTask extends AsyncTask<String, Void, Pair<SearchResult, Boolean>> {
        @Override
        protected Pair<SearchResult, Boolean> doInBackground(String... search_parameters) {
            verifySettings();

            String typeId = search_parameters[0];
            String finalQuery = getCompleteQuery(search_parameters[1]);
            Search search = new Search.Builder(finalQuery).addIndex(INDEX).addType(typeId).build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    return new Pair<>(result, Boolean.TRUE);
                } else {
                    Log.i("Error", "the search query failed to find any objects that matched");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
                return new Pair<>(null, Boolean.FALSE);
            }

            return new Pair<>(null, Boolean.TRUE);
        }
    }

    /**
     * Asynchronous Task for searching for an object in the database using it's unique ID
     *
     * Takes in 2 string parameters:
     * - The first one is the type id of the object to search for
     * - The second one is the unique id of the object to search for
     *
     * @return {@nullable result, success} as a pair
     */

    private static class IDSearchTask extends AsyncTask<String, Void, Pair<SearchResult, Boolean>> {
        @Override
        protected Pair<SearchResult, Boolean> doInBackground(String... search_parameters) {
            verifySettings();

            String typeId = search_parameters[0];
            String objectId = search_parameters[1];
            String query = "{\"query\":{\"match\":{\"_id\":\"" + objectId + "\"}}}}";

            Search search = new Search.Builder(query).addIndex(INDEX).addType(typeId).build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    return new Pair<>(result, Boolean.TRUE);
                } else {
                    Log.i("Error", "the search query failed to find any objects that matched");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
                return new Pair<>(null, Boolean.FALSE);
            }

            return new Pair<>(null, Boolean.TRUE);
        }
    }

    /**
     * Asynchronous Task for searching for multiple objects with multiple unique IDs
     *
     * Takes in 2 string parameters:
     * - The first one is the type id of the object to search for
     * - The second one is the final query to execute
     *
     * @return {@nullable result, success} as a pair
     */
    private static class SearchMultipleIDsTask extends AsyncTask<String, Void, Pair<SearchResult, Boolean>> {
        @Override
        protected Pair<SearchResult, Boolean> doInBackground(String... search_parameters) {
            verifySettings();

            String typeId = search_parameters[0];
            String query = search_parameters[1];
            Search search = new Search.Builder(query).addIndex(INDEX).addType(typeId).build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    return new Pair<>(result, Boolean.TRUE);
                } else {
                    Log.i("Error", "the search query failed to find any objects that matched");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
                return new Pair<>(null, Boolean.FALSE);
            }

            return new Pair<>(null, Boolean.TRUE);
        }
    }

    /**
     * Asynchronous Task for deleting an object from the database with it's unique ID
     *
     * Takes in 2 string parameters:
     * - The first one is the type id of the object to search for
     * - The second one is the unique id of the object to search for
     *
     * @return whether the object was deleted or not
     */
    private static class DeleteTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... search_parameters) {
            verifySettings();

            String typeId = search_parameters[0];
            String objectId = search_parameters[1];
            String query = search_parameters[2];

            Delete delete = new Delete.Builder(query).index(INDEX).type(typeId).id(objectId).build();

            try {
                return client.execute(delete).isSucceeded();
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return Boolean.FALSE;
        }
    }

    /**
     * Execute a search with ElasticSearch
     * @param typeId the type template id all results must match
     * @param query the query to execute
     * @return {@nullable result, success} as a pair
     */
    public static Pair<SearchResult, Boolean> search(String typeId, String query){
        try {
            return new SearchTask().execute(typeId, query).get(MAX_QUERY_TIME, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return new Pair<>(null, Boolean.FALSE);
    }

    /**
     * Execute a search for a specific object with ElasticSearch
     * @param typeId the type template id all results must match
     * @param query the query to execute
     * @param id the id of the object to search for
     * @return {@nullable result, success} as a pair
     */
    public static Pair<SearchResult, Boolean> search(String typeId, String query, String id){
        try {
            return new IDSearchTask().execute(typeId, id, query).get(MAX_QUERY_TIME, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return new Pair<>(null, Boolean.FALSE);
    }

    /**
     * Execute a search with ElasticSearch for all objects that have one of the IDs specified in the query
     * @param typeId the type template id all results must match
     * @param query the query to execute
     * @return {@nullable result, success} as a pair
     */
    public static Pair<SearchResult, Boolean> searchByIds(String typeId, String query){
        try {
            return new SearchMultipleIDsTask().execute(typeId, query).get(MAX_QUERY_TIME, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return new Pair<>(null, Boolean.FALSE);
    }

    /**
     * Search for all records with the specified type parameter values
     * @param typeId the type template id that all results must match
     * @param tempClass the java class of the generic type T
     * @param ids a list of IDs that an object could possibly have if it is returned
     * @param <T> generic representing the java type corresponding to that type ID
     * @return the list of all records matching that type ID with the required parameter values
     */
    public static <T extends ElasticSearchable> List<T> getListOf(String typeId, Class<T> tempClass, List<String> ids){
        Pair<SearchResult, Boolean> result = searchByIds(typeId, getCompleteIdsListQuery(ids, typeId));
        if (result.first != null && result.first.isSucceeded()) {

            List<T> found = result.first.getSourceAsObjectList(tempClass);

            // transform result into json
            JsonObject jo = result.first.getJsonObject();

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
     * Search for all records with the specified type parameter values
     * @param typeId the type template id that all results must match
     * @param tempClass the java class of the generic type T
     * @param values map where key=parameter and value=required record value for that parameter
     * @param <T> generic representing the java type corresponding to that type ID
     * @return the list of all records matching that type ID with the required parameter values
     */
    public static <T extends ElasticSearchable> List<T> getListOf(String typeId, Class<T> tempClass, Map<String, String> values){
        Pair<SearchResult, Boolean> result = search(typeId, getQueryFromMap(values));
        if (result.first != null && result.first.isSucceeded()) {

            List<T> found = result.first.getSourceAsObjectList(tempClass);

            // transform result into json
            JsonObject jo = result.first.getJsonObject();

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
     * @return {@nullable object, success} as a pair
     */
    public static <T extends ElasticSearchable> Pair<T, Boolean> getObject(String typeId, Class<T> tempClass, String id){
        Pair<SearchResult, Boolean> result = search(typeId, "", id);
        if (result.first != null && result.first.isSucceeded()) {
            T object = result.first.getSourceAsObject(tempClass);
            object.setId(id);
            return new Pair<>(object, Boolean.TRUE);
        }
        return new Pair<>(null, Boolean.FALSE);
    }

    /**
     * Search for the record with the specified parameter values
     * @param typeId the type template id of the result
     * @param tempClass the java class of the generic type T
     * @param values map where key=parameter and value=required record value for that parameter
     * @param <T> generic representing the java type corresponding to that type ID
     * @return {@nullable result, success} as a pair
     */
    public static <T extends ElasticSearchable> Pair<T, Boolean> getObject(String typeId, Class<T> tempClass, Map<String, String> values){
        Pair<SearchResult, Boolean> result = search(typeId, getQueryFromMap(values));
        if (result.first != null && result.first.isSucceeded()) {

            JsonObject jo = result.first.getJsonObject();

            // array of all of the records that match the search parameters
            JsonArray array = jo.get("hits").getAsJsonObject().get("hits").getAsJsonArray();
            if (array.size() == 0)
                return new Pair<>(null, Boolean.TRUE);

            String foundId = array.get(array.size() - 1).getAsJsonObject().get("_id").getAsString();

            // use the last object as the source
            T obj = result.first.getSourceAsObjectList(tempClass).get(array.size() - 1);//result.getSourceAsObject(tempClass);
            obj.setId(foundId);
            return new Pair<>(obj, Boolean.TRUE);
        }
        return new Pair<>(null, result.first != null && result.first.isSucceeded());
    }

    /**
     * Save an object into the database
     * @param object the object to save
     * @param <T> generic representing the java type corresponding to that object's type
     * @return whether the save was successful
     */
    public static <T extends ElasticSearchable> boolean save(T object){
        try {
            return new InsertTask().execute(object).get(MAX_QUERY_TIME, TimeUnit.MILLISECONDS).booleanValue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete an object from the database
     * @param typeId the type template id of the result
     * @param objId the id of the object to search for
     * @return whether the deletion was successful
     */
    public static boolean delete(String typeId, String objId) {
        try {
            return new DeleteTask().execute(typeId, objId, "").get(MAX_QUERY_TIME, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete an object from the database
     * @param object the object to delete
     * @return whether the deletion was successful
     */
    public static boolean delete(ElasticSearchable object) {
        try {
            return new DeleteTask().execute(object.getTypeId(), object.getId(), "").get(MAX_QUERY_TIME, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete all records with the specified parameter values
     * @param typeId the type template id of the result
     * @param values map where key=parameter and value=required record value for that parameter
     * @return true if atleast one object was found, and all found objects were deleted successfully
     */
    public static <T extends ElasticSearchable> boolean delete(String typeId, Class<T> deleteClass, Map<String, String> values){

        List<T> found = getListOf(typeId, deleteClass, values);
        boolean success = found.size() > 0;

        // delete all items individually
        for (T item : found){
            success = success && delete(item);
        }

        return success;
        //new DeleteSearchTask().execute(typeId, getQueryFromMap(values));
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

    /**
     * Get a complete ElasticSearch query for searching for multiple items with multiple IDs
     * @param ids a list of IDs that an object could possibly have if it is returned
     * @param type the type to search for
     * @return a string representing a query for any object with one of the specified IDs
     */
    private static String getCompleteIdsListQuery(List<String> ids, String type){
        StringBuilder builder = new StringBuilder();

        // TODO: test when there is more than 10 results
        builder.append("{\"query\": {\"ids\": {\"values\": [");
        for (String id : ids){
            builder.append("\"" + id + "\"");
            // add a comma if this is not the last ID
            if (!id.equals(ids.get(ids.size() - 1))){
                builder.append(", ");
            }
        }

        builder.append("]}}\n}");
        return builder.toString();
    }

}
