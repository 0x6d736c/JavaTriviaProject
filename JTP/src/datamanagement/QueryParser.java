package datamanagement;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Static class/method which parses a query String and returns a JsonArray object,
 * every entry in the JsonArray is going to be question/answer pair with category and difficulty assigned.
 */
public class QueryParser {
    /**
     * Takes a single query String and returns an array of all the questions retrieved from the API.
     * @param query - the query String to send to the webserver
     * @return JsonArray - a JsonArray containing the question data
     */
    public static JsonArray parse(String query) {
        //BEGIN STRING TO URL CONVERSION
        URL url = null;
        try {
            url = new URL(query);                        //Converts query string to valid URL format
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //BEGIN CONNECTION REQUEST
        URLConnection request = null;
        try {
            if (url != null) {
                request = url.openConnection();         //Lobs HTTP GET request at OpenTBD API
                request.connect();
            }                                           //Performs connection to API
        } catch (IOException e) {
            e.printStackTrace();
        }


        //BEGIN RESPONSE TO JSON CONVERSION
        JsonObject JSO = null;
        InputStreamReader reader = null;
        try {
            if (request != null) {
                reader = new InputStreamReader((InputStream) request.getContent());   //Read Stream from web server
                JSO = JsonParser.parseReader(reader).getAsJsonObject();                                 //Parse stream to Java JSON
            }//Close the stream
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // JSO will contain a "results" section, which can be gotten
        // If amount = X, you get a JsonArray of size Xx
        // get(i) getJsonObj get("item")
        // Appears to be an ArrayList underneath, so access with get() method
        if (JSO != null) {
            return JSO.get("results").getAsJsonArray();
        } else {
            return null;
        }
    }
}
