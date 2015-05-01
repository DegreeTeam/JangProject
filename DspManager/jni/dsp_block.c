#include "com_example_dspmanager_DSPforJNI.h"
#include <stdlib.h>

JNIEXPORT jfloatArray JNICALL Java_com_example_dspmanager_DSPforJNI_DSPfromJNI(
		JNIEnv * env, jobject obj, jbyteArray input) {

	int i;

// INPUT
	jbyte * M = NULL;
	jint Length = ((2 * DataLen) + FilterLen - 1);
	M = (*env)->GetByteArrayElements(env, input, NULL);

// RESULT
	jfloatArray ret = (*env)->NewFloatArray(env, Length);
	jfloat result[Length];


//	for (i = 0; i < 8; i++) {
//		__android_log_print(ANDROID_LOG_DEBUG, "NDK_LOG", "%f\n",
//				(float) M[i * 512 + 1]);
//	}

	javaUpSampling(M, result);
	javaLinear_interpolation_filter(result);

//	for (i = 0; i < 8192; i++) {
//		__android_log_print(ANDROID_LOG_DEBUG, "NDK_LOG", "%f\n", result[i]);
//	}

	javaConvolution(env, ret, result);


//	for (i = 0; i < 8192; i++) {
//		__android_log_print(ANDROID_LOG_DEBUG, "NDK_LOG", "%f\n", result[i]);
//	}

	javaFunctionCall(env, obj, ret);

	return ret;

}
