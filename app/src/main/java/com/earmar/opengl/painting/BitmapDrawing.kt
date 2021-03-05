package com.earmar.opengl.painting

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class BitmapDrawing(private val bitmap: Bitmap) : Drawing {
    private val vertexCoordinate = floatArrayOf(
        -1f, -1f,
        1f, -1f,
        -1f, 1f,
        1f, 1f
    )
    private val textureCoordinate = floatArrayOf(
        0f, 1f,
        1f, 1f,
        0f, 0f,
        1f, 0f
    )
    private val vertexSource = "attribute vec4 aPosition;" +
            "attribute vec2 aCoordinate;" +
            "varying vec2 vCoordinate;" +
            "void main() {" +
            "  gl_Position = aPosition;" +
            "  vCoordinate = aCoordinate;" +
            "}"
    private val fragmentSource = "precision mediump float;" +
            "uniform sampler2D uTexture;" +
            "varying vec2 vCoordinate;" +
            "void main() {" +
            "  vec4 color = texture2D(uTexture, vCoordinate);" +
            "  gl_FragColor = color;" +
            "}"

    private var program: Int = -1
    private var textureId: Int = -1

    private var vertexIndex: Int = -1
    private var textureIndex: Int = -1
    private var samplerIndex: Int = -1

    private var vertexBuffer: FloatBuffer
    private var textureBuffer: FloatBuffer

    init {
        var buffer = ByteBuffer.allocateDirect(vertexCoordinate.size * 4)
        buffer.order(ByteOrder.nativeOrder())
        vertexBuffer = buffer.asFloatBuffer()
        vertexBuffer.put(vertexCoordinate)
        vertexBuffer.position(0)

        buffer = ByteBuffer.allocateDirect(textureCoordinate.size * 4)
        buffer.order(ByteOrder.nativeOrder())
        textureBuffer = buffer.asFloatBuffer()
        textureBuffer.put(textureCoordinate)
        textureBuffer.position(0)

        val array = IntArray(1)
        GLES20.glGenTextures(1, array, 0)
        textureId = array[0]
    }

    override fun drawFrame() {
        createProgram()
        activeTexture()
        bindBitmap2Texture()

        GLES20.glEnableVertexAttribArray(vertexIndex)
        GLES20.glEnableVertexAttribArray(textureIndex)
        GLES20.glVertexAttribPointer(vertexIndex, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer)
        GLES20.glVertexAttribPointer(textureIndex, 2, GLES20.GL_FLOAT, false, 0, textureBuffer)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    private fun bindBitmap2Texture() {
        if (!bitmap.isRecycled) {
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        }
    }

    private fun activeTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1i(textureIndex, 0)
        GLES20.glTexParameterf(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_LINEAR.toFloat()
        )
        GLES20.glTexParameterf(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR.toFloat()
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE
        )
    }

    private fun createProgram() {
        if (program == -1) {
            program = GLES20.glCreateProgram()
            val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource)
            GLES20.glAttachShader(program, vertexShader)
            val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)
            GLES20.glAttachShader(program, fragmentShader)
            GLES20.glLinkProgram(program)

            GLES20.glDeleteShader(vertexShader)
            GLES20.glDeleteShader(fragmentShader)

            vertexIndex = GLES20.glGetAttribLocation(program, "aPosition")
            textureIndex = GLES20.glGetAttribLocation(program, "aCoordinate")
            samplerIndex = GLES20.glGetAttribLocation(program, "uTexture")
        }

        GLES20.glUseProgram(program)
    }

    private fun loadShader(type: Int, source: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, source)
        GLES20.glCompileShader(shader)
        return shader
    }
}