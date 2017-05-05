package admin4.techelm.com.techelmtechnologies.utility;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.ViewGroup;
import android.widget.ImageButton;


import admin4.techelm.com.techelmtechnologies.R;

/**
 * Created by admin 4 on 05/05/2017.
 * This is called and used at  ProjectJobLastFormFragment and SigningOff_FRGMT_4 Fragment Classes
 */

public class SignatureImageButtonUtil {
    public static void setClearImageSignature(Resources resource, ImageButton signatureImageButton) {
        BitmapDrawable ob = new BitmapDrawable(resource, String.valueOf(resource.getDrawable(R.drawable.composea)));
        signatureImageButton.setBackground(ob);
        // signatureImageButton.setBackgroundDrawable(ob);
    }

    public static void setDrawableImageSignature(Resources resource, ImageButton signatureImageButton, Bitmap drawableImageSignature) {
        BitmapDrawable ob = new BitmapDrawable(resource, drawableImageSignature);
        signatureImageButton.setBackground(ob);
        // signatureImageButton.setBackgroundDrawable(ob);

        // Set to wrap content signature
        ViewGroup.LayoutParams params = signatureImageButton.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        signatureImageButton.setLayoutParams(params);
    }
}
