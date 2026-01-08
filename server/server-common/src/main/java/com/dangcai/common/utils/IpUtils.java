package com.dangcai.common.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * IP地址工具类
 *
 * @author dangcai
 * @since 2026-01-07
 */
public class IpUtils {

    private static final String UNKNOWN = "unknown";
    private static final String IP_SEPARATOR = ",";
    private static final int IP_MAX_LENGTH = 15;

    /**
     * 获取客户端IP地址
     *
     * @param request 请求对象
     * @return IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }

        String ip = null;

        // 1. 检查X-Forwarded-For (通过代理服务器)
        ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            // 2. 检查Proxy-Client-IP
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            // 3. 检查WL-Proxy-Client-IP (WebLogic)
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            // 4. 检查HTTP_CLIENT_IP
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            // 5. 检查HTTP_X_FORWARDED_FOR
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            // 6. 直接获取RemoteAddr
            ip = request.getRemoteAddr();
        }

        // 7. 处理多个IP的情况（X-Forwarded-For可能包含多个IP，第一个为真实IP）
        if (ip != null && ip.length() > IP_MAX_LENGTH) {
            int indexOfComma = ip.indexOf(IP_SEPARATOR);
            if (indexOfComma > 0) {
                ip = ip.substring(0, indexOfComma);
            }
        }

        // 8. 处理localhost情况
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }

    /**
     * 判断是否为内网IP
     *
     * @param ip IP地址
     * @return true-内网IP false-外网IP
     */
    public static boolean isInternalIp(String ip) {
        if (ip == null || ip.length() == 0) {
            return false;
        }

        // 127.0.0.1
        if ("127.0.0.1".equals(ip)) {
            return true;
        }

        // 10.0.0.0 - 10.255.255.255
        if (ip.startsWith("10.")) {
            return true;
        }

        // 172.16.0.0 - 172.31.255.255
        if (ip.startsWith("172.")) {
            String[] parts = ip.split("\\.");
            if (parts.length == 4) {
                try {
                    int second = Integer.parseInt(parts[1]);
                    if (second >= 16 && second <= 31) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }

        // 192.168.0.0 - 192.168.255.255
        if (ip.startsWith("192.168.")) {
            return true;
        }

        return false;
    }

    /**
     * 判断IP地址是否有效
     *
     * @param ip IP地址
     * @return true-有效 false-无效
     */
    public static boolean isValidIp(String ip) {
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            return false;
        }

        // IPv4地址格式验证
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(ipPattern);
    }
}
