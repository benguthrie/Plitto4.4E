package com.example.android.plitto;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import java.util.Arrays;
import java.util.List;

/**
 * Created by batman on 14/10/14.
 */
public class MainFragment extends Fragment {
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    private boolean isResumed = false;
    private boolean userSkippedLogin = false;
    private FragmentActivity myContext;

    private static final String USER_SKIPPED_LOGIN_KEY = "user_skipped_login";
    private static final String TAG = "Facebook";
    private UiLifecycleHelper uiHelper;
    private final List<String> permissions;




    public MainFragment() {
        permissions = Arrays.asList("public_profile", "user_friends", "user_about_me",
                "user_relationships", "user_birthday", "user_location");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.splash, container,false);
        LoginButton authButton = (LoginButton) v.findViewById(R.id.login_button);
        authButton.setFragment(this);
        authButton.setReadPermissions(permissions);
        return v;
    }










}