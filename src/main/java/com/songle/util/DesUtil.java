package com.songle.util;


import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author songle
 * @create 2019-05-14 下午 14:13
 */
public class DesUtil {

    private static final String KEY_ALGRORITHM = "DESede";
    private static final String DEFAULT_CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";// 默认的加密算法

    /**
     * DESede 加密操作
     *
     * @param content
     *            待加密内容
     * @param key
     *            加密密钥
     * @return 返回Base64转码后的加密数据
     */

    public static String encrypt(String content,String key){
        try {
            //创建密码器;
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = content.getBytes("utf-8");
            //初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE,getSecretKeySpec(key));
            //加密
            byte[] result = cipher.doFinal(byteContent);
            //通过base64转码返回
            return Base64.encodeBase64String(result);

        }catch(Exception ex){
            Logger.getLogger(DesUtil.class.getName()).log(Level.SEVERE,null,ex);
        }
        return null;
    }

    /**
     * DESede 解密操作
     *
     * @param content
     * @param key
     * @return
     */
    public static String decrypt(String content,String key){
        try {
            //创建密码器;
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,getSecretKeySpec(key));
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));
            return new String(result,"utf-8");

        }catch(Exception ex){
            Logger.getLogger(DesUtil.class.getName()).log(Level.SEVERE,null,ex);
        }
        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKey getSecretKey(String key){
        try {

            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(),KEY_ALGRORITHM);
            SecretKey secretKey = SecretKeyFactory.getInstance(KEY_ALGRORITHM).generateSecret(secretKeySpec);
            return secretKey;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */

    private static SecretKeySpec getSecretKeySpec(String key){
        //返回生成指定算法密钥生成器的KeyGenerator对象；
        KeyGenerator kg = null;
        try {

            kg = KeyGenerator.getInstance(KEY_ALGRORITHM);
            kg.init(new SecureRandom(key.getBytes()));
            //生成一个加密密钥;
            SecretKey secretKey = kg.generateKey();
            //转换为DESede专用
            return new SecretKeySpec(secretKey.getEncoded(),KEY_ALGRORITHM);
        }catch(Exception e){
            Logger.getLogger(DesUtil.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    /**
     * 测试
     */
   /* public static void main(String args[]){
        String content = "我有一个小阔耐,我爱我家宝宝";
        String key = " ";
        String s1 = DesUtil.encrypt(content,key);
        String s2 = DesUtil.decrypt(s1,key);
        System.out.println("对称加密后的结果为:"+s1);
        System.out.println("对称解密后的结果为:"+s2);
    }*/
}
