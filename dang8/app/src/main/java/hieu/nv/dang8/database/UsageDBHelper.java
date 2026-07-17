package hieu.nv.dang8.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import hieu.nv.dang8.model.AppUsageModel;
import java.util.ArrayList;
import java.util.List;

public class UsageDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "SocialGuard.db";
    private static final int DB_VERSION = 1;

    public UsageDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usage_limits (id INTEGER PRIMARY KEY AUTOINCREMENT, app_name TEXT, time_limit INTEGER, parent_phone TEXT)");

        // Chèn sẵn dữ liệu mẫu các MXH thông dụng để test
        db.execSQL("INSERT INTO usage_limits (app_name, time_limit, parent_phone) VALUES ('Facebook', 30, '0912345678')");
        db.execSQL("INSERT INTO usage_limits (app_name, time_limit, parent_phone) VALUES ('TikTok', 15, '0988888888')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usage_limits");
        onCreate(db);
    }

    public List<AppUsageModel> getAllLimits() {
        List<AppUsageModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM usage_limits", null);
        if (c.moveToFirst()) {
            do {
                list.add(new AppUsageModel(c.getInt(0), c.getString(1), c.getInt(2), c.getString(3)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
}