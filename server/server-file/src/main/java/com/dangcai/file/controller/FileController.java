package com.dangcai.file.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dangcai.common.domain.Result;
import com.dangcai.file.domain.SysFile;
import com.dangcai.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件管理Controller
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Tag(name = "文件管理", description = "文件上传、下载、删除等接口")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 上传文件
     */
    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public Result<SysFile> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "businessType", required = false) String businessType,
            @RequestParam(value = "businessId", required = false) Long businessId) {
        SysFile sysFile = fileService.upload(file, businessType, businessId);
        return Result.success("上传成功", sysFile);
    }

    /**
     * 批量上传文件
     */
    @Operation(summary = "批量上传文件")
    @PostMapping("/upload/batch")
    public Result<Object> uploadBatch(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "businessType", required = false) String businessType,
            @RequestParam(value = "businessId", required = false) Long businessId) {
        if (files == null || files.length == 0) {
            return Result.error("请选择文件");
        }

        int successCount = 0;
        int failCount = 0;

        for (MultipartFile file : files) {
            try {
                fileService.upload(file, businessType, businessId);
                successCount++;
            } catch (Exception e) {
                log.error("文件上传失败：{}", file.getOriginalFilename(), e);
                failCount++;
            }
        }

        return Result.success(String.format("上传完成，成功%d个，失败%d个", successCount, failCount));
    }

    /**
     * 下载文件
     */
    @Operation(summary = "下载文件")
    @GetMapping("/download/{id}")
    public void download(@PathVariable Long id, HttpServletResponse response) {
        fileService.download(id, response);
    }

    /**
     * 删除文件
     */
    @Operation(summary = "删除文件")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:file:remove')")
    public Result<Void> delete(@PathVariable Long id) {
        fileService.delete(id);
        return Result.success("删除成功");
    }

    /**
     * 批量删除文件
     */
    @Operation(summary = "批量删除文件")
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('system:file:remove')")
    public Result<Void> deleteBatch(@RequestBody Long[] ids) {
        fileService.deleteBatch(ids);
        return Result.success("删除成功");
    }

    /**
     * 根据ID查询文件信息
     */
    @Operation(summary = "根据ID查询文件信息")
    @GetMapping("/{id}")
    public Result<SysFile> getById(@PathVariable Long id) {
        SysFile sysFile = fileService.getById(id);
        return Result.success(sysFile);
    }

    /**
     * 分页查询文件列表
     */
    @Operation(summary = "分页查询文件列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('system:file:list')")
    public Result<IPage<SysFile>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String fileName,
            @RequestParam(required = false) String businessType,
            @RequestParam(required = false) String contentType) {
        IPage<SysFile> pageResult = fileService.page(page, size, fileName, businessType, contentType);
        return Result.success(pageResult);
    }

    /**
     * 获取文件访问URL
     */
    @Operation(summary = "获取文件访问URL")
    @GetMapping("/url/{id}")
    public Result<String> getFileUrl(@PathVariable Long id) {
        String url = fileService.getFileUrl(id);
        return Result.success(url);
    }

    /**
     * 获取临时访问URL（带签名）
     */
    @Operation(summary = "获取临时访问URL")
    @GetMapping("/presigned/{id}")
    public Result<String> getPresignedUrl(
            @PathVariable Long id,
            @RequestParam(defaultValue = "604800") Integer expires) {
        String url = fileService.getPresignedUrl(id, expires);
        return Result.success(url);
    }

    /**
     * 判断文件是否存在
     */
    @Operation(summary = "判断文件是否存在")
    @GetMapping("/exists")
    public Result<Boolean> exists(@RequestParam String objectName) {
        boolean exists = fileService.exists(objectName);
        return Result.success(exists);
    }
}
