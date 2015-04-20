package com.example.caosh.record;

import android.media.AudioFormat;
import android.media.MediaRecorder;

/**
 * AudioRecord创建参数类
 *
 * @author christ
 */
class RecorderParameter {
    // 音频获取源
    static final int audioSource = MediaRecorder.AudioSource.MIC;
    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    static final int sampleRateInHz = 44100;
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
    static final int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
    static final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    static final int sampleRateInByte = sampleRateInHz * 16 / 8 * 2;
    static final int bytesPerSecondInput = sampleRateInHz * 2;
    static final int bytesPerSecondOutput = sampleRateInHz * 2 * 2;
    // 缓冲区字节大小
    static int bufferSizeInBytes = 0;
}
