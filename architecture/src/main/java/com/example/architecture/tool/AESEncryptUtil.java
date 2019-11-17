package com.example.architecture.tool;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

//import org.apache.log4j.Logger;
//import richinfo.tools.common.AssertUtil;

public class AESEncryptUtil {

//    private static Logger logger = Logger.getLogger(AESEncryptUtil.class);

    public static final String ENCODING_AES_KEY = "3aa1ec5c463be2d40b0e696927fe7ca33931f2c2zh7";
    //正式线:"3aa1ec5c463be2d40b0e696927fe7ca33931f2c2zh7";
    //测试线 "16e40d28631333dcaff078aa7c13a91c8762fca34ik";

    /**
     * 将byte[]转为各种进制的字符串
     * @param bytes byte[]
     * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix){
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    /**
     * base 64 encode
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes){
        return Base64.encodeBase64String(bytes);
    }

    /**
     * base 64 decode
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception{
        return StringUtils.isEmpty(base64Code) ? null : Base64.decodeBase64(base64Code);
    }

    /**
     * AES加密
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgenInit(kgen, encryptKey.getBytes());

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

        return cipher.doFinal(content.getBytes("utf-8"));
    }

    public static byte[] aesEncryptToBytes(String content, byte[] encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgenInit(kgen, encryptKey);

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

        return cipher.doFinal(content.getBytes("utf-8"));
    }

    /**
     * AES加密为base 64 code
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    public static String aesEncrypt(String content, byte[] encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * AES解密
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey 解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgenInit(kgen, decryptKey.getBytes());

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes,"utf-8");
    }

    public static String aesDecryptByBytes(byte[] encryptBytes, byte[] decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgenInit(kgen, decryptKey);

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes,"utf-8");
    }

    /**
     * 将base 64 code AES解密
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

    public static String aesDecrypt(String encryptStr, byte[] decryptKey) throws Exception {
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

    public static byte[] getAESKey(String encodingAESKey){
        byte[] array = Base64.decodeBase64(encodingAESKey+"=");
        return array;
    }

    /**防止在linux下随机生成key
     * @throws NoSuchAlgorithmException */
    public static void kgenInit(KeyGenerator kgen, byte[] bytes)
            throws NoSuchAlgorithmException{
        //1.防止linux下 随机生成key
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(bytes);
        //2.根据密钥初始化密钥生成器
        kgen.init(128, secureRandom);
    }



//    public static AddressBookMessage getRes(String message){
//        AddressBookMessage abm = null;
//        try {
//            abm = new AddressBookMessage();
//            String timeStamp = SignatureUtil.getTimeStamp();
//            String nonce = SignatureUtil.getRandomStr();
//            abm.setTimeStamp(timeStamp);
//            abm.setNonce(nonce);
//            abm.setMsg_signature(SignatureUtil.createSignature(timeStamp, nonce));
//            abm.setEncrypt(AESEncryptUtil.aesEncrypt(message,
//                    AESEncryptUtil.getAESKey(AESEncryptUtil.ENCODING_AES_KEY)));
//        } catch (Exception e) {
//            LOGGER.info("生成接口结果错误:"+e.getMessage());
//            e.printStackTrace();
//        }
//        return abm;
//    }


    public static void main(String[] args) throws Exception {

        String content = "success";
        System.out.println("raw string：" + content);

        String encrypt = aesEncrypt(content, getAESKey(ENCODING_AES_KEY));
        System.out.println("encrypt string：" + encrypt);

        String decrypt = aesDecrypt(encrypt, getAESKey(ENCODING_AES_KEY));
        System.out.println("decrypt string：" + decrypt);

//        String ss = "PqOFUQcp/8iJJ44x42Hgaw==";
//        String dd = aesDecrypt(ss, getAESKey(ENCODING_AES_KEY));
//        System.out.println(dd);
    }

}