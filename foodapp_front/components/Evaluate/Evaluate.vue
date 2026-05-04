<template>
	<view>
		<view class="evaluate container-moudle">
			<!-- 评价标题 -->
			<view class="evaluate-top" @click="goToEvaluatePage">
				<view class="evaluate-title">
					商品评价
					<text class="evaluate-title-totalnum">({{evaluateCount}})</text>
				</view>
				<view class="showArrow">
					<uni-icons type="forward" color="#AAA" size="16"></uni-icons>
				</view>
			</view>
			<!-- 评价标题 -->
			
			<!-- 评价类型数据 -->
			<view class="evaluate-typenum">
				<view class="evaluate-typenum-item" v-for="(item,index) in evaluatetypelist">
					<uni-icons v-if="item.etype==2" customPrefix="iconfont" type="icon-kechengshouquan" color="green"></uni-icons>
					{{item.etypestr}} {{item.etypenum}}
				</view>
			</view>
			<!-- 评价类型数据 -->
			
			<!-- 评价列表，最多显示3条 -->
			<view class="evaluate-list" v-for="(item,index) in displayEvaluateList">
				<view class="evaluate-list-customer">
					<image :src="item.hearderimg" v-if="item.hearderimg!=null && item.hearderimg!=''" class="evaluate-list-customer-img"></image>
					<view v-else class="evaluate-list-customer-img">
						<uni-icons type="person" size="18"></uni-icons>
					</view>
					{{item.douyinCode}}
					<view class="evaluate-list-backcust" v-if="item.isBackCustomer">回头客</view>
				</view>
				<view class="evaluate-list-content">
					{{item.econtent}}
				</view>
				<view class="evaluate-list-pics">
					<image class="evaluate-list-pics-item" :src="myitem" v-for="(myitem,myindex) in item.epics"></image>
				</view>
			</view>
			<!-- 评价列表 -->
			
			<!-- 查看全部评价按钮 -->
			<view class="view-all-btn" v-if="evaluateList.length > 3" @click="goToEvaluatePage">
				查看全部评价
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name:"Evaluate",
		props:['evaluateList','evaluateCount','goodsId'],
		data() {
			return {
				evaluatetypelist:[
					{etype:1,etypestr:"回头客",etypenum:450},
					{etype:2,etypestr:"是正品",etypenum:5},
					{etype:3,etypestr:"口感好",etypenum:200},
					{etype:4,etypestr:"会回购",etypenum:200}
				]
			};
		},
		computed: {
			// 最多显示3条评价
			displayEvaluateList() {
				if (!this.evaluateList || this.evaluateList.length === 0) {
					return [];
				}
				return this.evaluateList.slice(0, 3);
			}
		},
		methods: {
			// 跳转到评价详情页
			goToEvaluatePage() {
				uni.navigateTo({
					url: `/pages/evaluate/evaluate-list?gid=${this.goodsId}&count=${this.evaluateCount}`
				});
			},
			// 加载评价类型数据
			loadEvaluateTypeList() {
				const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
				const app = getApp({allowDefault: true});
				uni.request({
					url: app.globalData.baseUrl + 'api/evaluate/getEvaluateTypeList',
					method: 'GET',
					header: {
						'Authorization': token ? `Bearer ${token}` : ''
					},
					data: {
						gid: this.goodsId
					},
					success: (res) => {
						if (res.data.code === 200) {
							this.evaluatetypelist = res.data.data || [];
						}
					},
					fail: (err) => {
						console.error('获取评价类型数据失败:', err);
					}
				});
			}
		}
	}
</script>

<style lang="scss">
.evaluate{
			color: #333;
			.evaluate-top{
				display: flex;
				justify-content: space-between;
				.evaluate-title{
					font-size: 29rpx;
					
					.evaluate-title-totalnum{
						font-weight: 600;
						color: #444;
					}
				}
			}
			.evaluate-typenum{
				display: flex;
				flex-wrap: wrap;
				justify-content: flex-start;
				margin: 20rpx auto;

				.evaluate-typenum-item{
					height: 60rpx;
					background-color:#F6F6F8;
					padding: 15rpx;
					box-sizing: border-box;
					border-radius: 10rpx;
					line-height: 30rpx;
					margin: 10rpx 5rpx;
				}
			}
			
			.evaluate-list-content{
				padding: 15rpx 0;
			}
			
			.evaluate-list{
				margin-bottom: 20rpx;
				border-bottom: 1px solid #F0F0F0;
				padding-bottom: 30rpx;
				.evaluate-list-customer{
					display: flex;
					.evaluate-list-customer-img{
						width: 40rpx;
						height: 40rpx;
						border-radius: 20rpx;
						margin-right: 15rpx;
						background-color: #F0F0F0;
						overflow: hidden;
					}
					.evaluate-list-backcust{
						width: 100rpx;
						height: 40rpx;
						background-color: #FEF4EA;
						text-align: center;
						border-radius: 10rpx;
						margin-left: 10rpx;
						color: #BC712D;
						line-height: 40rpx;
					}
				}
				
				.evaluate-list-pics{
					width: 100%;
					display: flex;
					flex-wrap: wrap;
					.evaluate-list-pics-item{
						width: 160rpx;
						height: 160rpx;
						border-radius: 10rpx;
						margin-right: 6rpx;
						border: 2rpx solid #F0F0F0;
						box-sizing: border-box;
					}
					.evaluate-list-pics-item:last-child{
			margin-right:0
		}
	}
	}
	
	// 查看全部评价按钮
	.view-all-btn {
		text-align: center;
		padding: 20rpx;
		color: #ff4757;
		font-size: 28rpx;
		border-radius: 10rpx;
		background-color: #fef5f5;
		margin: 20rpx 0;
		cursor: pointer;
		transition: all 0.3s ease;
		
		&:hover {
			background-color: #ffebeb;
		}
	}
}
</style>