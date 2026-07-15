package hieu.nv.nguyenvanhieu_128.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import hieu.nv.nguyenvanhieu_128.model.TopicModel;
import java.util.ArrayList;
import java.util.List;

public class TopicDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ScienceTopic.db";

    public TopicDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE topics (id TEXT PRIMARY KEY, title TEXT, host TEXT, budget REAL, year INTEGER, result TEXT)");
        db.execSQL("INSERT INTO topics VALUES ('DT01', 'Nghiên cứu không gian MCDM Lao Cai', 'Nguyễn Văn Hiếu', 50000000, 2026, 'Xuất sắc')");
        db.execSQL("INSERT INTO topics VALUES ('DT02', 'Phân tích viễn thám lưu vực sông', 'Trần Thị Linh', 35000000, 2025, 'Đạt')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int n) {
        db.execSQL("DROP TABLE IF EXISTS topics");
        onCreate(db);
    }

    public void insertTopic(String id, String title, String host, double budget, int year, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("id", id); v.put("title", title); v.put("host", host);
        v.put("budget", budget); v.put("year", year); v.put("result", result);
        db.insertWithOnConflict("topics", null, v, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void updateTopic(String id, String title, String host, double budget, int year, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("title", title); v.put("host", host);
        v.put("budget", budget); v.put("year", year); v.put("result", result);
        db.update("topics", v, "id = ?", new String[]{id});
    }

    public void deleteTopic(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("topics", "id = ?", new String[]{id});
    }

    public List<TopicModel> getAllTopics() {
        return getTopicsByQuery("SELECT * FROM topics ORDER BY year DESC", null);
    }

    public List<TopicModel> searchTopics(String keyword) {
        return getTopicsByQuery("SELECT * FROM topics WHERE host LIKE ? OR year = ?", new String[]{"%" + keyword + "%", keyword});
    }

    private List<TopicModel> getTopicsByQuery(String sql, String[] args) {
        List<TopicModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor c = db.rawQuery(sql, args);
            if (c.moveToFirst()) {
                do {
                    list.add(new TopicModel(c.getString(0), c.getString(1), c.getString(2), c.getDouble(3), c.getInt(4), c.getString(5)));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}