package hieu.nv.kiemtra3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

import hieu.nv.kiemtra3.R;
import hieu.nv.kiemtra3.model.Publisher;

public class PublisherAdapter extends BaseAdapter {
    private Context context;
    private List<Publisher> publisherList;
    private PublisherActionListener actionListener;

    public interface PublisherActionListener {
        void onEdit(Publisher publisher);
        void onDelete(int publisherId);
    }

    public PublisherAdapter(Context context, List<Publisher> publisherList, PublisherActionListener actionListener) {
        this.context = context;
        this.publisherList = publisherList;
        this.actionListener = actionListener;
    }

    @Override
    public int getCount() { return publisherList.size(); }
    @Override
    public Object getItem(int position) { return publisherList.get(position); }
    @Override
    public long getItemId(int position) { return publisherList.get(position).id; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_publisher, parent, false);
        }

        TextView tvPubName = convertView.findViewById(R.id.tvPubName);
        Button btnEdit = convertView.findViewById(R.id.btnEdit);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);

        Publisher pub = publisherList.get(position);
        tvPubName.setText(pub.name);

        btnEdit.setOnClickListener(v -> actionListener.onEdit(pub));
        btnDelete.setOnClickListener(v -> actionListener.onDelete(pub.id));

        return convertView;
    }
}