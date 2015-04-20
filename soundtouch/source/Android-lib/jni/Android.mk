# Copyright (C) 2010 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# $Id: Android.mk 165 2012-12-28 19:55:23Z oparviai $

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

# *** Remember: Change -O0 into -O2 in add-applications.mk ***

LOCAL_MODULE    := soundtouch
LOCAL_SRC_FILES := soundtouch-jni.cpp ../../SoundTouch/AAFilter.cpp  ../../SoundTouch/FIFOSampleBuffer.cpp \
                ../../SoundTouch/FIRFilter.cpp ../../SoundTouch/cpu_detect_x86.cpp \
                ../../SoundTouch/RateTransposer.cpp ../../SoundTouch/SoundTouch.cpp \
                ../../SoundTouch/TDStretch.cpp ../../SoundTouch/BPMDetect.cpp ../../SoundTouch/PeakFinder.cpp \
                ../../SoundTouch/InterpolateShannon.cpp ../../SoundTouch/InterpolateCubic.cpp ../../SoundTouch/InterpolateLinear.cpp \
                ../../SoundStretch/WavFile.cpp ../../SoundStretch/RunParameters.cpp ../../SoundStretch/main.cpp

# for native audio
LOCAL_STATIC_LIBS    += -lgcc 
# --whole-archive -lgcc 
# for logging
LOCAL_LDLIBS    += -llog
# for native asset manager
#LOCAL_LDLIBS    += -landroid
# don't export all symbols
# added "-marm" switch to use arm instruction set instead of thumb for improved calculation performance.
#LOCAL_CFLAGS += -Wall -fvisibility=hidden -I ../../../include -D ST_NO_EXCEPTION_HANDLING -D SOUNDTOUCH_INTEGER_SAMPLES -fdata-sections -ffunction-sections -fexceptions -marm

LOCAL_CFLAGS += -Wall -I ../../../include -fexceptions -marm -D SOUNDTOUCH_INTEGER_SAMPLES 

include $(BUILD_SHARED_LIBRARY)
