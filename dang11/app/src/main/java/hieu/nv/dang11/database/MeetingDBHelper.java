package hieu.nv.dang11.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import hieu.nv.dang11.model.MeetingModel;
import java.util.ArrayList;
import java.util.List;

public class MeetingDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "MeetingManager.db";
    private static final int DB_VERSION = 1;

    public MeetingDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE meetings (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, attendees TEXT, host TEXT, time TEXT, location TEXT, status INTEGER)");

        // Chèn sẵn 1 cuộc họp mẫu để đi thi có dữ liệu test ngay lập tức
        db.execSQL("INSERT INTO meetings (title, attendees, host, time, location, status) VALUES ('Họp Đồ Án Tốt Nghiệp Khóa 2023-2027', 'gvhd.hunre@gmail.com, hieu.it.2005@gmail.com', 'Trưởng khoa CNTT', '09:00 - 2026-06-05', 'Phòng Hội Thảo tầng 2', 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS meetings");
        onCreate(db);
    }

    public void insertMeeting(String title, String attendees, String host, String time, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("title", title);
        v.put("attendees", attendees);
        v.put("host", host);
        v.put("time", time);
        v.put("location", location);
        v.put("status", 0); // Mặc định tạo cuộc họp mới là chưa có ai xem
        db.insert("meetings", null, v);
    }

    // Hàm cập nhật trạng thái Đã xem / Chưa xem lịch họp theo ID
    public void updateMeetingStatus(int id, int currentStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("status", currentStatus);
        db.update("meetings", v, "id = ?", new String[]{String.valueOf(id)});
    }

    public List<MeetingModel> getAllMeetings() {
        List<MeetingModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM meetings ORDER BY id DESC", null);
        if (c.moveToFirst()) {
            do {
                list.add(new MeetingModel(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
}