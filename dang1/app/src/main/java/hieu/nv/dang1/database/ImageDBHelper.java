package hieu.nv.dang1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import hieu.nv.dang1.model.ImageModel;
import java.util.ArrayList;
import java.util.List;

public class ImageDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ImageManager.db";
    private static final int DB_VERSION = 1;

    public ImageDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Album
        db.execSQL("CREATE TABLE albums (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
        // Tạo bảng Ảnh
        db.execSQL("CREATE TABLE images (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, path TEXT, timestamp LONG, album_id INTEGER)");

        // Chèn sẵn vài album mẫu để đi thi có dữ liệu test luôn
        db.execSQL("INSERT INTO albums (name) VALUES ('Mặc định')");
        db.execSQL("INSERT INTO albums (name) VALUES ('Du lịch')");
        db.execSQL("INSERT INTO albums (name) VALUES ('Gia đình')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS images");
        db.execSQL("DROP TABLE IF EXISTS albums");
        onCreate(db);
    }

    // --- LẤY DANH SÁCH ALBUM ĐỂ ĐỔ VÀO SPINNER LỌC ---
    public List<String> getAllAlbumNames() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM albums", null);
        if (cursor.moveToFirst()) {
            do { list.add(cursor.getString(0)); } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public int getAlbumIdByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM albums WHERE name = ?", new String[]{name});
        int id = 1;
        if (cursor.moveToFirst()) id = cursor.getInt(0);
        cursor.close();
        return id;
    }

    // --- CRUD ẢNH THEO ALBUM VÀ SẮP XẾP THỜI GIAN ---
    public void insertImage(String title, String path, int albumId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("title", title);
        v.put("path", path);
        v.put("timestamp", System.currentTimeMillis()); // Hiển thị theo thời gian thêm vào
        v.put("album_id", albumId);
        db.insert("images", null, v);
    }

    public List<ImageModel> getImagesByAlbum(int albumId) {
        List<ImageModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Sắp xếp thời gian mới nhất lên đầu (ORDER BY timestamp DESC)
        Cursor c = db.rawQuery("SELECT * FROM images WHERE album_id = ? ORDER BY timestamp DESC", new String[]{String.valueOf(albumId)});
        if (c.moveToFirst()) {
            do {
                list.add(new ImageModel(c.getInt(0), c.getString(1), c.getString(2), c.getLong(3), c.getInt(4)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public void updateImage(int id, String newTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("title", newTitle);
        db.update("images", v, "id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteImage(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("images", "id = ?", new String[]{String.valueOf(id)});
    }
}