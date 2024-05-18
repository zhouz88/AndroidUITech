package com.example.myapplication.chapter10

import android.content.Context
import android.graphics.SurfaceTexture
import android.media.MediaParser
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView

class MyFirstTexttureView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextureView(context, attrs) {
   var mediaPlayer: MediaPlayer? = null

    var mSurface: Surface? = null
    init {
        surfaceTextureListener = object : SurfaceTextureListener{
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                mSurface = Surface(surface)
                startVideo()
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

            }

        }
    }


    private fun startVideo() {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context.assets.openFd("party_challenge_pop.mp4"))
            setSurface(mSurface)
            setOnPreparedListener {
                mediaPlayer?.isLooping = true
                mediaPlayer?.start()
            }
            prepare()
        }
    }

}