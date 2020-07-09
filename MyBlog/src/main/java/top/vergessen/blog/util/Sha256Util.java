package top.vergessen.blog.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *生成sha256工具类
 * @author Vergessen
 * @date 2020/7/8 19:14.
 */
public class Sha256Util {

    /**
     * 利用java原生的摘要实现SHA256加密
     * @param str 加密前的报文
     * @return 加密后的报文
     */
    public static String str2Sha256(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }
    /**
     * 将byte转为16进制
     * @param bytes sha256数组
     * @return bytes转回的字符串
     */
    private static String byte2Hex(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                sb.append("0");
            }
            sb.append(temp);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String sha256 = Sha256Util.str2Sha256("vergessen");
        System.out.println(sha256);
    }
}
