package com.dangcai.miniapp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.common.domain.Result;
import com.dangcai.enterprise.domain.Enterprise;
import com.dangcai.enterprise.service.EnterpriseService;
import com.dangcai.enterprise.vo.EnterpriseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 小程序企业接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Tag(name = "小程序-企业", description = "小程序端企业相关接口")
@RestController
@RequestMapping("/miniapp-api/enterprise")
@RequiredArgsConstructor
public class MiniappEnterpriseController {

    private final EnterpriseService enterpriseService;

    /**
     * 分页查询企业列表（公开）
     */
    @Operation(summary = "分页查询企业列表")
    @GetMapping("/page")
    public Result<IPage<EnterpriseVO>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String enterpriseName,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) Integer isRecommended) {

        Page<EnterpriseVO> page = new Page<>(current, size);
        page.setRecords(enterpriseService.getEnterpriseList(
                enterpriseName, industry, isRecommended, null, null,
                current, size
        ));
        return Result.success(page);
    }

    /**
     * 查询企业详情
     */
    @Operation(summary = "查询企业详情")
    @GetMapping("/{id}")
    public Result<EnterpriseVO> getById(@PathVariable Long id) {
        EnterpriseVO vo = enterpriseService.getVOById(id);
        // 增加浏览次数
        enterpriseService.increaseViewCount(id);
        return Result.success(vo);
    }

    /**
     * 查询推荐企业
     */
    @Operation(summary = "查询推荐企业")
    @GetMapping("/recommended")
    public Result<java.util.List<EnterpriseVO>> getRecommended(
            @RequestParam(defaultValue = "6") Integer limit) {
        java.util.List<EnterpriseVO> list = enterpriseService.getRecommendedEnterprises(limit);
        return Result.success(list);
    }

    /**
     * 搜索企业
     */
    @Operation(summary = "搜索企业")
    @GetMapping("/search")
    public Result<IPage<EnterpriseVO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<EnterpriseVO> page = new Page<>(current, size);
        page.setRecords(enterpriseService.getEnterpriseList(
                keyword, null, null, null, null,
                current, size
        ));
        return Result.success(page);
    }
}
