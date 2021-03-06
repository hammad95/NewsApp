package com.android.hammad.testapp.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.hammad.testapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmptyFragment extends Fragment {

    /**
     * The fragment argument representing the tab number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";


    public EmptyFragment() {
        // Required empty public constructor

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static EmptyFragment newInstance(int sectionNumber) {
        EmptyFragment fragment = new EmptyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    // Get the ARG_SECTION_NUMBER int stored in this fragment's arguments,
    // and set a string formatted as "Tab: " to a TextView centered inside
    // the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_empty, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.tv_tab_number);
        textView.setText(getString(R.string.tab_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

}
