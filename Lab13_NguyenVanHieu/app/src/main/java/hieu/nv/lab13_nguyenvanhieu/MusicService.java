package hieu.nv.lab13_nguyenvanhieu;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import java.util.ArrayList;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private ArrayList<String> currentPlaylist;
    private ArrayList<String> currentSongNames; // Thêm list chứa tên
    private int currentIndex = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnCompletionListener(mp -> {
            if (currentPlaylist != null && currentIndex < currentPlaylist.size() - 1) {
                currentIndex++;
                playSong(currentIndex);
            } else {
                Toast.makeText(MusicService.this, "Đã hết danh sách phát", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();

            if (action.equals("PLAY_NEW_LIST")) {
                currentPlaylist = intent.getStringArrayListExtra("PLAYLIST");
                currentSongNames = intent.getStringArrayListExtra("SONG_NAMES"); // Nhận list tên
                currentIndex = 0;
                if (currentPlaylist != null && !currentPlaylist.isEmpty()) {
                    playSong(currentIndex);
                }
            }
            else if (action.equals("PAUSE_RESUME")) {
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                else mediaPlayer.start();
            }
            else if (action.equals("NEXT")) {
                if (currentPlaylist != null && currentIndex < currentPlaylist.size() - 1) {
                    currentIndex++;
                    playSong(currentIndex);
                } else {
                    Toast.makeText(this, "Đây là bài cuối rồi!", Toast.LENGTH_SHORT).show();
                }
            }
            else if (action.equals("PREV")) {
                if (currentPlaylist != null && currentIndex > 0) {
                    currentIndex--;
                    playSong(currentIndex);
                } else {
                    Toast.makeText(this, "Đây là bài đầu tiên!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return START_STICKY;
    }

    private void playSong(int index) {
        try {
            Uri audioUri = Uri.parse(currentPlaylist.get(index));
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, audioUri);
            mediaPlayer.prepare();
            mediaPlayer.start();

            // Lấy tên bài hát và PHÁT SÓNG (Broadcast) về MainActivity
            String songName = (currentSongNames != null && index < currentSongNames.size())
                    ? currentSongNames.get(index) : "Unknown";

            Intent intent = new Intent("UPDATE_SONG_NAME");
            intent.putExtra("SONG_NAME", songName);
            intent.setPackage(getPackageName());
            sendBroadcast(intent); // Ra lệnh phóng thanh!

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi đọc file bài số " + (index + 1), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}