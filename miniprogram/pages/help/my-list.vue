<template>
  <view class="help-list-page">
    <view class="empty-state">
      <text class="empty-icon">💬</text>
      <text class="empty-text">暂无求助记录</text>
      <text class="empty-tip">遇到问题可以点击"我要求助"提交</text>
    </view>
  </view>
</template>

<script>
export default {}
</script>

<style scoped lang="scss">
.help-list-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 0;

  .empty-icon {
    font-size: 120rpx;
    margin-bottom: 20rpx;
  }

  .empty-text {
    font-size: 28rpx;
    color: #666;
    margin-bottom: 12rpx;
  }

  .empty-tip {
    font-size: 24rpx;
    color: #999;
  }
}
</style>
