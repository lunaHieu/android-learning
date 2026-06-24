package hieu.nv.kiemtra3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import hieu.nv.kiemtra3.model.Book;
import hieu.nv.kiemtra3.model.Publisher;
import hieu.nv.kiemtra3.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BookManager.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");

        db.execSQL("CREATE TABLE Users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, role TEXT)");
        db.execSQL("CREATE TABLE Publishers (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
        db.execSQL("CREATE TABLE Books (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, author TEXT, price REAL, publisher_id INTEGER, FOREIGN KEY(publisher_id) REFERENCES Publishers(id))");

        db.execSQL("INSERT INTO Users (username, password, role) VALUES ('admin', 'admin', 'ADMIN')");
        db.execSQL("INSERT INTO Users (username, password, role) VALUES ('user', 'user', 'USER')");
        db.execSQL("INSERT INTO Publishers (name) VALUES ('NXB Kim Đồng')");
        db.execSQL("INSERT INTO Publishers (name) VALUES ('NXB Trẻ')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Books");
        db.execSQL("DROP TABLE IF EXISTS Publishers");
        db.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(db);
    }

    public User checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username=? AND password=?", new String[]{username, password});
        if (cursor.moveToFirst()) {
            User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }
    public List<Publisher> getAllPublishers() {
        List<Publisher> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Publishers", null);

        list.add(new Publisher(0, "Tất cả Nhà Xuất Bản"));

        if (cursor.moveToFirst()) {
            do {
                list.add(new Publisher(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean addPublisher(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        long result = db.insert("Publishers", null, values);
        return result != -1;
    }

    public boolean updatePublisher(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        int result = db.update("Publishers", values, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public void deletePublisher(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Publishers", "id=?", new String[]{String.valueOf(id)});
    }

    public boolean addBook(String title, String author, double price, int publisherId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("author", author);
        values.put("price", price);
        values.put("publisher_id", publisherId);
        long result = db.insert("Books", null, values);
        return result != -1;
    }

    public boolean updateBook(int id, String title, String author, double price, int publisherId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("author", author);
        values.put("price", price);
        values.put("publisher_id", publisherId);
        int result = db.update("Books", values, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public void deleteBook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Books", "id=?", new String[]{String.valueOf(id)});
    }

    public List<Book> getBooks(int publisherId) {
        List<Book> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT b.id, b.title, b.author, b.price, p.name FROM Books b INNER JOIN Publishers p ON b.publisher_id = p.id";
        Cursor cursor;

        if (publisherId > 0) {
            query += " WHERE p.id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(publisherId)});
        } else {
            cursor = db.rawQuery(query, null);
        }

        if (cursor.moveToFirst()) {
            do {
                list.add(new Book(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getString(4)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}