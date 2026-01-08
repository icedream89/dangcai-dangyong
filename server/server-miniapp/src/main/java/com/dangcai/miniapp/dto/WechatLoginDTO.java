package com.dangcai.miniapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 微信登录DTO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
@Schema(description = "微信登录请求")
public class WechatLoginDTO {

    @Schema(description = "微信登录凭证（code）")
    @NotBlank(message = "登录凭证不能为空")
    private String code;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户头像")
    private String avatarUrl;

    @Schema(description = "性别：0-未知 1-男 2-女")
    private Integer gender;

    @Schema(description = "省份")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "国家")
    private String country;

    @Schema(description = "语言")
    private String language;
}
