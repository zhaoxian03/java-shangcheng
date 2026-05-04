<template>
	<view class="password-page">
		<!-- 页面标题 -->
		<!-- <view class="page-header"> -->
			<!-- <view class="back-btn" @click="goBack">
				<uni-icons type="arrowleft" size="24" color="#333"></uni-icons>
			</view> -->
			<!-- <text class="page-title">修改密码</text> -->
		<!-- </view> -->

		<!-- 密码表单 -->
		<view class="password-form">
			<!-- 原密码 -->
			<view class="input-group">
				<text class="input-label">原密码</text>
				<view class="input-wrapper">
					<input 
						:type="showOldPassword ? 'text' : 'password'" 
						class="password-input" 
						v-model="passwordForm.oldPassword" 
						placeholder="请输入原密码"
						placeholder-style="color: #999;"
						@input="clearError"
					/>
					<view class="eye-icon" @click="togglePasswordVisibility('old')">
						<uni-icons 
							:type="showOldPassword ? 'eye' : 'eye-slash'" 
							size="20" 
							color="#999"
						></uni-icons>
					</view>
				</view>
			</view>

			<!-- 新密码 -->
			<view class="input-group">
				<text class="input-label">新密码</text>
				<view class="input-wrapper">
					<input 
						:type="showNewPassword ? 'text' : 'password'" 
						class="password-input" 
						v-model="passwordForm.newPassword" 
						placeholder="请输入新密码（长度大于6位，包含数字+字母）"
						placeholder-style="color: #999;"
						@input="clearError"
					/>
					<view class="eye-icon" @click="togglePasswordVisibility('new')">
						<uni-icons 
							:type="showNewPassword ? 'eye' : 'eye-slash'" 
							size="20" 
							color="#999"
						></uni-icons>
					</view>
				</view>
				<view class="password-hint">
					<text class="hint-text">密码长度需大于6位，包含数字+字母</text>
				</view>
			</view>

			<!-- 确认新密码 -->
			<view class="input-group">
				<text class="input-label">确认新密码</text>
				<view class="input-wrapper">
					<input 
						:type="showConfirmPassword ? 'text' : 'password'" 
						class="password-input" 
						v-model="passwordForm.confirmPassword" 
						placeholder="请再次输入新密码"
						placeholder-style="color: #999;"
						@input="clearError"
					/>
					<view class="eye-icon" @click="togglePasswordVisibility('confirm')">
						<uni-icons 
							:type="showConfirmPassword ? 'eye' : 'eye-slash'" 
							size="20" 
							color="#999"
						></uni-icons>
					</view>
				</view>
			</view>

			<!-- 错误提示 -->
			<view class="error-tip" v-if="errorMessage">
				<uni-icons type="info" size="16" color="#ff4444"></uni-icons>
				<text class="error-text">{{ errorMessage }}</text>
			</view>

			<!-- 提交按钮 -->
			<button 
				class="submit-btn" 
				:class="{ disabled: !isFormValid }" 
				:disabled="!isFormValid || isSubmitting"
				@click="handleSubmit"
			>
				<text v-if="!isSubmitting">确认修改</text>
				<text v-else>修改中...</text>
			</button>
		</view>

		<!-- 修改成功提示 -->
		<uni-popup ref="successPopup" type="center" background-color="#fff">
			<view class="success-popup">
				<uni-icons type="checkmark" size="60" color="#4CAF50"></uni-icons>
				<text class="success-title">修改成功</text>
				<text class="success-desc">密码已成功修改，请重新登录</text>
				<view class="popup-buttons">
					<button class="popup-btn cancel-btn" @click="handleCancel">返回</button>
					<button class="popup-btn confirm-btn" @click="handleReLogin">重新登录</button>
				</view>
			</view>
		</uni-popup>
	</view>
</template>

<script>
import { userApi } from '@/api/user.js';

export default {
	data() {
		return {
			// 密码表单数据
			passwordForm: {
				oldPassword: '',
				newPassword: '',
				confirmPassword: ''
			},
			// 显示/隐藏密码
			showOldPassword: false,
			showNewPassword: false,
			showConfirmPassword: false,
			// 错误信息
			errorMessage: '',
			// 提交状态
			isSubmitting: false,
			// 用户信息
			userId: '',
			userToken: '',
			userInfo: null
		};
	},
	
	computed: {
		// 表单验证 - 简化：只验证长度大于6位
		isFormValid() {
			return (
				this.passwordForm.oldPassword.trim() &&
				this.passwordForm.newPassword.trim() &&
				this.passwordForm.confirmPassword.trim() &&
				this.passwordForm.newPassword === this.passwordForm.confirmPassword &&
				this.passwordForm.newPassword.length >= 6
			);
		}
	},
	
	onLoad(options) {
		// 获取传递过来的参数
		this.userId = options.userId || '';
		this.userToken = uni.getStorageSync('userToken') || '';
		this.userInfo = uni.getStorageSync('userInfo') || {};
		
		if (!this.userId || !this.userToken) {
			uni.showToast({
				title: '请先登录',
				icon: 'error'
			});
			setTimeout(() => {
				uni.navigateBack();
			}, 1500);
		}
	},
	
	methods: {
		// 返回上一页
		goBack() {
			uni.navigateBack();
		},
		
		// 切换密码显示/隐藏
		togglePasswordVisibility(type) {
			if (type === 'old') {
				this.showOldPassword = !this.showOldPassword;
			} else if (type === 'new') {
				this.showNewPassword = !this.showNewPassword;
			} else if (type === 'confirm') {
				this.showConfirmPassword = !this.showConfirmPassword;
			}
		},
		
		// 清除错误信息
		clearError() {
			this.errorMessage = '';
		},
		
		// 处理表单提交 - 修复错误处理逻辑
		async handleSubmit() {
			// 验证表单
			if (!this.validateForm()) {
				return;
			}
			
			// 开始提交
			this.isSubmitting = true;
			this.errorMessage = '';
			
			try {
				uni.showLoading({
					title: '正在修改...',
					mask: true
				});
				
				// 调用API修改密码
				const result = await userApi.changePassword(
					this.passwordForm.oldPassword,
					this.passwordForm.newPassword,
					this.userToken
				);
				
				uni.hideLoading();
				
				console.log('修改密码API返回:', result);
				
				if (result.code === 200) {
					// 修改成功
					this.showSuccessPopup();
					
					// 清空表单
					this.resetForm();
				} else {
					// 修改失败 - 直接使用后端返回的错误信息
					this.errorMessage = result.message || '密码修改失败';
					uni.showToast({
						title: this.errorMessage,
						icon: 'none',
						duration: 3000
					});
				}
			} catch (error) {
				uni.hideLoading();
				console.error('修改密码失败:', error);
				
				// 关键修复：正确显示后端返回的错误信息
				// 检查是否是网络错误
				const errorMsg = error.message || '未知错误';
				
				if (errorMsg.includes('网络') || errorMsg.includes('请求失败')) {
					this.errorMessage = '网络错误，请稍后重试';
				} else {
					// 业务错误，直接显示错误信息
					this.errorMessage = errorMsg;
				}
				
				uni.showToast({
					title: this.errorMessage,
					icon: 'none',
					duration: 3000
				});
			} finally {
				this.isSubmitting = false;
			}
		},
		
		// 验证表单 - 简化验证逻辑
		validateForm() {
			// 验证原密码
			if (!this.passwordForm.oldPassword.trim()) {
				this.errorMessage = '请输入原密码';
				return false;
			}
			
			// 验证新密码
			if (!this.passwordForm.newPassword.trim()) {
				this.errorMessage = '请输入新密码';
				return false;
			}
			
			// 验证新密码长度（只要求大于6位）
			if (this.passwordForm.newPassword.length < 6) {
				this.errorMessage = '密码长度需大于6位，数字+字母';
				return false;
			}
			
			// 验证确认密码
			if (!this.passwordForm.confirmPassword.trim()) {
				this.errorMessage = '请确认新密码';
				return false;
			}
			
			// 验证两次输入是否一致
			if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
				this.errorMessage = '两次输入的密码不一致';
				return false;
			}
			
			// 验证新旧密码是否相同
			if (this.passwordForm.oldPassword === this.passwordForm.newPassword) {
				this.errorMessage = '新密码不能与原密码相同';
				return false;
			}
			
			return true;
		},
		
		// 重置表单
		resetForm() {
			this.passwordForm = {
				oldPassword: '',
				newPassword: '',
				confirmPassword: ''
			};
			this.showOldPassword = false;
			this.showNewPassword = false;
			this.showConfirmPassword = false;
			this.errorMessage = '';
		},
		
		// 显示成功弹窗
		showSuccessPopup() {
			this.$refs.successPopup.open('center');
		},
		
		// 处理取消
		handleCancel() {
			this.$refs.successPopup.close();
			setTimeout(() => {
				uni.navigateBack();
			}, 300);
		},
		
		// 处理重新登录
		handleReLogin() {
			this.$refs.successPopup.close();
			
			// 清除登录状态
			uni.removeStorageSync('userToken');
			uni.removeStorageSync('userId');
			uni.removeStorageSync('username');
			uni.removeStorageSync('userInfo');
			
			// 跳转到登录页面
			setTimeout(() => {
				uni.reLaunch({
					url: '/pages/index/index'
				});
			}, 500);
		}
	}
};
</script>

<style lang="scss" scoped>
.password-page {
	min-height: 100vh;
	background-color: #f5f5f5;
}

.page-header {
	display: flex;
	align-items: center;
	background-color: #fff;
	padding: 30rpx;
	border-bottom: 2rpx solid #f0f0f0;
	
	.back-btn {
		padding: 10rpx;
		margin-right: 20rpx;
		
		&:active {
			opacity: 0.6;
		}
	}
	
	.page-title {
		font-size: 34rpx;
		font-weight: bold;
		color: #333;
	}
}

.password-form {
	padding: 40rpx 30rpx;
}

.input-group {
	margin-bottom: 40rpx;
	
	.input-label {
		display: block;
		font-size: 30rpx;
		color: #333;
		margin-bottom: 20rpx;
		font-weight: 500;
	}
	
	.input-wrapper {
		display: flex;
		align-items: center;
		background-color: #fff;
		border: 2rpx solid #e0e0e0;
		border-radius: 10rpx;
		padding: 0 20rpx;
		height: 90rpx;
		
		&:focus-within {
			border-color: #4a90e2;
		}
		
		.password-input {
			flex: 1;
			height: 100%;
			font-size: 28rpx;
			color: #333;
		}
		
		.eye-icon {
			padding: 10rpx;
			display: flex;
			align-items: center;
			
			&:active {
				opacity: 0.6;
			}
		}
	}
	
	.password-hint {
		margin-top: 15rpx;
		
		.hint-text {
			font-size: 24rpx;
			color: #666;
		}
	}
}

.error-tip {
	display: flex;
	align-items: center;
	background-color: #fff2f2;
	border: 2rpx solid #ffcccc;
	border-radius: 8rpx;
	padding: 20rpx;
	margin-bottom: 30rpx;
	
	.error-text {
		flex: 1;
		font-size: 26rpx;
		color: #ff4444;
		margin-left: 10rpx;
	}
}

.submit-btn {
	width: 100%;
	height: 90rpx;
	background: linear-gradient(135deg, #4a90e2, #5a8bff);
	color: #fff;
	border-radius: 45rpx;
	font-size: 32rpx;
	font-weight: bold;
	margin-top: 30rpx;
	
	&::after {
		border: none;
	}
	
	&:active {
		opacity: 0.9;
	}
	
	&.disabled {
		background: #cccccc;
		color: #fff;
		opacity: 0.6;
	}
}

.success-popup {
	width: 600rpx;
	background-color: #fff;
	border-radius: 20rpx;
	padding: 60rpx 40rpx;
	text-align: center;
	
	.success-title {
		display: block;
		font-size: 36rpx;
		font-weight: bold;
		color: #333;
		margin: 30rpx 0 20rpx;
	}
	
	.success-desc {
		display: block;
		font-size: 28rpx;
		color: #666;
		margin-bottom: 50rpx;
		line-height: 1.5;
	}
	
	.popup-buttons {
		display: flex;
		justify-content: space-between;
		gap: 20rpx;
		
		.popup-btn {
			flex: 1;
			height: 80rpx;
			border-radius: 40rpx;
			font-size: 30rpx;
			font-weight: 500;
			
			&::after {
				border: none;
			}
			
			&:active {
				opacity: 0.9;
			}
		}
		
		.cancel-btn {
			background-color: #f0f0f0;
			color: #666;
		}
		
		.confirm-btn {
			background: linear-gradient(135deg, #4a90e2, #5a8bff);
			color: #fff;
		}
	}
}
</style>