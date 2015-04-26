#include "dsp_convolution_ConvolutionJNI.h"
#include "jni.h"
#include <stdio.h>
#include <stdlib.h>

#define DataLen    		8192
#define FilterLen       5

JNIEXPORT jfloatArray JNICALL Java_dsp_convolution_ConvolutionJNI_convolutionfromJNI(
		JNIEnv * env, jobject obj, jfloatArray input) {

	jfloat * M = NULL;
	jint i, j, k, nconv;
	jfloat tmp;
	jfloat f[FilterLen] = { 0.2, 0.3, 0.4, 0.5, 0.8 };

	// ADD FunctionCall Code

	jclass cls = (*env)->GetObjectClass(env,obj);
	jmethodID funcM = (*env)->GetMethodID(env,cls,"func","(F)V");


	jfloat result[DataLen + FilterLen - 1];
	nconv = DataLen + FilterLen - 1;

	jfloatArray ret = (*env)->NewFloatArray(env, nconv); // M = 8 N = 5    Filter 길이를 5라고 가정

	M = (*env)->GetFloatArrayElements(env, input, NULL);
	for (i = 0; i < nconv; i++) {
		result[i] = 0.0;
	}

	// Convloution Code

	for (i = 0; i < nconv; i++) {
		k = i;
		tmp = 0.0;
		for (j = 0; j < FilterLen; j++) {
			if (k >= 0 && k < DataLen) {
				tmp += (M[k] * f[j]);
			}
			k--;
			result[i] = tmp;
		}
	}

	// Receive Function Call
	(*env)->CallVoidMethod(env,obj,funcM,0.2);
	(*env)->SetFloatArrayRegion(env, ret, 0, nconv, result);
	return ret;

}
