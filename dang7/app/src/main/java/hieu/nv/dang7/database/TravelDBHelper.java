package hieu.nv.dang7.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import hieu.nv.dang7.model.PhraseModel;
import java.util.ArrayList;
import java.util.List;

public class TravelDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "TravelGuide.db";
    private static final int DB_VERSION = 1;

    public TravelDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Bảng lưu địa điểm du lịch, văn hóa và đặc sản đi kèm
        db.execSQL("CREATE TABLE spots (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, history TEXT, specialty TEXT)");
        // Bảng lưu các câu giao tiếp thông dụng
        db.execSQL("CREATE TABLE phrases (id INTEGER PRIMARY KEY AUTOINCREMENT, vn TEXT, en TEXT)");

        // Chèn sẵn dữ liệu mẫu thực tế để test tính năng tìm kiếm đặc sản
        db.execSQL("INSERT INTO spots (name, history, specialty) VALUES ('Hà Nội', 'Thủ đô ngàn năm văn hiến, có Chùa Một Cột, Hồ Gươm.', 'Phở Thìn, Bún chả OBAMA, Cà phê trứng')");
        db.execSQL("INSERT INTO spots (name, history, specialty) VALUES ('Hạ Long', 'Kỳ quan thiên nhiên thế giới với hàng ngàn đảo đá vôi.', 'Chả mực Hạ Long, Sá sùng')");
        db.execSQL("INSERT INTO spots (name, history, specialty) VALUES ('Huế', 'Kinh đô phong kiến cuối cùng, văn hóa cung đình đặc sắc.', 'Bún bò Huế, Bánh bột lọc, Cơm hến')");

        // Chèn sẵn bộ câu giao tiếp song ngữ cơ bản
        db.execSQL("INSERT INTO phrases (vn, en) VALUES ('Xin chào', 'Hello')");
        db.execSQL("INSERT INTO phrases (vn, en) VALUES ('Bao nhiêu tiền?', 'How much is it?')");
        db.execSQL("INSERT INTO phrases (vn, en) VALUES ('Cám ơn', 'Thank you')");
        db.execSQL("INSERT INTO phrases (vn, en) VALUES ('Ngon quá!', 'Very delicious!')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS spots");
        db.execSQL("DROP TABLE IF EXISTS phrases");
        onCreate(db);
    }

    public List<PhraseModel> getAllPhrases() {
        List<PhraseModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM phrases", null);
        if (c.moveToFirst()) {
            do {
                list.add(new PhraseModel(c.getInt(0), c.getString(1), c.getString(2)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    // --- 🔥 LOGIC TRUY XUẤT TÌM KIẾM ĐẶC SẢN THEO TÊN ĐỊA ĐIỂM ---
    public String searchSpecialtyBySpot(String spotName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT specialty FROM spots WHERE name LIKE ?", new String[]{"%" + spotName + "%"});
        String result = "Sorry, no specialty data found for this location!";
        if (c.moveToFirst()) {
            result = c.getString(0);
        }
        c.close();
        return result;
    }
}