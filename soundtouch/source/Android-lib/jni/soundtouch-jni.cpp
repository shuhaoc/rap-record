////////////////////////////////////////////////////////////////////////////////
///
/// Example Interface class for SoundTouch native compilation
///
/// Author        : Copyright (c) Olli Parviainen
/// Author e-mail : oparviai 'at' iki.fi
/// WWW           : http://www.surina.net
///
////////////////////////////////////////////////////////////////////////////////
//
// $Id: soundtouch-jni.cpp 173 2013-06-15 11:44:11Z oparviai $
//
////////////////////////////////////////////////////////////////////////////////

#include <jni.h>
#include <android/log.h>

#include "../../../include/SoundTouch.h"

#define LOGV(...)   __android_log_print((int)ANDROID_LOG_INFO, "SOUNDTOUCH", __VA_ARGS__)
//#define LOGV(...)


//#define DLL_PUBLIC __attribute__ ((visibility ("default")))

using namespace soundtouch;

static SoundTouch soundTouch; 

extern "C" {
/*
 * Class:     net_surina_soundtouch_SoundTouch
 * Method:    getVersionString
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_net_surina_soundtouch_SoundTouch_getVersionString
  (JNIEnv* env, jobject) {
    LOGV("JNI call SoundTouch.getVersionString");

    // Call example SoundTouch routine
    const char* verStr = SoundTouch::getVersionString();

    // return version as string
    return env->NewStringUTF(verStr);
}

/*
 * Class:     net_surina_soundtouch_SoundTouch
 * Method:    setTempo
 * Signature: (F)V
 */
JNIEXPORT void JNICALL Java_net_surina_soundtouch_SoundTouch_setTempo
  (JNIEnv* env, jobject, jfloat tempo) {
   //soundTouch.setSetting(SETTING_SEQUENCE_MS, 40);
    //soundTouch.setSetting(SETTING_SEEKWINDOW_MS, 15);
    //soundTouch.setSetting(SETTING_OVERLAP_MS, 8);
    soundTouch.setTempo(tempo);
    soundTouch.setSetting(SETTING_USE_AA_FILTER, 1);
    soundTouch.setSetting(SETTING_USE_QUICKSEEK, 0);
}

/*
 * Class:     net_surina_soundtouch_SoundTouch
 * Method:    setSampleRate
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_surina_soundtouch_SoundTouch_setSampleRate
  (JNIEnv *, jobject, jint sampleRate) {
    soundTouch.setSampleRate(sampleRate); 
}

/*
 * Class:     net_surina_soundtouch_SoundTouch
 * Method:    setChannels
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_surina_soundtouch_SoundTouch_setChannels
  (JNIEnv *, jclass, jint channels) {
    soundTouch.setChannels(channels);
}

/*
 * Class:     net_surina_soundtouch_SoundTouch
 * Method:    putSample
 * Signature: ([S)V
 */
JNIEXPORT void JNICALL Java_net_surina_soundtouch_SoundTouch_putSample
  (JNIEnv* env, jobject, jshortArray samples) {
    int len = env->GetArrayLength(samples);
    jboolean copy;
    jshort* buf = env->GetShortArrayElements(samples, &copy);
    soundTouch.putSamples(buf, len / 2);
    env->ReleaseShortArrayElements(samples, buf, 0);
    LOGV("putSamples: %d", len);
    //LOGV("receiveSamples: %d,%d", soundTouch.numUnprocessedSamples(), soundTouch.numSamples());
}

/*
 * Class:     net_surina_soundtouch_SoundTouch
 * Method:    receiveSample
 * Signature: (I)[S
 */
JNIEXPORT jshortArray JNICALL Java_net_surina_soundtouch_SoundTouch_receiveSample
  (JNIEnv* env, jobject, jint maxSamples) {
    soundTouch.flush();
    jshort* buf = new jshort[maxSamples * 2];
    LOGV("receiveSamples: %d,%d,%d,0x%x", soundTouch.numUnprocessedSamples(), soundTouch.numSamples(), maxSamples, (unsigned)buf);
    jsize samples = soundTouch.receiveSamples(buf, maxSamples);
    jshortArray ret = NULL; 
    LOGV("receiveSamples: %d,%d,%d", soundTouch.numUnprocessedSamples(), soundTouch.numSamples(), samples);
    if (samples > 0) {
        ret = env->NewShortArray(samples * 2);
        env->SetShortArrayRegion(ret, 0, samples * 2, buf);
    }
    delete[] buf;
    return ret;
}

}

