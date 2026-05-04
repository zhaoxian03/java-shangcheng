<template>
  <view class="color-setting">
    <view class="setting-title">主题颜色设置</view>
    
    <!-- 颜色选择器 -->
    <view class="color-list">
      <view 
        v-for="(color, index) in themeColors" 
        :key="index"
        :class="['color-item', { active: currentColor === color }]"
        :style="{ backgroundColor: color }"
        @click="changeColor(color)"
      ></view>
    </view>
    
    <!-- 自定义颜色输入 -->
    <uni-easyinput 
      v-model="customColor" 
      placeholder="输入十六进制颜色值"
      @confirm="confirmCustomColor"
    ></uni-easyinput>
  </view>
</template>

<script>
export default {
  data() {
    return {
      themeColors: [
        '#007AFF', // 默认蓝
        '#FF3B30', // 红
        '#34C759', // 绿
        '#FF9500', // 橙
        '#5856D6', // 紫
        '#000000'  // 黑
      ],
      currentColor: '',
      customColor: ''
    }
  },
  onLoad() {
    // 读取本地存储的颜色设置
    const savedColor = uni.getStorageSync('themeColor')
    this.currentColor = savedColor || '#007AFF'
    this.applyColor(this.currentColor)
  },
  methods: {
    changeColor(color) {
      this.currentColor = color
      this.applyColor(color)
      uni.setStorageSync('themeColor', color)
    },
    confirmCustomColor() {
      if (/^#([0-9A-F]{3}){1,2}$/i.test(this.customColor)) {
        this.changeColor(this.customColor)
      } else {
        uni.showToast({
          title: '请输入有效的颜色值',
          icon: 'none'
        })
      }
    },
    applyColor(color) {
      // 动态修改全局CSS变量
      document.documentElement.style.setProperty('--theme-color', color)
      document.documentElement.style.setProperty('--theme-color-light', this.lightenColor(color, 30))
    },
    // 颜色提亮辅助函数
    lightenColor(color, percent) {
      let R = parseInt(color.substring(1, 3), 16)
      let G = parseInt(color.substring(3, 5), 16)
      let B = parseInt(color.substring(5, 7), 16)

      R = Math.floor(R + (255 - R) * percent / 100)
      G = Math.floor(G + (255 - G) * percent / 100)
      B = Math.floor(B + (255 - B) * percent / 100)

      return `#${this.toHex(R)}${this.toHex(G)}${this.toHex(B)}`
    },
    toHex(n) {
      n = Math.min(255, Math.max(0, n))
      return (n < 16 ? '0' : '') + n.toString(16).toUpperCase()
    }
  }
}
</script>

<style scoped>
.color-setting {
  padding: 20rpx;
}

.setting-title {
  font-size: 32rpx;
  margin: 30rpx 0;
}

.color-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  margin: 40rpx 0;
}

.color-item {
  width: 60rpx;
  height: 60rpx;
  border-radius: 50%;
  border: 2rpx solid #eee;
}

.color-item.active {
  box-shadow: 0 0 0 3rpx #fff, 0 0 0 5rpx currentColor;
}
</style>