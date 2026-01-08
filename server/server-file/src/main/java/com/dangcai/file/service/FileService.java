package com.dangcai.file.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.file.domain.SysFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * 文件服务接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
public interface FileService {

    /**
     * 上传文件
     *
     * @param file         文件
     * @param businessType 业务类型
     * @param businessId   业务ID
     * @return 文件信息
     */
    SysFile upload(MultipartFile file, String businessType, Long businessId);

    /**
     * 上传文件（指定业务类型）
     *
     * @param file         文件
     * @param businessType 业务类型
     * @return 文件信息
     */
    SysFile upload(MultipartFile file, String businessType);

    /**
     * 上传文件（默认业务类型）
     *
     * @param file 文件
     * @return 文件信息
     */
    SysFile upload(MultipartFile file);

    /**
     * 下载文件
     *
     * @param id      文件ID
     * @param response 响应
     */
    void download(Long id, HttpServletResponse response);

    /**
     * 下载文件（通过文件名）
     *
     * @param objectName 对象名称
     * @param response   响应
     */
    void downloadByObjectName(String objectName, HttpServletResponse response);

    /**
     * 删除文件
     *
     * @param id 文件ID
     */
    void delete(Long id);

    /**
     * 批量删除文件
     *
     * @param ids 文件ID列表
     */
    void deleteBatch(Long[] ids);

    /**
     * 获取文件信息
     *
     * @param id 文件ID
     * @return 文件信息
     */
    SysFile getById(Long id);

    /**
     * 分页查询文件列表
     *
     * @param page         页码
     * @param size         每页数量
     * @param fileName     文件名
     * @param businessType 业务类型
     * @param contentType  文件类型
     * @return 文件列表
     */
    IPage<SysFile> page(Integer page, Integer size, String fileName, String businessType, String contentType);

    /**
     * 获取文件访问URL
     *
     * @param id 文件ID
     * @return 文件URL
     */
    String getFileUrl(Long id);

    /**
     * 获取临时访问URL（带签名）
     *
     * @param id      文件ID
     * @param expires 过期时间（秒）
     * @return 临时访问URL
     */
    String getPresignedUrl(Long id, Integer expires);

    /**
     * 判断文件是否存在
     *
     * @param objectName 对象名称
     * @return true-存在 false-不存在
     */
    boolean exists(String objectName);

    /**
     * 上传InputStream
     *
     * @param inputStream  输入流
     * @param fileName     文件名
     * @param contentType  内容类型
     * @param businessType 业务类型
     * @param businessId   业务ID
     * @return 文件信息
     */
    SysFile upload(InputStream inputStream, String fileName, String contentType, String businessType, Long businessId);
}
