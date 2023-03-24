package com.tiCloudServer.systemContact.aspect;

import com.tiCloudServer.systemContact.annotation.ControllerLog;
import com.tiCloudServer.systemContact.annotation.ServiceLog;
import com.tiCloudServer.systemContact.entity.Action;
import com.tiCloudServer.systemContact.service.imp.LogServiceImp;
import com.tiCloudServer.systemContact.util.JsonUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

@Aspect
@Component
public class LogAspect {

    @Autowired
    LogServiceImp logServiceImp;

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    //Service层切点
    @Pointcut("@annotation(com.tiCloudServer.systemContact.annotation.ServiceLog)")
    public void serviceAspect(){
    }

    //Controller层切点
    @Pointcut("@annotation(com.tiCloudServer.systemContact.annotation.ControllerLog)")
    public void controllerAspect(){
    }

    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) throws ClassNotFoundException {

        try {
            System.out.println("==============前置通知开始==============");
            System.out.println("请求方法" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName()));
            System.out.println("方法描述：" + getControllerMethodDescription(joinPoint));
            System.out.println("请求方："+ "");
            //*========数据库日志=========*//
            //保存数据库
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Action action = new Action();
            action.setDes(getControllerMethodDescription(joinPoint));
            action.setType("1");
            action.setUsername("HIS");
            action.setTime(simpleDateFormat.format(date));
            //保存到数据库
            logServiceImp.saveAction(action);
        }catch (Exception e){
            //记录本地异常日志
            logger.error("==前置通知异常==");
            logger.error("异常信息：{}",e.getMessage());
            System.out.println(e);
            throw e;
        }
    }

    @AfterThrowing(pointcut = "serviceAspect()",throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint,Throwable e) throws ClassNotFoundException {
        //获取请求方法的参数并序列化为JSON格式字符串
        StringBuilder params = new StringBuilder();
        if (joinPoint.getArgs()!=null&&joinPoint.getArgs().length>0){
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                params.append(JsonUtils.objectTOJSONString(joinPoint.getArgs()[i])).append(";");
            }
        }
        try{
            System.out.println("异常代码:" + e.getClass().getName());
            System.out.println("异常信息:" + e.getMessage());
            System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            System.out.println("方法描述:" + getServiceMethodDescription(joinPoint));
            System.out.println("请求方:" + "");
            System.out.println("请求参数:" + params);
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Action action = new Action();
            action.setDes(getServiceMethodDescription(joinPoint));
            action.setType("1");
            action.setUsername("HIS");
            action.setTime(simpleDateFormat.format(date));
            //保存到数据库
            logServiceImp.saveAction(action);
        }catch (Exception ex){
            //记录本地异常日志
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", ex.getMessage());
            System.out.println(ex);
            throw ex;
        }
    }

    private String getServiceMethodDescription(JoinPoint joinPoint) throws ClassNotFoundException {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class<?> targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method:methods) {
            if (method.getName().equals(methodName)){
                Class<?>[] clazzs = method.getParameterTypes();
                if (clazzs.length==arguments.length){
                    description = method.getAnnotation(ServiceLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

    private String getControllerMethodDescription(JoinPoint joinPoint) throws ClassNotFoundException {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class<?> targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method:methods) {
            if (method.getName().equals(methodName)){
                Class<?>[] clazzs = method.getParameterTypes();
                if (clazzs.length==arguments.length){
                    description = method.getAnnotation(ControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

}
