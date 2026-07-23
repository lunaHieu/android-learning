package hieu.nv.dang3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import hieu.nv.dang3.model.AlarmModel;
import java.util.ArrayList;
import java.util.List;

public class AlarmDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "AlarmClock.db";
    private static final int DB_VERSION = 1;

    public AlarmDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE alarms (id INTEGER PRIMARY KEY AUTOINCREMENT, hour INTEGER, minute INTEGER, is_active INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS alarms");
        onCreate(db);
    }

    public long insertAlarm(int hour, int minute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("hour", hour);
        v.put("minute", minute);
        v.put("is_active", 1); // Mặc định tạo xong là bật luôn
        return db.insert("alarms", null, v);
    }

    public List<AlarmModel> getAllAlarms() {
        List<AlarmModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM alarms ORDER BY hour ASC, minute ASC", null);
        if (c.moveToFirst()) {
            do {
                list.add(new AlarmModel(c.getInt(0), c.getInt(1), c.getInt(2), c.getInt(3)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public void updateAlarmStatus(int id, int isActive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("is_active", isActive);
        db.update("alarms", v, "id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteAlarm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("alarms", "id = ?", new String[]{String.valueOf(id)});
    }
}