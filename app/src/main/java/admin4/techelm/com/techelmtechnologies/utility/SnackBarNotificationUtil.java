package admin4.techelm.com.techelmtechnologies.utility;

import android.support.design.widget.Snackbar;
import android.view.View;

import admin4.techelm.com.techelmtechnologies.R;

/**
 * Created by admin 4 on 04/04/2017.
 * Just to eliminate Redudant Call and same implementation of Snackbar within the App
 */

public final class SnackBarNotificationUtil {

    private static SnackBarNotificationUtil mSnackBarNotificationUtil;
    private static Snackbar mSnackbar;
    // private static int mColor = Color.WHITE;

    SnackBarNotificationUtil () {}

    public static SnackBarNotificationUtil setSnackBar(View view, CharSequence message) {
        mSnackBarNotificationUtil = new SnackBarNotificationUtil();
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        return mSnackBarNotificationUtil;
    }

    public SnackBarNotificationUtil setColor(int color) {
        mSnackbar.setActionTextColor(color);
        return mSnackBarNotificationUtil;
    }

    public void show() {
        if (mSnackBarNotificationUtil != null && mSnackbar != null) {
            if (mSnackbar.isShown())
                mSnackbar.dismiss();

            if (!mSnackbar.isShown())
                mSnackbar.show();
        }
    }
}
