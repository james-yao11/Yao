package com.tiCloudServer.systemContact.util;

import org.apache.ibatis.javassist.*;

public class ReDefineClass {
    /***
     * 探针修改 class字节码，修改属性默认值
     */
    public static byte[] reDefineClass(String className, String field,byte[] data) {
        try {
            ClassPool pool = new ClassPool();
            pool.appendSystemPath();
            CtClass ctClass = pool.get(className);
            CtField ctField = ctClass.getField(field);
            ctField.setAttribute(field,data);
            return ctClass.toBytecode();
        } catch (Throwable e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}


