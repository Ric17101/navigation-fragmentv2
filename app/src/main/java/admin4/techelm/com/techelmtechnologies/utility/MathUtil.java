package admin4.techelm.com.techelmtechnologies.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by admin 4 on 22/06/2017.
 * For Math Operations, Define Here...
 */

public class MathUtil {

    public static double round2(Double val) {
        return new BigDecimal(val).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
