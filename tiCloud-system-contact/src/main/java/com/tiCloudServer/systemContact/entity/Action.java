package com.tiCloudServer.systemContact.entity;

import lombok.Data;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;


@Data
@Table(name = "action",comment = "日志信息表",isSimple = true)
public class Action {
    @IsAutoIncrement
    @IsKey
    private Long id ;

    @Column(comment = "日志描述")
    private String des;

    @Column(comment = "日志类型")
    private String type;

    @Column(comment = "操作方")
    private String username;

    @Column(comment = "日志产生时间",type = MySqlTypeConstant.DATETIME)
    private String time;
}
