package admin4.techelm.com.techelmtechnologies.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by admin 4 on 09/05/2017.
 * PERMISSION REQUEST if Android Phone API version/level is more than 6.0
         <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
         <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
         <uses-permission android:name="android.permission.RECORD_AUDIO" />
         <uses-permission android:name="android.permission.CAMERA" />

 * CALL this AS:
 *  PermissionUtil.verrifyReadStoragePermissions(this);
 *  or any public static method
 *  TODO: Should implement Override Method ?
 *      onRequestPermissionsResult()
 */

public class PermissionUtil {

    private static final String TAG = PermissionUtil.class.getSimpleName();

    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int WRITE_EXTERNAL_PERMISSION_REQUEST_CODE = 1;
    public static final int READ_EXTERNAL_PERMISSION_REQUEST_CODE = 2;
    public static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 3;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4;

    private static String[] PERMISSIONS_WRITE_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static String[] PERMISSIONS_READ_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private static String[] PERMISSIONS_AUDIO = {Manifest.permission.RECORD_AUDIO};
    private static String[] PERMISSIONS_CAMERA = {Manifest.permission.CAMERA};

    PermissionUtil() { }

    /**
     * SAMPLER
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity the activity from which permissions are checked
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_WRITE_STORAGE,
                    WRITE_EXTERNAL_PERMISSION_REQUEST_CODE
            );
        }
    }

    public static void verrifyReadStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, PERMISSIONS_READ_STORAGE[0]);
        if (isPermissionDenied(permission)) {
            processPermission(activity, PERMISSIONS_READ_STORAGE[0], PERMISSIONS_READ_STORAGE, READ_EXTERNAL_PERMISSION_REQUEST_CODE);
        }
    }

    public static void verrifyWriteStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, PERMISSIONS_WRITE_STORAGE[0]);
        if (isPermissionDenied(permission)) {
            processPermission(activity, PERMISSIONS_WRITE_STORAGE[0], PERMISSIONS_WRITE_STORAGE, WRITE_EXTERNAL_PERMISSION_REQUEST_CODE);
        }
    }

    public static void verrifyRecordAudioPermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, PERMISSIONS_AUDIO[0]);
        if (isPermissionDenied(permission)) {
            processPermission(activity, PERMISSIONS_AUDIO[0], PERMISSIONS_AUDIO, RECORD_AUDIO_PERMISSION_REQUEST_CODE);
        }
    }

    public static void verrifyCameraPermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, PERMISSIONS_CAMERA[0]);
        if (isPermissionDenied(permission)) {
            processPermission(activity, PERMISSIONS_CAMERA[0], PERMISSIONS_CAMERA, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private static boolean isPermissionDenied(int permission) {
        return permission != PackageManager.PERMISSION_GRANTED;
    }

    private static void processPermission(Activity activity, String permissionManifest, String[] permissions, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionManifest)) {
            Log.e(TAG, "shouldShowRequestPermissionRationale is invoked " + permissionManifest);
        } else {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
            Log.e(TAG, "requestPermissions is invoked " + permissionManifest);
        }
    }

    /**
     * SAMPLER
     * Request the permissions you need
        If your app doesn't already have the permission it needs, the app must call one of the requestPermissions() methods to request the appropriate permissions.
        Your app passes the permissions it wants, and also an integer request code that you specify to identify this permission request.
        This method functions asynchronously: it returns right away, and after the user responds to the dialog box,
        the system calls the app's callback method with the results, passing the same request code that the app passed to requestPermissions().
     * @param activity - you activity
     */
    public static void verifyShowRequestPrompt(Activity activity) {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.CAMERA)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            Log.e(TAG, "shouldShowRequestPermissionRationale is invoked");
        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    WRITE_EXTERNAL_PERMISSION_REQUEST_CODE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    /**
     * ABOVE CODES is not used
     * @param activity
     */

    public static void initPermissions(final Activity activity) {
        // The request code used in ActivityCompat.requestPermissions()
        // and returned in the Activity's onRequestPermissionsResult()
        // int PERMISSION_ALL = 1;
        final String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA};
        if(!hasPermissions(activity, PERMISSIONS)) {
            showMessageOKCancel(activity, "These permissions are mandatory for the application. Please allow access.",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!hasPermissions(activity, PERMISSIONS)) {
                                ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_EXTERNAL_STORAGE);
                            }
                        }
                    });
        }
    }

    public static boolean hasPermissions(final Context context, final String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                     return false;
                }
            }
        }
        return true;
    }

    private static void showMessageOKCancel(Context context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
