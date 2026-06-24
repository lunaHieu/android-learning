package com.example.kiemtra_nguyenvanhieu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.kiemtra_nguyenvanhieu.model.Task;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ToDoApp.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "cong_viec";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                "Ma INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Tieu_de TEXT, " +
                "Mo_ta TEXT, " +
                "Loai_cong_viec TEXT, " +
                "Ngay_tao TEXT, " +
                "Trang_thai INTEGER)";
        db.execSQL(createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Tieu_de", task.getTieuDe());
        values.put("Mo_ta", task.getMoTa());
        values.put("Loai_cong_viec", task.getLoaiCongViec());
        values.put("Ngay_tao", task.getNgayTao());
        values.put("Trang_thai", task.getTrangThai());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public List<Task> getAllTasks() {
        List<Task> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY Ma DESC", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Task(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getInt(5)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Tieu_de", task.getTieuDe());
        values.put("Mo_ta", task.getMoTa());
        values.put("Loai_cong_viec", task.getLoaiCongViec());
        values.put("Trang_thai", task.getTrangThai());
        db.update(TABLE_NAME, values, "Ma = ?", new String[]{String.valueOf(task.getMa())});
        db.close();
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "Ma = ?", new String[]{String.valueOf(taskId)});
        db.close();
    }
}
