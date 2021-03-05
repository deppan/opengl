package com.earmar.opengl

import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.earmar.opengl.databinding.ActivityMainBinding
import com.earmar.opengl.painting.BitmapDrawing

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.surfaceView) {
            setEGLContextClientVersion(2)
//            setRenderer(GraphicsRenderer(TriangleDrawing()))
            val bitmap = BitmapFactory.decodeResource(resources, R.raw.mountain_bluebird)
            setRenderer(GraphicsRenderer(BitmapDrawing(bitmap)))
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }
    }
}