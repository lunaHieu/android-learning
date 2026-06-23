package hieu.nv.ktra2_nguyenvanhieu.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import hieu.nv.ktra2_nguyenvanhieu.database.DatabaseHelper;
import hieu.nv.ktra2_nguyenvanhieu.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private SQLiteDatabase db;

    public TransactionDAO(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public long insertTransaction(Transaction transaction) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, transaction.getTitle());
        values.put(DatabaseHelper.COLUMN_AMOUNT, transaction.getAmount());
        values.put(DatabaseHelper.COLUMN_CATEGORY, transaction.getCategory());
        values.put(DatabaseHelper.COLUMN_DATE, transaction.getDate());
        values.put(DatabaseHelper.COLUMN_NOTE, transaction.getNote());
        values.put(DatabaseHelper.COLUMN_TYPE, transaction.getType());
        return db.insert(DatabaseHelper.TABLE_TRANSACTIONS, null, values);
    }
    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_TRANSACTIONS, null, null, null, null, null, DatabaseHelper.COLUMN_DATE + " DESC");
        if (cursor.moveToFirst()) {
            do {
                Transaction t = new Transaction();
                t.setId(cursor.getInt(0));
                t.setTitle(cursor.getString(1));
                t.setAmount(cursor.getDouble(2));
                t.setCategory(cursor.getString(3));
                t.setDate(cursor.getString(4));
                t.setNote(cursor.getString(5));
                t.setType(cursor.getInt(6));
                list.add(t);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    public int updateTransaction(Transaction transaction) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, transaction.getTitle());
        values.put(DatabaseHelper.COLUMN_AMOUNT, transaction.getAmount());
        values.put(DatabaseHelper.COLUMN_CATEGORY, transaction.getCategory());
        values.put(DatabaseHelper.COLUMN_DATE, transaction.getDate());
        values.put(DatabaseHelper.COLUMN_NOTE, transaction.getNote());
        values.put(DatabaseHelper.COLUMN_TYPE, transaction.getType());

        return db.update(DatabaseHelper.TABLE_TRANSACTIONS, values,
                DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(transaction.getId())});
    }
    public int deleteTransaction(int id) {
        return db.delete(DatabaseHelper.TABLE_TRANSACTIONS,
                DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }
}