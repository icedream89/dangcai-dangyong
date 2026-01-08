<template>
  <view class="policy-detail-page">
    <view class="detail-container card">
      <view class="detail-header">
        <text class="detail-title">{{ policyInfo.policyTitle }}</text>
        <view class="detail-badges">
          <view class="badge" v-if="policyInfo.isTop">📌 置顶</view>
          <view class="badge" v-if="policyInfo.isRecommended">⭐ 推荐</view>
        </view>
      </view>

      <view class="detail-meta">
        <view class="meta-item">
          <text class="meta-label">发文号</text>
          <text class="meta-value">{{ policyInfo.policyNo }}</text>
        </view>
        <view class="meta-item">
          <text class="meta-label">级别</text>
          <text class="meta-value tag">{{ policyInfo.policyType }}</text>
        </view>
        <view class="meta-item">
          <text class="meta-label">发文部门</text>
          <text class="meta-value">{{ policyInfo.issueDept }}</text>
        </view>
        <view class="meta-item">
          <text class="meta-label">发布日期</text>
          <text class="meta-value">{{ policyInfo.issueDate }}</text>
        </view>
      </view>

      <view class="detail-divider"></view>

      <view class="detail-section">
        <text class="section-title">📋 政策摘要</text>
        <text class="section-content">{{ policyInfo.summary }}</text>
      </view>

      <view class="detail-section">
        <text class="section-title">📄 政策内容</text>
        <text class="section-content">{{ policyInfo.content }}</text>
      </view>
    </view>

    <view class="detail-actions">
      <button class="action-btn primary" @click="handleCollect">⭐ 收藏</button>
      <button class="action-btn" @click="handleShare">📤 分享</button>
    </view>
  </view>
</template>

<script>
import { mockPolicies } from '@/api/index.js'

export default {
  data() {
    return {
      id: null,
      policyInfo: {}
    }
  },
  onLoad(options) {
    this.id = parseInt(options.id)
    this.loadDetail()
  },
  methods: {
    loadDetail() {
      this.policyInfo = mockPolicies.find(p => p.id === this.id) || {}
    },

    handleCollect() {
      uni.showToast({
        title: '收藏成功',
        icon: 'success'
      })
    },

    handleShare() {
      uni.showShareMenu({
        withShareTicket: true
      })
    }
  }
}
</script>

<style scoped lang="scss">
.policy-detail-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
  padding-bottom: 120rpx;
}

.detail-container {
  .detail-header {
    margin-bottom: 24rpx;

    .detail-title {
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
      line-height: 1.5;
      display: block;
      margin-bottom: 16rpx;
    }

    .detail-badges {
      display: flex;
      gap: 12rpx;

      .badge {
        font-size: 24rpx;
        color: #667eea;
        background: rgba(102, 126, 234, 0.1);
        padding: 6rpx 16rpx;
        border-radius: 8rpx;
      }
    }
  }

  .detail-meta {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 16rpx;
    margin-bottom: 24rpx;

    .meta-item {
      display: flex;
      flex-direction: column;
      gap: 8rpx;

      .meta-label {
        font-size: 24rpx;
        color: #999;
      }

      .meta-value {
        font-size: 28rpx;
        color: #333;

        &.tag {
          color: #667eea;
          background: rgba(102, 126, 234, 0.1);
          padding: 4rpx 12rpx;
          border-radius: 8rpx;
          text-align: center;
        }
      }
    }
  }

  .detail-divider {
    height: 1rpx;
    background: #e6e6e6;
    margin: 24rpx 0;
  }

  .detail-section {
    margin-bottom: 32rpx;

    .section-title {
      font-size: 30rpx;
      font-weight: bold;
      color: #333;
      display: block;
      margin-bottom: 16rpx;
    }

    .section-content {
      font-size: 28rpx;
      color: #666;
      line-height: 1.8;
    }
  }
}

.detail-actions {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  gap: 20rpx;
  padding: 20rpx;
  background: #fff;
  box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.05);

  .action-btn {
    flex: 1;
    height: 88rpx;
    border-radius: 50rpx;
    font-size: 32rpx;
    border: none;
    background: #f0f0f0;
    color: #333;

    &.primary {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
    }
  }
}
</style>
