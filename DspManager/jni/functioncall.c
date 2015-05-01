#include "com_example_dspmanager_DSPforJNI.h"

/*
 * FunctionCall Module
 */

void javaFunctionCall(JNIEnv * env, jobject obj, jfloatArray ret) {
	jclass cls = (*env)->GetObjectClass(env, obj);
	jmethodID funcM = (*env)->GetMethodID(env, cls, "func", "([FII)V");
	(*env)->CallVoidMethod(env, obj, funcM, ret, 0, 8192);
}

