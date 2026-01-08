package com.dangcai.business.service.impl;

import com.dangcai.business.domain.PurchaseMatch;
import com.dangcai.business.dto.PurchaseMatchDTO;
import com.dangcai.business.mapper.PurchaseMatchMapper;
import com.dangcai.business.mapper.PurchaseRequirementMapper;
import com.dangcai.business.service.PurchaseMatchService;
import com.dangcai.business.vo.PurchaseMatchVO;
import com.dangcai.common.exception.BusinessException;
import com.dangcai.common.utils.SecurityUtils;
import com.dangcai.enterprise.domain.Enterprise;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 需求匹配Service实现
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseMatchServiceImpl implements PurchaseMatchService {

    private final PurchaseMatchMapper purchaseMatchMapper;
    private final PurchaseRequirementMapper purchaseRequirementMapper;
    private final EnterpriseMapper enterpriseMapper;

    @Override
    public List<PurchaseMatchVO> listByRequirementId(Long requirementId) {
        return purchaseMatchMapper.selectMatchListByRequirementId(requirementId);
    }

    @Override
    public Long add(PurchaseMatch match) {
        purchaseMatchMapper.insert(match);
        log.info("新增需求匹配记录成功，匹配ID：{}，需求ID：{}", match.getId(), match.getRequirementId());
        return match.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void matchEnterprises(PurchaseMatchDTO dto) {
        List<Long> enterpriseIds = dto.getEnterpriseIds();
        List<PurchaseMatch> matchList = new ArrayList<>();

        for (Long enterpriseId : enterpriseIds) {
            // 检查是否已经匹配过
            PurchaseMatch existMatch = purchaseMatchMapper.selectByRequirementIdAndEnterpriseId(
                    dto.getRequirementId(), enterpriseId);

            if (existMatch != null) {
                continue; // 已匹配过，跳过
            }

            // 查询企业信息
            Enterprise enterprise = enterpriseMapper.selectById(enterpriseId);
            if (enterprise == null || enterprise.getDelFlag() == 1) {
                throw new BusinessException("企业不存在，企业ID：" + enterpriseId);
            }

            // 创建匹配记录
            PurchaseMatch match = new PurchaseMatch();
            match.setRequirementId(dto.getRequirementId());
            match.setEnterpriseId(enterpriseId);
            match.setEnterpriseName(enterprise.getEnterpriseName());
            match.setOperatorId(SecurityUtils.getUserId());
            match.setOperatorName(SecurityUtils.getUsername());
            match.setMatchTime(LocalDateTime.now());
            match.setHasReplied(0);
            match.setStatus(1); // 已匹配

            matchList.add(match);
        }

        // 批量插入
        if (!matchList.isEmpty()) {
            matchList.forEach(purchaseMatchMapper::insert);
        }

        log.info("批量匹配企业成功，需求ID：{}，匹配数量：{}", dto.getRequirementId(), matchList.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelMatch(Long requirementId, Long enterpriseId, String cancelReason) {
        PurchaseMatch match = getByRequirementIdAndEnterpriseId(requirementId, enterpriseId);

        match.setStatus(2); // 已取消
        match.setCancelTime(LocalDateTime.now());
        match.setCancelReason(cancelReason);
        match.setUpdateBy(SecurityUtils.getUsername());
        purchaseMatchMapper.updateById(match);

        log.info("取消需求匹配成功，需求ID：{}，企业ID：{}", requirementId, enterpriseId);
    }

    @Override
    public PurchaseMatch getByRequirementIdAndEnterpriseId(Long requirementId, Long enterpriseId) {
        PurchaseMatch match = purchaseMatchMapper.selectByRequirementIdAndEnterpriseId(requirementId, enterpriseId);
        if (match == null) {
            throw new BusinessException("匹配记录不存在");
        }
        return match;
    }
}
