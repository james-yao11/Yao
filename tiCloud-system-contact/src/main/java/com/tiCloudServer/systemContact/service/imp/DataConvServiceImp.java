package com.tiCloudServer.systemContact.service.imp;

import com.tiCloudServer.systemContact.annotation.ServiceLog;
import com.tiCloudServer.systemContact.rules.PartitionRule;
import com.tiCloudServer.systemContact.rules.ReplaceRule;
import com.tiCloudServer.systemContact.service.DataConvService;
import com.tiCloudServer.systemContact.util.ReadConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Service
public class DataConvServiceImp implements DataConvService {

       //替换规则配置文件路径
       private static final String DATA_RULE_REP_CONFIG = "D:\\code\\tiColud-server\\tiCloud-system-contact\\src\\main\\resources\\rule.properties";
       //分割规则配置文件路径
       private static final String DATA_RULE_PAR_CONFIG = "";
       private static String THIRD = "HIS";
       private static String METHOD = "add";
       /**
        *通过规则引擎完成请求字段名称转换
        */
       @SneakyThrows
       @ServiceLog
       public Map<String,Object> DateConversionReplace(Map<String,Object> data){
           ReplaceRule replaceRule = new ReplaceRule();
           ReadConfig rc = new ReadConfig();
           Map<String,Object> propertiesMap = rc.readConfig(DATA_RULE_REP_CONFIG,THIRD,METHOD);
           return replaceRule.Replace(data,propertiesMap);
       }

       /**
        *通过规则引擎完成字符串分割
        */
       @SneakyThrows
       @ServiceLog
       public Map<String, String> DateConversionPartition(String UDI){
           PartitionRule partitionRule = new PartitionRule();
           InputStream ins = new BufferedInputStream(new FileInputStream(DATA_RULE_PAR_CONFIG));
           Properties ps = new Properties();
           ps.load(ins);
           Set<String> names = ps.stringPropertyNames();
           Map<String, List<String>> map = new HashMap<>();
           for (String name:names
           ) {
               map.put(name,Arrays.asList(ps.getProperty(name).split(",")));
           }
           return partitionRule.Partition(UDI,map);
       }
}
