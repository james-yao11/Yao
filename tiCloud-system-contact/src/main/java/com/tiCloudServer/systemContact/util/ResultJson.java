package com.tiCloudServer.systemContact.util;

import com.tiCloudServer.systemContact.constant.DesEnum;
import com.tiCloudServer.systemContact.constant.StatusEnum;

import java.util.Map;

public class ResultJson {
        //请求结果信息
        private String message;
        //请求结果状态码
        private String code;
        //请求结果状态码,1成功,0失败
        private String result;
        //暂存的数据
        private String data;
        //暂存的数据map
        private Map<String,Object> dataMap;

        public ResultJson(){
        }
        public ResultJson(String message, String code, String result){
            this.setMessage(message);
            this.setCode(code);
            this.setResult(result);
        }
        public ResultJson(String message, String code, String result, String data){
            this.setMessage(message);
            this.setCode(code);
            this.setResult(result);
            this.setData(data);
        }
        public ResultJson(String message, String code, String result, Map<String, Object> dataMap){
        this.setMessage(message);
        this.setCode(code);
        this.setResult(result);
        this.setData(data);
        this.setDataMap(dataMap);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getData() {
                return data;
            }

    public void setData(String data) {
            this.data = data;
        }

    public Map<String,Object> getDataMap() {
            return dataMap;
        }

    public void setDataMap(Map<String,Object> dataMap) {
            this.dataMap = dataMap;
        }

}
