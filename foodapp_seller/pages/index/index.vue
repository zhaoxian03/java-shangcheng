<template>
  <view class="index-container">
    <!-- 顶部欢迎信息 -->
    <view class="welcome-section">
      <view class="welcome-left">
        <text class="welcome-title">欢迎回来，{{ sellerInfo?.shopInfo?.sname || '商家' }}</text>
        <text class="welcome-subtitle">管理您的店铺和商品</text>
      </view>
      <view class="welcome-right">
        <image v-if="sellerInfo?.shopInfo?.slogo" class="shop-logo" :src="sellerInfo?.shopInfo?.slogo" mode="aspectFill"></image>
        <uni-icons v-else class="shop-logo-placeholder" type="shop" size="60" color="#fff"></uni-icons>
      </view>
    </view>

    <!-- 功能导航 -->
    <view class="nav-section">
      <view class="nav-grid">
        <!-- 商品管理 -->
        <view class="nav-item" @click="goToGoodsList">
          <view class="nav-icon">
            <uni-icons type="shop" size="60" color="#007aff"></uni-icons>
          </view>
          <text class="nav-text">商品管理</text>
        </view>

        <!-- 订单管理 -->
        <view class="nav-item" @click="goToOrderList">
          <view class="nav-icon">
            <uni-icons type="list" size="60" color="#5ac8fa"></uni-icons>
          </view>
          <text class="nav-text">订单管理</text>
        </view>

        <!-- 店铺设置 -->
        <view class="nav-item" @click="goToShopSetting">
          <view class="nav-icon">
            <uni-icons type="setting" size="60" color="#4cd964"></uni-icons>
          </view>
          <text class="nav-text">店铺设置</text>
        </view>

        <!-- 退出登录 -->
        <view class="nav-item" @click="logout">
          <view class="nav-icon">
            <uni-icons type="log-out" size="60" color="#ff3b30"></uni-icons>
          </view>
          <text class="nav-text">退出登录</text>
        </view>
      </view>
    </view>

    <!-- 数据概览 -->
    <view class="stats-section">
      <uni-card title="数据概览">
        <view class="stats-grid">
          <view class="stat-item">
            <text class="stat-value">{{ goodsCount }}</text>
            <text class="stat-label">商品总数</text>
          </view>
          <view class="stat-item">
            <text class="stat-value">{{ onlineCount }}</text>
            <text class="stat-label">在线商品</text>
          </view>
          <view class="stat-item">
            <text class="stat-value">{{ orderCount }}</text>
            <text class="stat-label">今日订单</text>
          </view>
        </view>
      </uni-card>
    </view>
  </view>
</template>

<script>
import request from '../../utils/request.js'

export default {
  data() {
    return {
      sellerInfo: null,
      goodsCount: 0,
      onlineCount: 0,
      orderCount: 0
    }
  },
  onShow() {
    // 检查登录状态
    this.checkLoginStatus()
    // 加载数据概览
    this.loadStats()
  },
  methods: {
    // 检查登录状态
    checkLoginStatus() {
      const sellerInfo = uni.getStorageSync('sellerInfo')
      const token = uni.getStorageSync('token')
      // 同时检查sellerInfo和token
      if (sellerInfo && token) {
        this.sellerInfo = sellerInfo
        // 获取最新店铺信息
        this.loadShopInfo()
        return true
      } else {
        // 清除可能的残留数据
        uni.removeStorageSync('sellerInfo')
        uni.removeStorageSync('token')
        // 检查是否已经在登录页面
        const pages = getCurrentPages()
        const currentPage = pages[pages.length - 1]
        if (currentPage.route !== 'pages/login/login') {
          // 未登录且不在登录页面，跳转到登录页面
          uni.redirectTo({
            url: '/pages/login/login'
          })
        }
        return false
      }
    },
    
    // 获取最新店铺信息
    async loadShopInfo() {
      try {
        // 获取店铺ID
        const sid = this.sellerInfo.shopInfo?.sid
        if (!sid) {
          console.error('店铺ID不存在')
          return
        }
        
        // 调用接口获取最新店铺信息
        const res = await request.get(`/seller/shop/${sid}`)
        const shopData = res.data
        
        // 更新sellerInfo中的shopInfo
        this.sellerInfo.shopInfo = shopData
        // 同步更新本地存储
        uni.setStorageSync('sellerInfo', this.sellerInfo)
      } catch (error) {
        console.error('获取店铺信息失败:', error)
      }
    },
    
    // 加载数据概览
    async loadStats() {
      try {
        // 获取店铺ID
        const sid = this.sellerInfo.shopInfo?.sid
        if (!sid) {
          console.error('店铺ID不存在')
          return
        }
        
        // 调用接口获取真实数据
        const res = await request.get('/seller/stats', { sid })
        
        this.goodsCount = res.data.goodsCount
        this.onlineCount = res.data.onlineCount
        this.orderCount = res.data.orderCount
      } catch (error) {
        // 错误已在request工具中处理，使用默认数据
        this.goodsCount = 0
        this.onlineCount = 0
        this.orderCount = 0
      }
    },
    
    // 跳转到商品列表
    goToGoodsList() {
      uni.navigateTo({
        url: '/pages/goods/list'
      })
    },
    
    // 跳转到订单列表
    goToOrderList() {
      uni.navigateTo({
        url: '/pages/orders/list'
      })
    },
    
    // 跳转到店铺设置
    goToShopSetting() {
      uni.navigateTo({
        url: '/pages/shop/setting'
      })
    },
    
    // 退出登录
    logout() {
      uni.showModal({
        title: '退出登录',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            // 清除登录信息
            uni.removeStorageSync('sellerInfo')
            uni.removeStorageSync('token')
            // 跳转到登录页面
            uni.redirectTo({
              url: '/pages/login/login'
            })
          }
        }
      })
    }
  }
}
</script>

<style lang="scss">
.index-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.welcome-section {
  background-color: #007aff;
  color: #fff;
  padding: 40rpx;
  border-radius: 16rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 160rpx;
}

.welcome-left {
  flex: 1;
  padding-right: 20rpx;
}

.welcome-right {
  display: flex;
  align-items: center;
  justify-content: center;
}

.shop-logo {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  object-fit: cover;
}

.shop-logo-placeholder {
  width: 120rpx;
  height: 120rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
}

.welcome-title {
  font-size: 36rpx;
  font-weight: bold;
  display: block;
  margin-bottom: 10rpx;
}

.welcome-subtitle {
  font-size: 24rpx;
  opacity: 0.9;
}

.nav-section {
  background-color: #fff;
  padding: 30rpx;
  border-radius: 16rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
}

.nav-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30rpx;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 30rpx;
  background-color: #f8f8f8;
  border-radius: 12rpx;
  transition: all 0.3s ease;
}

.nav-item:active {
  transform: scale(0.98);
}

.nav-icon {
  margin-bottom: 20rpx;
}

.nav-text {
  font-size: 28rpx;
  color: #333;
}

.stats-section {
  margin-bottom: 30rpx;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20rpx;
  margin-top: 20rpx;
}

.stat-item {
  text-align: center;
  padding: 20rpx;
  background-color: #f8f8f8;
  border-radius: 12rpx;
}

.stat-value {
  display: block;
  font-size: 48rpx;
  font-weight: bold;
  color: #007aff;
  margin-bottom: 10rpx;
}

.stat-label {
  font-size: 24rpx;
  color: #666;
}
</style>