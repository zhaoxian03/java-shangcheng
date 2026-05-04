<template>
	<!-- 头部 -->
	<view class="header">
		<view style="height: 70rpx;width: 50%;"></view>
		<view class="nav-1">
			<view class="back" @click="back">
				<uni-icons type="back" size="26"></uni-icons>
			</view>
			<view class="title">购物车</view>
			<view class="management">
				<text v-if="isManagement==false" @click="clickmanagement">管理</text>
				<text v-if="isManagement" @click="clickcompete">完成</text>
			</view>
		</view>
		<view class="nav-2">
			<view class="nav-2-title" :class="selectNavIndex==1?'active':''" @click="clickNav(1)">全部
				<text v-if="cartAllTotalNums>0">{{cartAllTotalNums}}</text>
			</view>
			<view class="nav-2-title" :class="selectNavIndex==2?'active':''" @click="clickNav(2)">降价</view>
		</view>
	</view>
	<!-- 头部 -->

	<view class="container">
		<!-- 购物车列表 -->
		<view class="vcartlist" v-for="(item,index) in cartlist" :key="item.cartid">
			<view class="vcartlist-title">
				<checkbox-group :id="'cart'+item.cartid" @change="ckallshop">
					<checkbox :value="item.cartid+''" :checked="item.checked"></checkbox>
				</checkbox-group>
				<text class="shopname">{{item.shop.sname}}</text>
				<uni-icons type="forward" color="#999"></uni-icons>
				<text style="flex-grow: 1;text-align: right;">领劵</text>
			</view>
			<checkbox-group @change="ckchild($event,item)">
				<view class="vcartlist-goods" v-for="(gitem,gindex) in item.goodsDetail" :key="gitem.gdid">
					<checkbox :value="gitem.gdid+''" :checked="gitem.checked"></checkbox>
					<image :src="gitem.icon" class="goods-pic" @error="onImageError($event, gitem)"></image>
					<view class="goods-text">
						<view class="goods-text-gname">{{gitem.gname}}</view>
						<view class="goods-text-content">{{gitem.bcontent}}</view>
						<view class="goods-text-price">
							<view><text class="font12">￥</text>{{gitem.buyPrice}}</view>
							<view style="width: 160rpx;color:#333">
								<ControllerBuyNumBtn
									:myid="gitem.gdid"
									:max="item.restBuyNum"
									:num="gitem.buyNums"
									@toBuyNum="setBuyNumMethod"
									@toBuyId="getBuyIdMethod"
								></ControllerBuyNumBtn>
							</view>
						</view>
					</view>
				</view>
			</checkbox-group>
		</view>
		<view class="emptycart" v-if="cartlist.length==0">
			<view class="empty-text">
				挑点好商品,装满购物车吧~
				<view class="btn" @click="onclickBtn">去逛逛</view>
			</view>
		</view>
		<view style="width: 100%; height: 60px;"></view>
		<!-- 购物车列表 -->
	</view>

	<view class="footer">
		<view class="allckeck">
			<checkbox-group @change="ckallChange">
				<checkbox value="allchecked" :checked="AllChecked"></checkbox>全选
			</checkbox-group>
		</view>
		<view class="pay">
			<view style="display: flex;align-items: center;" v-if="isManagement==false">
				合计：
				<view class="pay-danwei">￥</view>
				<view class="pay-price">{{payAllPrcie}}</view>
				<view class="paybtn" @click="confirmPay">结算({{payGoodsNum}})</view>
			</view>
			<view class="paybtn" v-if="isManagement" @click="mydel">
				删除
			</view>
		</view>

	</view>

</template>

<script>
import GoodsList from '@/components/GoodsList/GoodsList.vue'
import ControllerBuyNumBtn from '@/components/ControllerBuyNumBtn/ControllerBuyNumBtn.vue'
const app = getApp({allowDefault: true})
export default {
	components: {
		GoodsList,
		ControllerBuyNumBtn
	},
	data() {
		return {
			cartlist: [],
			cartAllTotalNums: app.globalData.cartAllTotalNums,
			selectNavIndex: 1,
			modfiyBuyNum: -1,
			payGoodsNum: 0,
			payAllPrcie: 0,
			AllChecked: false,
			isManagement: false
		}
	},
	onShow() {
		// 检查登录状态
		if (!app.globalData.isLogin) {
			this.cartlist = []
			this.comptercartAllTotalNums()
			this.resetCartChecked()
			this.compterAllPrice()
			uni.showToast({
				title: '请先登录',
				icon: 'none'
			})
			return
		}
		// 加载购物车数据
		this.loadCart()
	},
	methods: {
		loadCart() {
			// 加载购物车的数据
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			uni.request({
				url: app.globalData.baseUrl + 'api/cart/getcartlist',
				method: 'GET',
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				success: (res) => {
					if (res.data.code == 200) {
						this.cartlist = res.data.data
					} else {
						this.cartlist = []
					}
					// 合算购物车内的种类总数量
					this.comptercartAllTotalNums()
					// 设置初始选中状态
					this.resetCartChecked()
					// 计算合计价钱
					this.compterAllPrice()
				},
				fail: () => {
					this.cartlist = []
					this.comptercartAllTotalNums()
					this.resetCartChecked()
					this.compterAllPrice()
					uni.showToast({
						title: '加载购物车数据失败',
						icon: 'none'
					})
				}
			})
		},
		confirmPay() {
			/**
			 * 实现思路
			 * 1.判断是否有选中的商品，无选中的商品弹出提示
			 * 2.把选中的商品，生成一个新的订单，（并且在购物车内把选中的商品移除掉，只有客户支付了，才去除掉）
			 * 3.把未选中的商品留在购物车内
			 * 4.把选中的商品，生成一个新的订单，传到结算页面
			 */

			//1. 判断是否有选中的商品，无选中的商品弹出提示
			if (this.payGoodsNum == 0) {
				uni.showToast({
					icon: 'none',
					title: '请选择结算商品'
				})
			} else {
				// 2.把选中的商品，生成一个新的订单
				let neworders = this.newOrders()
				app.globalData.neworders = neworders
				//4.把选中的商品，生成一个新的订单，传到结算页面
				uni.navigateTo({
					url: '/pages/confirmOrder/confirmOrder'
				})
			}
		},
		// 把选中的商品，生成一个新的订单
		newOrders() {
			/**
			 * 实现思路
			 * 1.创建存储新的订单对象
			 * 2.遍历购物车，找到选中的商品，添加到订单对象。
			 */
			// 1.创建存储新的订单对象
			let curNewOrders = []
			//2.遍历购物车，找到选中的商品，添加到订单对象中。
			this.cartlist.forEach(cartitem => {
				let cart = JSON.parse(JSON.stringify(cartitem))
				cart.goodsDetail = cartitem.goodsDetail.filter(cartdetailitem => cartdetailitem.checked == true)
				if (cart.goodsDetail.length > 0) {
					let nums = 0
					let sum = 0
					cart.goodsDetail.forEach(ditem => {
						sum += ditem.buyNums * ditem.buyPrice
						nums += ditem.buyNums
					})
					cart.buyTotalPrice = sum.toFixed(2)
					cart.buyTotalNums = nums
					curNewOrders.push(cart)
				}
			})
			return curNewOrders
		},
		// 合算购物车内的种类总数量
		comptercartAllTotalNums() {
			let mycartAllTotalNums = 0
			this.cartlist.forEach(mycartitem => {
				mycartAllTotalNums += mycartitem.goodsDetail.length
				let nums = 0
				let sum = 0
				mycartitem.goodsDetail.forEach(ditem => {
					sum += ditem.buyNums * ditem.buyPrice
					nums += ditem.buyNums
				})
				mycartitem.buyTotalPrice = sum.toFixed(2)
				mycartitem.buyTotalNums = nums
			})
			this.cartAllTotalNums = mycartAllTotalNums
			app.globalData.cartAllTotalNums = mycartAllTotalNums
		},
		mydel() {
			// 前端先删除选中的商品
			this.cartlist.forEach(mycartitem => {
				mycartitem.goodsDetail = mycartitem.goodsDetail.filter(mycartDetailitem => mycartDetailitem.checked == false)
			})
			// 如果当前店铺的商品全部删除了，那么当前店铺的购物车也被清空
			this.cartlist = this.cartlist.filter(mycartitem => mycartitem.goodsDetail.length > 0)
			// 与后端同步购物车
			this.updateCartList()
		},
		// 点击管理按钮
		clickmanagement() {
			this.isManagement = true
		},
		// 点击完成按钮
		clickcompete() {
			this.isManagement = false
			// 远程更新购物车的数据
			this.updateCartList()
		},
		// 远程更新购物车的数据（统一同步入口）
		updateCartList() {
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			// 如果购物车已空，在服务器端要删除当前用户的购物车数据,清空购物车
			if (!this.cartlist || this.cartlist.length === 0) {
				uni.request({
					url: app.globalData.baseUrl + 'api/cart/emptycartlist',
					method: 'GET',
					header: {
						'Authorization': token ? `Bearer ${token}` : ''
					},
					success: () => {},
					complete: () => {
						this.cartlist = []
						this.comptercartAllTotalNums()
						this.resetCartChecked()
						this.compterAllPrice()
					}
				})
				return
			}

			// 合算购物车内的种类总数量
			this.comptercartAllTotalNums()

			uni.request({
				url: app.globalData.baseUrl + 'api/cart/updatecartlist',
				method: 'POST',
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				data: this.cartlist,
				dataType: 'json',
				success: (res) => {
					if (res.data.code == 200) {
						this.cartlist = res.data.data || []
						this.comptercartAllTotalNums()
						// 更新后默认不选中，避免状态混乱
						this.resetCartChecked()
						this.compterAllPrice()
					} else {
						uni.showToast({
							title: '同步购物车失败',
							icon: 'none'
						})
					}
				},
				fail: () => {
					uni.showToast({
						title: '网络错误，购物车未同步',
						icon: 'none'
					})
				}
			})
		},

		/**
		 * 计算合计钱
		 */
		compterAllPrice() {
			let mypayGoodsNum = 0
			let mypayAllPrcie = 0
			let mycartAllTotalNums = 0
			this.cartlist.forEach(mycartitem => {
				mycartAllTotalNums += mycartitem.goodsDetail.length
				mycartitem.goodsDetail.forEach(mycartDetailitem => {
					if (mycartDetailitem.checked == true) {
						mypayGoodsNum++
						mypayAllPrcie += mycartDetailitem.buyNums * mycartDetailitem.buyPrice
					}
				})
			})
			this.payGoodsNum = mypayGoodsNum
			this.payAllPrcie = mypayAllPrcie.toFixed(2)
			this.cartAllTotalNums = mycartAllTotalNums
		},
		/**
		 * 设置是否全选
		 */
		myAllchecked() {
			let isAllChecked = true
			this.cartlist.forEach(mycartitem => {
				if (mycartitem.checked == false)
					isAllChecked = false
				mycartitem.goodsDetail.forEach(mycartDetailitem => {
					if (mycartDetailitem.checked == false)
						isAllChecked = false
				})
			})
			this.AllChecked = isAllChecked
		},
		ckchild(e, mycartitem) {
			let checkedArray = e.detail.value
			let goodsDetailLength = mycartitem.goodsDetail.length
			let mycartDetailitemcheckedNum = 0
			mycartitem.goodsDetail.forEach(mycartDetailitem => {
				if (checkedArray.includes(mycartDetailitem.gdid + '')) {
					mycartDetailitem.checked = true
					mycartDetailitemcheckedNum++
				} else
					mycartDetailitem.checked = false
			})
			// 当前店铺是否选中
			if (mycartDetailitemcheckedNum == goodsDetailLength) {
				mycartitem.checked = true
			} else {
				mycartitem.checked = false
			}
			// 设置是否全选
			this.myAllchecked()
			// 计算合计价钱
			this.compterAllPrice()
		},
		ckallshop(e) {
			let ckid = e.currentTarget.id
			let checkedArray = e.detail.value //选中数组
			// 获得所属cartid
			let cartid = ckid.substring(4)
			// 所属店铺全部选中/取消
			this.cartlist.forEach(mycartitem => {
				if (mycartitem.cartid + '' == cartid) {
					if (checkedArray.length > 0) { //全选中
						mycartitem.checked = true
						mycartitem.goodsDetail.forEach(mycartDetailitem => {
							mycartDetailitem.checked = true
						})
					} else { //全不选中
						mycartitem.checked = false
						mycartitem.goodsDetail.forEach(mycartDetailitem => {
							mycartDetailitem.checked = false
						})
					}
				}
			})
			// 设置是否全选
			this.myAllchecked()
			// 计算合计价钱
			this.compterAllPrice()
		},
		ckallChange(e) {
			let checkedArray = e.detail.value //选中数组
			if (checkedArray.includes('allchecked')) { //全选中
				this.AllChecked = true
				this.cartlist.forEach(mycartitem => {
					mycartitem.checked = true
					mycartitem.goodsDetail.forEach(mycartDetailitem => {
						mycartDetailitem.checked = true
					})
				})
			} else { //全不选中
				this.AllChecked = false
				this.cartlist.forEach(mycartitem => {
					mycartitem.checked = false
					mycartitem.goodsDetail.forEach(mycartDetailitem => {
						mycartDetailitem.checked = false
					})
				})
			}
			// 计算合计价钱
			this.compterAllPrice()
		},

		/**
		 * 设置初始选中状态
		 */
		resetCartChecked() {
			this.cartlist.forEach(mycartitem => {
				mycartitem.checked = false
				mycartitem.goodsDetail.forEach(mycartDetailitem => {
					mycartDetailitem.checked = false
				})
			})
			this.AllChecked = false
			this.payGoodsNum = 0
			this.payAllPrcie = 0
		},
		clickNav(index) {
			this.selectNavIndex = index
		},
		back() {
			uni.navigateBack({
				delta: 1
			})
		},
		onclickBtn() {
				uni.switchTab({
					url: '/pages/index/index'
				})
			},
			// 图片加载失败处理
			onImageError(e, gitem) {
				// 设置默认图片路径
				const defaultImage = '/static/goodsbanner01.png';
				// 更新当前商品的图片为默认图片
				if (gitem && gitem.icon) {
					gitem.icon = defaultImage;
				}
				// 防止重复触发错误
				const imgElement = e.target;
				if (imgElement) {
					imgElement.onerror = null;
				}
			},
			setBuyNumMethod(buyNum) {
				this.modfiyBuyNum = buyNum
			},
		getBuyIdMethod(myid) {
			this.cartlist.forEach(mycartitem => {
				mycartitem.goodsDetail.forEach(mycartDetailitem => {
					if (mycartDetailitem.gdid == myid) {
						mycartDetailitem.buyNums = this.modfiyBuyNum
						if (mycartDetailitem.checked) {
							this.compterAllPrice()
						}
					}
				})
			})
			// 远程更新购物车的数据
			this.updateCartList()
		}
	}
}
</script>

<style lang="scss">
	page {
		padding: 0;
	}

	checkbox {
		transform: scale(0.8);
	}

	.header {
		width: 100%;
		height: 220rpx;
		background-color: #FFF;
		padding: 20rpx 40rpx 20rpx 20rpx;
		box-sizing: border-box;
		text-align: center;
		position: sticky;
		top: 0;
		left: 0;
		z-index: 10;

		.nav-1 {
			width: 100%;
			height: 60rpx;
			display: flex;
			justify-content: space-between;
			align-items: flex-start;
			background-color: #FFF;
			color: #333;

			.back {
				width: 90rpx;
				height: 100%;
				text-align: left;
			}

			.title {
				font-size: 32rpx;
				font-weight: bold;
			}

			.management {
				width: 90rpx;
				height: 100%;
				font-size: 28rpx;
				text-align: right;
			}
		}

		.nav-2 {
			display: flex;
			justify-content: space-between;
			font-size: 28rpx;
			width: 100%;
			height: 70rpx;
			text-align: center;
			align-items: center;

			.nav-2-title {
				width: 45%;
				height: 100%;
				line-height: 80rpx;
				box-sizing: border-box;
			}

			.active {
				color: #333;
				font-weight: bold;
				border-bottom: 3px solid red;
			}
		}
	}

	.container {
		padding: 20rpx;

		.vcartlist {
			width: 100%;
			background-color: #FFF;
			border-radius: 20rpx;
			box-sizing: border-box;
			padding: 20rpx;
			margin: 20rpx 0;

			.vcartlist-title {
				line-height: 60rpx;
				display: flex;

				.shopname {
					color: #333;
					font-weight: bold;
					font-size: 28rpx;
				}
			}

			.vcartlist-goods {
				display: flex;
				align-items: center;
				margin: 20rpx 0;

				.goods-pic {
					width: 180rpx;
					height: 180rpx;
					border-radius: 20rpx;
					flex-shrink: 0;
					margin-right: 20rpx;
				}

				.goods-text {
					flex-grow: 1;
					width: 60%;
					height: 180rpx;
					display: flex;
					flex-direction: column;
					justify-content: space-between;

					.goods-text-gname {
						font-weight: bold;
						white-space: nowrap;
						width: 100%;
						overflow: hidden;
						text-overflow: ellipsis;
					}

					.goods-text-price {
						color: red;
						font-size: 30rpx;
						display: flex;
						align-items: center;
						justify-content: space-between;

						.font12 {
							font-size: 24rpx;
							transform: scale(0.8);
						}
					}

				}
			}
		}

		.emptycart {
			width: 100%;
			height: 400rpx;
			background-color: #FFF;
			border-radius: 20rpx;
			display: flex;
			flex-direction: column;
			justify-content: flex-end;
			align-items: center;

			.empty-text {
				display: flex;
				flex-direction: column;
				align-items: center;
			}

			.btn {
				width: 200rpx;
				height: 60rpx;
				font-size: 28rpx;
				color: #333;
				border: 1px solid #EEE;
				border-radius: 20rpx;
				text-align: center;
				line-height: 60rpx;
				margin-top: 100rpx;
				margin-bottom: 50rpx;
			}
		}

		.youlike {
			margin: 20rpx 0;
			font-size: 30rpx;
			font-weight: bold;
			color: #333;
		}
	}

	.footer {
		width: 100%;
		height: 130rpx;
		position: fixed;
		bottom: 50px;
		left: 0;
		background-color: #FFF;
		display: flex;
		justify-content: space-between;
		align-items: center;
		box-sizing: border-box;
		padding: 20rpx;

		.pay {
			display: flex;
			align-items: center;

			.pay-danwei {
				color: #FC2B55;
				font-size: 28rpx;
				height: 180rpx;
				line-height: 188rpx;
			}

			.pay-price {
				color: #FC2B55;
				font-size: 36rpx;
				height: 180rpx;
				line-height: 180rpx;
				padding-right: 20rpx;
			}
		}

		.paybtn {
			width: 190rpx;
			height: 70rpx;
			border-radius: 20rpx;
			background-color: #FC2B55;
			text-align: center;
			line-height: 70rpx;
			color: #FFF;
			font-size: 30rpx;
		}
	}
</style>

