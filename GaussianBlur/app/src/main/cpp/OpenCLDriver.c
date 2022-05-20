//
// Created by 77ha on 2021-11-26.
//
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <android/log.h>
#include <android/bitmap.h>

//#include <CL/opencl.h>

#define LOG_TAG     "DEBUG"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

JNIEXPORT jobject JNICALL
Java_com_example_gaussianblur_MainActivity_GaussianBlurBitmap(JNIEnv *env, jclass class, jobject bitmap)
{
    //getting bitmap info:
//    LOGD("reading bitmap info...");
    AndroidBitmapInfo info;
    int ret;
    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0)
    {
//        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return NULL;
    }
//    LOGD("width:%d height:%d stride:%d", info.width, info.height, info.stride);
    if(info.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
    {
//        LOGE("Bitmap format is not RGBA_8888!");
        return NULL;
    }

    //read pixels of bitmap into native memory :
//    LOGD("reading bitmap pixels...");
    void* bitmapPixels;
    if((ret = AndroidBitmap_lockPixels(env, bitmap, &bitmapPixels)) < 0)
    {
//        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
        return NULL;
    }

    uint32_t* src = (uint32_t*) bitmapPixels;
    uint32_t* tempPixels = (uint32_t*)malloc(info.height * info.width*4);

    int pixelsCount = info.height * info.width;
    memcpy(tempPixels, src, sizeof(uint32_t) * pixelsCount);

    unsigned char *src_img;
    unsigned char *dst_img;
    src_img = (char) (tempPixels+'0');
    dst_img = (char) (bitmapPixels+'0');

    float red=0, green =0, blue=0;
    int row=0, col=0;
    int m, n, k;
    int pix;
    float mask[9][9]=
            {	{0.011237, 0.011637, 0.011931, 0.012111, 0.012172, 0.012111, 0.011931, 0.011637, 0.011237},
                 {0.011637, 0.012051, 0.012356, 0.012542, 0.012605, 0.012542, 0.012356, 0.012051, 0.011637},
                 {0.011931, 0.012356, 0.012668, 0.012860, 0.012924, 0.012860, 0.012668, 0.012356, 0.011931},
                 {0.012111, 0.012542, 0.012860, 0.013054, 0.013119, 0.013054, 0.012860, 0.012542, 0.012111},
                 {0.012172, 0.012605, 0.012924, 0.013119, 0.013185, 0.013119, 0.012924, 0.012605, 0.012172},
                 {0.012111, 0.012542, 0.012860, 0.013054, 0.013119, 0.013054, 0.012860, 0.012542, 0.012111},
                 {0.011931, 0.012356, 0.012668, 0.012860, 0.012924, 0.012860, 0.012668, 0.012356, 0.011931},
                 {0.011637, 0.012051, 0.012356, 0.012542, 0.012605, 0.012542, 0.012356, 0.012051, 0.011637},
                 {0.011237, 0.011637, 0.011931, 0.012111, 0.012172, 0.012111, 0.011931, 0.011637, 0.011237}
            };

    for(row=0; row<info.height; row++)
    {
        for(col=0; col<info.width; col++)
        {
            blue=0;
            green=0;
            red=0;
            for( m=0; m<9; m++)
            {
                for( n=0; n<9; n++)
                {
                    pix=(((row+m-4)%info.height)*info.width)*3 + ((col+n-4)%info.width)*3;
                    red   += src_img[pix + 0] * mask[m][n];
                    green += src_img[pix + 1] * mask[m][n];
                    blue  += src_img[pix + 2] * mask[m][n];
                }
            }
            dst_img[(row*info.width + col)*3 + 0] = red;
            dst_img[(row*info.width + col)*3 + 1] = green;
            dst_img[(row*info.width + col)*3 + 2] = blue;
            dst_img[(row*info.width + col)*3 + 3] = 255;
        }
    }
}

