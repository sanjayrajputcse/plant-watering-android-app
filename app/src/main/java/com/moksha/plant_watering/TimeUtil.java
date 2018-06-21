package com.moksha.plant_watering;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    public static String getDateString(Long epoch) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM hh:mma", Locale.ENGLISH);
        return sdf.format(new Date(epoch));
    }
}
