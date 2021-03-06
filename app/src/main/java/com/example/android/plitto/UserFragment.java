package com.example.android.plitto;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by batman on 12/10/14.
 */
public class UserFragment extends Fragment {

    static final int NUM_ITEMS = 4;
    private ViewPager mPager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_main,
                container, false);
        TextView tv = (TextView)view.findViewById(R.id.content);
        tv.setText(getArguments().getString("username", ""));
        return view;
    }

    public static UserFragment newInstance(String username) {
        UserFragment fragmentDemo = new UserFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }
}
