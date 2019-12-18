package com.example.recordfragment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.recordfragment.data.DatabaseHandler;
import com.example.recordfragment.model.Record;

import java.text.DateFormat;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity {

    // activity_input.xml（インプット画面）の要素を格納する変数
    private TextView inputTitle;
    private EditText inputTime;
    private EditText inputComment;
    private Button inputSaveButton;

    // pop_number_picker.xml（時間入力ポップアップ）の要素
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private NumberPicker pickerHour, pickerMinute;
    private Button pickerSaveButton;

    // ナンバーピッカーダイアログで受け取った数値を格納する変数
    private String hour, minute;

    // SQLite
    private DatabaseHandler db;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        intent = getIntent();

        // レイアウト内の要素の取得
        inputTitle = findViewById(R.id.input_title);
        inputTime = findViewById(R.id.input_time);
        inputComment = findViewById(R.id.input_comment);

        // titleのセット
        inputTitle.setText(intent.getStringExtra("title"));

        // timeのセット
        int temp = Integer.parseInt(intent.getStringExtra("time"));
        hour = String.valueOf(temp / 60);
        minute = String.valueOf(temp % 60);
        inputTime.setText(hour + "時間" + minute + "分");

        // commentのセット
        inputComment.setText(intent.getStringExtra("comment"));

        // 時間の入力
        inputTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInputPopup();
            }
        });

        inputSaveButton = findViewById(R.id.input_save_button);
        inputSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hour != null && minute != null && !(hour.equals("0") && minute.equals("0"))) {

                    Record record = new Record();

                    // インプット画面からタイトル、時間、コメントを取得
                    String newTitle = inputTitle.getText().toString();
                    String newTime = String.valueOf(Integer.parseInt(hour)*60 + Integer.parseInt(minute));
                    String newComment = inputComment.getText().toString();

                    // それをrecordインスタンスに記録
                    record.setRecordTitle(newTitle);
                    record.setRecordedTime(newTime);
                    record.setRecordComment(newComment);

                    // intentから受け取ったid, dateをセット
                    record.setId(Integer.parseInt(intent.getStringExtra("id")));
                    record.setDateRecordAdded(intent.getStringExtra("date"));

                    // 保存
                    db = new DatabaseHandler(UpdateActivity.this);
                    db.updateRecord(record);

                    Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                    intent.putExtra("from", "input");
                    startActivity(intent);
                }
            }
        });
    }

    private void createInputPopup() {
        // ポップアップダイアログの作成・表示
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.pop_number_picker, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        pickerHour = view.findViewById(R.id.picker_hour);
        pickerMinute = view.findViewById(R.id.picker_minute);

        // ナンバーピッカーの上限設定（23時間59分まで）
        pickerHour.setMaxValue(23);
        pickerMinute.setMaxValue(59);

        pickerSaveButton = view.findViewById(R.id.picker_save_button);

        pickerSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = String.valueOf(pickerHour.getValue());
                minute = String.valueOf(pickerMinute.getValue());

                // 入力した数値のセット
                inputTime.setText(hour + "時間" + minute + "分");

                // ダイアログを閉じる
                dialog.dismiss();
            }
        });
    }
}
