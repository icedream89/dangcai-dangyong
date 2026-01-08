package com.dangcai.business.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.domain.PurchaseMatch;
import com.dangcai.business.domain.PurchaseRequirement;
import com.dangcai.business.dto.PurchaseMatchDTO;
import com.dangcai.business.dto.PurchaseRequirementDTO;
import com.dangcai.business.dto.PurchaseRequirementQueryDTO;
import com.dangcai.business.mapper.PurchaseMatchMapper;
import com.dangcai.business.mapper.PurchaseRequirementMapper;
import com.dangcai.business.service.PurchaseMatchService;
import com.dangcai.business.service.PurchaseRequirementService;
import com.dangcai.business.vo.PurchaseMatchVO;
import com.dangcai.business.vo.PurchaseRequirementVO;
import com.dangcai.common.exception.BusinessException;
import com.dangcai.common.utils.SecurityUtils;
import com.dangcai.enterprise.domain.Enterprise;
import com.dangcai.enterprise.mapper.EnterpriseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

/**
 * 采购需求Service实现
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseRequirementServiceImpl implements PurchaseRequirementService {

    private final PurchaseRequirementMapper purchaseRequirementMapper;
    private final PurchaseMatchService purchaseMatchService;
    private final EnterpriseMapper enterpriseMapper;

    @Override
    public Page<PurchaseRequirementVO> page(PurchaseRequirementQueryDTO queryDTO) {
        Page<PurchaseRequirementVO> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        page.setRecords(purchaseRequirementMapper.selectPurchaseRequirementList(
                queryDTO.getRequirementNo(),
                queryDTO.getEnterpriseId(),
                queryDTO.getUserId(),
                queryDTO.getCategory(),
                queryDTO.getStatus(),
                queryDTO.getTitle(),
                queryDTO.getStartTime(),
                queryDTO.getEndTime()
        ));
        return page;
    }

    @Override
    public PurchaseRequirementVO getVOById(Long id) {
        PurchaseRequirementVO vo = purchaseRequirementMapper.selectPurchaseRequirementVOById(id);
        if (vo == null) {
            throw new BusinessException("采购需求不存在");
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(PurchaseRequirementDTO dto) {
        PurchaseRequirement requirement = new PurchaseRequirement();
        BeanUtils.copyProperties(dto, requirement);

        // 生成需求编号：PR + yyyyMMdd + 6位序号
        requirement.setRequirementNo(generateRequirementNo());

        // 初始化浏览次数和回复数量
        requirement.setViewCount(0);
        requirement.setReplyCount(0);

        // 默认状态为待匹配
        requirement.setStatus(1);

        requirement.setCreateBy(SecurityUtils.getUsername());
        purchaseRequirementMapper.insert(requirement);

        log.info("新增采购需求成功，需求ID：{}，需求编号：{}", requirement.getId(), requirement.getRequirementNo());
        return requirement.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PurchaseRequirementDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("需求ID不能为空");
        }

        PurchaseRequirement oldRequirement = getById(dto.getId());

        // 只能修改待匹配状态的需求
        if (oldRequirement.getStatus() != 1) {
            throw new BusinessException("只能修改待匹配状态的需求");
        }

        PurchaseRequirement requirement = new PurchaseRequirement();
        BeanUtils.copyProperties(dto, requirement);
        requirement.setUpdateBy(SecurityUtils.getUsername());
        purchaseRequirementMapper.updateById(requirement);

        log.info("修改采购需求成功，需求ID：{}，需求编号：{}", requirement.getId(), oldRequirement.getRequirementNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PurchaseRequirement requirement = getById(id);

        // 只能删除待匹配状态的需求
        if (requirement.getStatus() != 1) {
            throw new BusinessException("只能删除待匹配状态的需求");
        }

        // 逻辑删除
        requirement.setDelFlag(1);
        requirement.setUpdateBy(SecurityUtils.getUsername());
        purchaseRequirementMapper.updateById(requirement);

        log.info("删除采购需求成功，需求ID：{}，需求编号：{}", id, requirement.getRequirementNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void matchEnterprises(PurchaseMatchDTO dto) {
        PurchaseRequirement requirement = getById(dto.getRequirementId());

        if (requirement.getStatus() != 1) {
            throw new BusinessException("只能匹配待匹配状态的需求");
        }

        // 批量匹配企业
        purchaseMatchService.matchEnterprises(dto);

        // 更新需求状态为已匹配
        requirement.setStatus(2);
        requirement.setUpdateBy(SecurityUtils.getUsername());
        purchaseRequirementMapper.updateById(requirement);

        log.info("匹配供应商企业成功，需求ID：{}，企业数量：{}", dto.getRequirementId(), dto.getEnterpriseIds().size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelMatch(Long requirementId, Long enterpriseId, String cancelReason) {
        PurchaseRequirement requirement = getById(requirementId);

        // 取消匹配
        purchaseMatchService.cancelMatch(requirementId, enterpriseId, cancelReason);

        // 检查是否还有匹配的企业，如果没有则将状态改为待匹配
        List<Long> matchedEnterpriseIds = purchaseMatchMapper.selectMatchedEnterpriseIds(requirementId);
        if (matchedEnterpriseIds == null || matchedEnterpriseIds.isEmpty()) {
            requirement.setStatus(1);
            requirement.setUpdateBy(SecurityUtils.getUsername());
            purchaseRequirementMapper.updateById(requirement);
        }

        log.info("取消匹配成功，需求ID：{}，企业ID：{}", requirementId, enterpriseId);
    }

    @Override
    public List<PurchaseMatchVO> listMatchedEnterprises(Long requirementId) {
        return purchaseMatchService.listByRequirementId(requirementId);
    }

    @Override
    public void increaseViewCount(Long id) {
        purchaseRequirementMapper.increaseViewCount(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        PurchaseRequirement requirement = getById(id);

        requirement.setStatus(status);
        requirement.setUpdateBy(SecurityUtils.getUsername());
        purchaseRequirementMapper.updateById(requirement);

        log.info("修改采购需求状态成功，需求ID：{}，状态：{}", id, status);
    }

    /**
     * 根据ID获取采购需求实体
     */
    private PurchaseRequirement getById(Long id) {
        PurchaseRequirement requirement = purchaseRequirementMapper.selectById(id);
        if (requirement == null || requirement.getDelFlag() == 1) {
            throw new BusinessException("采购需求不存在");
        }
        return requirement;
    }

    /**
     * 生成需求编号
     */
    private String generateRequirementNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.format("%06d", new Random().nextInt(1000000));
        return "PR" + dateStr + randomStr;
    }
}
