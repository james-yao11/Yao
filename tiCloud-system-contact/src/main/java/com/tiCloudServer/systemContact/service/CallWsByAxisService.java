package com.tiCloudServer.systemContact.service;

import com.tiCloudServer.systemContact.config.GlobalFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(value = "CallWsByAxisService" ,configuration = GlobalFeignConfig.class)
public interface CallWsByAxisService {
    @GetMapping()
    <T> T invokeWebservice_axis(String wsdlUrl, String targetNamespace,
                                String operationMethodName,
                                Map<String, Object> bodyParams,
                                Map<String, String> headerParams,
                                String loadPart);
}
