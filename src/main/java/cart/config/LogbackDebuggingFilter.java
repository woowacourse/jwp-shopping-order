package cart.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class LogbackDebuggingFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {
        String loggerName = event.getLoggerName();
        if (loggerName.contains("org.springframework") ||
                loggerName.contains("cart")) {
            return FilterReply.ACCEPT;
        }
        return FilterReply.DENY;
    }
}
