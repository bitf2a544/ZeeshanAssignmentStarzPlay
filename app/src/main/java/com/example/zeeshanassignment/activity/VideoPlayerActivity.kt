package com.example.zeeshanassignment.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.exoplayer.ExoPlayer
import android.net.Uri
import android.view.View

import androidx.media3.common.MediaItem
import com.example.zeeshanassignment.BuildConfig
import com.example.zeeshanassignment.databinding.ActivityVideoPlayerBinding

class VideoPlayerActivity : AppCompatActivity() {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var binding: ActivityVideoPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Hide the system UI for fullscreen experience
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        // Initialize ExoPlayer
        exoPlayer = ExoPlayer.Builder(this).build()
        binding.playerView.player = exoPlayer

        // Set the video URL
        val mediaItem = MediaItem.fromUri(Uri.parse(BuildConfig.VIDEO_LINK))
        exoPlayer.setMediaItem(mediaItem)

        // Prepare and play the video
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        exoPlayer.release() // Release the player when not in use
    }
}