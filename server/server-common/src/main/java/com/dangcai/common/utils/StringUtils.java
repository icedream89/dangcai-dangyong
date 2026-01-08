package com.dangcai.common.utils;

/**
 * 字符串工具类
 *
 * @author dangcai
 * @since 2026-01-07
 */
public class StringUtils extends org.springframework.util.StringUtils {

    /**
     * 空字符串
     */
    private static final String NULLSTR = "";

    /**
     * 下划线
     */
    private static final char SEPARATOR = '_';

    /**
     * 判断对象是否为空
     *
     * @param object 对象
     * @return true-为空 false-不为空
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * 判断对象是否非空
     *
     * @param object 对象
     * @return true-不为空 false-为空
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return true-为空 false-不为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str 字符串
     * @return true-不为空 false-为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 驼峰转下划线命名
     *
     * @param str 驼峰字符串
     * @return 下划线字符串
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean preCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (!preCharIsUpperCase) {
                    sb.append(SEPARATOR);
                }
                sb.append(Character.toLowerCase(c));
                preCharIsUpperCase = true;
            } else {
                sb.append(c);
                preCharIsUpperCase = false;
            }
        }
        return sb.toString();
    }

    /**
     * 下划线转驼峰命名
     *
     * @param str 下划线字符串
     * @return 驼峰字符串
     */
    public static String toCamelCase(String str) {
        if (str == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder(str.length());
        boolean upperCase = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == SEPARATOR) {
                upperCase = true;
            } else {
                if (upperCase) {
                    sb.append(Character.toUpperCase(c));
                    upperCase = false;
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 生成随机字符串
     *
     * @param length 长度
     * @return 随机字符串
     */
    public static String randomString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
}
