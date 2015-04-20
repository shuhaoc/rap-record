////////////////////////////////////////////////////////////////////////////////
///
/// Example class that invokes native SoundTouch routines through the JNI
/// interface.
///
/// Author        : Copyright (c) Olli Parviainen
/// Author e-mail : oparviai 'at' iki.fi
/// WWW           : http://www.surina.net
///
////////////////////////////////////////////////////////////////////////////////
//
// $Id: SoundTouch.java 165 2012-12-28 19:55:23Z oparviai $
//
////////////////////////////////////////////////////////////////////////////////

package net.surina.soundtouch;

public final class SoundTouch {
    // Native interface function that returns SoundTouch version string.
    // This invokes the native c++ routine defined in "soundtouch-jni.cpp".
//        public native static final String getVersionString();

//        public native static final void setTempo(float tempo);

//        public native static final void setSampleRate(int srate);

//        public native static final void setChannels(int channels);

//        public native static final void putSample(short[] samples);

//        public native static final short[] receiveSample(int maxSamples);

    public native static final void stretch(String inputWav, String outputWav,
                                            int tempoDelta, int pitchDelta, int rateDelta, boolean speech);

    // Load the native library upon startup
    static {
        System.loadLibrary("soundtouch");
    }
}
