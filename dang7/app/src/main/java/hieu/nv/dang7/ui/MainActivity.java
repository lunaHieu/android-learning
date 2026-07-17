package hieu.nv.dang7.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hieu.nv.dang7.R;
import hieu.nv.dang7.database.TravelDBHelper;
import hieu.nv.dang7.model.PhraseModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PhraseAdapter.OnPhraseClickListener {
    private TravelDBHelper dbHelper;
    private RecyclerView rvPhrases;
    private PhraseAdapter adapter;
    private List<PhraseModel> phraseList = new ArrayList<>();

    private EditText edtSearch;
    private Button btnSearch;
    private TextToSpeech ttsEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new TravelDBHelper(this);
        edtSearch = findViewById(R.id.edt_search_spot);
        btnSearch = findViewById(R.id.btn_search_specialty);
        rvPhrases = findViewById(R.id.rv_phrases);

        // Khởi tạo bộ máy phát âm TextToSpeech hệ thống
        ttsEngine = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                // Ép cấu hình giọng phát âm chuẩn Tiếng Việt
                int result = ttsEngine.setLanguage(new Locale("vi", "VN"));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(MainActivity.this, "Vietnamese voice data is missing on this emulator!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rvPhrases.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PhraseAdapter(phraseList, this);
        rvPhrases.setAdapter(adapter);

        phraseList.addAll(dbHelper.getAllPhrases());
        adapter.notifyDataSetChanged();

        // Xử lý sự kiện tìm kiếm đặc sản địa điểm
        btnSearch.setOnClickListener(v -> {
            String keyword = edtSearch.getText().toString().trim();
            if (!keyword.isEmpty()) {
                String specialties = dbHelper.searchSpecialtyBySpot(keyword);

                // Hiển thị hộp thoại kết quả trực quan
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("🍽️ LOCAL SPECIALTIES")
                        .setMessage("Famous foods in " + keyword + ":\n\n" + specialties)
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                Toast.makeText(MainActivity.this, "Please enter a destination name!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- 🔥 TÍNH NĂNG THEO ĐỀ BÀI: CLICK LÀ ĐỌC PHÁT ÂM TIẾNG VIỆT ---
    @Override
    public void onPhraseClick(PhraseModel model) {
        if (ttsEngine != null) {
            // Thực thi lệnh đọc ngầm dứt khoát câu tiếng Việt ra loa
            ttsEngine.speak(model.getVietnamese(), TextToSpeech.QUEUE_FLUSH, null, null);
            Toast.makeText(this, "Speaking: " + model.getVietnamese(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Giải phóng tài nguyên loa tránh rò rỉ bộ nhớ hệ thống
        if (ttsEngine != null) {
            ttsEngine.stop();
            ttsEngine.shutdown();
        }
    }
}