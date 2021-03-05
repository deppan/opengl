#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_earmar_opengl_painting_CrossDrawing_a(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from A";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_earmar_opengl_painting_CrossDrawing_b(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from B";
    return env->NewStringUTF(hello.c_str());
}