package com.example.gt.booklist;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ListM extends AppCompatActivity {
    private String search;
    final String key = "keyes&key";
    ListView Books;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Books = (ListView) findViewById(R.id.list);
        search = getIntent().getExtras().getString("userInput");

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new BooksAsyns().execute(search);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar.make(Books, "No Internet Connetion", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
    }

    private class BooksAsyns extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            String JsonResponse = null;
            BufferedReader reader = null;
            try {
                String QueryUrl = "https://www.googleapis.com/books/v1/volumes?q=" + search;

                Uri uri = Uri.parse(QueryUrl).buildUpon()
                        .appendQueryParameter(key, getString(R.string.ApiKey)).build();

                URL url = new URL(uri.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    if (builder.length() == 0) {
                        return null;
                    }
                    line = reader.readLine();
                }
                JsonResponse = builder.toString();


            } catch (IOException e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }
            try {
                return JsonResponse;
            } catch (Exception e) {
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            List<Book> list;
            list = fetchJson(s);

            ListAdapter adapter = new ListAdapter(ListM.this, list);
            Books.setAdapter(adapter);
            if (list.size() == 0) {
                Snackbar snackbar = Snackbar
                        .make(Books, "No books found match the search", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }
        }

        private List<Book> fetchJson(String s) {
            List<Book> list = new ArrayList<>();
            try {
                JSONObject rootObject = new JSONObject(s);
                JSONArray itemsArray = rootObject.optJSONArray("items");
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject subObject = itemsArray.getJSONObject(i);
                    JSONObject volumeObject = subObject.optJSONObject("volumeInfo");
                    String title = volumeObject.optString("title");
                    JSONArray authorArray = volumeObject.optJSONArray("authors");
                    String author = "";
                    for (int j = 0; j < authorArray.length(); j++) {
                        author = authorArray.optString(0);
                    }
                    list.add(new Book(title, author));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }
    }
}