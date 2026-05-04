<template>
	<view class="feedback-page">
		<!-- 页面标题 -->
		<!-- <view class="page-header"> -->
			<!-- <view class="back-btn" @click="goBack"> -->
				<!-- <uni-icons type="arrowleft" size="24" color="#333"></uni-icons> -->
			<!-- </view> -->
			<!-- <text class="page-title">意见反馈</text> -->
		<!-- </view> -->

		<!-- 表单内容 -->
		<view class="form-content">
			<!-- 反馈类型 -->
			<view class="form-group">
				<text class="form-label">反馈类型</text>
				<view class="type-select">
					<view 
						class="type-item" 
						:class="{ active: formData.type === 1 }"
						@click="selectType(1)"
					>
						<text>功能建议</text>
					</view>
					<view 
						class="type-item" 
						:class="{ active: formData.type === 2 }"
						@click="selectType(2)"
					>
						<text>问题反馈</text>
					</view>
					<view 
						class="type-item" 
						:class="{ active: formData.type === 3 }"
						@click="selectType(3)"
					>
						<text>其他</text>
					</view>
				</view>
			</view>

			<!-- 反馈内容 -->
			<view class="form-group">
				<text class="form-label">反馈内容</text>
				<textarea 
					class="feedback-textarea" 
					v-model="formData.content" 
					placeholder="请详细描述您的建议或问题，不少于10个字..."
					placeholder-style="color: #999;"
					maxlength="500"
					@input="checkContent"
				></textarea>
				<view class="word-count">
					<text>{{ formData.content.length }}/500</text>
				</view>
			</view>

			<!-- 联系方式（可选） -->
			<view class="form-group">
				<text class="form-label">联系方式（选填）</text>
				<view class="contact-wrapper">
					<input 
						class="contact-input" 
						v-model="formData.contact" 
						placeholder="请输入手机号/邮箱/QQ号"
						placeholder-style="color: #999;"
					/>
					<view class="contact-hint">
						<text>便于我们与您联系，及时反馈处理结果</text>
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
				<text v-if="!isSubmitting">提交反馈</text>
				<text v-else>提交中...</text>
			</button>

			<!-- 提交提示 -->
			<view class="submit-hint">
				<text class="hint-title">温馨提示：</text>
				<text class="hint-text">
					1. 请详细描述问题，帮助我们更好地改进<br>
					2. 我们会在1-3个工作日内处理您的反馈<br>
					3. 感谢您对产品的支持与建议
				</text>
			</view>
		</view>

		<!-- 提交成功弹窗 -->
		<uni-popup ref="successPopup" type="center" background-color="#fff">
			<view class="success-popup">
				<uni-icons type="checkmark" size="60" color="#4CAF50"></uni-icons>
				<text class="success-title">提交成功</text>
				<text class="success-desc">感谢您的反馈，我们会尽快处理</text>
				<view class="popup-buttons">
					<button class="popup-btn confirm-btn" @click="handleConfirm">确定</button>
				</view>
			</view>
		</uni-popup>
	</view>
</template>

<script>
import { feedbackApi } from '@/api/feedback.js';

export default {
	data() {
		return {
			// 表单数据
			formData: {
				type: 1, // 1:功能建议, 2:问题反馈, 3:其他
				content: '',
				contact: ''
			},
			// 错误信息
			errorMessage: '',
			// 提交状态
			isSubmitting: false,
			// 用户信息
			userId: '',
			userToken: ''
		};
	},
	
	computed: {
		// 表单验证
		isFormValid() {
			return (
				this.formData.content.trim().length >= 10 &&
				!this.isSubmitting
			);
		}
	},
	
	onLoad() {
		// 获取用户信息
		this.userId = uni.getStorageSync('userId') || '';
		this.userToken = uni.getStorageSync('userToken') || '';
	},
	
	methods: {
		// 返回上一页
		goBack() {
			uni.navigateBack();
		},
		
		// 选择反馈类型
		selectType(type) {
			this.formData.type = type;
		},
		
		// 检查内容长度
		checkContent() {
			if (this.formData.content.length > 500) {
				this.formData.content = this.formData.content.substring(0, 500);
			}
		},
		
		// 处理表单提交
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
					title: '提交中...',
					mask: true
				});
				
				// 提交反馈数据
				const feedbackData = {
					type: this.formData.type,
					content: this.formData.content.trim(),
					contact: this.formData.contact.trim()
				};
				
				// 调用API提交反馈
				const result = await feedbackApi.submitFeedback(feedbackData);
				
				uni.hideLoading();
				
				if (result.code === 200) {
					// 提交成功
					this.showSuccessPopup();
					// 清空表单
					this.resetForm();
				} else {
					this.errorMessage = result.message || '提交失败，请稍后重试';
					uni.showToast({
						title: this.errorMessage,
						icon: 'none'
					});
				}
			} catch (error) {
				uni.hideLoading();
				console.error('提交反馈失败:', error);
				this.errorMessage = '提交失败，请检查网络连接';
				uni.showToast({
					title: this.errorMessage,
					icon: 'none'
				});
			} finally {
				this.isSubmitting = false;
			}
		},
		
		// 验证表单
		validateForm() {
			// 验证反馈内容
			if (!this.formData.content.trim()) {
				this.errorMessage = '请输入反馈内容';
				return false;
			}
			
			if (this.formData.content.trim().length < 10) {
				this.errorMessage = '反馈内容不少于10个字';
				return false;
			}
			
			// 验证联系方式格式（如果填写了）
			if (this.formData.contact.trim()) {
				const contact = this.formData.contact.trim();
				// 简单的格式验证
				const phoneRegex = /^1[3-9]\d{9}$/;
				const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
				const qqRegex = /^[1-9][0-9]{4,10}$/;
				
				if (!phoneRegex.test(contact) && !emailRegex.test(contact) && !qqRegex.test(contact)) {
					this.errorMessage = '请输入有效的手机号、邮箱或QQ号';
					return false;
				}
			}
			
			return true;
		},
		
		// 重置表单
		resetForm() {
			this.formData = {
				type: 1,
				content: '',
				contact: ''
			};
		},
		
		// 显示成功弹窗
		showSuccessPopup() {
			this.$refs.successPopup.open('center');
		},
		
		// 处理确认
		handleConfirm() {
			this.$refs.successPopup.close();
			setTimeout(() => {
				uni.navigateBack();
			}, 300);
		}
	}
};
</script>

<style lang="scss" scoped>
.feedback-page {
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

.form-content {
	padding: 30rpx;
}

.form-group {
	margin-bottom: 40rpx;
	background-color: #fff;
	border-radius: 16rpx;
	padding: 30rpx;
	
	.form-label {
		display: block;
		font-size: 32rpx;
		font-weight: 500;
		color: #333;
		margin-bottom: 30rpx;
	}
}

.type-select {
	display: flex;
	gap: 20rpx;
	
	.type-item {
		flex: 1;
		height: 80rpx;
		background-color: #f5f5f5;
		border-radius: 8rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
		color: #666;
		
		&.active {
			background-color: #e6f3ff;
			color: #4a90e2;
			font-weight: 500;
		}
		
		&:active {
			opacity: 0.8;
		}
	}
}

.feedback-textarea {
	width: 100%;
	height: 300rpx;
	background-color: #f9f9f9;
	border: 2rpx solid #e0e0e0;
	border-radius: 8rpx;
	padding: 20rpx;
	font-size: 28rpx;
	color: #333;
	box-sizing: border-box;
	
	&:focus {
		border-color: #4a90e2;
		background-color: #fff;
	}
}

.word-count {
	text-align: right;
	margin-top: 10rpx;
	
	text {
		font-size: 24rpx;
		color: #999;
	}
}

.contact-wrapper {
	.contact-input {
		width: 100%;
		height: 80rpx;
		background-color: #f9f9f9;
		border: 2rpx solid #e0e0e0;
		border-radius: 8rpx;
		padding: 0 20rpx;
		font-size: 28rpx;
		color: #333;
		box-sizing: border-box;
		
		&:focus {
			border-color: #4a90e2;
			background-color: #fff;
		}
	}
	
	.contact-hint {
		margin-top: 15rpx;
		
		text {
			font-size: 24rpx;
			color: #999;
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
	margin: 20rpx 0;
	
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

.submit-hint {
	margin-top: 40rpx;
	padding: 30rpx;
	background-color: #fff;
	border-radius: 16rpx;
	
	.hint-title {
		display: block;
		font-size: 28rpx;
		font-weight: 500;
		color: #ff9800;
		margin-bottom: 15rpx;
	}
	
	.hint-text {
		display: block;
		font-size: 26rpx;
		color: #666;
		line-height: 1.8;
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
		.popup-btn {
			width: 100%;
			height: 80rpx;
			background: linear-gradient(135deg, #4a90e2, #5a8bff);
			color: #fff;
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
	}
}
</style>