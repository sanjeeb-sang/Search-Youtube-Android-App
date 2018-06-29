package sangraula.sanjeeb.wissionapp.utils.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sanjeeb on 8/27/2017.
 */

public class DateTimeUtils {

    public static String getCurrentTime () {

        Date date = Calendar.getInstance().getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm");

        return dateFormat.format(date);

    }

}
