package hieu.nv.dang4.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hieu.nv.dang4.R;
import hieu.nv.dang4.model.ContactModel;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Context context;
    private List<ContactModel> list;

    public ContactAdapter(Context context, List<ContactModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactModel model = list.get(position);
        holder.txtName.setText(model.getName());
        holder.txtSub.setText(model.getPhone() + " • " + model.getEmail());
        holder.txtGroup.setText(model.getGroupName());

        // Lấy chữ cái đầu tiên của tên viết hoa để làm avatar đại diện
        if (!model.getName().isEmpty()) {
            holder.txtLetter.setText(model.getName().substring(0, 1).toUpperCase());
        }
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtLetter, txtName, txtSub, txtGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLetter = itemView.findViewById(R.id.txt_avatar_letter);
            txtName = itemView.findViewById(R.id.txt_contact_name);
            txtSub = itemView.findViewById(R.id.txt_contact_phone_email);
            txtGroup = itemView.findViewById(R.id.txt_contact_group_tag);
        }
    }
}