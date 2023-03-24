package com.tiCloudServer.systemContact.controller;

import com.tiCloudServer.systemContact.service.imp.CallWsByAxisServiceImp;
import com.tiCloudServer.systemContact.util.ResultJson;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

@CrossOrigin
@RestController
public class TpWsInterfaceController {

    @Resource
    CallWsByAxisServiceImp callWsByAxisServiceImp;
    /**
     * 对应调用第三方可能的webservice接口（webservice转restfull）
     * @param wsdlUrl             服务地址
     * @param targetNamespace     服务命名空间
     * @param operationMethodName 接口方法名
     * @param bodyParams          body参数 <参数名，参数值>
     * @param headerParams        header参数 <参数名，参数值> （可为空）
     * @param loadPart            loadPart（设置header时才需要）
     * @return resultJson
     */
    @PostMapping(value = "/test")
    public ResultJson tpHisWSInterfaceCall(String wsdlUrl, String targetNamespace,
                                      String operationMethodName,
                                      Map<String, Object> bodyParams,
                                      Map<String, String> headerParams,
                                      String loadPart){

        String result =  Objects.requireNonNull(callWsByAxisServiceImp.invokeWebservice_axis(wsdlUrl, targetNamespace, operationMethodName, bodyParams, headerParams, loadPart)).toString();
        ResultJson resultJson = new ResultJson();
        resultJson.setData(result);
        return resultJson;
    }
}
