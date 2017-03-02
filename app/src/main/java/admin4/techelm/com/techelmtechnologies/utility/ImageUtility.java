package admin4.techelm.com.techelmtechnologies.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin 4 on 22/02/2017.
 * Class for handling the UI Thread which is asynchronous
 * USAGE:
     * To save:
         new ImageSaver(context).
         setFileName("myImage.png").
         setDirectoryName("images").
         save(bitmap);
     * To load:
         Bitmap bitmap = new ImageSaver(context).
         setFileName("myImage.png").
         setDirectoryName("images").
         load();
 *      ImageSaver.setExternal(boolean)
 */

public class ImageUtility {

    /**
     * Created by Ilya Gazman on 3/6/2016.
     */

    private String directoryName = "images";
    private String fileName = "image.png";
    private Context context;
    private boolean external;

    public ImageUtility(Context context) {
        this.context = context;
    }

    public ImageUtility setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

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

    public void save(Bitmap bitmapImage) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile());
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
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
    }

    @NonNull
    private File createFile() {
        File directory;
        if (external) {
            directory = getAlbumStorageDir(directoryName);
        } else {
            directory = context.getDir(directoryName, Context.MODE_PRIVATE);
        }

        return new File(directory, fileName);
    }

    private File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("ImageUtility", "Directory not created");
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

    public Bitmap load() {
        FileInputStream inputStream = null;
        try {
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
        return null;
    }
}
