package com.example.recordfragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recordfragment.data.DatabaseHandler;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {

    DatabaseHandler db;

    public GraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = new DatabaseHandler(getContext());
        if (db.getAllRecords().size() < 10) {
            generateRandom(365);
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph, container, false);
        }


    public void generateRandom(int days) {
        Random random = new Random();
        String[] menu = {"Android", "JAVA", "Kotlin", "Python"};
        long oneDay = 24 * 60 * 60 * 1000;
        long date = System.currentTimeMillis() - oneDay * days;
        db = new DatabaseHandler(getContext());


        for (int i = 0; i < days; i++) {
            String title = menu[random.nextInt(4)];
            String time = String.valueOf(random.nextInt(300));
            date += oneDay;
            db.addRandomRecord(title, time, "", date);
        }
    }
}
