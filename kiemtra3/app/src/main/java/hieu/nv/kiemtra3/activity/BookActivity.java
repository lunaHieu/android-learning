package hieu.nv.kiemtra3.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import hieu.nv.kiemtra3.R;
import hieu.nv.kiemtra3.adapter.BookAdapter;
import hieu.nv.kiemtra3.database.DatabaseHelper;
import hieu.nv.kiemtra3.model.Book;
import hieu.nv.kiemtra3.model.Publisher;

public class BookActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private Spinner spinnerFilter;
    private Button btnAddBook, btnExportCSV;
    private ListView lvBooks;
    private String userRole;
    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        dbHelper = new DatabaseHelper(this);
        spinnerFilter = findViewById(R.id.spinnerFilter);
        btnAddBook = findViewById(R.id.btnAddBook);
        btnExportCSV = findViewById(R.id.btnExportCSV);
        lvBooks = findViewById(R.id.lvBooks);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userRole = prefs.getString("ROLE", "USER");

        if ("USER".equals(userRole)) {
            btnAddBook.setVisibility(View.GONE);
        }

        loadSpinnerData();

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Publisher selectedPub = (Publisher) parent.getSelectedItem();
                loadBooksData(selectedPub.id);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnAddBook.setOnClickListener(v -> showBookDialog(null));
        btnExportCSV.setOnClickListener(v -> exportToCSV());
    }

    private void loadSpinnerData() {
        List<Publisher> publishers = dbHelper.getAllPublishers();
        ArrayAdapter<Publisher> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, publishers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter);
    }

    private void loadBooksData(int publisherId) {
        bookList = dbHelper.getBooks(publisherId);
        BookAdapter bookAdapter = new BookAdapter(this, bookList, userRole, new BookAdapter.BookActionListener() {
            @Override
            public void onEdit(Book book) {
                showBookDialog(book);
            }

            @Override
            public void onDelete(int bookId) {
                new AlertDialog.Builder(BookActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa cuốn sách này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            dbHelper.deleteBook(bookId);
                            loadBooksData(publisherId);
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
        lvBooks.setAdapter(bookAdapter);
    }

    private void showBookDialog(Book bookToEdit) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_book);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText edtTitle = dialog.findViewById(R.id.edtTitle);
        EditText edtAuthor = dialog.findViewById(R.id.edtAuthor);
        EditText edtPrice = dialog.findViewById(R.id.edtPrice);
        Spinner spinnerPublisher = dialog.findViewById(R.id.spinnerPublisher);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        List<Publisher> pubList = dbHelper.getAllPublishers();
        if (!pubList.isEmpty() && pubList.get(0).id == 0) {
            pubList.remove(0);
        }

        ArrayAdapter<Publisher> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pubList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPublisher.setAdapter(adapter);

        if (bookToEdit != null) {
            edtTitle.setText(bookToEdit.title);
            edtAuthor.setText(bookToEdit.author);
            edtPrice.setText(String.valueOf(bookToEdit.price));
            for (int i = 0; i < pubList.size(); i++) {
                if (pubList.get(i).name.equals(bookToEdit.publisherName)) {
                    spinnerPublisher.setSelection(i);
                    break;
                }
            }
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String author = edtAuthor.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            if (title.isEmpty() || author.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Điền đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);
            Publisher selectedPub = (Publisher) spinnerPublisher.getSelectedItem();

            if (bookToEdit == null) {
                dbHelper.addBook(title, author, price, selectedPub.id);
            } else {
                dbHelper.updateBook(bookToEdit.id, title, author, price, selectedPub.id);
            }

            Publisher currentFilter = (Publisher) spinnerFilter.getSelectedItem();
            loadBooksData(currentFilter != null ? currentFilter.id : 0);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void exportToCSV() {
        List<Book> allBooks = dbHelper.getBooks(0);
        File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!exportDir.exists()) exportDir.mkdirs();

        File file = new File(exportDir, "DanhSachSach.csv");
        try {
            FileWriter fw = new FileWriter(file);
            fw.append("Tên Sách,Tác Giả,Giá,Nhà Xuất Bản\n");
            for (Book b : allBooks) {
                fw.append(b.title).append(",").append(b.author).append(",")
                        .append(String.valueOf(b.price)).append(",").append(b.publisherName).append("\n");
            }
            fw.flush(); fw.close();
            Toast.makeText(this, "Đã xuất CSV tại Downloads", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi xuất file!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if ("USER".equals(userRole)) {
            menu.findItem(R.id.action_manage_publishers).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_manage_publishers) {
            startActivity(new Intent(this, PublisherActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}