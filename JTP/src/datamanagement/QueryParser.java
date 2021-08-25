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
            request = url.openConnection();             //Lobs HTTP GET request at OpenTBD API
            request.connect();                          //Performs connection to API
        } catch (IOException e) {
            e.printStackTrace();
        }


        //BEGIN RESPONSE TO JSON CONVERSION
        JsonObject JSO = null;
        try {
            InputStreamReader reader = new InputStreamReader((InputStream) request.getContent());   //Read Stream from web server
            JSO = JsonParser.parseReader(reader).getAsJsonObject();                                 //Parse stream to Java JSON
            reader.close();                                                                         //Close the stream
        } catch (IOException e) {
            e.printStackTrace();
        }

        //JSO will contain a "results" section, which can be gotten
        JsonArray results = JSO.get("results").getAsJsonArray();    //An array containing every question/answer dictionary/map
                                                                    //If amount = X, you get a JsonArray of size X
                                                                    //Appears to be an ArrayList underneath, so access with get() method
        return results;
    }


}
