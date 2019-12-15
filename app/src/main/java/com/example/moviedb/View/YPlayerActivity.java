package com.example.moviedb.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.moviedb.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import static com.example.moviedb.Util.VariableGlobal.TAG_YOUTUBE_KEY;

public class YPlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final String KEY = "Mohon ganti ke https://github.com/PierfrancescoSoffritti/android-youtube-player";
    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yplayer);

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        youTubePlayerView.initialize(KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean restored) {
        player.setFullscreen(true);

        if (!restored) {
            player.play();
            player.setShowFullscreenButton(false);
            player.loadVideo(getIntent().getExtras().getString(TAG_YOUTUBE_KEY));
        }

        player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {
                System.out.println("Loading");
                player.setShowFullscreenButton(false);
            }

            @Override
            public void onLoaded(String s) {
                player.setShowFullscreenButton(false);

            }

            @Override
            public void onAdStarted() {
                player.setShowFullscreenButton(false);
            }

            @Override
            public void onVideoStarted() {
                player.setShowFullscreenButton(false);
            }

            @Override
            public void onVideoEnded() {
                finish();
                finish();
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {
                finish();
            }
        });
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Failed to initialize Youtube Player", Toast.LENGTH_LONG).show();
        finish();
    }
}