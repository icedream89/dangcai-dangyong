<template>
  <view class="enterprise-list-page">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <view class="search-input">
        <text class="search-icon">🔍</text>
        <input type="text" placeholder="搜索企业名称" v-model="searchText" @confirm="handleSearch" />
      </view>
    </view>

    <!-- 企业分类 -->
    <view class="category-section">
      <scroll-view scroll-x class="category-scroll" show-scrollbar="false">
        <view
          class="category-item"
          :class="{ active: selectedCategoryId === item.id }"
          v-for="item in categories"
          :key="item.id"
          @click="selectCategory(item.id)"
        >
          <text class="category-icon">{{ item.icon }}</text>
          <text class="category-name">{{ item.name }}</text>
          <text class="category-count">{{ item.count }}</text>
        </view>
      </scroll-view>
    </view>

    <!-- 企业列表 -->
    <view class="enterprise-list">
      <view
        class="enterprise-item card"
        v-for="item in filteredEnterpriseList"
        :key="item.id"
        @click="goDetail(item.id)"
      >
        <view class="enterprise-header">
          <image :src="item.logo" mode="aspectFill" class="enterprise-logo" />
          <view class="enterprise-info">
            <text class="enterprise-name">{{ item.enterpriseName }}</text>
            <text class="enterprise-meta">{{ item.industry }} · {{ item.scale }}</text>
          </view>
          <view class="recommend-badge" v-if="item.isRecommended">⭐</view>
        </view>
        <text class="enterprise-intro">{{ item.intro }}</text>
        <view class="enterprise-tags">
          <text class="tag" v-for="(tag, idx) in item.tags" :key="idx">{{ tag }}</text>
        </view>
      </view>

      <!-- 空状态 -->
      <view class="empty-state" v-if="filteredEnterpriseList.length === 0">
        <text class="empty-icon">🏢</text>
        <text class="empty-text">暂无相关企业</text>
      </view>
    </view>
  </view>
</template>

<script>
import { mockEnterprises, mockEnterpriseCategories } from '@/api/index.js'

export default {
  data() {
    return {
      searchText: '',
      selectedCategoryId: 1, // 默认选中"全部"
      categories: [],
      enterpriseList: []
    }
  },
  computed: {
    // 根据选中的分类和搜索词过滤企业列表
    filteredEnterpriseList() {
      let result = [...this.enterpriseList]

      // 根据分类过滤
      if (this.selectedCategoryId !== 1) {
        const categoryName = this.categories.find(c => c.id === this.selectedCategoryId)?.name
        result = result.filter(e => e.industry === categoryName)
      }

      // 根据搜索词过滤
      if (this.searchText) {
        result = result.filter(e =>
          e.enterpriseName.includes(this.searchText)
        )
      }

      return result
    }
  },
  onLoad() {
    this.loadCategories()
    this.loadEnterprises()
  },
  methods: {
    loadCategories() {
      this.categories = [...mockEnterpriseCategories]
    },

    loadEnterprises() {
      this.enterpriseList = [...mockEnterprises]
    },

    selectCategory(id) {
      this.selectedCategoryId = id
    },

    handleSearch() {
      // 搜索逻辑已移至computed属性filteredEnterpriseList
    },

    goDetail(id) {
      uni.navigateTo({
        url: '/pages/enterprise/detail?id=' + id
      })
    }
  }
}
</script>

<style scoped lang="scss">
.enterprise-list-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.search-bar {
  padding: 20rpx;
  background: #fff;

  .search-input {
    display: flex;
    align-items: center;
    gap: 12rpx;
    padding: 16rpx 24rpx;
    background: #f5f5f5;
    border-radius: 50rpx;

    .search-icon {
      font-size: 32rpx;
    }

    input {
      flex: 1;
      font-size: 28rpx;
    }
  }
}

.category-section {
  background: #fff;
  padding: 20rpx 0;
  margin-bottom: 20rpx;

  .category-scroll {
    white-space: nowrap;
    padding: 0 20rpx;

    .category-item {
      display: inline-flex;
      flex-direction: column;
      align-items: center;
      gap: 8rpx;
      padding: 16rpx 24rpx;
      margin-right: 16rpx;
      background: #f8f8f8;
      border-radius: 16rpx;
      min-width: 140rpx;
      transition: all 0.3s;

      &.active {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        box-shadow: 0 4rpx 12rpx rgba(102, 126, 234, 0.3);

        .category-icon,
        .category-name,
        .category-count {
          color: #fff;
        }
      }

      .category-icon {
        font-size: 40rpx;
        margin-bottom: 4rpx;
      }

      .category-name {
        font-size: 26rpx;
        color: #333;
        font-weight: 500;
      }

      .category-count {
        font-size: 22rpx;
        color: #999;
      }
    }
  }
}

.enterprise-list {
  padding: 20rpx;

  .enterprise-item {
    .enterprise-header {
      display: flex;
      gap: 16rpx;
      margin-bottom: 16rpx;
      position: relative;

      .enterprise-logo {
        width: 100rpx;
        height: 100rpx;
        border-radius: 16rpx;
        flex-shrink: 0;
      }

      .enterprise-info {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: center;
        gap: 8rpx;

        .enterprise-name {
          font-size: 30rpx;
          font-weight: bold;
          color: #333;
        }

        .enterprise-meta {
          font-size: 24rpx;
          color: #999;
        }
      }

      .recommend-badge {
        position: absolute;
        top: 0;
        right: 0;
        font-size: 32rpx;
      }
    }

    .enterprise-intro {
      font-size: 26rpx;
      color: #666;
      line-height: 1.6;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
      margin-bottom: 12rpx;
    }

    .enterprise-tags {
      display: flex;
      gap: 12rpx;
      flex-wrap: wrap;

      .tag {
        font-size: 24rpx;
        color: #667eea;
        background: rgba(102, 126, 234, 0.1);
        padding: 4rpx 12rpx;
        border-radius: 8rpx;
      }
    }
  }

  .empty-state {
    text-align: center;
    padding: 120rpx 40rpx;

    .empty-icon {
      font-size: 120rpx;
      display: block;
      margin-bottom: 20rpx;
      opacity: 0.3;
    }

    .empty-text {
      font-size: 28rpx;
      color: #999;
    }
  }
}
</style>
