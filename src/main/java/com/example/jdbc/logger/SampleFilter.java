package com.example.jdbc.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.FilterReply;

public class SampleFilter extends ch.qos.logback.core.filter.Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {

        if (event.getFormattedMessage().contains("Request") && event.getFormattedMessage().contains("Response")) {
            return FilterReply.ACCEPT;
        } else {
            return FilterReply.DENY;
        }
    }
}
