package com.dangcai.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dangcai.business.domain.PurchaseReply;
import com.dangcai.business.dto.PurchaseReplyDTO;
import com.dangcai.business.mapper.PurchaseMatchMapper;
import com.dangcai.business.mapper.PurchaseReplyMapper;
import com.dangcai.business.mapper.PurchaseRequirementMapper;
import com.dangcai.business.service.PurchaseReplyService;
import com.dangcai.business.vo.PurchaseReplyVO;
import com.dangcai.common.exception.BusinessException;
import com.dangcai.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 采购需求回复Service实现
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseReplyServiceImpl implements PurchaseReplyService {

    private final PurchaseReplyMapper purchaseReplyMapper;
    private final PurchaseRequirementMapper purchaseRequirementMapper;
    private final PurchaseMatchMapper purchaseMatchMapper;

    @Override
    public List<PurchaseReplyVO> listByRequirementId(Long requirementId) {
        return purchaseReplyMapper.selectReplyListByRequirementId(requirementId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(PurchaseReplyDTO dto) {
        // 检查需求是否已匹配给该企业
        var match = purchaseMatchMapper.selectByRequirementIdAndEnterpriseId(
                dto.getRequirementId(), dto.getEnterpriseId());
        if (match == null) {
            throw new BusinessException("该需求未匹配给您的企业，无法回复");
        }

        // 检查是否已经回复过
        PurchaseReplyVO existReply = purchaseReplyMapper.selectByRequirementIdAndEnterpriseId(
                dto.getRequirementId(), dto.getEnterpriseId());
        if (existReply != null) {
            throw new BusinessException("您已经回复过该需求");
        }

        PurchaseReply reply = new PurchaseReply();
        BeanUtils.copyProperties(dto, reply);

        // 默认状态为已回复
        reply.setStatus(1);

        reply.setCreateBy(SecurityUtils.getUsername());
        purchaseReplyMapper.insert(reply);

        // 增加需求的回复数量
        purchaseRequirementMapper.increaseReplyCount(dto.getRequirementId());

        // 更新匹配记录的已回复标记
        match.setHasReplied(1);
        purchaseMatchMapper.updateById(match);

        log.info("新增采购需求回复成功，回复ID：{}，需求ID：{}", reply.getId(), dto.getRequirementId());
        return reply.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PurchaseReplyDTO dto) {
        PurchaseReply oldReply = getReplyById(dto.getId());

        // 只能修改已回复状态的回复
        if (oldReply.getStatus() != 1) {
            throw new BusinessException("只能修改已回复状态的回复");
        }

        PurchaseReply reply = new PurchaseReply();
        BeanUtils.copyProperties(dto, reply);
        reply.setUpdateBy(SecurityUtils.getUsername());
        purchaseReplyMapper.updateById(reply);

        log.info("修改采购需求回复成功，回复ID：{}", reply.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PurchaseReply reply = getReplyById(id);

        // 只能删除已回复状态的回复
        if (reply.getStatus() != 1) {
            throw new BusinessException("只能删除已回复状态的回复");
        }

        // 逻辑删除
        reply.setDelFlag(1);
        reply.setUpdateBy(SecurityUtils.getUsername());
        purchaseReplyMapper.updateById(reply);

        // 减少需求的回复数量
        purchaseRequirementMapper.decreaseReplyCount(reply.getRequirementId());

        log.info("删除采购需求回复成功，回复ID：{}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmReply(Long id) {
        PurchaseReply reply = getReplyById(id);

        if (reply.getStatus() != 1) {
            throw new BusinessException("只能确认已回复状态的回复");
        }

        reply.setStatus(2); // 已确认
        reply.setUpdateBy(SecurityUtils.getUsername());
        purchaseReplyMapper.updateById(reply);

        log.info("确认采购需求回复成功，回复ID：{}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeDeal(Long id) {
        PurchaseReply reply = getReplyById(id);

        if (reply.getStatus() != 2) {
            throw new BusinessException("只能对已确认的回复完成交易");
        }

        reply.setStatus(3); // 已完成交易
        reply.setUpdateBy(SecurityUtils.getUsername());
        purchaseReplyMapper.updateById(reply);

        log.info("完成采购需求交易成功，回复ID：{}", id);
    }

    /**
     * 根据ID获取回复实体
     */
    private PurchaseReply getReplyById(Long id) {
        PurchaseReply reply = purchaseReplyMapper.selectById(id);
        if (reply == null || reply.getDelFlag() == 1) {
            throw new BusinessException("回复记录不存在");
        }
        return reply;
    }
}
