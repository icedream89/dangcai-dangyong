package com.dangcai.business.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.domain.Policy;
import com.dangcai.business.dto.PolicyDTO;
import com.dangcai.business.mapper.PolicyMapper;
import com.dangcai.business.service.PolicyService;
import com.dangcai.business.vo.PolicyVO;
import com.dangcai.common.exception.BusinessException;
import com.dangcai.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 政策服务实现
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {

    private final PolicyMapper policyMapper;

    @Override
    public Page<PolicyVO> page(String policyTitle, String policyType,
                               Integer status, Integer isHot, Integer isTop,
                               Integer current, Integer size) {
        Page<PolicyVO> page = new Page<>(current, size);
        page.setRecords(policyMapper.selectPolicyList(
                policyTitle, policyType, status, isHot, isTop
        ));
        return page;
    }

    @Override
    public PolicyVO getVOById(Long id) {
        PolicyVO vo = policyMapper.selectPolicyVOById(id);
        if (vo == null) {
            throw new BusinessException("政策不存在");
        }

        // 设置状态名称
        if (vo.getStatus() != null) {
            vo.setStatusName(vo.getStatus() == 1 ? "已发布" : "下架");
        }

        // 设置是否热门名称
        if (vo.getIsHot() != null) {
            vo.setIsHotName(vo.getIsHot() == 1 ? "是" : "否");
        }

        // 设置是否置顶名称
        if (vo.getIsTop() != null) {
            vo.setIsTopName(vo.getIsTop() == 1 ? "是" : "否");
        }

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(PolicyDTO dto) {
        Policy policy = new Policy();
        BeanUtils.copyProperties(dto, policy);
        policy.setViewCount(0);
        policy.setIsHot(0);
        policy.setIsTop(0);
        policy.setStatus(0); // 默认下架
        policy.setCreateBy(SecurityUtils.getUsername());
        policyMapper.insert(policy);

        log.info("新增政策成功，政策ID：{}，政策标题：{}", policy.getId(), policy.getPolicyTitle());
        return policy.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PolicyDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("政策ID不能为空");
        }

        Policy oldPolicy = getById(dto.getId());

        Policy policy = new Policy();
        BeanUtils.copyProperties(dto, policy);
        policy.setUpdateBy(SecurityUtils.getUsername());
        policyMapper.updateById(policy);

        log.info("修改政策成功，政策ID：{}，政策标题：{}", policy.getId(), policy.getPolicyTitle());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Policy policy = getById(id);

        // 逻辑删除
        policy.setDelFlag(1);
        policy.setUpdateBy(SecurityUtils.getUsername());
        policyMapper.updateById(policy);

        log.info("删除政策成功，政策ID：{}，政策标题：{}", id, policy.getPolicyTitle());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(Long id) {
        Policy policy = getById(id);

        if (policy.getStatus() == 1) {
            throw new BusinessException("政策已发布，无需重复发布");
        }

        policy.setStatus(1);
        policy.setPublishTime(LocalDateTime.now());
        policy.setUpdateBy(SecurityUtils.getUsername());
        policyMapper.updateById(policy);

        log.info("发布政策成功，政策ID：{}，政策标题：{}", id, policy.getPolicyTitle());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unpublish(Long id) {
        Policy policy = getById(id);

        if (policy.getStatus() == 0) {
            throw new BusinessException("政策已下架，无需重复下架");
        }

        policy.setStatus(0);
        policy.setUpdateBy(SecurityUtils.getUsername());
        policyMapper.updateById(policy);

        log.info("取消发布政策成功，政策ID：{}，政策标题：{}", id, policy.getPolicyTitle());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setHot(Long id, Integer isHot) {
        Policy policy = getById(id);

        policy.setIsHot(isHot);
        policy.setUpdateBy(SecurityUtils.getUsername());
        policyMapper.updateById(policy);

        log.info("设置政策热门成功，政策ID：{}，是否热门：{}", id, isHot == 1 ? "是" : "否");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setTop(Long id, Integer isTop) {
        Policy policy = getById(id);

        policy.setIsTop(isTop);
        policy.setUpdateBy(SecurityUtils.getUsername());
        policyMapper.updateById(policy);

        log.info("设置政策置顶成功，政策ID：{}，是否置顶：{}", id, isTop == 1 ? "是" : "否");
    }

    /**
     * 根据ID获取政策实体
     */
    private Policy getById(Long id) {
        Policy policy = policyMapper.selectById(id);
        if (policy == null || policy.getDelFlag() == 1) {
            throw new BusinessException("政策不存在");
        }
        return policy;
    }
}
