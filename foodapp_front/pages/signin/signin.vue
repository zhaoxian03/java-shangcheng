<template>
	<view class="signin-page">

		<!-- 内容区域 -->
		<view class="content">
			<!-- 签到方块 -->
			<view 
				class="sign-square" 
				:class="{ 'signed': hasSigned, 'disabled': isLoading }"
				@click="signin"
			>
				<view class="square-content">
					<image 
						src="/static/other/qiandao.png" 
						class="sign-icon"
						:class="{ 'signed-icon': hasSigned }"
					></image>
					<text class="sign-text">{{ hasSigned ? '今日已签到' : '立即签到' }}</text>
					<text class="sign-tip" v-if="hasSigned">{{ formatTime(signTime) }}</text>
					<text class="sign-tip" v-else>点击签到</text>
				</view>
			</view>

			<!-- 签到统计 -->
			<view class="stats-box">
				<text class="stats-title">签到统计</text>
				<view class="stats-grid">
					<view class="stat-item">
						<text class="stat-number">{{ totalDays }}</text>
						<text class="stat-label">累计天数</text>
					</view>
					<view class="stat-divider"></view>
					<view class="stat-item">
						<text class="stat-number">0</text>
						<text class="stat-label">连续天数</text>
					</view>
					<view class="stat-divider"></view>
					<view class="stat-item">
						<text class="stat-number">{{ totalDays }}</text>
						<text class="stat-label">本月签到</text>
					</view>
				</view>
			</view>

			<!-- 签到记录 -->
			<view class="record-box">
				<view class="record-header">
					<text class="record-title">最近签到</text>
					<text class="record-count">共 {{ totalDays }} 次</text>
				</view>
				
				<view class="record-list">
					<view class="record-item" v-for="(item, index) in getRecentDays()" :key="index">
						<text class="record-day">{{ item }}</text>
						<view class="record-status" :class="{ 'signed': index < totalDays }">
							<text v-if="index < totalDays">✓</text>
							<text v-else>-</text>
						</view>
					</view>
				</view>
			</view>

			<!-- 签到规则 -->
			<view class="rules-box">
				<text class="rules-title">签到规则</text>
				<view class="rules-content">
					<text class="rule-text">• 每日可签到一次，签到时间00:00-23:59</text>
					<text class="rule-text">• 签到记录永久保存，可随时查看</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import { signinApi } from '../../api/signin.js';

export default {
	data() {
		return {
			hasSigned: false,
			totalDays: 0,
			signTime: '',
			userId: null,
			isLoading: false
		}
	},

	onLoad(options) {
		this.userId = options.userId || uni.getStorageSync('userId');
		
		if (!this.userId) {
			uni.showToast({
				title: '请先登录',
				icon: 'none'
			});
			setTimeout(() => {
				uni.navigateBack();
			}, 1500);
			return;
		}

		this.loadSigninInfo();
	},

	methods: {
		async loadSigninInfo() {
			try {
				const result = await signinApi.getInfo(this.userId);
				
				if (result.code === 200) {
					this.hasSigned = result.data.todaySigned;
					this.totalDays = result.data.totalDays || 0;
					this.signTime = result.data.signinTime || '';
				}
			} catch (error) {
				console.error('加载签到信息出错:', error);
			}
		},

		async signin() {
			if (this.isLoading || this.hasSigned) {
				return;
			}

			this.isLoading = true;

			try {
				const result = await signinApi.doSignin(this.userId);
				
				if (result.code === 200) {
					this.hasSigned = true;
					this.totalDays = result.data.totalDays || this.totalDays + 1;
					this.signTime = new Date().toLocaleTimeString();
					
					uni.showToast({
						title: '签到成功 +1天',
						icon: 'success',
						duration: 1500
					});
				} else {
					uni.showToast({
						title: result.message || '签到失败',
						icon: 'none'
					});
				}
			} catch (error) {
				console.error('签到出错:', error);
				uni.showToast({
					title: '签到失败',
					icon: 'none'
				});
			} finally {
				this.isLoading = false;
			}
		},

		goBack() {
			uni.navigateBack();
		},

		formatTime(timeStr) {
			if (!timeStr) return '今日';
			if (timeStr.includes(' ')) {
				return timeStr.split(' ')[1];
			}
			return timeStr;
		},

		getRecentDays() {
			// 显示最近7天
			return ['今天', '昨天', '前天', '3天前', '4天前', '5天前', '6天前'];
		}
	}
}
</script>

<style scoped>
/* 页面样式 */
.signin-page {
	min-height: 100vh;
	background-color: #f8f9fa;
}

/* 头部样式 */
.header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	height: 100rpx;
	padding: 0 32rpx;
	background-color: #fff;
	border-bottom: 1rpx solid #e9ecef;
}

.back-btn {
	width: 80rpx;
	height: 80rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}

.back-icon {
	font-size: 56rpx;
	color: #333;
	font-weight: 300;
}

.title {
	font-size: 36rpx;
	font-weight: 600;
	color: #333;
}

.placeholder {
	width: 80rpx;
}

/* 内容区域 */
.content {
	padding: 32rpx;
}

/* 签到方块 */
.sign-square {
	background: linear-gradient(135deg, #4a90e2, #5a8bff);
	border-radius: 20rpx;
	padding: 40rpx;
	margin-bottom: 32rpx;
	box-shadow: 0 8rpx 24rpx rgba(74, 144, 226, 0.2);
	transition: all 0.2s ease;
}

.sign-square.signed {
	background: linear-gradient(135deg, #6c757d, #868e96);
}

.sign-square:active:not(.signed):not(.disabled) {
	transform: translateY(2rpx);
	box-shadow: 0 4rpx 12rpx rgba(74, 144, 226, 0.2);
}

.square-content {
	display: flex;
	flex-direction: column;
	align-items: center;
}

.sign-icon {
	width: 120rpx;
	height: 120rpx;
	margin-bottom: 20rpx;
}

.sign-square.signed .sign-icon {
	filter: grayscale(100%) brightness(120%);
}

.sign-text {
	font-size: 40rpx;
	font-weight: bold;
	color: #fff;
	margin-bottom: 12rpx;
}

.sign-tip {
	font-size: 28rpx;
	color: rgba(255, 255, 255, 0.9);
}

/* 统计盒子 */
.stats-box {
	background: #fff;
	border-radius: 20rpx;
	padding: 32rpx;
	margin-bottom: 32rpx;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
}

.stats-title {
	display: block;
	font-size: 32rpx;
	font-weight: 600;
	color: #333;
	margin-bottom: 32rpx;
}

.stats-grid {
	display: flex;
	justify-content: space-around;
	align-items: center;
}

.stat-item {
	display: flex;
	flex-direction: column;
	align-items: center;
	flex: 1;
}

.stat-number {
	font-size: 56rpx;
	font-weight: bold;
	color: #4a90e2;
	margin-bottom: 8rpx;
	line-height: 1;
}

.stat-label {
	font-size: 26rpx;
	color: #666;
}

.stat-divider {
	width: 1rpx;
	height: 60rpx;
	background: #e9ecef;
}

/* 记录盒子 */
.record-box {
	background: #fff;
	border-radius: 20rpx;
	padding: 32rpx;
	margin-bottom: 32rpx;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
}

.record-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 32rpx;
}

.record-title {
	font-size: 32rpx;
	font-weight: 600;
	color: #333;
}

.record-count {
	font-size: 28rpx;
	color: #666;
}

.record-list {
	display: flex;
	flex-wrap: wrap;
	gap: 24rpx;
}

.record-item {
	width: calc((100% - 72rpx) / 4);
	display: flex;
	flex-direction: column;
	align-items: center;
}

.record-day {
	font-size: 26rpx;
	color: #666;
	margin-bottom: 16rpx;
}

.record-status {
	width: 60rpx;
	height: 60rpx;
	border-radius: 12rpx;
	background: #f8f9fa;
	border: 2rpx solid #e9ecef;
	display: flex;
	align-items: center;
	justify-content: center;
}

.record-status.signed {
	background: #4a90e2;
	border-color: #4a90e2;
}

.record-status text {
	font-size: 28rpx;
	color: #adb5bd;
	font-weight: bold;
}

.record-status.signed text {
	color: #fff;
}

/* 规则盒子 */
.rules-box {
	background: #fff;
	border-radius: 20rpx;
	padding: 32rpx;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
}

.rules-title {
	display: block;
	font-size: 32rpx;
	font-weight: 600;
	color: #333;
	margin-bottom: 24rpx;
}

.rules-content {
	display: flex;
	flex-direction: column;
	gap: 16rpx;
}

.rule-text {
	font-size: 28rpx;
	color: #666;
	line-height: 1.6;
}

/* 加载状态 */
.sign-square.disabled {
	opacity: 0.7;
	position: relative;
	overflow: hidden;
}

.sign-square.disabled::after {
	content: '';
	position: absolute;
	top: 0;
	left: -100%;
	width: 100%;
	height: 100%;
	background: linear-gradient(90deg, 
		transparent, 
		rgba(255, 255, 255, 0.2), 
		transient);
	animation: shimmer 1.5s infinite;
}

@keyframes shimmer {
	0% { left: -100%; }
	100% { left: 100%; }
}

/* 响应式调整 */
@media (max-width: 375px) {
	.content {
		padding: 24rpx;
	}
	
	.sign-square {
		padding: 32rpx 24rpx;
	}
	
	.sign-icon {
		width: 100rpx;
		height: 100rpx;
	}
	
	.sign-text {
		font-size: 36rpx;
	}
	
	.stat-number {
		font-size: 48rpx;
	}
	
	.record-item {
		width: calc((100% - 48rpx) / 4);
	}
}
</style>