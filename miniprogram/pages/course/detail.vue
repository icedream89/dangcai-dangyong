<template>
  <view class="course-detail-page">
    <view class="detail-container card">
      <image :src="courseInfo.coverImage" mode="aspectFill" class="course-cover" />
      <text class="course-title">{{ courseInfo.title }}</text>

      <view class="course-meta">
        <text class="meta-item">📂 {{ courseInfo.category }}</text>
        <text class="meta-item">👤 {{ courseInfo.author }}</text>
        <text class="meta-item">👁 {{ courseInfo.viewCount }}</text>
      </view>

      <view class="detail-divider"></view>

      <view class="detail-section">
        <text class="section-title">📝 课程简介</text>
        <text class="section-content">{{ courseInfo.summary }}</text>
      </view>

      <view class="detail-section">
        <text class="section-title">🎬 课程内容</text>
        <text class="section-content">{{ courseInfo.content || '暂无详细内容' }}</text>
      </view>
    </view>

    <view class="detail-actions">
      <button class="action-btn primary" @click="handleCollect">⭐ 收藏</button>
      <button class="action-btn" @click="handleShare">📤 分享</button>
    </view>
  </view>
</template>

<script>
import { mockCourses } from '@/api/index.js'

export default {
  data() {
    return {
      id: null,
      courseInfo: {}
    }
  },
  onLoad(options) {
    this.id = parseInt(options.id)
    this.loadDetail()
  },
  methods: {
    loadDetail() {
      this.courseInfo = mockCourses.find(c => c.id === this.id) || {}
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
.course-detail-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
  padding-bottom: 120rpx;
}

.detail-container {
  .course-cover {
    width: 100%;
    height: 400rpx;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
  }

  .course-title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
    line-height: 1.5;
    display: block;
    margin-bottom: 16rpx;
  }

  .course-meta {
    display: flex;
    flex-wrap: wrap;
    gap: 16rpx;
    margin-bottom: 24rpx;

    .meta-item {
      font-size: 24rpx;
      color: #999;
      background: #f8f8f8;
      padding: 8rpx 16rpx;
      border-radius: 8rpx;
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
