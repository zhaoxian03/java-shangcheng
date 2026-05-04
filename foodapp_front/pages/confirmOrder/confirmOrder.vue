<template>
	<!-- 顶部返回按钮 -->
	<view class="top-nav">
		<view class="nav-left" @click="goBack">
			<uni-icons type="left" size="28"></uni-icons>
		</view>
		<view class="nav-title">提交订单</view>
		<view class="nav-right"></view>
	</view>
	<view class="line"></view>
	<!-- 地址 -->
	<view class="address-parent" @click="openAddressModal">
		<view class="address-main">
			<view class="address-info">
				<view class="address-name-tel">
					<text class="address-name">{{address.receiveName || '请选择收货人'}}</text>
					<text class="address-tel">{{address.tel || '请选择手机号'}}</text>
				</view>
				<view class="address-detail">
					{{address.province || ''}}{{address.city || ''}}{{address.district || ''}}{{address.detailaddress || '请选择收货地址'}}
				</view>
			</view>
			<view class="forward">
				<uni-icons type="forward" size="20" color="#999"></uni-icons>
			</view>
		</view>
	</view>
	<!-- 地址 -->
	<view class="bgline"></view>
	<!-- 购买的商品列表 -->
	<view class="buygoodslist" v-for="(item,index) in curNewOrders">
		<view class="buygoodslist-shop">
			<text class="shop-name">{{item.shop.sname}}</text>
		</view>
		<view class="buygoodslist-curgoods" v-for="(ditem,index) in item.goodsDetail">
			<image :src="ditem.icon" class="curgoods-img"></image>
			<view class="buygoodslist-curgoods-content">
				<view class="curgoods-content-gname">
					{{ditem.gname}}
				</view>
				<view class="curgoods-content-text">
					{{ditem.bcontent}}
				</view>
				<view class="curgoods-content-price">
					<view>
						<text class="danwei">￥</text>
						<text class="price">{{ditem.buyPrice}}</text>
					</view>
					<view class="buy-nums">
						<text>x{{ditem.buyNums}}</text>
					</view>
				</view>
			</view>
		</view>
		<view class="buygoodslist-postprice">
			<text>配送</text>
			<text v-if="item.postprice==null || item.postprice==0 " class="buygoodslist-postprice-baoyou">快递 包邮</text>
			<text v-else>快递 运费￥{{item.postprice}}</text>
		</view>
		<view class="buygoodslist-liuyan">
			<text>备注</text>
			<view class="liuyan-content">
				<input
					type="text"
					v-model="item.beizhu"
					placeholder="请输入备注信息"
					class="remark-input"
				/>
			</view>
		</view>
	</view>
	<!-- 购买的商品列表 -->
	
	<!-- 价格明细 -->
	<view class="price-detail">
		<view class="detail-item">
			<text>商品总价</text>
			<text>￥{{totalPrice}}</text>
		</view>
		<view class="detail-item">
			<text>运费</text>
			<text>￥{{this.curNewOrders.reduce((sum, item) => sum + (item.postprice || 0), 0).toFixed(2)}}</text>
		</view>
		<view class="detail-item">
			<text>共减</text>
			<text class="discount-text">-￥{{discountsNum}}</text>
		</view>
	</view>
	<!-- 价格明细 -->
	
	<!-- 底部提交订单 -->
	<view class="footer">
		<view class="totalprice">
			<text>实付款：</text>
			<text class="final-price">￥{{parseFloat(totalPrice - discountsNum + this.curNewOrders.reduce((sum, item) => sum + (item.postprice || 0), 0)).toFixed(2)}}</text>
		</view>
		<view class="btn" @click="clickSubmitOrders">
			提交订单
		</view>
	</view>
	<!-- 底部提交订单 -->
	
	<!-- 地址选择弹窗 -->
	<view class="address-modal" v-if="showAddressModal">
		<!-- 遮罩层 -->
		<view class="modal-mask" @click="closeAddressModal"></view>
		
		<!-- 弹窗内容 -->
		<view class="modal-content">
			<!-- 弹窗头部 -->
			<view class="modal-header">
				<text class="modal-title">选择收货地址</text>
				<view class="modal-close" @click="closeAddressModal">
					<uni-icons type="close" size="24" color="#999"></uni-icons>
				</view>
			</view>
			
			<!-- 地址列表 -->
			<scroll-view class="modal-address-list" scroll-y>
				<view v-if="loadingAddresses" class="loading">加载中...</view>
				
				<view v-else-if="addressList.length === 0" class="empty-address">
					<text>暂无收货地址</text>
					<view class="add-address-btn" @click="goToAddAddress">
						<uni-icons type="plus" size="18" color="#4a90e2"></uni-icons>
						<text>添加地址</text>
					</view>
				</view>
				
				<view 
					class="modal-address-item" 
					v-for="item in addressList" 
					:key="item.id"
					@click="selectAddressFromModal(item)"
					:class="{ 'active': address.id === item.id }"
				>
					<view class="address-header">
						<text class="address-name">{{ item.name || item.receiveName }}</text>
						<text class="address-phone">{{ item.phone || item.tel }}</text>
						<text class="default-tag" v-if="item.isDefault === 1">默认</text>
					</view>
					<view class="address-detail">
						{{ item.province || '' }}{{ item.city || '' }}{{ item.district || '' }}{{ item.detail || item.detailAddress || '' }}
					</view>
				</view>
			</scroll-view>
			
			<!-- 底部添加按钮 -->
			<view class="modal-footer">
				<view class="add-address-btn" @click="goToAddAddress">
					<uni-icons type="plus" size="24" color="#4a90e2"></uni-icons>
					<text>添加新地址</text>
				</view>
			</view>
		</view>
	</view>
	<!-- 地址选择弹窗 -->
</template>

<script>
	import fDate from "@/common/utils/formatDateUtil.js"
	import { addressApi } from '../../api/address.js'
	const app = getApp({allowDefault: true})
	export default {
		data() {
				return {
					curNewOrders:[],
					address:{
						province:'',
						city:'',
						district:'',
						detailaddress:'',
						receiveName:'',
						tel:''
					},
					totalPrice:0,
					discountsNum:0,
					// 地址选择弹窗相关
					showAddressModal: false,
					addressList: [],
					loadingAddresses: false
				}
			},
		onLoad(options) {
			// console.log(app.globalData.neworders)
			if(app.globalData.neworders!=undefined && app.globalData.neworders!=null){
				this.curNewOrders = app.globalData.neworders
			}
			// 获取用户地址信息
			this.getUserAddress();
			// 计算所有的价格
			this.computerTotalPrice();
		},
		methods: {
			// 返回上一页
			goBack() {
				uni.navigateBack()
			},
			computerTotalPrice(){
				this.totalPrice = 0
				this.curNewOrders.forEach(item=>{
					this.totalPrice += parseFloat(item.buyTotalPrice) + item.postprice
				})
				this.totalPrice = this.totalPrice.toFixed(2)
			},
			getDecimal(price) {
				let att = (price + '').split('.')
				if (att.length > 1) {
					return '.' + att[1]
				} else {
					return ''
				}
			},
			// 获取用户地址信息
		async getUserAddress() {
			// 从本地存储或全局获取用户信息
			let userInfo = uni.getStorageSync('userInfo') || app.globalData.userInfo
			const userId = app.globalData.currentUserId || app.globalData.cid || ''
			
			if (userInfo && userId) {
				try {
					// 调用API获取默认地址
					const defaultAddress = await addressApi.getDefaultAddress(userId)
					if (defaultAddress) {
						// 打印默认地址数据，方便调试
						console.log('默认地址数据:', defaultAddress)
						// 使用API返回的默认地址
						this.address = {
							receiveName: defaultAddress.name || defaultAddress.receiveName || '',
							tel: defaultAddress.phone || defaultAddress.tel || '',
							province: defaultAddress.province || '',
							city: defaultAddress.city || '',
							district: defaultAddress.district || '',
							detailaddress: defaultAddress.detail || defaultAddress.detailAddress || defaultAddress.address || '',
							id: defaultAddress.id
						}
						console.log('格式化后的地址:', this.address)
					} else {
						// 如果没有默认地址，使用提示信息
						this.address = {
							receiveName: '',
							tel: '',
							province: '',
							city: '',
							district: '',
							detailaddress: '请选择收货地址'
						}
					}
				} catch (error) {
					console.error('获取默认地址失败:', error)
					// 出错时使用提示信息
					this.address = {
							receiveName: '',
							tel: '',
							province: '',
							city: '',
							district: '',
							detailaddress: '获取地址失败，请重新选择'
					}
				}
			} else {
				// 如果没有用户信息，使用默认值
				this.address = {
					receiveName: '',
					tel: '',
					province: '',
					city: '',
					district: '',
					detailaddress: '请先登录'
				}
			}
		},
		// 打开地址选择弹窗
		openAddressModal() {
			const userId = app.globalData.currentUserId || app.globalData.cid || ''
			if (!userId) {
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				})
				return
			}
			
			this.showAddressModal = true
			this.loadAddressList()
		},
		
		// 关闭地址选择弹窗
		closeAddressModal() {
			this.showAddressModal = false
		},
		
		// 加载地址列表
		async loadAddressList() {
			const userId = app.globalData.currentUserId || app.globalData.cid || ''
			if (!userId) return
			
			this.loadingAddresses = true
			try {
				const addresses = await addressApi.getList(userId)
				this.addressList = addresses || []
				console.log('加载地址列表成功:', this.addressList)
			} catch (error) {
				console.error('加载地址列表失败:', error)
				uni.showToast({
					title: '加载地址失败',
					icon: 'none'
				})
				this.addressList = []
			} finally {
				this.loadingAddresses = false
			}
		},
		
		// 从弹窗选择地址
		selectAddressFromModal(item) {
			console.log('从弹窗选择地址:', item)
			// 更新当前地址
			this.address = {
				receiveName: item.name || item.receiveName,
				tel: item.phone || item.tel,
				province: item.province || '',
				city: item.city || '',
				district: item.district || '',
				detailaddress: item.detail || item.detailAddress || '',
				id: item.id
			}
			// 关闭弹窗
			this.showAddressModal = false
		},
		
		// 跳转到添加地址页面
		goToAddAddress() {
			const userId = app.globalData.currentUserId || app.globalData.cid || ''
			if (!userId) {
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				})
				return
			}
			
			// 关闭弹窗
			this.showAddressModal = false
			
			// 跳转到添加地址页面
			uni.navigateTo({
				url: `/pages/address/edit?userId=${userId}&from=confirmOrder`,
				// 监听返回事件，刷新地址列表
				events: {
					addressAdded: () => {
						// 地址添加成功后，重新获取地址列表
						this.getUserAddress()
					}
				}
			})
		},
			clickSubmitOrders(){
				// 一个门店一个订单
				let orderlist = []
				this.curNewOrders.forEach(item=>{
					// 订单主信息
					let orders = {}
					orders = item
					orders.orderid = orders.cid + new Date().getTime()+parseInt((Math.random()*100)*(Math.random()*100));
					orders.ordertime = fDate.formatTime(new Date())
					orders.paytime = null
					orders.orderstatus = 0
					orders.orderstatusStr = '待支付'
					orders.buyTotalPrice = orders.buyTotalPrice - this.discountsNum
					
					// 添加用户信息
					let userInfo = uni.getStorageSync('userInfo') || app.globalData.userInfo
					if (userInfo) {
						orders.uid = app.globalData.currentUserId || app.globalData.cid
						orders.receiveName = this.address.receiveName
						orders.tel = this.address.tel
						orders.address = this.address.detailaddress
					}
					
					// 订单详细信息
					let ordersDetail = item.goodsDetail
					ordersDetail.forEach(ditem=>{
						ditem.orderid = item.orderid
						ditem.odid = 0
						// 添加收货地址信息到每个订单详细中
						ditem.receiveName = this.address.receiveName
						ditem.tel = this.address.tel
						ditem.province = this.address.province
						ditem.city = this.address.city
						ditem.district = this.address.district
						ditem.detailAddress = this.address.detailaddress
					})
					orders.ordersDetail = ordersDetail
					
					Reflect.deleteProperty(orders, 'goodsDetail')
					Reflect.deleteProperty(orders, 'checked')
					// 添加订单
					orderlist.push(orders)
				})
				
				// 远程提交订单。。。。
				const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
				uni.request({
					url: app.globalData.baseUrl + 'api/orders/addorders',
					method:"POST",
				data:orderlist,
				header: {
						'Authorization': token ? `Bearer ${token}` : ''
					},
				success: (res) => {
					console.log(res)
					// 转向订单页面
					uni.switchTab({
						url:'../orders/orders'
					})
				}
			})
			}
		}
	}
</script>

<style lang="scss">
	page {
		padding: 0;
		background-color: #f5f5f5;
	}

	.top-nav {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 88rpx;
		background-color: #fff;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 30rpx;
		box-sizing: border-box;
		z-index: 100;
		
		.nav-left {
			width: 60rpx;
			height: 88rpx;
			display: flex;
			align-items: center;
		}
		
		.nav-title {
			font-size: 36rpx;
			font-weight: bold;
			color: #333;
		}
		
		.nav-right {
			width: 60rpx;
		}
	}

	.line {
		position: fixed;
		top: 88rpx;
		left: 0;
		width: 100%;
		height: 2rpx;
		background-color: #f0f0f0;
		z-index: 100;
	}

	.address-parent {
		margin-top: 88rpx;
		width: 100%;
		padding: 20rpx 30rpx;
		box-sizing: border-box;
		background-color: #FFF;
		
		.address-main {
			display: flex;
			justify-content: space-between;
			align-items: flex-start;
		}
		
		.address-info {
			flex: 1;
		}
		
		.address-name-tel {
			margin-bottom: 10rpx;
			
			.address-name {
				font-size: 32rpx;
				font-weight: bold;
				color: #333;
				margin-right: 30rpx;
			}
			
			.address-tel {
				font-size: 30rpx;
				color: #666;
			}
		}
		
		.address-detail {
			font-size: 30rpx;
			color: #666;
			line-height: 44rpx;
			word-break: break-all;
		}
		
		.forward {
			margin-left: 20rpx;
			margin-top: 10rpx;
		}
	}

	.bgline {
		width: 100%;
		height: 20rpx;
		background-color: #F6F6F8;
	}

	.buygoodslist {
		width: 100%;
		margin-bottom: 20rpx;
		background-color: #FFF;
		
		.buygoodslist-shop {
			padding: 20rpx 30rpx;
			
			.shop-name {
				font-size: 30rpx;
				color: #333;
				font-weight: bold;
			}
		}
		
		.buygoodslist-curgoods {
			display: flex;
			padding: 0 30rpx 20rpx;
			
			.curgoods-img {
				width: 180rpx;
				height: 180rpx;
				border-radius: 10rpx;
				flex-shrink: 0;
				margin-right: 20rpx;
			}
			
			.buygoodslist-curgoods-content {
				flex: 1;
				display: flex;
				flex-direction: column;
				justify-content: space-between;
				height: 180rpx;
				
				.curgoods-content-gname {
					font-size: 32rpx;
					color: #333;
					line-height: 44rpx;
					display: -webkit-box;
					-webkit-box-orient: vertical;
					-webkit-line-clamp: 2;
					overflow: hidden;
				}
				
				.curgoods-content-text {
					font-size: 26rpx;
					color: #999;
					margin-bottom: 10rpx;
				}
				
				.curgoods-content-price {
					display: flex;
					justify-content: space-between;
					align-items: center;
					
					.danwei {
						font-size: 24rpx;
						color: #FF4444;
					}
					
					.price {
						font-size: 36rpx;
						color: #FF4444;
						font-weight: bold;
					}
					
					.buy-nums {
						font-size: 30rpx;
						color: #666;
					}
				}
			}
		}
		
		.buygoodslist-postprice {
			display: flex;
			justify-content: space-between;
			align-items: center;
			font-size: 28rpx;
			color: #666;
			padding: 20rpx 30rpx;
			border-top: 2rpx solid #f0f0f0;
			
			.buygoodslist-postprice-baoyou {
				color: #666;
			}
		}
		
		.buygoodslist-liuyan {
		display: flex;
		justify-content: space-between;
		align-items: center;
		font-size: 28rpx;
		color: #666;
		padding: 20rpx 30rpx;
		border-top: 2rpx solid #f0f0f0;
		
		.liuyan-content {
			flex: 1;
			margin-left: 30rpx;
		}
		
		.remark-input {
			width: 100%;
			height: 60rpx;
			font-size: 28rpx;
			color: #333;
			border: none;
			background-color: transparent;
			outline: none;
			text-align: right;
		}
		
		.remark-input::placeholder {
			color: #999;
		}
	}
	}

	.price-detail {
		width: 100%;
		background-color: #FFF;
		margin-bottom: 140rpx;
		
		.detail-item {
			display: flex;
			justify-content: space-between;
			align-items: center;
			font-size: 30rpx;
			padding: 20rpx 30rpx;
			
			text:nth-child(1) {
				color: #666;
			}
			
			text:nth-child(2) {
				color: #333;
			}
		}
		
		.discount-text {
			color: #FF4444 !important;
		}
	}

	.footer {
		width: 100%;
		height: 130rpx;
		background-color: #FFF;
		position: fixed;
		bottom: 0;
		left: 0;
		display: flex;
		justify-content: space-between;
		align-items: center;
		box-sizing: border-box;
		padding: 0 30rpx;
		border-top: 2rpx solid #f0f0f0;
		
		.totalprice {
			display: flex;
			align-items: baseline;
			
			text {
				color: #666;
				font-size: 30rpx;
				margin-right: 10rpx;
			}
			
			.final-price {
				color: #FF4444;
				font-size: 48rpx;
				font-weight: bold;
				margin-right: 0;
			}
		}
		
		.btn {
			width: 240rpx;
			height: 90rpx;
			background-color: #FF4444;
			color: #FFF;
			text-align: center;
			line-height: 90rpx;
			font-weight: bold;
			font-size: 32rpx;
			border-radius: 45rpx;
		}
	}
	
	/* 地址选择弹窗样式 */
	.address-modal {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		z-index: 1000;
	}
	
	.modal-mask {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background-color: rgba(0, 0, 0, 0.5);
	}
	
	.modal-content {
		position: absolute;
		bottom: 0;
		left: 0;
		width: 100%;
		max-height: 70vh;
		background-color: #FFF;
		border-top-left-radius: 30rpx;
		border-top-right-radius: 30rpx;
		padding-bottom: 20rpx;
		box-sizing: border-box;
	}
	
	.modal-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 30rpx;
		border-bottom: 1rpx solid #f0f0f0;
		height: 80rpx;
		box-sizing: border-box;
		
		.modal-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333;
		}
		
		.modal-close {
			width: 60rpx;
			height: 60rpx;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}
	
	.modal-address-list {
		height: calc(70vh - 80rpx - 120rpx);
		padding: 20rpx;
	}
	
	.modal-footer {
		padding: 20rpx;
		border-top: 1rpx solid #f0f0f0;
		height: 120rpx;
		box-sizing: border-box;
		position: relative;
		z-index: 10;
	}
	
	.loading {
		text-align: center;
		padding: 40rpx 0;
		color: #999;
		font-size: 28rpx;
	}
	
	.empty-address {
		text-align: center;
		padding: 60rpx 0;
		color: #999;
		font-size: 28rpx;
		
		.add-address-btn {
			margin-top: 30rpx;
			display: inline-flex;
			align-items: center;
			color: #4a90e2;
			font-size: 28rpx;
			
			uni-icons {
				margin-right: 8rpx;
			}
		}
	}
	
	.modal-address-item {
		background-color: #f8f8f8;
		border-radius: 16rpx;
		padding: 25rpx;
		margin-bottom: 20rpx;
		transition: all 0.3s ease;
		border: 2rpx solid transparent;
		box-sizing: border-box;
		
		&.active {
			border-color: #4a90e2;
			background-color: rgba(74, 144, 226, 0.05);
		}
		
		.address-header {
			display: flex;
			align-items: center;
			margin-bottom: 15rpx;
			flex-wrap: wrap;
			
			.address-name {
				font-size: 32rpx;
				font-weight: bold;
				color: #333;
				margin-right: 20rpx;
			}
			
			.address-phone {
				font-size: 28rpx;
				color: #666;
				margin-right: 20rpx;
				margin-bottom: 10rpx;
			}
			
			.default-tag {
				background-color: #4a90e2;
				color: #fff;
				font-size: 22rpx;
				padding: 4rpx 12rpx;
				border-radius: 15rpx;
				margin-bottom: 10rpx;
			}
		}
		
		.address-detail {
			font-size: 28rpx;
			color: #333;
			line-height: 1.5;
			word-break: break-all;
			white-space: normal;
			width: 100%;
			flex-wrap: wrap;
		}
	}
	
	/* 确保新增按钮在弹窗底部 */
	.add-address-btn {
		position: relative;
		z-index: 20;
		margin-bottom: 0;
	}
	
	.modal-footer {
		padding: 20rpx;
		border-top: 1rpx solid #f0f0f0;
	}
	
	.add-address-btn {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
		height: 80rpx;
		background-color: #fff;
		color: #4a90e2;
		border: 2rpx solid #4a90e2;
		border-radius: 40rpx;
		font-size: 30rpx;
		font-weight: 500;
		
		uni-icons {
			margin-right: 10rpx;
		}
	}
</style>
