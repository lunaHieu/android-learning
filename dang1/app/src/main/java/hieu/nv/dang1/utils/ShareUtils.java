package hieu.nv.dang1.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class ShareUtils {
    public static void shareImageViaIntent(Context context, String pathString) {
        try {
            // Chuyển ngược chuỗi lưu trong DB thành Uri chuẩn hệ thống
            Uri uri = Uri.parse(pathString);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            // Cấp quyền đọc Uri trực tiếp cho ứng dụng đích nhận file
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            context.startActivity(Intent.createChooser(intent, "Chia sẻ ảnh qua:"));
        } catch (Exception e) {
            Toast.makeText(context, "Không thể chia sẻ bức ảnh này!", Toast.LENGTH_SHORT).show();
        }
    }
}