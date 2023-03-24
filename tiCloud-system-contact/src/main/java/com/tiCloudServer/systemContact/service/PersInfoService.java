package com.tiCloudServer.systemContact.service;

import com.tiCloudServer.systemContact.util.ResultJson;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public interface PersInfoService {
    ResultJson perInformation(Map<String,Object> data, String from, Date time);
    boolean ifPersistent(Map<String,Object> data) throws IOException;
}
