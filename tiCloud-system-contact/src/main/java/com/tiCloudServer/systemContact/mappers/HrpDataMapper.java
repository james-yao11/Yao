package com.tiCloudServer.systemContact.mappers;

import com.tiCloudServer.systemContact.entity.HrpInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface HrpDataMapper extends Mapper<HrpInfo>, MySqlMapper<HrpInfo> {
}
