<template>
	<view class="security-page">
		<!-- 导航栏 -->
		<!-- <uni-nav-bar title="账户安全" left-icon="back" @clickLeft="goBack" :border="false" /> -->
		
		<!-- 安全评分卡片 -->
		<view class="security-card">
			<view class="score-header">
				<text class="title">账户安全评分</text>
				<view class="score-wrapper">
					<text class="score">{{ securityScore }}</text>
					<text class="score-label">分</text>
				</view>
			</view>
			
			<!-- 安全等级 -->
			<view class="security-level">
				<view class="level-indicator" :style="{width: securityPercent + '%'}"></view>
				<view class="level-text">
					<text v-if="securityScore >= 80" class="high-level">高</text>
					<text v-else-if="securityScore >= 60" class="medium-level">中</text>
					<text v-else class="low-level">低</text>
					<text>安全等级</text>
				</view>
			</view>
			
			<view class="score-tip">
				<text v-if="securityScore >= 80">账户非常安全，继续保持！</text>
				<text v-else-if="securityScore >= 60">账户基本安全，建议完善信息</text>
				<text v-else>账户存在风险，请立即完善信息</text>
			</view>
		</view>
		
		<!-- 安全项目列表 -->
		<view class="security-list">
			<!-- 手机号绑定 -->
			<view class="security-item" @click="handlePhone">
				<view class="item-left">
					<uni-icons type="phone" size="22" color="#4a90e2"></uni-icons>
					<view class="item-info">
						<text class="item-title">手机号绑定</text>
						<text class="item-status" :class="{'bound': userPhone, 'unbound': !userPhone}">
							{{ userPhone ? '已绑定' : '未绑定' }}
						</text>
					</view>
				</view>
				<view class="item-right">
					<text class="item-value">{{ userPhone || '未绑定' }}</text>
					<uni-icons type="forward" size="18" color="#999"></uni-icons>
				</view>
			</view>
			
			<!-- 邮箱绑定 -->
			<view class="security-item" @click="handleEmail">
				<view class="item-left">
					<uni-icons type="email" size="22" color="#4a90e2"></uni-icons>
					<view class="item-info">
						<text class="item-title">邮箱绑定</text>
						<text class="item-status" :class="{'bound': userEmail, 'unbound': !userEmail}">
							{{ userEmail ? '已绑定' : '未绑定' }}
						</text>
					</view>
				</view>
				<view class="item-right">
					<text class="item-value">{{ userEmail || '未绑定' }}</text>
					<uni-icons type="forward" size="18" color="#999"></uni-icons>
				</view>
			</view>
			
			<!-- 密码安全（不可点击） -->
			<view class="security-item">
				<view class="item-left">
					<uni-icons type="locked" size="22" color="#4a90e2"></uni-icons>
					<view class="item-info">
						<text class="item-title">登录密码</text>
						<text class="item-status" :class="passwordLevelClass">
							{{ passwordLevelText }}
						</text>
					</view>
				</view>
				<view class="item-right">
					<text class="item-value">已设置</text>
				</view>
			</view>
		</view>
		
		<!-- 安全建议 -->
		<view class="suggestions" v-if="securityScore < 80">
			<view class="suggestions-title">安全建议</view>
			<view class="suggestion-list">
				<view class="suggestion-item" v-if="!userPhone">
					<uni-icons type="checkbox" size="16" color="#07c160"></uni-icons>
					<text>绑定手机号，提升账户安全性</text>
				</view>
				<view class="suggestion-item" v-if="!userEmail">
					<uni-icons type="checkbox" size="16" color="#07c160"></uni-icons>
					<text>绑定邮箱，方便找回密码</text>
				</view>
				<view class="suggestion-item" v-if="securityScore < 60">
					<uni-icons type="checkbox" size="16" color="#07c160"></uni-icons>
					<text>修改为更强密码，包含数字、字母、特殊字符</text>
				</view>
				<view class="suggestion-item" v-if="isOldPassword">
					<uni-icons type="checkbox" size="16" color="#07c160"></uni-icons>
					<text>密码已使用较长时间，建议定期更换</text>
				</view>
			</view>
		</view>
		
		<!-- 安全提示 -->
		<view class="security-tips">
			<view class="tips-title">安全提示</view>
			<view class="tips-content">
				<text>1. 定期修改密码，建议每3个月更换一次</text>
				<text>2. 不要使用过于简单的密码</text>
				<text>3. 不要将密码告诉他人</text>
				<text>4. 在公共设备上登录后及时退出</text>
				<text>5. 开启登录保护功能（如果支持）</text>
			</view>
		</view>
	</view>
</template>

<script>
import { userApi } from '@/api/user.js';

export default {
	data() {
		return {
			userId: null,
			userToken: null,
			// 用户信息
			userPhone: null,
			userEmail: null,
			passwordStrength: 'medium', // weak, medium, strong
			isOldPassword: false,
			// 安全评分
			securityScore: 0,
			securityPercent: 0
		}
	},
	
	computed: {
		passwordLevelText() {
			switch (this.passwordStrength) {
				case 'strong': return '高强度';
				case 'medium': return '中强度';
				case 'weak': return '低强度';
				default: return '未知';
			}
		},
		passwordLevelClass() {
			return {
				'strong-level': this.passwordStrength === 'strong',
				'medium-level': this.passwordStrength === 'medium',
				'weak-level': this.passwordStrength === 'weak'
			}
		}
	},
	
	onLoad(options) {
		console.log('账户安全页面参数:', options);
		
		// 获取参数
		if (options.userId) {
			this.userId = options.userId;
		}
		
		// 从全局获取用户信息
		this.getUserInfo();
		
		// 加载安全信息
		if (this.userId) {
			this.loadSecurityInfo();
		} else {
			uni.showToast({
				title: '请先登录',
				icon: 'none'
			});
			setTimeout(() => {
				uni.navigateBack();
			}, 1500);
		}
	},
	
	onShow() {
		// 页面显示时重新加载数据
		if (this.userId) {
			this.loadSecurityInfo();
		}
	},
	
	methods: {
		// 获取用户信息
		getUserInfo() {
			const app = getApp();
			
			// 从全局获取
			this.userId = app.globalData.userId || uni.getStorageSync('userId');
			this.userToken = app.globalData.userToken || uni.getStorageSync('userToken');
			
			// 从本地存储获取用户信息
			const userInfo = uni.getStorageSync('userInfo') || {};
			this.userPhone = userInfo.phone;
			this.userEmail = userInfo.email;
			
			console.log('用户信息:', { 
				userId: this.userId, 
				phone: this.userPhone, 
				email: this.userEmail 
			});
		},
		
		// 加载安全信息
		async loadSecurityInfo() {
			uni.showLoading({
				title: '加载中...'
			});
			
			try {
				// 1. 获取手机号
				if (!this.userPhone) {
					await this.loadUserPhone();
				}
				
				// 2. 获取邮箱
				if (!this.userEmail) {
					await this.loadUserEmail();
				}
				
				// 3. 评估密码强度
				await this.evaluatePasswordStrength();
				
				// 4. 计算安全评分
				this.calculateSecurityScore();
				
			} catch (error) {
				console.error('加载安全信息失败:', error);
				uni.showToast({
					title: '加载失败，请重试',
					icon: 'none'
				});
			} finally {
				uni.hideLoading();
			}
		},
		
		// 加载用户手机号
		async loadUserPhone() {
			try {
				if (!this.userToken) return;
				
				const result = await userApi.getUserPhone(this.userToken);
				console.log('手机号获取结果:', result);
				
				if (result.code === 200 && result.data && result.data.phone) {
					this.userPhone = result.data.phone;
					
					// 更新本地存储
					const userInfo = uni.getStorageSync('userInfo') || {};
					userInfo.phone = this.userPhone;
					uni.setStorageSync('userInfo', userInfo);
				}
			} catch (error) {
				console.error('获取手机号失败:', error);
			}
		},
		
		// 加载用户邮箱
		async loadUserEmail() {
			try {
				if (!this.userToken) return;
				
				const result = await userApi.getUserEmail(this.userToken);
				console.log('邮箱获取结果:', result);
				
				if (result.code === 200 && result.data && result.data.email) {
					this.userEmail = result.data.email;
					
					// 更新本地存储
					const userInfo = uni.getStorageSync('userInfo') || {};
					userInfo.email = this.userEmail;
					uni.setStorageSync('userInfo', userInfo);
				}
			} catch (error) {
				console.error('获取邮箱失败:', error);
			}
		},
		
		// 评估密码强度
		async evaluatePasswordStrength() {
			try {
				// 这里应该调用后端API检查密码强度
				// 由于后端可能没有直接接口，我们可以根据一些规则评估
				
				// 模拟从用户信息中获取密码相关信息
				// 在实际项目中，可能需要后端提供密码强度评估接口
				
				// 默认设为中等强度
				this.passwordStrength = 'medium';
				
				// 如果有登录时间信息，判断密码是否老旧
				const userInfo = uni.getStorageSync('userInfo') || {};
				if (userInfo.createTime) {
					const createTime = new Date(userInfo.createTime);
					const now = new Date();
					const diffMonths = (now.getFullYear() - createTime.getFullYear()) * 12 + 
									   (now.getMonth() - createTime.getMonth());
					
					this.isOldPassword = diffMonths > 6; // 超过6个月视为老旧
					
					if (this.isOldPassword) {
						this.passwordStrength = 'weak';
					}
				}
				
			} catch (error) {
				console.error('评估密码强度失败:', error);
				this.passwordStrength = 'medium';
			}
		},
		
		// 计算安全评分
		calculateSecurityScore() {
			let score = 0;
			
			// 1. 手机号绑定 (30分)
			if (this.userPhone && this.userPhone.length === 11) {
				score += 30;
			}
			
			// 2. 邮箱绑定 (20分)
			if (this.userEmail && this.userEmail.includes('@')) {
				score += 20;
			}
			
			// 3. 密码强度 (40分)
			switch (this.passwordStrength) {
				case 'strong': score += 40; break;
				case 'medium': score += 30; break;
				case 'weak': score += 15; break;
			}
			
			// 4. 密码时效性 (10分)
			if (!this.isOldPassword) {
				score += 10;
			}
			
			this.securityScore = Math.min(Math.max(score, 0), 100);
			this.securityPercent = this.securityScore;
			
			console.log('安全评分计算完成:', {
				score: this.securityScore,
				phone: this.userPhone,
				email: this.userEmail,
				passwordStrength: this.passwordStrength,
				isOldPassword: this.isOldPassword
			});
		},
		
		// 返回上一页
		goBack() {
			uni.navigateBack();
		},
		
		// 处理手机号点击
		handlePhone() {
			if (this.userPhone) {
				// 如果已绑定手机号，显示手机号信息
				uni.showModal({
					title: '手机号信息',
					content: `已绑定的手机号：${this.userPhone}`,
					showCancel: false,
					confirmText: '我知道了'
				});
			} else {
				// 如果未绑定，跳转到绑定手机页面
				uni.navigateTo({
					url: `/pages/bind-phone/bind-phone?userId=${this.userId}`
				});
			}
		},
		
		// 处理邮箱点击
		handleEmail() {
			if (this.userEmail) {
				// 如果已绑定邮箱，显示邮箱信息
				uni.showModal({
					title: '邮箱信息',
					content: `已绑定的邮箱：${this.userEmail}`,
					showCancel: false,
					confirmText: '我知道了'
				});
			} else {
				// 如果未绑定，跳转到绑定邮箱页面
				uni.navigateTo({
					url: `/pages/bind-email/bind-email?userId=${this.userId}`
				});
			}
		}
	}
}
</script>

<style lang="scss" scoped>
.security-page {
	min-height: 100vh;
	background-color: #f5f5f5;
	padding-bottom: 40rpx;
	
	.security-card {
		background: linear-gradient(135deg, #4a90e2, #5a8bff);
		margin: 30rpx;
		border-radius: 20rpx;
		padding: 40rpx;
		box-shadow: 0 10rpx 30rpx rgba(74, 144, 226, 0.3);
		color: white;
		
		.score-header {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 30rpx;
			
			.title {
				font-size: 32rpx;
				font-weight: 500;
			}
			
			.score-wrapper {
				display: flex;
				align-items: baseline;
				
				.score {
					font-size: 72rpx;
					font-weight: bold;
					line-height: 1;
				}
				
				.score-label {
					font-size: 28rpx;
					margin-left: 8rpx;
					opacity: 0.9;
				}
			}
		}
		
		.security-level {
			background-color: rgba(255, 255, 255, 0.2);
			height: 24rpx;
			border-radius: 12rpx;
			margin-bottom: 20rpx;
			overflow: hidden;
			position: relative;
			
			.level-indicator {
				height: 100%;
				background: linear-gradient(90deg, #07c160, #4cd964);
				border-radius: 12rpx;
				transition: width 0.5s ease;
			}
			
			.level-text {
				position: absolute;
				top: 0;
				left: 0;
				right: 0;
				bottom: 0;
				display: flex;
				align-items: center;
				justify-content: center;
				font-size: 24rpx;
				color: white;
				
				.high-level {
					font-weight: bold;
					color: #07c160;
					margin-right: 10rpx;
				}
				
				.medium-level {
					font-weight: bold;
					color: #f0a500;
					margin-right: 10rpx;
				}
				
				.low-level {
					font-weight: bold;
					color: #ff4444;
					margin-right: 10rpx;
				}
			}
		}
		
		.score-tip {
			font-size: 26rpx;
			opacity: 0.9;
			text-align: center;
		}
	}
	
	.security-list {
		background-color: #fff;
		margin: 30rpx;
		border-radius: 20rpx;
		overflow: hidden;
		
		.security-item {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 30rpx 40rpx;
			border-bottom: 1rpx solid #f0f0f0;
			
			&:last-child {
				border-bottom: none;
			}
			
			// 移除所有项目的点击效果
			&:not(:has(.item-right uni-icons)) {
				&:active {
					background-color: transparent;
				}
			}
			
			// 保持手机号和邮箱的点击效果
			&:has(.item-right uni-icons):active {
				background-color: #f9f9f9;
			}
			
			.item-left {
				display: flex;
				align-items: center;
				flex: 1;
				
				uni-icons {
					margin-right: 20rpx;
				}
				
				.item-info {
					display: flex;
					flex-direction: column;
					
					.item-title {
						font-size: 32rpx;
						color: #333;
						margin-bottom: 8rpx;
					}
					
					.item-status {
						font-size: 24rpx;
						
						&.bound {
							color: #07c160;
						}
						
						&.unbound {
							color: #ff9500;
						}
						
						&.strong-level {
							color: #07c160;
						}
						
						&.medium-level {
							color: #f0a500;
						}
						
						&.weak-level {
							color: #ff4444;
						}
					}
				}
			}
			
			.item-right {
				display: flex;
				align-items: center;
				
				.item-value {
					font-size: 28rpx;
					color: #666;
					margin-right: 20rpx;
				}
			}
		}
	}
	
	.suggestions {
		background-color: #fff;
		margin: 30rpx;
		border-radius: 20rpx;
		padding: 30rpx;
		
		.suggestions-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333;
			margin-bottom: 30rpx;
		}
		
		.suggestion-list {
			.suggestion-item {
				display: flex;
				align-items: flex-start;
				margin-bottom: 20rpx;
				
				&:last-child {
					margin-bottom: 0;
				}
				
				uni-icons {
					margin-right: 15rpx;
					margin-top: 4rpx;
					flex-shrink: 0;
				}
				
				text {
					font-size: 28rpx;
					color: #666;
					line-height: 1.5;
				}
			}
		}
	}
	
	.security-tips {
		background-color: #fff;
		margin: 30rpx;
		border-radius: 20rpx;
		padding: 30rpx;
		
		.tips-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333;
			margin-bottom: 30rpx;
		}
		
		.tips-content {
			display: flex;
			flex-direction: column;
			gap: 20rpx;
			
			text {
				font-size: 26rpx;
				color: #666;
				line-height: 1.5;
				padding-left: 20rpx;
				position: relative;
				
				&::before {
					content: "•";
					position: absolute;
					left: 0;
					color: #4a90e2;
				}
			}
		}
	}
}
</style>