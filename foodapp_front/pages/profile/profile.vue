<template>
	<view class="profile-page">
		<!-- 内容区域 -->
		<view class="content">
			<view class="avatar-section" @click="changeAvatar">
				<image :src="getAvatarUrl(userInfo.avatar)" class="avatar-img" mode="aspectFill"></image>
				<text class="avatar-text">点击更换头像</text>
			</view>
			
			<!-- 信息列表 -->
			<view class="info-list">
				<view class="info-item">
					<text class="label">用户名</text>
					<view class="value-wrapper">
						<text class="value">{{ userInfo.name || userInfo.username || '未设置' }}</text>
					</view>
				</view>
				
				<view class="info-item">
					<text class="label">手机号</text>
					<view class="value-wrapper">
						<text class="value">{{ userInfo.phone || '未绑定' }}</text>
						<view v-if="userInfo.phone" class="verified-tag">已绑定</view>
					</view>
				</view>
				
				<view class="info-item">
					<text class="label">邮箱</text>
					<view class="value-wrapper">
						<text class="value">{{ userInfo.email || '未设置' }}</text>
					</view>
				</view>
				
				<view class="info-item">
					<text class="label">注册时间</text>
					<view class="value-wrapper">
						<text class="value">{{ formatRegisterTime(userInfo.registerTime || userInfo.createTime) || '--' }}</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { userApi } from '@/api/user.js';
	
	const app = getApp({
		allowDefault: true
	})
	
	export default {
		data() {
			return {
				userId: null,
				userToken: null,
				userInfo: {
					name: '',
					username: '',
					phone: '',
					email: '',
					registerTime: '',
					createTime: '',
					avatar: '',
					avatarUrl: ''
				},
				isUploading: false // 防止重复上传
			}
		},
		
		onLoad(options) {
			console.log('Profile页面加载，参数:', options);
			
			// 从参数获取userId
			if (options.userId) {
				this.userId = options.userId;
				console.log('获取到userId:', this.userId);
			}
			
			// 从参数获取token
			if (options.token) {
				this.userToken = options.token;
				console.log('获取到token:', this.userToken);
			}
			
			// 加载用户信息
			this.loadUserInfo();
		},
		
		onShow() {
			console.log('Profile页面显示');
			this.checkAndLoadUserInfo();
		},
		
		methods: {
			// 获取完整的头像URL
			getAvatarUrl(avatar) {
				const avatarValue = avatar || this.userInfo.avatarUrl;
				
				if (!avatarValue) {
					return '/static/other/moren_1.jpg';
				}
				
				// 如果是Base64格式，直接返回
				if (avatarValue.startsWith('data:image')) {
					return avatarValue;
				}
				
				// 如果是相对路径，补全为完整URL
				if (avatarValue.startsWith('/uploads/')) {
					return 'http://localhost:8089' + avatarValue;
				}
				
				// 如果是完整URL，直接返回
				if (avatarValue.startsWith('http')) {
					return avatarValue;
				}
				
				// 如果是相对路径，转换为完整路径
				if (avatarValue.startsWith('/') || avatarValue.startsWith('./')) {
					return avatarValue;
				}
				
				// 其他情况，返回默认头像
				return '/static/other/moren_1.jpg';
			},
			
			async loadUserInfo() {
				console.log('开始加载用户信息...');
				
				try {
					// 第一步：从本地存储加载
					this.loadFromLocalStorage();
					
					// 第二步：从全局数据加载
					this.loadFromGlobalData();
					
					// 第三步：从API获取最新数据
					await this.loadFromAPI();
					
				} catch (error) {
					console.error('加载用户信息失败:', error);
					
					if (!this.userInfo.name && !this.userInfo.username) {
						uni.showToast({
							title: '加载用户信息失败',
							icon: 'none',
							duration: 2000
						});
					}
				}
			},
			
			// 从本地存储加载
			loadFromLocalStorage() {
				console.log('从本地存储加载用户信息...');
				try {
					const userInfo = uni.getStorageSync('userInfo');
					const username = uni.getStorageSync('username');
					const userId = uni.getStorageSync('userId');
					const token = uni.getStorageSync('token');
					
					if (userInfo) {
						console.log('本地存储中找到userInfo:', userInfo);
						this.userInfo = {
							...this.userInfo,
							...userInfo
						};
					}
					
					if (username && !this.userInfo.name && !this.userInfo.username) {
						this.userInfo.username = username;
					}
					
					if (userId && !this.userId) {
						this.userId = userId;
					}
					
					if (token && !this.userToken) {
						this.userToken = token;
					}
					
				} catch (e) {
					console.error('读取本地存储失败:', e);
				}
			},
			
			// 从全局数据加载
			loadFromGlobalData() {
				console.log('从全局数据加载用户信息...');
				
				if (app.globalData && app.globalData.userInfo) {
					const globalInfo = app.globalData.userInfo;
					console.log('全局数据中的userInfo:', globalInfo);
					
					this.userInfo = {
						...this.userInfo,
						...globalInfo
					};
				}
				
				if (!this.userId && app.globalData.userId) {
					this.userId = app.globalData.userId;
				}
				
				if (!this.userToken && app.globalData.userToken) {
					this.userToken = app.globalData.userToken;
				}
			},
			
			// 从API加载最新数据
			async loadFromAPI() {
				if (!this.userToken) {
					console.warn('没有用户token，跳过API调用');
					return;
				}
				
				console.log('从API加载用户信息，token:', this.userToken);
				
				try {
					const result = await userApi.getProfile(this.userToken);
					console.log('API返回结果:', result);
					
					if (result && result.code === 200 && result.data) {
						const apiData = result.data;
						console.log('API返回的用户数据:', apiData);
						
						// 合并API返回的数据
						this.userInfo = {
							...this.userInfo,
							...apiData
						};
						
						// 从API数据中提取userId
						if (apiData.id && !this.userId) {
							this.userId = apiData.id;
						}
						if (apiData.userId && !this.userId) {
							this.userId = apiData.userId;
						}
						
						// 更新本地存储
						this.updateLocalStorage(apiData);
						
						// 更新全局数据
						this.updateGlobalData(apiData);
						
						console.log('用户信息更新成功:', this.userInfo);
					} else {
						console.warn('API返回数据格式异常:', result);
					}
					
				} catch (error) {
					console.error('API调用失败:', error);
					throw error;
				}
			},
			
			// 更新本地存储
			updateLocalStorage(data) {
				try {
					let storedInfo = uni.getStorageSync('userInfo') || {};
					
					storedInfo = {
						...storedInfo,
						...data
					};
					
					uni.setStorageSync('userInfo', storedInfo);
					
					if (this.userId) {
						uni.setStorageSync('userId', this.userId);
					}
					
					if (this.userToken) {
						uni.setStorageSync('token', this.userToken);
					}
					
					console.log('本地存储更新完成');
					
				} catch (e) {
					console.error('更新本地存储失败:', e);
				}
			},
			
			// 更新全局数据
			updateGlobalData(data) {
				if (!app.globalData.userInfo) {
					app.globalData.userInfo = {};
				}
				
				app.globalData.userInfo = {
					...app.globalData.userInfo,
					...data
				};
				
				if (this.userId) {
					app.globalData.userId = this.userId;
				}
				
				if (this.userToken) {
					app.globalData.userToken = this.userToken;
				}
				
				console.log('全局数据更新完成');
			},
			
			// 检查和加载用户信息
			checkAndLoadUserInfo() {
				const hasBasicInfo = this.userInfo.name || this.userInfo.username || this.userInfo.phone;
				
				if (!hasBasicInfo) {
					console.log('基本用户信息缺失，重新加载...');
					this.loadUserInfo();
				}
			},
			
			// 格式化注册时间
			formatRegisterTime(timeStr) {
				if (!timeStr) return '';
				
				try {
					let dateStr = timeStr;
					
					if (dateStr.includes('T')) {
						dateStr = dateStr.replace('T', ' ');
					}
					
					if (dateStr.includes('.')) {
						dateStr = dateStr.split('.')[0];
					}
					
					if (dateStr.includes('+')) {
						dateStr = dateStr.split('+')[0];
					}
					
					return dateStr;
					
				} catch (e) {
					console.error('格式化时间失败:', e);
					return timeStr;
				}
			},
			
			// 更换头像
			async changeAvatar() {
				// 防止重复点击
				if (this.isUploading) {
					uni.showToast({
						title: '正在上传中，请稍候...',
						icon: 'none',
						duration: 1500
					});
					return;
				}
				
				if (!this.userId || !this.userToken) {
					uni.showToast({
						title: '请先登录',
						icon: 'none',
						duration: 2000
					});
					return;
				}
				
				// #ifdef H5
				// H5端使用HTML5文件选择器
				console.log('🌐 H5端：使用HTML5文件选择器');
				
				const fileInput = document.createElement('input');
				fileInput.type = 'file';
				fileInput.accept = 'image/*';
				fileInput.style.display = 'none';
				
				fileInput.onchange = async (e) => {
					const file = e.target.files[0];
					if (!file) {
						document.body.removeChild(fileInput);
						return;
					}
					
					console.log('选择的文件:', file.name, file.size);
					
					// 检查文件大小（限制5MB）
					if (file.size > 5 * 1024 * 1024) {
						uni.showToast({
							title: '图片大小不能超过5MB',
							icon: 'none',
							duration: 3000
						});
						document.body.removeChild(fileInput);
						return;
					}
					
					this.isUploading = true;
					uni.showLoading({
						title: '上传中...',
						mask: true
					});
					
					try {
						// 转换为Base64（保持完整格式）
						const base64 = await new Promise((resolve, reject) => {
							const reader = new FileReader();
							reader.onload = (event) => {
								// ✅ 保持完整的Base64 URL
								const fullBase64 = event.target.result;
								console.log('完整Base64格式，前50字符:', fullBase64.substring(0, 50) + '...');
								resolve(fullBase64);
							};
							reader.onerror = (error) => {
								reject(new Error('读取文件失败: ' + error.message));
							};
							reader.readAsDataURL(file);
						});
						
						console.log('Base64转换完成，长度:', base64.length);
						
						// ✅ 调用上传方法
						await this.uploadAvatarBase64({
							userId: this.userId,
							base64Image: base64,  // 完整的Base64 URL
							fileName: file.name,
							token: this.userToken
						});
						
					} catch (error) {
						console.error('处理图片失败:', error);
						uni.showToast({
							title: '处理图片失败: ' + error.message,
							icon: 'none',
							duration: 3000
						});
					} finally {
						this.isUploading = false;
						uni.hideLoading();
						// 清理
						document.body.removeChild(fileInput);
					}
				};
				
				document.body.appendChild(fileInput);
				fileInput.click();
				// #endif
				
				// #ifndef H5
				// 非H5端（小程序/App）使用原版uni.chooseImage
				console.log('📱 非H5端：使用uni.chooseImage');
				
				this.isUploading = true;
				
				uni.chooseImage({
					count: 1,
					sizeType: ['compressed'],
					sourceType: ['album'],
					success: async (res) => {
						const tempFilePath = res.tempFilePaths[0];
						console.log('选择的图片路径:', tempFilePath);
						
						uni.showLoading({
							title: '上传中...',
							mask: true
						});
						
						try {
							// 调用上传方法
							await this.uploadAvatar({
								userId: this.userId,
								filePath: tempFilePath,
								token: this.userToken
							});
						} finally {
							this.isUploading = false;
							uni.hideLoading();
						}
					},
					fail: (err) => {
						this.isUploading = false;
						console.log('选择图片失败:', err);
						if (!err.errMsg.includes('cancel')) {
							uni.showToast({
								title: '选择图片失败',
								icon: 'none'
							});
						}
					}
				});
				// #endif
			},
			
			// 上传头像到服务器（用于非H5端）
			async uploadAvatar(data) {
				try {
					console.log('开始上传头像...');
					console.log('用户信息:', {
						userId: this.userId,
						token: this.userToken ? '已提供' : '未提供',
						userName: this.userInfo.name
					});
					
					// 调用上传接口
					const result = await userApi.uploadAvatar({
						userId: this.userId,
						filePath: data.filePath,
						token: this.userToken
					});
					
					console.log('头像上传结果:', result);
					
					await this.handleUploadResult(result);
					
				} catch (error) {
					console.error('头像上传失败:', error);
					
					// 显示错误信息
					uni.showToast({
						title: error.message || '上传失败，请重试',
						icon: 'none',
						duration: 2000
					});
				}
			},
			
			// 上传Base64头像（用于H5端）- 最终修复版
			async uploadAvatarBase64(params) {
				try {
					console.log('🚀 开始上传Base64头像...', params);
					
					// 验证必要参数
					if (!params.userId) {
						throw new Error('用户ID不能为空');
					}
					if (!params.base64Image) {
						throw new Error('头像数据不能为空');
					}
					
					// 添加详细调试信息
					const base64Image = params.base64Image;
					console.log('📊 调试信息:', {
						userId: params.userId,
						base64Length: base64Image.length,
						base64StartsWithDataImage: base64Image.startsWith('data:image'),
						hasToken: !!params.token,
						fileName: params.fileName,
						base64Preview: base64Image.substring(0, 100)
					});
					
					// ✅ 方法1：先尝试新接口
					let result;
					try {
						console.log('🔄 尝试使用新接口...');
						result = await userApi.uploadAvatarV2({
							userId: params.userId,
							base64Image: base64Image,
							fileName: params.fileName || `avatar_${params.userId}_${Date.now()}.jpg`,
							token: params.token || this.userToken
						});
						console.log('✅ 新接口调用成功');
					} catch (newApiError) {
						console.warn('新接口失败，尝试旧接口:', newApiError.message);
						
						// ✅ 方法2：使用旧接口（智能版）
						result = await userApi.uploadAvatarBase64({
							userId: params.userId,
							base64Image: base64Image,
							token: params.token || this.userToken
						});
						console.log('✅ 旧接口调用成功');
					}
					
					console.log('✅ 头像上传API响应:', result);
					
					// 处理上传结果
					await this.handleUploadResult(result);
					
				} catch (error) {
					console.error('❌ 头像上传失败详情:', {
						error,
						message: error.message,
						stack: error.stack,
						userId: params.userId
					});
					
					// 显示详细的错误信息
					let errorMessage = '上传失败';
					if (error.message.includes('头像数据不能为空')) {
						errorMessage = '图片数据为空，请重新选择';
					} else if (error.message.includes('Token无效') || error.message.includes('未授权')) {
						errorMessage = '登录已过期，请重新登录';
					} else if (error.message.includes('格式错误')) {
						errorMessage = '图片格式不支持，请选择JPEG或PNG格式';
					} else if (error.message.includes('太大') || error.message.includes('超过')) {
						errorMessage = '图片太大，请选择小于5MB的图片';
					} else if (error.message.includes('网络请求失败')) {
						errorMessage = '网络连接失败，请检查网络';
					} else {
						errorMessage = error.message || '上传失败，请重试';
					}
					
					uni.showToast({
						title: errorMessage,
						icon: 'none',
						duration: 3000
					});
					
					// 抛出错误以便上层处理
					throw error;
				}
			},
			
			// 处理上传结果（通用）- 增强版
			async handleUploadResult(result) {
				console.log('🔄 处理上传结果:', result);
				
				// ✅ 多种格式兼容处理
				let finalResult = result;
				
				// 如果返回的是boolean true
				if (result === true) {
					finalResult = {
						code: 200,
						message: '头像上传成功',
						data: {
							avatarUrl: `/uploads/avatars/${this.userId}_${Date.now()}.jpg`
						}
					};
				}
				// 如果只有avatarUrl字段
				else if (result && result.avatarUrl && !result.code) {
					finalResult = {
						code: 200,
						message: '头像上传成功',
						data: { avatarUrl: result.avatarUrl }
					};
				}
				
				if (finalResult.code === 200) {
					const avatarUrl = finalResult.data?.avatarUrl || finalResult.avatarUrl;
					
					if (!avatarUrl) {
						console.warn('服务器未返回明确的头像URL，使用默认路径');
						// 生成默认头像URL
						const defaultAvatarUrl = `/uploads/avatars/${this.userId}_${Date.now()}.jpg`;
						
						// 更新本地数据
						this.userInfo.avatar = defaultAvatarUrl;
						this.userInfo.avatarUrl = defaultAvatarUrl;
						
						// 更新本地存储
						this.updateLocalStorage({ 
							avatar: defaultAvatarUrl,
							avatarUrl: defaultAvatarUrl
						});
						
						// 更新全局数据
						this.updateGlobalData({ 
							avatar: defaultAvatarUrl,
							avatarUrl: defaultAvatarUrl
						});
					} else {
						console.log('✅ 服务器返回的头像URL:', avatarUrl);
						
						// 更新本地数据
						this.userInfo.avatar = avatarUrl;
						this.userInfo.avatarUrl = avatarUrl;
						
						// 更新本地存储
						this.updateLocalStorage({ 
							avatar: avatarUrl,
							avatarUrl: avatarUrl
						});
						
						// 更新全局数据
						this.updateGlobalData({ 
							avatar: avatarUrl,
							avatarUrl: avatarUrl
						});
					}
					
					// 发送更新事件
					uni.$emit('avatarUpdated', {
						avatar: this.userInfo.avatar,
						userId: this.userId
					});
					
					// 延迟一点显示成功提示，让头像有更新效果
					setTimeout(() => {
						uni.showToast({
							title: '头像更新成功',
							icon: 'success',
							duration: 1500
						});
					}, 300);
					
				} else {
					throw new Error(finalResult.message || '头像上传失败');
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
.profile-page {
	min-height: 100vh;
	background-color: #f5f5f5;
	
	.content {
		.avatar-section {
			display: flex;
			flex-direction: column;
			align-items: center;
			padding: 60rpx 0 40rpx;
			background-color: #fff;
			margin-bottom: 20rpx;
			
			.avatar-img {
				width: 160rpx;
				height: 160rpx;
				border-radius: 50%;
				border: 4rpx solid #f0f0f0;
				background-color: #f8f8f8;
				transition: transform 0.3s ease;
			}
			
			.avatar-img:active {
				transform: scale(0.95);
			}
			
			.avatar-text {
				font-size: 28rpx;
				color: #666;
				margin-top: 20rpx;
			}
		}
		
		.info-list {
			background-color: #fff;
			margin-bottom: 40rpx;
			
			.info-item {
				display: flex;
				justify-content: space-between;
				align-items: center;
				padding: 30rpx 40rpx;
				border-bottom: 1rpx solid #f0f0f0;
				
				.label {
					font-size: 32rpx;
					color: #333;
					font-weight: 500;
					flex-shrink: 0;
					width: 200rpx;
				}
				
				.value-wrapper {
					flex: 1;
					display: flex;
					justify-content: flex-end;
					align-items: center;
					gap: 20rpx;
					
					.value {
						font-size: 30rpx;
						color: #666;
						text-align: right;
						word-break: break-all;
					}
					
					.verified-tag {
						font-size: 24rpx;
						color: #07c160;
						background-color: rgba(7, 193, 96, 0.1);
						padding: 4rpx 12rpx;
						border-radius: 20rpx;
					}
				}
				
				&:last-child {
					border-bottom: none;
				}
			}
		}
	}
}
</style>