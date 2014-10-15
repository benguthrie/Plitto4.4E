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
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.Arrays;
import java.util.List;

/**
 * Created by batman on 14/10/14.
 */
public class MainFragment extends Fragment {
    FragmentActivity context;

    private final List<String> permissions;

    private SkipLoginCallback skipLoginCallback;

    public interface SkipLoginCallback {
        void onSkipLoginPressed();
    }


    public void setSkipLoginCallback(SkipLoginCallback callback) {
        skipLoginCallback = callback;
    }


    public MainFragment() {
        permissions = Arrays.asList("public_profile", "user_friends", "user_about_me",
                "user_relationships", "user_birthday", "user_location");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = (FragmentActivity)activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.splash, container,false);
        LoginButton authButton = (LoginButton) v.findViewById(R.id.login_button);
        authButton.setReadPermissions(permissions);
        authButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    //Toast.makeText(context,"Logged in",Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(context,"Logout successfully",Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }









}