<template>
  <view class="enterprise-detail-page">
    <!-- 企业头部 -->
    <view class="enterprise-header card">
      <image :src="enterprise.logo" mode="aspectFill" class="enterprise-logo" />
      <view class="enterprise-info">
        <text class="enterprise-name">{{ enterprise.enterpriseName }}</text>
        <view class="enterprise-meta">
          <text class="meta-item">{{ enterprise.industry }}</text>
          <text class="meta-item">{{ enterprise.scale }}</text>
        </view>
        <text class="enterprise-intro">{{ enterprise.intro }}</text>
      </view>
    </view>

    <!-- 企业图片 -->
    <view class="section-container card" v-if="enterprise.images && enterprise.images.length > 0">
      <text class="section-title">📷 企业风采</text>
      <scroll-view class="image-scroll" scroll-x>
        <image
          v-for="(img, idx) in enterprise.images"
          :key="idx"
          :src="img"
          mode="aspectFill"
          class="enterprise-image"
          @click="previewImage(idx)"
        />
      </scroll-view>
    </view>

    <!-- 企业产品 -->
    <view class="section-container card">
      <view class="section-header">
        <text class="section-title">📦 企业产品</text>
        <text class="product-count">共{{ productList.length }}个产品</text>
      </view>
      <view class="product-list">
        <view
          class="product-item"
          v-for="item in productList"
          :key="item.id"
        >
          <image :src="item.coverImage" mode="aspectFill" class="product-cover" />
          <view class="product-info">
            <text class="product-name">{{ item.productName }}</text>
            <view class="product-footer">
              <text class="product-price">¥{{ item.price }}/{{ item.unit }}</text>
              <text class="product-sales">已售{{ item.salesCount }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 联系方式 -->
    <view class="section-container card">
      <text class="section-title">📞 联系方式</text>
      <view class="contact-info">
        <view class="contact-item">
          <text class="contact-label">法人代表</text>
          <text class="contact-value">{{ enterprise.legalPerson }}</text>
        </view>
        <view class="contact-item">
          <text class="contact-label">企业地址</text>
          <text class="contact-value">{{ enterprise.address }}</text>
        </view>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="detail-actions">
      <button class="action-btn primary" @click="handleCall">📞 电话咨询</button>
      <button class="action-btn" @click="handleCollect">⭐ 收藏企业</button>
    </view>
  </view>
</template>

<script>
import { mockEnterprises, mockProducts } from '@/api/index.js'

export default {
  data() {
    return {
      id: null,
      enterprise: {},
      productList: []
    }
  },
  onLoad(options) {
    this.id = parseInt(options.id)
    this.loadDetail()
  },
  methods: {
    loadDetail() {
      this.enterprise = mockEnterprises.find(e => e.id === this.id) || {}
      this.productList = mockProducts.filter(p => p.enterpriseId === this.id)
    },

    previewImage(index) {
      uni.previewImage({
        urls: this.enterprise.images,
        current: index
      })
    },

    handleCall() {
      uni.makePhoneCall({
        phoneNumber: '13800138000'
      })
    },

    handleCollect() {
      uni.showToast({
        title: '收藏成功',
        icon: 'success'
      })
    }
  }
}
</script>

<style scoped lang="scss">
.enterprise-detail-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
  padding-bottom: 120rpx;
}

.enterprise-header {
  display: flex;
  gap: 20rpx;
  padding: 24rpx;

  .enterprise-logo {
    width: 120rpx;
    height: 120rpx;
    border-radius: 16rpx;
    flex-shrink: 0;
  }

  .enterprise-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 12rpx;

    .enterprise-name {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
    }

    .enterprise-meta {
      display: flex;
      gap: 16rpx;

      .meta-item {
        font-size: 24rpx;
        color: #667eea;
        background: rgba(102, 126, 234, 0.1);
        padding: 4rpx 12rpx;
        border-radius: 8rpx;
      }
    }

    .enterprise-intro {
      font-size: 26rpx;
      color: #666;
      line-height: 1.6;
    }
  }
}

.section-container {
  margin-top: 20rpx;

  .section-title {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
    display: block;
    margin-bottom: 16rpx;
  }

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;

    .product-count {
      font-size: 24rpx;
      color: #999;
    }
  }
}

.image-scroll {
  white-space: nowrap;

  .enterprise-image {
    display: inline-block;
    width: 400rpx;
    height: 300rpx;
    border-radius: 16rpx;
    margin-right: 20rpx;
  }
}

.product-list {
  .product-item {
    display: flex;
    gap: 16rpx;
    padding: 16rpx 0;
    border-bottom: 1rpx solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .product-cover {
      width: 160rpx;
      height: 160rpx;
      border-radius: 12rpx;
      flex-shrink: 0;
    }

    .product-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: space-between;

      .product-name {
        font-size: 28rpx;
        color: #333;
        font-weight: 500;
      }

      .product-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .product-price {
          font-size: 32rpx;
          color: #f5576c;
          font-weight: bold;
        }

        .product-sales {
          font-size: 24rpx;
          color: #999;
        }
      }
    }
  }
}

.contact-info {
  .contact-item {
    display: flex;
    justify-content: space-between;
    padding: 16rpx 0;
    border-bottom: 1rpx solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .contact-label {
      font-size: 28rpx;
      color: #999;
    }

    .contact-value {
      font-size: 28rpx;
      color: #333;
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
