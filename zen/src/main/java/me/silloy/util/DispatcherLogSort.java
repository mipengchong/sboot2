package me.silloy.util;


import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.concurrent.atomic.LongAdder;

/**
 * Created with IntelliJ IDEA.
 * <p>Description: </p>
 *
 * @author SuShaohua
 * @date 2018/9/6 17:28
 * @verion 1.0
 */
public class DispatcherLogSort extends ClassicConverter {

    private static LongAdder adder = new LongAdder();

    @Override
    public String convert(ILoggingEvent loggingEvent) {
        adder.increment();
        return adder.longValue() + "";
    }
}
