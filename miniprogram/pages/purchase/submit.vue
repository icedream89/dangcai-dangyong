<template>
  <view class="purchase-submit-page">
    <view class="form-container card">
      <view class="form-group">
        <text class="form-label required">需求标题</text>
        <input
          class="form-input"
          v-model="formData.title"
          placeholder="请简要描述您的采购需求"
        />
      </view>

      <view class="form-group">
        <text class="form-label">预算金额（元）</text>
        <input
          class="form-input"
          v-model="formData.budget"
          placeholder="请输入预算金额"
          type="digit"
        />
      </view>

      <view class="form-group">
        <text class="form-label">期望交付日期</text>
        <picker mode="date" @change="onDateChange">
          <view class="form-picker">
            <text class="picker-text" :class="{ placeholder: !formData.expectDate }">
              {{ formData.expectDate || '请选择期望交付日期' }}
            </text>
            <text class="picker-arrow">📅</text>
          </view>
        </picker>
      </view>

      <view class="form-group">
        <text class="form-label required">需求描述</text>
        <textarea
          class="form-textarea"
          v-model="formData.content"
          placeholder="请详细描述您的采购需求，包括数量、规格等"
          :maxlength="500"
        />
        <text class="char-count">{{ formData.content.length }}/500</text>
      </view>

      <view class="form-group">
        <text class="form-label">联系电话</text>
        <input
          class="form-input"
          v-model="formData.phone"
          placeholder="请输入您的联系电话"
          type="number"
        />
      </view>

      <button class="submit-btn" @click="handleSubmit" :disabled="submitting">
        {{ submitting ? '提交中...' : '提交需求' }}
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
        budget: '',
        expectDate: '',
        content: '',
        phone: ''
      },
      submitting: false
    }
  },
  methods: {
    onDateChange(e) {
      this.formData.expectDate = e.detail.value
    },

    async handleSubmit() {
      if (!this.formData.title) {
        return uni.showToast({ title: '请输入需求标题', icon: 'none' })
      }
      if (!this.formData.content) {
        return uni.showToast({ title: '请输入需求描述', icon: 'none' })
      }

      this.submitting = true

      setTimeout(() => {
        this.submitting = false
        uni.showToast({
          title: '提交成功',
          icon: 'success'
        })

        setTimeout(() => {
          uni.navigateTo({
            url: '/pages/purchase/my-list'
          })
        }, 1500)
      }, 1000)
    }
  }
}
</script>

<style scoped lang="scss">
.purchase-submit-page {
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
        font-size: 32rpx;
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
