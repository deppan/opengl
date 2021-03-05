package com.earmar.opengl.painting

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class TriangleDrawing : Drawing {
    private val vertexCoordinate = floatArrayOf(
        -1f, -1f,
        1f, -1f,
        0f, 1f
    )

    private val vertexSource = "attribute vec4 aPosition;" +
            "void main() {" +
            "  gl_Position = aPosition;" +
            "}"

    private val fragmentSource = "precision mediump float;" +
            "void main() {" +
            "  gl_FragColor = vec4(1.0, 1.0, 0.0, 1.0);" +
            "}"

    private var program: Int = -1
    private var positionIndex: Int = -1
    private var vertexBuffer: FloatBuffer

    init {
        val buffer = ByteBuffer.allocateDirect(vertexCoordinate.size * 4)
        buffer.order(ByteOrder.nativeOrder())
        vertexBuffer = buffer.asFloatBuffer()
        vertexBuffer.put(vertexCoordinate)
        vertexBuffer.position(0)
    }

    override fun drawFrame() {
        create()
        GLES20.glEnableVertexAttribArray(positionIndex)
        GLES20.glVertexAttribPointer(positionIndex, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 3)
    }

    fun release() {
        GLES20.glDisableVertexAttribArray(positionIndex)
        GLES20.glDeleteProgram(program)
    }

    private fun create() {
        if (program == -1) {
            program = GLES20.glCreateProgram()
            var shader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource)
            GLES20.glAttachShader(program, shader)
            shader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)
            GLES20.glAttachShader(program, shader)
            GLES20.glLinkProgram(program)

            positionIndex = GLES20.glGetAttribLocation(program, "aPosition")
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