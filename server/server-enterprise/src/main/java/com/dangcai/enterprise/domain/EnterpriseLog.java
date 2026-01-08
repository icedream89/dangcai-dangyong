package com.dangcai.enterprise.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 企业变更记录实体类
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
@TableName("ent_enterprise_log")
public class EnterpriseLog {

    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 操作类型：新增、修改、审核、状态变更
     */
    private String operationType;

    /**
     * 操作内容（JSON格式）
     */
    private String operationContent;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

    /**
     * 备注
     */
    private String remark;
}
