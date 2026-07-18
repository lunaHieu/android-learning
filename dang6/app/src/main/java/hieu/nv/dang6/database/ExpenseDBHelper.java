package hieu.nv.dang6.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import hieu.nv.dang6.model.ExpenseModel;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "FinanceManager.db";
    private static final int DB_VERSION = 1;

    public ExpenseDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Bảng lưu giao dịch Thu Chi
        db.execSQL("CREATE TABLE transactions (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, amount REAL, type TEXT)");
        // Bảng lưu tài khoản tiết kiệm ngân hàng
        db.execSQL("CREATE TABLE savings (id INTEGER PRIMARY KEY AUTOINCREMENT, bank TEXT, capital REAL, rate REAL, months INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS transactions");
        db.execSQL("DROP TABLE IF EXISTS savings");
        onCreate(db);
    }

    public void insertTransaction(String title, double amount, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("title", title);
        v.put("amount", amount);
        v.put("type", type);
        db.insert("transactions", null, v);
    }

    public void insertSaving(String bank, double capital, double rate, int months) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("bank", bank);
        v.put("capital", capital);
        v.put("rate", rate);
        v.put("months", months);
        db.insert("savings", null, v);
    }

    public List<ExpenseModel> getAllTransactions() {
        List<ExpenseModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM transactions ORDER BY id DESC", null);
        if (c.moveToFirst()) {
            do {
                list.add(new ExpenseModel(c.getInt(0), c.getString(1), c.getDouble(2), c.getString(3)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public double getTotalAmountByType(String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(amount) FROM transactions WHERE type = ?", new String[]{type});
        double total = 0;
        if (c.moveToFirst()) total = c.getDouble(0);
        c.close();
        return total;
    }
}