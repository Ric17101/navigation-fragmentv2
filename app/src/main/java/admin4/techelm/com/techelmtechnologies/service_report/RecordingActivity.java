package admin4.techelm.com.techelmtechnologies.service_report;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import admin4.techelm.com.techelmtechnologies.R;

/**
 * @author admin 4 on 06/03/2017.
 * TODO: Handle Memory Card Full
 */
public class RecordingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTimerTextView;
    private Button mCancelButton;
    private Button mStopButton;
    private Button mPlayButton;
    String AudioSavePathInDevice = null;
    private File mOutputFile;

    private MediaRecorder mRecorder;
    private MediaPlayer mMediaPlayer;
    private long mStartTime = 0;

    private int[] amplitudes = new int[100];
    private int i = 0;

    private Handler mHandler = new Handler();
    private Runnable mTickExecutor = new Runnable() {
        @Override
        public void run() {
            tick();
            mHandler.postDelayed(mTickExecutor, 100);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_signing_off_record_voice);

        this.mTimerTextView = (TextView) this.findViewById(R.id.timer);
        this.mCancelButton = (Button) this.findViewById(R.id.cancel_button);
        this.mCancelButton.setOnClickListener(this);
        this.mStopButton = (Button) this.findViewById(R.id.share_button);
        this.mStopButton.setOnClickListener(this);
        this.mPlayButton = (Button) this.findViewById(R.id.play_button);
        this.mPlayButton.setOnClickListener(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Voice Recorder", "output: " + getOutputFile());
        startRecording();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRecorder != null) {
            stopRecording(false);
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            mRecorder.setAudioEncodingBitRate(48000);
        } else {
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setAudioEncodingBitRate(64000);
        }
        mRecorder.setAudioSamplingRate(16000);
        mOutputFile = getOutputFile();
        mOutputFile.getParentFile().mkdirs();
        mRecorder.setOutputFile(mOutputFile.getAbsolutePath());

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartTime = SystemClock.elapsedRealtime();
            mHandler.postDelayed(mTickExecutor, 100);
            Log.d("Voice Recorder", "started recording to " + mOutputFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("Voice Recorder", "prepare() failed " + e.getMessage());
        }
    }

    protected void stopRecording(boolean saveFile) {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        mStartTime = 0;
        mHandler.removeCallbacks(mTickExecutor);
        if (!saveFile && mOutputFile != null) {
            mOutputFile.delete();
        }
    }

    private File getOutputFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.US);
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Voice Recorder/RECORDING_"
                + dateFormat.format(new Date())
                + ".3gp");
    }

    private void tick() {
        long time = (mStartTime < 0) ? 0 : (SystemClock.elapsedRealtime() - mStartTime);
        int minutes = (int) (time / 60000);
        int seconds = (int) (time / 1000) % 60;
        int milliseconds = (int) (time / 100) % 10;
        mTimerTextView.setText(minutes + ":" + (seconds < 10 ? "0" + seconds : seconds) + "." + milliseconds);
        if (mRecorder != null) {
            amplitudes[i] = mRecorder.getMaxAmplitude();
            //Log.d("Voice Recorder","amplitude: "+(amplitudes[i] * 100 / 32767));
            if (i >= amplitudes.length - 1) {
                i = 0;
            } else {
                ++i;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_button:
                stopRecording(false);
                setResult(RESULT_CANCELED);
                // finish();
                break;
            case R.id.share_button:
                stopRecording(true);
                Uri uri = Uri.parse("file://" + mOutputFile.getAbsolutePath());
                Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                scanIntent.setData(uri);
                sendBroadcast(scanIntent);
                setResult(Activity.RESULT_OK, new Intent().setData(uri));
                // finish();
                break;
            case R.id.play_button:
                stopRecording(true);
                mMediaPlayer = new MediaPlayer();
                try {
                    mMediaPlayer.setDataSource(mOutputFile.getAbsolutePath());
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mMediaPlayer.start();
                Toast.makeText(RecordingActivity.this, "Recording Playing", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
