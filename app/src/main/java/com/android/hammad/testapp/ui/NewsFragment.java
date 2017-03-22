package com.android.hammad.testapp.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.hammad.testapp.R;
import com.android.hammad.testapp.ui.utils.FetchNewsTask;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment NewsFragment.
     */
    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        // Get the RecyclerView
        RecyclerView recylerViewNews = (RecyclerView) view.findViewById(R.id.recycler_view_news);
        // Create a LinearLayoutManager to associate with the RecyclerView
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        // Associate the LayoutManager with our RecyclerView
        recylerViewNews.setLayoutManager(layoutManager);

        // Start the FetchNewsTask to fetch news data and set
        // an adapter to the RecyclerView to display the data
        new FetchNewsTask(getContext(), recylerViewNews).execute();

        return view;
    }

}
