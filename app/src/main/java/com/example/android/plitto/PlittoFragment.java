package com.example.android.plitto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
 /* BG Fragment appears in the "content_frame", showing a specific Fragment     */
public class PlittoFragment extends Fragment {

    public static final String ARG_NAV_NUMBER = "nav_number";
    public static final String TAG = PlittoFragment.class.getSimpleName();
    ListView listview;
    List<RowInfo> content;
    private FragmentActivity myContext;

    public PlittoFragment() {
    }

    public static PlittoFragment newInstance(int position) {
        PlittoFragment fragment = new PlittoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NAV_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }


    // TODO Change these to handle the correct Plitto Fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);
        // BG Removed - text = (TextView) rootView.findViewById(R.id.itemListTitle);
        listview = (ListView) rootView.findViewById(R.id.userlist);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String myId = (String)view.getTag();
                android.support.v4.app.FragmentManager fm = myContext.getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                if(Integer.parseInt(myId) == 0)
                {
                    String name = content.get(i).getName();
                    UserFragment fragment = UserFragment.newInstance(name);
                    transaction.replace(R.id.content_frame, fragment);

                }

                else if(Integer.parseInt(myId) == 1)
                {
                    String name = content.get(i).getName();
                    ListFragment fragment = ListFragment.newInstance(name);
                    transaction.replace(R.id.content_frame, fragment);
                }
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        content = new ArrayList<RowInfo>();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

// This sets the view to be the fragment_item_list
        int i = getArguments().getInt(ARG_NAV_NUMBER);
        // BG This takes the nav item name from the string.
        String navItem = getResources().getStringArray(R.array.nav_array)[i];

        String getSomeUrl = " ";
        // http://plitto.com/api/getSometest
        if (new String("Ditto").equals(navItem)) {
            Log.d(TAG, "You chose 'Ditto'");
            getSomeUrl = "http://www.plitto.com/api/2.0/getsometestrich";
        } else if (new String("Friends").equals(navItem)) {
            Log.d(TAG, "You chose 'Friends'");
            getSomeUrl = "http://www.plitto.com/api/2.0/getsometestrich";
        } else if (new String("Search").equals(navItem)) {
            Log.d(TAG, "You chose 'Search'");
            getSomeUrl = "http://www.plitto.com/api/2.0/getsometestrich";
        } else {
            Log.d(TAG, "You chose something other than 'Ditto'" + navItem);
            getSomeUrl = "http://www.plitto.com/api/2.0/getsometestrich";
        }
        Log.d(TAG, "URL To Call " + getSomeUrl);
        // TODO Make the API call
        // new HttpAsyncTask().execute(getSomeUrl);
        new HttpAsyncTask().execute(getSomeUrl);
        // TODO - Update the list contents based on the condition
    }


    public class HttpAsyncTask extends AsyncTask<String, String, JSONObject> {


        private Exception exception;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute(){
            pDialog = new ProgressDialog(myContext);
            pDialog.setMessage("Loading plitto data...");
            pDialog.show();
        }


        @Override
        protected JSONObject doInBackground(String... urls) {
            JSONObject jsonObj = null;
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(urls[0], ServiceHandler.GET);
            if (jsonStr != null) {
                try {
                    jsonObj = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return jsonObj;
        }


        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(JSONObject result) {
            //Toast.makeText(getActivity(), "Data Sent! "+result, Toast.LENGTH_LONG).show();
            if(result!=null)

            try {
                JSONArray userArray = result.getJSONArray("results");
                if (userArray != null) {
                    for (int i = 0; i < userArray.length(); i++) {
                        if (userArray.get(i) instanceof JSONObject) {
                            JSONObject user = userArray.getJSONObject(i);
                            String username = user.getString("username");
                            content.add(new RowInfo(1, username,"","",user.getString("fbuid")));
                            JSONArray user_lists = (JSONArray) user.getJSONArray("lists");
                            for (int j = 0; j < user_lists.length(); j++) {
                                JSONObject user_desc = (JSONObject) user_lists.get(j);
                                content.add(new RowInfo(2, user_desc.getString("listname"),"","",user_desc.getString("lid")));
                                JSONArray final_list = (JSONArray) user_desc.getJSONArray("items");
                                for (int k = 0; k < final_list.length(); k++) {
                                    JSONObject final_elem = (JSONObject) final_list.get(k);
                                    content.add(new RowInfo(3, final_elem.getString("thingname"), final_elem.getString("added"), final_elem.getString("mykey"),final_elem.getString("tid")));
                                }
                            }
                        }

                        else if(userArray.get(i) instanceof JSONArray) {
                            JSONArray kk = userArray.getJSONArray(i);
                            for(int l=0;l<kk.length();l++) {
                                JSONObject user = kk.getJSONObject(l);
                                String username = user.getString("username");
                                content.add(new RowInfo(1, username,"","",user.getString("fbuid")));
                                JSONArray user_lists = (JSONArray) user.getJSONArray("lists");
                                for (int j = 0; j < user_lists.length(); j++) {
                                    JSONObject user_desc = (JSONObject) user_lists.get(j);
                                    content.add(new RowInfo(2, user_desc.getString("listname"),"","",user_desc.getString("lid")));
                                    JSONArray final_list = (JSONArray) user_desc.getJSONArray("items");
                                    for (int k = 0; k < final_list.length(); k++) {
                                        JSONObject final_elem = (JSONObject) final_list.get(k);
                                        content.add(new RowInfo(3, final_elem.getString("thingname"), final_elem.getString("added"), final_elem.getString("mykey"),final_elem.getString("tid")));
                                    }
                                }

                            }


                        }
                    }

                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
              SimpleAdapter s = new SimpleAdapter(content, myContext.getApplicationContext());
                    listview.setAdapter(s);
            pDialog.dismiss();
        }
    }

    private void showFragment(int fragmentIndex)
    {
        
    }

}