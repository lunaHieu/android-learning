package hieu.nv.dang7.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hieu.nv.dang7.R;
import hieu.nv.dang7.model.PhraseModel;
import java.util.List;

public class PhraseAdapter extends RecyclerView.Adapter<PhraseAdapter.ViewHolder> {
    private List<PhraseModel> list;
    private OnPhraseClickListener listener;

    public interface OnPhraseClickListener {
        void onPhraseClick(PhraseModel model);
    }

    public PhraseAdapter(List<PhraseModel> list, OnPhraseClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phrase, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhraseModel model = list.get(position);
        holder.txtVn.setText(model.getVietnamese());
        holder.txtEn.setText(model.getEnglish());
        holder.itemView.setOnClickListener(v -> listener.onPhraseClick(model));
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtVn, txtEn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtVn = itemView.findViewById(R.id.txt_phrase_vn);
            txtEn = itemView.findViewById(R.id.txt_phrase_en);
        }
    }
}