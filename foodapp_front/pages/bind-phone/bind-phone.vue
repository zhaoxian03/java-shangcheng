<template>
	<view class="bind-phone-page">
		<!-- <view class="header">
			<uni-icons type="arrow-left" size="24" class="back-icon" @click="goBack"></uni-icons>
			<text class="header-title">{{ currentPhone ? '修改手机号' : '绑定手机号' }}</text>
		</view> -->

		<view class="content">
			<!-- 当前绑定状态 -->
			<view class="status-card" v-if="currentPhone">
				<uni-icons type="checkmark-circle-filled" size="24" color="#4a90e2"></uni-icons>
				<text class="status-text">已绑定手机号：{{ maskedPhone }}</text>
			</view>
			
			<view class="status-card warning" v-else>
				<uni-icons type="info" size="24" color="#ff9900"></uni-icons>
				<text class="status-text">您还未绑定手机号</text>
			</view>

			<!-- 手机号输入框 -->
			<view class="input-container">
				<view class="input-label">手机号码</view>
				<input 
					type="number" 
					v-model="phone" 
					:placeholder="currentPhone ? '请输入新手机号' : '请输入要绑定的手机号'" 
					class="phone-input"
					maxlength="11"
					@input="formatPhone"
				/>
				<text class="input-hint" v-if="!isValidPhone && phone">请输入正确的11位手机号</text>
			</view>

			<!-- 操作按钮 -->
			<button 
				class="submit-btn" 
				:class="{ 'disabled': !isValidPhone || isLoading }"
				:disabled="!isValidPhone || isLoading"
				@click="handleSavePhone"
			>
				{{ isLoading ? '处理中...' : (currentPhone ? '修改手机号' : '绑定手机号') }}
			</button>
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
			currentPhone: '',
			phone: '',
			isLoading: false,
			userInfo: null
		};
	},

	computed: {
		maskedPhone() {
			if (!this.currentPhone) return '';
			return this.currentPhone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2');
		},

		isValidPhone() {
			const phone = this.phone;
			return phone && phone.length === 11 && /^1[3-9]\d{9}$/.test(phone);
		}
	},

	onLoad(options) {
		this.userId = options.userId || uni.getStorageSync('userId');
		this.userToken = uni.getStorageSync('userToken');
		
		if (!this.userId) {
			uni.showToast({
				title: '请先登录',
				icon: 'error'
			});
			setTimeout(() => {
				uni.navigateBack();
			}, 1500);
			return;
		}
		
		this.loadPhoneStatus();
	},

	methods: {
		// 格式化手机号
		formatPhone() {
			// 移除所有空格
			this.phone = this.phone.replace(/\s+/g, '');
			// 只保留数字
			this.phone = this.phone.replace(/\D/g, '');
			// 限制11位
			if (this.phone.length > 11) {
				this.phone = this.phone.substring(0, 11);
			}
		},
		
		async loadPhoneStatus() {
			try {
				uni.showLoading({ title: '加载中...' });

				let userInfo = uni.getStorageSync('userInfo');
				
				if (!userInfo) {
					const result = await userApi.getProfile(this.userToken);
					if (result.code === 200 && result.data) {
						userInfo = result.data;
						uni.setStorageSync('userInfo', userInfo);
					}
				}
				
				this.userInfo = userInfo;
				
				if (userInfo) {
					const phone = userInfo.phone || userInfo.phoneNumber || userInfo.mobile || userInfo.tel;
					if (phone && phone.length === 11) {
						this.currentPhone = phone;
						this.phone = phone;
					} else {
						this.currentPhone = '';
						this.phone = '';
					}
				}
				
			} catch (error) {
				console.error('加载失败:', error);
			} finally {
				uni.hideLoading();
			}
		},

		// 主方法：保存手机号
		async handleSavePhone() {
		    if (!this.isValidPhone || this.isLoading) return;
		    
		    try {
		        this.isLoading = true;
		        uni.showLoading({ title: '保存中...', mask: true });
		
		        console.log('开始更新手机号...');
		        
		        // 获取当前用户名（作为name字段）
		        let currentName = '';
		        if (this.userInfo && (this.userInfo.username || this.userInfo.name)) {
		            currentName = this.userInfo.username || this.userInfo.name;
		        } else {
		            // 如果没有，从后端获取
		            try {
		                const profileResult = await userApi.getProfile(this.userToken);
		                currentName = profileResult.data?.username || profileResult.data?.name || 'user_' + this.userId;
		            } catch (error) {
		                currentName = 'user_' + this.userId;
		            }
		        }
		        
		        console.log('使用name字段:', currentName);
		        
		        // 使用savePhone函数（会正确处理name字段）
		        const result = await userApi.savePhone(this.userId, this.phone, this.userToken, currentName);
		        
		        console.log('更新结果:', result);
		        
		        if (result.code === 200) {
		            uni.showToast({
		                title: '手机号更新成功!',
		                icon: 'success',
		                duration: 2000
		            });
		            
		            // 更新本地数据
		            this.currentPhone = this.phone;
		            
		            // 更新本地存储
		            const userInfo = uni.getStorageSync('userInfo') || {};
		            userInfo.phone = this.phone;
		            uni.setStorageSync('userInfo', userInfo);
		            
		            // ✅ 只修改这里：去除自动跳转，停留在当前页面
		            // 用户可以通过点击返回按钮手动返回
		            
		        } else {
		            throw new Error(result.message || '更新失败');
		        }
		        
		    } catch (error) {
		        console.error('保存失败:', error);
		        
		        // 如果失败，尝试备用方法
		        try {
		            await this.tryBackupUpdate();
		        } catch (backupError) {
		            uni.showToast({
		                title: '更新失败: ' + error.message,
		                icon: 'none',
		                duration: 3000
		            });
		        }
		    } finally {
		        this.isLoading = false;
		        uni.hideLoading();
		    }
		},
		
		// 备用更新方法
		async tryBackupUpdate() {
		    try {
		        console.log('尝试备用更新方法...');
		        
		        // 获取用户名
		        let name = '';
		        if (this.userInfo && (this.userInfo.username || this.userInfo.name)) {
		            name = this.userInfo.username || this.userInfo.name;
		        } else {
		            const profileResult = await userApi.getProfile(this.userToken);
		            name = profileResult.data?.username || profileResult.data?.name || 'user_' + this.userId;
		        }
		        
		        // 构建最小化数据
		        const updateData = {
		            userId: String(this.userId),
		            name: name,  // 关键：发送name字段
		            phone: this.phone
		        };
		        
		        console.log('备用方法发送数据:', updateData);
		        
		        const result = await uni.request({
		            url: 'http://localhost:8089/api/user/update',
		            method: 'POST',
		            header: {
		                'Content-Type': 'application/json',
		                'Authorization': 'Bearer ' + this.userToken
		            },
		            data: updateData
		        });
		        
		        console.log('备用方法响应:', result.data);
		        
		        if (result.data.code === 200) {
		            uni.showToast({
		                title: '手机号更新成功!',
		                icon: 'success',
		                duration: 2000
		            });
		            
		            this.currentPhone = this.phone;
		            const userInfo = uni.getStorageSync('userInfo') || {};
		            userInfo.phone = this.phone;
		            uni.setStorageSync('userInfo', userInfo);
		            
		            // ✅ 同样去除自动跳转，停留在当前页面
		            return true;
		        } else {
		            throw new Error(result.data.message || '更新失败');
		        }
		        
		    } catch (error) {
		        console.error('备用方法失败:', error);
		        throw error;
		    }
		},

		// 返回
		goBack() {
			uni.navigateBack();
		}
	}
};
</script>

<style lang="scss" scoped>
.bind-phone-page {
	// min-height: 100vh;
	// background-color: #f5f5f5;
	// overflow: hidden; /* 禁止页面滚动 */
	height: 100vh;
	 background-color: #f5f5f5;
	/* 就这一行，保证页面不滚动 */
	overflow: hidden !important;
}

.header {
	height: 88rpx;
	background-color: #fff;
	display: flex;
	align-items: center;
	padding: 0 30rpx;
	border-bottom: 1rpx solid #f0f0f0;
	position: sticky;
	top: 0;
	z-index: 10;

	.back-icon {
		padding: 10rpx;
		margin-right: 20rpx;
	}

	.header-title {
		font-size: 36rpx;
		font-weight: bold;
		color: #333;
	}
}

.content {
	height: calc(100vh - 88rpx);
	padding: 30rpx;
}

.status-card {
	background-color: #e8f4ff;
	border-radius: 16rpx;
	padding: 30rpx;
	display: flex;
	align-items: center;
	margin-bottom: 40rpx;
	
	&.warning {
		background-color: #fff8e8;
	}

	.status-text {
		font-size: 28rpx;
		color: #4a90e2;
		margin-left: 20rpx;
		
		.warning & {
			color: #ff9900;
		}
	}
}

.input-container {
	background-color: #fff;
	border-radius: 16rpx;
	padding: 30rpx;
	margin-bottom: 40rpx;

	.input-label {
		font-size: 32rpx;
		color: #333;
		margin-bottom: 20rpx;
		font-weight: 500;
	}

	.phone-input {
		width: 100%;
		height: 90rpx;
		border: 2rpx solid #e0e0e0;
		border-radius: 8rpx;
		padding: 0 20rpx;
		font-size: 32rpx;
		box-sizing: border-box;
		background-color: #f9f9f9;

		&:focus {
			border-color: #4a90e2;
			background-color: #fff;
		}
	}

	.input-hint {
		display: block;
		font-size: 26rpx;
		color: #ff4444;
		margin-top: 10rpx;
	}
}

.submit-btn {
	width: 100%;
	height: 90rpx;
	background: linear-gradient(135deg, #4a90e2, #5a8bff);
	color: #fff;
	font-size: 34rpx;
	font-weight: bold;
	border-radius: 45rpx;
	border: none;
	margin-bottom: 30rpx;

	&.disabled {
		background: #ccc;
		color: #999;
	}
}
</style>