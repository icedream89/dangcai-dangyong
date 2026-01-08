<template>
  <view class="help-submit-page">
    <view class="form-container card">
      <view class="form-group">
        <text class="form-label required">问题标题</text>
        <input
          class="form-input"
          v-model="formData.title"
          placeholder="请简要描述您的问题"
        />
      </view>

      <view class="form-group">
        <text class="form-label required">问题类型</text>
        <view class="form-picker" @click="showCategoryPicker = true">
          <text class="picker-text" :class="{ placeholder: !formData.category }">
            {{ formData.category || '请选择问题类型' }}
          </text>
          <text class="picker-arrow">›</text>
        </view>
      </view>

      <view class="form-group">
        <text class="form-label required">紧急程度</text>
        <view class="urgency-options">
          <view
            class="urgency-item"
            v-for="(item, index) in urgencyOptions"
            :key="index"
            :class="{ active: formData.urgency === item.value }"
            @click="formData.urgency = item.value"
          >
            <text class="urgency-icon">{{ item.icon }}</text>
            <text class="urgency-text">{{ item.label }}</text>
          </view>
        </view>
      </view>

      <view class="form-group">
        <text class="form-label required">问题描述</text>
        <textarea
          class="form-textarea"
          v-model="formData.content"
          placeholder="请详细描述您遇到的问题，我们会尽快为您处理"
          :maxlength="500"
        />
        <text class="char-count">{{ formData.content.length }}/500</text>
      </view>

      <view class="form-group">
        <text class="form-label">联系方式</text>
        <input
          class="form-input"
          v-model="formData.phone"
          placeholder="请输入您的联系电话"
          type="number"
        />
      </view>

      <button class="submit-btn" @click="handleSubmit" :disabled="submitting">
        {{ submitting ? '提交中...' : '提交求助' }}
      </button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        title: '',
        category: '',
        urgency: 1,
        content: '',
        phone: ''
      },
      urgencyOptions: [
        { label: '一般', value: 1, icon: '😊' },
        { label: '紧急', value: 2, icon: '😰' },
        { label: '非常紧急', value: 3, icon: '🚨' }
      ],
      showCategoryPicker: false,
      categories: ['政策咨询', '用电问题', '人才服务', '融资服务', '其他'],
      submitting: false
    }
  },
  methods: {
    selectCategory(category) {
      this.formData.category = category
      this.showCategoryPicker = false
    },

    async handleSubmit() {
      // 验证
      if (!this.formData.title) {
        return uni.showToast({ title: '请输入问题标题', icon: 'none' })
      }
      if (!this.formData.category) {
        return uni.showToast({ title: '请选择问题类型', icon: 'none' })
      }
      if (!this.formData.content) {
        return uni.showToast({ title: '请输入问题描述', icon: 'none' })
      }

      this.submitting = true

      // 模拟提交
      setTimeout(() => {
        this.submitting = false
        uni.showToast({
          title: '提交成功',
          icon: 'success'
        })

        // 跳转到我的求助列表
        setTimeout(() => {
          uni.navigateTo({
            url: '/pages/help/my-list'
          })
        }, 1500)
      }, 1000)
    }
  }
}
</script>

<style scoped lang="scss">
.help-submit-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
}

.form-container {
  .form-group {
    margin-bottom: 32rpx;
    position: relative;

    .form-label {
      font-size: 28rpx;
      color: #333;
      font-weight: 500;
      display: block;
      margin-bottom: 12rpx;

      &.required::before {
        content: '*';
        color: #f5576c;
        margin-right: 4rpx;
      }
    }

    .form-input {
      width: 100%;
      padding: 20rpx 24rpx;
      background: #f8f8f8;
      border-radius: 12rpx;
      font-size: 28rpx;
      box-sizing: border-box;
    }

    .form-picker {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20rpx 24rpx;
      background: #f8f8f8;
      border-radius: 12rpx;

      .picker-text {
        font-size: 28rpx;
        color: #333;

        &.placeholder {
          color: #999;
        }
      }

      .picker-arrow {
        font-size: 40rpx;
        color: #999;
      }
    }

    .urgency-options {
      display: flex;
      gap: 16rpx;

      .urgency-item {
        flex: 1;
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 8rpx;
        padding: 20rpx;
        background: #f8f8f8;
        border-radius: 12rpx;
        border: 2rpx solid transparent;

        &.active {
          background: rgba(102, 126, 234, 0.1);
          border-color: #667eea;
        }

        .urgency-icon {
          font-size: 48rpx;
        }

        .urgency-text {
          font-size: 24rpx;
          color: #666;
        }
      }
    }

    .form-textarea {
      width: 100%;
      min-height: 240rpx;
      padding: 20rpx 24rpx;
      background: #f8f8f8;
      border-radius: 12rpx;
      font-size: 28rpx;
      box-sizing: border-box;
    }

    .char-count {
      position: absolute;
      bottom: 16rpx;
      right: 24rpx;
      font-size: 24rpx;
      color: #999;
    }
  }

  .submit-btn {
    width: 100%;
    height: 88rpx;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: #fff;
    border: none;
    border-radius: 50rpx;
    font-size: 32rpx;
    font-weight: 500;
    margin-top: 40rpx;

    &[disabled] {
      opacity: 0.6;
    }
  }
}
</style>
