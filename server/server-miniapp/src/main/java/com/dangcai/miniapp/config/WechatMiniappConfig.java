package com.dangcai.miniapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信小程序配置类
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wechat.miniapp")
public class WechatMiniappConfig {

    /**
     * 小程序AppID
     */
    private String appId;

    /**
     * 小程序AppSecret
     */
    private String appSecret;

    /**
     * 消息推送配置
     */
    private MessagePush messagePush = new MessagePush();

    @Data
    public static class MessagePush {
        /**
         * 模板消息ID
         */
        private String templateId;

        /**
         * 跳转页面
         */
        private String page;
    }
}
