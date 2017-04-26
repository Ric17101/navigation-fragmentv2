package admin4.techelm.com.techelmtechnologies.utility.dialog;

import android.app.DatePickerDialog;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by admin 4 on 26/04/2017.
 * Interface Holder is a just class with a static reference to pass it anywhere around...
 */

public class InterfaceDialogHolder {
    private static OpenDialog openDialog;
    private static MaterialDialog mDialog;
    private static DatePickerDialog dDialog;

    public static void set(OpenDialog dialog) {
        openDialog = dialog;
    }

    public static void set(MaterialDialog dialog) {
        mDialog = dialog;
    }

    public static void set(DatePickerDialog dialog) {
        dDialog = dialog;
    }

    public static OpenDialog get() {
        return openDialog;
    }
    public static MaterialDialog getmDialog() { return mDialog; }
    public static DatePickerDialog getDatePickerDialog() {
        return dDialog;
    }
}
