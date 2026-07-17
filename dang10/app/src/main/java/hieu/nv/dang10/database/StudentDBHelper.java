package hieu.nv.dang10.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import hieu.nv.dang10.model.StudentModel;
import java.util.ArrayList;
import java.util.List;

public class StudentDBHelper extends SQLiteOpenHelper {
    public StudentDBHelper(Context context) { super(context, "StudentDB.db", null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE students(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, score REAL, email TEXT)");
        db.execSQL("INSERT INTO students(name, score, email) VALUES ('Nguyễn Văn Hiếu', 9.2, 'hieu.dev.java@gmail.com')");
        db.execSQL("INSERT INTO students(name, score, email) VALUES ('Trần Linh', 7.5, 'linhtran@gmail.com')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS students"); onCreate(db);
    }

    public void insertStudent(String name, double score, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("name", name); v.put("score", score); v.put("email", email);
        db.insert("students", null, v);
    }

    public List<StudentModel> getAllStudents() {
        List<StudentModel> list = new ArrayList<>();
        Cursor c = this.getReadableDatabase().rawQuery("SELECT * FROM students", null);
        if (c.moveToFirst()) {
            do {
                list.add(new StudentModel(c.getInt(0), c.getString(1), c.getDouble(2), c.getString(3)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    // ĐỀ BÀI YÊU CẦU: Tính điểm trung bình của toàn bộ học sinh trong danh sách lớp
    public double getAverageScore() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT AVG(score) FROM students", null);
        double avg = 0.0;
        if (c.moveToFirst()) { avg = c.getDouble(0); }
        c.close();
        return avg;
    }
}