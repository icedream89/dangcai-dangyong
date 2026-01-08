package com.dangcai.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.enterprise.domain.Employee;
import com.dangcai.enterprise.vo.EmployeeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工Mapper
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 查询员工列表（含企业名称、用户名）
     *
     * @param enterpriseId  企业ID
     * @param employeeName  员工姓名
     * @param phone         手机号
     * @param department    部门
     * @param isAdmin       是否企业管理员
     * @param status        状态
     * @return 员工VO列表
     */
    List<EmployeeVO> selectEmployeeList(
            @Param("enterpriseId") Long enterpriseId,
            @Param("employeeName") String employeeName,
            @Param("phone") String phone,
            @Param("department") String department,
            @Param("isAdmin") Integer isAdmin,
            @Param("status") Integer status
    );

    /**
     * 根据ID查询员工VO
     *
     * @param id 员工ID
     * @return 员工VO
     */
    EmployeeVO selectEmployeeVOById(@Param("id") Long id);

    /**
     * 根据用户ID查询员工
     *
     * @param userId 用户ID
     * @return 员工
     */
    Employee selectByUserId(@Param("userId") Long userId);

    /**
     * 查询企业的员工数量
     *
     * @param enterpriseId 企业ID
     * @return 员工数量
     */
    int countByEnterpriseId(@Param("enterpriseId") Long enterpriseId);
}
