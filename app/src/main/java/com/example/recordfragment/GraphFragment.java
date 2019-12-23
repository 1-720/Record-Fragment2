package com.example.recordfragment;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recordfragment.data.DatabaseHandler;
import com.example.recordfragment.model.Record;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {

    BarChart barChart;
    LineChart lineChart;
    PieChart pieChart;

    View view;

    DatabaseHandler db;
    private List<Record> recordList;


    public GraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = new DatabaseHandler(getContext());
        recordList = new ArrayList<>();

        // 疑似データの生成
        if (db.getAllRecords().size() < 10) generateRandom(365);

        view = inflater.inflate(R.layout.fragment_graph, container, false);

        // 上記のviewの内部に、探す
        barChart = view.findViewById(R.id.barChart);
        lineChart = view.findViewById(R.id.lineChart);
        pieChart = view.findViewById(R.id.pieChart);

        // Inflate the layout for this fragment
        return view;
        }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recordList = db.getAllRecords();
        
        createLineChart();

        createBarChart();

        createPieChart();
    }

    private void createPieChart() {
        pieChart.setUsePercentValues(true);

        Map<String, Integer> map = new HashMap<>();
        map.put("Android", 0);
        map.put("JAVA", 0);
        map.put("Kotlin", 0);
        map.put("Python", 0);

        for (Record record: recordList) {
            map.put(record.getRecordTitle(), map.get(record.getRecordTitle())+1);
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(map.get("Android"), "Android"));
        entries.add(new PieEntry(map.get("JAVA"), "JAVA"));
        entries.add(new PieEntry(map.get("Kotlin"), "Kotlin"));
        entries.add(new PieEntry(map.get("Python"), "Python"));

        PieDataSet dataSet = new PieDataSet(entries, "項目ごとの割合");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);




    }

    private void createLineChart() {
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);

        ArrayList<Entry> entries = new ArrayList<>();

        // 累計時間を格納する
        List<Integer> cumSum = new ArrayList<>();
        cumSum.add(0);
        for (int i = 0; i < recordList.size(); i++) {
            int time = Integer.parseInt(recordList.get(i).getRecordedTime());
            cumSum.add(cumSum.get(i)+time);
        }

        for (int i = 0; i < cumSum.size(); i++) {
            int sumTime = cumSum.get(i);
            entries.add(new BarEntry(i, sumTime));
        }

        LineDataSet set = new LineDataSet(entries, "CumSum");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);



    }

    private void createBarChart() {
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        barChart.setMaxVisibleValueCount(7);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        for (int i = 0; i < recordList.size(); i++) {
            int time = Integer.parseInt(recordList.get(i).getRecordedTime());
            barEntries.add(new BarEntry(i, time));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Time");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.9f);

        barChart.setData(data);
    }

    public void generateRandom(int days) {
        Random random = new Random();
        String[] menu = {"Android", "JAVA", "Kotlin", "Python"};
        long oneDay = 24 * 60 * 60 * 1000;
        long date = System.currentTimeMillis() - oneDay * days;
        db = new DatabaseHandler(getContext());


        for (int i = 0; i < days; i++) {
            String title = menu[random.nextInt(4)];
            int time = random.nextInt(200);
            if (time > 30) {
                db.addRandomRecord(title, String.valueOf(time), "", date);
            }

            date += oneDay;
        }
    }
}
