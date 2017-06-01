package admin4.techelm.com.techelmtechnologies.utility;

import java.util.ArrayList;

import admin4.techelm.com.techelmtechnologies.BuildConfig;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_ASRWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_CFWrapper;
import admin4.techelm.com.techelmtechnologies.model.servicejob.ServiceJobComplaint_MobileWrapper;

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

public final class Constants {
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
    public Constants() {} // Prevents instantiation of myself and my subclasses

    public static final String VERSION = BuildConfig.VERSION_NAME;
    public static final String HTTP_AUTHENTICATION_ACCESS = "firstcom:opendemolink88"; // REMOVE THIS if web server has no Authentication
    public static final String NEW_DOMAIN_URL = "http://techelm2012.firstcomdemolinks.com/api/ci-rest-api-techelm/";
    public static final String FIRSTCM_DOMAIN_URL = "http://techelm2012.firstcomdemolinks.com/system/backend/web/"; ///system/backend/web/projectjob/drawings/projectjobid70/Reverse_White_Logo.jpg
    public static final String DOMAIN_URL = "http://enercon714.firstcomdemolinks.com/sampleREST/ci-rest-api-techelm/index.php/";

    public static final String LOGIN_URL = NEW_DOMAIN_URL + "auth/user?user=%s&password=%s";

    public static final String SERVICE_JOB_URL = DOMAIN_URL + "servicejob/";
    public static final String NEW_SERVICE_JOB_URL = NEW_DOMAIN_URL + "servicejob/";
    public static final String SERVICE_JOB_DETAILS_URL = NEW_SERVICE_JOB_URL + "detail/";
    // public static final String SERVICE_JOB_LIST_URL = NEW_SERVICE_JOB_URL + "get_date_services/";
    public static final String SERVICE_JOB_LIST_URL = NEW_SERVICE_JOB_URL + "get_date_Services_by_employee_id?employee_id=%d&date=%s";
    public static final String SERVICE_JOB_NEW_PARTS_UPLOAD_URL = NEW_SERVICE_JOB_URL + "servicejob_new_parts_json";
    public static final String SERVICE_JOB_UPLOAD_CAPTURE_URL = NEW_SERVICE_JOB_URL + "servicejob_upload_capture";
    public static final String SERVICE_JOB_UPLOAD_RECORDING_URL = NEW_SERVICE_JOB_URL + "servicejob_upload_recording";
    public static final String SERVICE_JOB_UPLOAD_SIGNATURE_URL = NEW_SERVICE_JOB_URL + "servicejob_upload_signature";
    public static final String SERVICE_JOB_SAVE_START_DATE_URL = NEW_SERVICE_JOB_URL + "save_start_date";
    public static final String SERVICE_JOB_SAVE_CONTINUE_START_DATE_URL = NEW_SERVICE_JOB_URL + "save_continue_start_date";
    public static final String SERVICE_JOB_GET_PARTS_RATES_URL = NEW_SERVICE_JOB_URL + "get_part_replacement_rates";
    public static final String SERVICE_JOB_SAVE_REVERT_STATUS_URL = NEW_SERVICE_JOB_URL + "save_revert_status";
    //public static final String SERVICE_JOB_UNSIGNED_LIST_URL = NEW_SERVICE_JOB_URL + "get_date_services_unsigend_services/";
    public static final String SERVICE_JOB_UNSIGNED_LIST_URL = NEW_SERVICE_JOB_URL + "get_date_services_unsigend_services_by_employee_id?employee_id=%d";
    public static final String SERVICE_JOB_VIEW_DETAIL_URL = NEW_SERVICE_JOB_URL + "view_details?servicejob_id=";
    // public static final String SERVICE_JOB_BY_MONTH_URL = NEW_SERVICE_JOB_URL + "get_date_services_by_month";
    public static final String SERVICE_JOB_BY_MONTH_URL = NEW_SERVICE_JOB_URL + "get_date_services_by_month_and_by_employee_id";
    public static final String SERVICE_JOB_SEND_EMAIL_URL = NEW_SERVICE_JOB_URL + "send_email";
    public static final String SERVICE_JOB_POST_ALL_COMPLAINTS_URL = NEW_SERVICE_JOB_URL + "get_all_servicejob_complaint_to_json_by_sjid";

    public static final String PROJECT_JOB_URL = DOMAIN_URL + "projectjob/";
    public static final String NEW_PROJECT_JOB_URL = NEW_DOMAIN_URL + "projectjob/";
    public static final String PROJECT_JOB_PISS_DETAILS_URL = PROJECT_JOB_URL + "get_piss_details?id=";
    // public static final String PROJECT_JOB_LIST_URL = NEW_PROJECT_JOB_URL + "get_all_services/";
    public static final String PROJECT_JOB_LIST_URL = NEW_PROJECT_JOB_URL + "get_all_services_by_employee_id?employee_id=%d";
    public static final String PROJECT_JOB_PISS_TASK_LIST_URL = NEW_PROJECT_JOB_URL + "get_piss_tasks?projectjob_id=";
    public static final String PROJECT_JOB_SAVE_PISS_TASK_FORM_URL = NEW_PROJECT_JOB_URL + "projectjob_piss_tasks_save";
    public static final String PROJECT_JOB_SAVE_IPI_TASK_FORM_A_URL = NEW_PROJECT_JOB_URL + "projectjob_ipi_tasks_save";
    public static final String PROJECT_JOB_SAVE_IPI_TASK_FORM_B_URL = NEW_PROJECT_JOB_URL + "projectjob_ipi_correctiveActions_save";
    public static final String PROJECT_JOB_SAVE_IPI_TASK_FORM_C_URL = NEW_PROJECT_JOB_URL + "projectjob_upload_signature";
    public static final String PROJECT_JOB_PISS_TASK_DETAILS_URL = PROJECT_JOB_URL + "get_piss_tasks_detail?id=";
    public static final String PROJECT_JOB_PISS_TASK_UPLOAD_DRAWING_URL = NEW_PROJECT_JOB_URL + "projectjob_piss_tasks_drawing";
    public static final String PROJECT_JOB_FORM_TASK_APPEND = "&form_type=";
    // public static final String PROJECT_JOB_IPI_TASK_LIST_URL = PROJECT_JOB_URL + "get_ipi_tasks?projectjob_ipi_pw_id=";
    public static final String PROJECT_JOB_IPI_TASK_LIST_URL = NEW_PROJECT_JOB_URL + "get_ipi_tasks?projectjob_id=%d&form_type=%s";
    public static final String PROJECT_JOB_IPI_TASK_FINAL_LIST_URL = NEW_PROJECT_JOB_URL + "get_ipi_correctiveAction?projectjob_id=%d&form_type=%s";

	public static final String NEW_TOOLBOX_MEETING_URL = NEW_DOMAIN_URL + "toolboxmeeting/";
    public static final String TOOLBOXMEETING_IMAGE_UPLOAD_URL = NEW_TOOLBOX_MEETING_URL + "toolboxmeeeting_save_image";
    public static final String TOOLBOXMEETING_MEETING_DETAILS_UPLOAD_URL = NEW_TOOLBOX_MEETING_URL + "toolboxmeeeting_save";
    // public static final String TOOLBOXMEETING_ATTENDEES_UPLOAD_URL = NEW_TOOLBOX_MEETING_URL + "toolboxmeeeting_save_attendees/";
    public static final String TOOLBOXMEETING_ATTENDEES_UPLOAD_URL = NEW_TOOLBOX_MEETING_URL + "toolboxmeeeting_save_attendee/";
    public static final String TOOLBOXMEETING_ATTENDEES_DELETE_URL = NEW_TOOLBOX_MEETING_URL + "toolboxmeeeting_delete_attendee/";

    public static final String LIST_DELIM = ":-:";

    /**** NAVIGATION DRAWER TO SHOW ON MAIN ACTIVITY ****/
    public static final int NAVIGATION_DRAWER_SELECTED_SERVICEJOB = 0;
    public static final int NAVIGATION_DRAWER_SELECTED_PROJECTJOB = 1;
    public static final int NAVIGATION_DRAWER_SELECTED_TOOLBOX = 5;

    /**** SECTION A ****/
    // ACTION, action is being called in the MainActivity
    public static final int ACTION_BEGIN = 0;
    public static final int ACTION_EDIT = 1;
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
    /** Type is int. */
    public static final String LANDING_PAGE_ACTIVE_KEY = "LANDING_PAGE";
    /** Type is int. */
    public static final String SERVICE_JOB_ID_KEY = "SERVICE_ID";
    /** Type is Object ServiceJobWrapper. */
    public static final String SERVICE_JOB_SERVICE_KEY = "SERVICE_JOB";
    /** Type is Object ServiceJobWrapper. */
    public static final String SERVICE_JOB_EQUIP_RATES_KEY = "SERVICE_JOB_EQUIP_REATES";
    /** Type is String. */
    public static final String SERVICE_JOB_PREVIOUS_STATUS_KEY = "SERVICE_JOB_PREVIOUS_STATUS";
    /** Type is String. */
    public static final String SERVICE_JOB_TAKEN_KEY = "TAKEN";
    /** Type is Object ServiceJobNewReplacementPartsRatesWrapper. */
    public static final String SERVICE_JOB_PARTS_REPLACEMENT_LIST_KEY = "REPLACEMENT_LIST";
    /** Type is Object ArrayList<ServiceJobComplaint_MobileWrapper>. */
    public static final String SERVICE_JOB_COMPLAINTS_MOBILE_LIST_KEY = "SJ_COMPLAINTS_MOBILE_LIST";
    /** Type is Object ArrayList<ServiceJobComplaint_CFWrapper>. */
    public static final String SERVICE_JOB_COMPLAINTS_CF_LIST_KEY = "SJ_COMPLAINTS_CF_LIST";
    /** Type is Object ArrayList<ServiceJobComplaint_ASRWrapper>. */
    public static final String SERVICE_JOB_COMPLAINTS_ASR_LIST_KEY = "SJ_COMPLAINTS_ASR_LIST";
    /** Type is String. */
    public static final String PROJECT_JOB_FORM_TYPE_KEY = "TYPE_OF_FORM";
    /** Type is ProjectJobWrapper Object. */
    public static final String PROJECT_JOB_KEY = "PROJECT_JOB";
    /** Type is PISSTaskWrapper Object. */
    public static final String PROJECT_JOB_PISS_TASK_KEY = "PROJECT_JOB_TASK";
    /** Type is IPIFinalTaskWrapper Object. */
    public static final String PROJECT_JOB_IPI_FINAL_TASK_KEY = "PROJECT_JOB_IPI_FINAL_TASK";
    /** Type is View Object. */
    public static final String PROJECT_JOB_FORM_DATE_FORM_KEY = "SHOW_ANOTHER_DIALOG";
    /** Type is IPIFinalTaskWrapper Object. */
    public static final String TOOLBOX_MEETING_KEY = "TOOLBOX_MEETING";

    /**** SECTION B ****/
    // STATUS of Project Jobs, the Task Button
    public static final String PROJECT_JOB_NEW = "0";
    public static final String PROJECT_JOB_PENDING = "1";
    public static final String PROJECT_JOB_COMPLETED = "2";
    public static final String PROJECT_JOB_ON_PROCESS = "3";

    // ACTION PROJECT JOB
    public static final int ACTION_CHOOSE_FORM = 10; // PROJECT JOB, Choosing the form
    public static final int ACTION_START_DRAWING = 11;
    public static final int ACTION_START_TASK = 12;
    public static final int ACTION_CONTINUE_TASK = 13;
    public static final int ACTION_VIEW_TASK = 14;
    public static final int ACTION_START_IPI_CORRECTIVE_ACTION_FORM = 15;
    public static final int ACTION_START_IPI_CONFIRMATION_DATE_FORM = 16;
    public static final int ACTION_START_IPI_CORRECTIVE_ACTION_TASK_FORM = 17;
    public static final int ACTION_TOOLBOX_MEETING = 18;
    public static final int ACTION_TASK_START_DRAWING = 19;

    public static final String PROJECT_JOB_CHOOSE_FORM = "10";
    public static final String PROJECT_JOB_START_DRAWING = "11"; // This is for showB2B3FormDialog() @ ProjectJobViewPagerActivity
    public static final String PROJECT_JOB_START_TASK = "12";
    public static final String PROJECT_JOB_CONTINUE_TASK = "13";
    public static final String PROJECT_JOB_CORRECTIVE_ACTION_FORM = "15";
    public static final String PROJECT_JOB_CONFIRMATION_DATE_FORM = "16";
    public static final String PROJECT_JOB_IPI_CORRECTIVE_ACTION_TASK_FORM = "17";
    public static final String PROJECT_JOB_TASK_START_DRAWING = "18";

    // TYPE OF FORM for SECTION B
    public static final int PROJECT_JOB_FORM_B1 = 1;
    public static final int PROJECT_JOB_FORM_B2 = 2;
    public static final int PROJECT_JOB_FORM_B3 = 3;
    public static final String PROJECT_JOB_FORM_EPS = "EPS";
    public static final String PROJECT_JOB_FORM_PW = "PW";

    // FRAGMENT POSITION for Navigation between Fragments especially in the Forms
    public static final int PROJECT_JOB_FRAGMENT_POSITION_1 = 1;
    public static final int PROJECT_JOB_FRAGMENT_POSITION_2 = 2;
    public static final int PROJECT_JOB_FRAGMENT_POSITION_3 = 3;

    // FRAGMENT BACKSTACK
    public static final String FRAGMENT_BACK_STACK = "DRAWING_CANVAS";
}
