#include "com_example_dspmanager_DSPforJNI.h"

/*
 * BPF Convolution Module
 */

jfloat bpf[FilterLen] = { 0.3009, 0.6262, 0.2689, -0.2396, -0.0394, 0.1637,
		-0.0818, -0.0547, 0.1030, -0.0441, -0.0417, 0.0717, -0.0317, -0.0287,
		0.0528, -0.0269, -0.0173, 0.0387, -0.0232, -0.0094, 0.0286, -0.0209,
		-0.0024, 0.0191, -0.0166, 0.0006, 0.0135, -0.0147, 0.0048, 0.0059,
		-0.0086, 0.0028, 0.0055, -0.0095, 0.0072, -0.0017, -0.0029, 0.0042,
		-0.0031, 0.0013 };

void javaConvolution(JNIEnv * env, jfloatArray ret, jfloat* input) {

	jint nconv = (2 * DataLen) + FilterLen - 1;
	jfloat tmp;
	jint i, j, k;

	jfloat result[nconv];

	for (i = 0; i < nconv; i++) {
		k = i;
		tmp = 0.0;
		for (j = 0; j < FilterLen; j++) {
			if (k >= 0 && k < DataLen) {
				tmp += (input[k] * bpf[j]);
			}
			k--;
			result[i] = tmp;
		}
	}

	(*env)->SetFloatArrayRegion(env, ret, 0, nconv, result);

}
