package com.dangcai.business.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.dto.CourseDTO;
import com.dangcai.business.service.CourseService;
import com.dangcai.business.vo.CourseVO;
import com.dangcai.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 企业课堂管理Controller
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Tag(name = "企业课堂管理", description = "企业课堂管理相关接口")
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    /**
     * 分页查询课堂列表
     */
    @Operation(summary = "分页查询课堂列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('business:course:list')")
    public Result<Page<CourseVO>> page(
            @RequestParam(required = false) String courseTitle,
            @RequestParam(required = false) String courseType,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer isRecommended,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<CourseVO> page = courseService.page(
                courseTitle, courseType, category, isRecommended, status, current, size
        );
        return Result.success(page);
    }

    /**
     * 根据ID查询课堂详情
     */
    @Operation(summary = "根据ID查询课堂详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('business:course:query')")
    public Result<CourseVO> getById(@PathVariable Long id) {
        CourseVO vo = courseService.getVOById(id);
        return Result.success(vo);
    }

    /**
     * 新增课堂
     */
    @Operation(summary = "新增课堂")
    @PostMapping
    @PreAuthorize("hasAuthority('business:course:add')")
    public Result<Long> add(@Validated @RequestBody CourseDTO dto) {
        Long id = courseService.add(dto);
        return Result.success(id, "新增课堂成功");
    }

    /**
     * 修改课堂
     */
    @Operation(summary = "修改课堂")
    @PutMapping
    @PreAuthorize("hasAuthority('business:course:edit')")
    public Result<Void> update(@Validated @RequestBody CourseDTO dto) {
        courseService.update(dto);
        return Result.success("修改课堂成功");
    }

    /**
     * 删除课堂
     */
    @Operation(summary = "删除课堂")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('business:course:remove')")
    public Result<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return Result.success("删除课堂成功");
    }

    /**
     * 发布课堂
     */
    @Operation(summary = "发布课堂")
    @PutMapping("/publish/{id}")
    @PreAuthorize("hasAuthority('business:course:publish')")
    public Result<Void> publish(@PathVariable Long id) {
        courseService.publish(id);
        return Result.success("发布课堂成功");
    }

    /**
     * 取消发布课堂
     */
    @Operation(summary = "取消发布课堂")
    @PutMapping("/unpublish/{id}")
    @PreAuthorize("hasAuthority('business:course:unpublish')")
    public Result<Void> unpublish(@PathVariable Long id) {
        courseService.unpublish(id);
        return Result.success("取消发布课堂成功");
    }

    /**
     * 设置推荐
     */
    @Operation(summary = "设置推荐")
    @PutMapping("/recommend/{id}")
    @PreAuthorize("hasAuthority('business:course:edit')")
    public Result<Void> setRecommended(
            @PathVariable Long id,
            @RequestParam Integer isRecommended) {
        courseService.setRecommended(id, isRecommended);
        return Result.success("设置推荐成功");
    }
}
