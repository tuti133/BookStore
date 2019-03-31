package ptit.htpt.bookstore.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatetimeUtils {
    public static Long convert(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return Long.parseLong(sdf.format(date));
    }
}
