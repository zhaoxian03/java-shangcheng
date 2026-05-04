<template>
	<view class="evaluate-container">
		<!-- 自定义加载遮罩（替代uni.showLoading） -->
		<view class="loading-mask" v-if="isLoading">
			<uni-icons type="spinner" size="40" color="#fff" class="loading-icon"></uni-icons>
			<text class="loading-text">提交中...</text>
		</view>

		<!-- 头部 -->
		<view class="header">
			<uni-icons type="back" size="24" color="#999" @click="back"></uni-icons>
			<text class="title">{{isAppend ? '追加评价' : '评价商品'}}</text>
		</view>

		<!-- 订单商品列表 -->
		<view class="goods-list">
			<view class="goods-item" v-for="(goods, index) in order.ordersDetail" :key="index">
				<image :src="goods.icon" class="goods-img"></image>
				<view class="goods-info">
					<text class="goods-name">{{goods.gname}}</text>
					<text class="goods-spec">{{goods.bcontent || '无规格'}}</text>
				</view>
			</view>
		</view>

		<!-- 评分 -->
		<view class="score-section">
			<text class="score-title">商品评分：</text>
			<view class="stars">
				<uni-icons 
					type="star-filled" 
					size="40" 
					:color="index < score ? '#FFCC00' : '#EEEEEE'" 
					v-for="(item, index) in 5" 
					:key="index"
					@click="setScore(index + 1)"
				></uni-icons>
			</view>
		</view>

		<!-- 评价内容 -->
		<view class="content-section">
			<text class="content-title">评价内容：</text>
			<textarea 
				v-model="evaluateContent" 
				placeholder="请输入你的评价内容（选填）"
				class="content-input"
			></textarea>
		</view>
		
		<!-- 评价图片 -->
		<view class="image-section">
			<text class="image-title">评价图片：</text>
			<view class="image-uploader">
				<!-- 已选择的图片 -->
				<view class="image-item" v-for="(image, index) in evaluateImages" :key="index">
					<image :src="image" class="uploaded-image" @click="previewImage(index)"></image>
					<view class="image-delete" @click="deleteImage(index)">
						<uni-icons type="close" size="20" color="#fff"></uni-icons>
					</view>
				</view>
				
				<!-- 选择图片按钮 -->
				<view class="upload-btn" @click="chooseImage" v-if="evaluateImages.length < 9">
					<uni-icons type="plus" size="40" color="#999"></uni-icons>
					<text class="upload-text">添加图片</text>
				</view>
			</view>
		</view>

		<!-- 提交按钮 -->
		<view class="submit-btn" @click="submitEvaluate" :class="{disabled: isLoading}" :disabled="isLoading">
			{{isAppend ? '提交追加评价' : '提交评价'}}
		</view>
	</view>
</template>

<script>
	const app = getApp()
	export default {
		data() {
			return {
			order: {}, // 订单信息
			isAppend: false, // 是否追加评价
			score: 5, // 默认5星
			evaluateContent: '', // 评价内容
			evaluateImages: [], // 评价图片数组
			evaluate: {
				orderId: '', // 订单ID
				cid: '', // 用户ID
				sid: '', // 店铺ID
				gid: '', // 商品ID（多商品时可传数组）
				gdid: '', // 商品详情ID
				etype: '', // 评价类型
				pic: null, // 评价图片
				score: 5, // 评分
				content: '', // 评价内容
				isAppend: false // 是否追加评价
			},
			isLoading: false // 加载状态标记（自定义）
		}
		},
		onLoad(options) {
			// 解析传递的参数（修复URL解码+异常捕获）
			try {
				this.order = JSON.parse(decodeURIComponent(options.order))
				this.isAppend = options.isAppend === 'true'
				
				// 调试信息：查看订单详情结构
				console.log('订单详情:', this.order.ordersDetail[0])
				
				// 初始化评价参数
				this.evaluate.orderId = this.order.orderid
				this.evaluate.cid = app.globalData.cid || 1 // 兜底默认值
				this.evaluate.sid = this.order.sid
				this.evaluate.gid = this.order.ordersDetail[0].gid // 单商品，多商品需调整
				// 确保gdid不为null，使用默认值0
				this.evaluate.gdid = Number(this.order.ordersDetail[0].gdid) || 0 // 商品详情ID，转为数字类型，默认值0
				this.evaluate.etype = this.score.toString() // 使用评分为评价类型
				// 设置pic为默认值，避免null值
				this.evaluate.pic = '' // 暂无图片上传功能，设置为空字符串
				// 设置evidp默认值
				this.evaluate.evidp = 0 // 父评价ID，默认值0
				this.evaluate.isAppend = this.isAppend
			} catch (e) {
				console.error('参数解析失败：', e)
				uni.showToast({
					title: '参数解析失败',
					icon: 'none'
				})
				setTimeout(() => {
					this.back()
				}, 1500)
			}
		},
		methods: {
			/**
			 * 返回上一页
			 */
			back() {
				// 即使加载中也允许返回，重置加载状态
				this.isLoading = false
				uni.navigateBack()
			},

			/**
			 * 设置评分
			 * @param {Number} score 评分
			 */
			setScore(score) {
				this.score = score
				this.evaluate.score = score
			},

			/**
			 * 选择图片
			 */
			chooseImage() {
				uni.chooseImage({
					count: 9 - this.evaluateImages.length, // 最多可选择的图片数量
					sizeType: ['compressed'], // 压缩图
					sourceType: ['album', 'camera'], // 相册和相机
					success: (res) => {
						// 将选择的图片添加到评价图片数组
						this.evaluateImages = [...this.evaluateImages, ...res.tempFilePaths]
					}
				})
			},
			
			/**
			 * 预览图片
			 */
			previewImage(index) {
				uni.previewImage({
					current: index,
					urls: this.evaluateImages
				})
			},
			
			/**
			 * 删除图片
			 */
			deleteImage(index) {
				this.evaluateImages.splice(index, 1)
			},
			
			/**
			 * 上传单张图片
			 */
			uploadImage(imagePath) {
				return new Promise((resolve, reject) => {
					uni.uploadFile({
						url: app.globalData.baseUrl + 'api/file/test-upload',
						filePath: imagePath,
						name: 'file',
						success: (res) => {
							try {
								const data = JSON.parse(res.data)
								if (data.code === 200) {
									// 测试接口返回的是文件名等信息，这里模拟返回图片URL
									// 实际项目中应该使用真实的图片上传接口
									const mockImageUrl = app.globalData.baseUrl + '/uploads/' + data.data.fileName;
									resolve(mockImageUrl)
								} else {
									reject(new Error(data.msg || '图片上传失败'))
								}
							} catch (e) {
								// 如果上传失败，直接返回本地图片路径用于预览
								console.error('图片上传失败，使用本地路径:', e);
								resolve(imagePath)
							}
						},
						fail: (err) => {
							// 上传失败时，使用本地图片路径用于预览
							console.error('图片上传失败，使用本地路径:', err);
							resolve(imagePath)
						}
					})
				})
			},
			
			/**
			 * 批量上传图片
			 */
			async uploadImages() {
				const uploadedUrls = []
				
				// 上传每张图片
				for (const imagePath of this.evaluateImages) {
					try {
						const url = await this.uploadImage(imagePath)
						uploadedUrls.push(url)
					} catch (err) {
						console.error('上传图片失败:', err)
						// 继续上传其他图片，不中断
					}
				}
				
				return uploadedUrls
			},
			
			/**
			 * 提交评价（完全抛弃uni.showLoading，使用自定义加载）
			 */
			submitEvaluate() {
				// 防止重复点击
				if (this.isLoading) return
				
				if (!this.score) {
					uni.showToast({
						title: '请选择评分',
						icon: 'none'
					})
					return
				}

				// 标记加载状态（显示自定义遮罩）
				this.isLoading = true

				try {
					// 先上传图片
					this.uploadImages().then(uploadedUrls => {
						// 填充评价内容
						this.evaluate.content = this.evaluateContent
						// 设置评价图片，用分号分隔
						this.evaluate.pic = uploadedUrls.join(';')

						// 封装请求为Promise
						const submitRequest = () => {
							return new Promise((resolve, reject) => {
								uni.request({
									url: app.globalData.baseUrl + 'api/evaluate/add',
									method: 'POST',
									data: this.evaluate,
									header: {
										'Content-Type': 'application/json' // 明确请求头，解决跨域预检问题
									},
									success: resolve,
									fail: reject
								})
							})
						}

						// 执行请求并处理结果
						console.log('提交的评价数据:', this.evaluate)
						submitRequest()
							.then(res => {
								console.log('评价提交响应:', res.data)
								if (res.data && res.data.code === 200) {
									uni.showToast({
										title: res.data.msg || '评价提交成功',
										icon: 'success'
									})
									setTimeout(() => {
										this.back()
									}, 1500)
								} else {
									uni.showToast({
										title: res.data?.msg || '评价提交失败',
										icon: 'none'
									})
								}
							})
								.catch(err => {
									console.error('评价提交请求失败：', err)
									// 区分跨域/网络错误提示
									if (err.errMsg.includes('request:fail')) {
										uni.showToast({
											title: '网络错误或跨域配置异常',
											icon: 'none',
											duration: 3000
										})
									} else {
										uni.showToast({
											title: '提交失败，请重试',
											icon: 'none'
										})
									}
								})
								.finally(() => {
									// 无论任何情况，最终都会关闭加载状态
									this.isLoading = false
								})
					})
				} catch (err) {
					console.error('评价提交异常:', err)
					uni.showToast({
						title: '提交失败，请重试',
						icon: 'none'
					})
					this.isLoading = false
				}
			}
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #f5f5f5;
	}

	.evaluate-container {
		padding: 0 20rpx;
		background-color: #fff;
		min-height: 100vh;
		position: relative; // 为自定义加载遮罩提供定位父级
	}

	// 自定义加载遮罩样式（替代uni.showLoading）
	.loading-mask {
		position: fixed;
		top: 0;
		left: 0;
		width: 100vw;
		height: 100vh;
		background-color: rgba(0, 0, 0, 0.5);
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		z-index: 9999;

		.loading-icon {
			animation: spin 1s linear infinite;
			margin-bottom: 20rpx;
		}

		.loading-text {
			font-size: 28rpx;
			color: #fff;
		}
	}

	// 加载动画
	@keyframes spin {
		0% {
			transform: rotate(0deg);
		}
		100% {
			transform: rotate(360deg);
		}
	}

	.header {
		display: flex;
		align-items: center;
		height: 88rpx;
		border-bottom: 1rpx solid #f5f5f5;

		.title {
			font-size: 32rpx;
			font-weight: bold;
			margin-left: 20rpx;
		}
	}

	.goods-list {
		padding: 20rpx 0;
		border-bottom: 1rpx solid #f5f5f5;

		.goods-item {
			display: flex;
			align-items: center;
			margin-bottom: 20rpx;

			&:last-child {
				margin-bottom: 0;
			}

			.goods-img {
				width: 120rpx;
				height: 120rpx;
				border-radius: 10rpx;
			}

			.goods-info {
				margin-left: 20rpx;

				.goods-name {
					font-size: 28rpx;
					color: #333;
					display: -webkit-box;
					-webkit-line-clamp: 1;
					-webkit-box-orient: vertical;
					overflow: hidden;
				}

				.goods-spec {
					font-size: 24rpx;
					color: #999;
					margin-top: 10rpx;
				}
			}
		}
	}

	.score-section {
		padding: 30rpx 0;
		border-bottom: 1rpx solid #f5f5f5;

		.score-title {
			font-size: 30rpx;
			color: #333;
			margin-bottom: 20rpx;
			display: block;
		}

		.stars {
			display: flex;
			gap: 10rpx;
		}
	}

	.content-section {
		padding: 30rpx 0;

		.content-title {
			font-size: 30rpx;
			color: #333;
			margin-bottom: 20rpx;
			display: block;
		}

		.content-input {
			width: 100%;
			height: 200rpx;
			border: 1rpx solid #e5e5e5;
			border-radius: 10rpx;
			padding: 20rpx;
			box-sizing: border-box;
			font-size: 28rpx;
			color: #333;
		}
	}
	
	/* 评价图片样式 */
	.image-section {
		padding: 30rpx 0;
		border-top: 1rpx solid #f5f5f5;

		.image-title {
			font-size: 30rpx;
			color: #333;
			margin-bottom: 20rpx;
			display: block;
		}
	}
	
	.image-uploader {
		display: flex;
		flex-wrap: wrap;
		gap: 20rpx;
	}
	
	.image-item {
		position: relative;
		width: 160rpx;
		height: 160rpx;
		border-radius: 10rpx;
		overflow: hidden;
	}
	
	.uploaded-image {
		width: 100%;
		height: 100%;
		object-fit: cover;
	}
	
	.image-delete {
		position: absolute;
		top: 8rpx;
		right: 8rpx;
		width: 32rpx;
		height: 32rpx;
		background-color: rgba(0, 0, 0, 0.5);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.upload-btn {
		width: 160rpx;
		height: 160rpx;
		border: 2rpx dashed #e5e5e5;
		border-radius: 10rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		background-color: #fafafa;
	}
	
	.upload-text {
		font-size: 24rpx;
		color: #999;
		margin-top: 10rpx;
	}

	.submit-btn {
		width: 100%;
		height: 88rpx;
		line-height: 88rpx;
		text-align: center;
		background-color: #F36C8A;
		color: #fff;
		font-size: 32rpx;
		border-radius: 10rpx;
		margin: 40rpx 0;
		transition: background-color 0.3s;

		// 禁用状态样式
		&.disabled {
			background-color: #ccc;
			color: #999;
			pointer-events: none; // 禁止点击
		}
	}
</style>