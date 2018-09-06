package me.silloy.filter;

import lombok.extern.slf4j.Slf4j;
import me.silloy.util.IpUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * <p>Description: </p>
 *
 * @author SuShaohua
 * @date 2018/9/6 17:52
 * @verion 1.0
 */
@Aspect
@Slf4j
public class ControllerExecutionTime {

    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMappingMethod(){
    }


    @Around("requestMappingMethod()")
    public Object aroundControllerAdvice(ProceedingJoinPoint joinPoint)throws Throwable{
        return executeTimeLog(joinPoint);
    }
    private Object executeTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long begin = System.currentTimeMillis();
        Signature sig = joinPoint.getSignature();
        String className = sig.getDeclaringType().getName();
        String method = sig.getName();

        StringBuffer stringBuffer = new StringBuffer();
        Object[] argsArray = joinPoint.getArgs();
        String[] parameterNamesArray = ((CodeSignature)joinPoint.getStaticPart().getSignature()).getParameterNames();
        for (int i = 0; i < argsArray.length; i++) {
            stringBuffer.append(parameterNamesArray[i] + ":" + argsArray[i]);
            if(i < argsArray.length - 1) {
                stringBuffer.append(",");
            }
        }
        if(argsArray.length > 0){
            log.info("{} - begin, " + stringBuffer.toString(), method);
        }

        try {
            Object result = joinPoint.proceed(argsArray);
            if(result != null) {
                log.info("{} - end, result:" + result.toString(), method);
            }
            return result;
        } finally {
            long end = System.currentTimeMillis();

            float execTime = (end - begin);
            //logger.info(String.format("[%s#%s] Executed %f ms.", className, method, execTime));
            //class_name,method,exectime,server_host,client_host
            HttpServletRequest request = getRequest();
            log.info("{} - Executed {} ms, server host is {}, remote ip is {}", method, execTime, IpUtil.getLocalAddress(), IpUtil.getIp(request));
        }
    }

    private HttpServletRequest getRequest() {
        return (RequestContextHolder.getRequestAttributes() == null) ? null : ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
