package com.tiangles.storm.utilities;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @ClassName: com.qust.SecretUtils
 * @Description: 3DES加密解密工具类
 * @author zhaokaiqiang
 * @date 2014-11-13 下午11:28:14
 *
 */
public class DES3Utils {

    // 定义加密算法，DESede即3DES
    private static final String Algorithm = "DESede";
    // 加密密钥
    private static final String PASSWORD_CRYPT_KEY = "$T0rm*(^%$#";

    /**
     * 加密方法
     *
     * @param str
     *            源数据的字节数组
     * @return
     */
    public static String encryptMode(String str) {
        byte[] src = str.getBytes();
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(
                    build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            // 实例化Cipher
            Cipher cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, deskey);
            byte[] bytes = cipher.doFinal(src);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 解密函数
     *
     * @param str
     *            密文的字节数组
     * @return
     */
    public static String decryptMode(String str) {
        byte[] src = Base64.decode(str, Base64.DEFAULT);
        try {
            SecretKey deskey = new SecretKeySpec(
                    build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            byte b[] =  c1.doFinal(src);
            return new String(b);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 根据字符串生成密钥24位的字节数组
     *
     * @param keyStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] build3DesKey(String keyStr)
            throws UnsupportedEncodingException {
        byte[] key = new byte[24];
        byte[] temp = keyStr.getBytes("UTF-8");

        if (key.length > temp.length) {
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }
}