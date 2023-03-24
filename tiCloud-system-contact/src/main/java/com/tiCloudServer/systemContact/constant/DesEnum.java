package com.tiCloudServer.systemContact.constant;

public enum DesEnum {
    NO_PERM("没有权限"),
    DATA_FM_FAIL("格式转换失败"),
    PER_INFO_FAIL("持久化失败"),
    THIRD_CALL_SUC("第三方调用成功"),
    DECRYPT_DATA("解密数据"),
    PER_DATA("存储数据"),DECRYPT_SUC("解密成功"),DECRYPT_FAIL("解密失败");

    public String DES;

    DesEnum(String DES) {
        this.DES = DES;
    }

}
