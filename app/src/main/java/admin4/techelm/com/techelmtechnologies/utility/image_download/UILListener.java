package admin4.techelm.com.techelmtechnologies.utility.image_download;

import android.graphics.Bitmap;

/**
 * Created by admin 4 on 27/04/2017.
 */

public interface UILListener {
    void OnHandleError(String message);
    void OnHandleStartDownload(String message);
    void OnHandleLoadingCompleted(String imageURI, Bitmap imageLoaded);
}
