LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE :=libconvolution
LOCAL_SRC_FILES := convolution.c

include $(BUILD_SHARED_LIBRARY)