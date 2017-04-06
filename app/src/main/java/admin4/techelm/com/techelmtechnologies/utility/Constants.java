package admin4.techelm.com.techelmtechnologies.utility;

import admin4.techelm.com.techelmtechnologies.BuildConfig;

/**
 * Created by admin 4 on 03/04/2017.
 * FINAL CLASS
 * USE AS:
 * import static MyValues.*
     //...

     if(variable.equals(VALUE1)){
     //...
     }
 *
 */

    /*
        0 - New - Begin Task
        1 - unsigned - Can be Edited
        2 - Pending
        3 - Completed

        Calendar and Service Jobs TAB :
            EXAMPLE : user click 14/5/2017
            -all service jobs at 14/5/2017

        Unsigned Services Form TAB:
            - all service jobs where status = ‘unsigned’

        Service JOB Form TAB:
            - SHOW ALL SERVICE JOBS
      */
public final class Constants {

    public Constants() {} // Prevents instanciation of myself and my subclasses

    public static final String VERSION = BuildConfig.VERSION_NAME;
    public static String SERVICE_JOB_UPLOAD_URL =
            "http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/servicejob/";
    public static String SERVICE_JOB_DETAILS_URL =
            "http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/";

    // ACTION, action is being called in the MainActivity
    public static final int ACTION_BEGIN_JOB_SERVICE = 0;
    public static final int ACTION_EDIT_JOB_SERVICE = 1;
    public static final int ACTION_VIEW_DETAILS = 2;
    public static final int ACTION_ALREADY_COMPLETED = 3;
    public static final int ACTION_ALREADY_ON_PROCESS = 4; // This already being called as Continue whenever service

    // STATUS of Service Jobs
    public static final String SERVICE_JOB_NEW = "0";
    public static final String SERVICE_JOB_UNSIGNED = "1";
    public static final String SERVICE_JOB_PENDING = "2";
    public static final String SERVICE_JOB_COMPLETED = "3";
    public static final String SERVICE_JOB_ON_PROCESS = "4";

    // Intent putExtra KEY, used to decode data passed to ServiceJobViewPagerActivity
    public static final String SERVICE_JOB_ID_KEY = "SERVICE_ID";
    public static final String SERVICE_JOB_SERVICE_KEY = "SERVICE_JOB";
    public static final String SERVICE_JOB_PREVIOUS_STATUS_KEY = "SERVICE_JOB_PREVIOUS_STATUS";
    public static final String SERVICE_JOB_TAKEN_KEY = "TAKEN";

}
