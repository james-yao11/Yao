package com.tiCloudServer.systemContact.constant;

public enum StatusEnum {

    DE_FAIL_STATUS_CODE(400,"调用服务失败"),
    DE_SUCC_STATUS_CODE(401,"调用服务成功"),
    SUCC_RESULT(1,"成功"),
    FAIL_RESULT(0,"失败");

    public final int code;
    public final String des;
    StatusEnum(int code, String des) {
        this.code = code;
        this.des = des;
    }

}
