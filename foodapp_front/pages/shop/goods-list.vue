<template>
	<view class="shop-goods-list">
		<!-- 页面头部 -->
		<view class="page-header">
			<view class="header-left" @click="goBack">
				<uni-icons type="back" size="24" color="#000"></uni-icons>
				<text class="header-title">{{ shopInfo.sname || '店铺商品' }}</text>
			</view>
			<view class="header-right">
				<uni-icons type="search" size="24" color="#000"></uni-icons>
			</view>
		</view>

		<!-- 店铺信息 -->
		<view class="shop-info-section">
			<view class="shop-basic-info">
				<image :src="shopInfo.slogo || '/static/uni.png'" class="shop-logo"></image>
				<view class="shop-info">
					<view class="shop-name">{{ shopInfo.sname || '店铺名称' }}</view>
					<view class="shop-rating">
						<uni-icons type="star" size="14" color="#FFD700"></uni-icons>
						<text>{{ shopInfo.overallExperience || 0 }}</text>
					</view>
				</view>
				<view class="follow-btn" :class="{ 'followed': isFollowed }" @click="toggleFollow">
					{{ isFollowed ? '已关注' : '+ 关注' }}
				</view>
			</view>
			<view class="shop-stats">
				<view class="stat-item">
					<text class="stat-value">{{ goodsCount || 0 }}</text>
					<text class="stat-label">商品</text>
				</view>
				<view class="stat-item">
					<text class="stat-value">{{ fansCount || 0 }}</text>
					<text class="stat-label">粉丝</text>
				</view>
				<view class="stat-item">
					<text class="stat-value">{{ shopInfo.openDays || 0 }}</text>
					<text class="stat-label">开店天数</text>
				</view>
			</view>
		</view>

		<!-- 商品列表 -->
		<view class="goods-list-container">
			<view class="goods-item" v-for="goods in goodsList" :key="goods.gid" @click="goToGoodsDetail(goods.gid)">
				<image :src="goods.icon.split(';')[0] || '/static/uni.png'" class="goods-image" @error="onImageError"></image>
				<view class="goods-info">
					<view class="goods-name">{{ goods.gname }}</view>
					<view class="goods-price">￥{{ goods.price }}</view>
					<view class="goods-sales">已售{{ goods.sellTotalNum }}件</view>
				</view>
			</view>
			<view class="empty-tip" v-if="goodsList.length === 0">
				<text>暂无商品</text>
			</view>
		</view>
	</view>
</template>

<script>
const app = getApp({ allowDefault: true })

export default {
	data() {
		return {
			shopId: '',
			shopInfo: {},
			goodsList: [],
			goodsCount: 0,
			fansCount: 0,
			isFollowed: false,
			page: 1,
			pageSize: 20
		}
	},
	onLoad(options) {
		// 获取店铺ID
		this.shopId = options.shopId || options.sid
		if (this.shopId) {
			this.loadShopInfo()
			this.loadGoodsList()
			this.loadFansInfo()
			this.checkFollowStatus()
		}
	},
	methods: {
		// 返回上一页
		goBack() {
			uni.navigateBack({
				delta: 1
			})
		},
		// 加载店铺信息
		loadShopInfo() {
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token')
			uni.request({
				url: app.globalData.baseUrl + 'api/seller/shop/' + this.shopId,
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				success: (res) => {
					if (res.data.code == 200) {
						this.shopInfo = res.data.data
						// 计算开店天数
						this.calculateOpenDays()
					}
				}
			})
		},
		// 计算开店天数
		calculateOpenDays() {
			if (!this.shopInfo.ontime) {
				this.shopInfo.openDays = 0
				return
			}
			const now = new Date()
			const ontime = new Date(this.shopInfo.ontime)
			const diffTime = now - ontime
			this.shopInfo.openDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
		},
		// 加载商品列表
		loadGoodsList() {
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token')
			uni.request({
				url: app.globalData.baseUrl + 'api/goods/findbycgid',
				method: 'GET',
				data: {
					sid: this.shopId,
					page: this.page,
					pageSize: this.pageSize
				},
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				success: (res) => {
					if (res.data.code == 200) {
						this.goodsList = res.data.data
						this.goodsCount = res.data.data.length
					}
				}
			})
		},
		// 加载粉丝信息
		loadFansInfo() {
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token')
			uni.request({
				url: app.globalData.baseUrl + 'api/shop/fans/count/' + this.shopId,
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				success: (res) => {
					if (res.data.code == 200) {
						this.fansCount = res.data.data
					}
				}
			})
		},
		// 检查关注状态
		checkFollowStatus() {
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token')
			if (!token) {
				this.isFollowed = false
				return
			}
			uni.request({
				url: app.globalData.baseUrl + 'api/shop/fans/check/' + this.shopId,
				header: {
					'Authorization': `Bearer ${token}`
				},
				success: (res) => {
					if (res.data.code == 200) {
						this.isFollowed = res.data.data
					}
				}
			})
		},
		// 切换关注状态
		toggleFollow() {
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token')
			if (!token) {
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				})
				return
			}
			
			const url = this.isFollowed ? 
				app.globalData.baseUrl + 'api/shop/fans/unfollow/' + this.shopId : 
				app.globalData.baseUrl + 'api/shop/fans/follow/' + this.shopId
			
			uni.request({
				url: url,
				method: 'POST',
				header: {
					'Authorization': `Bearer ${token}`
				},
				success: (res) => {
					if (res.data.code == 200) {
						this.isFollowed = !this.isFollowed
						// 更新粉丝数量
						this.fansCount += this.isFollowed ? 1 : -1
						uni.showToast({
							title: this.isFollowed ? '关注成功' : '取消关注成功',
							icon: 'success'
						})
					} else {
						uni.showToast({
							title: res.data.msg || '操作失败',
							icon: 'none'
						})
					}
				}
			})
		},
		// 跳转到商品详情
		goToGoodsDetail(gid) {
			uni.navigateTo({
				url: '../goods/goods?gid=' + gid
			})
		},
		// 图片加载失败处理
		onImageError(e) {
			e.target.src = '/static/uni.png'
		}
	}
}
</script>

<style scoped>
.shop-goods-list {
	padding-bottom: 20rpx;
}

/* 页面头部 */
.page-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 20rpx 30rpx;
	background-color: #fff;
	box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.1);
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	z-index: 100;
}

.header-left {
	display: flex;
	align-items: center;
	gap: 20rpx;
}

.header-title {
	font-size: 32rpx;
	font-weight: bold;
	color: #333;
}

.header-right {
	padding: 10rpx;
}

/* 店铺信息区域 */
.shop-info-section {
	margin-top: 90rpx;
	background-color: #fff;
	padding: 30rpx;
	margin-bottom: 20rpx;
}

.shop-basic-info {
	display: flex;
	align-items: center;
	margin-bottom: 30rpx;
}

.shop-logo {
	width: 100rpx;
	height: 100rpx;
	border-radius: 50%;
	margin-right: 20rpx;
}

.shop-info {
	flex: 1;
}

.shop-name {
	font-size: 32rpx;
	font-weight: bold;
	color: #333;
	margin-bottom: 10rpx;
}

.shop-rating {
	display: flex;
	align-items: center;
	gap: 5rpx;
	font-size: 24rpx;
	color: #666;
}

.follow-btn {
	padding: 15rpx 30rpx;
	border: 2rpx solid #ff4757;
	border-radius: 40rpx;
	font-size: 24rpx;
	color: #ff4757;
	background-color: #fff;
}

.follow-btn.followed {
	background-color: #ff4757;
	color: #fff;
}

.shop-stats {
	display: flex;
	justify-content: space-around;
	padding-top: 20rpx;
	border-top: 1rpx solid #eee;
}

.stat-item {
	display: flex;
	flex-direction: column;
	align-items: center;
}

.stat-value {
	font-size: 36rpx;
	font-weight: bold;
	color: #333;
	margin-bottom: 5rpx;
}

.stat-label {
	font-size: 24rpx;
	color: #666;
}

/* 商品列表 */
.goods-list-container {
	padding: 0 20rpx;
}

.goods-item {
	background-color: #fff;
	border-radius: 20rpx;
	margin-bottom: 20rpx;
	overflow: hidden;
	box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.goods-image {
	width: 100%;
	height: 400rpx;
	object-fit: cover;
}

.goods-info {
	padding: 20rpx;
}

.goods-name {
	font-size: 28rpx;
	color: #333;
	margin-bottom: 15rpx;
	line-height: 1.4;
	display: -webkit-box;
	-webkit-line-clamp: 2;
	-webkit-box-orient: vertical;
	overflow: hidden;
}

.goods-price {
	font-size: 32rpx;
	font-weight: bold;
	color: #ff4757;
	margin-bottom: 10rpx;
}

.goods-sales {
	font-size: 24rpx;
	color: #999;
}

/* 空状态 */
.empty-tip {
	text-align: center;
	padding: 100rpx 0;
	color: #999;
	font-size: 28rpx;
	background-color: #fff;
	border-radius: 20rpx;
	margin: 20rpx;
}
</style>