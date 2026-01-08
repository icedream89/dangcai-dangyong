<template>
  <view class="purchase-list-page">
    <view class="empty-state">
      <text class="empty-icon">🛒</text>
      <text class="empty-text">暂无采购需求</text>
      <text class="empty-tip">点击"我要采购"发布您的需求</text>
    </view>
  </view>
</template>

<script>
export default {}
</script>

<style scoped lang="scss">
.purchase-list-page {
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
