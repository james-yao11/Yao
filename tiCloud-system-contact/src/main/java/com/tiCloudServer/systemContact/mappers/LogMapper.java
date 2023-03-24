package com.tiCloudServer.systemContact.mappers;

import com.tiCloudServer.systemContact.entity.Action;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface LogMapper extends Mapper<Action>, MySqlMapper<Action> {

}
