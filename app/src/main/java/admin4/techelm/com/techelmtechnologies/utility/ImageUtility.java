package admin4.techelm.com.techelmtechnologies.utility;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin 4 on 22/02/2017.
 * This location works best if you want the created images to be shared
 *  between applications and persist after your app has been uninstalled.
 * USAGE:
     * To save:
         new ImageSaver(mContext).
         setFileName("myImage.png").
         setDirectoryName("images").
         save(bitmap);
     * To load:
         Bitmap bitmap = new ImageSaver(mContext).
         setFileName("myImage.png").
         setDirectoryName("images").
         load();
 *      ImageSaver.setExternal(boolean)
 *      CREDIT TO: Ilya Gazman on 3/6/2016.
 */

public class ImageUtility {

    private static final String TAG = ImageUtility.class.getSimpleName();

    private String directoryName = "images";
    private String directoryPath = "";
    private String fileName = "image.png";
    private File mImageFile;
    private Context mContext;
    private boolean external = false;

    public ImageUtility(Context context) {
        this.mContext = context;
    }

    public ImageUtility setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFileName() { return this.fileName; }
    public String getDirectoryPath() {return this.getDirectoryPath(); }

    public ImageUtility setExternal(boolean external) {
        this.external = external;
        return this;
    }

    public ImageUtility setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
        return this;
    }

    public boolean deleteFile() {
        File file = createFile();
        return file.delete();
    }

    public boolean save(Bitmap bitmapImage) {
        FileOutputStream fileOutputStream = null;

        Log.e(TAG, "Directory is isExternalStorageReadable: " + isExternalStorageReadable());
        Log.e(TAG, "Directory is isExternalStorageWritable: " + isExternalStorageWritable());
        
        try {
            this.mImageFile = createFile();
            fileOutputStream = new FileOutputStream(this.mImageFile);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

            boolean isReadable = false;
            if (this.mImageFile.canRead()) isReadable = true;

            Log.e(TAG, "Directory has been created created " + mImageFile.getName());
            Log.e(TAG, "is Readable: " + isReadable);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Test if Phone Has Mem Card
     * @return
     *  TRUE - has mem card
     *  FALSE - none
     */
    private boolean hasMemoryCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    @NonNull
    private File createFile() {
        File directory;
        if (external && hasMemoryCard()) {
            directory = getAlbumStorageDir(directoryName);
        } else {
            directory = this.mContext.getDir(directoryName, Context.MODE_PRIVATE);
        }
        this.directoryPath = directory.getAbsolutePath();
        return new File(directory, fileName);
    }

    private File getAlbumStorageDir(String albumName) {
        /*File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);*/
        String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        // String mFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        mFilePath += "/TELCHEM/" + albumName;
        File file = new File(mFilePath);

        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        } else {
            Log.e(TAG, "Directory has been created created " + mFilePath);
        }
        return file;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    /**
     * Load the Filebeing captured
     * TODO: This should be saved on the DB then load as File from DBQuery
     * @return
     */
    public File loadImageFile() {
        return ((this.mImageFile == null) ? null : this.mImageFile);
    }

    public Bitmap load() {
        FileInputStream inputStream = null;
        try {
            Log.e(TAG, "TRYDirectory has been created");
            inputStream = new FileInputStream(createFile());
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "TRYDirectory has been created");
        return null;
    }

    /*********************** Resize Image For Background of the Device *************************/
    /**
     * This is used for background image for each Actvity as per Design Scheme
     * @param imageID
     * @return
     */
    public Drawable ResizeImage(int imageID) {
        // Get device dimensions
        Display display = ((Activity) this.mContext).getWindowManager().getDefaultDisplay();
        double deviceWidth = display.getWidth();

        BitmapDrawable bd = (BitmapDrawable) ((Activity) this.mContext).getResources().getDrawable(
                imageID);
        double imageHeight = bd.getBitmap().getHeight();
        double imageWidth = bd.getBitmap().getWidth();

        double ratio = deviceWidth / imageWidth;
        int newImageHeight = (int) (imageHeight * ratio);

        Bitmap bMap = BitmapFactory.decodeResource(((Activity) this.mContext).getResources(), imageID);
        Drawable drawable = new BitmapDrawable(((Activity) this.mContext).getResources(),
                getResizedBitmap(bMap, newImageHeight, (int) deviceWidth));

        return drawable;
    }
    /************************ Resize Bitmap *********************************/
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    // TODO: Image Resizing Before upload/save to Gallery Directory
    public File rescaleImageFile(File file) {
        try {
            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 100; // 75

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    /************* IMAGE UTIL USED IN FRAGMENT FORMS *****************/
    public static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static Bitmap convertFileToBitmap(File fileImage) {
        Bitmap bitmap = BitmapFactory.decodeFile(fileImage.getAbsolutePath());
        return bitmap;
    }

    public static Bitmap loadBitmapFromile(File file) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
