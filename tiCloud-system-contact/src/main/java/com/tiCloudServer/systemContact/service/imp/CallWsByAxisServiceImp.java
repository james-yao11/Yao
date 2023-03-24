package com.tiCloudServer.systemContact.service.imp;

import com.tiCloudServer.systemContact.service.CallWsByAxisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.message.SOAPHeaderElement;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.soap.SOAPException;
import java.util.Map;
import java.util.Set;

@Slf4j
@org.springframework.stereotype.Service
public class CallWsByAxisServiceImp implements CallWsByAxisService {

    /**
     * Axis动态调用wsdl
     *
     * @param wsdlUrl             服务地址
     * @param targetNamespace     服务命名空间
     * @param operationMethodName 接口方法名
     * @param bodyParams          body参数 <参数名，参数值>
     * @param headerParams        header参数 <参数名，参数值> （可为空）
     * @param loadPart            loadPart（设置header时才需要）
     * @return T
     */
    public <T> T invokeWebservice_axis(String wsdlUrl, String targetNamespace,
                                              String operationMethodName,
                                              Map<String, Object> bodyParams,
                                              Map<String, String> headerParams,
                                              String loadPart) {
        try {
            //引用远程的wsdl文件
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(wsdlUrl);
            //接口名
            call.setOperationName(new QName(targetNamespace, operationMethodName));
            //由于需要认证，故需要设置头部调用的密钥
            if (headerParams != null && headerParams.size() > 0) {
                SOAPHeaderElement soapHeaderElement = new SOAPHeaderElement(targetNamespace, loadPart);
                soapHeaderElement.setNamespaceURI(targetNamespace);
                Set<Map.Entry<String, String>> headParamSet = headerParams.entrySet();
                for (Map.Entry<String, String> map : headParamSet) {
                    try {
                        soapHeaderElement.addChildElement(map.getKey()).setValue(map.getValue());
                    } catch (SOAPException e) {
                        e.printStackTrace();
                    }
                }
                call.addHeader(soapHeaderElement);
            }
            Object[] params = new Object[bodyParams.size()];
            //参数
            Set<Map.Entry<String, Object>> entries = bodyParams.entrySet();
            int i = 0;
            for (Map.Entry<String, Object> map : entries) {
                call.addParameter(new QName(targetNamespace, map.getKey()), XMLType.XSD_STRING, ParameterMode.IN);
                params[i] = map.getValue();
                i++;
            }
            // 设置返回类型
            call.setReturnType(XMLType.XSD_STRING);
            //传递参数，并且调用方法
            String result = (String) call.invoke(params);
            return (T) result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
