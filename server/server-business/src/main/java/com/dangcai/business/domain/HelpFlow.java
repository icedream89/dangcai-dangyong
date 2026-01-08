package com.dangcai.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工单流转记录实体类
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
@TableName("biz_help_flow")
public class HelpFlow {

    /**
     * 流转记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 工单ID
     */
    private Long ticketId;

    /**
     * 原状态
     */
    private Integer fromStatus;

    /**
     * 目标状态
     */
    private Integer toStatus;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 操作内容
     */
    private String operation;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
