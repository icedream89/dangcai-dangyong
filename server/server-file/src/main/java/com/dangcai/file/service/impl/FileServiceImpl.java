package com.dangcai.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.common.exception.BusinessException;
import com.dangcai.common.utils.SecurityUtils;
import com.dangcai.common.utils.StringUtils;
import com.dangcai.file.config.MinioConfig;
import com.dangcai.file.domain.SysFile;
import com.dangcai.file.mapper.SysFileMapper;
import com.dangcai.file.service.FileService;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 文件服务实现
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;
    private final SysFileMapper fileMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysFile upload(MultipartFile file, String businessType, Long businessId) {
        try {
            // 1. 参数校验
            if (file == null || file.isEmpty()) {
                throw new BusinessException("文件不能为空");
            }

            // 2. 获取原始文件名和扩展名
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);

            // 3. 生成对象名称（UUID + 扩展名）
            String objectName = generateObjectName(extension);

            // 4. 获取输入流
            InputStream inputStream = file.getInputStream();

            // 5. 上传到MinIO
            PutObjectArgs.Builder builder = PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(objectName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType());

            minioClient.putObject(builder.build());

            // 6. 构建文件信息
            SysFile sysFile = new SysFile();
            sysFile.setFileName(originalFilename);
            sysFile.setFilePath("/" + minioConfig.getBucketName() + "/" + objectName);
            sysFile.setFileSize(file.getSize());
            sysFile.setContentType(file.getContentType());
            sysFile.setExtension(extension);
            sysFile.setBucketName(minioConfig.getBucketName());
            sysFile.setObjectName(objectName);
            sysFile.setFileUrl(minioConfig.getEndpoint() + "/" + minioConfig.getBucketName() + "/" + objectName);
            sysFile.setUploadUserId(SecurityUtils.getUserId());
            sysFile.setUploadUserName(SecurityUtils.getUsername());
            sysFile.setBusinessType(businessType);
            sysFile.setBusinessId(businessId);
            sysFile.setDownloadCount(0);
            sysFile.setIsPublic(0);

            // 7. 保存到数据库
            fileMapper.insert(sysFile);

            log.info("文件上传成功，文件ID：{}，文件名：{}", sysFile.getId(), originalFilename);
            return sysFile;

        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败：" + e.getMessage());
        }
    }

    @Override
    public SysFile upload(MultipartFile file, String businessType) {
        return upload(file, businessType, null);
    }

    @Override
    public SysFile upload(MultipartFile file) {
        return upload(file, null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysFile upload(InputStream inputStream, String fileName, String contentType, String businessType, Long businessId) {
        try {
            // 1. 参数校验
            if (inputStream == null) {
                throw new BusinessException("输入流不能为空");
            }

            // 2. 获取扩展名
            String extension = getFileExtension(fileName);

            // 3. 生成对象名称
            String objectName = generateObjectName(extension);

            // 4. 读取所有字节（获取文件大小）
            byte[] bytes = inputStream.readAllBytes();
            long fileSize = bytes.length;

            // 5. 上传到MinIO
            PutObjectArgs.Builder builder = PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(objectName)
                    .stream(new java.io.ByteArrayInputStream(bytes), fileSize, -1)
                    .contentType(contentType);

            minioClient.putObject(builder.build());

            // 6. 构建文件信息
            SysFile sysFile = new SysFile();
            sysFile.setFileName(fileName);
            sysFile.setFilePath("/" + minioConfig.getBucketName() + "/" + objectName);
            sysFile.setFileSize(fileSize);
            sysFile.setContentType(contentType);
            sysFile.setExtension(extension);
            sysFile.setBucketName(minioConfig.getBucketName());
            sysFile.setObjectName(objectName);
            sysFile.setFileUrl(minioConfig.getEndpoint() + "/" + minioConfig.getBucketName() + "/" + objectName);
            sysFile.setUploadUserId(SecurityUtils.getUserId());
            sysFile.setUploadUserName(SecurityUtils.getUsername());
            sysFile.setBusinessType(businessType);
            sysFile.setBusinessId(businessId);
            sysFile.setDownloadCount(0);
            sysFile.setIsPublic(0);

            // 7. 保存到数据库
            fileMapper.insert(sysFile);

            log.info("文件上传成功，文件ID：{}，文件名：{}", sysFile.getId(), fileName);
            return sysFile;

        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败：" + e.getMessage());
        }
    }

    @Override
    public void download(Long id, HttpServletResponse response) {
        try {
            // 1. 查询文件信息
            SysFile sysFile = getById(id);

            // 2. 从MinIO下载
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(sysFile.getBucketName())
                            .object(sysFile.getObjectName())
                            .build()
            );

            // 3. 设置响应头
            response.setContentType(sysFile.getContentType());
            response.setHeader("Content-Disposition", "attachment; filename=" +
                    URLEncoder.encode(sysFile.getFileName(), "UTF-8"));

            // 4. 写入响应流
            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            // 5. 增加下载次数
            sysFile.setDownloadCount(sysFile.getDownloadCount() + 1);
            fileMapper.updateById(sysFile);

            log.info("文件下载成功，文件ID：{}，文件名：{}", id, sysFile.getFileName());

        } catch (Exception e) {
            log.error("文件下载失败", e);
            throw new BusinessException("文件下载失败：" + e.getMessage());
        }
    }

    @Override
    public void downloadByObjectName(String objectName, HttpServletResponse response) {
        try {
            // 1. 查询文件信息
            LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysFile::getObjectName, objectName);
            SysFile sysFile = fileMapper.selectOne(wrapper);
            if (sysFile == null) {
                throw new BusinessException("文件不存在");
            }

            // 2. 下载文件
            download(sysFile.getId(), response);

        } catch (Exception e) {
            log.error("文件下载失败", e);
            throw new BusinessException("文件下载失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        try {
            // 1. 查询文件信息
            SysFile sysFile = getById(id);

            // 2. 从MinIO删除
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(sysFile.getBucketName())
                            .object(sysFile.getObjectName())
                            .build()
            );

            // 3. 从数据库删除
            fileMapper.deleteById(id);

            log.info("文件删除成功，文件ID：{}，文件名：{}", id, sysFile.getFileName());

        } catch (Exception e) {
            log.error("文件删除失败", e);
            throw new BusinessException("文件删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    @Override
    public SysFile getById(Long id) {
        SysFile sysFile = fileMapper.selectById(id);
        if (sysFile == null) {
            throw new BusinessException("文件不存在");
        }
        return sysFile;
    }

    @Override
    public IPage<SysFile> page(Integer page, Integer size, String fileName, String businessType, String contentType) {
        Page<SysFile> pageParam = new Page<>(page, size);

        LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(fileName), SysFile::getFileName, fileName)
                .eq(StringUtils.isNotEmpty(businessType), SysFile::getBusinessType, businessType)
                .eq(StringUtils.isNotEmpty(contentType), SysFile::getContentType, contentType)
                .orderByDesc(SysFile::getCreateTime);

        return fileMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public String getFileUrl(Long id) {
        SysFile sysFile = getById(id);
        return sysFile.getFileUrl();
    }

    @Override
    public String getPresignedUrl(Long id, Integer expires) {
        try {
            SysFile sysFile = getById(id);

            // 默认7天过期
            if (expires == null) {
                expires = (int) TimeUnit.DAYS.toSeconds(7);
            }

            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(sysFile.getBucketName())
                            .object(sysFile.getObjectName())
                            .expiry(expires)
                            .build()
            );

        } catch (Exception e) {
            log.error("获取临时URL失败", e);
            throw new BusinessException("获取临时URL失败：" + e.getMessage());
        }
    }

    @Override
    public boolean exists(String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (StringUtils.isEmpty(filename)) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }

    /**
     * 生成对象名称
     */
    private String generateObjectName(String extension) {
        // 使用UUID生成唯一文件名，按日期分目录存储
        LocalDateTime now = LocalDateTime.now();
        String datePath = String.format("%d/%02d/%02d",
                now.getYear(), now.getMonthValue(), now.getDayOfMonth());

        String uuid = UUID.randomUUID().toString().replace("-", "");

        if (StringUtils.isNotEmpty(extension)) {
            return datePath + "/" + uuid + "." + extension;
        } else {
            return datePath + "/" + uuid;
        }
    }
}
