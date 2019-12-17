package com.example.recordfragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {

    // Android, JAVA, Kotlin, Python
    private TextView menu1, menu2, menu3, menu4;
    private View view;


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_menu, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        menu1 = view.findViewById(R.id.menu1);
        menu2 = view.findViewById(R.id.menu2);
        menu3 = view.findViewById(R.id.menu3);
        menu4 = view.findViewById(R.id.menu4);

        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);
        menu4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(getActivity(), InputActivity.class);

        switch (v.getId()) {
            case R.id.menu1:
                intent.putExtra("menu", "Android");
                break;
            case R.id.menu2:
                intent.putExtra("menu", "JAVA");
                break;
            case R.id.menu3:
                intent.putExtra("menu", "Kotlin");
                break;
            case R.id.menu4:
                intent.putExtra("menu", "Python");
                break;
        }

        startActivity(intent);
    }
}
