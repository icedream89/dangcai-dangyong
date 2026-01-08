<template>
  <view class="contact-edit-page">
    <view class="form-container card">
      <view class="form-group">
        <text class="form-label">企业名称</text>
        <input class="form-input" v-model="formData.enterpriseName" disabled />
      </view>

      <view class="form-group">
        <text class="form-label required">联系人</text>
        <input class="form-input" v-model="formData.contactPerson" placeholder="请输入联系人姓名" />
      </view>

      <view class="form-group">
        <text class="form-label required">联系电话</text>
        <input class="form-input" v-model="formData.contactPhone" placeholder="请输入联系电话" type="number" />
      </view>

      <view class="form-group">
        <text class="form-label">企业邮箱</text>
        <input class="form-input" v-model="formData.email" placeholder="请输入企业邮箱" />
      </view>

      <view class="form-group">
        <text class="form-label">企业地址</text>
        <textarea
          class="form-textarea"
          v-model="formData.address"
          placeholder="请输入企业地址"
          :maxlength="200"
        />
      </view>

      <view class="form-group">
        <text class="form-label">营业时间</text>
        <input class="form-input" v-model="formData.businessHours" placeholder="例如：周一至周五 9:00-18:00" />
      </view>

      <button class="submit-btn" @click="handleSubmit" :disabled="submitting">
        {{ submitting ? '保存中...' : '保存联系方式' }}
      </button>
    </view>
  </view>
</template>

<script>
import { userStore } from '@/store/user.js'
import { mockEnterprises } from '@/api/index.js'

export default {
  data() {
    return {
      formData: {
        enterpriseName: '',
        contactPerson: '',
        contactPhone: '',
        email: '',
        address: '',
        businessHours: ''
      },
      submitting: false
    }
  },
  onLoad() {
    this.loadEnterpriseInfo()
  },
  methods: {
    loadEnterpriseInfo() {
      const enterpriseId = userStore.getEnterpriseId()
      const enterprise = mockEnterprises.find(e => e.id === enterpriseId)

      if (enterprise) {
        this.formData = {
          enterpriseName: enterprise.enterpriseName,
          contactPerson: enterprise.legalPerson || '',
          contactPhone: '',
          email: '',
          address: enterprise.address || '',
          businessHours: '周一至周五 9:00-18:00'
        }
      }
    },

    async handleSubmit() {
      if (!this.formData.contactPerson) {
        return uni.showToast({ title: '请输入联系人', icon: 'none' })
      }
      if (!this.formData.contactPhone) {
        return uni.showToast({ title: '请输入联系电话', icon: 'none' })
      }

      this.submitting = true

      setTimeout(() => {
        this.submitting = false
        uni.showToast({
          title: '保存成功',
          icon: 'success'
        })

        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      }, 1000)
    }
  }
}
</script>

<style scoped lang="scss">
.contact-edit-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
}

.form-container {
  .form-group {
    margin-bottom: 32rpx;

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

      &[disabled] {
        opacity: 0.6;
      }
    }

    .form-textarea {
      width: 100%;
      min-height: 180rpx;
      padding: 20rpx 24rpx;
      background: #f8f8f8;
      border-radius: 12rpx;
      font-size: 28rpx;
      box-sizing: border-box;
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
