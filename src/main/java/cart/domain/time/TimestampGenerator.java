package cart.domain.time;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

public class TimestampGenerator {

    private TimestampGenerator() {
    }

    public static Timestamp getCurrentTime(Region region) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(region.getTimeZone()));
        return new Timestamp(calendar.getTimeInMillis());
    }
}
