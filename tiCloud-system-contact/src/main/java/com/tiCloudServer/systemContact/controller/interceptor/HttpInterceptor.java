package com.tiCloudServer.systemContact.controller.interceptor;

import com.tiCloudServer.systemContact.entity.Action;
import com.tiCloudServer.systemContact.service.imp.LogServiceImp;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HttpInterceptor implements HandlerInterceptor, Interceptor {
    /**
     * 拦截Feign远程调用请求
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        //日志记录被远程调用的方法
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Action action =new Action();
        action.setDes("这是远程调用");
        action.setUsername("");
        action.setTime(simpleDateFormat.format(date));
        LogServiceImp logServiceImp = new LogServiceImp();
        logServiceImp.saveAction(action);
        return chain.proceed(builder.build());
    }
}
