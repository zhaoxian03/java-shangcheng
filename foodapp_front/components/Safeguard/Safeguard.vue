<template>
	<view class="container">
		<view class="top"></view>
		<scroll-view class="safe-list-parent" scroll-y="true">
			<view class="safe-list" v-for="(item,index) in safeList">
				<view class="safe-list-sgicon">
					<image :src="item.sgicon" class="safe-list-img"></image>
				</view>
				<view class="safe-list-content">
					<view class="safe-list-content-title">
						{{item.sgname}}
					</view>
					<view class="safe-list-content-text">
						{{item.sgcontent}}
					</view>
				</view>
			</view>
			<view class="safe-list">
				<view class="safe-list-sgicon">
					<image src="http://www.itlaobing.com/douyin/other/safeguard07.png" class="safe-list-img"></image>
				</view>
				<view class="safe-list-content">
					<view class="safe-list-content-title">
						商家资质
					</view>
					<view class="safe-list-content-text">
						查看商家详细资质信息
					</view>
				</view>
			</view>
		</scroll-view>
		<view class="completebtn" @click="colseSafePopup">完成</view>
	</view>
</template>

<script>
	export default {
		name: "Safeguard",
		props: ['safeList'],
		data() {
			return {
				cursafeList:this.safeList,
				mysafeList: [{
					sgid: 1,
					sgname: '晚发即赔',
					sgcontent: '若发货超时或物流异常，将为你补贴至少3元无门槛优惠劵（特殊商品、大促活动及不可抗力因素除外）',
					sgicon: 'http://www.itlaobing.com/douyin/other/safeguard01.png'
				},
				{
					sgid: 2,
					sgname: '假一赔四',
					sgcontent: '若商品鉴定为假货，商家支持假一赔四，且承担商品的鉴定费和退回的运费',
					sgicon: 'http://www.itlaobing.com/douyin/other/safeguard01.png'
				},
				{
					sgid: 3,
					sgname: '坏损包退',
					sgcontent: '签收后72小时内发现商品破损、腐烂、变质 (食品酒饮、奶粉、生鲜绿植为24小时)，申请退款，商家将在24小时内处理',
					sgicon: 'http://www.itlaobing.com/douyin/other/safeguard02.png'
				},
				{
					sgid: 4,
					sgname: '运费险',
					sgcontent: '退货或换货，均可享受一定金额的运费补贴 (全球购商品不支持换货)',
					sgicon: 'http://www.itlaobing.com/douyin/other/safeguard03.png'
				},
				{
					sgid: 5,
					sgname: '极速退款',
					sgcontent: '满足相应条件时，待发货状态下，提交退款申请将免审核立即退款',
					sgicon: 'http://www.itlaobing.com/douyin/other/safeguard04.png'
				},
				{
					sgid: 6,
					sgname: '7天无理由退货',
					sgcontent: '7天无理由退货符合相应条件时，消费者可申请7天无理由退货',
					sgicon: 'http://www.itlaobing.com/douyin/other/safeguard05.png'
				},
				{
					sgid: 7,
					sgname: '消费者保障服务',
					sgcontent: '如商品有质量问题、描述不符等情况，你有权申请退货退款',
					sgicon: 'http://www.itlaobing.com/douyin/other/safeguard06.png'
				}]
			};
		},
		mounted() {
			//加载保障数据
		},
		methods:{
			colseSafePopup(){
				this.$emit('colseSafePopup',true)
			}
		}
	}
</script>

<style lang="scss">
	.container {
		width: 100vw;
		height: 85vh;
		background-color: #FFF;
		border-radius: 15rpx 15px 0 0;
		box-sizing: border-box;

		.top {
			width: 100%;
			height: 100rpx;
			background-image: url(http://www.itlaobing.com/douyin/other/safetitle.png);
		}
		.safe-list-parent{
			width: 720rpx;
			height: 80%;
			
			.safe-list{
				display: flex;
				align-items: center;
				width: 96%;
				height: 180rpx;
				
				.safe-list-sgicon{
					width: 120rpx;
					height: 120rpx;
					text-align: center;
					// 空间不够时，不缩小
					flex-shrink: 0;
					
					.safe-list-img{
						width: 100rpx;
						height: 100rpx;
					}
				}
				.safe-list-content{
					flex-grow: 1;
					.safe-list-content-title{
						font-size: 28rpx;
						font-weight: bold;
						padding-bottom: 10rpx;
					}
					.safe-list-content-text{
						
					}
				}
			}
		}
	
		.completebtn{
			width: 720rpx;
			height: 70rpx;
			background-color: #FC2B55;
			color: #FFF;
			text-align: center;
			border-radius: 15rpx;
			font-weight: bold;
			line-height: 70rpx;
			font-size: 32rpx;
			margin-top: 20rpx;
		}
	}
</style>
