//
// Created by earmar on 3/5/21.
//

#ifndef OPENGL_DRAWING_H
#define OPENGL_DRAWING_H

class Drawing {
    virtual void onDraw() = 0;

    virtual void release() = 0;
};

#endif //OPENGL_DRAWING_H
