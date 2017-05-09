package admin4.techelm.com.techelmtechnologies.activity.login;

import admin4.techelm.com.techelmtechnologies.model.UserLoginWrapper;
import admin4.techelm.com.techelmtechnologies.task.LoginActivityAuthenticationTask;

/**
 * Created by admin 4 on 09/05/2017.
 */

public interface LoginAuthenticationTaskListener {
    /**
     * Callback invoked when clicked and onProgress
     */
    void onHandleSelection(int position, UserLoginWrapper user, int mode);
    void onHandleShowProgessLogin(boolean taskStatus);
    void onHandleAuthTask(LoginActivityAuthenticationTask mAuthTask);
    void onHandleEmailError(String emailError);
    void onHandlePasswordError(String passwordError);
    void onHandleSuccessLogin(UserLoginWrapper user);
    void onHandleShowDetails(String details);
}
