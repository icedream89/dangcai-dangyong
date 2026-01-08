<template>
  <view class="index-page">
    <!-- 顶部Banner -->
    <swiper class="banner-swiper" indicator-dots autoplay circular interval="3000">
      <swiper-item v-for="(item, index) in banners" :key="index">
        <image :src="item.image" mode="aspectFill" class="banner-image" />
        <view class="banner-content">
          <text class="banner-title">{{ item.title }}</text>
        </view>
      </swiper-item>
    </swiper>

    <!-- 功能入口 -->
    <view class="entrance-container">
      <view class="entrance-item" @click="navigateTo('/pages/policy/list')">
        <view class="entrance-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
          <text class="iconfont">📄</text>
        </view>
        <text class="entrance-text">政策查询</text>
      </view>
      <view class="entrance-item" @click="navigateTo('/pages/course/list')">
        <view class="entrance-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
          <text class="iconfont">📚</text>
        </view>
        <text class="entrance-text">企业课堂</text>
      </view>
      <view class="entrance-item" @click="navigateTo('/pages/enterprise/list')">
        <view class="entrance-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
          <text class="iconfont">🏢</text>
        </view>
        <text class="entrance-text">企业画册</text>
      </view>
      <view class="entrance-item" @click="navigateTo('/pages/purchase/submit')">
        <view class="entrance-icon" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);">
          <text class="iconfont">🛒</text>
        </view>
        <text class="entrance-text">我要采购</text>
      </view>
      <view class="entrance-item" @click="navigateTo('/pages/help/submit')">
        <view class="entrance-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
          <text class="iconfont">💬</text>
        </view>
        <text class="entrance-text">我要求助</text>
      </view>
    </view>

    <!-- 最新政策 -->
    <view class="section-container">
      <view class="section-header flex-between">
        <text class="section-title">📢 最新政策</text>
        <text class="section-more" @click="navigateTo('/pages/policy/list')">更多 →</text>
      </view>
      <view class="policy-list">
        <view
          class="policy-item"
          v-for="item in policies"
          :key="item.id"
          @click="navigateTo('/pages/policy/detail?id=' + item.id)"
        >
          <view class="policy-tag" v-if="item.isTop">置顶</view>
          <text class="policy-title">{{ item.policyTitle }}</text>
          <view class="policy-meta">
            <text class="policy-type">{{ item.policyType }}</text>
            <text class="policy-date">{{ item.issueDate }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 推荐企业 -->
    <view class="section-container">
      <view class="section-header flex-between">
        <text class="section-title">⭐ 推荐企业</text>
        <text class="section-more" @click="navigateTo('/pages/enterprise/list')">更多 →</text>
      </view>
      <scroll-view class="enterprise-scroll" scroll-x>
        <view
          class="enterprise-card"
          v-for="item in recommendedEnterprises"
          :key="item.id"
          @click="navigateTo('/pages/enterprise/detail?id=' + item.id)"
        >
          <image :src="item.logo" mode="aspectFill" class="enterprise-logo" />
          <text class="enterprise-name">{{ item.enterpriseName }}</text>
          <text class="enterprise-industry">{{ item.industry }}</text>
          <view class="enterprise-tags">
            <text class="tag" v-for="(tag, idx) in item.tags" :key="idx">{{ tag }}</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 热门课程 -->
    <view class="section-container">
      <view class="section-header flex-between">
        <text class="section-title">🎓 热门课程</text>
        <text class="section-more" @click="navigateTo('/pages/course/list')">更多 →</text>
      </view>
      <view class="course-list">
        <view
          class="course-item"
          v-for="item in hotCourses"
          :key="item.id"
          @click="navigateTo('/pages/course/detail?id=' + item.id)"
        >
          <image :src="item.coverImage" mode="aspectFill" class="course-cover" />
          <view class="course-info">
            <text class="course-title">{{ item.title }}</text>
            <view class="course-meta">
              <text class="course-category">{{ item.category }}</text>
              <text class="course-views">👁 {{ item.viewCount }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { mockPolicies, mockEnterprises, mockCourses } from '@/api/index.js'

export default {
  data() {
    return {
      banners: [
        {
          image: 'https://images.unsplash.com/photo-1497366216548-37526070297c?w=800',
          title: '当才当用 - 企业服务平台'
        },
        {
          image: 'https://images.unsplash.com/photo-1551434678-e076c223a692?w=800',
          title: '助力企业发展'
        },
        {
          image: 'https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=800',
          title: '政策精准推送'
        }
      ],
      policies: [],
      recommendedEnterprises: [],
      hotCourses: []
    }
  },
  onLoad() {
    this.loadData()
  },
  methods: {
    async loadData() {
      // 加载政策
      this.policies = mockPolicies.slice(0, 3)

      // 加载推荐企业
      this.recommendedEnterprises = mockEnterprises.filter(e => e.isRecommended)

      // 加载热门课程
      this.hotCourses = mockCourses.filter(c => c.isRecommended)
    },

    navigateTo(url) {
      uni.navigateTo({ url })
    }
  }
}
</script>

<style scoped lang="scss">
.index-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20rpx;
}

.banner-swiper {
  width: 100%;
  height: 360rpx;
  position: relative;

  .banner-image {
    width: 100%;
    height: 100%;
  }

  .banner-content {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 30rpx;
    background: linear-gradient(transparent, rgba(0, 0, 0, 0.6));

    .banner-title {
      color: #fff;
      font-size: 36rpx;
      font-weight: bold;
      text-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.3);
    }
  }
}

.entrance-container {
  display: flex;
  justify-content: space-between;
  padding: 30rpx 20rpx;
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;

  .entrance-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10rpx;
    flex: 1;

    .entrance-icon {
      width: 100rpx;
      height: 100rpx;
      border-radius: 20rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 8rpx 16rpx rgba(102, 126, 234, 0.3);

      .iconfont {
        font-size: 48rpx;
      }
    }

    .entrance-text {
      font-size: 22rpx;
      color: #333;
      text-align: center;
    }
  }
}

.section-container {
  margin: 20rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;

  .section-header {
    margin-bottom: 20rpx;

    .section-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
    }

    .section-more {
      font-size: 26rpx;
      color: #999;
    }
  }
}

// 政策列表
.policy-list {
  .policy-item {
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f0f0f0;
    position: relative;

    &:last-child {
      border-bottom: none;
    }

    .policy-tag {
      position: absolute;
      top: 0;
      right: 0;
      background: linear-gradient(135deg, #f5576c 0%, #f093fb 100%);
      color: #fff;
      font-size: 20rpx;
      padding: 4rpx 12rpx;
      border-radius: 8rpx;
    }

    .policy-title {
      font-size: 28rpx;
      color: #333;
      line-height: 1.6;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
      padding-right: 80rpx;
    }

    .policy-meta {
      display: flex;
      justify-content: space-between;
      margin-top: 10rpx;

      .policy-type {
        font-size: 24rpx;
        color: #667eea;
        background: rgba(102, 126, 234, 0.1);
        padding: 4rpx 12rpx;
        border-radius: 8rpx;
      }

      .policy-date {
        font-size: 24rpx;
        color: #999;
      }
    }
  }
}

// 企业滚动
.enterprise-scroll {
  white-space: nowrap;

  .enterprise-card {
    display: inline-block;
    width: 280rpx;
    background: #f8f8f8;
    border-radius: 16rpx;
    padding: 20rpx;
    margin-right: 20rpx;
    vertical-align: top;

    .enterprise-logo {
      width: 120rpx;
      height: 120rpx;
      border-radius: 16rpx;
      display: block;
      margin: 0 auto 16rpx;
    }

    .enterprise-name {
      font-size: 28rpx;
      font-weight: bold;
      color: #333;
      display: block;
      text-align: center;
      margin-bottom: 8rpx;
      white-space: normal;
    }

    .enterprise-industry {
      font-size: 24rpx;
      color: #999;
      display: block;
      text-align: center;
      margin-bottom: 12rpx;
    }

    .enterprise-tags {
      display: flex;
      justify-content: center;
      gap: 8rpx;

      .tag {
        font-size: 20rpx;
        color: #667eea;
        background: rgba(102, 126, 234, 0.1);
        padding: 4rpx 12rpx;
        border-radius: 8rpx;
      }
    }
  }
}

// 课程列表
.course-list {
  .course-item {
    display: flex;
    gap: 20rpx;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .course-cover {
      width: 200rpx;
      height: 140rpx;
      border-radius: 12rpx;
      flex-shrink: 0;
    }

    .course-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: space-between;

      .course-title {
        font-size: 28rpx;
        color: #333;
        font-weight: 500;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
      }

      .course-meta {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .course-category {
          font-size: 24rpx;
          color: #667eea;
          background: rgba(102, 126, 234, 0.1);
          padding: 4rpx 12rpx;
          border-radius: 8rpx;
        }

        .course-views {
          font-size: 24rpx;
          color: #999;
        }
      }
    }
  }
}
</style>
