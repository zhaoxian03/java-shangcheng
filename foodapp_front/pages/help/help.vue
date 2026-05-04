<template>
	<view class="help-page">
		<!-- 帮助列表 -->
		<view class="help-list">
			<!-- 发货时间 -->
			<view class="help-section">
				<view class="section-title">发货时间</view>
				<view class="question-item" @click="toggleQuestion(1)">
					<view class="question-header">
						<text class="question-text">商品什么时候发货？</text>
						<uni-icons 
							:type="activeQuestion === 1 ? 'top' : 'bottom'" 
							size="16" 
							color="#999"
						></uni-icons>
					</view>
					<view class="answer-content" v-if="activeQuestion === 1">
						<text class="answer-text">一般情况下，订单会在您支付成功后24小时内发货（周末和节假日顺延）。发货后您会收到短信通知，也可以在"我的订单"中查看物流信息。</text>
					</view>
				</view>
				
				<view class="question-item" @click="toggleQuestion(2)">
					<view class="question-header">
						<text class="question-text">商家一直不发货怎么办？</text>
						<uni-icons 
							:type="activeQuestion === 2 ? 'top' : 'bottom'" 
							size="16" 
							color="#999"
						></uni-icons>
					</view>
					<view class="answer-content" v-if="activeQuestion === 2">
						<text class="answer-text">如果商家超过承诺的发货时间仍未发货，您可以：</text>
						<view class="solution-item">
							<text class="solution-title">1. 联系商家客服</text>
							<text class="solution-desc">在订单详情页点击"联系商家"按钮，直接与商家沟通发货事宜。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">2. 申请催发货</text>
							<text class="solution-desc">在订单详情页点击"催发货"按钮，系统会提醒商家尽快处理。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">3. 申请退款</text>
							<text class="solution-desc">如果长时间未发货，您可以在订单详情页申请退款，退款会在1-3个工作日内原路退回。</text>
						</view>
					</view>
				</view>
			</view>

			<!-- 快递物流 -->
			<view class="help-section">
				<view class="section-title">快递物流</view>
				<view class="question-item" @click="toggleQuestion(3)">
					<view class="question-header">
						<text class="question-text">可以送货上门吗？</text>
						<uni-icons 
							:type="activeQuestion === 3 ? 'top' : 'bottom'" 
							size="16" 
							color="#999"
						></uni-icons>
					</view>
					<view class="answer-content" v-if="activeQuestion === 3">
						<text class="answer-text">是的，我们支持送货上门服务。具体服务范围取决于您的收货地址和选择的快递公司。</text>
						<view class="solution-item">
							<text class="solution-title">城市地区</text>
							<text class="solution-desc">大部分城市地区支持送货上门，快递员会提前电话联系您。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">乡镇农村</text>
							<text class="solution-desc">部分乡镇和农村地区可能需到指定快递点自提，具体以快递公司服务为准。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">特殊区域</text>
							<text class="solution-desc">学校、单位、小区等可能有特定配送要求，请保持电话畅通以便联系。</text>
						</view>
					</view>
				</view>
				
				<view class="question-item" @click="toggleQuestion(4)">
					<view class="question-header">
						<text class="question-text">什么时候能收到商品？</text>
						<uni-icons 
							:type="activeQuestion === 4 ? 'top' : 'bottom'" 
							size="16" 
							color="#999"
						></uni-icons>
					</view>
					<view class="answer-content" v-if="activeQuestion === 4">
						<text class="answer-text">收货时间取决于发货地和收货地距离：</text>
						<view class="solution-item">
							<text class="solution-title">同城配送</text>
							<text class="solution-desc">一般1-2天送达</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">省内配送</text>
							<text class="solution-desc">一般2-3天送达</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">省外配送</text>
							<text class="solution-desc">一般3-5天送达</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">偏远地区</text>
							<text class="solution-desc">可能需要5-7天，具体以物流信息为准</text>
						</view>
					</view>
				</view>
			</view>

			<!-- 商品问题 -->
			<view class="help-section">
				<view class="section-title">商品问题</view>
				<view class="question-item" @click="toggleQuestion(5)">
					<view class="question-header">
						<text class="question-text">哪里可以查看我的订单？</text>
						<uni-icons 
							:type="activeQuestion === 5 ? 'top' : 'bottom'" 
							size="16" 
							color="#999"
						></uni-icons>
					</view>
					<view class="answer-content" v-if="activeQuestion === 5">
						<text class="answer-text">您可以通过以下方式查看订单：</text>
						<view class="solution-item">
							<text class="solution-title">1. 在APP中查看</text>
							<text class="solution-desc">点击底部导航栏"我的"，进入"我的订单"页面即可查看所有订单。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">2. 订单状态说明</text>
							<text class="solution-desc">订单状态分为：待付款、待发货、待收货、已完成、已取消等。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">3. 订单详情</text>
							<text class="solution-desc">点击任意订单可查看详细内容，包括商品信息、物流跟踪等。</text>
						</view>
					</view>
				</view>
				
				<view class="question-item" @click="toggleQuestion(6)">
					<view class="question-header">
						<text class="question-text">没收到货怎么办？</text>
						<uni-icons 
							:type="activeQuestion === 6 ? 'top' : 'bottom'" 
							size="16" 
							color="#999"
						></uni-icons>
					</view>
					<view class="answer-content" v-if="activeQuestion === 6">
						<text class="answer-text">如果您长时间未收到货，请按以下步骤处理：</text>
						<view class="solution-item">
							<text class="solution-title">第一步：查看物流信息</text>
							<text class="solution-desc">在订单详情页查看最新物流信息，确认配送状态。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">第二步：联系快递员</text>
							<text class="solution-desc">根据物流信息中的快递员电话进行联系。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">第三步：联系客服</text>
							<text class="solution-desc">如果无法联系到快递员或解决问题，请及时联系平台客服。</text>
						</view>
					</view>
				</view>
			</view>

			<!-- 运费问题 -->
			<view class="help-section">
				<view class="section-title">运费问题</view>
				<view class="question-item" @click="toggleQuestion(7)">
					<view class="question-header">
						<text class="question-text">退换货运费谁承担？</text>
						<uni-icons 
							:type="activeQuestion === 7 ? 'top' : 'bottom'" 
							size="16" 
							color="#999"
						></uni-icons>
					</view>
					<view class="answer-content" v-if="activeQuestion === 7">
						<text class="answer-text">运费承担规则如下：</text>
						<view class="solution-item">
							<text class="solution-title">质量问题退换货</text>
							<text class="solution-desc">由于商品质量问题导致的退换货，运费由商家承担。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">7天无理由退换货</text>
							<text class="solution-desc">非质量问题的7天无理由退换货，运费由买家承担。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">发错货/漏发货</text>
							<text class="solution-desc">商家发错商品或漏发商品，运费由商家承担。</text>
						</view>
					</view>
				</view>
				
				<view class="question-item" @click="toggleQuestion(8)">
					<view class="question-header">
						<text class="question-text">运费什么时候退？</text>
						<uni-icons 
							:type="activeQuestion === 8 ? 'top' : 'bottom'" 
							size="16" 
							color="#999"
						></uni-icons>
					</view>
					<view class="answer-content" v-if="activeQuestion === 8">
						<text class="answer-text">运费退款时间根据具体情况而定：</text>
						<view class="solution-item">
							<text class="solution-title">退款到账时间</text>
							<text class="solution-desc">运费退款一般会在商家确认收货后1-3个工作日内到账。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">原路退回</text>
							<text class="solution-desc">运费将原路退回至您支付时使用的账户或支付方式。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">查看退款进度</text>
							<text class="solution-desc">您可以在"我的订单"-"退款/售后"中查看运费退款进度。</text>
						</view>
					</view>
				</view>
			</view>

			<!-- 付款问题 -->
			<view class="help-section">
				<view class="section-title">付款问题</view>
				<view class="question-item" @click="toggleQuestion(9)">
					<view class="question-header">
						<text class="question-text">下单后如何支付？</text>
						<uni-icons 
							:type="activeQuestion === 9 ? 'top' : 'bottom'" 
							size="16" 
							color="#999"
						></uni-icons>
					</view>
					<view class="answer-content" v-if="activeQuestion === 9">
						<text class="answer-text">下单成功后，您可以通过以下方式支付：</text>
						<view class="solution-item">
							<text class="solution-title">1. 立即支付</text>
							<text class="solution-desc">下单后直接进入支付页面，选择支付方式完成支付。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">2. 订单列表支付</text>
							<text class="solution-desc">在"我的订单"中找到待付款订单，点击"立即支付"。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">3. 支付方式</text>
							<text class="solution-desc">支持微信支付、支付宝、银行卡等多种支付方式。</text>
						</view>
					</view>
				</view>
				
				<view class="question-item" @click="toggleQuestion(10)">
					<view class="question-header">
						<text class="question-text">支付失败怎么办？</text>
						<uni-icons 
							:type="activeQuestion === 10 ? 'top' : 'bottom'" 
							size="16" 
							color="#999"
						></uni-icons>
					</view>
					<view class="answer-content" v-if="activeQuestion === 10">
						<text class="answer-text">如果支付失败，请尝试以下解决方法：</text>
						<view class="solution-item">
							<text class="solution-title">检查网络连接</text>
							<text class="solution-desc">确保网络连接正常，尝试切换WiFi/移动数据。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">检查支付账户</text>
							<text class="solution-desc">确认支付账户余额充足，或更换其他支付方式。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">重新支付</text>
							<text class="solution-desc">返回订单页面重新发起支付。</text>
						</view>
						<view class="solution-item">
							<text class="solution-title">联系客服</text>
							<text class="solution-desc">如多次支付失败，请联系客服协助处理。</text>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			// 当前展开的问题ID
			activeQuestion: 0
		};
	},
	
	methods: {
		// 切换问题展开状态
		toggleQuestion(id) {
			if (this.activeQuestion === id) {
				// 如果点击的是已经展开的问题，则关闭
				this.activeQuestion = 0;
			} else {
				// 否则展开新的问题
				this.activeQuestion = id;
			}
		}
	}
};
</script>

<style lang="scss" scoped>
.help-page {
	min-height: 100vh;
	background-color: #f5f5f5;
	padding-bottom: 120rpx;
}

.help-list {
	padding: 0 30rpx;
}

.help-section {
	background-color: #fff;
	border-radius: 16rpx;
	margin-bottom: 30rpx;
	overflow: hidden;
	box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
	
	.section-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
		padding: 30rpx;
		border-bottom: 2rpx solid #f0f0f0;
		background-color: #f9f9f9;
	}
}

.question-item {
	border-bottom: 2rpx solid #f0f0f0;
	
	&:last-child {
		border-bottom: none;
	}
	
	.question-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 30rpx;
		
		.question-text {
			flex: 1;
			font-size: 30rpx;
			color: #333;
			line-height: 1.4;
			margin-right: 20rpx;
		}
		
		&:active {
			background-color: #f9f9f9;
		}
	}
	
	.answer-content {
		padding: 0 30rpx 30rpx;
		background-color: #f9f9f9;
		
		.answer-text {
			display: block;
			font-size: 28rpx;
			color: #666;
			line-height: 1.6;
			margin-bottom: 20rpx;
		}
	}
}

.solution-item {
	margin-top: 20rpx;
	padding: 20rpx;
	background-color: #fff;
	border-radius: 8rpx;
	border-left: 4rpx solid #4a90e2;
	
	.solution-title {
		display: block;
		font-size: 28rpx;
		color: #333;
		font-weight: 500;
		margin-bottom: 8rpx;
	}
	
	.solution-desc {
		display: block;
		font-size: 26rpx;
		color: #666;
		line-height: 1.5;
	}
}
</style>