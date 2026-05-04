<template>
	<view class="buycontainer">
		<!-- 当前商品信息 -->
		<view class="curgoods-info">
			<view v-if="selectGoodsDetail.gdpic!=null && selectGoodsDetail.gdpic!=undefined">
				<image :src="selectGoodsDetail.gdpic" class="curgoods-info-icon"></image>
			</view>

			<view class="curgoods-info-text">
				<view class="curgoods-price">
					<view class="danwei">￥</view>
					<view class="my-integer-price">{{parseInt(selectGoodsDetail.specialPrice)}}</view>
					<view class="my-decimal-price">{{getDecimal(selectGoodsDetail.specialPrice)}}</view>
				</view>
				<view class="curgoods-info-stocknum">
					库存{{selectGoodsDetail.stockNum}}件
				</view>
				<view class="curgoods-info-select">
					{{selectText}}
				</view>
			</view>
			<view class="close" @click="colsebuyGoodsPopup">
				<uni-icons type="closeempty"></uni-icons>
			</view>
		</view>
		<!-- 当前商品信息 -->

		<!-- 选择当前商品规格，颜色等等 -->
		<scroll-view class="selectparent" scroll-y="true">
			<view class="select-type" v-for="(typeitem,typeindex) in typelist">
				<view class="select-type-attributeName">
					{{typeitem.attributeName}}
				</view>
				<view class="select-type-attributeValueList">
					<view class="select-type-attributeValue" :class="item.checked?'active':''"
						v-for="(item,index) in typeitem.attributeValueList">
						<view @click="selectCurGoodsType(typeindex,index)">{{item.attributeValue}}</view>
						<view class="mask" v-if="item.selected==3"></view>
					</view>
				</view>
			</view>
			<!-- 选择当前商品规格，颜色等等 -->

			<!-- 购买数量 -->
			<view class="select-buy">
				<view class="select-buy-text">
					购买数量<text class="restbuynum">
						每人限购{{curgoods.restBuyNum}}件
					</text>
				</view>

				<view class="controllerBuyNumBtn">
					<ControllerBuyNumBtn :max="curgoods.restBuyNum" @toBuyNum="setBuyNumMethod"></ControllerBuyNumBtn>
				</view>
			</view>
			<!-- 购买数量 -->
		</scroll-view>

		<!-- 底部导航 -->
		<view class="footer">
			<view class="nav-addcart">
				<view class="nav-addcart-btn" @click="addBuyGoods">加入购物车</view>
			</view>
		</view>
		<!-- 底部导航 -->
	</view>
</template>

<script>
	import ControllerBuyNumBtn from "@/components/ControllerBuyNumBtn/ControllerBuyNumBtn.vue"
	const app = getApp({
		allowDefault: true
	})
	export default {
		name: "BuyGoodsInfo",
		props: ['curgoods', 'curcartlist'],
		components: {
			ControllerBuyNumBtn
		},
		data() {
			return {
				typelist: [],
				selectText: '',
				curBuyNum: 1,
				attributeValue1ListK: [],
				attributeValue2ListK: [],
				cartlist: this.curcartlist,
				cartAllTotalNums: 0,
				selectGoodsDetail: {}
			};
		},
		mounted() {
			
			// 加载选择当前商品规格，颜色等等
			this.selecttype()
			// 设置选中商品
			if (this.curgoods && this.curgoods.goodsDetailList && this.curgoods.goodsDetailList.length > 0) {
				this.selectGoodsDetail = this.curgoods.goodsDetailList[0]
			}
			// 购物车内的商品类别数量
			this.cartAllTotalNums = this.cartlist ? this.cartlist.length : 0
		},
		methods: {
			/**
			 * 判断是否已选择了商品类型
			 */
			checkIsSelectType() {
				let sum = 0
				this.typelist.forEach(typeitem => {
					typeitem.attributeValueList.forEach(item => {
						if (item.checked)
							sum++
					})
				})
				if (sum == this.typelist.length)
					return true
				else {
					uni.showToast({
						icon: "none",
						title: '请选择分类信息'
					})
					return false
				}
			},
			addBuyGoods() {
				// 判断是否已选择了商品类型
				if (this.checkIsSelectType()) {
					//添加购物车
					this.addCart()
					// 合算当前店铺的总数量、价钱和购物车总数量
					this.compterShopCart()
					// 通知主组件关闭底部弹出的页面
					this.$emit('colsebuyGoodsPopup', this.cartAllTotalNums)
				}
			},
			/**
			 * 合算当前店铺的总数量及价钱
			 */
			compterShopCart() {
				let myAllTotalNums = 0
				this.cartlist.forEach(mycartitem => {
					// 找到同一家店铺
					if (mycartitem.sid == this.curgoods.shop.sid) {
						// 合计总数量
						let totalNums = 0
						// 合计总价钱
						let totalPrice = 0
						mycartitem.goodsDetail.forEach(mycartDetailitem => {
							totalNums += mycartDetailitem.buyNums
							totalPrice += mycartDetailitem.buyNums * mycartDetailitem.buyPrice
						})
						mycartitem.buyTotalNums = totalNums
						mycartitem.buyTotalPrice = totalPrice
					}
					myAllTotalNums += mycartitem.goodsDetail.length
				})
				this.cartAllTotalNums = myAllTotalNums
				app.globalData.cartAllTotalNums = this.cartAllTotalNums
			},
			addCart() {
				// 创建当前购买的商品数据
				let buygoods = {}
				// 获得购买的商品详细信息
				let buygoodsDetail = this.findSelectedGoods()
				//======================== 如果购物车存在此商品，数量增加===========================
				let isFind = false

				for (var i = 0; i < this.cartlist.length; i++) {
					// 找到同一家店铺
					if (this.cartlist[i].sid == this.curgoods.shop.sid) {
						// 查找商品详细，是否有相同的商品
						for (var j = 0; j < this.cartlist[i].goodsDetail.length; j++) {
							// 找到相同商品
							if (this.cartlist[i].goodsDetail[j].gdid == buygoodsDetail.gdid) {
								// 更改购买数量及价钱
								this.cartlist[i].goodsDetail[j].buyNums += this.curBuyNum
								this.cartlist[i].goodsDetail[j].buyPrice = buygoodsDetail.specialPrice
								isFind = true
								break
							}
						}
						// 当前店铺的不同商品，添加一个商品详细信息
						if (!isFind) {
							// 创建新商品详细数据
							let buygoodsDetail_new = {}

							buygoodsDetail_new.gid = this.curgoods.gid
							buygoodsDetail_new.gdid = buygoodsDetail.gdid
							buygoodsDetail_new.gname = this.curgoods.gname
							buygoodsDetail_new.icon = buygoodsDetail.gdpic
							buygoodsDetail_new.bcontent = buygoodsDetail.attributeValue1
							if (buygoodsDetail.attributeValue2 != null) {
								buygoodsDetail_new.bcontent += '/' + buygoodsDetail.attributeValue2
							}
							buygoodsDetail_new.buyPrice = buygoodsDetail.specialPrice
							buygoodsDetail_new.buyNums = this.curBuyNum
							buygoodsDetail_new.cartid = buygoods.cartid
							this.cartlist[i].goodsDetail.push(buygoodsDetail_new)
						}
						// 找到当前店铺相同的商品
						buygoods = this.cartlist[i]
						// 合计购物车的总价格
						this.compterCurCart(buygoods)
						// 远程更新购物车
						this.updateCurCart()
						// 退出循环
						return
					}
				}


				//=========================如果购物车内不存此商品，添加新的记录===========================
				//=========== 当前店铺的购物车主数据======================
				if (buygoods.cartid == undefined || buygoods.cartid == null) {
					buygoods.cartid = 0
					buygoods.cid = app.globalData.cid
					buygoods.sid = this.curgoods.shop.sid
					buygoods.shop = this.curgoods.shop
					buygoods.buyTotalNums = this.curBuyNum
					buygoods.buyTotalPrice = 0
					buygoods.restBuyNum = this.curgoods.restBuyNum
					buygoods.postprice = this.curgoods.postprice
					buygoods.beizhu = ''
				}


				//============== 当前店铺的购物车详细数据=============
				// 创建新商品详细数据
				if (buygoods.goodsDetail == undefined)
					buygoods.goodsDetail = []
				let buygoodsDetail_new = {}

				buygoodsDetail_new.gid = this.curgoods.gid
				buygoodsDetail_new.gdid = buygoodsDetail.gdid
				buygoodsDetail_new.gname = this.curgoods.gname
				buygoodsDetail_new.icon = buygoodsDetail.gdpic
				buygoodsDetail_new.bcontent = buygoodsDetail.attributeValue1
				if (buygoodsDetail.attributeValue2 != null) {
					buygoodsDetail_new.bcontent += '/' + buygoodsDetail.attributeValue2
				}
				buygoodsDetail_new.buyPrice = buygoodsDetail.specialPrice
				buygoodsDetail_new.buyNums = this.curBuyNum
				buygoodsDetail_new.cartid = buygoods.cartid
				buygoods.goodsDetail.push(buygoodsDetail_new)

				// 合计购物车的总价格
				this.compterCurCart(buygoods);

				// 添加到购物车
				if (buygoods.cartid == 0) {
					//buygoods.cartid= buygoods.cid + new Date().getTime()
					//buygoodsDetail_new.cartid = buygoods.cartid
					buygoods.cartid = null
					buygoodsDetail_new.cartid = null
					this.cartlist.push(buygoods)
				}

				// 远程更新购物车
				this.updateCurCart()

			},
			// 远程更新购物车
			updateCurCart() {
				// 远程更新购物车
				const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
				uni.request({
					url: app.globalData.baseUrl + 'api/cart/updatecartlist',
					method: "POST",
					header: {
						'Authorization': token ? `Bearer ${token}` : ''
					},
					data: JSON.parse(JSON.stringify(this.cartlist)),
					dataType: 'json',
					success: (result) => {
						if(result.data.code==200){
								 this.cartlist = result.data.data
						}
					}
				})
			},
			//合计购物车的总价格
			compterCurCart(curCart) {
				let priceSum = 0
				let numSum = 0
				curCart.goodsDetail.forEach(ditem => {
					priceSum += ditem.buyNums * ditem.buyPrice
					numSum += ditem.buyNums
				})
				curCart.buyTotalPrice = priceSum
				curCart.buyTotalNums = numSum
			},
			/**
			 * 查找选中的商品详细
			 */
			findSelectedGoods() {
				let goodsDetail = {}
				// 如果只有一个分类
				if (this.typelist.length == 1) {
					let attributeValueItem = this.typelist[0].attributeValueList.filter(item => item.checked)[0]
					goodsDetail = this.curgoods.goodsDetailList.filter(item => item.attributeValue1 == attributeValueItem
						.attributeValue)[0]
				}
				// 如果只有二个分类
				if (this.typelist.length == 2) {
					let attributeValueItem1 = this.typelist[0].attributeValueList.filter(item => item.checked)[0]
					let attributeValueItem2 = this.typelist[1].attributeValueList.filter(item => item.checked)[0]
					goodsDetail = this.curgoods.goodsDetailList.filter(item => item.attributeValue1 == attributeValueItem1
						.attributeValue && item.attributeValue2 == attributeValueItem2.attributeValue)[0]
				}
				this.selectGoodsDetail = goodsDetail
				return goodsDetail
			},
			getDiscounts() {
				this.addBuyGoods()
			},
			setBuyNumMethod(buyNum) {
				this.curBuyNum = buyNum
			},
			selectCurGoodsType(typeindex, index) {
				// 先设置当前类别全部不选中
				this.typelist[typeindex].attributeValueList.forEach(item => {
					item.checked = false
				})
				this.typelist[typeindex].attributeValueList[index].checked = true

				// 查找是否有对应的类型选项
				this.getOtherSelect(typeindex, index)
				// 查找选中的商品详细
				this.findSelectedGoods()

				// 设置已选择类别的文字
				this.setSelectedText()
			},
			// 设置已选择类别的文字
			setSelectedText() {
				this.selectText = "已选择  "
				this.typelist.forEach(typeitem => {
					typeitem.attributeValueList.forEach(item => {
						if (item.checked)
							this.selectText += item.attributeValue + " "
					})
				})
			},
			getOtherSelect(typeindex, index) {
				// 如果只有一个分类，直接返回
				if (this.typelist.length == 1) return
				// 选中第一个类别，查看第二个对应类别的选项
				let attributeValue = this.typelist[typeindex].attributeValueList[index].attributeValue

				if (typeindex == 0) {
					this.attributeValue2ListK = this.curgoods.goodsDetailList.filter(item => item.attributeValue1 ==
						attributeValue)
					//console.log(this.attributeValue2ListK)
					// 重置
					this.typelist[1].attributeValueList.forEach(fitem => {
						// fitem.selected = 3
						fitem.checked = false
					})
					let checkedNum = 0
					this.attributeValue2ListK.forEach(kitem => {
						this.typelist[1].attributeValueList.forEach(fitem => {
							if (fitem.attributeValue == kitem.attributeValue2) {
								// fitem.selected =2
								checkedNum++
								if (checkedNum < 2)
									fitem.checked = true
								else
									fitem.checked = false
							}
						})
					})
					// console.log(this.typelist)
				}
				if (typeindex == 1) {
					this.attributeValue1ListK = this.curgoods.goodsDetailList.filter(item => item.attributeValue2 ==
						attributeValue)
					// 重置
					this.typelist[0].attributeValueList.forEach(fitem => {
						fitem.checked = false
						//fitem.selected =3
					})
					let checkedNum = 0
					this.attributeValue1ListK.forEach(kitem => {
						this.typelist[0].attributeValueList.forEach(fitem => {
							if (fitem.attributeValue == kitem.attributeValue1) {
								//fitem.selected =2
								checkedNum++
								if (checkedNum < 2)
									fitem.checked = true
								else
									fitem.checked = false
							}
						})
					})
				}
			},
			selecttype() {
			// 检查 curgoods 和 goodsDetailList 是否存在
			if (!this.curgoods || !this.curgoods.goodsDetailList || this.curgoods.goodsDetailList.length === 0) {
				return
			}
			
			// 添加选择类别
			let item = this.curgoods.goodsDetailList[0]
			for (let index = 0; index < 5; index++) {
				let curtype = {}
				if (index == 0 && item.attributeName1 != null && item.attributeName1 != '') {
					curtype.attributeName = item.attributeName1
					this.typelist.push(curtype)
				}
				if (index == 1 && item.attributeName2 != null && item.attributeName2 != '') {
					curtype.attributeName = item.attributeName2
					this.typelist.push(curtype)
				}
				if (index == 2 && item.attributeName3 != null && item.attributeName3 != '') {
					curtype.attributeName = item.attributeName3
					this.typelist.push(curtype)
				}
				if (index == 3 && item.attributeName4 != null && item.attributeName4 != '') {
					curtype.attributeName = item.attributeName4
					this.typelist.push(curtype)
				}
				if (index == 4 && item.attributeName5 != null && item.attributeName5 != '') {
					curtype.attributeName = item.attributeName5
					this.typelist.push(curtype)
				}
			}
			//添加选择类别值
			this.typelist.forEach((typeitem, typeindex) => {
				typeitem.attributeValueList = []
				this.curgoods.goodsDetailList.forEach((item, index) => {
					let curvalue = {}
					if (index == 0)
						curvalue.checked = true
					else curvalue.checked = false
					// curvalue.selected = 1
					if (typeindex == 0 && item.attributeValue1 != null && item.attributeValue1 != '') {
						curvalue.attributeValue = item.attributeValue1
						let findex = typeitem.attributeValueList.findIndex(fitem => fitem
							.attributeValue == curvalue.attributeValue)
						if (findex == -1)
							typeitem.attributeValueList.push(curvalue)
					}
					if (typeindex == 1 && item.attributeValue2 != null && item.attributeValue2 != '') {
						curvalue.attributeValue = item.attributeValue2
						let findex = typeitem.attributeValueList.findIndex(fitem => fitem
							.attributeValue == curvalue.attributeValue)
						if (findex == -1)
							typeitem.attributeValueList.push(curvalue)
					}
					if (typeindex == 2 && item.attributeValue3 != null && item.attributeValue3 != '') {
						curvalue.attributeValue = item.attributeValue3
						let findex = typeitem.attributeValueList.findIndex(fitem => fitem
							.attributeValue == curvalue.attributeValue)
						if (findex == -1)
							typeitem.attributeValueList.push(curvalue)
					}
					if (typeindex == 3 && item.attributeValue4 != null && item.attributeValue4 != '') {
						curvalue.attributeValue = item.attributeValue4
						let findex = typeitem.attributeValueList.findIndex(fitem => fitem
							.attributeValue == curvalue.attributeValue)
						if (findex == -1)
							typeitem.attributeValueList.push(curvalue)
					}
					if (typeindex == 4 && item.attributeValue5 != null && item.attributeValue5 != '') {
						curvalue.attributeValue = item.attributeValue5
						let findex = typeitem.attributeValueList.findIndex(fitem => fitem
							.attributeValue == curvalue.attributeValue)
						if (findex == -1)
							typeitem.attributeValueList.push(curvalue)
					}
				})
			})
				// 设置选择的文字
				this.setSelectedText()
			},
			getDecimal(price) {
				let att = (price + '').split('.')
				if (att.length > 1) {
					return '.' + att[1]
				} else {
					return ''
				}
			},
			colsebuyGoodsPopup() {
				this.$emit('colsebuyGoodsPopup')
			}
		}
	}
</script>

<style lang="scss">
	.buycontainer {
		width: 100vw;
		height: 85vh;
		background-color: #FFF;
		border-radius: 15rpx 15px 0 0;
		box-sizing: border-box;
		font-size: 24rpx;
		font-family: 'Microsoft YaHei';
		padding: 30rpx;
		color: #777;
		letter-spacing: 1px;

		.curgoods-info {
			display: flex;
			position: relative;
			border-bottom: 1px solid #F2F2F8;
			padding-bottom: 40rpx;

			.close {
				height: 50rpx;
				width: 50rpx;
				position: absolute;
				top: 0;
				right: 0;
			}

			.curgoods-info-icon {
				width: 200rpx;
				height: 200rpx;
				flex-shrink: 0;
			}

			.curgoods-info-text {
				padding-top: 50rpx;
				padding-left: 20rpx;
				display: flex;
				flex-direction: column;
				justify-content: space-between;

				.curgoods-price {
					height: 50rpx;
					display: flex;
					color: red;
					font-size: 24rpx;
					font-weight: 600;
					line-height: 50rpx;
					justify-content: flex-start;

					.danwei,
					.my-decimal-price {
						line-height: 60rpx;
					}

					.my-integer-price {
						font-size: 40rpx;
					}
				}

				.curgoods-info-select {
					width: 460rpx;
					white-space: nowrap;
					overflow: hidden;
					text-overflow: ellipsis;
				}
			}

		}

		.selectparent {
			height: 700rpx;
		}

		.select-type {
			padding-top: 30rpx;
			padding-bottom: 20rpx;
			border-bottom: 1px solid #F2F2F8;

			.select-type-attributeName {
				font-weight: bold;
				font-size: 26rpx;
			}

			.select-type-attributeValueList {
				width: 100%;
				display: flex;
				flex-wrap: wrap;
				padding-top: 10rpx;

				.select-type-attributeValue {
					box-sizing: border-box;
					height: 50rpx;
					line-height: 30rpx;
					padding: 10rpx 30rpx;
					background-color: #F0F0F0;
					margin: 10rpx 10rpx 10rpx 0;
					border: 1px solid #F0F0F0;
					border-radius: 5rpx;
					z-index: 10;
					position: relative;
				}

				.mask {
					width: 100%;
					height: 100%;
					position: absolute;
					top: 0;
					left: 0;
					background-color: #FFF;
					opacity: 0.7;
					z-index: 15;
				}

				.active {
					color: #F36C8A;
					background-color: #F8F8F8;
					border: 1px solid #FCE5ED;
				}
			}
		}

		.select-buy {
			margin-top: 40rpx;
			display: flex;
			justify-content: space-between;

			.select-buy-text {
				font-weight: bold;

				.restbuynum {
					font-weight: 400;
					padding-left: 10rpx;
					color: #999;
				}
			}

			.controllerBuyNumBtn {
				width: 180rpx;
			}

		}

		.footer {
			width: 100%;
			height: 60px;
			background-color: #FFF;
			position: fixed;
			bottom: 0;
			left: 0;
			display: flex;
			justify-content: space-around;
			align-items: center;
			font-size: 26rpx;
			color: #777;

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
					height: 45px;
					border-radius: 20rpx;
					background-color: #FE4A6D;
					color: #FFF;
				}
			}
		}
	}
</style>