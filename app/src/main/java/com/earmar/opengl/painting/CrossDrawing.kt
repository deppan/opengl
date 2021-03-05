package com.earmar.opengl.painting

class CrossDrawing : Drawing {
    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun drawFrame() {
    }

    external fun a(): String
    external fun b(): String
}