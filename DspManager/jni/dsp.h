#ifndef DSP_H
#define DSP_H

#include <android/log.h>
#define DataLen    		4096
#define FilterLen       40
#define DOWN_FACTOR   	2

extern void javaFunctionCall(JNIEnv * env, jobject obj, jfloatArray ret);
extern void javaConvolution(JNIEnv * env, jfloatArray ret, jfloat* input);
extern void javaLinear_interpolation_filter(float* data);
extern void javaUpSampling(char* poper, float* result);

#endif
