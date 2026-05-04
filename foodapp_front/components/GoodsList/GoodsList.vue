<template>
	<view class="goodslistparent">
		<view class="goodsitem" v-for="(item,index) in goodslist" @click="clickgoodsitem(item)">
			<view class="goods-image-wrapper">
				<image :src="getFirstImage(item.icon)" class="firstimg" @error="onImageError($event, item)"></image>
				<view class="discounts" v-if="item.discounts!=''">
					{{item.discounts}}
				</view>
			</view>
			<view class="goods-info">
				<view class="gname">{{item.gname}}</view>
				<view class="goods-rating" v-if="item.rating || item.rating === 0">
					<uni-icons type="star-filled" size="20" color="#FFD700"></uni-icons>
					<text class="rating-score">{{item.rating || 5.0}}</text>
				</view>
				<view class="priceparent">
					<text class="danwei">￥</text>
				    <text class="price">{{item.price}}</text>
					<text class="sellTotalNum">已售{{item.sellTotalNum}}件</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	const app = getApp({allowDefault:true})
	export default {
		name: "GoodsList",
		data() {
			return {
				goodslist: []
			};
		},
		// 生命周期函数
		created() {
			this.loadGoodsList()
		},
		methods: {
			// 图片加载失败处理
			onImageError(e, item) {
				// 设置默认图片路径
				const defaultImage = '/static/goodsbanner01.png';
				// 更新当前商品的图片为默认图片
				item.icon = defaultImage;
				// 防止重复触发错误
				const imgElement = e.target;
				if (imgElement) {
					imgElement.onerror = null;
				}
			},
			// 点击当前商品，转向商品详细页面
			clickgoodsitem(item) {
				// 1. 保存当前商品信息
				app.globalData.curGoods = item
				// 2. 转向商品详细页面
				uni.navigateTo({
					url: "/pages/goods/goods?gid=" + item.gid
				})
			},
			// 根据搜索关键字，查询商品列表
			searchKeyWord(keyword) {
				if (keyword == undefined || keyword.trim() == '')
					return;
				
				// 搜索商品
				uni.request({
					url: app.globalData.baseUrl + 'api/goods/findbykeyword/' + keyword,
					success: (res) => {
						if (res.data.code == 200) {
							this.goodslist = res.data.data
							// console.log(this.goodslist)
						}
						// console.log(res)
					}
				})
			},
			// 去远程加载商品列表
			loadGoodsList(cgid) {
				let myurl = app.globalData.baseUrl + 'api/goods/findbycgid/' + cgid
				if (cgid == undefined || cgid == '') {
					myurl = app.globalData.baseUrl + 'api/goods/findbycgid'
				}
					
				uni.request({
					url: myurl,
					success: (res) => {
						if (res.data.code == 200) {
							this.goodslist = res.data.data
							// console.log(this.goodslist)
						}
						// console.log(res)
					}
				})
			},
			// 获取第一张图片URL
			getFirstImage(icon) {
				if (!icon) {
					return '/static/goodsbanner01.png'
				}
				// 按分号分割，取第一张图片，并去除空格
				return icon.split(';')[0].trim()
			}
		}
	}
</script>

<style lang="scss">
	.goodslistparent {
		width: 100%;
		height: auto;
		margin: 0 auto;
		// 分列显示
		column-count: 2;
		column-gap: 20rpx;
		padding: 0 10rpx;
		
		.goodsitem{
			width: 100%;
			height: auto;
			background-color: #FFF;
			// 禁止盒子换行
			break-inside: avoid;
			margin-bottom: 25rpx;
			border-radius: 20rpx;
			overflow: hidden;
			box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
			transition: all 0.3s ease;
			
			&:hover {
				transform: translateY(-2rpx);
				box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
			}
			
			.goods-image-wrapper {
				position: relative;
				width: 100%;
				height: 320rpx;
				overflow: hidden;
				
				.firstimg{
					width: 100%;
					height: 100%;
					object-fit: cover;
					transition: all 0.3s ease;
				}
				
				&:hover .firstimg {
					transform: scale(1.05);
				}
				
				.discounts{
					position: absolute;
					top: 15rpx;
					left: 15rpx;
					background-color: #ff4444;
					color: white;
					font-size: 24rpx;
					font-weight: bold;
					padding: 8rpx 15rpx;
					border-radius: 20rpx;
					box-shadow: 0 2rpx 6rpx rgba(255, 68, 68, 0.3);
				}
			}
			
			.goods-info {
				padding: 20rpx;
			}
			
			.gname{
				font-size: 26rpx;
				color: #333;
				font-weight: 500;
				margin-bottom: 15rpx;
				line-height: 36rpx;
				display: -webkit-box;
				-webkit-line-clamp: 2;
				-webkit-box-orient: vertical;
				overflow: hidden;
			}
			
			.goods-rating {
				display: flex;
				align-items: center;
				margin-bottom: 15rpx;
				
				.rating-score {
					margin-left: 8rpx;
					font-size: 22rpx;
					color: #666;
				}
			}
			
			.priceparent{
				display: flex;
				align-items:baseline;
				margin-top:15rpx;
				
				.danwei{
					color: #ff4444;
					font-size: 24rpx;
					font-weight: bold;
				}
				.price{
					color: #ff4444;
					font-size: 36rpx;
					font-weight: bold;
				}
				.sellTotalNum{
					color: #999;
					font-size: 22rpx;
					margin-left: 15rpx;
				}
			}	
		}
	}
</style>