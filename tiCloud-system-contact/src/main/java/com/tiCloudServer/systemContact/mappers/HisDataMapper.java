package com.tiCloudServer.systemContact.mappers;

import com.tiCloudServer.systemContact.entity.HisInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface HisDataMapper extends Mapper<HisInfo>, MySqlMapper<HisInfo> {
}
