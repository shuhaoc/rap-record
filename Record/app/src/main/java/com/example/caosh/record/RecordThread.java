package com.example.caosh.record;

import android.media.AudioRecord;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by caosh on 4/8/15.
 */
public class RecordThread extends GeneralTaskThread {
    // AudioRecord对象
    private AudioRecord recorder;
    private String rawFilePath;
    private String outFilePath;
    private boolean running = false;

    RecordThread(String rawFilePath, String outFilePath) {
        this.rawFilePath = rawFilePath;
        this.outFilePath = outFilePath;

        // 创建AudioRecord对象
        recorder = new AudioRecord(RecorderParameter.audioSource,
                RecorderParameter.sampleRateInHz,
                RecorderParameter.channelConfig,
                RecorderParameter.audioFormat,
                RecorderParameter.bufferSizeInBytes);
    }

    @Override
    public synchronized void start() {
        super.start();
        running = true;
    }

    @Override
    protected void onRun() {
        FileOutputStream rawOutputStream = null;
        try {
            rawOutputStream = new FileOutputStream(rawFilePath);

            int bufLen = RecorderParameter.bufferSizeInBytes;
            short[] buf = new short[bufLen];
            recorder.startRecording();
            while (running) {
                int len = recorder.read(buf, 0, bufLen);
                byte[] temp = new byte[len * 2];
                for (int i = 0; i < len; ++i) {
                    temp[i * 2] = (byte) (buf[i] & 0xff);
                    temp[i * 2 + 1] = (byte) ((buf[i] & 0xff00) >>> 8);
                }
                rawOutputStream.write(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            recorder.release();
            try {
                if (rawOutputStream != null) {
                    rawOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        WavUtil.copyWaveFile(rawFilePath, outFilePath, 1);
    }

    public void postStop() {
        running = false;
    }
}
