<template>
  <view class="orders-list-container">
    <!-- 顶部搜索栏 -->
    <view class="search-section">
      <uni-search-bar
        v-model="searchKeyword"
        placeholder="搜索订单ID或商品名称"
        @confirm="onSearch"
      ></uni-search-bar>
    </view>

    <!-- 订单状态筛选 -->
    <view class="status-filter">
      <view 
        class="filter-item" 
        :class="{ active: selectedStatus === null }"
        @click="filterByStatus(null)"
      >
        全部
      </view>
      <view 
        class="filter-item" 
        :class="{ active: selectedStatus === 0 }"
        @click="filterByStatus(0)"
      >
        待支付
      </view>
      <view 
        class="filter-item" 
        :class="{ active: selectedStatus === 1 }"
        @click="filterByStatus(1)"
      >
        已支付
      </view>
      <view 
        class="filter-item" 
        :class="{ active: selectedStatus === 2 }"
        @click="filterByStatus(2)"
      >
        已完成
      </view>
      <view 
        class="filter-item" 
        :class="{ active: selectedStatus === 3 }"
        @click="filterByStatus(3)"
      >
        已取消
      </view>
      <view 
        class="filter-item" 
        :class="{ active: selectedStatus === 4 }"
        @click="filterByStatus(4)"
      >
        退单
      </view>
    </view>

    <!-- 订单列表 -->
    <view class="orders-list">
      <uni-card
        v-for="order in ordersList"
        :key="order.orderid"
        :title="`订单ID: ${order.orderid}`"
      >
        <!-- 订单基本信息 -->
        <view class="order-basic-info">
          <view class="customer-info">
            <text class="label">客户:</text>
            <text class="value">{{ order.customer?.username || '未知客户' }}</text>
          </view>
          <view class="order-time">
            <text class="label">下单时间:</text>
            <text class="value">{{ formatTime(order.ordertime) }}</text>
          </view>
          <view class="order-status">
            <uni-tag :type="getStatusType(order.status)">
              {{ getStatusText(order.status) }}
            </uni-tag>
          </view>
        </view>

        <!-- 订单商品列表 -->
        <view class="order-goods">
          <view class="goods-item" v-for="(detail, index) in order.ordersDetail" :key="index">
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

        <!-- 订单金额信息 -->
        <view class="order-amount">
          <view class="amount-item">
            <text class="label">商品总价:</text>
            <text class="value">¥{{ order.totalPrice }}</text>
          </view>
          <view class="amount-item">
            <text class="label">运费:</text>
            <text class="value">¥{{ order.freightPrice || 0 }}</text>
          </view>
          <view class="amount-item total">
            <text class="label">订单总价:</text>
            <text class="value">¥{{ order.totalPrice + (order.freightPrice || 0) }}</text>
          </view>
        </view>

        <!-- 订单操作按钮 -->
        <view class="order-actions">
          <button class="action-btn detail-btn" @click="viewOrderDetail(order.orderid)">
            查看详情
          </button>
          <button 
            class="action-btn status-btn" 
            :type="getStatusBtnType(order.status)"
            @click="handleOrderAction(order)"
          >
            {{ getActionText(order.status) }}
          </button>
        </view>
      </uni-card>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="ordersList.length === 0">
      <uni-icons type="document" size="80" color="#ccc"></uni-icons>
      <text class="empty-text">暂无订单数据</text>
    </view>

    <!-- 加载更多 -->
    <view class="load-more" v-if="ordersList.length > 0">
      <uni-load-more :status="loadStatus" @clickLoadMore="loadMore"></uni-load-more>
    </view>
  </view>
</template>

<script>
import request from '../../utils/request.js'

export default {
  data() {
    return {
      sellerInfo: null,
      // 订单列表数据
      ordersList: [],
      // 搜索关键字
      searchKeyword: '',
      // 选中的状态筛选
      selectedStatus: null,
      // 加载状态
      loadStatus: 'more', // more, loading, noMore
      // 当前页码
      page: 1,
      // 是否还有更多数据
      hasMore: true,
      // 筛选条件
      filterParams: {
        status: null,
        keyword: ''
      }
    }
  },
  onShow() {
    // 检查登录状态
    this.checkLoginStatus()
    // 重置数据
    this.resetData()
    // 加载订单列表
    this.loadOrdersList()
  },
  methods: {
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
    
    // 重置数据
    resetData() {
      this.page = 1
      this.ordersList = []
      this.hasMore = true
      this.loadStatus = 'more'
      this.filterParams = {
        status: null,
        keyword: ''
      }
    },

    // 加载订单列表
    async loadOrdersList() {
      if (!this.hasMore) {
        this.loadStatus = 'noMore'
        return
      }

      this.loadStatus = 'loading'

      try {
        // 获取店铺ID
        const sid = this.sellerInfo.shopInfo?.sid
        if (!sid) {
          console.error('店铺ID不存在')
          this.loadStatus = 'more'
          return
        }
        
        let url = '/seller/orders/list'
        let params = {
          page: this.page,
          sid: sid
        }
        
        // 如果是搜索，使用搜索接口
        if (this.filterParams.keyword) {
          url = '/seller/orders/search'
          params.keyword = this.filterParams.keyword
        }
        
        // 发送请求
        const res = await request.get(url, params)
        
        const data = res.data
        if (this.page === 1) {
          this.ordersList = data
        } else {
          this.ordersList = [...this.ordersList, ...data]
        }
        
        // 筛选订单状态
        if (this.filterParams.status !== null) {
          this.ordersList = this.ordersList.filter(order => order.status === this.filterParams.status)
        }
        
        // 检查是否还有更多数据
        this.hasMore = data.length === 10
        this.loadStatus = this.hasMore ? 'more' : 'noMore'
      } catch (error) {
        console.error('加载订单列表失败:', error)
        this.loadStatus = 'more'
      }
    },

    // 搜索订单
    onSearch() {
      this.filterParams.keyword = this.searchKeyword
      this.page = 1
      this.ordersList = []
      this.hasMore = true
      this.loadOrdersList()
    },

    // 按状态筛选订单
    filterByStatus(status) {
      this.selectedStatus = status
      this.filterParams.status = status
      this.page = 1
      this.ordersList = []
      this.hasMore = true
      this.loadOrdersList()
    },

    // 加载更多订单
    loadMore() {
      if (this.loadStatus === 'more') {
        this.page++
        this.loadOrdersList()
      }
    },

    // 查看订单详情
    viewOrderDetail(orderid) {
      uni.navigateTo({
        url: `/pages/orders/detail?id=${orderid}`
      })
    },

    // 处理订单操作
    handleOrderAction(order) {
      switch (order.status) {
        case 0: // 待支付
          // 可以添加提醒客户支付等操作
          uni.showToast({
            title: '待支付订单',
            icon: 'none'
          })
          break
        case 1: // 已支付
          // 可以添加发货等操作
          this.updateOrderStatus(order.orderid, 2)
          break
        case 2: // 已完成
          // 可以添加评价管理等操作
          uni.showToast({
            title: '订单已完成',
            icon: 'none'
          })
          break
        default:
          break
      }
    },

    // 更新订单状态
    async updateOrderStatus(orderid, status) {
      try {
        const sid = this.sellerInfo.shopInfo?.sid
        await request.put('/seller/orders/status', {
          sid: sid,
          orderid: orderid,
          status: status
        })
        
        // 刷新订单列表
        this.loadOrdersList()
        
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
        0: 'default',
        1: 'primary',
        2: 'default',
        3: 'default',
        4: 'default'
      }
      return typeMap[status] || 'default'
    },

    // 获取订单操作文本
    getActionText(status) {
      const actionMap = {
        0: '提醒支付',
        1: '标记发货',
        2: '查看详情',
        3: '查看详情',
        4: '查看详情'
      }
      return actionMap[status] || '查看详情'
    }
  }
}
</script>

<style scoped>
.orders-list-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.search-section {
  margin-bottom: 20rpx;
}

.status-filter {
  display: flex;
  background-color: #fff;
  border-radius: 8rpx;
  padding: 10rpx;
  margin-bottom: 20rpx;
  overflow-x: auto;
}

.filter-item {
  padding: 10rpx 20rpx;
  margin-right: 10rpx;
  border-radius: 20rpx;
  font-size: 28rpx;
  color: #666;
  white-space: nowrap;
}

.filter-item.active {
  background-color: #007aff;
  color: #fff;
}

.orders-list {
  margin-bottom: 20rpx;
}

.order-basic-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  flex-wrap: wrap;
}

.customer-info,
.order-time {
  display: flex;
  align-items: center;
  margin-bottom: 10rpx;
  width: 100%;
}

.label {
  color: #999;
  margin-right: 10rpx;
  font-size: 28rpx;
}

.value {
  color: #333;
  font-size: 28rpx;
}

.order-status {
  margin-left: auto;
}

.order-goods {
  margin-bottom: 20rpx;
}

.goods-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
  padding-bottom: 15rpx;
  border-bottom: 1rpx solid #eee;
}

.goods-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.goods-icon {
  width: 100rpx;
  height: 100rpx;
  border-radius: 8rpx;
  margin-right: 20rpx;
}

.goods-details {
  flex: 1;
  overflow: hidden;
}

.goods-name {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 5rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.goods-spec {
  font-size: 26rpx;
  color: #999;
}

.goods-price {
  text-align: right;
}

.price {
  font-size: 28rpx;
  color: #ff4444;
  font-weight: bold;
}

.quantity {
  font-size: 26rpx;
  color: #999;
}

.order-amount {
  text-align: right;
  margin-bottom: 20rpx;
}

.amount-item {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10rpx;
}

.amount-item.total {
  font-weight: bold;
}

.order-actions {
  display: flex;
  justify-content: flex-end;
}

.action-btn {
  margin-left: 15rpx;
  padding: 0 30rpx;
  height: 60rpx;
  line-height: 60rpx;
  font-size: 28rpx;
  border-radius: 30rpx;
}

.detail-btn {
  border: 1rpx solid #007aff;
  color: #007aff;
  background-color: #fff;
}

.status-btn {
  color: #fff;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
  color: #999;
}

.empty-text {
  margin-top: 20rpx;
  font-size: 28rpx;
}

.load-more {
  padding: 20rpx 0;
  text-align: center;
}
</style>