<template>
	<view class="profile-page">
		<!-- 内容区域 -->
		<view class="content">
			<!-- 下面是原有的所有代码，完全不变 -->
			<view class="register-page">
				<!-- 用户名 -->
				<view class="input-group">
					<text class="label">用户名</text>
					<input 
						v-model="form.username" 
						placeholder="请输入用户名" 
						maxlength="20"
					/>
					<view v-if="errors.username" class="error-text">
						{{ errors.username }}
					</view>
				</view>
				
				<!-- 密码 -->
				<view class="input-group">
					<text class="label">密码</text>
					<input 
						v-model="form.password" 
						:type="showPassword ? 'text' : 'password'"
						placeholder="请输入密码(6-20位数字+字母)" 
						maxlength="20"
					/>
					<view class="eye-icon" @click="showPassword = !showPassword">
						<uni-icons 
							:type="showPassword ? 'eye' : 'eye-slash'" 
							size="22" 
							color="#999"
						></uni-icons>
					</view>
					<view v-if="errors.password" class="error-text">
						{{ errors.password }}
					</view>
				</view>
				
				<!-- 确认密码 -->
				<view class="input-group">
					<text class="label">确认密码</text>
					<input 
						v-model="form.confirmPassword" 
						:type="showConfirmPassword ? 'text' : 'password'"
						placeholder="请再次输入密码" 
						maxlength="20"
					/>
					<view class="eye-icon" @click="showConfirmPassword = !showConfirmPassword">
						<uni-icons 
							:type="showConfirmPassword ? 'eye' : 'eye-slash'" 
							size="22" 
							color="#999"
						></uni-icons>
					</view>
					<view v-if="errors.confirmPassword" class="error-text">
						{{ errors.confirmPassword }}
					</view>
				</view>
				
				<!-- 手机号 -->
				<view class="input-group">
					<text class="label">手机号 <text style="color:red">*</text></text>
					<input 
						v-model="form.phone" 
						type="number" 
						placeholder="请输入手机号" 
						maxlength="11"
					/>
					<view v-if="errors.phone" class="error-text">
						{{ errors.phone }}
					</view>
				</view>
				
				<!-- 邮箱 -->
				<view class="input-group">
					<text class="label">邮箱</text>
					<input 
						v-model="form.email" 
						type="email" 
						placeholder="请输入邮箱（可选）" 
					/>
					<view v-if="errors.email" class="error-text">
						{{ errors.email }}
					</view>
				</view>
				
				<!-- 注册按钮 -->
				<button 
					class="register-btn" 
					@tap="handleRegister" 
					:disabled="registering"
				>
					{{ registering ? '注册中...' : '注册' }}
				</button>
			</view>
		</view>
	</view>
</template>

<script>
import { userApi } from '@/api/user.js';

// 添加getApp
const app = getApp({
  allowDefault: true
});

export default {
  data() {
    return {
      form: {
        username: '',
        password: '',
        confirmPassword: '',
        phone: '',
        email: ''
      },
      errors: {},
      registering: false,
      showPassword: false,
      showConfirmPassword: false
    }
  },
  
  onLoad() {
    // 页面加载时不需要特别处理
  },
  
  methods: {
    validateForm() {
      this.errors = {};
      let isValid = true;
      
      // 验证用户名
      if (!this.form.username.trim()) {
        this.errors.username = '用户名不能为空';
        isValid = false;
      } else if (this.form.username.length < 3) {
        this.errors.username = '用户名至少3个字符';
        isValid = false;
      }
      
      // 验证密码
      const password = this.form.password;
      if (!password.trim()) {
        this.errors.password = '密码不能为空';
        isValid = false;
      } else if (password.length < 6) {
        this.errors.password = '密码至少6个字符';
        isValid = false;
      } else if (!this.validatePasswordComplexity(password)) {
        this.errors.password = '密码必须包含数字和字母';
        isValid = false;
      }
      
      // 验证确认密码
      if (password !== this.form.confirmPassword) {
        this.errors.confirmPassword = '两次输入的密码不一致';
        isValid = false;
      }
      
      // 验证手机号
      if (!this.form.phone.trim()) {
        this.errors.phone = '手机号不能为空';
        isValid = false;
      } else {
        const phoneReg = /^1[3-9]\d{9}$/;
        if (!phoneReg.test(this.form.phone)) {
          this.errors.phone = '手机号格式不正确';
          isValid = false;
        }
      }
      
      // 验证邮箱（可选）
      if (this.form.email.trim() && !this.validateEmail(this.form.email)) {
        this.errors.email = '邮箱格式不正确';
        isValid = false;
      }
      
      return isValid;
    },
    
    // 验证密码复杂度：必须包含数字和字母
    validatePasswordComplexity(password) {
      // 必须同时包含数字和字母
      const hasNumber = /\d/.test(password);
      const hasLetter = /[a-zA-Z]/.test(password);
      return hasNumber && hasLetter;
    },
    
    validateEmail(email) {
      const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return re.test(email);
    },
    
    async handleRegister() {
      if (!this.validateForm()) {
        return;
      }
      
      this.registering = true;
      
      try {
        const requestData = {
          username: this.form.username.trim(),
          password: this.form.password.trim(),
          phone: this.form.phone.trim(),
          email: this.form.email.trim() || ''
        };
        
        console.log('注册请求数据:', requestData);
        
        // 1. 注册
        const registerResult = await userApi.register(requestData);
        console.log('注册结果:', registerResult);
        
        if (registerResult.code === 200) {
          const userId = registerResult.data.userId;
          
          uni.showToast({
            title: '注册成功',
            icon: 'success'
          });
          
          // 2. 注册成功后自动登录
          await this.autoLoginAfterRegister(requestData.username, requestData.password, userId);
          
        } else {
          uni.showToast({
            title: registerResult.message || '注册失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('注册错误:', error);
        uni.showToast({
          title: error.toString(),
          icon: 'none',
          duration: 3000
        });
      } finally {
        this.registering = false;
      }
    },
    
    // 注册成功后自动登录
    async autoLoginAfterRegister(username, password, userId) {
      try {
        uni.showLoading({
          title: '自动登录中...',
          mask: true
        });
        
        console.log('开始自动登录，用户名:', username);
        
        // 调用登录API
        const loginResult = await userApi.login(username, password);
        console.log('自动登录结果:', loginResult);
        
        if (loginResult.code === 200 && loginResult.data) {
          const { token, user } = loginResult.data;
          let actualUserId = loginResult.data.userId || userId;
          
          // 如果没有userId，尝试从user对象获取
          if (!actualUserId && user && user.id) {
            actualUserId = user.id;
          }
          
          console.log('登录成功:', { token, userId: actualUserId, username });
          
          // 保存登录状态
          this.saveLoginInfo(token, actualUserId, username, user);
          
          uni.hideLoading();
          
          uni.showToast({
            title: '注册成功',
            icon: 'success'
          });
          
          // 3. 跳转到首页
          setTimeout(() => {
            uni.switchTab({
              url: '/pages/my/my'  // 跳转到我的页面
            });
          }, 1500);
          
        } else {
          console.error('自动登录失败，响应:', loginResult);
          throw new Error(loginResult.message || '自动登录失败');
        }
      } catch (error) {
        uni.hideLoading();
        console.error('自动登录失败:', error);
        
        // 如果自动登录失败，跳转到登录页面
        uni.showToast({
          title: '注册成功，请手动登录',
          icon: 'none'
        });
        
        setTimeout(() => {
          uni.navigateTo({
            url: '/pages/login/login?username=' + encodeURIComponent(username)
          });
        }, 1500);
      }
    },
    
    // 保存登录信息（与my.vue保持一致）
    saveLoginInfo(token, userId, username, userInfo) {
      console.log('保存登录信息:', { token, userId, username });
      
      // 保存到本地存储
      uni.setStorageSync('userToken', token);
      uni.setStorageSync('userId', userId);
      uni.setStorageSync('username', username);
      uni.setStorageSync('userInfo', userInfo || {});
      
      // 更新全局数据
      if (app && app.globalData) {
        app.globalData.isLogin = true;
        app.globalData.userId = userId;
        app.globalData.userToken = token;
        app.globalData.username = username;
        app.globalData.userInfo = userInfo || {};
        
        console.log('全局数据已更新:', app.globalData);
      } else {
        console.warn('app或globalData不存在');
      }
    }
  }
}
</script>

<style scoped>
.profile-page {
	min-height: 100vh;
	background-color: #f5f5f5;
}

.content {
	/* 不需要自定义返回按钮区域 */
}

/* 原有样式完全不变 */
.register-page {
  padding: 30rpx;
  padding-top: 40rpx; /* 调整顶部内边距 */
}

.input-group {
  margin-bottom: 40rpx;
  position: relative;
}

.label {
  display: block;
  font-size: 32rpx;
  color: #333;
  margin-bottom: 20rpx;
  font-weight: 500;
}

input {
  width: 100%;
  height: 80rpx;
  border: 2rpx solid #e0e0e0;
  border-radius: 8rpx;
  padding: 0 20rpx;
  font-size: 30rpx;
  box-sizing: border-box;
}

input:focus {
  border-color: #4a90e2;
}

/* 小眼睛图标样式 - 修正为准确居中在输入框内 */
.eye-icon {
  position: absolute;
  right: 20rpx;
  top: 70rpx; /* 标签高度(32rpx) + 标签间距(20rpx) + 输入框高度的一半(40rpx) = 92rpx，微调为70rpx更准确 */
  height: 80rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}

.eye-icon:active {
  opacity: 0.6;
}

.error-text {
  color: #ff4444;
  font-size: 26rpx;
  margin-top: 10rpx;
}

.register-btn {
  width: 100%;
  height: 90rpx;
  background: linear-gradient(135deg, #4a90e2, #5a8bff);
  color: #fff;
  border: none;
  border-radius: 45rpx;
  font-size: 34rpx;
  font-weight: bold;
  margin-top: 40rpx;
}

.register-btn:disabled {
  background: #ccc;
  opacity: 0.7;
}
</style>