<template>
  <view class="goods-list-container">
    <!-- 顶部搜索栏 -->
    <view class="search-section">
      <uni-search-bar
        v-model="searchKeyword"
        placeholder="搜索商品名称"
        @confirm="onSearch"
      ></uni-search-bar>
      <button class="add-btn" @click="goToAddGoods">
        <uni-icons type="plus" size="24" color="#fff"></uni-icons>
        新增商品
      </button>
    </view>

    <!-- 商品列表 -->
    <view class="goods-list">
      <uni-card
        v-for="item in goodsList"
        :key="item.gid"
        :title="item.gname"
        :thumb="item.icon.split(';')[0]"
      >
        <view class="goods-info">
          <view class="goods-basic">
            <text class="goods-price">¥{{ item.price }}</text>
            <text class="goods-stock">库存: {{ item.stockTotalNum }}</text>
          </view>
          <view class="goods-status">
            <uni-tag :type="item.gstatus === 1 ? 'success' : 'danger'">
              {{ item.gstatus === 1 ? '已上架' : '已下架' }}
            </uni-tag>
          </view>
        </view>

        <view class="goods-actions">
          <button class="action-btn edit-btn" @click="goToEditGoods(item)">
            <uni-icons type="compose" size="20" color="#fff"></uni-icons>
            编辑
          </button>
          <button 
            class="action-btn status-btn" 
            :type="item.gstatus === 1 ? 'warn' : 'success'"
            @click="toggleStatus(item)"
          >
            <uni-icons :type="item.gstatus === 1 ? 'close' : 'checkmark'" size="20" color="#fff"></uni-icons>
            {{ item.gstatus === 1 ? '下架' : '上架' }}
          </button>
          <button class="action-btn delete-btn" @click="deleteGoods(item)">
            <uni-icons type="trash" size="20" color="#fff"></uni-icons>
            删除
          </button>
        </view>
      </uni-card>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="goodsList.length === 0">
      <uni-icons type="shop" size="80" color="#ccc"></uni-icons>
      <text class="empty-text">暂无商品数据</text>
      <button class="empty-btn" @click="goToAddGoods">去添加商品</button>
    </view>

    <!-- 加载更多 -->
    <view class="load-more" v-if="goodsList.length > 0">
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
      searchKeyword: '',
      goodsList: [],
      loadStatus: 'more', // more, loading, noMore
      page: 1,
      pageSize: 10,
      hasMore: true
    }
  },
  onShow() {
    // 检查登录状态
    this.checkLoginStatus()
    // 重置数据
    this.resetData()
    // 加载商品列表
    this.loadGoodsList()
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
      this.goodsList = []
      this.hasMore = true
      this.loadStatus = 'more'
    },

    // 加载商品列表
    async loadGoodsList() {
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
        
        // 调用接口获取商品列表
        const res = await request.get('/seller/goods/list', {
          page: this.page,
          pageSize: this.pageSize,
          keyword: this.searchKeyword,
          sid: sid
        })
        
        const data = res.data
        if (this.page === 1) {
          this.goodsList = data.list
        } else {
          this.goodsList = [...this.goodsList, ...data.list]
        }
        
        this.hasMore = this.page < data.totalPages
        this.loadStatus = this.hasMore ? 'more' : 'noMore'
        this.page++
      } catch (error) {
        // 错误已在request工具中处理
        this.loadStatus = 'more'
      }
    },

    // 加载更多
    loadMore() {
      this.loadGoodsList()
    },

    // 搜索商品
    onSearch() {
      this.resetData()
      this.loadGoodsList()
    },

    // 跳转到新增商品页面
    goToAddGoods() {
      uni.navigateTo({
        url: '/pages/goods/add'
      })
    },

    // 跳转到编辑商品页面
    goToEditGoods(item) {
      uni.navigateTo({
        url: `/pages/goods/edit?gid=${item.gid}`
      })
    },

    // 切换商品状态
    async toggleStatus(item) {
      uni.showModal({
        title: '提示',
        content: `确定要${item.gstatus === 1 ? '下架' : '上架'}该商品吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              // 获取店铺ID
              const sid = this.sellerInfo.shopInfo?.sid
              if (!sid) {
                uni.showToast({
                  title: '店铺信息错误',
                  icon: 'none'
                })
                return
              }
              
              // 调用接口更新状态
              const newStatus = item.gstatus === 1 ? 0 : 1
              await request.put('/seller/goods/status', {
                gid: item.gid,
                gstatus: newStatus,
                sid: sid
              })
              
              item.gstatus = newStatus
              uni.showToast({
                title: `商品已${newStatus === 1 ? '上架' : '下架'}`,
                icon: 'success'
              })
            } catch (error) {
              // 错误已在request工具中处理
            }
          }
        }
      })
    },

    // 删除商品
    async deleteGoods(item) {
      uni.showModal({
        title: '警告',
        content: '确定要删除该商品吗？此操作不可恢复！',
        success: async (res) => {
          if (res.confirm) {
            try {
              // 获取店铺ID
              const sid = this.sellerInfo.shopInfo?.sid
              if (!sid) {
                uni.showToast({
                  title: '店铺信息错误',
                  icon: 'none'
                })
                return
              }
              
              // 调用接口删除商品
          await request.delete(`/seller/goods/${item.gid}?sid=${sid}`)
              
              const index = this.goodsList.findIndex(g => g.gid === item.gid)
              if (index !== -1) {
                this.goodsList.splice(index, 1)
              }
              uni.showToast({
                title: '商品已删除',
                icon: 'success'
              })
            } catch (error) {
              // 错误已在request工具中处理
            }
          }
        }
      })
    }
  }
}
</script>

<style lang="scss">
.goods-list-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.search-section {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.add-btn {
  margin-left: 10rpx;
  background-color: #007aff;
  color: #fff;
  border-radius: 20rpx;
  font-size: 24rpx;
  height: 60rpx;
  line-height: 60rpx;
  padding: 0 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.goods-list {
  margin-bottom: 20rpx;
}

.goods-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.goods-basic {
  display: flex;
  flex-direction: column;
}

.goods-price {
  font-size: 32rpx;
  font-weight: bold;
  color: #ff3b30;
  margin-bottom: 10rpx;
}

.goods-stock {
  font-size: 24rpx;
  color: #666;
}

.goods-status {
  margin-left: 20rpx;
}

.goods-actions {
  display: flex;
  gap: 10rpx;
}

.action-btn {
  flex: 1;
  height: 60rpx;
  line-height: 60rpx;
  font-size: 24rpx;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  border: none;
}

.edit-btn {
  background-color: #007aff;
}

.status-btn {
  background-color: #ff9500;
}

.status-btn.success {
  background-color: #4cd964;
}

.delete-btn {
  background-color: #ff3b30;
}

.empty-state {
  text-align: center;
  padding: 100rpx 0;
  background-color: #fff;
  border-radius: 16rpx;
}

.empty-text {
  display: block;
  margin: 30rpx 0;
  font-size: 28rpx;
  color: #666;
}

.empty-btn {
  background-color: #007aff;
  color: #fff;
  border-radius: 8rpx;
  font-size: 28rpx;
  height: 70rpx;
  line-height: 70rpx;
  padding: 0 40rpx;
}

.load-more {
  padding: 20rpx 0;
  text-align: center;
}
</style>