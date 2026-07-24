package hieu.nv.dang2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import hieu.nv.dang2.model.VideoModel;
import java.util.ArrayList;
import java.util.List;

public class VideoDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "VideoManager.db";
    private static final int DB_VERSION = 1;

    public VideoDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE videos (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, path TEXT, timestamp LONG, category TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS videos");
        onCreate(db);
    }

    public void insertVideo(String title, String path, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("title", title);
        v.put("path", path);
        v.put("timestamp", System.currentTimeMillis());
        v.put("category", category);
        db.insert("videos", null, v);
    }

    // sortType: "NAME" -> Xếp chữ cái A-Z | "DATE" -> Xếp ngày mới nhất lên đầu
    public List<VideoModel> getAllVideos(String sortType) {
        List<VideoModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String orderBy = "timestamp DESC";
        if ("NAME".equals(sortType)) {
            orderBy = "title ASC";
        }

        Cursor c = db.rawQuery("SELECT * FROM videos ORDER BY " + orderBy, null);
        if (c.moveToFirst()) {
            do {
                list.add(new VideoModel(c.getInt(0), c.getString(1), c.getString(2), c.getLong(3), c.getString(4)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public void updateVideo(int id, String newTitle, String newCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("title", newTitle);
        v.put("category", newCategory);
        db.update("videos", v, "id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteVideo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("videos", "id = ?", new String[]{String.valueOf(id)});
    }
}