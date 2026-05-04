<!-- pages/logistics/logistics.vue -->
<template>
  <view class="logistics-container">
    <!-- 头部 -->
    <view class="header">
      <view class="header-left" @click="goBack">
        <uni-icons type="arrowleft" size="25" color="#333"></uni-icons>
      </view>
      <view class="header-title">物流信息</view>
      <view class="header-right"></view>
    </view>

    <!-- 包裹信息 -->
    <view class="package-info">
      <view class="info-item">
        <text class="label">快递公司：</text>
        <text class="value">{{ logisticsInfo.company || '顺丰速运' }}</text>
      </view>
      <view class="info-item">
        <text class="label">运单号码：</text>
        <text class="value">{{ logisticsInfo.trackingNumber || 'SF1234567890123' }}</text>
      </view>
      <view class="info-item">
        <text class="label">收货人：</text>
        <text class="value">{{ logisticsInfo.receiver || '红红儿' }}</text>
      </view>
      <view class="info-item">
        <text class="label">联系电话：</text>
        <text class="value">{{ logisticsInfo.phone || '138****8888' }}</text>
      </view>
      <view class="info-item">
        <text class="label">收货地址：</text>
        <text class="value">{{ logisticsInfo.address || '北京市海淀区中关村大街' }}</text>
      </view>
    </view>

    <!-- 物流轨迹 -->
    <view class="tracking-section">
      <view class="section-title">物流轨迹</view>
      
      <!-- 时间轴 -->
      <view class="timeline">
        <view class="timeline-item" v-for="(item, index) in trackingList" :key="index"
              :class="{ 'active': index === 0 }">
          <view class="timeline-dot">
            <uni-icons v-if="index === 0" type="checkbox-filled" size="16" color="#07c160"></uni-icons>
            <view v-else class="dot"></view>
          </view>
          <view class="timeline-content">
            <view class="time">{{ item.time }}</view>
            <view class="status">{{ item.status }}</view>
            <view class="location" v-if="item.location">{{ item.location }}</view>
          </view>
        </view>
      </view>
    </view>

    <!-- 复制运单号 -->
    <view class="copy-btn" @click="copyTrackingNumber">
      <uni-icons type="copy" size="20" color="#fff"></uni-icons>
      <text>复制运单号</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      orderId: '',
      logisticsInfo: {
        company: '顺丰速运',
        trackingNumber: 'SF1234567890123',
        receiver: '红红儿',
        phone: '138****8888',
        address: '北京市海淀区中关村大街'
      },
      trackingList: [
        {
          time: '2023-10-15 14:30:00',
          status: '已签收，感谢使用顺丰，期待再次为您服务',
          location: '北京海淀区中关村配送站'
        },
        {
          time: '2023-10-15 08:15:00',
          status: '正在派送中',
          location: '北京海淀区配送中心'
        },
        {
          time: '2023-10-14 22:30:00',
          status: '快件到达北京转运中心',
          location: '北京转运中心'
        },
        {
          time: '2023-10-14 18:45:00',
          status: '快件在【上海转运中心】装车，已发往北京',
          location: '上海转运中心'
        },
        {
          time: '2023-10-14 14:20:00',
          status: '快件已揽收',
          location: '上海浦东新区营业点'
        }
      ]
    };
  },
  onLoad(options) {
    if (options.orderId) {
      this.orderId = options.orderId;
      this.loadLogisticsInfo();
    }
  },
  methods: {
    goBack() {
      uni.navigateBack();
    },

    loadLogisticsInfo() {
      // 模拟API请求
      console.log('加载订单物流信息，订单ID:', this.orderId);
      
      // 实际开发中调用API
      // uni.request({
      //   url: '/api/logistics/' + this.orderId,
      //   success: (res) => {
      //     this.logisticsInfo = res.data.info;
      //     this.trackingList = res.data.tracking;
      //   }
      // });
    },

    copyTrackingNumber() {
      uni.setClipboardData({
        data: this.logisticsInfo.trackingNumber,
        success: () => {
          uni.showToast({
            title: '运单号已复制',
            icon: 'success'
          });
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.logistics-container {
  min-height: 100vh;
  background-color: #f5f5f5;

  .header {
    height: 88rpx;
    background-color: #fff;
    display: flex;
    align-items: center;
    padding: 0 30rpx;
    border-bottom: 1rpx solid #eee;

    .header-left {
      width: 60rpx;
    }

    .header-title {
      flex: 1;
      text-align: center;
      font-size: 34rpx;
      font-weight: bold;
      color: #333;
    }

    .header-right {
      width: 60rpx;
    }
  }

  .package-info {
    background-color: #fff;
    margin: 20rpx;
    padding: 30rpx;
    border-radius: 16rpx;

    .info-item {
      display: flex;
      margin-bottom: 20rpx;
      font-size: 28rpx;

      &:last-child {
        margin-bottom: 0;
      }

      .label {
        width: 140rpx;
        color: #666;
        flex-shrink: 0;
      }

      .value {
        flex: 1;
        color: #333;
        word-break: break-all;
      }
    }
  }

  .tracking-section {
    background-color: #fff;
    margin: 20rpx;
    padding: 30rpx;
    border-radius: 16rpx;

    .section-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 30rpx;
    }

    .timeline {
      .timeline-item {
        display: flex;
        margin-bottom: 40rpx;
        position: relative;

        &:last-child {
          margin-bottom: 0;
          
          &::after {
            display: none;
          }
        }

        &.active {
          .timeline-content {
            .time,
            .status {
              color: #07c160;
            }
          }
        }

        &::after {
          content: '';
          position: absolute;
          left: 16rpx;
          top: 36rpx;
          width: 2rpx;
          height: calc(100% + 40rpx);
          background-color: #e0e0e0;
          z-index: 1;
        }

        .timeline-dot {
          width: 36rpx;
          height: 36rpx;
          background-color: #fff;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          z-index: 2;
          position: relative;

          .dot {
            width: 16rpx;
            height: 16rpx;
            border-radius: 50%;
            background-color: #ccc;
          }
        }

        .timeline-content {
          flex: 1;
          margin-left: 30rpx;

          .time {
            font-size: 26rpx;
            color: #999;
            margin-bottom: 8rpx;
          }

          .status {
            font-size: 30rpx;
            color: #333;
            margin-bottom: 8rpx;
            line-height: 1.4;
          }

          .location {
            font-size: 26rpx;
            color: #999;
          }
        }
      }
    }
  }

  .copy-btn {
    position: fixed;
    bottom: 40rpx;
    left: 30rpx;
    right: 30rpx;
    height: 88rpx;
    background: linear-gradient(90deg, #ff4d4f, #ff7a45);
    border-radius: 44rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    font-size: 32rpx;
    font-weight: bold;

    uni-icons {
      margin-right: 10rpx;
    }
  }
}
</style>