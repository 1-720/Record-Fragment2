package com.example.recordfragment.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.recordfragment.model.Record;
import com.example.recordfragment.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECORD_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_TITLE + " TEXT,"
                + Constants.KEY_TIME + " TEXT,"
                + Constants.KEY_COMMENT + " TEXT,"
                + Constants.KEY_DATE_ADDED + " LONG);";

        db.execSQL(CREATE_RECORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    // レコードの追加
    public void addRecord(Record record) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_TITLE, record.getRecordTitle());
        values.put(Constants.KEY_TIME, record.getRecordedTime()); // 時間は分に変換して格納する
        values.put(Constants.KEY_COMMENT, record.getRecordComment());
        values.put(Constants.KEY_DATE_ADDED, java.lang.System.currentTimeMillis());

        db.insert(Constants.TABLE_NAME, null, values);

        db.close();
    }

    // 全てのレコードを取得する
    public List<Record> getAllRecords() {

        SQLiteDatabase db = this.getReadableDatabase();

        // データ格納先のインスタンス
        List<Record> recordList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{
                        // ID、タイトル、時間、コメント、追加された日時
                        Constants.KEY_ID,
                        Constants.KEY_TITLE,
                        Constants.KEY_TIME,
                        Constants.KEY_COMMENT,
                        Constants.KEY_DATE_ADDED
                }, null, null, null, null,
                Constants.KEY_DATE_ADDED + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                // インスタンスへのID、タイトル、時間、コメントのセット
                record.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                record.setRecordTitle(cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)));
                record.setRecordedTime(cursor.getString(cursor.getColumnIndex(Constants.KEY_TIME)));
                record.setRecordComment(cursor.getString(cursor.getColumnIndex(Constants.KEY_COMMENT)));
                // データの取得時点（DatabaseHandlerクラス）で日時情報を加工しようとしていたが、それは、利用先に委ねることにした
                record.setDateRecordAdded(cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE_ADDED)));

                recordList.add(record);

            } while (cursor.moveToNext());
        }

        db.close();

        return recordList;
    }


    // レコードの削除
    public void deleteRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // レコードのアップデート
    public void updateRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_TITLE, record.getRecordTitle());
        values.put(Constants.KEY_TIME, record.getRecordedTime()); // 時間は分に変換して格納する
        values.put(Constants.KEY_COMMENT, record.getRecordComment());
        values.put(Constants.KEY_DATE_ADDED, record.getDateRecordAdded());

        db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?",
                new String[]{String.valueOf(record.getId())});

        db.close();
    }
}