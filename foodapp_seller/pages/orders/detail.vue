<template>
  <view class="order-detail-container">
    <!-- 顶部导航 -->
    <view class="top-nav">
      <view class="nav-left" @click="goBack">
        <uni-icons type="left" size="28"></uni-icons>
        <text>返回</text>
      </view>
      <view class="nav-title">订单详情</view>
      <view class="nav-right"></view>
    </view>

    <!-- 订单基本信息卡片 -->
    <uni-card class="order-card">
      <view class="order-header">
        <view class="order-id">订单ID: {{ orderDetail.orderid }}</view>
        <uni-tag :type="getStatusType(orderDetail.status)">
          {{ getStatusText(orderDetail.status) }}
        </uni-tag>
      </view>

      <view class="order-info">
        <view class="info-item">
          <text class="info-label">下单时间:</text>
          <text class="info-value">{{ formatTime(orderDetail.ordertime) }}</text>
        </view>
        <view class="info-item" v-if="orderDetail.paytime">
          <text class="info-label">支付时间:</text>
          <text class="info-value">{{ formatTime(orderDetail.paytime) }}</text>
        </view>
        <view class="info-item" v-if="orderDetail.deliverytime">
          <text class="info-label">发货时间:</text>
          <text class="info-value">{{ formatTime(orderDetail.deliverytime) }}</text>
        </view>
        <view class="info-item" v-if="orderDetail.finishTime">
          <text class="info-label">完成时间:</text>
          <text class="info-value">{{ formatTime(orderDetail.finishTime) }}</text>
        </view>
      </view>
    </uni-card>

    <!-- 客户信息卡片 -->
    <uni-card class="customer-card" title="客户信息">
      <view class="customer-info">
        <view class="info-item">
          <text class="info-label">客户昵称:</text>
          <text class="info-value">{{ orderDetail.customer?.username || '未知客户' }}</text>
        </view>
        <view class="info-item" v-if="orderDetail.customer?.nickName">
          <text class="info-label">客户姓名:</text>
          <text class="info-value">{{ orderDetail.customer?.nickName }}</text>
        </view>
      </view>
    </uni-card>
    
    <!-- 收货地址卡片 -->
    <uni-card class="address-card" title="收货地址" v-if="orderDetail.ordersDetail && orderDetail.ordersDetail.length > 0">
      <view class="address-info">
        <view class="info-item">
          <text class="info-label">收货人:</text>
          <text class="info-value">{{ orderDetail.ordersDetail[0].receiveName || '暂无收货人' }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">联系电话:</text>
          <text class="info-value">{{ orderDetail.ordersDetail[0].tel || '暂无联系电话' }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">详细地址:</text>
          <text class="info-value address-detail">
            {{ orderDetail.ordersDetail[0].province || '' }}{{ orderDetail.ordersDetail[0].city || '' }}{{ orderDetail.ordersDetail[0].district || '' }}{{ orderDetail.ordersDetail[0].detailAddress || '暂无详细地址' }}
          </text>
        </view>
      </view>
    </uni-card>

    <!-- 商品信息卡片 -->
    <uni-card class="goods-card" title="商品信息">
      <view class="goods-list">
        <view class="goods-item" v-for="(detail, index) in orderDetail.ordersDetail" :key="index">
          <view class="goods-info">
            <image v-if="detail.icon" :src="detail.icon" class="goods-icon"></image>
            <view class="goods-details">
              <view class="goods-name">{{ detail.gname }}</view>
              <view class="goods-spec">{{ detail.spec }}</view>
            </view>
          </view>
          <view class="goods-price">
            <view class="price">¥{{ detail.buyPrice }}</view>
            <view class="quantity">x{{ detail.buyNums }}</view>
          </view>
        </view>
      </view>
    </uni-card>

    <!-- 价格信息卡片 -->
    <uni-card class="price-card" title="价格信息">
      <view class="price-info">
        <view class="price-item">
          <text class="price-label">商品总价:</text>
          <text class="price-value">¥{{ orderDetail.totalPrice }}</text>
        </view>
        <view class="price-item">
          <text class="price-label">运费:</text>
          <text class="price-value">¥{{ orderDetail.freightPrice || 0 }}</text>
        </view>
        <view class="price-item total">
          <text class="price-label">订单总价:</text>
          <text class="price-value">¥{{ orderDetail.totalPrice + (orderDetail.freightPrice || 0) }}</text>
        </view>
      </view>
    </uni-card>

    <!-- 支付信息卡片 -->
    <uni-card class="payment-card" title="支付信息" v-if="orderDetail.paytime">
      <view class="payment-info">
        <view class="info-item">
          <text class="info-label">支付方式:</text>
          <text class="info-value">{{ getPaymentMethodText(orderDetail.paymentMethod) }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">支付状态:</text>
          <text class="info-value">{{ orderDetail.payStatus ? '已支付' : '未支付' }}</text>
        </view>
      </view>
    </uni-card>

    <!-- 物流信息卡片 -->
    <uni-card class="logistics-card" title="物流信息">
      <view class="logistics-info" v-if="orderDetail.logisticsInfo">
        <view class="logistics-item" v-for="(log, index) in orderDetail.logisticsInfo" :key="index">
          <view class="logistics-time">{{ formatTime(log.time) }}</view>
          <view class="logistics-content">{{ log.content }}</view>
        </view>
      </view>
      <view class="no-logistics" v-else>
        <uni-icons type="info" size="48" color="#ccc"></uni-icons>
        <text>暂无物流信息</text>
      </view>
    </uni-card>

    <!-- 客户评价卡片 -->
    <uni-card class="evaluate-card" title="客户评价" v-if="evaluates && evaluates.length > 0">
      <view class="evaluate-list">
        <view class="evaluate-item" v-for="(evaluate, index) in evaluates" :key="index">
          <view class="evaluate-header">
            <view class="customer-info">
              <image v-if="evaluate.headerimg" :src="evaluate.headerimg" class="customer-avatar"></image>
              <text class="customer-name">{{ evaluate.douyinCode || '匿名客户' }}</text>
            </view>
          </view>
          <view class="evaluate-content">
            {{ evaluate.content || '暂无评价内容' }}
          </view>
          <view class="evaluate-tags" v-if="evaluate.etype">
            <uni-tag size="small" v-for="(tag, tagIndex) in getEvaluateTags(evaluate.etype)" :key="tagIndex">
              {{ tag }}
            </uni-tag>
          </view>
          <view class="evaluate-pics" v-if="evaluate.pic">
            <image 
              v-for="(pic, picIndex) in evaluate.pic.split(';')" 
              :key="picIndex" 
              :src="pic" 
              class="evaluate-pic"
              @click="previewImage(pic)"
            ></image>
          </view>
        </view>
      </view>
    </uni-card>

    <!-- 订单操作按钮 -->
    <view class="order-actions" v-if="canUpdateStatus">
      <button class="action-btn" :type="getStatusBtnType(orderDetail.status)" @click="handleOrderAction">
        {{ getActionText(orderDetail.status) }}
      </button>
    </view>
  </view>
</template>

<script>
import request from '../../utils/request.js'

export default {
  data() {
    return {
      sellerInfo: null,
      orderDetail: {},
      orderId: null,
      canUpdateStatus: false,
      evaluates: []
    }
  },
  onLoad(options) {
    this.orderId = options.id
    this.checkLoginStatus()
    this.loadOrderDetail()
  },
  methods: {
    // 返回上一页
    goBack() {
      uni.navigateBack()
    },
    
    // 检查登录状态
    checkLoginStatus() {
      const sellerInfo = uni.getStorageSync('sellerInfo')
      const token = uni.getStorageSync('token')
      // 同时检查sellerInfo和token
      if (sellerInfo && token) {
        this.sellerInfo = sellerInfo
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
    
    // 加载订单详情
    async loadOrderDetail() {
      uni.showLoading({
        title: '加载中...'
      })
      
      try {
        const sid = this.sellerInfo.shopInfo?.sid
        if (!sid || !this.orderId) {
          uni.hideLoading()
          return
        }
        
        const res = await request.get(`/seller/orders/${this.orderId}`, {
          sid: sid
        })
        
        this.orderDetail = res.data
        this.canUpdateStatus = this.checkCanUpdateStatus()
        
        // 加载订单评价
        this.loadOrderEvaluates()
      } catch (error) {
        console.error('加载订单详情失败:', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        uni.hideLoading()
      }
    },
    
    // 加载订单评价
    async loadOrderEvaluates() {
      try {
        const sid = this.sellerInfo.shopInfo?.sid
        if (!sid || !this.orderId) {
          return
        }
        
        const res = await request.get(`/seller/orders/${this.orderId}/evaluates`, {
          sid: sid
        })
        
        this.evaluates = res.data
      } catch (error) {
        console.error('加载订单评价失败:', error)
        // 评价加载失败不影响页面展示，只在控制台打印错误
      }
    },
    
    // 检查是否可以更新订单状态
    checkCanUpdateStatus() {
      // 只有已支付状态可以更新为已完成
      return this.orderDetail.status === 1
    },
    
    // 处理订单操作
    async handleOrderAction() {
      if (this.orderDetail.status === 1) {
        // 已支付状态，更新为已完成
        this.updateOrderStatus(2)
      }
    },
    
    // 更新订单状态
    async updateOrderStatus(status) {
      try {
        const sid = this.sellerInfo.shopInfo?.sid
        await request.put('/seller/orders/status', {
          sid: sid,
          orderid: this.orderId,
          status: status
        })
        
        // 刷新订单详情
        this.loadOrderDetail()
        
        uni.showToast({
          title: '订单状态更新成功',
          icon: 'success'
        })
      } catch (error) {
        console.error('更新订单状态失败:', error)
        uni.showToast({
          title: '更新失败',
          icon: 'none'
        })
      }
    },
    
    // 格式化时间
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      return date.toLocaleString()
    },
    
    // 获取订单状态文本
    getStatusText(status) {
      const statusMap = {
        0: '待支付',
        1: '已支付',
        2: '已完成',
        3: '已取消',
        4: '退单'
      }
      return statusMap[status] || '未知状态'
    },
    
    // 获取订单状态标签类型
    getStatusType(status) {
      const typeMap = {
        0: 'warning',
        1: 'success',
        2: 'success',
        3: 'error',
        4: 'error'
      }
      return typeMap[status] || 'default'
    },
    
    // 获取订单操作按钮类型
    getStatusBtnType(status) {
      const typeMap = {
        1: 'primary'
      }
      return typeMap[status] || 'default'
    },
    
    // 获取订单操作文本
    getActionText(status) {
      const actionMap = {
        1: '标记发货'
      }
      return actionMap[status] || ''
    },
    
    // 获取支付方式文本
    getPaymentMethodText(method) {
      const methodMap = {
        1: '微信支付',
        2: '支付宝',
        3: '银行卡'
      }
      return methodMap[method] || '其他支付方式'
    },
    
    // 获取评价标签
    getEvaluateTags(etype) {
      const tags = []
      const typeMap = {
        '1': '回头客',
        '2': '是正品',
        '3': '口感好',
        '4': '会回购'
      }
      
      if (etype) {
        etype.split(',').forEach(type => {
          if (typeMap[type]) {
            tags.push(typeMap[type])
          }
        })
      }
      
      return tags
    },
    
    // 预览评价图片
    previewImage(url) {
      uni.previewImage({
        urls: [url],
        current: url
      })
    }
  }
}
</script>

<style scoped>
.order-detail-container {
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 顶部导航 */
.top-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  background-color: #fff;
  border-bottom: 1rpx solid #eee;
  padding: 0 20rpx;
  position: sticky;
  top: 0;
  z-index: 999;
}

.nav-left {
  display: flex;
  align-items: center;
  width: 100rpx;
}

.nav-left text {
  margin-left: 10rpx;
  font-size: 32rpx;
}

.nav-title {
  font-size: 36rpx;
  font-weight: bold;
}

.nav-right {
  width: 100rpx;
}

/* 卡片样式 */
.order-card,
.customer-card,
.address-card,
.goods-card,
.price-card,
.payment-card,
.logistics-card {
  margin: 20rpx;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
}

/* 地址信息 */
.address-detail {
  word-break: break-all;
  line-height: 40rpx;
  text-align: right;
  flex: 1;
  text-align: left;
  margin-left: 20rpx;
}

.address-info {
  display: flex;
  flex-direction: column;
}

/* 订单卡片 */
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.order-id {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.order-info {
  display: flex;
  flex-direction: column;
}

.info-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15rpx;
  font-size: 28rpx;
}

.info-label {
  color: #999;
}

.info-value {
  color: #333;
}

/* 商品列表 */
.goods-list {
  display: flex;
  flex-direction: column;
}

.goods-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #eee;
}

.goods-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.goods-icon {
  width: 120rpx;
  height: 120rpx;
  border-radius: 8rpx;
  margin-right: 20rpx;
}

.goods-details {
  flex: 1;
  overflow: hidden;
}

.goods-name {
  font-size: 30rpx;
  color: #333;
  margin-bottom: 10rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.goods-spec {
  font-size: 26rpx;
  color: #999;
}

.goods-price {
  text-align: right;
}

.price {
  font-size: 32rpx;
  color: #ff4444;
  font-weight: bold;
}

.quantity {
  font-size: 28rpx;
  color: #999;
  margin-top: 10rpx;
}

/* 价格信息 */
.price-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.price-item {
  display: flex;
  margin-bottom: 15rpx;
  font-size: 28rpx;
}

.price-label {
  color: #999;
  margin-right: 20rpx;
}

.price-value {
  color: #333;
}

.price-item.total {
  font-weight: bold;
  margin-top: 10rpx;
  padding-top: 15rpx;
  border-top: 1rpx solid #eee;
  width: 100%;
  justify-content: space-between;
}

.price-item.total .price-value {
  color: #ff4444;
  font-size: 32rpx;
}

/* 物流信息 */
.logistics-info {
  display: flex;
  flex-direction: column;
}

.logistics-item {
  margin-bottom: 25rpx;
  padding-left: 30rpx;
  position: relative;
}

.logistics-item::before {
  content: '';
  position: absolute;
  left: 10rpx;
  top: 10rpx;
  width: 8rpx;
  height: 8rpx;
  border-radius: 50%;
  background-color: #007aff;
}

.logistics-time {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 8rpx;
}

.logistics-content {
  font-size: 28rpx;
  color: #333;
  line-height: 40rpx;
}

.no-logistics {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60rpx 0;
  color: #999;
}

.no-logistics text {
  margin-top: 20rpx;
  font-size: 28rpx;
}

/* 订单操作按钮 */
.order-actions {
  padding: 20rpx;
  background-color: #fff;
  margin-top: 20rpx;
  position: sticky;
  bottom: 0;
}

.action-btn {
  width: 100%;
  height: 80rpx;
  line-height: 80rpx;
  font-size: 32rpx;
  border-radius: 40rpx;
  color: #fff;
}

/* 评价卡片样式 */
.evaluate-card {
  margin: 20rpx;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.evaluate-list {
  display: flex;
  flex-direction: column;
}

.evaluate-item {
  margin-bottom: 30rpx;
  padding-bottom: 30rpx;
  border-bottom: 1rpx solid #eee;
}

.evaluate-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.evaluate-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.customer-info {
  display: flex;
  align-items: center;
}

.customer-avatar {
  width: 60rpx;
  height: 60rpx;
  border-radius: 50%;
  margin-right: 15rpx;
}

.customer-name {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
}

.evaluate-content {
  font-size: 28rpx;
  color: #333;
  line-height: 44rpx;
  margin-bottom: 15rpx;
}

.evaluate-tags {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 15rpx;
}

.evaluate-tags uni-tag {
  margin-right: 10rpx;
  margin-bottom: 10rpx;
}

.evaluate-pics {
  display: flex;
  flex-wrap: wrap;
}

.evaluate-pic {
  width: 120rpx;
  height: 120rpx;
  border-radius: 8rpx;
  margin-right: 15rpx;
  margin-bottom: 15rpx;
}

.evaluate-pic:last-child {
  margin-right: 0;
}
</style>