package hieu.nv.kiemtra3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

import hieu.nv.kiemtra3.R;
import hieu.nv.kiemtra3.model.Book;

public class BookAdapter extends BaseAdapter {
    private Context context;
    private List<Book> bookList;
    private String userRole;
    private BookActionListener actionListener;

    public interface BookActionListener {
        void onEdit(Book book);
        void onDelete(int bookId);
    }

    public BookAdapter(Context context, List<Book> bookList, String userRole, BookActionListener actionListener) {
        this.context = context;
        this.bookList = bookList;
        this.userRole = userRole;
        this.actionListener = actionListener;
    }

    @Override
    public int getCount() { return bookList.size(); }
    @Override
    public Object getItem(int position) { return bookList.get(position); }
    @Override
    public long getItemId(int position) { return bookList.get(position).id; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        }

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvAuthor = convertView.findViewById(R.id.tvAuthor);
        TextView tvPublisher = convertView.findViewById(R.id.tvPublisher);
        TextView tvPrice = convertView.findViewById(R.id.tvPrice);
        LinearLayout layoutActions = convertView.findViewById(R.id.layoutActions);
        Button btnEdit = convertView.findViewById(R.id.btnEdit);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);

        Book book = bookList.get(position);

        tvTitle.setText(book.title);
        tvAuthor.setText("Tác giả: " + book.author);
        tvPublisher.setText("NXB: " + book.publisherName);
        tvPrice.setText("Giá: " + book.price + " VNĐ");

        if ("USER".equals(userRole)) {
            layoutActions.setVisibility(View.GONE);
        } else {
            layoutActions.setVisibility(View.VISIBLE);
        }

        btnEdit.setOnClickListener(v -> actionListener.onEdit(book));
        btnDelete.setOnClickListener(v -> actionListener.onDelete(book.id));

        return convertView;
    }
}