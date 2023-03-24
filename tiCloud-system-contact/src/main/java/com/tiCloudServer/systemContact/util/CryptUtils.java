package com.tiCloudServer.systemContact.util;

import com.tiCloudServer.systemContact.constant.DesEnum;
import com.tiCloudServer.systemContact.constant.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
public class CryptUtils {

    private static final String MD5_SECRET = "appSecret";
    public static final String MD5_CONFIG = "tiCloud-system-contact/src/main/resources/MD5.properties";
    private static final String AES_CBC = "AES/CBC/PKCS5Padding";
    public static final String AES_CONFIG = "tiCloud-system-contact/src/main/resources/keyseed.properties";
    public static final String AES_KEYSEED = "keySeed";
    private static final String AES_INITKEYSEED = "abcd1234!@#$";
    private static final String AES_SYS_KEY = "AES_SYS_KEY";
    private static final String DES_ALGORITHM = "DES";
    public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

    /**
     * 根据MD5加密规则解密
     *
     * @return boolean
     */
    public Boolean DecryptMD5(String appId, String timestamp, String sign) throws IOException {
        long start = System.currentTimeMillis();
        long diffSeconds = start - Long.parseLong(timestamp);
        boolean flag = Boolean.FALSE;
        double hour = BigDecimal.valueOf((double) diffSeconds / (60 * 60 * 1000)).setScale(2, RoundingMode.HALF_UP).doubleValue();
        System.out.println(hour);
        if (hour > 12){
            return false;
        }
        ReadConfig readConfig = new ReadConfig();
        String appSecret = readConfig.readConfig(MD5_CONFIG,appId).get(MD5_SECRET).toString();
        String md5code = appId +
                appSecret +
                timestamp;

        if(DigestUtils.md5Hex(md5code).equals(sign)){
           flag = true;
        }
        return flag;
    }

    /**
     * 根据密钥对指定的密文cipherText进行解密.
     *
     * @param cipherText 密文
     * @return plainText 解密后的明文
    */
    //这里AES解密采用CBC模式，PKCS5填充方式
    public ResultJson DecryptAES(String cipherText){
        try {
            ReadConfig rc = new ReadConfig();
            String keySeed = rc.readConfig(AES_CONFIG,"HIS").get(AES_KEYSEED).toString();
            SecretKey secretKey = getKey(keySeed);
            String keyStr = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            byte[] iv = Base64.getMimeDecoder().decode(keyStr);
            byte[] key = Base64.getMimeDecoder().decode(keyStr);
            String plainText = null;
            if(decrypt(iv, key, Base64.getMimeDecoder().decode(cipherText))!=null){
                plainText = new String(decrypt(iv, key, Base64.getMimeDecoder().decode(cipherText)), StandardCharsets.UTF_8);
                if(cipherText.equals(EncryptAES(plainText,AES_INITKEYSEED))){
                    return new ResultJson(DesEnum.DECRYPT_FAIL.toString(),StatusEnum.DE_FAIL_STATUS_CODE.toString() ,StatusEnum.FAIL_RESULT.toString());
                }
            }
            return new ResultJson(DesEnum.DECRYPT_SUC.toString(),StatusEnum.DE_SUCC_STATUS_CODE.toString(),StatusEnum.SUCC_RESULT.toString(),plainText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static byte[] decrypt(byte[] iv, byte[] key, byte[] encryptContent) {

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ips);
            return cipher.doFinal(encryptContent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据密钥对指定的明文plainText进行加密.
     *
     * @param  plainText 明文
     * @return cipherText 密文
     */
    public static String EncryptAES(String plainText, String keySeed) {
        try {
            SecretKey secretKey = getKey(keySeed);
            String keyStr = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            byte[] iv = Base64.getDecoder().decode(keyStr);
            byte[] key = Base64.getDecoder().decode(keyStr);
            IvParameterSpec ips = new IvParameterSpec(iv);
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance(AES_CBC);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ips);
            byte[] p = plainText.getBytes(StandardCharsets.UTF_8);
            byte[] result = cipher.doFinal(p);
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取密匙
     *
     * @param keySeed 密匙种子
     * @return key 密匙
     */
    public static SecretKey getKey(String keySeed) {

        if (keySeed == null) {
            keySeed = System.getenv(AES_SYS_KEY);
        }

        if (keySeed == null) {
            keySeed = System.getProperty(AES_SYS_KEY);
        }

        if (keySeed == null || keySeed.trim().length() == 0) {
            keySeed = AES_INITKEYSEED;// 默认种子
        }

        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128,new SecureRandom(keySeed.getBytes()));
            return generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DES解密
     *
     * @return plainText
     */
    public ResultJson DecryptDES(String cipherText, String hexKeyStr) throws Exception {
        Key desKey = keyGenerator(hexKeyStr);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //初始化Cipher对象，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, desKey);
        String plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText)).toString();
        return new ResultJson();
    }

    /**
     * DES密匙生成
     *
     */
    private static SecretKey keyGenerator(String hexKeyStr) throws Exception {
        byte[] input = hexString2Bytes(hexKeyStr);
        DESKeySpec desKey = new DESKeySpec(input);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
        return keyFactory.generateSecret(desKey);
    }

    /**
     * 十六进制字符串到字节数组转换
     */
    public static byte[] hexString2Bytes(String hexStr) {
        byte[] b = new byte[hexStr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexStr.charAt(j++);
            char c1 = hexStr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }
    /**
     * DES填充
     */
    private static int parse(char c) {
        if (c >= 'a') {
            return (c - 'a' + 10) & 0x0f;
        }
        if (c >= 'A') {
            return (c - 'A' + 10) & 0x0f;
        }
        return (c - '0') & 0x0f;
    }
}
