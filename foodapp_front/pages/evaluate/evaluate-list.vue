<template>
	<view class="evaluate-list-container">
		<!-- 头部 -->
		<view class="header">
			<uni-icons type="back" size="24" color="#999" @click="back"></uni-icons>
			<text class="title">商品评价</text>
		</view>

		<!-- 评价统计 -->
		<view class="evaluate-stats">
			<view class="stats-total">
				<text class="total-count">{{ totalCount }}</text>
				<text class="total-label">条评价</text>
			</view>
			<view class="stats-score">
				<text class="score-value">{{ scoreValue }}</text>
				<view class="stars">
					<uni-icons type="star-filled" size="24" color="#FFCC00" v-for="(item, index) in 5" :key="index"></uni-icons>
				</view>
			</view>
		</view>

		<!-- 评价类型筛选 -->
		<view class="evaluate-filter">
			<view class="filter-item" :class="{ active: activeFilter === 'all' }" @click="setFilter('all')">
				<text>全部</text>
			</view>
			<view class="filter-item" :class="{ active: activeFilter === 'image' }" @click="setFilter('image')">
				<text>有图</text>
			</view>
			<view class="filter-item" :class="{ active: activeFilter === 'good' }" @click="setFilter('good')">
				<text>好评</text>
			</view>
			<view class="filter-item" :class="{ active: activeFilter === 'medium' }" @click="setFilter('medium')">
				<text>中评</text>
			</view>
			<view class="filter-item" :class="{ active: activeFilter === 'bad' }" @click="setFilter('bad')">
				<text>差评</text>
			</view>
		</view>

		<!-- 评价列表 -->
		<view class="evaluate-list">
			<view class="evaluate-item" v-for="(item, index) in evaluateList" :key="index">
				<view class="evaluate-header">
					<image :src="item.hearderimg" v-if="item.hearderimg && item.hearderimg !== ''" class="user-avatar"></image>
					<view v-else class="user-avatar placeholder">
						<uni-icons type="person" size="24" color="#999"></uni-icons>
					</view>
					<view class="user-info">
						<text class="user-name">{{ item.douyinCode }}</text>
						<view class="user-tags">
							<view class="tag back-customer" v-if="item.isBackCustomer">回头客</view>
						</view>
					</view>
					<view class="evaluate-time">{{ item.createTime || '2023-01-01' }}</view>
				</view>
				<view class="evaluate-content">
					<text>{{ item.econtent }}</text>
				</view>
				<view class="evaluate-images" v-if="item.epics && item.epics.length > 0">
					<image :src="img" class="evaluate-image" v-for="(img, imgIndex) in item.epics" :key="imgIndex"></image>
				</view>
			</view>
		</view>

		<!-- 加载更多 -->
		<view class="load-more" v-if="hasMore">
			<text @click="loadMore">加载更多</text>
		</view>
		<view class="no-more" v-else>
			<text>没有更多评价了</text>
		</view>
	</view>
</template>

<script>
const app = getApp()

export default {
	data() {
		return {
			goodsId: '', // 商品ID
			totalCount: 0, // 评价总数
			scoreValue: 4.9, // 商品评分
			evaluateList: [], // 评价列表
			activeFilter: 'all', // 当前筛选类型
			page: 1, // 当前页码
			pageSize: 10, // 每页条数
			hasMore: true // 是否还有更多数据
		}
	},
	onLoad(options) {
		// 获取商品ID和评价总数
		this.goodsId = options.gid || ''
		this.totalCount = parseInt(options.count || '0')
		// 加载评价数据
		this.loadEvaluateList()
	},
	methods: {
		// 返回上一页
		back() {
			uni.navigateBack()
		},
		// 设置筛选条件
		setFilter(filter) {
			this.activeFilter = filter
			this.page = 1
			this.evaluateList = []
			this.hasMore = true
			this.loadEvaluateList()
		},
		// 加载评价列表
		loadEvaluateList() {
			// 从获取商品详情接口获取评价数据
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token')
			uni.request({
				url: app.globalData.baseUrl + 'api/goods/getcurgoodsinfo/' + this.goodsId,
				method: 'GET',
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				success: (res) => {
					if (res.data.code === 200) {
						const goodsData = res.data.data
						// 获取评价数据
						let evaluateList = goodsData.evaluateList || []
						
						// 根据筛选类型过滤评价
						if (this.activeFilter === 'image') {
							evaluateList = evaluateList.filter(item => item.pic && item.pic.trim() !== '')
						} else if (this.activeFilter === 'good') {
							// 假设评分大于等于4为好评
							evaluateList = evaluateList.filter(item => item.score >= 4)
						} else if (this.activeFilter === 'medium') {
							// 假设评分大于等于3且小于4为中评
							evaluateList = evaluateList.filter(item => item.score >= 3 && item.score < 4)
						} else if (this.activeFilter === 'bad') {
							// 假设评分小于3为差评
							evaluateList = evaluateList.filter(item => item.score < 3)
						}
						
						// 转换评价数据格式，使其与组件兼容
						const formattedEvaluateList = evaluateList.map(item => {
							return {
								douyinCode: '**' + Math.floor(Math.random() * 100), // 生成随机用户代码
								hearderimg: '', // 头像，这里可以设置默认头像
								isBackCustomer: item.etype && item.etype.includes('1'), // 1表示回头客
								econtent: item.content || '好评', // 评价内容
								epics: item.pic ? item.pic.split(';').filter(url => url.trim()) : [], // 评价图片
								createTime: item.createTime || '' // 评价时间
							}
						})
						
						// 分页处理
						const startIndex = (this.page - 1) * this.pageSize
						const endIndex = startIndex + this.pageSize
						const paginatedList = formattedEvaluateList.slice(startIndex, endIndex)
						
						// 更新评价列表
						if (this.page === 1) {
							this.evaluateList = paginatedList
						} else {
							this.evaluateList = [...this.evaluateList, ...paginatedList]
						}
						
						// 更新是否还有更多数据
						this.hasMore = endIndex < formattedEvaluateList.length
						
						// 更新评价总数和评分
						this.totalCount = formattedEvaluateList.length
						if (goodsData.score) {
							this.scoreValue = goodsData.score
						}
						
						this.page++
					}
				},
				fail: (err) => {
					console.error('获取评价列表失败:', err)
				}
			})
		},
		// 加载更多
		loadMore() {
			if (this.hasMore) {
				this.loadEvaluateList()
			}
		}
	}
}
</script>

<style lang="scss">
.evaluate-list-container {
	padding: 0 20rpx;
	background-color: #f5f5f5;
	min-height: 100vh;
}

.header {
	display: flex;
	align-items: center;
	height: 88rpx;
	background-color: #fff;
	border-bottom: 1rpx solid #f5f5f5;
	padding: 0 20rpx;
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	z-index: 999;

	.title {
		font-size: 32rpx;
		font-weight: bold;
		margin-left: 20rpx;
	}
}

.evaluate-stats {
	display: flex;
	align-items: center;
	justify-content: space-between;
	background-color: #fff;
	padding: 30rpx 20rpx;
	margin-top: 88rpx;
	border-radius: 10rpx;
	margin-bottom: 20rpx;

	.stats-total {
		.total-count {
			font-size: 48rpx;
			font-weight: bold;
			color: #ff4757;
		}
		.total-label {
			font-size: 28rpx;
			color: #999;
			margin-left: 10rpx;
		}
	}

	.stats-score {
		.score-value {
			font-size: 48rpx;
			font-weight: bold;
			color: #ff4757;
		}
		.stars {
			margin-left: 20rpx;
			display: flex;
		}
	}
}

.evaluate-filter {
	display: flex;
	background-color: #fff;
	padding: 20rpx;
	border-radius: 10rpx;
	margin-bottom: 20rpx;
	overflow-x: auto;

	.filter-item {
		padding: 10rpx 30rpx;
		margin-right: 20rpx;
		border-radius: 20rpx;
		background-color: #f5f5f5;
		white-space: nowrap;
		cursor: pointer;
		transition: all 0.3s;

		&.active {
			background-color: #ff4757;
			color: #fff;
		}
	}
}

.evaluate-list {
	background-color: #fff;
	border-radius: 10rpx;
	margin-bottom: 20rpx;
}

.evaluate-item {
	padding: 30rpx 20rpx;
	border-bottom: 1rpx solid #f5f5f5;

	&:last-child {
		border-bottom: none;
	}
}

.evaluate-header {
	display: flex;
	align-items: center;
	margin-bottom: 20rpx;

	.user-avatar {
		width: 60rpx;
		height: 60rpx;
		border-radius: 30rpx;
		margin-right: 20rpx;

		&.placeholder {
			background-color: #f5f5f5;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}

	.user-info {
		flex: 1;

		.user-name {
			font-size: 28rpx;
			color: #333;
			margin-right: 20rpx;
		}

		.user-tags {
			display: flex;
			margin-top: 10rpx;

			.tag {
				padding: 5rpx 15rpx;
				border-radius: 15rpx;
				font-size: 22rpx;
				margin-right: 10rpx;

				&.back-customer {
					background-color: #FEF4EA;
					color: #BC712D;
				}
			}
		}
	}

	.evaluate-time {
		font-size: 24rpx;
		color: #999;
	}
}

.evaluate-content {
	font-size: 28rpx;
	color: #333;
	line-height: 40rpx;
	margin-bottom: 20rpx;
	word-break: break-all;
}

.evaluate-images {
	display: flex;
	flex-wrap: wrap;

	.evaluate-image {
		width: 160rpx;
		height: 160rpx;
		border-radius: 10rpx;
		margin-right: 10rpx;
		margin-bottom: 10rpx;

		&:nth-child(3n) {
			margin-right: 0;
		}
	}
}

.load-more {
	background-color: #fff;
	padding: 30rpx;
	text-align: center;
	border-radius: 10rpx;
	margin-bottom: 20rpx;
	cursor: pointer;

	text {
		font-size: 28rpx;
		color: #ff4757;
	}
}

.no-more {
	background-color: #fff;
	padding: 30rpx;
	text-align: center;
	border-radius: 10rpx;
	margin-bottom: 20rpx;

	text {
		font-size: 28rpx;
		color: #999;
	}
}
</style>