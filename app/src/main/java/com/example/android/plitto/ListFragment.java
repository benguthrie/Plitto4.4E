package com.example.android.plitto;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by batman on 13/10/14.
 */
public class ListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_main,
                container, false);
        TextView tv = (TextView) view.findViewById(R.id.content);
        tv.setText(getArguments().getString("username", ""));
        return view;
    }

    public static ListFragment newInstance(String username) {
        ListFragment fragmentDemo = new ListFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }
}
