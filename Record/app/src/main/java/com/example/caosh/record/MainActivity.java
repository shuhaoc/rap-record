package com.example.caosh.record;

import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    private static final String ACCOMPANY_WAV = "/apple_15s.wav";
    private static final String OUT_RAW = "/out.raw";
    private static final String RECORD_WAV = "/record.wav";
    private static final String OUT_WAV = "/out.wav";

    private TextView textView;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private RecordThread recordThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.prompt);

        // TODO: fucking code, think about a better position to initialize
        // 获得缓冲区字节大小
        RecorderParameter.bufferSizeInBytes = AudioRecord.getMinBufferSize(RecorderParameter.sampleRateInHz,
                RecorderParameter.channelConfig, RecorderParameter.audioFormat) * 20;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRecord(View view) {
        startRecord();
//                startTest();
    }

    public void onTempo(View view) {
        startTest();
    }

    private void startTest() {
        StretchThread stretchThread = new StretchThread();
        stretchThread.setStateListener(new StateListener() {
            @Override
            public void onStart() {
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(R.string.recording);
                    }
                });
            }

            @Override
            public void onEnd() {
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(R.string.complete);
                    }
                });
                startMix();
            }
        });
        stretchThread.start();
    }

    private void startRecord() {
        recordThread = new RecordThread(getWorkingDirectory() + OUT_RAW,
                getWorkingDirectory() + RECORD_WAV);
        recordThread.setStateListener(new StateListener() {
            @Override
            public void onStart() {
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(R.string.recording);
                    }
                });
            }

            @Override
            public void onEnd() {
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(R.string.complete);
                    }
                });
            }
        });
        recordThread.start();
    }

    private void startMix() {
        GeneralTaskThread thread = new GeneralTaskThread() {
            @Override
            protected void onRun() {
                try {
                    new MixRunnable(getWorkingDirectory() + "/apple_15s.wav").run();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setStateListener(new StateListener() {
            @Override
            public void onStart() {
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(R.string.recording);
                    }
                });
            }

            @Override
            public void onEnd() {
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(R.string.complete);
                    }
                });
            }
        });
        thread.start();
    }

    public void onPlay(View view) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(getWorkingDirectory() + OUT_WAV);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onStop(View view) {
        if (recordThread != null) {
            recordThread.postStop();
        }
        mediaPlayer.stop();
    }

    private static String getWorkingDirectory() {
        return Environment.getExternalStorageDirectory() + "/_rec";
    }
}
