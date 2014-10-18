package com.example.android.plitto;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by batman on 16/10/14.
 */
public class FriendFragment extends Fragment {

    private static List<FriendModel> mDataSourceList = new ArrayList<FriendModel>();;
    private List<FragmentTransaction> mBackStackList = new ArrayList<FragmentTransaction>();
    ListView friend_list;
    FragmentActivity context;

    public FriendFragment()
    {

    }

    public FriendFragment(List<FriendModel> data)
    {

        mDataSourceList.addAll(data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_friends, container, false);
        friend_list = (ListView) v.findViewById(R.id.list);
        FriendAdapter fa = new FriendAdapter(mDataSourceList,context);
        friend_list.setAdapter(fa);

        //friend_list.setAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, mDataSourceList));

/*
        friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context,"Item "+i+" clicked",Toast.LENGTH_SHORT).show();
            }
        });
*/

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = (FragmentActivity)activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        // Remove adapter refference from list
        friend_list.setAdapter(null);
        super.onDestroy();

    }

    /*
    public static void set_friend_list(List<String> data)
    {
        mDataSourceList.addAll(data);
        listAdapter.notifyDataSetChanged();
    }

    public static FriendFragment newInstance(List<String> data) {
        FriendFragment fragmentDemo = new FriendFragment();
        fragmentDemo.set_friend_list(data);
        return fragmentDemo;
    }*/
}