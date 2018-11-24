package com.zj.web.event;

import java.lang.annotation.Annotation;

/**
 * Created with IntelliJ IDEA.
 * <p>Description: </p>
 *
 * @author SuShaohua
 * @date 2018/9/8 11:14
 * @verion 1.0
 */
public class AbstractMethodMonitorEventListener implements MethodMonitorEventListener {

    @Override
    public void onMethodBegin(MethodMonitorEvent event) {
        event.timestamp = System.currentTimeMillis();
    }

    @Override
    public void onMethodEnd(MethodMonitorEvent event) {
        long duration = System.currentTimeMillis() - event.timestamp;
        System.out.println("time elapsed: " + duration);
    }



    @Override
    public Class<?>[] value() {
        return new Class[0];
    }

    @Override
    public Class<?>[] classes() {
        return new Class[0];
    }

    @Override
    public String condition() {
        return null;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
