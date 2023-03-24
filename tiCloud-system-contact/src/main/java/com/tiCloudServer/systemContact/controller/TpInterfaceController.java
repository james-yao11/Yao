package com.tiCloudServer.systemContact.controller;

import com.tiCloudServer.systemContact.annotation.ControllerLog;
import com.tiCloudServer.systemContact.constant.DesEnum;
import com.tiCloudServer.systemContact.constant.StatusEnum;
import com.tiCloudServer.systemContact.service.imp.DataConvServiceImp;
import com.tiCloudServer.systemContact.service.imp.PermVeriServiceImp;
import com.tiCloudServer.systemContact.service.imp.PersInfoServiceImp;
import com.tiCloudServer.systemContact.util.ResultJson;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@CrossOrigin
@RestController
public class TpInterfaceController {

        @Resource
        public PermVeriServiceImp permVeriServiceImp;

        @Resource
        public PersInfoServiceImp persInfoServiceImp;

        @Resource
        DataConvServiceImp dataConvServiceImp;

        @SneakyThrows
        @ControllerLog(description = "请求开始调用第三方接口")
        @PostMapping("/third/**")
        public ResultJson tpInterfaceCall(@RequestParam String encrypt_data){
            ResultJson result = new ResultJson();
            //解密并校验权限
            if(Objects.equals(doPermVeri(encrypt_data).getResult(), StatusEnum.FAIL_RESULT.toString())){
                result.setMessage(DesEnum.NO_PERM.toString());
                result.setCode(StatusEnum.DE_FAIL_STATUS_CODE.toString());
                result.setResult(StatusEnum.FAIL_RESULT.toString());
                return result;
            }
            Map<String, Object> data = doPermVeri(encrypt_data).getDataMap();
            //格式转换
            ResultJson resDataInfo = doDataConv(data);
            if(Objects.equals(resDataInfo.getResult(), StatusEnum.FAIL_RESULT.toString())){
                result.setMessage(DesEnum.DATA_FM_FAIL.toString());
                result.setCode(StatusEnum.DE_FAIL_STATUS_CODE.toString());
                result.setResult(StatusEnum.FAIL_RESULT.toString());
                return result;
            }
            //持久化数据
            CompletableFuture<ResultJson> resPiInfo = doPerInfo(resDataInfo.getDataMap(), "HIS");
            if (Objects.equals(resPiInfo.join().getResult(), StatusEnum.FAIL_RESULT.toString())) {
                result.setMessage(DesEnum.DATA_FM_FAIL.toString());
                result.setCode(StatusEnum.DE_FAIL_STATUS_CODE.toString());
                result.setResult(StatusEnum.FAIL_RESULT.toString());
                return result;
            }
            result.setDataMap(resDataInfo.getDataMap());
            result.setMessage(DesEnum.THIRD_CALL_SUC.toString());
            result.setCode(StatusEnum.DE_SUCC_STATUS_CODE.toString());
            result.setResult(StatusEnum.SUCC_RESULT.toString());
            return result;
        }

        public ResultJson doPermVeri(String encrypt_data){
            //解密
            ResultJson resPvInfo = permVeriServiceImp.perVerificationAESDc(encrypt_data);
            if(permVeriServiceImp.perVerification(resPvInfo)){
                return  resPvInfo;
            }
            return new ResultJson(DesEnum.NO_PERM.toString(),StatusEnum.DE_FAIL_STATUS_CODE.toString(),StatusEnum.FAIL_RESULT.toString());
        }

        public ResultJson doDataConv(Map<String, Object> data){
            //
            Map<String,Object> map = dataConvServiceImp.DateConversionReplace(data);
            ResultJson resultJson = new ResultJson();
            resultJson.setDataMap(map);
            return resultJson;
        }

        @Async("asyncPoolExecutor")
        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public CompletableFuture<ResultJson> doPerInfo(Map<String,Object> data, String from){
            return CompletableFuture.completedFuture(persInfoServiceImp.perInformation(data, from, new Date(System.currentTimeMillis())));
        }

}
