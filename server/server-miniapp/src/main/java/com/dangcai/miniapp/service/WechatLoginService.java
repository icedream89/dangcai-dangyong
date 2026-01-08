package com.dangcai.miniapp.service;

import com.dangcai.miniapp.dto.WechatLoginDTO;

/**
 * 微信登录服务接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
public interface WechatLoginService {

    /**
     * 微信登录
     *
     * @param loginDTO 登录请求
     * @return 登录用户信息（包含Token）
     */
    Object login(WechatLoginDTO loginDTO);

    /**
     * 获取微信Access Token
     *
     * @param code 微信登录凭证
     * @return OpenID
     */
    String getWxOpenId(String code);

    /**
     * 获取微信用户手机号
     *
     * @param code 手机号授权码
     * @return 手机号
     */
    String getPhoneNumber(String code);
}
