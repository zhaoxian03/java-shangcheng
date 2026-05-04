<template>
	<view>
		<!-- 返回箭头 -->
		<view class="back-arrow" @click="goBack">
			<uni-icons type="back" size="28" color="#fff"></uni-icons>
		</view>
		<swiper class="goodsswiper" autoplay="true">
			<swiper-item v-for="(item,index) in curgoods.icons">
				<image :src="item" @error="onImageError($event, curgoods.icons, index)"></image>
			</swiper-item>
		</swiper>
		
		<view class="container">
			<!-- 商品价格 -->
			<view class="goods-price-name container-moudle">
				<view class="goods-price">
					<view class="goods-price-parent">
						<view class="danwei">￥</view>
						<view class="my-price">{{curgoods.price}}</view>
						<view class="danwei">起</view>
					</view>
					<view class="my-selltotalnum">已售{{curgoods.sellTotalNum}}件</view>
				</view>
				<view class="goods-title">
					{{curgoods.gname}}
				</view>
			</view>
			<!-- 商品价格 -->
			<!-- 商品选择 -->
			<view class="goods-select container-moudle">
				<view class="goods-select-p" @click="openSafe(curgoods.cgid)">
					<view class="goods-select-type">
						保障
					</view>
					<view class="goods-select-title">
						<text v-for="(safeitem,index) in curgoods.safeguardList">
							<text v-if="index<3">
								{{safeitem.sgname}}
							</text>
							<text v-if="index<2">•</text>
						</text>
					</view>
					<view class="showArrow">
						<uni-icons type="forward" color="#AAA" size="16"></uni-icons>
					</view>
				</view>
				<view class="goods-select-p" @click="openBuyGoods">
					<view class="goods-select-type">
						选择
					</view>
					<view class="goods-select-title">
						<view class="goods-select-left">
							<!-- 商品缩略图 -->
							<image v-if="curgoods.selectType==2" :src="item.gdpic"
								v-for="(item,index) in curgoods.goodsDetailList"
								class="goods-select-left-pics-item" @error="onGoodsDetailImageError($event, item)"></image>
							<!-- 商品缩略图 -->
							<!-- 商品标题 -->
							<view v-if="curgoods.selectType==1" class="goods-select-left-gname"
								v-for="(item,index) in curgoods.goodsDetailList">
								{{item.attributeValue1}}
							</view>
							<!-- 商品标题 -->
						</view>
						<view class="goods-select-text">
							{{curgoods.selectTypeText}}
						</view>
					</view>
					<view class="showArrow">
						<uni-icons type="forward" color="#AAA" size="16"></uni-icons>
					</view>
				</view>
				<view class="goods-select-p">
					<view class="goods-select-type">
						活动
					</view>
					<view class="goods-select-title">
						抖音月付 先领劵再下单！{{curgoods.discounts}}
					</view>
					<view class="showArrow">
						<uni-icons type="forward" color="#AAA" size="16"></uni-icons>
					</view>
				</view>
				<view class="goods-select-p">
					<view class="goods-select-type">
						物流
					</view>
					<view class="goods-select-title">
						现货 48小时内发货，
						<text v-if="curgoods.postprice<=0">
							包邮
						</text>
						<text v-else>邮费:￥{{curgoods.postprice}}</text>
						
					</view>
				</view>
			</view>
			<!-- 商品选择 -->

			<!-- 商品评价 -->
			<view class="container-moudle">
				<Evaluate :evaluateCount="curgoods.evaluateCount" :evaluateList="curgoods.evaluateList" :goodsId="curgoods.gid"></Evaluate>
			</view>
			<!-- 商品评价 -->

			<!-- 店铺信息 -->
			<view class="shops container-moudle">
				<view class="shop-header" @click="goToShop">
					<image :src="shopInfo.slogo || '/static/uni.png'" class="shop-logo"></image>
					<view class="shop-info">
						<view class="shop-name">{{ shopInfo.sname || '店铺名称' }}</view>
						<view class="shop-rating">
							<uni-icons type="star" size="16" color="#FFD700"></uni-icons>
							<text>{{ shopInfo.overallExperience || 0 }}</text>
							<text class="shop-sales">销量 {{ shopInfo.sales || 0 }}</text>
						</view>
					</view>
					<view class="shop-actions">
						<view class="follow-btn" :class="{ 'followed': isFollowed }" @click.stop="toggleFollow">
							{{ isFollowed ? '已关注' : '+ 关注' }}
						</view>
						<uni-icons type="right" size="18" color="#999"></uni-icons>
					</view>
				</view>
				<view class="shop-stats">
					<view class="stat-item">
						<text class="stat-value">{{ shopInfo.goodsCount || 0 }}</text>
						<text class="stat-label">商品</text>
					</view>
					<view class="stat-item">
						<text class="stat-value">{{ shopInfo.fansCount || 0 }}</text>
						<text class="stat-label">粉丝</text>
					</view>
					<view class="stat-item">
						<text class="stat-value">{{ shopInfo.openDays || 0 }}</text>
						<text class="stat-label">开店天数</text>
					</view>
				</view>
			</view>
			<!-- 店铺信息 -->

			<view class="curgoodsinfo container-moudle">
				<view class="module-title">商品详情</view>
				<view class="goods-detail">
					<image v-for="(icon, index) in curgoods.icons" :key="index" :src="icon" class="detail-img"></image>
					<view class="detail-content">
						<text class="detail-title">{{ curgoods.gname }}</text>
						<view class="detail-desc">
							<view class="desc-item">
								<text class="desc-label">价格：</text>
								<text class="desc-value price">￥{{ curgoods.price }}</text>
							</view>
							<view class="desc-item">
								<text class="desc-label">销量：</text>
								<text class="desc-value">{{ curgoods.sellTotalNum }}件</text>
							</view>
							<view class="desc-item">
								<text class="desc-label">库存：</text>
								<text class="desc-value">{{ curgoods.stockNum }}件</text>
							</view>
							<view class="desc-item">
								<text class="desc-label">配送：</text>
								<text class="desc-value">
									{{ curgoods.postprice <= 0 ? '包邮' : '邮费:￥' + curgoods.postprice }}
								</text>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<view style="width: 50px;height: 60px;"></view>
		
		<!-- 底部导航 -->
		<view class="footer">
			<view class="nav-item" @click="openHome">
				<uni-icons type="shop" size="28"></uni-icons>
				<text>主页</text>
			</view>
			<view class="nav-item" @click="openCart">
				<uni-icons type="cart" size="28"></uni-icons>
				<text>购物车</text>
				<view class="cart-num" v-if="cartAllTotalNums>0">{{cartAllTotalNums}}</view>
			</view>
			<view class="nav-item" @click="collectGoods">
				<uni-icons type="heart" size="28" :color="isCollected ? '#ff4757' : '#777'"></uni-icons>
				<text :style="{color: isCollected ? '#ff4757' : '#777'}">收藏</text>
			</view>
			
			<view class="nav-addcart">
					<view class="nav-addcart-btn" @click="openBuyGoods">加入购物车</view>
				</view>
		</view>
		<!-- 底部导航 -->

		<!-- 底部弹出的保障信息窗口 -->
		<uni-popup ref="safePopup" type="bottom">
			<Safeguard :safeList="curgoods.safeguardList" @colseSafePopup="colseSafePopupMethod"></Safeguard>
		</uni-popup>
		<!-- 底部弹出的保障信息窗口 -->
		
		<!-- 底部弹出选择购买商品的详细信息 -->
		<uni-popup ref="buyGoodsPopup" type="bottom">
			<BuyGoodsInfo :curgoods="curgoods" :curcartlist="cartlist" @colsebuyGoodsPopup="colsebuyGoodsPopupMethod"></BuyGoodsInfo>
		</uni-popup>
		<!-- 底部弹出选择购买商品的详细信息 -->
	</view>
</template>

<script>
import Safeguard from '@/components/Safeguard/Safeguard.vue'
import Evaluate from '@/components/Evaluate/Evaluate.vue'
import BuyGoodsInfo from '@/components/BuyGoodsInfo/BuyGoodsInfo.vue'
import { collectApi } from '@/api/collect.js'
const app = getApp({allowDefault: true})

export default {
	components: {
		Safeguard,
		Evaluate,
		BuyGoodsInfo
	},
	data() {
			return {
				curgoods: {
					gid: 0,
					gname: '',
					icons: [],
					price: 0,
					sellTotalNum: 0,
					selectType: 1,
					selectTypeText: '',
					attributeName1: '',
					attributeValue1: '',
					attributeName2: '',
					attributeValue2: '',
					attributeName3: '',
					attributeValue3: '',
					attributeName4: '',
					attributeValue4: '',
					attributeName5: '',
					attributeValue5: '',
					stockNum: 0,
					originalPrice: 0,
					specialPrice: 0,
					gdpic: '',
					postprice: 0,
					discounts: '',
					safeguardList: [],
					evaluateCount: 0,
					evaluateList: [],
					goodsDetailList: [],
					shopId: 0 // 新增店铺ID字段
				},
				cartlist: [],
				cartAllTotalNums: app.globalData.cartAllTotalNums || 0,
				// 保障信息弹窗
				safeShow: false,
				// 购买商品弹窗
				buyGoodsShow: false,
				// 商品评价弹窗
				evaluateShow: false,
				// 收藏状态
				isCollected: false,
				// 新增店铺相关数据
				shopInfo: {},
				recommendGoods: [],
				goodsDetail: {},
			// 店铺关注状态
			isFollowed: false
			}
		},
	onLoad(options) {
			console.log(options)
			// 获得当前商品信息
			if (app.globalData.curGoods.gid != undefined && app.globalData.curGoods.gid != null) {
				this.loadCurGoods(app.globalData.curGoods.gid)
			} else {
				// 同时检查id和gid参数，兼容不同的调用方式
				const goodsId = options.gid || options.id
				this.loadCurGoods(goodsId)
			}
			this.loadCart()
			// 检查收藏状态
			this.checkCollectStatus()
		},
	methods: {
		loadCart() {
			// 加载购物车的数据
			// this.cartlist = app.globalData.cartList
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
		// 加载当前商品的所有数据
		loadCurGoods(gid) {
			/**
			 * 实现思路
			 * 1.获得当前商品id
			 * 2.加载当前商品信息
			 * 3.加载当前商品相关的信息
			 */
			// 检查gid是否有效，并转换为数字类型
			if (!gid || gid === 'undefined' || gid === undefined) {
				console.error('无效的商品ID:', gid);
				return;
			}
			
			// 转换为数字类型，确保API调用正确
			const goodsId = Number(gid);
			console.log('加载商品详情，ID:', goodsId);
			
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			//2.加载当前商品信息
			uni.request({
				url: app.globalData.baseUrl + 'api/goods/getcurgoodsinfo/' + goodsId,
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				success: (res) => {
					console.log('商品详情数据:', res.data);
					if (res.data.code == 200) {
						this.curgoods = res.data.data
						app.globalData.curGoods = this.curgoods
						this.curgoods.icons = this.curgoods.icon.split(";").map(url => url.trim())
						console.log('加载店铺信息，店铺ID:', this.curgoods.sid);
						
						// 转换评价数据格式，使其与Evaluate组件兼容
						if (this.curgoods.evaluateList && this.curgoods.evaluateList.length > 0) {
							this.curgoods.evaluateList = this.curgoods.evaluateList.map(item => {
								return {
									douyinCode: '**' + Math.floor(Math.random() * 100), // 生成随机用户代码
									hearderimg: '', // 头像，这里可以设置默认头像
									isBackCustomer: item.etype && item.etype.includes('1'), // 1表示回头客
									econtent: item.content || '好评', // 评价内容
									epics: item.pic ? item.pic.split(';').filter(url => url.trim()) : [] // 评价图片
								}
							})
						}
						
						// 3.加载当前商品相关的信息
						this.loadShopInfo(this.curgoods.sid)
						this.loadRecommendGoods(this.curgoods.sid)
					}
				},
				fail: (err) => {
					console.error('获取商品详情失败:', err);
				}
			})
		},
		// 加载店铺信息
		loadShopInfo(shopId) {
			if (!shopId) {
				console.error('无效的店铺ID:', shopId);
				return;
			}
			
			console.log('加载店铺信息，ID:', shopId);
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			
			// 获取店铺基本信息
			uni.request({
				// 使用正确的店铺API路径
				url: app.globalData.baseUrl + 'api/seller/shop/' + shopId,
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				success: (res) => {
					console.log('店铺信息数据:', res.data);
					if (res.data.code == 200) {
						this.shopInfo = res.data.data
						console.log('店铺信息:', this.shopInfo);
						// 计算开店天数
						this.calculateOpenDays()
						// 获取商品数量
						this.getGoodsCount(shopId)
						// 获取粉丝数量
						this.getFansCount(shopId)
						// 检查关注状态
						this.checkFollowStatus()
					}
				},
				fail: (err) => {
					console.error('获取店铺信息失败:', err);
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
		// 获取商品数量
		getGoodsCount(shopId) {
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			uni.request({
				url: app.globalData.baseUrl + 'api/seller/stats?sid=' + shopId,
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				success: (res) => {
					if (res.data.code == 200) {
						this.shopInfo.goodsCount = res.data.data.goodsCount || 0
					}
				},
				fail: (err) => {
					console.error('获取商品数量失败:', err);
					this.shopInfo.goodsCount = 0
				}
			})
		},
		// 获取粉丝数量
		getFansCount(shopId) {
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			uni.request({
				url: app.globalData.baseUrl + 'api/shop/fans/count/' + shopId,
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				success: (res) => {
					if (res.data.code == 200) {
						this.shopInfo.fansCount = res.data.data || 0
					}
				},
				fail: (err) => {
					console.error('获取粉丝数量失败:', err);
					this.shopInfo.fansCount = 0
				}
			})
		},
		// 检查关注状态
		checkFollowStatus() {
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			if (!token || !this.shopInfo.sid) {
				this.isFollowed = false
				return
			}
			
			uni.request({
				url: app.globalData.baseUrl + 'api/shop/fans/check/' + this.shopInfo.sid,
				header: {
					'Authorization': `Bearer ${token}`
				},
				success: (res) => {
					if (res.data.code == 200) {
						this.isFollowed = res.data.data || false
					}
				},
				fail: (err) => {
					console.error('检查关注状态失败:', err);
					this.isFollowed = false
				}
			})
		},
		// 加载推荐商品
		loadRecommendGoods(shopId) {
			// 由于后端没有推荐商品API，直接设置为空数组
			this.recommendGoods = [];
		},
		openHome() {
			uni.switchTab({
				url: "../index/index"
			})
		},
		// 返回上一页
		goBack() {
			uni.navigateBack({
				delta: 1
			})
		},
		openCart() {
			uni.switchTab({
				url: "../cart/cart?cartAllTotalNums=" + this.cartAllTotalNums
			})
		},
		// 跳转到店铺商品列表页面
		goToShop() {
			if (this.shopInfo.sid) {
				uni.navigateTo({
					url: `../shop/goods-list?shopId=${this.shopInfo.sid}`
				})
			}
		},
		// 切换关注店铺状态
		toggleFollow() {
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			if (!token) {
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				})
				return
			}
			
			const url = this.isFollowed ? 
				app.globalData.baseUrl + 'api/shop/fans/unfollow/' + this.shopInfo.sid : 
				app.globalData.baseUrl + 'api/shop/fans/follow/' + this.shopInfo.sid
			
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
						this.shopInfo.fansCount += this.isFollowed ? 1 : -1
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
		goToGoods(gid) {
			uni.navigateTo({
				url: `../goods/goods?gid=${gid}`
			})
		},
		// 轮播图图片加载失败处理
		onImageError(e, iconsArray, index) {
			// 设置默认图片路径
			const defaultImage = '/static/goodsbanner01.png';
			// 更新当前索引的图片为默认图片
			if (iconsArray && iconsArray[index]) {
				iconsArray[index] = defaultImage;
			}
			// 防止重复触发错误
			const imgElement = e.target;
			if (imgElement) {
				imgElement.onerror = null;
			}
		},
		// 商品详情图片加载失败处理
		onGoodsDetailImageError(e, item) {
			// 设置默认图片路径
			const defaultImage = '/static/goodsbanner01.png';
			// 更新当前商品详情的图片为默认图片
			if (item && item.gdpic) {
				item.gdpic = defaultImage;
			}
			// 防止重复触发错误
			const imgElement = e.target;
			if (imgElement) {
				imgElement.onerror = null;
			}
		},
		openSafe(cgid) {
			this.$refs.safePopup.open('bottom')
		},
		openBuyGoods() {
			this.$refs.buyGoodsPopup.open('bottom')
		},
		
		colsebuyGoodsPopupMethod(data) {
				if (data != undefined)
					this.cartAllTotalNums = data
					
				this.$refs.buyGoodsPopup.close()
			},
			colseSafePopupMethod() {
				this.$refs.safePopup.close()
			},
			// 检查收藏状态
			checkCollectStatus() {
				const userId = uni.getStorageSync('userId') || app.globalData.userId || ''
				if (!userId) {
					this.isCollected = false
					return
				}
				
				// 检查商品是否已收藏
				collectApi.checkGoods(userId, this.curgoods.gid).then(res => {
					if (res.code === 200) {
						this.isCollected = res.data
					}
				}).catch(err => {
					console.error('检查收藏状态失败:', err)
					this.isCollected = false
				})
			},
			// 收藏商品
			collectGoods() {
				const userId = uni.getStorageSync('userId') || app.globalData.userId || ''
				if (!userId) {
					uni.showToast({
						title: '请先登录',
						icon: 'none'
					})
					return
				}
				
				if (this.isCollected) {
					// 取消收藏
					collectApi.cancelGoods(userId, this.curgoods.gid).then(res => {
						if (res.code === 200) {
							this.isCollected = false
							uni.showToast({
								title: '取消收藏成功',
								icon: 'success'
							})
						} else {
							uni.showToast({
								title: res.msg || '取消收藏失败',
								icon: 'none'
							})
						}
					}).catch(err => {
						console.error('取消收藏失败:', err)
						uni.showToast({
							title: '取消收藏失败',
							icon: 'none'
						})
					})
				} else {
					// 添加收藏
					const collectData = {
						userId: userId,
						goodsId: this.curgoods.gid,
						goodsName: this.curgoods.gname,
						goodsImage: this.curgoods.icons[0] || '',
						goodsPrice: this.curgoods.price
					}
					
					collectApi.addGoods(collectData).then(res => {
						if (res.code === 200) {
							this.isCollected = true
							uni.showToast({
								title: '收藏成功',
								icon: 'success'
							})
						} else {
							uni.showToast({
								title: res.msg || '收藏失败',
								icon: 'none'
							})
						}
					}).catch(err => {
						console.error('收藏失败:', err)
						uni.showToast({
							title: '收藏失败',
							icon: 'none'
						})
					})
				}
			}
		}
	}
</script>

<style lang="scss">
	page{
		padding: 0;
	}
	/* 返回箭头样式 */
	.back-arrow {
		position: fixed;
		top: 20rpx;
		left: 20rpx;
		width: 80rpx;
		height: 80rpx;
		background-color: rgba(0, 0, 0, 0.5);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		z-index: 9999;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.2);
	}

	.goodsswiper {
		width: 100%;
		height: 500rpx;

		image {
			width: 100%;
			height: 100%;
		}
	}

	.container {
		padding: 0 20rpx;

		.container-moudle {
			width: 100%;
			background-color: #FFF;
			margin: 10px auto;
			padding: 20rpx;
			box-sizing: border-box;
			border-radius: 15rpx;
		}

		.goods-price-name {
			height: 180rpx;

			.goods-price {
				height: 50rpx;
				display: flex;
				color: #EE425A;
				font-size: 24rpx;
				font-weight: 500;
				margin: 5rpx auto;
				line-height: 50rpx;
				justify-content: space-between;

				.goods-price-parent {
					display: flex;

					.danwei {
						transform: scale(0.9);
						line-height: 64rpx;
					}

					.my-price {
						font-size: 38rpx;
						font-weight: bold;
						padding: 0 3rpx;
					}
				}

				.my-selltotalnum {
					transform: scale(0.8);
					color: #999;
					padding-left: 4rpx;
					letter-spacing: 2rpx;
					line-height: 64rpx;
				}

			}

			.goods-title {
				font-size: 28rpx;
				font-weight: bold;
				color: #555;
			}
		}

		.showArrow {
			width: 50rpx;
			text-align: right;
		}

		.goods-select {
			height: 300rpx;

			.goods-select-p {
				display: flex;
				justify-content: space-between;
				margin-bottom: 35rpx;

				.goods-select-type {
					width: 70rpx;
				}

				.goods-select-title {
					flex-grow: 1;
					color: #333;
					position: relative;

					.goods-select-left {
						position: absolute;
						top: -7rpx;
						left: 0;
						width: 100%;
						height: 50rpx;
						display: flex;
						overflow: hidden;
						z-index: 9;

						.goods-select-left-pics-item {
							max-width: 90rpx;
							height: 50rpx;
							border-radius: 10rpx;
							margin-right: 8rpx;
						}

						.goods-select-left-gname {
							height: 50rpx;
							background-color: #E8E9EE;
							border-radius: 10rpx;
							line-height: 50rpx;
							white-space: nowrap;
							margin-right: 8rpx;
						}
					}

					.goods-select-text {
						width: 100%;
						height: 50rpx;
						position: absolute;
						top: -7rpx;
						right: 0;
						z-index: 10;
						background-image: linear-gradient(to right, transparent 10%, #FFF 90%);
						text-align: right;
						line-height: 50rpx;
					}
				}
			}
		}

		/* 店铺信息样式 */
	.shops {
		background-color: #FFF;
		padding: 20rpx;
		
		.shop-header {
			display: flex;
			align-items: center;
			margin-bottom: 20rpx;
			cursor: pointer;
			
			.shop-logo {
				width: 80rpx;
				height: 80rpx;
				border-radius: 50%;
				margin-right: 15rpx;
				border: 1px solid #eee;
			}
			
			.shop-info {
				flex: 1;
				
				.shop-name {
					font-size: 32rpx;
					font-weight: bold;
					margin-bottom: 5rpx;
				}
				
				.shop-rating {
					display: flex;
					align-items: center;
					font-size: 24rpx;
					color: #666;
					
					.shop-sales {
						margin-left: 15rpx;
					}
				}
			}
		}
		
		.shop-stats {
			display: flex;
			justify-content: space-around;
			padding-top: 20rpx;
			border-top: 1px solid #eee;
			
			.stat-item {
				display: flex;
				flex-direction: column;
				align-items: center;
				
				.stat-value {
					font-size: 32rpx;
					font-weight: bold;
					color: #EE425A;
					margin-bottom: 5rpx;
				}
				
				.stat-label {
					font-size: 24rpx;
					color: #999;
				}
			}
		}
	}

	/* 商品信息样式 */
	.curgoodsinfo {
		background-color: #FFF;
		padding: 20rpx;
		
		.module-title {
			font-size: 32rpx;
			font-weight: bold;
			margin-bottom: 20rpx;
		}
		
		.goods-detail {
			
			.detail-img {
				width: 100%;
				margin-bottom: 20rpx;
				border-radius: 10rpx;
			}
			
			.detail-content {
				.detail-title {
					font-size: 30rpx;
					font-weight: bold;
					margin-bottom: 15rpx;
					display: block;
				}
				
				.detail-desc {
					background-color: #f9f9f9;
					padding: 20rpx;
					border-radius: 10rpx;
					
					.desc-item {
						display: flex;
						margin-bottom: 10rpx;
						
						.desc-label {
							width: 80rpx;
							color: #999;
							font-size: 26rpx;
						}
						
						.desc-value {
							flex: 1;
							font-size: 26rpx;
							color: #333;
							
							&.price {
								color: #EE425A;
								font-weight: bold;
							}
						}
					}
				}
			}
		}
	}
	}

	.footer {
		width: 100%;
		height: 50px;
		background-color: #FFF;
		position: fixed;
		bottom: 0;
		left: 0;
		border-top: 1px solid #E8E9EE;
		display: flex;
		justify-content: space-around;
		align-items: center;
		font-size: 26rpx;
		color: #777;

		.nav-item {
			width: 110rpx;
			height: 50px;
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			line-height: 20px;
			position: relative;

			.cart-num {
				padding: 1rpx;
				border-radius: 14rpx;
				background-color: #EE425A;
				color: #FFF;
				position: absolute;
				top: 0;
				right: 14rpx;
				text-align: center;
				line-height: 28rpx;
			}
		}

		.nav-addcart {
			flex-grow: 1;
			display: flex;
			justify-content: center;
			font-weight: bold;
			text-align: center;
			line-height: 40px;
			font-size: 30rpx;

			.nav-addcart-btn {
				width: 100%;
				height: 40px;
				border-radius: 20rpx;
				background-color: #FE4A6D;
				color: #FFF;
			}
		}
	}
</style>