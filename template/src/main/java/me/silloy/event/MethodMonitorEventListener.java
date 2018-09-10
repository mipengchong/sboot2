package me.silloy.event;


import org.springframework.context.event.EventListener;

/**
 * Created with IntelliJ IDEA.
 * <p>Description: </p>
 *
 * @author SuShaohua
 * @date 2018/9/8 11:11
 * @verion 1.0
 */
public interface MethodMonitorEventListener extends EventListener {

    // 处理方法执行之前发布的事件
    public void onMethodBegin(MethodMonitorEvent event);


    // 处理方法结束时发布的事件
    public void onMethodEnd(MethodMonitorEvent event);
}
