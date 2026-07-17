package hieu.nv.dang9.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import hieu.nv.dang9.model.MedicineModel;
import java.util.ArrayList;
import java.util.List;

public class MedicineDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "PharmacyManager.db";
    private static final int DB_VERSION = 2; // Nâng lên phiên bản 2 để cập nhật cấu trúc cột code

    public MedicineDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE medicines (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, expiry_date TEXT, quantity INTEGER, location TEXT, code TEXT)");

        // Chèn sẵn dữ liệu mẫu có chứa mã mặt hàng và giá giả lập trong tên hoặc xử lý trực tiếp
        db.execSQL("INSERT INTO medicines (name, expiry_date, quantity, location, code) VALUES ('Paracetamol 500mg (Giá: 20000đ)', '2027-12-31', 20, 'Thuốc Hạ Sốt', 'MS01')");
        db.execSQL("INSERT INTO medicines (name, expiry_date, quantity, location, code) VALUES ('Siro Ho Prospan (Giá: 75000đ)', '2026-06-01', 3, 'Thuốc Ho', 'MS02')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS medicines");
        onCreate(db);
    }

    public void insertMedicine(String name, String expiryDate, int quantity, String location, String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("name", name);
        v.put("expiry_date", expiryDate);
        v.put("quantity", quantity);
        v.put("location", location);
        v.put("code", code);
        db.insert("medicines", null, v);
    }

    // ĐỀ BÀI YÊU CẦU: Sắp xếp mặt hàng theo hạn sử dụng (Từ cận hạn nhất đến xa nhất)
    public List<MedicineModel> getAllMedicines() {
        List<MedicineModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM medicines ORDER BY expiry_date ASC", null);
        if (c.moveToFirst()) {
            do {
                list.add(new MedicineModel(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4), c.getString(5)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    // ĐỀ BÀI YÊU CẦU: Truy xuất thông tin của mặt hàng theo MÃ
    public MedicineModel getMedicineByCode(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM medicines WHERE code = ?", new String[]{code});
        MedicineModel model = null;
        if (c.moveToFirst()) {
            model = new MedicineModel(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4), c.getString(5));
        }
        c.close();
        return model;
    }

    public void deleteMedicine(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("medicines", "id = ?", new String[]{String.valueOf(id)});
    }
}