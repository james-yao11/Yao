import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Base64;


public class test2 {
    public static void main(String[] args) {
        try {
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String appid ="HIS"; LocalDateTime dateTime = LocalDateTime.now();
            String timestamp = "1679562101720";
            String as = "123321";
            String md5code = appid+as+timestamp;
            String sign = DigestUtils.md5Hex(md5code);
            System.out.println(sign);
            //根据指定字符串生成秘钥
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128,new SecureRandom("123321123".getBytes()));
            SecretKey sk = kg.generateKey();
            String keyStr = Base64.getEncoder().encodeToString(sk.getEncoded());

            byte[] iv = Base64.getDecoder().decode(keyStr);
            byte[] key = Base64.getDecoder().decode(keyStr);
            String content = "{\"house_id\":123,\n" + "\"order_id\":1,\n" + "\"keep_id\":10,\n" + "\"haha_id\":9,\n" + "\"timestamp\":1679562101720,\n" + "\"appId\":HIS,\n" + "\"sign\":565e248efaac9f614ede9ab2932ca303}";

            String encrypted = Base64.getMimeEncoder().encodeToString(encrypt(iv, key, content.getBytes()));
            System.out.println("加密后: " + encrypted);

            String raw = new String(decrypt(iv, key, Base64.getMimeDecoder().decode(encrypted.replaceAll("\r|\n", ""))));
            System.out.println("解密后: " + raw);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(byte[] iv, byte[] key, byte[] raw) {

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
            IvParameterSpec ips = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
            return cipher.doFinal(raw);
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

}