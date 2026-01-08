package com.dangcai.enterprise.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.enterprise.domain.Employee;
import com.dangcai.enterprise.dto.EmployeeDTO;
import com.dangcai.enterprise.dto.EmployeeQueryDTO;
import com.dangcai.enterprise.vo.EmployeeVO;

/**
 * 员工服务接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
public interface EmployeeService {

    /**
     * 分页查询员工列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<EmployeeVO> page(EmployeeQueryDTO queryDTO);

    /**
     * 根据ID查询员工详情
     *
     * @param id 员工ID
     * @return 员工VO
     */
    EmployeeVO getVOById(Long id);

    /**
     * 根据ID查询员工实体
     *
     * @param id 员工ID
     * @return 员工实体
     */
    Employee getById(Long id);

    /**
     * 新增员工
     *
     * @param dto 员工DTO
     * @return 员工ID
     */
    Long add(EmployeeDTO dto);

    /**
     * 修改员工
     *
     * @param dto 员工DTO
     */
    void update(EmployeeDTO dto);

    /**
     * 删除员工
     *
     * @param id 员工ID
     */
    void delete(Long id);

    /**
     * 根据用户ID查询员工
     *
     * @param userId 用户ID
     * @return 员工
     */
    Employee getByUserId(Long userId);

    /**
     * 批量导入员工
     *
     * @param enterpriseId 企业ID
     * @param dataList     员工数据列表
     * @return 导入成功数量
     */
    int batchImport(Long enterpriseId, java.util.List<EmployeeDTO> dataList);
}
