package com.dangcai.enterprise.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.common.result.Result;
import com.dangcai.enterprise.dto.EmployeeDTO;
import com.dangcai.enterprise.dto.EmployeeQueryDTO;
import com.dangcai.enterprise.service.EmployeeService;
import com.dangcai.enterprise.vo.EmployeeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工管理Controller
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Tag(name = "员工管理", description = "员工管理相关接口")
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * 分页查询员工列表
     */
    @Operation(summary = "分页查询员工列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('enterprise:employee:list')")
    public Result<Page<EmployeeVO>> page(EmployeeQueryDTO queryDTO) {
        Page<EmployeeVO> page = employeeService.page(queryDTO);
        return Result.success(page);
    }

    /**
     * 根据ID查询员工详情
     */
    @Operation(summary = "根据ID查询员工详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('enterprise:employee:query')")
    public Result<EmployeeVO> getById(@PathVariable Long id) {
        EmployeeVO vo = employeeService.getVOById(id);
        return Result.success(vo);
    }

    /**
     * 新增员工
     */
    @Operation(summary = "新增员工")
    @PostMapping
    @PreAuthorize("hasAuthority('enterprise:employee:add')")
    public Result<Long> add(@Validated(EmployeeDTO.Add.class) @RequestBody EmployeeDTO dto) {
        Long id = employeeService.add(dto);
        return Result.success(id, "新增员工成功");
    }

    /**
     * 修改员工
     */
    @Operation(summary = "修改员工")
    @PutMapping
    @PreAuthorize("hasAuthority('enterprise:employee:edit')")
    public Result<Void> update(@Validated(EmployeeDTO.Update.class) @RequestBody EmployeeDTO dto) {
        employeeService.update(dto);
        return Result.success("修改员工成功");
    }

    /**
     * 删除员工
     */
    @Operation(summary = "删除员工")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('enterprise:employee:remove')")
    public Result<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return Result.success("删除员工成功");
    }

    /**
     * 批量导入员工
     */
    @Operation(summary = "批量导入员工")
    @PostMapping("/batch-import")
    @PreAuthorize("hasAuthority('enterprise:employee:import')")
    public Result<String> batchImport(
            @RequestParam Long enterpriseId,
            @RequestBody List<EmployeeDTO> dataList) {
        int count = employeeService.batchImport(enterpriseId, dataList);
        return Result.success(String.format("成功导入%d条员工数据", count));
    }
}
