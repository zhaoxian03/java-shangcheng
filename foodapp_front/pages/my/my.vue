<template>
	<view class="my-page">
		<!-- 用户信息头部 -->
		<view class="my_header">
			<image :src="getAvatarUrl()" class="headerimg"></image>
			<view class="user-info">
				<view class="denglu_username" @click="login" v-if="isLogin">{{loginUser}}</view>
				<view class="denglu_btn" @click="login" v-else>登录/注册</view>
				<view class="user-desc" v-if="isLogin">欢迎回来！</view>
				<view class="user-desc" v-else>立即登录享受更多服务</view>
			</view>
		</view>

		<!-- 功能菜单 -->
		<view class="menu-grid">
			<view class="menu-item" @click="navigateTo('orders')">
				<image src="/static/other/dingdan6.png" class="menu-icon"></image>
				<text class="menu-title">我的订单</text>
			</view>
			<view class="menu-item" @click="navigateTo('collect')">
				<image src="/static/other/shouchang.png" class="menu-icon"></image>
				<text class="menu-title">商品收藏</text>
			</view>
			<view class="menu-item" @click="navigateTo('address')">
				<image src="/static/other/shouhuo.png" class="menu-icon"></image>
				<text class="menu-title">收货地址</text>
			</view>
			<view class="menu-item" @click="navigateTo('signin')">
				<image src="/static/other/qiandao2.png" class="menu-icon"></image>
				<text class="menu-title">每日签到</text>
			</view>
		</view>

		<!-- 列表功能 -->
		<view class="section">
			<view class="section-title">账号管理</view>
			<view 
				class="itemlist" 
				v-for="(item, index) in accountList" 
				:key="item.id" 
				@click="handleAccountItem(item.id)"
			>
				<image :src="item.icon" class="iconimg"></image>
				<view class="itemtitle">{{ item.title }}</view>
				<view class="arrow">></view>
			</view>
		</view>

		<view class="section">
			<view class="section-title">安全设置</view>
			<view 
				class="itemlist" 
				v-for="(item, index) in securityList" 
				:key="item.id" 
				@click="handleSecurityItem(item.id)"
			>
				<image :src="item.icon" class="iconimg"></image>
				<view class="itemtitle">{{ item.title }}</view>
				<view class="arrow">></view>
			</view>
		</view>

		<view class="section">
			<view class="section-title">其他</view>
			<view 
				class="itemlist" 
				v-for="(item, index) in otherList" 
				:key="item.id" 
				@click="handleOtherItem(item.id)"
			>
				<image :src="item.icon" class="iconimg"></image>
				<view class="itemtitle">{{ item.title }}</view>
				<view class="arrow">></view>
			</view>
		</view>

		<view class="logout-btn" @click="logout" v-if="isLogin">
			退出登录
		</view>

		<!-- 登录弹窗 -->
		<uni-popup ref="popup" type="center" background-color="#fff">
			<view class="popup-content">
				<uni-icons type="close" size="26" class="close" @click="colseloginPopup"></uni-icons>
				<view class="title">欢迎登录</view>
				<view class="inputview">
					<text class="label">账号：</text>
					<input 
						type="text" 
						class="myinput" 
						v-model="loginForm.username" 
						placeholder="请输入账号" 
					/>
				</view>
				<view class="inputview">
					<text class="label">密码：</text>
					<input 
						:type="showLoginPassword ? 'text' : 'password'"
						class="myinput" 
						v-model="loginForm.password" 
						placeholder="请输入密码" 
					/>
					<!-- 添加小眼睛图标 -->
					<view class="password-eye" @click="toggleLoginPassword">
						<uni-icons 
							:type="showLoginPassword ? 'eye' : 'eye-slash'" 
							size="22" 
							color="#999"
						></uni-icons>
					</view>
				</view>
				<view class="login-options">
					<text class="register-link" @click="goRegister">注册账号</text>
					<text class="forgot-link" @click="showForgotPassword">忘记密码</text>
				</view>
				<view class="loginbtn" @click="handleLogin">登录</view>
			</view>
		</uni-popup>

		<!-- 忘记密码弹窗 -->
		<uni-popup ref="forgotPopup" type="center" background-color="#fff">
			<view class="forgot-content">
				<uni-icons type="close" size="26" class="close" @click="closeForgotPopup"></uni-icons>
				<view class="forgot-title">忘记密码</view>
				<view class="forgot-text">
					<text>如需重置密码，请联系管理员：</text>
					<text class="email">admin@foodapp.com</text>
				</view>
				<view class="forgot-btn" @click="closeForgotPopup">确定</view>
			</view>
		</uni-popup>
	</view>
</template>

<script>
import { userApi } from '@/api/user.js';

const app = getApp({
	allowDefault: true
})

export default {
	data() {
		return {
			isLogin: false,
			loginUser: '未登录',
			userId: null,
			userToken: null,
			userAvatar: '',
			avatarTimestamp: Date.now(),
			
			loginForm: {
				username: '',
				password: ''
			},
			showLoginPassword: false, // 控制登录密码显示/隐藏
			
			// 删除了会员中心
			accountList: [
				{ "id": 1, "icon": "/static/other/grzx.png", "title": "个人信息" },
				{ "id": 3, "icon": "/static/other/zh.png", "title": "账户安全" }
			],
			securityList: [
				{ "id": 4, "icon": "/static/other/mima.png", "title": "修改密码" },
				{ "id": 5, "icon": "/static/other/shoujihao.png", "title": "修改手机号" },
				{ "id": 6, "icon": "/static/other/youxiang.png", "title": "绑定邮箱" }
			],
			otherList: [
				{ "id": 8, "icon": "/static/other/yjfk3.png", "title": "意见反馈" },
				{ "id": 9, "icon": "/static/other/gywm.png", "title": "关于我们" },
				{ "id": 10, "icon": "/static/other/bangzhuzhongxin.png", "title": "帮助中心" }
			]
		}
	},
	
	onLoad() {
		console.log('my.vue页面加载');
		this.checkLocalLoginStatus();
	},
	
	onShow() {
		console.log('my.vue页面显示');
		this.checkLocalLoginStatus();
	},
	
	onReady() {
		uni.$on('avatarUpdated', this.handleAvatarUpdated);
	},
	
	onUnload() {
		uni.$off('avatarUpdated', this.handleAvatarUpdated);
	},
	
	methods: {
		getAvatarUrl() {
			if (!this.isLogin) {
				return '/static/other/moren_1.jpg';
			}
			
			if (!this.userAvatar) {
				return '/static/other/moren_1.jpg';
			}
			
			if (this.userAvatar.startsWith('data:image')) {
				return this.userAvatar;
			}
			
			if (this.userAvatar.startsWith('/uploads/')) {
				return 'http://localhost:8089' + this.userAvatar + '?t=' + this.avatarTimestamp;
			}
			
			if (this.userAvatar.startsWith('http')) {
				return this.userAvatar + (this.userAvatar.includes('?') ? '&' : '?') + 't=' + this.avatarTimestamp;
			}
			
			if (this.userAvatar.startsWith('/') || this.userAvatar.startsWith('./')) {
				return this.userAvatar + '?t=' + this.avatarTimestamp;
			}
			
			return '/static/other/moren_1.jpg';
		},
		
		updateAvatarTimestamp() {
			this.avatarTimestamp = Date.now();
		},
		
		checkLocalLoginStatus() {
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			const userId = uni.getStorageSync('userId');
			const username = uni.getStorageSync('username');
			
			if (token && userId && username) {
				this.isLogin = true;
				this.userId = userId;
				this.userToken = token;
				this.loginUser = username;
				
				app.globalData.isLogin = true;
				app.globalData.userId = userId;
				app.globalData.userToken = token;
				app.globalData.username = username;
				
				this.loadCorrectAvatar();
			} else {
				this.clearLoginState();
			}
		},
		
		loadCorrectAvatar() {
			if (!this.isLogin) {
				this.userAvatar = '/static/other/moren_1.jpg';
				return;
			}
			
			let avatar = null;
			
			if (app.globalData.userInfo && (app.globalData.userInfo.avatar || app.globalData.userInfo.avatarUrl)) {
				avatar = app.globalData.userInfo.avatar || app.globalData.userInfo.avatarUrl;
			}
			
			if (!avatar) {
				const userInfo = uni.getStorageSync('userInfo');
				if (userInfo && (userInfo.avatar || userInfo.avatarUrl)) {
					avatar = userInfo.avatar || userInfo.avatarUrl;
				}
			}
			
			this.userAvatar = avatar || '/static/other/moren_1.jpg';
			this.updateAvatarTimestamp();
		},
		
		loadUserInfo() {
			this.isLogin = app.globalData.isLogin || false;
			
			if (this.isLogin && app.globalData.userInfo) {
				this.loginUser = app.globalData.userInfo.name || app.globalData.userInfo.username || '用户';
				this.userId = app.globalData.userId;
				this.userToken = app.globalData.userToken;
				this.loadCorrectAvatar();
			} else {
				this.loginUser = '未登录';
				this.userAvatar = '';
			}
		},
		
		handleAvatarUpdated(data) {
			if (data && data.avatar && this.isLogin) {
				this.userAvatar = data.avatar;
				this.updateAvatarTimestamp();
				
				if (app.globalData.userInfo) {
					app.globalData.userInfo.avatar = data.avatar;
					app.globalData.userInfo.avatarUrl = data.avatar;
				}
				
				const userInfo = uni.getStorageSync('userInfo') || {};
				userInfo.avatar = data.avatar;
				userInfo.avatarUrl = data.avatar;
				uni.setStorageSync('userInfo', userInfo);
			}
		},
		
		clearLoginState() {
			this.isLogin = false;
			this.loginUser = '未登录';
			this.userId = null;
			this.userToken = null;
			this.userAvatar = '';
			this.updateAvatarTimestamp();
			
			app.globalData.isLogin = false;
			app.globalData.userId = null;
			app.globalData.userToken = null;
			app.globalData.username = null;
		},

		colseloginPopup() {
			this.$refs.popup.close()
		},

		// 切换登录密码显示/隐藏
		toggleLoginPassword() {
			this.showLoginPassword = !this.showLoginPassword;
		},

		async handleLogin() {
		  if (!this.loginForm.username.trim()) {
		    uni.showToast({
		      title: '请输入账号',
		      icon: 'none'
		    })
		    return
		  }
		
		  if (!this.loginForm.password.trim()) {
		    uni.showToast({
		      title: '请输入密码',
		      icon: 'none'
		    })
		    return
		  }
		
		  uni.showLoading({
		    title: '登录中...',
		    mask: true
		  })
		
		  try {
		    // 登录前强制清除所有缓存
		    uni.clearStorageSync();
		    if (app && app.globalData) {
		      app.globalData.isLogin = false;
		      app.globalData.userId = null;
		      app.globalData.userToken = null;
		      app.globalData.username = null;
		      app.globalData.userInfo = null;
		    }
		    
		    const result = await userApi.login(
		      this.loginForm.username,
		      this.loginForm.password
		    );
		    
		    uni.hideLoading();
		    
		    if (!result || result.code !== 200) {
		      throw new Error(result?.message || '登录失败');
		    }
		    
		    if (!result.data) {
		      throw new Error('登录响应缺少数据');
		    }
		    
		    const { token, user, userId } = result.data;
		    
		    if (!token) {
		      throw new Error('登录响应缺少token');
		    }
		    
		    // 存储登录信息，添加token的兼容存储
		    uni.setStorageSync('userToken', token);
		    uni.setStorageSync('token', token); // 兼容存储
		    uni.setStorageSync('userId', userId);
		    uni.setStorageSync('cid', userId); // 存储cid，保持一致
		    uni.setStorageSync('username', this.loginForm.username);
		    
		    // 存储完整的用户信息
		    const userInfoToStore = {
		      ...user,
		      name: this.loginForm.username,
		      username: this.loginForm.username,
		      cid: userId, // 添加cid字段
		      userId: userId // 确保userInfo中有userId字段
		    };
		    uni.setStorageSync('userInfo', userInfoToStore);
		    
		    // 更新页面状态
		    this.isLogin = true;
		    this.userId = userId;
		    this.cid = userId;
		    this.userToken = token;
		    this.loginUser = this.loginForm.username;
		    
		    // 加载头像
		    this.loadCorrectAvatar();
		    
		    // 更新全局状态
		    app.globalData.isLogin = true;
		    app.globalData.userId = userId;
		    app.globalData.cid = userId;
		    app.globalData.userToken = token;
		    app.globalData.token = token;
		    app.globalData.username = this.loginForm.username;
		    app.globalData.userInfo = userInfoToStore;
		    
		    // 关闭弹窗并清空表单
		    this.$refs.popup.close();
		    this.loginForm.username = '';
		    this.loginForm.password = '';
		    this.showLoginPassword = false; // 重置密码显示状态
		    
		    uni.showToast({
		      title: '登录成功',
		      icon: 'success'
		    });
		    
		  } catch (error) {
		    uni.hideLoading();
		    console.error('登录失败详情:', error);
		    uni.showToast({
		      title: error.message || '登录失败',
		      icon: 'none',
		      duration: 3000
		    });
		  }
		},

		login() {
			if (this.isLogin) {
				uni.navigateTo({
					url: `/pages/profile/profile?userId=${this.userId}`
				})
			} else {
				this.$refs.popup.open('center')
			}
		},

		handleAccountItem(itemid) {
			if (!this.isLogin) {
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				})
				this.$refs.popup.open('center')
				return
			}

			if (!this.userId) {
				uni.showToast({
					title: '用户信息异常，请重新登录',
					icon: 'none'
				})
				this.clearLoginState();
				return;
			}
			
			switch (itemid) {
				case 1:
					uni.navigateTo({
						url: `/pages/profile/profile?userId=${this.userId}`
					})
					break
				case 3:  // 原来是会员中心，现在直接跳到账户安全
					uni.navigateTo({
						url: `/pages/security/security?userId=${this.userId}`
					})
					break
			}
		},

		handleSecurityItem(itemid) {
			if (!this.isLogin) {
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				})
				this.$refs.popup.open('center')
				return
			}

			if (!this.userId) {
				uni.showToast({
					title: '用户信息异常，请重新登录',
					icon: 'none'
				})
				this.clearLoginState();
				return;
			}
			
			switch (itemid) {
				case 4:
					uni.navigateTo({
						url: `/pages/password/password?userId=${this.userId}`
					})
					break
				case 5:
					uni.navigateTo({
						url: `/pages/bind-phone/bind-phone?userId=${this.userId}`
					})
					break
				case 6:
					uni.navigateTo({
						url: `/pages/bind-email/bind-email?userId=${this.userId}`
					})
					break
			}
		},

		handleOtherItem(itemid) {
			switch (itemid) {
				case 8:
					uni.navigateTo({
						url: '/pages/feedback/feedback'
					})
					break
				case 9:
					uni.navigateTo({
						url: '/pages/our/our'
					})
					break
				case 10:
					uni.navigateTo({
						url: '/pages/help/help'
					})
					break
			}
		},

		navigateTo(type) {
		     if (!this.isLogin) {
		         uni.showToast({
		             title: '请先登录',
		             icon: 'none'
		         })
		         this.$refs.popup.open('center')
		         return
		     }
		 
		     if (!this.userId) {
		         uni.showToast({
		             title: '用户信息异常，请重新登录',
		             icon: 'none'
		         })
		         this.clearLoginState();
		         return;
		     }
		     
		     switch(type) {
		         case 'orders':
		             uni.setStorageSync('currentUserId', this.userId);
		             uni.setStorageSync('userToken', this.userToken);
		             
		             uni.switchTab({
		                 url: '/pages/orders/orders'
		             });
		             break;
		             
		         case 'collect':
		             uni.navigateTo({
		                 url: `/pages/collect/collect?userId=${this.userId}`
		             });
		             break;
		             
		         case 'address':
		             uni.navigateTo({
		                 url: `/pages/address/list?userId=${this.userId}`
		             });
		             break;
		             
		         case 'signin':
		             uni.navigateTo({
		                 url: `/pages/signin/signin?userId=${this.userId}`
		             });
		             break;
		     }
		 },

		logout() {
			uni.showModal({
				title: '提示',
				content: '确定要退出登录吗？',
				success: (res) => {
					if (res.confirm) {
						// 1. 清除所有本地存储
						uni.clearStorageSync();
						
						// 2. 清除全局数据缓存
						if (app && app.globalData) {
							app.globalData.isLogin = false;
							app.globalData.userId = null;
							app.globalData.userToken = null;
							app.globalData.username = null;
							app.globalData.userInfo = null;
						}
						
						// 3. 清除页面数据
						this.isLogin = false;
						this.loginUser = '未登录';
						this.userId = null;
						this.userToken = null;
						this.userAvatar = '';
						this.avatarTimestamp = Date.now();
						this.showLoginPassword = false; // 重置密码显示状态
						
						// 4. 重新加载页面
						setTimeout(() => {
							uni.switchTab({
								url: '/pages/my/my'
							});
							
							uni.showToast({
								title: '已退出登录',
								icon: 'success',
								duration: 1500
							});
						}, 300);
					}
				}
			})
		},

		goRegister() {
			this.$refs.popup.close();
			uni.navigateTo({
				url: '/pages/register/register'
			});
		},

		// 显示忘记密码弹窗
		showForgotPassword() {
			this.$refs.popup.close(); // 先关闭登录弹窗
			setTimeout(() => {
				this.$refs.forgotPopup.open('center');
			}, 300);
		},

		// 关闭忘记密码弹窗
		closeForgotPopup() {
			this.$refs.forgotPopup.close();
		}
	}
}
</script>

<style lang="scss" scoped>
.my-page {
	min-height: 100vh;
	background-color: #f5f5f5;
	padding-bottom: 120rpx;
	box-sizing: border-box;
}

.my_header {
	width: 100%;
	height: 380rpx;
	background: linear-gradient(135deg, #4a90e2, #5a8bff);
	display: flex;
	align-items: center;
	padding: 0 60rpx;
	box-sizing: border-box;

	.headerimg {
		width: 160rpx;
		height: 160rpx;
		border-radius: 50%;
		border: 6rpx solid rgba(255, 255, 255, 0.3);
		margin-right: 40rpx;
	}

	.user-info {
		flex: 1;

		.denglu_username {
			font-size: 42rpx;
			font-weight: bold;
			color: #fff;
			margin-bottom: 10rpx;
		}

		.denglu_btn {
			width: 240rpx;
			height: 80rpx;
			background-color: #fff;
			color: #4a90e2;
			text-align: center;
			line-height: 80rpx;
			border-radius: 40rpx;
			font-size: 32rpx;
			font-weight: bold;
			margin-bottom: 10rpx;
		}

		.denglu_btn:active {
			background-color: #f0f0f0;
		}

		.denglu_username:active {
			opacity: 0.8;
		}

		.user-desc {
			font-size: 28rpx;
			color: rgba(255, 255, 255, 0.9);
		}
	}
}

.menu-grid {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	background-color: #fff;
	margin: -40rpx 30rpx 30rpx;
	border-radius: 20rpx;
	padding: 40rpx 0;
	box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);

	.menu-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 20rpx 0;

		.menu-icon {
			width: 80rpx;
			height: 80rpx;
			margin-bottom: 20rpx;
		}

		.menu-title {
			font-size: 26rpx;
			color: #333;
		}

		&:active {
			opacity: 0.7;
		}
	}
}

.section {
	background-color: #fff;
	margin: 0 30rpx 30rpx;
	border-radius: 20rpx;
	padding: 30rpx 0;
	box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);

	.section-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
		padding: 0 40rpx 30rpx;
		border-bottom: 2rpx solid #f0f0f0;
		margin-bottom: 20rpx;
	}
}

.itemlist {
	display: flex;
	align-items: center;
	padding: 30rpx 40rpx;
	border-bottom: 1rpx solid #f0f0f0;

	&:last-child {
		border-bottom: none;
	}

	.iconimg {
		width: 50rpx;
		height: 50rpx;
		margin-right: 30rpx;
	}

	.itemtitle {
		flex: 1;
		font-size: 32rpx;
		color: #333;
	}

	.arrow {
		color: #ccc;
		font-size: 36rpx;
		font-weight: bold;
	}

	&:active {
		background-color: #f9f9f9;
	}
}

.logout-btn {
	margin: 40rpx 30rpx;
	margin-bottom: 60rpx;
	height: 90rpx;
	background-color: #fff;
	color: #ff4444;
	text-align: center;
	line-height: 90rpx;
	border-radius: 10rpx;
	font-size: 34rpx;
	font-weight: 500;
	box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);

	&:active {
		background-color: #f9f9f9;
	}
}

.popup-content {
	width: 650rpx;
	background-color: #fff;
	border-radius: 20rpx;
	padding: 50rpx 40rpx;
	position: relative;

	.close {
		position: absolute;
		right: 20rpx;
		top: 20rpx;
		color: #666;
	}

	.close:active {
		color: #333;
	}

	.title {
		font-size: 40rpx;
		font-weight: bold;
		text-align: center;
		margin-bottom: 50rpx;
		color: #333;
	}

	.inputview {
		display: flex;
		align-items: center;
		margin-bottom: 40rpx;
		position: relative;
		
		.label {
			width: 120rpx;
			font-size: 32rpx;
			color: #333;
		}
		
		.myinput {
			flex: 1;
			border: 2rpx solid #e0e0e0;
			height: 80rpx;
			padding: 0 60rpx 0 20rpx;
			box-sizing: border-box;
			border-radius: 8rpx;
			font-size: 30rpx;
			background-color: #f9f9f9;
		}
		
		.myinput:focus {
			border-color: #4a90e2;
			background-color: #fff;
		}
		
		// 小眼睛图标样式
		.password-eye {
			position: absolute;
			right: 20rpx;
			top: 50%;
			transform: translateY(-50%);
			width: 40rpx;
			height: 40rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			z-index: 10;
		}
		
		.password-eye:active {
			opacity: 0.6;
		}
	}

	.login-options {
		display: flex;
		justify-content: space-between;
		margin-bottom: 40rpx;

		.register-link,
		.forgot-link {
			font-size: 28rpx;
			color: #4a90e2;
		}

		.register-link:active,
		.forgot-link:active {
			opacity: 0.8;
		}
	}

	.loginbtn {
		width: 100%;
		height: 90rpx;
		background: linear-gradient(135deg, #4a90e2, #5a8bff);
		color: #fff;
		text-align: center;
		line-height: 90rpx;
		border-radius: 45rpx;
		font-size: 34rpx;
		font-weight: bold;
		margin-bottom: 40rpx;
	}

	.loginbtn:active {
		opacity: 0.9;
	}
}

/* 忘记密码弹窗样式 */
.forgot-content {
	width: 600rpx;
	background-color: #fff;
	border-radius: 20rpx;
	padding: 50rpx 40rpx;
	position: relative;

	.close {
		position: absolute;
		right: 20rpx;
		top: 20rpx;
		color: #666;
	}

	.close:active {
		color: #333;
	}

	.forgot-title {
		font-size: 36rpx;
		font-weight: bold;
		text-align: center;
		margin-bottom: 40rpx;
		color: #333;
	}

	.forgot-text {
		font-size: 30rpx;
		color: #666;
		text-align: center;
		line-height: 1.6;
		margin-bottom: 50rpx;
		
		.email {
			display: block;
			font-size: 32rpx;
			color: #4a90e2;
			font-weight: bold;
			margin-top: 20rpx;
		}
	}

	.forgot-btn {
		width: 100%;
		height: 90rpx;
		background: linear-gradient(135deg, #4a90e2, #5a8bff);
		color: #fff;
		text-align: center;
		line-height: 90rpx;
		border-radius: 45rpx;
		font-size: 34rpx;
		font-weight: bold;
		margin-bottom: 20rpx;
	}

	.forgot-btn:active {
		opacity: 0.9;
	}
}
</style>