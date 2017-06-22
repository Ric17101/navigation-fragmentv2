package admin4.techelm.com.techelmtechnologies.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin 4 on 22/06/2017.
 * Date Utility uses Static method
 */

public class DateUtil {

    /**
     * Reformat "dd/mm/yyyy" to "yyyy-MM-dd"
     * @param date - date to reformat
     * @return
     */
    public static String formatDate(String date) {
        if (date.isEmpty() || date.equals(""))
            return date;

        Date yourDate;
        SimpleDateFormat formatNeeded = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat yourDateFormatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            yourDate = yourDateFormatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }

        return formatNeeded.format(yourDate);
    }
}
