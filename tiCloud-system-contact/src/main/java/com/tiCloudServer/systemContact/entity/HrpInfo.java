package com.tiCloudServer.systemContact.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Repository;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "hrp_info",comment = "Hrp数据信息表",isSimple = true)
public class HrpInfo extends PerInfo{
    @IsKey
    @IsAutoIncrement
    private Integer	id;

    @Column
    private String data;

    @Column
    private String time;
}
