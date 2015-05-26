#include "com_example_audiotcp_DSPforJNI.h"

void javaPauseSound(char* input) {
	__android_log_print(ANDROID_LOG_DEBUG, "NDK_LOG", "PAUSE-SOUND");
	int i;
	for (i = 0; i < DataLen * 2; i++) {
		input[i] = NO_SOUND_DATA;
	}
}

