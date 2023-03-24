package com.tiCloudServer.systemContact.service.imp;

import com.tiCloudServer.systemContact.mappers.LogMapper;
import com.tiCloudServer.systemContact.entity.Action;
import com.tiCloudServer.systemContact.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LogServiceImp implements LogService {
    @Resource
    public LogMapper logMapper;

    public void saveAction(Action action){
        logMapper.insertSelective(action);
    }
}
