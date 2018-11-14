package com.zj.event;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * <p>Description: </p>
 *
 * @author SuShaohua
 * @date 2018/9/8 11:21
 * @verion 1.0
 */
public class MethodMonitorEventPublisher {

    private List<MethodMonitorEventListener> listeners = Lists.newArrayList();

    public void methodMonitor() throws Exception {
        MethodMonitorEvent eventObject = new MethodMonitorEvent(this);
        publishEvent("begin", eventObject);
        TimeUnit.SECONDS.sleep(5);
        publishEvent("end", eventObject);
    }

    private void publishEvent(String status, MethodMonitorEvent event) {
        // 避免在事件处理期间，监听器被移除，这里为了安全做一个复制操作
        List<MethodMonitorEventListener> copyListeners = new ArrayList<MethodMonitorEventListener>(listeners);

        for (MethodMonitorEventListener listener : copyListeners) {
            if ("begin".equals(status)) {
                listener.onMethodBegin(event);
            } else {
                listener.onMethodEnd(event);
            }
        }
    }


    public static void main(String[] args) throws Exception {
        MethodMonitorEventPublisher publisher = new MethodMonitorEventPublisher();
        publisher.addEventListener(new AbstractMethodMonitorEventListener());
        publisher.methodMonitor();

    }


    public void addEventListener( MethodMonitorEventListener listener) {}
    public void removeEventListener( MethodMonitorEventListener listener) {}
    public void removeAllListeners() {}
}
