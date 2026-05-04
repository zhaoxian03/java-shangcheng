<template>
	<view class="our-page">
		<!-- 自定义头部 -->
		<view class="header">
			<!-- <view class="back-btn" @click="goBack">
				<uni-icons type="arrowleft" size="24" color="#333"></uni-icons>
				<text class="back-text">返回</text>
			</view> -->
			<!-- <view class="title">关于我们</view> -->
			<view class="placeholder"></view>
		</view>
		
		<!-- 内容区域 -->
		<view class="content">
			<!-- Logo和标题 -->
			<view class="top-section">
				<view class="logo-container">
					<image src="/static/other/logo.jpg" class="logo" mode="aspectFit"></image>
				</view>
				<view class="name-container">
					<text class="mall-name">美滋滋商城</text>
					<text class="version">版本号：V1.0.0</text>
				</view>
			</view>
			
			<!-- 基本信息卡片 -->
			<view class="card">
				<view class="card-header">
					<text class="card-title">商城信息</text>
				</view>
				<view class="card-body">
					<view class="info-row">
						<text class="label">商城名称：</text>
						<text class="value">美滋滋商城</text>
					</view>
					<view class="info-row">
						<text class="label">成立时间：</text>
						<text class="value">2025年12月30日</text>
					</view>
					<view class="info-row">
						<text class="label">客服电话：</text>
						<text class="value">400-888-8888</text>
					</view>
					<view class="info-row">
						<text class="label">联系邮箱：</text>
						<text class="value">service@foodmall.com</text>
					</view>
					<view class="info-row">
						<text class="label">公司地址：</text>
						<text class="value">河北省邯郸市丛台区666号</text>
					</view>
				</view>
			</view>
			
			<!-- 商城简介 -->
			<view class="card">
				<view class="card-header">
					<text class="card-title">商城简介</text>
				</view>
				<view class="card-body">
					<view class="intro-text">
						美滋滋商城是一个专注于低价商品的电商平台，致力于为用户提供最优惠、最实用的购物体验。
					</view>
					<view class="intro-text">
						我们与全国各地的商品供应商合作，严格把控产品的质量，确保每一件商品都符合安全标准。
					</view>
					<view class="intro-text">
						平台提供在线购买、快速配送、售后保障等一站式服务，让您足不出户就能享受低价，快捷的购物。
					</view>
					<view class="intro-text highlight">
						我们的使命：让您足不出户就能够买到物美价廉的商品！
					</view>
				</view>
			</view>
			
			<!-- 平台特色 -->
			<view class="card">
				<view class="card-header">
					<text class="card-title">平台特色</text>
				</view>
				<view class="card-body">
					<view class="feature-item">
						<view class="feature-icon">✓</view>
						<view class="feature-content">
							<text class="feature-title">源头直供</text>
							<text class="feature-desc">与产地直接合作，减少中间环节，确保商品配送时间减少</text>
						</view>
					</view>
					<view class="feature-item">
						<view class="feature-icon">✓</view>
						<view class="feature-content">
							<text class="feature-title">严格质检</text>
							<text class="feature-desc">三重质量检测，每批商品都有质检报告</text>
						</view>
					</view>
					<view class="feature-item">
						<view class="feature-icon">✓</view>
						<view class="feature-content">
							<text class="feature-title">快速配送</text>
							<text class="feature-desc">覆盖全国主要城市，最快3小时送达</text>
						</view>
					</view>
					<view class="feature-item">
						<view class="feature-icon">✓</view>
						<view class="feature-content">
							<text class="feature-title">无忧售后</text>
							<text class="feature-desc">7天无理由退货，质量问题先行赔付</text>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 版权信息 -->
			<view class="footer">
				<text class="copyright">© 2026 美滋滋商城 版权所有</text>
				<text class="record">ICP备案号：冀ICP备2026000001号</text>
				<text class="record">经营许可证：JY202601000001</text>
			</view>
			
			<view class="safe-area"></view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {}
	},
	methods: {
		// 返回上一页
		goBack() {
			uni.navigateBack()
		}
	}
}
</script>

<style lang="scss" scoped>
.our-page {
	height: 100vh;
	background-color: #f5f5f5;
	display: flex;
	flex-direction: column;
	
	.header {
		height: 88rpx;
		background-color: #fff;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 30rpx;
		border-bottom: 1rpx solid #f0f0f0;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
		position: sticky;
		top: 0;
		z-index: 10;
		
		.back-btn {
			display: flex;
			align-items: center;
			
			.back-text {
				font-size: 30rpx;
				color: #333;
				margin-left: 10rpx;
			}
		}
		
		.title {
			font-size: 34rpx;
			font-weight: bold;
			color: #333;
		}
		
		.placeholder {
			width: 120rpx;
		}
	}
	
	.content {
		flex: 1;
		padding: 30rpx;
	}
	
	.top-section {
		background-color: #fff;
		border-radius: 20rpx;
		padding: 50rpx 30rpx;
		margin-bottom: 30rpx;
		display: flex;
		align-items: center;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
		
		.logo-container {
			.logo {
				width: 120rpx;
				height: 120rpx;
				border-radius: 24rpx;
			}
		}
		
		.name-container {
			margin-left: 40rpx;
			flex: 1;
			
			.mall-name {
				display: block;
				font-size: 40rpx;
				font-weight: bold;
				color: #333;
				margin-bottom: 10rpx;
			}
			
			.version {
				font-size: 26rpx;
				color: #666;
			}
		}
	}
	
	.card {
		background-color: #fff;
		border-radius: 20rpx;
		margin-bottom: 30rpx;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
		overflow: hidden;
		
		.card-header {
			padding: 30rpx;
			border-bottom: 1rpx solid #f0f0f0;
			background-color: #f9f9f9;
			
			.card-title {
				font-size: 32rpx;
				font-weight: bold;
				color: #333;
			}
		}
		
		.card-body {
			padding: 30rpx;
		}
	}
	
	.info-row {
		display: flex;
		padding: 20rpx 0;
		border-bottom: 1rpx solid #f5f5f5;
		
		&:last-child {
			border-bottom: none;
		}
		
		.label {
			flex-shrink: 0;
			width: 180rpx;
			font-size: 30rpx;
			color: #666;
		}
		
		.value {
			flex: 1;
			font-size: 30rpx;
			color: #333;
			font-weight: 500;
		}
	}
	
	.intro-text {
		font-size: 30rpx;
		line-height: 1.8;
		color: #333;
		margin-bottom: 25rpx;
		text-align: justify;
		
		&:last-child {
			margin-bottom: 0;
		}
		
		&.highlight {
			color: #4a90e2;
			font-weight: bold;
			text-align: center;
			margin-top: 10rpx;
		}
	}
	
	.feature-item {
		display: flex;
		align-items: flex-start;
		padding: 25rpx 0;
		border-bottom: 1rpx solid #f5f5f5;
		
		&:last-child {
			border-bottom: none;
		}
		
		.feature-icon {
			width: 50rpx;
			height: 50rpx;
			background: linear-gradient(135deg, #4a90e2, #5a8bff);
			border-radius: 50%;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-right: 25rpx;
			flex-shrink: 0;
			color: #fff;
			font-weight: bold;
			font-size: 28rpx;
		}
		
		.feature-content {
			flex: 1;
			display: flex;
			flex-direction: column;
			
			.feature-title {
				font-size: 32rpx;
				font-weight: bold;
				color: #333;
				margin-bottom: 10rpx;
			}
			
			.feature-desc {
				font-size: 28rpx;
				color: #666;
				line-height: 1.5;
			}
		}
	}
	
	.footer {
		padding: 40rpx 0;
		display: flex;
		flex-direction: column;
		align-items: center;
		
		.copyright {
			font-size: 26rpx;
			color: #999;
			margin-bottom: 15rpx;
		}
		
		.record {
			font-size: 24rpx;
			color: #bbb;
			margin-bottom: 8rpx;
		}
	}
	
	.safe-area {
		height: 60rpx;
	}
}
</style>