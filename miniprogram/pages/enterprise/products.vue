<template>
  <view class="products-page">
    <!-- 顶部操作栏 -->
    <view class="action-bar">
      <button class="add-btn" @click="handleAdd">
        <text class="btn-icon">+</text>
        <text class="btn-text">发布产品</text>
      </button>
    </view>

    <!-- 产品列表 -->
    <view class="products-list">
      <view
        class="product-item card"
        v-for="item in myProducts"
        :key="item.id"
        @click="handleEdit(item)"
      >
        <image :src="item.coverImage" mode="aspectFill" class="product-cover" />
        <view class="product-info">
          <text class="product-name">{{ item.productName }}</text>
          <view class="product-meta">
            <text class="product-category">{{ item.category }}</text>
            <text class="product-price">¥{{ item.price }}/{{ item.unit }}</text>
          </view>
          <text class="product-desc">{{ item.description }}</text>
          <view class="product-stats">
            <text class="stat-item">销量 {{ item.salesCount }}</text>
            <text class="stat-item">{{ item.status === 'online' ? '已上架' : '已下架' }}</text>
          </view>
        </view>
        <view class="product-actions">
          <button class="action-btn edit" @click.stop="handleEdit(item)">编辑</button>
          <button class="action-btn delete" @click.stop="handleDelete(item)">删除</button>
        </view>
      </view>

      <!-- 空状态 -->
      <view class="empty-state" v-if="myProducts.length === 0">
        <text class="empty-icon">📦</text>
        <text class="empty-text">暂无产品</text>
        <text class="empty-tip">点击上方"发布产品"按钮添加</text>
      </view>
    </view>
  </view>
</template>

<script>
import { userStore } from '@/store/user.js'
import { mockProducts } from '@/api/index.js'

export default {
  data() {
    return {
      myProducts: []
    }
  },
  onLoad() {
    this.loadProducts()
  },
  methods: {
    loadProducts() {
      // 获取当前用户的企业ID
      const enterpriseId = userStore.getEnterpriseId()
      if (enterpriseId) {
        // 筛选属于当前企业的产品
        this.myProducts = mockProducts.filter(p => p.enterpriseId === enterpriseId)
      }
    },

    handleAdd() {
      uni.showToast({
        title: '产品发布功能开发中',
        icon: 'none'
      })
    },

    handleEdit(product) {
      uni.showToast({
        title: '产品编辑功能开发中',
        icon: 'none'
      })
    },

    handleDelete(product) {
      uni.showModal({
        title: '确认删除',
        content: `确定要删除"${product.productName}"吗？`,
        success: (res) => {
          if (res.confirm) {
            // TODO: 调用删除API
            uni.showToast({
              title: '删除成功',
              icon: 'success'
            })
            // 重新加载列表
            this.loadProducts()
          }
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
.products-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.action-bar {
  padding: 20rpx;
  background: #fff;
  position: sticky;
  top: 0;
  z-index: 10;

  .add-btn {
    width: 100%;
    height: 88rpx;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: #fff;
    border: none;
    border-radius: 50rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12rpx;
    font-size: 32rpx;
    font-weight: 500;
    box-shadow: 0 8rpx 16rpx rgba(102, 126, 234, 0.3);

    .btn-icon {
      font-size: 40rpx;
      font-weight: bold;
    }
  }
}

.products-list {
  padding: 20rpx;

  .product-item {
    position: relative;

    .product-cover {
      width: 100%;
      height: 320rpx;
      border-radius: 16rpx;
      margin-bottom: 20rpx;
    }

    .product-info {
      .product-name {
        font-size: 32rpx;
        font-weight: bold;
        color: #333;
        display: block;
        margin-bottom: 12rpx;
      }

      .product-meta {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12rpx;

        .product-category {
          font-size: 24rpx;
          color: #667eea;
          background: rgba(102, 126, 234, 0.1);
          padding: 4rpx 12rpx;
          border-radius: 8rpx;
        }

        .product-price {
          font-size: 32rpx;
          font-weight: bold;
          color: #f5576c;
        }
      }

      .product-desc {
        font-size: 26rpx;
        color: #666;
        line-height: 1.6;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
        margin-bottom: 12rpx;
      }

      .product-stats {
        display: flex;
        gap: 24rpx;

        .stat-item {
          font-size: 24rpx;
          color: #999;
        }
      }
    }

    .product-actions {
      display: flex;
      gap: 12rpx;
      margin-top: 20rpx;
      padding-top: 20rpx;
      border-top: 1rpx solid #f0f0f0;

      .action-btn {
        flex: 1;
        height: 64rpx;
        border: none;
        border-radius: 12rpx;
        font-size: 28rpx;

        &.edit {
          background: #667eea;
          color: #fff;
        }

        &.delete {
          background: #f5f5f5;
          color: #f5576c;
        }
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
      display: block;
      margin-bottom: 12rpx;
    }

    .empty-tip {
      font-size: 24rpx;
      color: #ccc;
    }
  }
}
</style>
