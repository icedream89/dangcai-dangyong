<template>
  <view class="course-list-page">
    <view class="filter-bar">
      <view
        class="filter-item"
        :class="{ active: currentCategory === '' }"
        @click="switchCategory('')"
      >
        全部
      </view>
      <view
        class="filter-item"
        v-for="cat in categories"
        :key="cat"
        :class="{ active: currentCategory === cat }"
        @click="switchCategory(cat)"
      >
        {{ cat }}
      </view>
    </view>

    <view class="course-list">
      <view
        class="course-item card"
        v-for="item in courseList"
        :key="item.id"
        @click="goDetail(item.id)"
      >
        <image :src="item.coverImage" mode="aspectFill" class="course-cover" />
        <view class="course-info">
          <view class="course-header">
            <text class="course-title">{{ item.title }}</text>
            <view class="course-type">{{ item.courseType }}</view>
          </view>
          <text class="course-summary">{{ item.summary }}</text>
          <view class="course-footer">
            <text class="course-author">👤 {{ item.author }}</text>
            <text class="course-views">👁 {{ item.viewCount }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { mockCourses } from '@/api/index.js'

export default {
  data() {
    return {
      categories: ['政策解读', '财务税务', '数字化'],
      currentCategory: '',
      courseList: []
    }
  },
  onLoad() {
    this.loadCourses()
  },
  methods: {
    loadCourses() {
      let list = [...mockCourses]
      if (this.currentCategory) {
        list = list.filter(item => item.category === this.currentCategory)
      }
      this.courseList = list
    },

    switchCategory(category) {
      this.currentCategory = category
      this.loadCourses()
    },

    goDetail(id) {
      uni.navigateTo({
        url: '/pages/course/detail?id=' + id
      })
    }
  }
}
</script>

<style scoped lang="scss">
.course-list-page {
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

    &.active {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
    }
  }
}

.course-list {
  padding: 20rpx;

  .course-item {
    display: flex;
    gap: 20rpx;

    .course-cover {
      width: 240rpx;
      height: 180rpx;
      border-radius: 12rpx;
      flex-shrink: 0;
    }

    .course-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: space-between;

      .course-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        gap: 12rpx;
        margin-bottom: 8rpx;

        .course-title {
          flex: 1;
          font-size: 28rpx;
          font-weight: bold;
          color: #333;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }

        .course-type {
          font-size: 20rpx;
          color: #fff;
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
          padding: 4rpx 12rpx;
          border-radius: 8rpx;
          flex-shrink: 0;
        }
      }

      .course-summary {
        font-size: 24rpx;
        color: #666;
        line-height: 1.5;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
        margin-bottom: 8rpx;
      }

      .course-footer {
        display: flex;
        justify-content: space-between;
        font-size: 24rpx;
        color: #999;
      }
    }
  }
}
</style>
