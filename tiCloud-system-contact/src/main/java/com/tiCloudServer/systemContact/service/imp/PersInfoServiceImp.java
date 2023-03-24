package com.tiCloudServer.systemContact.service.imp;

import com.tiCloudServer.systemContact.annotation.ServiceLog;
import com.tiCloudServer.systemContact.constant.DesEnum;
import com.tiCloudServer.systemContact.constant.StatusEnum;
import com.tiCloudServer.systemContact.entity.HisInfo;
import com.tiCloudServer.systemContact.entity.HrpInfo;
import com.tiCloudServer.systemContact.mappers.HisDataMapper;
import com.tiCloudServer.systemContact.mappers.HrpDataMapper;
import com.tiCloudServer.systemContact.service.PersInfoService;
import com.tiCloudServer.systemContact.util.ReadConfig;
import com.tiCloudServer.systemContact.util.ResultJson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@EnableAsync
@Service
public class PersInfoServiceImp implements PersInfoService {

    private static final String CONFIG_FILE = "tiCloud-system-contact/src/main/resources/persistent.properties";

    @Resource
    public HisDataMapper hisDataMapper;

    @Resource
    public HrpDataMapper hrpDataMapper;

    @ServiceLog
    public ResultJson perInformation(Map<String,Object> data, String from, Date time) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(from.equals("HIS")){
            HisInfo hisInfo = new HisInfo();
            hisInfo.setData(data.toString());
            hisInfo.setTime(simpleDateFormat.format(date));
            hisDataMapper.insertSelective(hisInfo);
            return new ResultJson(DesEnum.PER_DATA.toString(),StatusEnum.DE_SUCC_STATUS_CODE.toString(),StatusEnum.SUCC_RESULT.toString());
        }
        if(from.equals("HRP")){
            HrpInfo hrpInfo = new HrpInfo();
            hrpInfo.setData(data.toString());
            hrpInfo.setTime(simpleDateFormat.format(date));
            hrpDataMapper.insertSelective(hrpInfo);
            return new ResultJson(DesEnum.PER_DATA.toString(),StatusEnum.DE_SUCC_STATUS_CODE.toString(),StatusEnum.SUCC_RESULT.toString());
        }
        return new ResultJson(DesEnum.PER_DATA.toString(),StatusEnum.DE_FAIL_STATUS_CODE.toString(),StatusEnum.FAIL_RESULT.toString());
    }

    @SneakyThrows
    @ServiceLog
    public boolean ifPersistent(Map<String,Object> data){
        ReadConfig rc = new ReadConfig();
        Map<String,Object> propertiesMap =  rc.readConfig(CONFIG_FILE);
        if(propertiesMap.equals(data)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
