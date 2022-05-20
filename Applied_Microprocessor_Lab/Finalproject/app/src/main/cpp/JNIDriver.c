//
// Created by 77ha on 2021-12-03.
//

#include <jni.h>
#include <fcntl.h>
#include <string.h>
#include <stdio.h>
#include <unistd.h>

int fd = 0;
int fd1 = 0;
int fd2 = 0;

JNIEXPORT jint JNICALL Java_com_example_finalproject_JNIDriver_openDriver
        (JNIEnv *env, jclass class, jstring path) {
    jboolean iscopy;
    const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
    fd=open(path_utf, O_RDONLY);
    (*env)->ReleaseStringUTFChars(env, path, path_utf);

    if (fd<0)
        return -1;
    else
        return 1;
}

JNIEXPORT void JNICALL Java_com_example_finalproject_JNIDriver_closeDriver
(JNIEnv *env, jclass class) {
if(fd>0) close(fd);
}

JNIEXPORT jchar JNICALL Java_com_example_finalproject_JNIDriver_readDriver
        (JNIEnv *env, jobject obj){
    char ch = 0;

    if(fd>0) {
        read(fd, &ch, 1);
    }

    return ch;
}

JNIEXPORT jint JNICALL Java_com_example_finalproject_JNIDriver_getInterrupt
        (JNIEnv *env, jobject obj){
    int ret = 0;
    char value[100];
    char* ch1 = "Up";
    char* ch2= "Down";
    char* ch3 = "Left";
    char* ch4 = "Right";
    char* ch5 = "Center";
    ret = read(fd, &value, 100);

    if(ret<0)
        return -1;
    else {
        if (strcmp(ch1, value) == 0)
            return 1;
        else if (strcmp(ch2, value) == 0)
            return 2;
        else if (strcmp(ch3, value) == 0)
            return 3;
        else if (strcmp(ch4, value) == 0)
            return 4;
        else if (strcmp(ch5, value) == 0)
            return 5;
    }
    return 0;
}



JNIEXPORT jint JNICALL
Java_com_example_finalproject_MainActivity_openDriver1(JNIEnv *env, jclass clazz, jstring path) {
    jboolean iscopy;
    const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
    fd1 = open(path_utf, O_WRONLY);
    (*env)->ReleaseStringUTFChars(env, path, path_utf);

    if(fd1<0) return -1;
    else return 1;
}

JNIEXPORT void JNICALL
Java_com_example_finalproject_MainActivity_closeDriver1(JNIEnv *env, jclass clazz) {
    if(fd1>0) close(fd1);
}

JNIEXPORT void JNICALL
Java_com_example_finalproject_MainActivity_writeDriver1(JNIEnv *env, jclass clazz, jbyteArray data, jint length) {
    jbyte* chars = (*env)->GetByteArrayElements(env, data, 0);
    if(fd1>0) write(fd1, (unsigned char*)chars, length);
    (*env)->ReleaseByteArrayElements(env, data, chars, 0);
}

JNIEXPORT jint JNICALL
Java_com_example_finalproject_MainActivity_openDriver2
        (JNIEnv *env, jclass class, jstring path)
{
    jboolean iscopy;
    const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
    fd2 = open(path_utf, O_WRONLY);
    (*env)->ReleaseStringUTFChars(env, path, path_utf);

    if(fd2<0) return -1;
    else return 1;
}

JNIEXPORT void JNICALL
Java_com_example_finalproject_MainActivity_closeDriver2
        (JNIEnv *env, jclass class)
{
    if(fd2>0) close(fd2);
}

JNIEXPORT void JNICALL
Java_com_example_finalproject_MainActivity_writeDriver2
        (JNIEnv *env, jclass class, jbyteArray arr, jint count)
{
    jbyte* chars = (*env)->GetByteArrayElements(env, arr, 0);
    if(fd2>0) write(fd2, (unsigned char*)chars, count);
    (*env)->ReleaseByteArrayElements(env, arr, chars, 0);
}
