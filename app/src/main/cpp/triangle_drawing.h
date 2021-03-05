//
// Created by earmar on 3/5/21.
//

#ifndef OPENGL_TRIANGLE_DRAWING_H
#define OPENGL_TRIANGLE_DRAWING_H

#include "drawing.h"
#include <GLES3/gl3.h>

class TriangleDrawing : public Drawing {

    const GLfloat m_vertex_coordinate[6] = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            0.0f, 1.0f
    };

    const GLchar *vertex_source = R"(
  attribute vec4 aPosition;
  void main() {
    gl_Position = aPosition;
  }
)";

    const GLchar *fragment_source = R"(
  precision mediump float;
  void main()
  {
    gl_FragColor = vec4(1.0,0.0,0.0,0.0,1.0);
  }
)";

    GLuint m_program_id = 0;
    GLint m_vertex_pos_handler = -1;

    void GLCreateShaderProgram();

    GLuint loadShader(GLenum type, const GLchar *shader_code);

public:
    TriangleDrawing();

    ~TriangleDrawing();

    void onDraw() override;

    void release() override;
};

#endif //OPENGL_TRIANGLE_DRAWING_H
