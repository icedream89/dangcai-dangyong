package com.dangcai.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.common.exception.BusinessException;
import com.dangcai.common.utils.SecurityUtils;
import com.dangcai.enterprise.domain.Employee;
import com.dangcai.enterprise.dto.EmployeeDTO;
import com.dangcai.enterprise.dto.EmployeeQueryDTO;
import com.dangcai.enterprise.mapper.EmployeeMapper;
import com.dangcai.enterprise.service.EmployeeService;
import com.dangcai.enterprise.vo.EmployeeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工服务实现
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;

    @Override
    public Page<EmployeeVO> page(EmployeeQueryDTO queryDTO) {
        Page<EmployeeVO> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        page.setRecords(employeeMapper.selectEmployeeList(
                queryDTO.getEnterpriseId(),
                queryDTO.getEmployeeName(),
                queryDTO.getPhone(),
                queryDTO.getDepartment(),
                queryDTO.getIsAdmin(),
                queryDTO.getStatus()
        ));
        return page;
    }

    @Override
    public EmployeeVO getVOById(Long id) {
        EmployeeVO vo = employeeMapper.selectEmployeeVOById(id);
        if (vo == null) {
            throw new BusinessException("员工不存在");
        }
        return vo;
    }

    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        if (employee == null || employee.getDelFlag() == 1) {
            throw new BusinessException("员工不存在");
        }
        return employee;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EmployeeDTO dto) {
        // 检查手机号唯一性
        Employee existEmployee = employeeMapper.selectOne(
                new LambdaQueryWrapper<Employee>()
                        .eq(Employee::getPhone, dto.getPhone())
                        .eq(Employee::getEnterpriseId, dto.getEnterpriseId())
        );
        if (existEmployee != null) {
            throw new BusinessException("该手机号已存在");
        }

        // 如果关联了用户ID，检查用户是否已绑定企业
        if (dto.getUserId() != null) {
            Employee existUserEmployee = employeeMapper.selectByUserId(dto.getUserId());
            if (existUserEmployee != null) {
                throw new BusinessException("该用户已绑定其他企业");
            }
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);
        if (employee.getIsAdmin() == null) {
            employee.setIsAdmin(0);
        }
        if (employee.getStatus() == null) {
            employee.setStatus(1);
        }
        employee.setCreateBy(SecurityUtils.getUsername());
        employeeMapper.insert(employee);

        log.info("新增员工成功，员工ID：{}，员工姓名：{}", employee.getId(), employee.getEmployeeName());
        return employee.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EmployeeDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("员工ID不能为空");
        }

        Employee oldEmployee = getById(dto.getId());

        // 检查手机号唯一性（排除自己）
        Employee existEmployee = employeeMapper.selectOne(
                new LambdaQueryWrapper<Employee>()
                        .eq(Employee::getPhone, dto.getPhone())
                        .eq(Employee::getEnterpriseId, dto.getEnterpriseId())
                        .ne(Employee::getId, dto.getId())
        );
        if (existEmployee != null) {
            throw new BusinessException("该手机号已存在");
        }

        // 如果修改了用户ID，检查用户是否已绑定企业
        if (dto.getUserId() != null && !dto.getUserId().equals(oldEmployee.getUserId())) {
            Employee existUserEmployee = employeeMapper.selectByUserId(dto.getUserId());
            if (existUserEmployee != null) {
                throw new BusinessException("该用户已绑定其他企业");
            }
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);
        employee.setUpdateBy(SecurityUtils.getUsername());
        employeeMapper.updateById(employee);

        log.info("修改员工成功，员工ID：{}，员工姓名：{}", employee.getId(), employee.getEmployeeName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Employee employee = getById(id);

        // 逻辑删除
        employee.setDelFlag(1);
        employee.setUpdateBy(SecurityUtils.getUsername());
        employeeMapper.updateById(employee);

        log.info("删除员工成功，员工ID：{}，员工姓名：{}", id, employee.getEmployeeName());
    }

    @Override
    public Employee getByUserId(Long userId) {
        return employeeMapper.selectByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchImport(Long enterpriseId, List<EmployeeDTO> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        List<String> errorMessages = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            EmployeeDTO dto = dataList.get(i);
            try {
                dto.setEnterpriseId(enterpriseId);
                add(dto);
                successCount++;
            } catch (Exception e) {
                errorMessages.add(String.format("第%d行：%s", i + 1, e.getMessage()));
            }
        }

        if (!errorMessages.isEmpty()) {
            log.warn("批量导入员工部分失败，失败原因：{}", errorMessages);
        }

        log.info("批量导入员工完成，成功{}条，失败{}条", successCount, dataList.size() - successCount);
        return successCount;
    }
}
