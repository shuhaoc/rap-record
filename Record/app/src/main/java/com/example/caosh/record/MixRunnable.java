package com.example.caosh.record;

import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MixRunnable implements Runnable {

    // 设置运行状态
    private boolean isRunning = true;
    // 伴奏文件
    private FileInputStream accompany;
    private FileInputStream recordStream;

    public MixRunnable(String accompany) throws FileNotFoundException {
        this.accompany = new FileInputStream(accompany);
        recordStream = new FileInputStream("/sdcard/_rec/record2.wav");
    }

    @Override
    public void run() {
        try {
            // 跳过头
            accompany.read(new byte[44]);
            recordStream.read(new byte[44]);
            File file = new File("/sdcard/_rec/out.raw");
            FileOutputStream fos = new FileOutputStream(file);
            // 开始读
            byte[] sourceReader = new byte[RecorderParameter.bufferSizeInBytes * 2];
            short[] sourceShortArray;
            byte[] audioReader = new byte[sourceReader.length / 2];
            short[] audioShortArray;

            while (isRunning) {
                int sourceReadSize = accompany.read(sourceReader, 0, sourceReader.length);
                if (sourceReadSize < 0) {
                    isRunning = false;
                    break;
                }
                sourceShortArray = byteToShortArray(sourceReader, sourceReadSize / 2);
                int audioReadSize = recordStream.read(audioReader, 0, sourceReadSize / 2);
                if (audioReadSize < 0) {
                    isRunning = false;
                    break;
                }
                audioShortArray = byteToShortArray(audioReader, audioReadSize / 2);
                short[] oneSecond = mixVoice(sourceShortArray, audioShortArray, audioReadSize);
                byte[] outStream = new byte[oneSecond.length * 2];
                for (int i = 0; i < oneSecond.length; i++) {
                    byte[] b = shortToByteArray(oneSecond[i]);
                    outStream[2 * i] = b[0];
                    outStream[2 * i + 1] = b[1];
                }
                Log.d("mtime4 ", "" + System.currentTimeMillis());
                fos.write(outStream);
            }
            fos.close();
            WavUtil.copyWaveFile("/sdcard/_rec/out.raw", "/sdcard/_rec/out.wav", 2);
            Log.i("Mix", "complete!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private short[] mixVoice(short[] source, short[] audio, int items) {
        short[] array = new short[items];
        for (int i = 0; i < items; i++) {
            // 伴奏双声道，录音单声道，所以要除以2
            array[i] = (short) ((source[i] * 0.2 + audio[i / 2] * 1.1));
        }
        return array;
    }

    /**
     * byte数组转换成short数组
     *
     * @param data
     * @param items
     * @return
     */
    private short[] byteToShortArray(byte[] data, int items) {
        short[] retVal = new short[items];
        for (int i = 0; i < retVal.length; i++)
            retVal[i] = (short) ((data[i * 2] & 0xff) | (data[i * 2 + 1] & 0xff) << 8);
        return retVal;
    }

    /**
     * short转byte数组
     *
     * @param s
     * @return
     */
    private byte[] shortToByteArray(short s) {
        byte[] shortBuf = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (shortBuf.length - 2 + i) * 8;
            shortBuf[i] = (byte) ((s >>> offset) & 0xff);
        }
        return shortBuf;
    }

}