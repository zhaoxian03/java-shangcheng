<template>
	<!-- 头部 -->
	<view class="top">
		<!-- 商品分类名称 -->
		<scroll-view scroll-x="true" class="categorylist">
			<view class="categoryitem" :class="selectedCategoryitem==index?'active':''" v-for="(item,index) in categorylist"
				@click="clickCategoryitem(index,item.cgid)">
				{{item.cgname}}
			</view>
		</scroll-view>
		<!-- 商品分类名称 -->

		<!-- 搜索 -->
		<view class="searchparent">
			<view class="search-container">
				<view class="search">
					<uni-icons type="search" size="25" color="#AAAAAA"></uni-icons>
					<input type="text" name="keyword" v-model="keyword" placeholder="请输入搜索关键词" />
					<uni-icons type="camera" size="25" color="#AAAAAA"></uni-icons>
					<view class="search-btn" @click="search">搜索</view>
				</view>
			</view>
			<navigator url="/pages/cart/cart" open-type="switchTab">
				<view class="cart-icon">
					<uni-icons type="cart" size="25"></uni-icons>
					<view class="cart-nums" v-if="cartAllTotalNums > 0">{{cartAllTotalNums}}</view>
				</view>
			</navigator>
		</view>
		<!-- 搜索 -->
	</view>
	<!-- 头部 -->

	<!-- 垫出头部高度 -->
	<view style="width: 100%;height: 180rpx;"></view>
	<!-- 垫出头部高度 -->

	<!-- 订单及购物车 -->
	<view class="orders_cart_parent">
		<navigator url="/pages/orders/orders" open-type="switchTab" style="display: block;height: 100%;line-height: 55rpx;" class="orders_cart_item">
			<uni-icons type="calendar" size="25"></uni-icons>
			<text class="item_text">我的订单</text>
			<text class="item_text">快捷查单</text>
		</navigator>

		<!-- <view class="orders_cart_item"> -->
			<navigator url="/pages/cart/cart" open-type="switchTab" style="display: block;height: 100%;line-height: 55rpx;" class="orders_cart_item">
				<uni-icons type="cart" size="25"></uni-icons>
				<text class="item_text">购物车</text>
				<text class="item_text">商品({{cartAllTotalNums}})</text>
			</navigator>
		<!-- </view> -->

	</view>
	<!-- 订单及购物车 -->

	<!-- 轮播广告 -->
	<swiper autoplay="true" indicator-dots="true" interval="3000" class="bannerparent">
		<swiper-item>
			<image src="/static/lunbo1.png" class="bannerimg"></image>
		</swiper-item>
		<swiper-item>
			<image src="/static/lunbo2.png" class="bannerimg"></image>
		</swiper-item>
		<swiper-item>
			<image src="/static/lunbo3.png" class="bannerimg"></image>
		</swiper-item>
	</swiper>
	<!-- 轮播广告 -->

	<!-- 商品列表组件 -->
	<GoodsList ref="mygoodslist"></GoodsList>
	<!-- 商品列表组件 -->
</template>

<script>
	/**
	 * 使用组件分为3步骤
	 * 1. 创建组件
	 * 2. 引入组件并且注册组件
	 * 3. 使用组件
	 */
	import GoodsList from '@/components/GoodsList/GoodsList.vue'
	const app = getApp({
		allowDefault: true
	})
	export default {
		// 注册组件
		components: {
			GoodsList
		},
		data() {
			return {
				categorylist: [],
				selectedCategoryitem: 0,
				keyword: '',
				cartAllTotalNums: 0
			}
		},
		// 生命周期函数
		created() {
			// 调用远程加载商品分类
			this.loadCategoryList()
			// 加载购物车数量
			this.loadCart()
			// 设置初始选中的分类为推荐
			this.$nextTick(() => {
				this.selectedCategoryitem = 0
			})
		},
		methods: {
			loadCart() {
				// 加载购物车的数据
				 this.cartlist =[]
				// 发送请求
				const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
				uni.request({
					url: app.globalData.baseUrl + 'api/cart/getcartlist',
					header: {
						'Authorization': token ? `Bearer ${token}` : ''
					},
					success: (res) => {
						if (res.data.code == 200) {
							this.cartlist = res.data.data
							app.globalData.cartAllTotalNums = 0
							this.cartlist.forEach((item, index) => {
								app.globalData.cartAllTotalNums += item.goodsDetail.length
							})
							this.cartAllTotalNums = app.globalData.cartAllTotalNums
						}
					}
				})
			},
			// 搜索
			search() {
				this.$refs.mygoodslist.searchKeyWord(this.keyword)
			},
			//远程加载商品分类
			loadCategoryList() {
				// 发送请求
				uni.request({
					url: app.globalData.baseUrl + 'api/category/findall',
					success: (res) => {
						if (res.data.code == 200) {
							this.categorylist = res.data.data
							console.log(this.categorylist)
						}
					}
				})
			},
			clickCategoryitem(index, cgid) {
				this.selectedCategoryitem = index
				this.$refs.mygoodslist.loadGoodsList(cgid)
			}

		}
	}
</script>

<style lang="scss">
	.top {
		width: 100%;
		background-color: #fff;
		// 设置头部固定
		position: fixed;
		top: 0;
		left: 0;
		z-index: 100;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.1);

		.categorylist {
			width: 100%;
			height: 80rpx;
			margin: 0;
			color: #666;
			font-size: 28rpx;
			font-weight: 500;
			white-space: nowrap;
			background-color: #fff;
			box-shadow: 0 1rpx 5rpx rgba(0, 0, 0, 0.05);

			.categoryitem {
				display: inline-block;
				padding: 0 25rpx;
				height: 80rpx;
				line-height: 80rpx;
				transition: all 0.3s ease;
				border-radius: 40rpx;
				margin: 5rpx 5rpx;
			}

			.active {
				color: #ff4444;
				font-weight: bold;
				background-color: rgba(255, 68, 68, 0.1);
			}

			.active::after {
				content: "";
				display: block;
				width: 40rpx;
				height: 4rpx;
				background-color: #ff4444;
				margin: -5rpx auto 0;
				border-radius: 2rpx;
			}
		}

		.searchparent {
			width: 95%;
			height: 80rpx;
			margin: 15rpx auto;
			display: flex;
			justify-content: space-between;
			align-items: center;
			white-space: nowrap;

			.search-container {
				flex-grow: 1;
				height: 100%;
				display: flex;
				align-items: center;
				border-radius: 40rpx;
				overflow: hidden;
				background-color: #f5f5f5;
				box-shadow: inset 0 2rpx 4rpx rgba(0, 0, 0, 0.05);
			}

			.search {
				flex-grow: 1;
				height: 100%;
				display: flex;
				align-items: center;
				justify-content: space-between;
				padding: 0 20rpx;
				box-sizing: border-box;

				input {
					flex-grow: 1;
					height: 100%;
					border: none;
					background-color: transparent;
					font-size: 28rpx;
					margin: 0 15rpx;
					color: #333;
					placeholder {
						color: #999;
					}
				}

				.uni-icons {
					font-size: 30rpx;
					color: #999;
				}

				.search-btn {
					padding: 0 25rpx;
					height: 60rpx;
					line-height: 60rpx;
					color: white;
					font-size: 28rpx;
					font-weight: 500;
					background: linear-gradient(135deg, #ff4444 0%, #ff6666 100%);
					border-radius: 30rpx;
					transition: all 0.3s ease;
					box-shadow: 0 2rpx 8rpx rgba(255, 68, 68, 0.3);
				}

				.search-btn:active {
					transform: scale(0.95);
				}

				.btntext {
					font-size: 28rpx;
					color: #333;
					font-weight: bold;
				}
			}

			.cart-icon {
				position: relative;
				margin-left: 20rpx;
				height: 100%;
				display: flex;
				align-items: center;
				width: 60rpx;
				height: 60rpx;
				justify-content: center;
				background-color: #f5f5f5;
				border-radius: 50%;
				transition: all 0.3s ease;

				&:hover {
					background-color: #e0e0e0;
				}

				.uni-icons {
					font-size: 32rpx;
					color: #333;
				}

				.cart-nums {
					position: absolute;
					top: -5rpx;
					right: -5rpx;
					width: 28rpx;
					height: 28rpx;
					background-color: #ff4444;
					color: white;
					font-size: 18rpx;
					font-weight: bold;
					border-radius: 50%;
					display: flex;
					justify-content: center;
					align-items: center;
					box-shadow: 0 2rpx 6rpx rgba(255, 68, 68, 0.3);
				}
			}
		}
	}

	.orders_cart_parent {
		width: 95%;
		height: 80rpx;
		margin: 15rpx auto;
		display: flex;
		justify-content: space-between;
		gap: 20rpx;

		.orders_cart_item {
			flex: 1;
			height: 100%;
			background: linear-gradient(135deg, #fff 0%, #f9f9f9 100%);
			border-radius: 20rpx;
			padding: 0 25rpx;
			box-sizing: border-box;
			display: flex;
			align-items: center;
			font-size: 26rpx;
			color: #333;
			font-weight: 500;
			transition: all 0.3s ease;
			box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
			border: 1rpx solid #f0f0f0;

			&:hover {
				transform: translateY(-2rpx);
				box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
			}

			uni-icons {
				margin-right: 15rpx;
				font-size: 32rpx;
				color: #ff4444;
			}

			.item_text {
				padding: 0 8rpx;
				&:last-child {
					color: #999;
					font-size: 24rpx;
					font-weight: normal;
				}
			}
		}
	}

	.bannerparent {
		width: 95%;
		height: 320rpx;
		margin: 20rpx auto;
		border-radius: 25rpx;
		overflow: hidden;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
		transition: all 0.3s ease;

		&:hover {
			box-shadow: 0 6rpx 20rpx rgba(0, 0, 0, 0.15);
		}

		.bannerimg {
			width: 100%;
			height: 100%;
			object-fit: cover;
		}
	}

	/* 商品推荐区域样式 */
	.goods-recommend {
		width: 95%;
		margin: 25rpx auto;
	}

	/* 调整商品列表的间距和样式 */
	.mygoodslist {
		padding: 0 10rpx;
	}

	.categorylist {
		width: 100%;
		height: 80rpx;
		// background-color: aqua;
		margin: 0;
		color: #333;
		font-size: 28rpx;
		font-weight: bold;
		/* 不换行*/
		white-space: nowrap;
		background-color: #fff;

		.categoryitem {
			/*行内块*/
			display: inline-block;
			padding: 0 25rpx;
			height: 80rpx;
			line-height: 80rpx;
		}

		.active {
			color: #ff4444;
		}

		.active::after {
			content: "";
			display: block;
			width: 40rpx;
			height: 4rpx;
			background-color: #ff4444;
			margin: -5rpx auto 0;
		}
	}
</style>