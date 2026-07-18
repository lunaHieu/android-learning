package hieu.nv.dang5.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import hieu.nv.dang5.model.RecordModel;
import java.util.ArrayList;
import java.util.List;

public class RecordDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "RecordAudio.db";
    private static final int DB_VERSION = 1;

    public RecordDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE records (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, path TEXT, timestamp LONG, length TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS records");
        onCreate(db);
    }

    public void insertRecord(String title, String path, String length) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("title", title);
        v.put("path", path);
        v.put("timestamp", System.currentTimeMillis());
        v.put("length", length);
        db.insert("records", null, v);
    }

    public List<RecordModel> getAllRecords() {
        List<RecordModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM records ORDER BY timestamp DESC", null);
        if (c.moveToFirst()) {
            do {
                list.add(new RecordModel(c.getInt(0), c.getString(1), c.getString(2), c.getLong(3), c.getString(4)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public void updateRecordTitle(int id, String newTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("title", newTitle);
        db.update("records", v, "id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("records", "id = ?", new String[]{String.valueOf(id)});
    }
}