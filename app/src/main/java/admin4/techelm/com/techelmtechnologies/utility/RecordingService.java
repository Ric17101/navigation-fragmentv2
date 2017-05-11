package admin4.techelm.com.techelmtechnologies.utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import admin4.techelm.com.techelmtechnologies.R;
import admin4.techelm.com.techelmtechnologies.db.servicejob.RecordingSJDBUtil;
import admin4.techelm.com.techelmtechnologies.activity.service_report.ServiceReport_1;

public class RecordingService extends Service {

    private static final String RECORD_SERVICE_KEY = "SERVICE_ID";
    private static final String RECORD_SERVICE_TAKEN_KEY = "TAKEN";
    private int mServiceID;
    private String mTaken;

    private static final String LOG_TAG = "RecordingService";
    private static final SimpleDateFormat mTimerFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());

    private String mFileName = null;

    private File mOutputFile;
    private String mFilePath = null;
    private boolean external = true;

    private MediaRecorder mRecorder = null;

    private RecordingSJDBUtil mDatabase;
    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;
    private int mElapsedSeconds = 0;
    private OnTimerChangedListener onTimerChangedListener = null;

    private Timer mTimer = null;
    private TimerTask mIncrementTimerTask = null;
    private static final String TAG = RecordingService.class.getSimpleName();
    private String directoryName = "recording";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // This not working??? I dont know why, is it because this class is only a Service Class???
    public interface OnTimerChangedListener {
        void onTimerChanged(int seconds);
        // RecordingSJDBUtil onHandleGetDB();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            // onTimerChangedListener = (OnTimerChangedListener) getApplication();
            mDatabase = new RecordingSJDBUtil(getApplicationContext(),
                    "From Recording service, can implement Interface here.");
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e(LOG_TAG, "Must implement the ProjectJobListener in the Activity", ex);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();
        mServiceID = bundle.getInt(RECORD_SERVICE_KEY);
        mTaken = bundle.getString(RECORD_SERVICE_TAKEN_KEY);

        try {
            startRecording();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mRecorder != null) {
            stopRecording();
        }

        super.onDestroy();
    }

    public void startRecording() throws IllegalStateException, IOException {
        mOutputFile = setFileNameAndPath();


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
        mRecorder.setOutputFile(mOutputFile.getAbsolutePath());
        mRecorder.setAudioChannels(1);
        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartingTimeMillis = System.currentTimeMillis();

            //startTimer();
            //startForeground(1, createNotification());
            Log.e(LOG_TAG, "Start recording");
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
            System.out.println(""+e);
        }
    }

    private File getAlbumStorageDir(String albumName) {
        /* File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName); */
        String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFilePath += "/TELCHEM/" + albumName;
        File file = new File(mFilePath);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created, or already existed");
        }
        return file;
    }

    private boolean hasMemoryCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public File setFileNameAndPath() {
        int count = 0;
        File file;

        do { // Redo Creating of file if file Already exist
            count++;

            mFileName = getString(R.string.default_file_name) + "_" + mTaken + "_"
                    + count + System.currentTimeMillis() + ".3gp";
            if (external && hasMemoryCard()) {
                mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                mFilePath += "/TELCHEM/";
            } else {
                mFilePath = getApplicationContext().getDir(directoryName,Context.MODE_PRIVATE).getAbsolutePath();
            }

            File f = new File(mFilePath);
            if (!f.mkdirs()) {
                Log.e(TAG, "Directory not created...count=" + count);
            }
            file = new File(mFilePath, mFileName);

        } while (file.exists() && !file.isDirectory());
        return file;
    }

    public void stopRecording() {
        mRecorder.stop();
        mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);
        mRecorder.release();
        Log.e(LOG_TAG, getString(R.string.toast_recording_finish) + " " + mFilePath);
        Log.e(LOG_TAG, "FILE:  " + mOutputFile.getAbsolutePath() + " size=" + mOutputFile.getTotalSpace());

        //remove notification
        if (mIncrementTimerTask != null) {
            mIncrementTimerTask.cancel();
            mIncrementTimerTask = null;
        }

        mRecorder = null;
        int idInserted = 0;
        try {
            // mDatabase = onTimerChangedListener.onHandleGetDB();
            mDatabase.open();
            // idInserted = mDatabase.addRecording(mFileName, mFilePath, mElapsedMillis, mServiceID);
            idInserted = mDatabase.addRecording(mFileName, mOutputFile.getAbsolutePath(), mElapsedMillis, mServiceID, mTaken);
            mDatabase.close();
        } catch (Exception e) {
            Log.e(LOG_TAG, "exception", e);
        }
        Log.e(LOG_TAG, "Stop recording, inserted ID:" + idInserted);
    }

    private void startTimer() {
        mTimer = new Timer();
        mIncrementTimerTask = new TimerTask() {
            @Override
            public void run() {
                mElapsedSeconds++;
                if (onTimerChangedListener != null)
                    onTimerChangedListener.onTimerChanged(mElapsedSeconds);
                NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mgr.notify(1, createNotification());
            }
        };
        mTimer.scheduleAtFixedRate(mIncrementTimerTask, 1000, 1000);
    }

    //TODO:  Notification
    private Notification createNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_mic_white_36dp)
                        .setContentTitle(getString(R.string.notification_recording))
                        .setContentText(mTimerFormat.format(mElapsedSeconds * 1000))
                        .setOngoing(true);

        mBuilder.setContentIntent(PendingIntent.getActivities(getApplicationContext(), 0,
                new Intent[]{new Intent(getApplicationContext(), ServiceReport_1.class)}, 0));

        return mBuilder.build();
    }

}
