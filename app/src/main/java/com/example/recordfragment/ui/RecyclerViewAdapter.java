package com.example.recordfragment.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recordfragment.InputActivity;
import com.example.recordfragment.R;
import com.example.recordfragment.UpdateActivity;
import com.example.recordfragment.data.DatabaseHandler;
import com.example.recordfragment.model.Record;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Record> recordList;

    // deleteポップアップに利用する
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Record> recordList) {
        this.context = context;
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        Record record = recordList.get(position);

        holder.recordTitle.setText(record.getRecordTitle());
        holder.recordedTime.setText(record.getRecordedTime());
        holder.recordComment.setText(record.getRecordComment());

        // unix time を読めるよう変換する
        DateFormat dateFormat = DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(Long.valueOf(record.getDateRecordAdded())).getTime());

        holder.recordDateAdded.setText(formattedDate);

    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public int id;

        // ViewHolderに抱え込む変数たち
        public TextView recordTitle; // タイトル
        public TextView recordedTime; // 時間
        public TextView recordComment; // コメント
        public TextView recordDateAdded; // 追加日時

        public Button editButton; // 修正ボタン
        public Button deleteButton; // 削除ボタン

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            // 各要素の取得
            recordTitle = itemView.findViewById(R.id.list_row_title);
            recordedTime = itemView.findViewById(R.id.list_row_time);
            recordComment = itemView.findViewById(R.id.list_row_comment);
            recordDateAdded = itemView.findViewById(R.id.list_row_date_added);

            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);


            // edit, deleteボタンを押してからの処理
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {
            
            int position;
            Record record;
            
            switch (v.getId()) {
                case R.id.edit_button:
                    position = getAdapterPosition(); // アダプターのポジションの取得し
                    record = recordList.get(position); // それと対応するrecordを取得
                    editRecord(record);
                    break;
                case R.id.delete_button:
                    position = getAdapterPosition();
                    record = recordList.get(position);
                    deleteRecord(record.getId(), position);
                    break;
            }
            
        }
    }

    private void deleteRecord(final int id, final int position) {
        builder = new AlertDialog.Builder(context);
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pop_confirmation, null);

        Button yesButton = view.findViewById(R.id.confirmation_yes);
        Button noButton = view.findViewById(R.id.confirmation_no);

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(context);
                // SQLiteからデータを削除する
                db.deleteRecord(id);
                // 表示（リスト）から削除する
                recordList.remove(position);
                notifyItemRemoved(position);

                dialog.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void editRecord(Record record) {
        // エディットボタンを押すと、inputActivityの方へと遷移する
        // TODO: 遷移先をInputActivityでなく、UpdateActivityにする
        Intent intent = new Intent(context, UpdateActivity.class);
        // インテントに遷移元の情報をもたせる
        intent.putExtra("from", "RecyclerViewAdapter");
        // ID, タイトル、時間、コメント、追加日時
        intent.putExtra("id", String.valueOf(record.getId()));
        intent.putExtra("title", record.getRecordTitle());
        intent.putExtra("time", String.valueOf(record.getRecordedTime()));
        intent.putExtra("comment", record.getRecordComment());
        intent.putExtra("date", record.getDateRecordAdded());

        context.startActivity(intent);
    }
}
