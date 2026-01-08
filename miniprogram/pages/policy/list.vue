<template>
  <view class="policy-list-page">
    <!-- 筛选栏 -->
    <view class="filter-bar">
      <view
        class="filter-item"
        v-for="(item, index) in filters"
        :key="index"
        :class="{ active: currentFilter === index }"
        @click="switchFilter(index)"
      >
        {{ item.name }}
      </view>
    </view>

    <!-- 政策列表 -->
    <view class="policy-list">
      <view
        class="policy-item card"
        v-for="item in policyList"
        :key="item.id"
        @click="goDetail(item.id)"
      >
        <view class="policy-header">
          <text class="policy-title">{{ item.policyTitle }}</text>
          <view class="policy-badge" v-if="item.isTop">置顶</view>
          <view class="policy-badge recommend" v-if="item.isRecommended && !item.isTop">推荐</view>
        </view>
        <view class="policy-meta">
          <text class="policy-type">{{ item.policyType }}</text>
          <text class="policy-dept">{{ item.issueDept }}</text>
        </view>
        <text class="policy-summary">{{ item.summary }}</text>
        <view class="policy-footer">
          <text class="policy-date">📅 {{ item.issueDate }}</text>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="policyList.length === 0">
      <text class="empty-icon">📄</text>
      <text class="empty-text">暂无政策信息</text>
    </view>
  </view>
</template>

<script>
import { mockPolicies } from '@/api/index.js'

export default {
  data() {
    return {
      filters: [
        { name: '全部', type: '' },
        { name: '国家级', type: '国家级' },
        { name: '省级', type: '省级' },
        { name: '市级', type: '市级' }
      ],
      currentFilter: 0,
      policyList: []
    }
  },
  onLoad() {
    this.loadPolicies()
  },
  methods: {
    loadPolicies() {
      let list = [...mockPolicies]
      const currentType = this.filters[this.currentFilter].type
      if (currentType) {
        list = list.filter(item => item.policyType === currentType)
      }
      this.policyList = list
    },

    switchFilter(index) {
      this.currentFilter = index
      this.loadPolicies()
    },

    goDetail(id) {
      uni.navigateTo({
        url: '/pages/policy/detail?id=' + id
      })
    }
  }
}
</script>

<style scoped lang="scss">
.policy-list-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.filter-bar {
  display: flex;
  background: #fff;
  padding: 20rpx;
  gap: 20rpx;
  overflow-x: auto;
  white-space: nowrap;

  .filter-item {
    flex-shrink: 0;
    padding: 12rpx 32rpx;
    background: #f0f0f0;
    border-radius: 50rpx;
    font-size: 28rpx;
    color: #666;
    transition: all 0.3s;

    &.active {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
    }
  }
}

.policy-list {
  padding: 20rpx;

  .policy-item {
    .policy-header {
      display: flex;
      align-items: flex-start;
      gap: 12rpx;
      margin-bottom: 16rpx;

      .policy-title {
        flex: 1;
        font-size: 30rpx;
        font-weight: bold;
        color: #333;
        line-height: 1.5;
      }

      .policy-badge {
        flex-shrink: 0;
        padding: 4rpx 12rpx;
        border-radius: 8rpx;
        font-size: 20rpx;
        color: #fff;
        background: linear-gradient(135deg, #f5576c 0%, #f093fb 100%);

        &.recommend {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }
    }

    .policy-meta {
      display: flex;
      gap: 16rpx;
      margin-bottom: 12rpx;

      .policy-type {
        font-size: 24rpx;
        color: #667eea;
        background: rgba(102, 126, 234, 0.1);
        padding: 4rpx 12rpx;
        border-radius: 8rpx;
      }

      .policy-dept {
        font-size: 24rpx;
        color: #999;
      }
    }

    .policy-summary {
      font-size: 26rpx;
      color: #666;
      line-height: 1.6;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
      margin-bottom: 12rpx;
    }

    .policy-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .policy-date {
        font-size: 24rpx;
        color: #999;
      }
    }
  }
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 0;

  .empty-icon {
    font-size: 120rpx;
    margin-bottom: 20rpx;
  }

  .empty-text {
    font-size: 28rpx;
    color: #999;
  }
}
</style>
