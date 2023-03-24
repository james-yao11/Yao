package com.tiCloudServer.systemContact.aspect;

import com.alibaba.nacos.shaded.org.checkerframework.checker.nullness.qual.Nullable;
import com.tiCloudServer.systemContact.util.ResultJson;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;



@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object>{

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @SneakyThrows
    @Nullable
    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(body instanceof ResultJson){

        }
        return body;
    }
}
