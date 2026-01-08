package com.dangcai.business.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 求助工单DTO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class HelpTicketDTO {

    /**
     * 工单ID（修改时必填）
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 求助类型：1-技术咨询 2-故障报修 3-服务请求 4-投诉建议
     */
    @NotNull(message = "求助类型不能为空", groups = {Add.class, Update.class})
    private Integer helpType;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = {Add.class, Update.class})
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空", groups = {Add.class, Update.class})
    private String content;

    /**
     * 图片地址（多个用逗号分隔）
     */
    private String images;

    /**
     * 优先级：1-紧急 2-普通 3-低
     */
    @NotNull(message = "优先级不能为空", groups = {Add.class, Update.class})
    private Integer priority;

    /**
     * 新增分组
     */
    public interface Add {
    }

    /**
     * 修改分组
     */
    public interface Update {
    }
}
