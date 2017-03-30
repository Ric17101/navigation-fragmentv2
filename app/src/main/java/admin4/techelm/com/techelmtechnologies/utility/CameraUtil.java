package admin4.techelm.com.techelmtechnologies.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import admin4.techelm.com.techelmtechnologies.R;

/**
 * Used by
 *  ServiceReport_FRGMT_BEFORE Class
 *  PartReplacement_FRGMT_2 Class
 */
public class CameraUtil {
    
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Activity activity;
    private Bitmap cameraImageBitmap;
    private FileUtility mFileUtil;
    private String mImageName = "CAPTURE";

    public CameraUtil(Activity act, String imageName) {
        activity = act;
        mImageName = imageName;
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("upload", "Directory "+ albumName +" not created");
        }
        return file;
    }


    public Bitmap loadBitmap() {
        return this.cameraImageBitmap;
    }

    public String getFilePath() {
        // return this.mFileUtil.getFile().getPath();
        return this.mFileUtil.getFile().toString();
    }

    public String getFileName() {
        return this.mFileUtil.getFileName();
    }


    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        stream.close();
    }

    public boolean addJpgUploadToGallery(Bitmap signature, String folder) {
        mFileUtil = new FileUtility(activity.getApplicationContext());
        mFileUtil.setDirectoryName(folder)
                .setExternal(true)
                .setFileName(String.format(mImageName +"_%d.jpg", System.currentTimeMillis()));

        boolean result = mFileUtil.saveImage(signature);
        Log.e("upload", "Directory :" + mFileUtil.getFileName());

        this.cameraImageBitmap = mFileUtil.loadImage();

        scanMediaFile(mFileUtil.getFile());
        return result;
    }

    public boolean addJpgSignatureToGallery_v1(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("upload"), String.format("CAPTURE_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);
    }

    public boolean addSvgSignatureToGallery(String signatureSvg) {
        boolean result = false;
        try {
            File svgFile = new File(getAlbumStorageDir("upload"), String.format(mImageName + "_%d.svg", System.currentTimeMillis()));
            verifyStoragePermissions(this.activity);
            OutputStream stream = new FileOutputStream(svgFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(signatureSvg);
            writer.close();
            stream.flush();
            stream.close();
            scanMediaFile(svgFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean deleteFile(String filePath) {
        File fDelete = new File(filePath);
        boolean deleted = false;
        if (fDelete.exists()) {
            if (fDelete.delete()) {
                System.out.println("file Deleted :" + filePath);
                deleted = true;
            } else {
                System.out.println("file not Deleted :" + filePath);
            }
        }
        return deleted;
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity the activity from which permissions are checked
     */
    private static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
