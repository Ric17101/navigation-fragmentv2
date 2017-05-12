package admin4.techelm.com.techelmtechnologies.utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.View;

/**
 * Created by admin 4 on 09/05/2017.
 * Progressbar when API Request invoked
 * On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
 *  for very easy animations. If available, use these APIs to fade-in
 *  the progress spinner.
 */

public class ProgressbarUtil {

    private static final String TAG = ProgressbarUtil.class.getSimpleName();
    // Instance Variable
    private View mProgressView = null;
    private View mMainView = null;
    private Resources res = null;

    public ProgressbarUtil newInstance(View progressView, View mainView, Resources resources) {
        this.mProgressView = progressView;
        this.mMainView = mainView;
        this.res = resources;
        return this;
    }

    // Make sure instance vars were initialized properly
    private boolean testInstanceVars() {
        return this.mProgressView != null || this.mMainView != null || this.res != null;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show) {
        if (testInstanceVars()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = this.res.getInteger(android.R.integer.config_shortAnimTime);

                mMainView.setVisibility(show ? View.GONE : View.VISIBLE);
                mMainView.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mMainView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mProgressView.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                this.mMainView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        } else {
            Log.e(TAG, "Instanciate class first.");
        }
    }
}
