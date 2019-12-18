package com.example.recordfragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recordfragment.data.DatabaseHandler;
import com.example.recordfragment.model.Record;
import com.example.recordfragment.ui.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimelineFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Record> recordList;
    private DatabaseHandler db;

    private View view;
    private Context context;

    public TimelineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_timeline, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getContext();
        db = new DatabaseHandler(context);

        // リサイクラーヴューへの紐付け
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true); // これがないと消えるときの挙動が変です
        recordList = new ArrayList<>();
        recordList = db.getAllRecords();

        // アダプターを作成しそれにデータを渡す
        recyclerViewAdapter = new RecyclerViewAdapter(context, recordList);
        // リサイクラーヴューとアダプターを接続する
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewAdapter.notifyDataSetChanged();

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        int position = viewHolder.getAdapterPosition();
                        Record record = recyclerViewAdapter.getRecordPosition(position);
                        DatabaseHandler db = new DatabaseHandler(context);
                        // SQLiteからデータを削除する
                        db.deleteRecord(record.getId());
                        // 表示（リスト）から削除する
                        recordList.remove(position);
                        recyclerViewAdapter.notifyItemRemoved(position);

                    }
                }
        );
        helper.attachToRecyclerView(recyclerView);
    }
}