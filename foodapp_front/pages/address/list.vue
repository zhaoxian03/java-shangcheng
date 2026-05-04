<template>
  <view class="address-page">
    <uni-nav-bar title="收货地址" left-icon="back" @clickLeft="goBack":border="false" />
    
    <scroll-view class="address-list" scroll-y :refresher-enabled="true" @refresherrefresh="onRefresh">
      <!-- 地址列表 -->
      <view v-if="loading">
        <view class="loading">加载中...</view>
      </view>
      
      <view v-else>
        <view 
          class="address-item" 
          v-for="item in addressList" 
          :key="item.id" 
          @click="selectAddress(item)"
        >
          <view class="address-header">
            <text class="name">{{ item.name }}</text>
            <text class="phone">{{ item.phone }}</text>
            <text class="default-tag" v-if="item.isDefault === 1">默认</text>
          </view>
          
          <view class="address-content">
            <text class="address-text">{{ item.province }}{{ item.city }}{{ item.district }}{{ item.detail }}</text>
          </view>
          
          <view class="address-actions">
            <view class="action-left">
              <view class="action-item" @click.stop="editAddress(item)">
                <uni-icons type="compose" size="18" color="#666"></uni-icons>
                <text>编辑</text>
              </view>
              <view class="action-item" @click.stop="deleteAddress(item.id)">
                <uni-icons type="trash" size="18" color="#666"></uni-icons>
                <text>删除</text>
              </view>
            </view>
            <view class="action-right">
              <!-- 条件渲染：默认地址显示红色标签，非默认地址显示开关 -->
              <view v-if="item.isDefault === 1" class="default-tag-container" @click="setDefaultAddress(item.id)">
                <text class="default-check-icon">✓</text>
                <text class="default-tag-text">已设默认</text>
              </view>
              <view v-else class="default-switch-container">
                <switch 
                  :checked="false" 
                  @change="setDefaultAddress(item.id)"
                  color="#ff4757"
                />
                <text class="default-text">默认</text>
              </view>
            </view>
          </view>
        </view>
        
        <!-- 空状态 -->
        <view class="empty-state" v-if="addressList.length === 0">
          <image src="/static/other/address-empty.png" class="empty-icon"></image>
          <text class="empty-text">暂无收货地址</text>
          <text class="empty-tip">快去添加一个吧～</text>
        </view>
      </view>
    </scroll-view>
    
    <!-- 添加按钮 -->
    <view class="add-btn-wrapper">
      <view class="add-btn" @click="goToAdd">
        <uni-icons type="plus" size="24" color="#fff"></uni-icons>
        <text>添加收货地址</text>
      </view>
    </view>
  </view>
</template>

<script>
import { addressApi } from '@/api/address.js';
import { userApi } from '@/api/user.js';

export default {
  data() {
    return {
      userId: null,  // 从登录信息获取
      addressList: [],
      loading: false,
      userToken: null
    };
  },
  
  onLoad(options) {
    console.log('地址列表页面参数:', options);
    
    // 获取用户ID和token
    this.getUserInfo();
    
    // 如果有参数传递的userId，使用参数的
    if (options.userId) {
      this.userId = options.userId;
    }
    
    console.log('当前用户ID:', this.userId);
    
    if (!this.userId) {
      uni.showToast({
        title: '请先登录',
        icon: 'none'
      });
      
      // 返回上一页
      setTimeout(() => {
        uni.navigateBack();
      }, 1500);
      
      return;
    }
    
    this.loadAddressList();
  },
  
  onShow() {
    // 页面显示时刷新数据
    if (this.userId) {
      this.loadAddressList();
    }
  },
  
  methods: {
    // 获取用户信息
    getUserInfo() {
      const app = getApp();
      
      // 先从全局获取
      this.userId = app.globalData.userId;
      this.userToken = app.globalData.userToken;
      
      // 如果全局没有，从本地存储获取
      if (!this.userId) {
        this.userId = uni.getStorageSync('userId');
        this.userToken = uni.getStorageSync('userToken');
        
        // 更新到全局
        app.globalData.userId = this.userId;
        app.globalData.userToken = this.userToken;
      }
    },
    
    goBack() {
      uni.navigateBack();
    },
    
    // 加载地址列表
    async loadAddressList() {
      this.loading = true;
      try {
        const data = await addressApi.getList(this.userId);
        this.addressList = data || [];
        
        // 调试信息：打印地址列表数据
        console.log('📋 地址列表数据:', this.addressList);
        
        // 检查是否有默认地址
        const hasDefault = this.addressList.some(item => item.isDefault === 1);
        console.log('🔍 是否有默认地址:', hasDefault);
      } catch (error) {
        console.error('加载地址列表失败:', error);
        uni.showToast({
          title: error || '加载失败',
          icon: 'none'
        });
        
        // 如果token可能过期，尝试刷新
        if (error.includes('未授权') || error.includes('Token')) {
          this.checkAndRefreshToken();
        }
      } finally {
        this.loading = false;
      }
    },
    
    // 检查并刷新token
    async checkAndRefreshToken() {
      try {
        const token = uni.getStorageSync('userToken');
        if (token) {
          const result = await userApi.validateToken(token);
          if (result.code === 200) {
            // token有效，重新加载数据
            this.loadAddressList();
          } else {
            // token无效，清除登录状态
            this.clearLoginState();
          }
        }
      } catch (error) {
        console.error('验证token失败:', error);
      }
    },
    
    // 清除登录状态
    clearLoginState() {
      uni.removeStorageSync('userToken');
      uni.removeStorageSync('userId');
      uni.removeStorageSync('username');
      
      const app = getApp();
      app.globalData.isLogin = false;
      app.globalData.userId = null;
      app.globalData.userToken = null;
      
      uni.showToast({
        title: '登录已过期，请重新登录',
        icon: 'none'
      });
      
      // 返回到我的页面
      uni.switchTab({
        url: '/pages/my/my'
      });
    },
    
    // 下拉刷新
    async onRefresh() {
      try {
        await this.loadAddressList();
        uni.showToast({
          title: '刷新成功',
          icon: 'success'
        });
      } catch (error) {
        console.error('刷新失败:', error);
      }
    },
    
    // 选择地址（用于订单页面选择收货地址）
    selectAddress(item) {
      console.log('选择地址:', item);
      
      // 获取上一页和当前页参数
      const pages = getCurrentPages();
      const prevPage = pages[pages.length - 2];
      const fromParam = this.$mp.query.from;
      
      // 适配不同的订单页面路由
      const orderRoutes = ['pages/order/create', 'pages/confirmOrder/confirmOrder'];
      
      console.log('上一页路由:', prevPage ? prevPage.route : '无');
      console.log('from参数:', fromParam);
      
      // 如果是从订单页面跳转过来的，选择后返回
      if (prevPage && (orderRoutes.includes(prevPage.route) || fromParam === 'confirmOrder')) {
        // 使用事件通道传递数据
        const eventChannel = this.getOpenerEventChannel();
        if (eventChannel) {
          console.log('使用事件通道传递地址数据');
          eventChannel.emit('selectAddress', {
            ...item,
            // 确保字段名一致
            receiveName: item.name || item.receiveName,
            tel: item.phone || item.tel,
            detailAddress: item.detail || item.detailAddress
          });
        } else {
          // 兼容旧方式
          console.log('使用uni.$emit传递地址数据');
          uni.$emit('selectAddress', {
            ...item,
            receiveName: item.name || item.receiveName,
            tel: item.phone || item.tel,
            detailAddress: item.detail || item.detailAddress
          });
        }
        
        // 返回上一页
        uni.navigateBack();
        return;
      }
    },
    
    // 编辑地址
    editAddress(item) {
      uni.navigateTo({
        url: `/pages/address/edit?id=${item.id}&userId=${this.userId}`
      });
    },
    
    // 删除地址
    deleteAddress(id) {
      uni.showModal({
        title: '提示',
        content: '确定要删除这个地址吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await addressApi.delete(id, this.userId);
              uni.showToast({
                title: '删除成功',
                icon: 'success'
              });
              this.loadAddressList();
            } catch (error) {
              uni.showToast({
                title: error || '删除失败',
                icon: 'none'
              });
            }
          }
        }
      });
    },
    
    // 设置默认地址
    async setDefaultAddress(id) {
      try {
        await addressApi.setDefault(id, this.userId);
        uni.showToast({
          title: '设置成功',
          icon: 'success'
        });
        this.loadAddressList();
      } catch (error) {
        uni.showToast({
          title: error || '设置失败',
          icon: 'none'
        });
      }
    },
    
    // 跳转到添加地址页面
    goToAdd() {
      uni.navigateTo({
        url: `/pages/address/edit?userId=${this.userId}`
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.address-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  .address-list {
    height: calc(100vh - 200rpx - 120rpx);
    
    .loading {
      text-align: center;
      padding: 100rpx 0;
      color: #999;
    }
    
    .address-item {
      background: #fff;
      margin: 20rpx;
      padding: 30rpx;
      border-radius: 12rpx;
      box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
      
      .address-header {
        display: flex;
        align-items: center;
        margin-bottom: 20rpx;
        
        .name {
          font-size: 32rpx;
          font-weight: bold;
          color: #333;
          margin-right: 20rpx;
        }
        
        .phone {
          font-size: 28rpx;
          color: #666;
          margin-right: 20rpx;
        }
        
        .default-tag {
          background: #4a90e2;
          color: #fff;
          font-size: 22rpx;
          padding: 4rpx 12rpx;
          border-radius: 20rpx;
        }
      }
      
      .address-content {
        margin-bottom: 30rpx;
        
        .address-text {
          font-size: 28rpx;
          color: #333;
          line-height: 1.5;
        }
      }
      
      .address-actions {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-top: 20rpx;
        border-top: 1rpx solid #f0f0f0;
        
        .action-left {
          display: flex;
          gap: 30rpx;
          
          .action-item {
            display: flex;
            align-items: center;
            color: #666;
            font-size: 26rpx;
            
            uni-icons {
              margin-right: 8rpx;
            }
          }
        }
        
        .action-right {
          display: flex;
          align-items: center;
          
          /* 默认地址标签容器 */
          .default-tag-container {
            display: flex;
            align-items: center;
            cursor: pointer;
          }
          
          /* 已设默认标签样式 */
          .default-check-icon {
            color: #ff4757;
            font-weight: bold;
            margin-right: 5rpx;
          }
          
          .default-tag-text {
            color: #ff4757;
            font-size: 26rpx;
            font-weight: bold;
          }
          
          /* 开关容器 */
          .default-switch-container {
            display: flex;
            align-items: center;
          }
          
          .default-text {
            font-size: 26rpx;
            color: #666;
            margin-left: 10rpx;
          }
        }
      }
    }
    
    .empty-state {
      text-align: center;
      padding: 100rpx 0;
      
      .empty-icon {
        width: 200rpx;
        height: 200rpx;
        margin-bottom: 30rpx;
      }
      
      .empty-text {
        display: block;
        font-size: 32rpx;
        color: #999;
        margin-bottom: 10rpx;
      }
      
      .empty-tip {
        font-size: 26rpx;
        color: #ccc;
      }
    }
  }
  
  .add-btn-wrapper {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 20rpx;
    background: #fff;
    border-top: 1rpx solid #f0f0f0;
    
    .add-btn {
      background: linear-gradient(135deg, #4a90e2, #5a8bff);
      color: #fff;
      height: 90rpx;
      border-radius: 45rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 32rpx;
      font-weight: 500;
      
      uni-icons {
        margin-right: 10rpx;
      }
    }
  }
}
</style>