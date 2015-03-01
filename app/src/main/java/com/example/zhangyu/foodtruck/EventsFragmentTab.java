package com.example.zhangyu.foodtruck;

import com.example.zhangyu.foodtruck.db_Layout.Dao_Sqlite;
import com.example.zhangyu.foodtruck.entities.Event;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AlphabetIndexer;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventsFragmentTab extends Fragment {
    private ListView events_ListView;
    private EventAdapter adapter;

    private AlphabetIndexer indexer;

    private static final String TAG = "EventsFragmentTab";

    private static final String SEARCH_URL = "https://graph.facebook.com/OffTheGridSF/events?access_token=";

    private static final String TOKEN = "798204470272069|b9a9c74ca530bada6cc0e664ac62ecf9";

    // Store the list of the events.
    private List<Event> events = new ArrayList<Event>();

    private Dao_Sqlite dbHelper;

    private void ParseResultAndStoreInDB(String json) throws JSONException {
        Log.d(TAG, "Json data" + ":" + json.toString());

        JSONObject jsonObject = new JSONObject(json);

        // The data is in the "data" section.
        JSONArray jsonArray_results = jsonObject
                .getJSONArray("data");

        for (int i = 0; i < jsonArray_results.length(); i++) {
            JSONObject jsonObject_i = jsonArray_results.getJSONObject(i);

            // Store the data to the database and add to the list.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date date = null;
            try {
                date = formatter.parse(jsonObject_i.getString("start_time"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // convert the date to unix epoch
            long timeInMillisSinceEpoch = date.getTime();
            long timeUnix = timeInMillisSinceEpoch / (1000);

            Event event = new Event(jsonObject_i.getString("name")
                            ,timeUnix
                            ,jsonObject_i.getString("location")
                            ,jsonObject_i.getString("id")
            );

            events.add(event);

            // Add the data into the local database.
            dbHelper.addEvent(event);
        }
    }

    private String sendQuery(String query) throws IOException {
        String result = "";

        URL searchURL = new URL(query);

        HttpURLConnection httpURLConnection = (HttpURLConnection) searchURL.openConnection();

        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStreamReader inputStreamReader = new InputStreamReader(
                    httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader, 8192);

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            bufferedReader.close();
        }

        Log.d(TAG, "http response:" + httpURLConnection.getResponseCode() + "");
        return result;
    }

    private class JsonSearchTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0) {
            // get the input.
            String search_query = SEARCH_URL + TOKEN;

            String searchResult = null;

            try {
                searchResult = sendQuery(search_query);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "CLOUD RESULT:" + searchResult);
            return searchResult;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    // parse the data got, and store them in the database.
                    ParseResultAndStoreInDB(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Set the view and display the view.
            if (events.size() > 0) {
                setupEventsListView();
            }

            super.onPostExecute(result);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events_fragment_tab, container, false);

        events_ListView = (ListView) rootView.findViewById(R.id.events_ListView);

        dbHelper = new Dao_Sqlite(getActivity());

        // load data to the view.
        loadRecentEvents();

        return rootView;
    }

    private void loadRecentEvents() {
        adapter = new EventAdapter(getActivity(), R.layout.event_item, events);

        // load data from facebook and store it in the local database.
        readDataFromCloud();
    }

    public static String getSortKey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        }

        return "#";
    }

    private void readDataFromCloudAndStoreToLocalDB() {

    }

    // Get the data from the Facebook API and then save the data to the local database.
    private void readDataFromCloud() {
        // load data from Facebook.
        new JsonSearchTask().execute();
    }

    private void setupEventsListView() {
        events_ListView.setAdapter(adapter);
        events_ListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount,
                                 int totalItemCount) {

            }

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
                // TODO Auto-generated method stub

            }
        });

        // click the contact item to show the profile view. Added by yu zhang
        events_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                Event event = events.get(position);

                // show the profile of the contact.
                Intent intent = new Intent(getActivity(), VenderActivity.class);

                intent.putExtra("name", event.getName());
                startActivity(intent);
            }
        });
    }
}
