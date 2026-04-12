package com.example.istreamapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.example.istreamapp.R;
import com.example.istreamapp.database.AppDatabase;
import com.example.istreamapp.database.PlaylistItem;
import com.example.istreamapp.session.SessionManager;
import com.example.istreamapp.viewmodel.SharedViewModel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeFragment extends Fragment {

    private WebView webViewPlayer;
    private EditText etVideoUrl;
    private String currentUrl = "";
    private SessionManager session;
    private AppDatabase db;
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        session = new SessionManager(requireContext());
        db = AppDatabase.getDatabase(requireContext());
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        webViewPlayer = view.findViewById(R.id.webViewPlayer);
        etVideoUrl    = view.findViewById(R.id.etVideoUrl);
        TextView tvWelcome = view.findViewById(R.id.tvWelcome);
        tvWelcome.setText("Welcome, " + session.getFullName());

        webViewPlayer.getSettings().setJavaScriptEnabled(true);
        webViewPlayer.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webViewPlayer.setWebViewClient(new WebViewClient());
        webViewPlayer.setWebChromeClient(new WebChromeClient());

        showPlayerPlaceholder();

        view.findViewById(R.id.btnPlay).setOnClickListener(v -> {
            String url = etVideoUrl.getText().toString().trim();
            if (url.isEmpty()) {
                Toast.makeText(getContext(), "Enter a YouTube URL", Toast.LENGTH_SHORT).show();
                return;
            }
            String videoId = extractYouTubeId(url);
            if (videoId != null) {
                currentUrl = url;
                loadYouTubePlayer(videoId);
            } else {
                Toast.makeText(getContext(), "Invalid YouTube URL.\nEnter a valid YouTube link.", Toast.LENGTH_LONG).show();
            }
        });

        view.findViewById(R.id.btnAddPlaylist).setOnClickListener(v -> {
            if (currentUrl.isEmpty()) {
                Toast.makeText(getContext(), "Please play a video first before adding to playlist", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(() -> {
                PlaylistItem duplicate = db.playlistDao().findDuplicate(session.getUserId(), currentUrl);requireActivity().runOnUiThread(() -> {
                    if (duplicate != null) {
                        Toast.makeText(getContext(), "This video is already in your playlist", Toast.LENGTH_SHORT).show();
                    } else {
                        new Thread(() -> {
                            db.playlistDao().addToPlaylist(
                                    new PlaylistItem(
                                            session.getUserId(), currentUrl, "Video " + extractYouTubeId(currentUrl)
                                    )
                            );
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(getContext(), "Added to your playlist!", Toast.LENGTH_SHORT).show());
                        }).start();
                    }
                });
            }).start();
        });

        view.findViewById(R.id.btnMyPlaylist).setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_home_to_playlist));

        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            session.logout();
            NavHostFragment.findNavController(this).navigate(R.id.action_home_to_login);
        });

        sharedViewModel.getSelectedUrl().observe(getViewLifecycleOwner(), url -> {
            if (url != null && !url.isEmpty()) {
                etVideoUrl.setText(url);
                String videoId = extractYouTubeId(url);
                if (videoId != null) {
                    currentUrl = url;
                    loadYouTubePlayer(videoId);
                }
                sharedViewModel.setSelectedUrl("");
            }
        });
    }

    private String extractYouTubeId(String url) {
        if (url == null || url.isEmpty()) return null;
        String[] patterns = {
                "(?:youtube\\.com/watch\\?v=)([a-zA-Z0-9_-]{11})",
                "(?:youtu\\.be/)([a-zA-Z0-9_-]{11})",
                "(?:youtube\\.com/embed/)([a-zA-Z0-9_-]{11})",
                "(?:youtube\\.com/shorts/)([a-zA-Z0-9_-]{11})"
        };
        for (String pattern : patterns) {
            Matcher matcher = Pattern.compile(pattern).matcher(url);
            if (matcher.find()) return matcher.group(1);
        }
        return null;
    }

    private void loadYouTubePlayer(String videoId) {
        String html = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "  * { margin:0; padding:0; box-sizing:border-box; }"
                + "  body { background:#000; width:100%; height:100vh; }"
                + "  iframe { width:100%; height:100%; border:none; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<iframe "
                + "  src=\"https://www.youtube.com/embed/" + videoId + "?autoplay=1&playsinline=1\" "
                + "  frameborder=\"0\" "
                + "  allow=\"autoplay; encrypted-media; fullscreen\" "
                + "  allowfullscreen>"
                + "</iframe>"
                + "</body>"
                + "</html>";
        webViewPlayer.loadDataWithBaseURL(
                "https://www.youtube.com", html, "text/html", "utf-8", null);
    }

    private void showPlayerPlaceholder() {
        String html = "<!DOCTYPE html>"
                + "<html><head>"
                + "<style>"
                + "  body { margin:0; background:#1a1a1a; display:flex; "
                + "         align-items:center; justify-content:center; height:100vh; }"
                + "  .msg { color:#888; font-family:sans-serif; font-size:16px; text-align:center; }"
                + "  .icon { font-size:48px; display:block; margin-bottom:10px; }"
                + "</style></head><body>"
                + "<div class='msg'>"
                + "  <span class='icon'>▶️</span>"
                + "  Enter a YouTube URL and tap Play"
                + "</div>"
                + "</body></html>";
        webViewPlayer.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
    }

    @Override
    public void onDestroy() {
        if (webViewPlayer != null) {
            webViewPlayer.destroy();
        }
        super.onDestroy();
    }
}