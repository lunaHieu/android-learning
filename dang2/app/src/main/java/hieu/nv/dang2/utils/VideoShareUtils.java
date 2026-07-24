package hieu.nv.dang2.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class VideoShareUtils {
    public static void shareVideoViaIntent(Context context, String pathString) {
        try {
            Uri uri = Uri.parse(pathString);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("video/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(intent, "Chia sẻ Video qua:"));
        } catch (Exception e) {
            Toast.makeText(context, "Không thể chia sẻ video này!", Toast.LENGTH_SHORT).show();
        }
    }
}