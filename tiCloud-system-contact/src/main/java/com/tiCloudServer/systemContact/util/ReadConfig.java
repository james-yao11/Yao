package com.tiCloudServer.systemContact.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class ReadConfig {


    /**
     * 读配置文件
     */
    public Map<String, Object> readConfig(String from) throws IOException {

        InputStream ins = new BufferedInputStream(new FileInputStream(from));

        Properties ps = new Properties();
        ps.load(ins);
        Set<String> names = ps.stringPropertyNames();
        Map<String, Object> propertiesMap = new HashMap<>();
        for (String name:names
             ) {
                propertiesMap.put(name, ps.getProperty(name));
                }
        return propertiesMap;
    }
    public Map<String, Object> readConfig(String from,String use) throws IOException {
        InputStream ins = new BufferedInputStream(new FileInputStream(from));

        Properties ps = new Properties();
        ps.load(ins);
        Set<String> names = new HashSet<>();
        String appSecret = null;
        Set<String> tempNames = ps.stringPropertyNames();
        Map<String, Object> propertiesMap = new HashMap<>();
        String realName = null;
        for (String name:tempNames
        ) {
            String[] temp = name.split("\\.");
            if(use.equals(temp[0])) {
                names.add(temp[0]);
                appSecret = temp[1];
            }
        }
        for (String name:names
             ) {
            for (String s:tempNames
            ) {
                if(s.contains(name)){
                    realName = s;
                }
            }
            propertiesMap.put(appSecret, ps.getProperty(realName));

        }
        return propertiesMap;
    }

    /**
     * 数据转换读配置文件
     */
    public Map<String, Object> readConfig(String from,String use,String method) throws IOException {

        InputStream ins = new BufferedInputStream(new FileInputStream(from));

        Properties ps = new Properties();
        ps.load(ins);
        Map<String,Map<String,List<String>>> tags = new HashMap();
        Set<String> names = new HashSet<>();
        Set<String> tempNames = ps.stringPropertyNames();

        //循环添加调用接口对应方法和参数，temp[0]表第三方，temp[1]表方法，temp[2]表参数
        for (String name:tempNames
            ) {
                String[] temp = name.split("\\.");
                if (tags.containsKey(temp[0])){
                    if (tags.get(temp[0]).containsKey(temp[1])){
                        tags.get(temp[0]).get(temp[1]).add(temp[2]);
                    }
                    continue;
                }
                    Map<String,List<String>> map = new HashMap<>();
                    List<String> params = new ArrayList<>();
                    params.add(temp[2]);
                    map.put(temp[1],params);
                    tags.put(temp[0],map);
                }

        //取出对应的参数
        for (Map.Entry<String,Map<String,List<String>>> e:tags.entrySet()
             ) {
                if(use.equals(e.getKey())){
                    for (Map.Entry<String,List<String>> entry: e.getValue().entrySet()
                         ) {
                            if (method.equals(entry.getKey())) {
                                names.addAll(entry.getValue());
                            }
                    }
                }
            }

        //返回参数
        Map<String, Object> propertiesMap = new HashMap<>();
        String realName = null;
        for (String name:names
        ) {
            for (String s:tempNames
            ) {
                if(s.contains(name)){
                    realName = s;
                }
            }
            propertiesMap.put(name, ps.getProperty(realName));
        }
        return propertiesMap;
    }
}
