package hieu.nv.dang12.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import hieu.nv.dang12.model.JobModel;
import java.util.ArrayList;
import java.util.List;

public class JobDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "JobManager.db";

    public JobDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE jobs (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, role TEXT, start_t TEXT, end_t TEXT, email TEXT, status TEXT)");

        // Dữ liệu mẫu ban đầu để kiểm thử đồ án
        db.execSQL("INSERT INTO jobs (title, role, start_t, end_t, email, status) VALUES ('Phát triển Module không gian MCDM Lao Cai', 'Chủ trì', '2026-05-01', '2026-06-15', 'hieu.dev.hunre@gmail.com', 'đang xử lý')");
        db.execSQL("INSERT INTO jobs (title, role, start_t, end_t, email, status) VALUES ('Thẩm định thiết kế phần mềm THedu', 'Chỉ đạo', '2026-05-10', '2026-05-25', 'thaygiaohuongdan@gmail.com', 'chưa xử lý')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS jobs");
        onCreate(db);
    }

    public void insertJob(String title, String role, String start, String end, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("title", title);
        v.put("role", role);
        v.put("start_t", start);
        v.put("end_t", end);
        v.put("email", email);
        v.put("status", "chưa xử lý"); // Mặc định tạo việc mới tinh là Chưa xử lý
        db.insert("jobs", null, v);
    }

    // CHỨC NĂNG THEO ĐỀ: Thực hiện báo cáo công việc cập nhật trạng thái (đang xử lý, đã xử lý)
    public void updateJobStatus(int id, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("status", newStatus);
        db.update("jobs", v, "id = ?", new String[]{String.valueOf(id)});
    }

    // Hàm lấy toàn bộ danh sách không lọc
    public List<JobModel> getAllJobs() {
        return getJobsByQuery("SELECT * FROM jobs ORDER BY id DESC", null);
    }

    // 💡 HÀM PHỤC VỤ TÍNH NĂNG TÙY CHỌN: Lọc danh sách công việc theo từng trạng thái cụ thể
    public List<JobModel> getJobsByStatus(String status) {
        return getJobsByQuery("SELECT * FROM jobs WHERE status = ? ORDER BY id DESC", new String[]{status});
    }

    private List<JobModel> getJobsByQuery(String sql, String[] args) {
        List<JobModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql, args);
        if (c.moveToFirst()) {
            do {
                list.add(new JobModel(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
}