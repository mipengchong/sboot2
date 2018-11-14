package com.zj.event;

import java.util.EventObject;

/**
 * Created with IntelliJ IDEA.
 * <p>Description: </p>
 *
 * @author SuShaohua
 * @date 2018/9/8 11:10
 * @verion 1.0
 */
public class MethodMonitorEvent extends EventObject {

    public long timestamp;
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MethodMonitorEvent(Object source) {
        super(source);
    }
}
