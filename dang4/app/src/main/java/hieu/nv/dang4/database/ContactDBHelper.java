package hieu.nv.dang4.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import hieu.nv.dang4.model.ContactModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ContactManager.db";
    private static final int DB_VERSION = 1;

    public ContactDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contacts (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone TEXT, email TEXT, group_name TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public void insertContact(String name, String phone, String email, String groupName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("name", name);
        v.put("phone", phone);
        v.put("email", email);
        v.put("group_name", groupName);
        db.insert("contacts", null, v);
    }

    // Tự động sắp xếp theo bảng chữ cái từ A-Z theo yêu cầu của đề (ORDER BY name ASC)
    public List<ContactModel> getAllContactsAlphabet() {
        List<ContactModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM contacts ORDER BY name ASC", null);
        if (c.moveToFirst()) {
            do {
                list.add(new ContactModel(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    // --- 🔥 THUẬT TOÁN HASHMAP: HỢP NHẤT SỐ TRÙNG LẶP TUYỆT ĐỐI CHÍNH XÁC ---
    public int mergeDuplicateContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<ContactModel> allContacts = getAllContactsAlphabet();
        Map<String, ContactModel> phoneMap = new HashMap<>();
        int mergeCount = 0;

        for (ContactModel current : allContacts) {
            String phone = current.getPhone().trim();

            if (phoneMap.containsKey(phone)) {
                // Phát hiện trùng số! Tiến hành gộp tên liên lạc cũ với liên lạc mới
                ContactModel existing = phoneMap.get(phone);
                String newName = existing.getName() + " & " + current.getName();

                // Cập nhật lại liên lạc gốc trong cơ sở dữ liệu
                ContentValues v = new ContentValues();
                v.put("name", newName);
                db.update("contacts", v, "id = ?", new String[]{String.valueOf(existing.getId())});

                // Xóa bỏ liên lạc trùng thừa thãi vừa quét trúng
                db.delete("contacts", "id = ?", new String[]{String.valueOf(current.getId())});

                // Cập nhật lại thông tin trong HashMap để quét tiếp
                existing.setName(newName);
                mergeCount++;
            } else {
                // Chưa trùng, đẩy vào map để làm mốc kiểm tra cho các liên lạc sau
                phoneMap.put(phone, current);
            }
        }
        return mergeCount; // Trả về số lượng dòng đã gộp để hiện Toast thông báo
    }
}