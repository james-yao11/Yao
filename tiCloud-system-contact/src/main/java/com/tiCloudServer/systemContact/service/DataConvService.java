package com.tiCloudServer.systemContact.service;

import com.tiCloudServer.systemContact.config.GlobalFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;
import java.util.Map;

@FeignClient(value = "DataConvService" ,configuration = GlobalFeignConfig.class)
public interface DataConvService {
    @GetMapping()
    Map<String,Object> DateConversionReplace(Map<String,Object> data) throws IOException;
    Map<String, String> DateConversionPartition(String UDI) throws IOException;
}
