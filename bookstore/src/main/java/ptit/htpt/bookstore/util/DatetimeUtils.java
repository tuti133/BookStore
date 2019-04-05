package ptit.htpt.bookstore.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatetimeUtils {
    public static String convert(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }
}
