package com.example.android.navigationdrawerexample;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
 /* BG Fragment appears in the "content_frame", showing a specific Fragment     */
public class PlittoFragment extends Fragment {
    public static final String ARG_NAV_NUMBER = "nav_number";
    public static final String TAG = PlittoFragment.class.getSimpleName();
    public PlittoFragment() {
        // Empty constructor required for fragment subclasses
    }

    public static PlittoFragment newInstance(int position) {
        PlittoFragment fragment = new PlittoFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_NAV_NUMBER, position);
        fragment.setArguments(args);

        return fragment;
    }

    // TODO Change these to handle the correct Plitto Fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

// This sets the view to be the fragment_item_list
        int i = getArguments().getInt(ARG_NAV_NUMBER);
        // BG This takes the nav item name from the string.
        String navItem = getResources().getStringArray(R.array.nav_array)[i];
        Log.d(TAG, "NavItem: " + i + " " + navItem);

        String getSomeUrl = " ";

        // TODO - Build up the proper URL to call.
        // http://plitto.com/api/getSometest
        if( new String("Ditto").equals(navItem) ){
            Log.d(TAG,"You chose 'Ditto'");
            getSomeUrl = "http://plitto.com/api/getSometest";
        } else if( new String("Friends").equals(navItem) ){
            Log.d(TAG,"You chose 'Friends'");
            getSomeUrl = "http://plitto.com/api/friends";
        } else if( new String("Search").equals(navItem) ){
            Log.d(TAG,"You chose 'Search'");
            getSomeUrl = "http://plitto.com/api/search";
        } else {
            Log.d(TAG,"You chose something other than 'Ditto'" + navItem);
            getSomeUrl = "http://plitto.com/api/getSometest";
        }
        Log.d(TAG,"URL To Call" + getSomeUrl);
        // TODO Make the API call
        // new HttpAsyncTask().execute(getSomeUrl);
        new HttpAsyncTask().execute(getSomeUrl);
        // TODO - Update the list contents based on the condition


        // TODO Select the active fragment instead of setting the image.
        // Log.d(TAG,"gridTitle: " + rootView.findViewById(R.id.gridTitle));
        // BG Update the text in the item list.
        // android:id="@+id/itemListTitle"
        // itemListTitle.setTitle(navItem);


/*
            // This creates the file name for the planet and makes it an image.
            int imageId = getResources().getIdentifier(navItem.toLowerCase(Locale.getDefault()),
                    "drawable", getActivity().getPackageName());

            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);

            // getActivity().setTitle(planet);
            */
        }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            OkHttpClient client = new OkHttpClient();

            String url = urls[0];
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                return response.body().string();
            } catch (IOException e) {
                return null;
            }

        }

        private ListView userListView;
        private ArrayAdapter<String> listAdapter;

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.v(TAG, "RESULT: " + result);
//             Toast.makeText(getActivity(), "Data Sent!", Toast.LENGTH_LONG).show();
            // TODO Take the data results and put them in the list where they can be shown.

            // 1. Concert it into an object?
            JSONObject obj = null;
            try {
                obj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JSONArray getSomeResults = null;
            if (obj != null) try {
                getSomeResults = obj.getJSONArray("results");
                // BG Create the list
                List<String> list = new ArrayList<String>();


                // BG At this point, we have the items from the results in an object / array, and we need to build up the list.
                for (int i = 0; i < getSomeResults.length(); i++) {
                    list.add(getSomeResults.getJSONObject(i).getString("username"));
                }
                // BG We can prove that we can get the user information from the call.
                Log.v(TAG, "First User: " + getSomeResults.getJSONObject(0).getString("username"));
                Log.v(TAG, "what is the list?: " + list);
                Log.v(TAG, "raw results: " + getSomeResults); // This is the exact JSON object.
                Toast.makeText(getActivity(), getSomeResults.getJSONObject(0).getString("username"), Toast.LENGTH_LONG).show();
                // TODO Update the list view with these results
                // Following http://developer.android.com/guide/topics/ui/layout/listview.html

                // Following http://windrealm.org/tutorials/android/android-listview.php
                // TODO FAILS HERE - "cannot find symbol method findViewById(int)"

                // 
                Log.v(TAG, "Find by ID" + getActivity().findViewById(R.id.userlist));


            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v(TAG, "Record: " + obj.length());


        }
    }

}