#include "com_example_dspmanager_DSPforJNI.h"

/*
 * Upsampling Module
 */

void javaUpSampling(char* poper, float* result) {
	__android_log_print(ANDROID_LOG_DEBUG, "NDK_LOG", "INTO UPSAMPLING");
	int i;
	float upto = 128;
	for (i = 0; i < DataLen; i++) {
		result[DOWN_FACTOR * i + 1] = (float) poper[i] / upto;
		__android_log_print(ANDROID_LOG_DEBUG, "NDK_LOG", " %d : %f, %f\n", i, (float) poper[i], result[DOWN_FACTOR * i + 1]);
	}
}

