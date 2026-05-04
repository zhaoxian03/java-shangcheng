<template>
  <view class="user-info-page">
    <!-- 用户名（只读，从数据库查询） -->
    <view class="info-item">
      <label>用户名：</label>
      <view class="info-value">{{ userInfo.username || '未获取' }}</view>
    </view>

    <!-- 电话（可编辑） -->
    <view class="info-item">
      <label>联系电话：</label>
      <input v-model="formData.phone" type="tel" placeholder="请输入手机号" />
    </view>

    <!-- 地址（可编辑） -->
    <view class="info-item">
      <label>详细地址：</label>
      <input v-model="formData.address" type="text" placeholder="请输入详细地址" />
    </view>

    <!-- 保存修改按钮 -->
    <button class="save-btn" @click="saveUserInfo">保存修改</button>
  </view>
</template>

<script>
const app = getApp()
export default {
  data() {
    return {
      userInfo: {}, // 存储用户查询结果
      formData: {
        // 全局用户ID转为String类型，与后端接口参数一致
        userid: String(app.globalData.cid || 1),
        phone: '',
        address: ''
      }
    }
  },

  // 页面加载时查询用户信息
  onLoad() {
    this.getUserInfo()
  },

  methods: {
    // 1. 查询用户信息（调用后端/api/user/info接口）
    getUserInfo() {
      uni.request({
        url: 'http://localhost:8080/api/user/info', // 后端接口地址，替换为你的实际端口
        method: 'GET',
        data: {
          userid: this.formData.userid // 传递String类型用户ID，与后端一致
        },
        success: (res) => {
          if (res.data) {
            this.userInfo = res.data
            // 初始化表单数据（填充已有电话和地址）
            this.formData.phone = res.data.phone || ''
            this.formData.address = res.data.address || ''
          } else {
            uni.showToast({ title: '未查询到用户信息', icon: 'error' })
          }
        },
        fail: () => {
          uni.showToast({ title: '网络异常，查询失败', icon: 'error' })
        }
      })
    },

    // 2. 保存用户信息（调用后端/api/user/update接口）
    saveUserInfo() {
      // 表单校验
      if (!this.formData.phone.trim()) {
        return uni.showToast({ title: '请填写联系电话', icon: 'error' })
      }
      if (!this.formData.address.trim()) {
        return uni.showToast({ title: '请填写详细地址', icon: 'error' })
      }

      uni.request({
        url: 'http://localhost:8080/api/user/update', // 后端接口地址
        method: 'PUT',
        data: this.formData, // 传递userid、phone、address，与后端接口一致
        success: (res) => {
          if (res.data) {
            uni.showToast({ title: '修改成功', icon: 'success' })
            this.getUserInfo() // 重新查询最新用户信息
          } else {
            uni.showToast({ title: '修改失败', icon: 'error' })
          }
        },
        fail: () => {
          uni.showToast({ title: '网络异常，保存失败', icon: 'error' })
        }
      })
    }
  }
}
</script>

<style scoped>
.user-info-page {
  padding: 30rpx;
  background-color: #ffffff;
  min-height: 100vh;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 40rpx;
  font-size: 32rpx;
}

.info-item label {
  width: 160rpx;
  color: #666666;
}

.info-item .info-value {
  flex: 1;
  color: #333333;
}

.info-item input {
  flex: 1;
  padding: 15rpx 20rpx;
  border: 1rpx solid #eeeeee;
  border-radius: 8rpx;
  outline: none;
  color: #333333;
}

.save-btn {
  width: 90%;
  height: 80rpx;
  line-height: 80rpx;
  background-color: cornflowerblue;
  color: #ffffff;
  border: none;
  border-radius: 40rpx;
  margin: 50rpx auto 0;
  display: block;
  font-size: 32rpx;
}
</style>