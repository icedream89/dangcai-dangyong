package com.dangcai.file.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dangcai.common.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 系统文件实体
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_file")
public class SysFile extends BaseEntity {

    /**
     * 文件ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文件名（原始文件名）
     */
    private String fileName;

    /**
     * 文件路径（存储路径）
     */
    private String filePath;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件类型（MIME类型）
     */
    private String contentType;

    /**
     * 文件扩展名
     */
    private String extension;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * 对象名称（MinIO中的对象名）
     */
    private String objectName;

    /**
     * 文件URL（访问地址）
     */
    private String fileUrl;

    /**
     * 上传用户ID
     */
    private Long uploadUserId;

    /**
     * 上传用户名
     */
    private String uploadUserName;

    /**
     * 业务类型（企业资质、产品图片等）
     */
    private String businessType;

    /**
     * 业务ID
     */
    private Long businessId;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 是否公开：0-私有 1-公开
     */
    private Integer isPublic;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;
}
