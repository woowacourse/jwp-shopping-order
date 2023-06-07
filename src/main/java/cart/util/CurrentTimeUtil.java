package cart.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CurrentTimeUtil {

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String asString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT);

        return LocalDateTime.now().format(dateTimeFormatter);
    }
}
