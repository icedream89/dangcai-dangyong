package com.dangcai.enterprise.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.common.exception.BusinessException;
import com.dangcai.common.utils.SecurityUtils;
import com.dangcai.enterprise.domain.Enterprise;
import com.dangcai.enterprise.domain.EnterpriseLog;
import com.dangcai.enterprise.dto.EnterpriseDTO;
import com.dangcai.enterprise.dto.EnterpriseQueryDTO;
import com.dangcai.enterprise.mapper.EnterpriseMapper;
import com.dangcai.enterprise.service.EnterpriseLogService;
import com.dangcai.enterprise.service.EnterpriseService;
import com.dangcai.enterprise.vo.EnterpriseVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 企业服务实现
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EnterpriseServiceImpl implements EnterpriseService {

    private final EnterpriseMapper enterpriseMapper;
    private final EnterpriseLogService enterpriseLogService;
    private final ObjectMapper objectMapper;

    @Override
    public Page<EnterpriseVO> page(EnterpriseQueryDTO queryDTO) {
        Page<EnterpriseVO> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        page.setRecords(enterpriseMapper.selectEnterpriseList(
                queryDTO.getEnterpriseName(),
                queryDTO.getUnifiedCode(),
                queryDTO.getIndustry(),
                queryDTO.getEnterpriseType(),
                queryDTO.getScale(),
                queryDTO.getStatus(),
                queryDTO.getIsRecommended()
        ));
        return page;
    }

    @Override
    public EnterpriseVO getVOById(Long id) {
        EnterpriseVO vo = enterpriseMapper.selectEnterpriseVOById(id);
        if (vo == null) {
            throw new BusinessException("企业不存在");
        }
        return vo;
    }

    @Override
    public Enterprise getById(Long id) {
        Enterprise enterprise = enterpriseMapper.selectById(id);
        if (enterprise == null || enterprise.getDelFlag() == 1) {
            throw new BusinessException("企业不存在");
        }
        return enterprise;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EnterpriseDTO dto) {
        // 检查统一社会信用代码唯一性
        Enterprise existEnterprise = enterpriseMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Enterprise>()
                        .eq(Enterprise::getUnifiedCode, dto.getUnifiedCode())
        );
        if (existEnterprise != null) {
            throw new BusinessException("统一社会信用代码已存在");
        }

        Enterprise enterprise = new Enterprise();
        BeanUtils.copyProperties(dto, enterprise);
        enterprise.setIsRecommended(0);
        enterprise.setViewCount(0);
        enterprise.setStatus(0); // 默认待审核
        enterprise.setCreateBy(SecurityUtils.getUsername());
        enterpriseMapper.insert(enterprise);

        // 记录变更日志
        recordLog(enterprise.getId(), "新增", convertToJson(dto), null);

        log.info("新增企业成功，企业ID：{}，企业名称：{}", enterprise.getId(), enterprise.getEnterpriseName());
        return enterprise.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EnterpriseDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("企业ID不能为空");
        }

        Enterprise oldEnterprise = getById(dto.getId());

        // 检查统一社会信用代码唯一性（排除自己）
        Enterprise existEnterprise = enterpriseMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Enterprise>()
                        .eq(Enterprise::getUnifiedCode, dto.getUnifiedCode())
                        .ne(Enterprise::getId, dto.getId())
        );
        if (existEnterprise != null) {
            throw new BusinessException("统一社会信用代码已存在");
        }

        Enterprise enterprise = new Enterprise();
        BeanUtils.copyProperties(dto, enterprise);
        enterprise.setUpdateBy(SecurityUtils.getUsername());
        enterpriseMapper.updateById(enterprise);

        // 记录变更日志
        recordLog(enterprise.getId(), "修改", convertToJson(dto), null);

        log.info("修改企业成功，企业ID：{}，企业名称：{}", enterprise.getId(), enterprise.getEnterpriseName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Enterprise enterprise = getById(id);

        // 逻辑删除
        enterprise.setDelFlag(1);
        enterprise.setUpdateBy(SecurityUtils.getUsername());
        enterpriseMapper.updateById(enterprise);

        // 记录变更日志
        recordLog(id, "删除", convertToJson(enterprise), null);

        log.info("删除企业成功，企业ID：{}，企业名称：{}", id, enterprise.getEnterpriseName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(Long id, Integer status, String remark) {
        Enterprise enterprise = getById(id);

        if (enterprise.getStatus() != 0) {
            throw new BusinessException("只能审核待审核状态的企业");
        }

        if (status != 1 && status != 2) {
            throw new BusinessException("审核状态不正确");
        }

        enterprise.setStatus(status);
        enterprise.setAuditRemark(remark);
        enterprise.setUpdateBy(SecurityUtils.getUsername());
        enterpriseMapper.updateById(enterprise);

        // 记录变更日志
        recordLog(id, "审核",
                String.format("审核状态：%s，审核备注：%s",
                        status == 1 ? "通过" : "不通过", remark), null);

        log.info("审核企业成功，企业ID：{}，审核状态：{}", id, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        Enterprise enterprise = getById(id);

        enterprise.setStatus(status);
        enterprise.setUpdateBy(SecurityUtils.getUsername());
        enterpriseMapper.updateById(enterprise);

        // 记录变更日志
        recordLog(id, "状态变更", String.format("状态变更为：%s", getStatusName(status)), null);

        log.info("修改企业状态成功，企业ID：{}，状态：{}", id, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setRecommended(Long id, Integer isRecommended) {
        Enterprise enterprise = getById(id);

        enterprise.setIsRecommended(isRecommended);
        enterprise.setUpdateBy(SecurityUtils.getUsername());
        enterpriseMapper.updateById(enterprise);

        log.info("设置企业推荐成功，企业ID：{}，是否推荐：{}", id, isRecommended);
    }

    @Override
    public void increaseViewCount(Long id) {
        enterpriseMapper.increaseViewCount(id);
    }

    /**
     * 记录变更日志
     */
    private void recordLog(Long enterpriseId, String operationType, String operationContent, String remark) {
        EnterpriseLog log = new EnterpriseLog();
        log.setEnterpriseId(enterpriseId);
        log.setOperationType(operationType);
        log.setOperationContent(operationContent);
        log.setOperator(SecurityUtils.getUsername());
        log.setRemark(remark);
        enterpriseLogService.add(log);
    }

    /**
     * 转换为JSON字符串
     */
    private String convertToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("转换为JSON失败", e);
            return "{}";
        }
    }

    /**
     * 获取状态名称
     */
    private String getStatusName(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 0:
                return "待审核";
            case 1:
                return "正常";
            case 2:
                return "禁用";
            default:
                return "未知";
        }
    }
}
