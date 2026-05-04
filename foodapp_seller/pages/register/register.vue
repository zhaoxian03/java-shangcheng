<template>
  <view class="register-container">
    <!-- 顶部标题 -->
    <view class="register-header">
      <text class="register-title">商家注册</text>
      <text class="register-subtitle">创建您的商家账号</text>
    </view>

    <!-- 注册表单 -->
    <view class="register-form">
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

      <!-- 确认密码 -->
      <view class="form-item">
        <uni-easyinput
          v-model="form.confirmPassword"
          placeholder="请确认密码"
          type="password"
          placeholder-style="color: #999"
        />
      </view>

      <!-- 店铺名称 -->
      <view class="form-item">
        <uni-easyinput
          v-model="form.shopName"
          placeholder="请输入店铺名称"
          type="text"
          placeholder-style="color: #999"
        />
      </view>

      <!-- 注册按钮 -->
      <button class="register-btn" @click="register">注册</button>

      <!-- 登录入口 -->
      <view class="login-link">
        <text>已有账号？</text>
        <text class="link" @click="goLogin">立即登录</text>
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
        password: '',
        confirmPassword: '',
        shopName: ''
      }
    }
  },
  methods: {
    // 注册方法
    async register() {
      // 表单验证
      if (!this.form.username || !this.form.password || !this.form.confirmPassword || !this.form.shopName) {
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        })
        return
      }

      if (this.form.password !== this.form.confirmPassword) {
        uni.showToast({
          title: '两次密码输入不一致',
          icon: 'none'
        })
        return
      }

      try {
        // 调用注册接口
        const res = await request.post('/seller/register', {
          username: this.form.username,
          password: this.form.password,
          shopName: this.form.shopName,
          role: 1 // 1表示商家角色
        })
        
        uni.showToast({
          title: '注册成功',
          icon: 'success'
        })
        
        // 跳转到登录页面
        setTimeout(() => {
          uni.navigateTo({
            url: '/pages/login/login'
          })
        }, 1500)
      } catch (error) {
        // 错误已在request工具中处理
      }
    },
    // 跳转到登录页面
    goLogin() {
      uni.navigateTo({
        url: '/pages/login/login'
      })
    }
  }
}
</script>

<style lang="scss">
.register-container {
  padding: 60rpx 40rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.register-header {
  text-align: center;
  margin-bottom: 80rpx;
  margin-top: 100rpx;
}

.register-title {
  font-size: 48rpx;
  font-weight: bold;
  color: #333;
  display: block;
  margin-bottom: 20rpx;
}

.register-subtitle {
  font-size: 28rpx;
  color: #666;
}

.register-form {
  flex: 1;
}

.form-item {
  margin-bottom: 40rpx;
  background-color: #fff;
  border-radius: 8rpx;
  padding: 0 20rpx;
}

.register-btn {
  width: 100%;
  height: 88rpx;
  line-height: 88rpx;
  background-color: #007aff;
  color: #fff;
  border-radius: 8rpx;
  font-size: 32rpx;
  margin-top: 60rpx;
}

.login-link {
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