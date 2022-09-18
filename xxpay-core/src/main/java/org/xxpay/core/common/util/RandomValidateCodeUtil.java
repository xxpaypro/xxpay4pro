package org.xxpay.core.common.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomValidateCodeUtil {


    private static String randString = "0123456789";//随机产生只有数字的字符串 private String
    //private String randString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";//随机产生只有字母的字符串
    //private static String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";//随机产生数字与字母组合的字符串
    private static int lineSize = 40;// 干扰线数量


    private static Random random = new Random();

    /**
     * 获得字体
     */
    private static Font getFont(int fontSize) {
        return new Font("arial", Font.ITALIC | Font.BOLD, fontSize);
    }

    /**
     * 获得颜色
     */
    private static Color getRandColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }

    /**
     * 生成随机图片
     */
    public static Map<String, Object> getRandcode(int width, int height, int stringNum, int fontSize) {
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();// 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
        g.fillRect(0, 0, width, height);//图片大小
        g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, fontSize));//字体大小
        g.setColor(getRandColor(110, 133));//字体颜色
        // 绘制干扰线
        for (int i = 0; i <= lineSize; i++) {
            drowLine(g, width, height);
        }
        // 绘制随机字符
        String randomString = "";
        for (int i = 1; i <= stringNum; i++) {
            randomString = drowString(g, randomString, i, fontSize);
        }
        g.dispose();

        Map map = new HashMap();
        map.put("randomString", randomString);
        map.put("randomImage", image);
        return map;
    }

    /**
     * 绘制字符串
     */
    private static String drowString(Graphics g, String randomString, int i, int fontSize) {
        g.setFont(getFont(fontSize));
        g.setColor(new Color(random.nextInt(101), random.nextInt(111), random
                .nextInt(121)));
        String rand = String.valueOf(getRandomString(random.nextInt(randString
                .length())));
        randomString += rand;
        g.translate(random.nextInt(3), random.nextInt(3));
        g.drawString(rand, 13 * i, 25);
        return randomString;
    }

    /**
     * 绘制干扰线
     */
    private static void drowLine(Graphics g, int width, int height) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.drawLine(x, y, x + xl, y + yl);
    }

    /**
     * 获取随机的字符
     */
    public static String getRandomString(int num) {
        return String.valueOf(randString.charAt(num));
    }

    public static void main(String[] args) {
        System.out.println(RandomValidateCodeUtil.getRandcode(100, 20, 5, 24));
    }

}