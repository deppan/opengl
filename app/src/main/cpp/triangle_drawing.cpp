//
// Created by earmar on 3/5/21.
//
#include "triangle_drawing.h"
#include <malloc.h>

TriangleDrawing::TriangleDrawing() = default;

TriangleDrawing::~TriangleDrawing() = default;

void TriangleDrawing::GLCreateShaderProgram() {
    if (m_program_id == 0) {
        m_program_id = glCreateProgram();
        if (glGetError() != GL_NO_ERROR) {
            return;
        }
        GLuint vertexShader = loadShader(GL_VERTEX_SHADER, vertex_source);
        GLuint fragmentShader = loadShader(GL_FRAGMENT_SHADER, fragment_source);
        glAttachShader(m_program_id, vertexShader);
        glAttachShader(m_program_id, fragmentShader);
        glLinkProgram(m_program_id);
        m_vertex_pos_handler = glGetAttribLocation(m_program_id, "aPosition");
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    if (m_program_id != 0) {
        glUseProgram(m_program_id);
    }
}

GLuint TriangleDrawing::loadShader(GLenum type, const GLchar *shader_code) {
    GLuint shader = glCreateShader(type);
    glShaderSource(shader, 1, &shader_code, nullptr);
    glCompileShader(shader);

    GLint compiled;
    glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
    if (!compiled) {
        GLint infoLen = 0;
        glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
        if (infoLen > 1) {
            GLchar *infoLog = (GLchar *) malloc(sizeof(GLchar) * infoLen);
            glGetShaderInfoLog(shader, infoLen, nullptr, infoLog);
            free(infoLog);
        }
        glDeleteShader(shader);
        return 0;
    }
    return shader;
}

void TriangleDrawing::onDraw() {
    GLCreateShaderProgram();
    glEnableVertexAttribArray(m_vertex_pos_handler);
    glVertexAttribPointer(m_vertex_pos_handler, 2, GL_FLOAT, GL_FALSE, 0, m_vertex_coordinate);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 3);
}

void TriangleDrawing::release() {
    glDisableVertexAttribArray(m_vertex_pos_handler);
    glBindTexture(GL_TEXTURE_2D, 0);
    glDeleteProgram(m_program_id);
}