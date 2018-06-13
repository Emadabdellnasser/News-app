package com.example.emad.newsfeedapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EMAD on 1/28/2018.
 */

public class QueryUtils {

    static HttpURLConnection urlConnection = null;
    private static final String logcat = QueryUtils.class.getSimpleName();

    public QueryUtils() {
    }

    public static List<News_object> fetch(String request) {
        URL mrl = converter(request);
        InputStream inputs = null;
        String jsonResponse = null;
        try {
            inputs = makeHttpRequest(mrl);
            jsonResponse = getjsonFromStream(inputs);
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputs != null) {
                inputs.close();
            }

            if (inputs != null) {
                inputs.close();
            }

        } catch (IOException e) {
            Log.e(logcat, "Problem making the HTTP request.", e);
        }

        List<News_object> news = getingdetailesfromjson(jsonResponse);
        return news;

    }

    //converting string to url
    public static URL converter(String ul) {
        URL r = null;
        try {
            r = new URL(ul);
        } catch (MalformedURLException e) {
            Log.e(logcat, "ther is a problem in converting url", e);
        }
        return r;
    }

    //network checking method and return inputstream
    public static InputStream makeHttpRequest(URL ul) throws IOException {
        InputStream inputStream = null;
        // URL checking
        if (ul == null) {
            return inputStream;
        }
        try {
            urlConnection = (HttpURLConnection) ul.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // check the connection
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
            } else {
                Log.e(logcat, "Error in connection: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(logcat, "ther is aproblem in getting json.", e);
        }
        return inputStream;
    }

    private static String getjsonFromStream(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                result.append(line);
                line = reader.readLine();
            }
        }
        //converting from stringbuilder to string
        return result.toString();
    }

    //extract m data from json
    public static List<News_object> getingdetailesfromjson(String json_response) {
        // Creaing list to add news in it
        List<News_object> newslist = new ArrayList<>();
        //Json Parsing
        try {
            String title;
            String name;
            String author;
            String date;
            String news_website;
            JSONObject baseJsonResponse = new JSONObject(json_response);
            JSONObject responsobject = baseJsonResponse.getJSONObject("response");
            JSONArray newsarray = responsobject.getJSONArray("results");
            int j = 0;
            while (j < newsarray.length()) {
                JSONObject curnews = newsarray.getJSONObject(j);
                // geting the value for the title
                title = curnews.getString("webTitle");
                if (curnews.has("webPublicationDate")) {
                    date = curnews.getString("webPublicationDate");
                } else {
                    date = "no date";
                }
                name = curnews.getString("sectionName");
                news_website = curnews.getString("webUrl");
                List<String> authorsList = new ArrayList<>();
                JSONArray tagsArray = curnews.getJSONArray("tags");
                for (int i = 0; i < tagsArray.length(); i++) {
                    JSONObject tagsObject = tagsArray.getJSONObject(i);
                    String firstName = tagsObject.getString("firstName");
                    String lastName = tagsObject.getString("lastName");
                    String authorName;
                    if (TextUtils.isEmpty(firstName)) {
                        authorName = lastName;
                    } else {
                        authorName = firstName + " " + lastName;
                    }
                    authorsList.add(authorName);
                }

                if (authorsList.size() == 0) {
                    author = "no authors";
                } else {
                    author = TextUtils.join(", ", authorsList);
                }

                News_object news = new News_object(title, name, date, author, news_website);
                newslist.add(news);
                j++;
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "ther is a problem in parsing ", e);
        }
        return newslist;
    }
}



