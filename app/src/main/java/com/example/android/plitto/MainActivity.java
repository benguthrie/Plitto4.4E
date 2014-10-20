package com.example.android.plitto;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.Signature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;

import com.facebook.LoginActivity;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Request;
import com.facebook.SharedPreferencesTokenCachingStrategy;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphMultiResult;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;

import java.io.IOException;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends FragmentActivity {


    private static final String USER_SKIPPED_LOGIN_KEY = "user_skipped_login";

    private static final int SPLASH = 0;
    private static final int SELECTION = 1;
    private static final int FRAGMENT_COUNT = SELECTION +1;

    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
    private MenuItem settings;
    private boolean isResumed = false;
    private boolean userSkippedLogin = false;
    private UiLifecycleHelper uiHelper;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mNavTitles;
    private List<String> friend_list;

    private MainFragment mainFragment;

    Fragment fbFragment;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = mDrawerTitle = getTitle();
        mNavTitles = getResources().getStringArray(R.array.nav_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mNavTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);



        if(isConnectedToInternet()) {
            Log.e("Ronak","In IFF");
            friend_list = new ArrayList<String>();
            try {
                PackageInfo info = getPackageManager().getPackageInfo("com.example.android.plitto", PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                    Log.e("HASH", sign);
                }
            } catch (NoSuchAlgorithmException e) {
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (savedInstanceState != null) {
                userSkippedLogin = savedInstanceState.getBoolean(USER_SKIPPED_LOGIN_KEY);
            }
            uiHelper = new UiLifecycleHelper(this, callback);
            uiHelper.onCreate(savedInstanceState);

            FragmentManager fm = getSupportFragmentManager();
            MainFragment mf = new MainFragment();
            fragments[SPLASH] = mf;
            fragments[SELECTION] = PlittoFragment.newInstance(0);
        }

        else
        {
            Log.e("Ronak","In else");
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.content_frame, new NoInternetFragment()).commit();
            getActionBar().hide();
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }



    private void showFragment(int fragmentIndex, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if(fragmentIndex == SPLASH)
        {
            transaction.replace(R.id.content_frame,fragments[SPLASH]);
            getActionBar().hide();
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        else if(fragmentIndex == SELECTION)
        {
            transaction.replace(R.id.content_frame,fragments[SELECTION]);
            getActionBar().show();
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        transaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }



    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        if(mDrawerLayout !=null) {
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
            menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.


        if (mDrawerToggle!=null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_websearch:
                if(isConnectedToInternet()) {
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            case R.id.action_refresh:
                if(isConnectedToInternet()) {
                    Fragment fragment2 = PlittoFragment.newInstance(0);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment2).commit();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        Log.d("Ronak", "position === " + position);

        if(mDrawerList!=null && mDrawerLayout!=null) {


            if (position < 3) {

                if (position == 2) {
                    //calling friend code here

                    requestMyAppFacebookFriends(Session.getActiveSession());
                    mDrawerList.setItemChecked(position, true);
                    setTitle(mNavTitles[position]);
                    mDrawerLayout.closeDrawer(mDrawerList);

                } else {

                    Fragment fragment2 = PlittoFragment.newInstance(0);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment2).commit();
                    mDrawerList.setItemChecked(position, true);
                    setTitle(mNavTitles[position]);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
            } else {
                if (position == 6) {
                    Toast.makeText(this, "Logout button clicked", Toast.LENGTH_LONG).show();
                    Session s = Session.getActiveSession();
                    if (s != null) {
                        Log.e("Finally", "Clearing session data");
                        s.closeAndClearTokenInformation();
                    }
                } else {
                    Toast.makeText(this, "ELSE button clicked", Toast.LENGTH_LONG).show();
                    Fragment fragment = new PlanetFragment();
                    Bundle args = new Bundle();
                    args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
                    fragment.setArguments(args);

                    FragmentManager fragmentManager2 = getSupportFragmentManager();
                    fragmentManager2.beginTransaction().replace(R.id.content_frame, fragment).commit();

                    // update selected item and title, then close the drawer
                    mDrawerList.setItemChecked(position, true);
                    setTitle(mNavTitles[position]);
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if(mDrawerToggle!=null)
            mDrawerToggle.syncState();
    }



    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        if(mDrawerToggle !=null)
            mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.planets_array)[i];

            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                    "drawable", getActivity().getPackageName());

            // TODO Select the active fragment instead of setting the image.

            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);

            // getActivity().setTitle(planet);
            return rootView;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        if(isConnectedToInternet()) {
            Session session = Session.getActiveSession();

            if (session != null && session.isOpened()) {
                showFragment(SELECTION, false);
            } else {
                // otherwise present the splash screen and ask the user to login, unless the user explicitly skipped.

                showFragment(SPLASH, false);
            }
        }
        else
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new NoInternetFragment()).commit();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(uiHelper!=null)
            uiHelper.onResume();
        isResumed = true;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
        outState.putBoolean(USER_SKIPPED_LOGIN_KEY, userSkippedLogin);
    }


        private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (isResumed) {
            FragmentManager manager = getSupportFragmentManager();
            int backStackSize = manager.getBackStackEntryCount();
            for (int i = 0; i < backStackSize; i++) {
                manager.popBackStack();
            }

            if (state.equals(SessionState.OPENED)) {
                Log.e("Finally","In normal state");
                showFragment(SELECTION, false);
            } else if (state.isClosed()) {
                Log.e("Finally","In logout state");
                showFragment(SPLASH, false);
            }
        }
    }

    private Request createRequest(Session session) {
        Request request = Request.newGraphPathRequest(session, "me/friends", null);

        Set<String> fields = new HashSet<String>();
        String[] requiredFields = new String[] { "id", "name", "picture","installed","first_name","last_name" };
        fields.addAll(Arrays.asList(requiredFields));

        Bundle parameters;
        parameters = request.getParameters();
        parameters.putString("fields", TextUtils.join(",", fields));
        request.setParameters(parameters);
        return request;
    }

    private void requestMyAppFacebookFriends(Session session) {
        final List<String> friend_id=new ArrayList<String>();
        Request friendsRequest = createRequest(session);
        friendsRequest.setCallback(new Request.Callback() {

            @Override
            public void onCompleted(Response response) {
                if (response != null) {
                    List<GraphUser> friends = getResults(response);
                    if(friends==null)
                        return;

                    Log.e("Finally", friends.toString());
                    Log.e("Friend List", "Total friend = " + friends.size());
                    GraphUser friend;
                    String first_name = "", last_name = "";
                    friend_list.clear();
                    for (int i = 0; i < friends.size(); i++) {
                        Log.e("Friend List", "Added");
                        friend = friends.get(i);
                        friend_id.add(friend.getId());
                    }

                    new Friend_Data().execute(friend_id);

                }
            }
        });
        friendsRequest.executeAsync();

    }

    private List<GraphUser> getResults(Response response) {
        GraphMultiResult multiResult = response
                .getGraphObjectAs(GraphMultiResult.class);
        if(multiResult!=null) {
            GraphObjectList<GraphObject> data = multiResult.getData();
            return data.castToListOf(GraphUser.class);
        }
        else
            return null;
    }


    private String POST(List<String> id){
        InputStream inputStream = null;
        String url="http://www.plitto.com/api/fbfriendstest";

        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            JSONArray mJSONArray = new JSONArray(Arrays.asList(id));
            String json = "";

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Id",mJSONArray);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
                Log.e("RESULT ==", result);
            }
            else
                result = "Did not work!";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    class Friend_Data extends AsyncTask<List<String>, Void, String> {

        private Exception exception;
        ProgressDialog pDialog;

            @Override
            protected void onPreExecute(){
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Getting interesting data...");
                pDialog.show();


            }


            protected String doInBackground(List<String>... urls) {
            List<String> id = urls[0];
            InputStream inputStream = null;
            String url="http://www.plitto.com/api/2.0/fbfriendstest";

            String result = "";
            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                JSONArray mJSONArray = new JSONArray(Arrays.asList(id));
                String json = "";

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Id",mJSONArray);
                json = jsonObject.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                if(inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    Log.e("RESULT ==", result);
                }
                else
                    result = "Did not work!";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }


        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            if(result!=null) {
                JSONObject obj = null;
                List<FriendModel> friend_list = new ArrayList<FriendModel>();
                try {
                    obj = new JSONObject(result);
                    Log.d("MyApp", obj.toString());
                } catch (Throwable t) {
                    Log.e("MyApp", "Could not parse malformed JSON: \"" + result + "\"");
                }
                if (obj != null) {
                    try {
                        FriendModel fm;
                        JSONObject temp;
                        JSONArray results = obj.getJSONArray("result");
                        for (int i = 0; i < results.length(); i++) {
                            temp = results.getJSONObject(i);
                            fm = new FriendModel();
                            fm.setId(temp.getString("id"));
                            fm.setName(temp.getString("name"));
                            fm.setFbuid(temp.getString("fbuid"));
                            fm.setThings(temp.getString("things"));
                            fm.setShared(temp.getString("shared"));
                            fm.setDittoable(temp.getString("dittoable"));
                            fm.setLists(temp.getString("lists"));
                            fm.setSharedlists(temp.getString("sharedlists"));
                            friend_list.add(fm);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                pDialog.dismiss();


                FriendFragment ff = new FriendFragment(friend_list);
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.content_frame, ff).commit();
            }

        }
    }


    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

}
