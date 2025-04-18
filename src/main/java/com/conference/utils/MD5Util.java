package com.conference.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public final class MD5Util {

    private static final String KEY = "KEY";

    public static void main(String[] args) {
        String password = "123456";
        String md5Pwd = encrypt(password+KEY);
        System.out.println("md5Pwd = " + md5Pwd);

        String password1 = "1234567";
        String inputMd5Pwd1 = encrypt(password1+KEY);
        System.out.println("1234567 --> " + inputMd5Pwd1);
        if (Objects.equals(md5Pwd, inputMd5Pwd1)){
            System.out.println("密码正确");
        }else {
            System.out.println("密码错误");
        }

        String password2 = "123456";
        String inputMd5Pwd2 = encrypt(password2+KEY);
        System.out.println("123456 --> " + inputMd5Pwd2);
        if (Objects.equals(md5Pwd, inputMd5Pwd2)){
            System.out.println("密码正确");
        }else {
            System.out.println("密码错误");
        }
    }

    /**
     * MD5加密
     * @param strSrc 明文密码
     * @return 加密后的密码
     */
    public static String encrypt(String strSrc) {   // MD5加密方法
        try {
            char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                    '9', 'a', 'b', 'c', 'd', 'e', 'f' };
            byte[] bytes = strSrc.getBytes();

            // 获取一个MD5消息摘要实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 更新消息摘要，将输入的文本内容转换为字节数组并进行处理
            md.update(bytes);

            // 计算消息摘要，得到MD5散列值
            bytes = md.digest();
            int j = bytes.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (int i = 0; i < bytes.length; i++) {
                byte b = bytes[i];
                chars[k++] = hexChars[b >>> 4 & 0xf];   // 取字节中高 4 位的数字转换, 可使用 >>> 逻辑右移替代>>，>>>将符号位一起右移
                chars[k++] = hexChars[b & 0xf];     // 取字节中低 4 位的数字转换
            }
            return new String(chars);
        } catch (NoSuchAlgorithmException e) {
            // 处理NoSuchAlgorithmException异常，通常是因为指定的MD5算法不可用
            e.printStackTrace();
            throw new RuntimeException("MD5加密出错！！+" + e);
        }
    }

    /**
     * 判断字符串的md5校验码是否与一个已知的md5码相匹配
     *
     * @param password  要校验的字符串
     * @param md5PwdStr 已知的md5校验码
     * @return
     */
    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = encrypt(password);
        return s.equals(md5PwdStr);
    }
}