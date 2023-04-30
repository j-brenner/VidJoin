package vidjoin;

import java.util.Calendar;

public class JaDateToStr {

    static String toYYyMMmDD(Calendar cal) {
        String mm = String.format("%02d", cal.get(Calendar.MONTH) + 1);
        String dd = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
        String yy = ("" + cal.get(Calendar.YEAR)).substring(2);
        System.out.println(yy + "y" + mm + "m" + dd);
        return yy + "y" + mm + "m" + dd;
    }

}

