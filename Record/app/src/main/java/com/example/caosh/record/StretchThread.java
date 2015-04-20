package com.example.caosh.record;

import net.surina.soundtouch.SoundTouch;

/**
 * Calls soundstretch program.
 */
public class StretchThread extends GeneralTaskThread {
    @Override
    protected void onRun() {
        String dir = "/sdcard/_rec";
        SoundTouch.stretch(
                dir + "/record.wav",
                dir + "/record2.wav",
                30,     // tempo
                0,      // pitch
                0,      // rate
                true);  // speech
    }
}
