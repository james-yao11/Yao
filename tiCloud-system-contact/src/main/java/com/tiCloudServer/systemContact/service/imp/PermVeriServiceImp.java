package com.tiCloudServer.systemContact.service.imp;

import com.tiCloudServer.systemContact.annotation.ServiceLog;
import com.tiCloudServer.systemContact.constant.DesEnum;
import com.tiCloudServer.systemContact.constant.StatusEnum;
import com.tiCloudServer.systemContact.service.PermVeriService;
import com.tiCloudServer.systemContact.util.CryptUtils;
import com.tiCloudServer.systemContact.util.JsonUtils;
import com.tiCloudServer.systemContact.util.ResultJson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class PermVeriServiceImp implements PermVeriService {

    /**
     *权限校验
     * @return boolean
     */
    @SneakyThrows
    @ServiceLog
    public boolean perVerification(ResultJson resultJson){
        boolean flag = true;
        CryptUtils cryptUtils = new CryptUtils();
        if(resultJson.getDataMap().get("appId")!=null
                &&resultJson.getDataMap().get("timestamp")!=null
                &&resultJson.getDataMap().get("sign")!=null){
            if(!cryptUtils.DecryptMD5((String) resultJson.getDataMap().get("appId"),
                    resultJson.getDataMap().get("timestamp").toString(),
                    (String) resultJson.getDataMap().get("sign"))){
                flag = false;
            }
        }
        return flag;
    }

    /**
     *解密
     * @return boolean
     */
    @SneakyThrows
    @ServiceLog
    public ResultJson perVerificationAESDc(String encrypt) {
        CryptUtils decrypt = new CryptUtils();
        ResultJson resultJson = decrypt.DecryptAES(encrypt);
        if(resultJson.getResult() == StatusEnum.FAIL_RESULT.toString()){
            return new ResultJson(DesEnum.DECRYPT_DATA.toString(), StatusEnum.DE_FAIL_STATUS_CODE.toString(),StatusEnum.FAIL_RESULT.toString());
        }

        Map<String,Object> data = JsonUtils.jsonToMap(resultJson.getData());

        return new ResultJson(DesEnum.DECRYPT_DATA.toString(), StatusEnum.DE_SUCC_STATUS_CODE.toString(),StatusEnum.SUCC_RESULT.toString(),data);
    }

}
