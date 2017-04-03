package admin4.techelm.com.techelmtechnologies.utility;

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
        4 - Incomplete - Continue

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

    // ACTION
    public static final int ACTION_BEGIN_JOB_SERVICE = 0;
    public static final int ACTION_EDIT_JOB_SERVICE = 1;
    public static final int ACTION_VIEW_DETAILS = 2;
    public static final int ACTION_ALREADY_COMPLETED = 3;
     public static final int ACTION_PENDING = 5;

    // STATUS of Service Jobs
    public static final String SERVICE_JOB_NEW = "0";
    public static final String SERVICE_JOB_UNSIGNED = "1";
    public static final String SERVICE_JOB_PENDING = "2";
    public static final String SERVICE_JOB_COMPLETED = "3";
    public static final String SERVICE_JOB_INCOMPLETE = "4";
    // public static final String SERVICE_JOB_ON_PROCESS = "5";

}
