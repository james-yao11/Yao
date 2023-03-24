package com.tiCloudServer.systemContact.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "his_info",comment = "His数据信息表",isSimple = true)
public class HisInfo extends PerInfo{
    @IsKey
    @IsAutoIncrement
    private Integer	id;

    @Column
    private String data;

    @Column
    private String time;

}
