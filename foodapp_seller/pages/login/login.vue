<template>
  <view class="login-container">
    <!-- 顶部标题 -->
    <view class="login-header">
      <text class="login-title">商家登录</text>
      <text class="login-subtitle">欢迎回来</text>
    </view>

    <!-- 登录表单 -->
    <view class="login-form">
      <!-- 账号输入 -->
      <view class="form-item">
        <uni-easyinput
          v-model="form.username"
          placeholder="请输入账号"
          type="text"
          placeholder-style="color: #999"
        />
      </view>

      <!-- 密码输入 -->
      <view class="form-item">
        <uni-easyinput
          v-model="form.password"
          placeholder="请输入密码"
          type="password"
          placeholder-style="color: #999"
        />
      </view>

      <!-- 登录按钮 -->
      <button class="login-btn" @click="login">登录</button>

      <!-- 注册入口 -->
      <view class="register-link">
        <text>还没有账号？</text>
        <text class="link" @click="goRegister">立即注册</text>
      </view>
    </view>
  </view>
</template>

<script>
import request from '../../utils/request.js'

export default {
  data() {
    return {
      form: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    // 登录方法
    async login() {
      // 表单验证
      if (!this.form.username || !this.form.password) {
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        })
        return
      }

      try {
        // 调用登录接口
        const res = await request.post('/seller/login', this.form)
        
        // 登录成功，保存用户信息和token
        uni.setStorageSync('sellerInfo', res.data)
        uni.setStorageSync('token', res.data.token)
        
        // 跳转到商家中心，使用redirectTo替换当前页面，移除登录页的历史记录
        uni.redirectTo({
          url: '/pages/index/index'
        })
      } catch (error) {
        // 错误已在request工具中处理
      }
    },
    // 跳转到注册页面
    goRegister() {
      uni.navigateTo({
        url: '/pages/register/register'
      })
    }
  }
}
</script>

<style lang="scss">
.login-container {
  padding: 60rpx 40rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.login-header {
  text-align: center;
  margin-bottom: 80rpx;
  margin-top: 100rpx;
}

.login-title {
  font-size: 48rpx;
  font-weight: bold;
  color: #333;
  display: block;
  margin-bottom: 20rpx;
}

.login-subtitle {
  font-size: 28rpx;
  color: #666;
}

.login-form {
  flex: 1;
}

.form-item {
  margin-bottom: 40rpx;
  background-color: #fff;
  border-radius: 8rpx;
  padding: 0 20rpx;
}

.login-btn {
  width: 100%;
  height: 88rpx;
  line-height: 88rpx;
  background-color: #007aff;
  color: #fff;
  border-radius: 8rpx;
  font-size: 32rpx;
  margin-top: 60rpx;
}

.register-link {
  text-align: center;
  margin-top: 40rpx;
  font-size: 28rpx;
  color: #666;
}

.link {
  color: #007aff;
  margin-left: 10rpx;
}
</style>