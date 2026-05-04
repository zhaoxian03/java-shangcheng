<template>
	<view class="bind-email-page">
		<view class="content">
			<!-- 当前绑定状态 -->
			<view class="status-card" v-if="currentEmail && isValidEmailFormat(currentEmail)">
				<uni-icons type="checkmark-circle-filled" size="24" color="#4a90e2"></uni-icons>
				<text class="status-text">已绑定邮箱：{{ maskedEmail }}</text>
			</view>
			
			<view class="status-card error" v-else-if="currentEmail && !isValidEmailFormat(currentEmail)">
				<uni-icons type="close-circle-filled" size="24" color="#ff4444"></uni-icons>
				<text class="status-text">无效邮箱：{{ currentEmail }}（请重新绑定）</text>
			</view>
			
			<view class="status-card warning" v-else>
				<uni-icons type="info" size="24" color="#ff9900"></uni-icons>
				<text class="status-text">您还未绑定邮箱</text>
			</view>

			<!-- 邮箱输入框 -->
			<view class="input-container">
				<view class="input-label">邮箱地址</view>
				<input 
					type="text" 
					v-model="email" 
					:placeholder="getEmailPlaceholder()" 
					class="email-input"
					@input="formatEmail"
				/>
				<text class="input-hint" v-if="!isValidEmail && email">请输入有效的邮箱地址</text>
				<text class="input-tip" v-if="hasInvalidEmail">
					当前邮箱格式不正确，请输入正确的邮箱地址进行更新
				</text>
				
				<!-- 邮箱格式提示 -->
				<view class="email-format-guide">
					<view class="guide-title">
						<uni-icons type="info" size="16" color="#666"></uni-icons>
						<text class="title-text">什么是有效的邮箱地址？</text>
					</view>
					
					<view class="format-examples">
						<view class="example-section">
							<text class="section-title">✅ 正确的格式：</text>
							<view class="example-item" v-for="(example, index) in validEmailExamples" :key="index">
								<uni-icons type="checkmarkempty" size="14" color="#4CAF50"></uni-icons>
								<text class="example-text">{{ example }}</text>
							</view>
						</view>
						
						<view class="example-section">
							<text class="section-title">❌ 错误的格式：</text>
							<view class="example-item" v-for="(example, index) in invalidEmailExamples" :key="'invalid-' + index">
								<uni-icons type="closeempty" size="14" color="#f44336"></uni-icons>
								<text class="example-text">{{ example }}</text>
							</view>
						</view>
						
						<view class="format-tips">
							<text class="tip-item">📧 邮箱地址应该包含 @ 符号</text>
							<text class="tip-item">🔤 @ 前面是用户名（可以是字母、数字、点、下划线）</text>
							<text class="tip-item">🌐 @ 后面是域名，必须包含点号（如 gmail.com）</text>
							<text class="tip-item">💡 常见的邮箱服务商：@gmail.com, @qq.com, @163.com, @outlook.com</text>
						</view>
					</view>
				</view>
			</view>

			<!-- 操作按钮 -->
			<button 
				class="submit-btn" 
				:class="{ 'disabled': !isValidEmail || isLoading }"
				:disabled="!isValidEmail || isLoading"
				@click="handleSaveEmail"
			>
				{{ getButtonText() }}
			</button>
			
			<!-- 调试按钮（开发时使用） -->
			<button 
				class="debug-btn" 
				@click="testBackendUpdate"
				v-if="showDebug"
			>
				测试后端接口
			</button>
		</view>
	</view>
</template>

<script>
import { userApi } from '@/api/user.js';

export default {
	data() {
		return {
			userId: null,
			userToken: null,
			currentEmail: '',
			email: '',
			isLoading: false,
			userInfo: null,
			showDebug: false, // 开发模式设为true，生产环境设为false
			
			// 有效的邮箱示例
			validEmailExamples: [
				'username@gmail.com',
				'user.name@qq.com',
				'user123@163.com',
				'my_email@outlook.com',
				'first.last@company.com'
			],
			
			// 无效的邮箱示例
			invalidEmailExamples: [
				'123',                    // 纯数字
				'username',               // 没有@符号
				'username@',              // 没有域名
				'@gmail.com',             // 没有用户名
				'user name@gmail.com'     // 包含空格
			]
		};
	},

	computed: {
		// 邮箱脱敏显示
		maskedEmail() {
			if (!this.currentEmail) return '';
			
			// 如果是无效邮箱，显示完整值
			if (!this.isValidEmailFormat(this.currentEmail)) {
				return this.currentEmail;
			}
			
			const [local, domain] = this.currentEmail.split('@');
			if (local.length <= 3) {
				return '***@' + domain;
			}
			return local.substring(0, 3) + '***@' + domain;
		},

		// 邮箱验证（用于输入框）
		isValidEmail() {
			const email = this.email;
			return this.isValidEmailFormat(email);
		},
		
		// 检查是否有无效邮箱需要修复
		hasInvalidEmail() {
			return this.currentEmail && !this.isValidEmailFormat(this.currentEmail);
		}
	},

	onLoad(options) {
		this.userId = options.userId || uni.getStorageSync('userId');
		this.userToken = uni.getStorageSync('userToken');
		
		console.log('📝 用户ID:', this.userId);
		console.log('🔑 Token:', this.userToken ? '存在' : '不存在');
		
		if (!this.userId || !this.userToken) {
			uni.showToast({
				title: '请先登录',
				icon: 'error',
				duration: 1500
			});
			setTimeout(() => {
				uni.navigateBack();
			}, 1500);
			return;
		}
		
		this.loadEmailStatus();
	},

	methods: {
		// 获取占位符文本
		getEmailPlaceholder() {
			if (this.hasInvalidEmail) {
				return '请输入正确的邮箱地址';
			}
			return this.currentEmail ? '请输入新邮箱地址' : '请输入要绑定的邮箱地址';
		},
		
		// 获取按钮文本
		getButtonText() {
			if (this.isLoading) return '处理中...';
			if (this.hasInvalidEmail) return '修复邮箱';
			return this.currentEmail ? '修改邮箱' : '绑定邮箱';
		},
		
		// 邮箱格式验证方法
		isValidEmailFormat(email) {
			if (!email) return false;
			
			// 移除首尾空格
			email = email.trim();
			
			// 如果email是数字或其他无效值
			if (/^\d+$/.test(email)) {
				return false;
			}
			
			// 基础邮箱格式验证
			const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
			if (!emailRegex.test(email)) {
				return false;
			}
			
			// 进一步验证域名
			const [local, domain] = email.split('@');
			if (domain.split('.').length < 2) {
				return false;
			}
			
			return true;
		},
		
		// 格式化邮箱
		formatEmail() {
			// 移除空格
			this.email = this.email.trim();
		},
		
		// 加载邮箱状态
		async loadEmailStatus() {
			try {
				uni.showLoading({ 
					title: '加载中...',
					mask: true 
				});

				let userInfo = uni.getStorageSync('userInfo');
				
				// 如果本地没有用户信息，从后端获取
				if (!userInfo) {
					try {
						const result = await userApi.getProfile(this.userToken);
						if (result.code === 200 && result.data) {
							userInfo = result.data;
							uni.setStorageSync('userInfo', userInfo);
						}
					} catch (error) {
						console.error('❌ 获取用户信息失败:', error);
					}
				}
				
				this.userInfo = userInfo;
				
				if (userInfo) {
					// 尝试多种可能的邮箱字段名
					const emailFields = ['email', 'mail', 'eMail', 'emailAddress', 'userEmail'];
					let foundEmail = null;
					
					for (const field of emailFields) {
						if (userInfo[field] !== undefined && userInfo[field] !== null && userInfo[field] !== '') {
							foundEmail = userInfo[field];
							break;
						}
					}
					
					if (foundEmail) {
						// 转换为字符串
						foundEmail = String(foundEmail);
						
						// 改进的邮箱验证逻辑
						if (this.isValidEmailFormat(foundEmail)) {
							this.currentEmail = foundEmail;
							this.email = foundEmail; // 预填充当前邮箱
						} else {
							// 即使是无效格式也显示，但标记为无效
							this.currentEmail = foundEmail;
							this.email = ''; // 清空输入框
						}
					} else {
						this.currentEmail = '';
						this.email = '';
					}
				} else {
					this.currentEmail = '';
					this.email = '';
				}
				
			} catch (error) {
				console.error('❌ 加载失败:', error);
				uni.showToast({
					title: '加载失败',
					icon: 'none'
				});
			} finally {
				uni.hideLoading();
			}
		},

		// 主方法：保存邮箱 - 删除确认对话框
		async handleSaveEmail() {
		    if (!this.isValidEmail || this.isLoading) return;
		    
		    // 检查是否与当前邮箱相同（仅当当前邮箱有效时）
		    if (this.currentEmail && this.isValidEmailFormat(this.currentEmail) && this.email === this.currentEmail) {
		        uni.showToast({
		            title: '新邮箱与当前邮箱相同',
		            icon: 'none',
		            duration: 2000
		        });
		        return;
		    }
		    
		    // 直接执行保存操作，不显示确认对话框
		    await this.doSaveEmail();
		},
		
		// 执行保存邮箱操作
		async doSaveEmail() {
		    try {
		        this.isLoading = true;
		        uni.showLoading({ 
		            title: '保存中...', 
		            mask: true 
		        });

		        console.log('🔄 开始更新邮箱...');
		        
		        // 验证必填字段
		        if (!this.userId || !this.userToken) {
		            throw new Error('用户信息不完整');
		        }
		        
		        // 获取当前用户信息（用于获取手机号）
		        let userPhone = '';
		        let currentName = '';
		        
		        // 从本地或后端获取完整用户信息
		        if (this.userInfo) {
		            currentName = this.userInfo.username || this.userInfo.name;
		            userPhone = this.userInfo.phone || this.userInfo.phoneNumber || this.userInfo.mobile;
		        } else {
		            try {
		                const profileResult = await userApi.getProfile(this.userToken);
		                if (profileResult.code === 200 && profileResult.data) {
		                    currentName = profileResult.data.username || profileResult.data.name || 'user_' + this.userId;
		                    userPhone = profileResult.data.phone || '';
		                    // 更新本地存储
		                    uni.setStorageSync('userInfo', profileResult.data);
		                    this.userInfo = profileResult.data;
		                }
		            } catch (error) {
		                console.log('⚠️ 获取用户信息失败:', error);
		            }
		        }
		        
		        // 如果还没有手机号，使用默认值（重要！）
		        if (!userPhone) {
		            userPhone = '13800138000'; // 临时默认手机号
		        }
		        
		        // 如果还没有用户名，使用默认值
		        if (!currentName) {
		            currentName = 'user_' + this.userId;
		        }
		        
		        // 方法1：使用updateUserInfo API（发送完整数据）
		        try {
		            console.log('🔧 尝试方法1：updateUserInfo API');
		            
		            // 构建完整更新数据（必须包含手机号！）
		            const updateData = {
		                userId: this.userId,
		                email: this.email,
		                name: currentName,
		                username: currentName,
		                phone: userPhone, // 关键：必须有手机号
		                address: this.userInfo?.address || '',
		                avatarUrl: this.userInfo?.avatarUrl || ''
		            };
		            
		            const result = await userApi.updateUserInfo(
		                this.userId, 
		                updateData, 
		                this.userToken
		            );
		            
		            if (result.code === 200) {
		                await this.handleSuccess();
		                return;
		            } else {
		                throw new Error(result.message || '更新失败');
		            }
		        } catch (error) {
		            console.log('⚠️ 方法1失败，尝试方法2:', error.message);
		            
		            // 方法2：使用updateEmail API
		            try {
		                console.log('🔧 尝试方法2：updateEmail API');
		                
		                const result = await userApi.updateEmail({
		                    userId: this.userId,
		                    token: this.userToken,
		                    email: this.email,
		                    name: currentName,
		                    username: currentName
		                });
		                
		                if (result.code === 200) {
		                    await this.handleSuccess();
		                    return;
		                } else {
		                    throw new Error(result.message || '更新失败');
		                }
		            } catch (error2) {
		                console.log('⚠️ 方法2失败，尝试方法3:', error2.message);
		                
		                // 方法3：直接调用接口
		                await this.tryBackupUpdate(currentName, userPhone);
		            }
		        }
		        
		    } catch (error) {
		        console.error('❌ 保存失败:', error);
		        
		        // 根据错误类型提供不同的提示
		        let errorMessage = '更新失败: ' + (error.message || '请稍后重试');
		        
		        if (error.message.includes('手机号')) {
		            errorMessage = '用户信息不完整，请先绑定手机号';
		        } else if (error.message.includes('网络')) {
		            errorMessage = '网络连接失败，请检查网络设置';
		        } else if (error.message.includes('格式')) {
		            errorMessage = '邮箱格式不正确，请检查输入';
		        } else if (error.message.includes('Token')) {
		            errorMessage = '登录已过期，请重新登录';
		        } else if (error.message.includes('用户信息')) {
		            errorMessage = '用户信息不完整，请重新登录';
		        }
		        
		        uni.showToast({
		            title: errorMessage,
		            icon: 'none',
		            duration: 3000
		        });
		    } finally {
		        this.isLoading = false;
		        uni.hideLoading();
		    }
		},
		
		// 备用更新方法（直接调用接口）
		async tryBackupUpdate(currentName, userPhone) {
		    try {
		        console.log('🔧 使用方法3：直接调用接口');
		        
		        // 构建完整更新数据（必须包含手机号！）
		        const updateData = {
		            userId: String(this.userId),
		            name: currentName,
		            username: currentName,  // 同时发送username和name
		            phone: userPhone,  // 关键：必须有手机号
		            email: this.email,
		            address: this.userInfo?.address || '',
		            avatarUrl: this.userInfo?.avatarUrl || ''
		        };
		        
		        return new Promise((resolve, reject) => {
		            uni.request({
		                url: 'http://localhost:8089/api/user/update',
		                method: 'POST',
		                header: {
		                    'Content-Type': 'application/json',
		                    'Authorization': 'Bearer ' + this.userToken
		                },
		                data: updateData,
		                success: (res) => {
		                    if (res.statusCode === 200 && res.data?.code === 200) {
		                        resolve(res.data);
		                    } else {
		                        reject(new Error(res.data?.message || '更新失败'));
		                    }
		                },
		                fail: (error) => {
		                    reject(new Error('网络请求失败: ' + error.errMsg));
		                }
		            });
		        });
		        
		    } catch (error) {
		        console.error('❌ 方法3失败详情:', error);
		        throw error;
		    }
		},
		
		// 处理成功结果 - 不跳转页面
		async handleSuccess() {
		    console.log('✅ 邮箱更新成功');
		    
		    const actionText = this.hasInvalidEmail ? '修复' : (this.currentEmail ? '修改' : '绑定');
		    
		    // 显示成功提示
		    uni.showToast({
		        title: `邮箱${actionText}成功`,
		        icon: 'success',
		        duration: 2000
		    });
		    
		    // 更新当前页面数据
		    const oldEmail = this.currentEmail;
		    this.currentEmail = this.email;
		    
		    // 更新本地存储的用户信息
		    let userInfo = uni.getStorageSync('userInfo') || {};
		    userInfo.email = this.email;
		    
		    // 同时更新其他可能的邮箱字段名
		    userInfo.mail = this.email;
		    userInfo.eMail = this.email;
		    
		    uni.setStorageSync('userInfo', userInfo);
		    this.userInfo = userInfo;
		    
		    // 发送事件通知其他页面
		    uni.$emit('userInfoUpdated', { 
		        email: this.email,
		        oldEmail: oldEmail 
		    });
		    
		    // 不跳转页面，停留在当前页面
		    // 可以清空输入框或保持显示
		    setTimeout(() => {
		        // 可选：清空输入框
		        // this.email = '';
		    }, 1500);
		},
		
		// 测试方法：直接调用后端接口
		async testBackendUpdate() {
		    try {
		        uni.showLoading({ title: '测试中...' });
		        
		        // 测试数据 - 必须包含手机号！
		        const testData = {
		            userId: String(this.userId),
		            name: '测试用户',
		            username: '测试用户',
		            phone: '13800138000',  // 必须有手机号
		            email: 'test@example.com',
		            address: '测试地址'
		        };
		        
		        uni.request({
		            url: 'http://localhost:8089/api/user/update',
		            method: 'POST',
		            header: {
		                'Content-Type': 'application/json',
		                'Authorization': 'Bearer ' + this.userToken
		            },
		            data: testData,
		            success: (res) => {
		                uni.hideLoading();
		                if (res.statusCode === 200) {
		                    uni.showToast({
		                        title: `测试成功: ${res.data?.message || 'OK'}`,
		                        icon: 'success',
		                        duration: 3000
		                    });
		                } else {
		                    uni.showToast({
		                        title: `服务器错误: ${res.statusCode} - ${res.data?.message || ''}`,
		                        icon: 'none',
		                        duration: 3000
		                    });
		                }
		            },
		            fail: (error) => {
		                uni.hideLoading();
		                uni.showToast({
		                    title: '请求失败: ' + (error.errMsg || '未知错误'),
		                    icon: 'none',
		                    duration: 3000
		                });
		            }
		        });
		        
		    } catch (error) {
		        uni.hideLoading();
		        uni.showToast({
		            title: '测试异常: ' + error.message,
		            icon: 'none',
		            duration: 3000
		        });
		    }
		}
	}
};
</script>

<style lang="scss" scoped>
.bind-email-page {
	/* 修复1: 使用min-height确保内容可以扩展 */
	min-height: 100vh;
	background-color: #f5f5f5;
	/* 修复2: 允许页面滚动 */
	overflow-y: auto;
	/* 修复3: 添加flex布局避免内容溢出 */
	display: flex;
	flex-direction: column;
}

.content {
	/* 修复4: 使用flex: 1填充可用空间 */
	flex: 1;
	padding: 40rpx 30rpx;
	box-sizing: border-box;
	/* 修复5: 移除内部滚动，由父容器处理 */
	/* overflow-y: auto; */
	/* 修复6: 在flex布局中允许内容滚动 */
	min-height: 0;
}

.status-card {
	background-color: #e8f4ff;
	border-radius: 16rpx;
	padding: 30rpx;
	display: flex;
	align-items: center;
	margin-bottom: 40rpx;
	/* 修复7: 防止被压缩 */
	flex-shrink: 0;
	
	&.warning {
		background-color: #fff8e8;
	}
	
	&.error {
		background-color: #ffe8e8;
		border-left: 8rpx solid #ff4444;
	}

	.status-text {
		font-size: 28rpx;
		color: #4a90e2;
		margin-left: 20rpx;
		line-height: 1.5;
		
		.warning & {
			color: #ff9900;
		}
		
		.error & {
			color: #ff4444;
			font-weight: 500;
		}
	}
}

.input-container {
	background-color: #fff;
	border-radius: 16rpx;
	padding: 30rpx;
	margin-bottom: 40rpx;
	/* 修复8: 防止被压缩 */
	flex-shrink: 0;

	.input-label {
		font-size: 32rpx;
		color: #333;
		margin-bottom: 20rpx;
		font-weight: 500;
	}

	.email-input {
		width: 100%;
		height: 90rpx;
		border: 2rpx solid #e0e0e0;
		border-radius: 8rpx;
		padding: 0 20rpx;
		font-size: 32rpx;
		box-sizing: border-box;
		background-color: #f9f9f9;

		&:focus {
			border-color: #4a90e2;
			background-color: #fff;
		}
	}

	.input-hint {
		display: block;
		font-size: 26rpx;
		color: #ff4444;
		margin-top: 10rpx;
	}
	
	.input-tip {
		display: block;
		font-size: 26rpx;
		color: #ff9900;
		margin-top: 10rpx;
		background-color: #fff8e8;
		padding: 12rpx 16rpx;
		border-radius: 8rpx;
		border-left: 4rpx solid #ff9900;
	}
	
	.email-format-guide {
		margin-top: 30rpx;
		padding-top: 20rpx;
		border-top: 1rpx solid #f0f0f0;
		
		.guide-title {
			display: flex;
			align-items: center;
			margin-bottom: 20rpx;
			
			.title-text {
				font-size: 28rpx;
				color: #666;
				font-weight: 500;
				margin-left: 10rpx;
			}
		}
		
		.format-examples {
			.example-section {
				margin-bottom: 20rpx;
				
				.section-title {
					display: block;
					font-size: 26rpx;
					color: #333;
					font-weight: 500;
					margin-bottom: 10rpx;
				}
				
				.example-item {
					display: flex;
					align-items: center;
					margin-bottom: 8rpx;
					
					.example-text {
						font-size: 24rpx;
						color: #666;
						margin-left: 8rpx;
					}
				}
			}
			
			.format-tips {
				background-color: #f9f9f9;
				border-radius: 8rpx;
				padding: 16rpx;
				margin-top: 16rpx;
				
				.tip-item {
					display: block;
					font-size: 24rpx;
					color: #666;
					margin-bottom: 8rpx;
					line-height: 1.4;
					
					&:last-child {
						margin-bottom: 0;
					}
				}
			}
		}
	}
}

.submit-btn {
	width: 100%;
	height: 90rpx;
	background: linear-gradient(135deg, #4a90e2, #5a8bff);
	color: #fff;
	font-size: 34rpx;
	font-weight: bold;
	border-radius: 45rpx;
	border: none;
	margin-bottom: 30rpx;
	transition: all 0.3s ease;
	/* 修复9: 按钮不被压缩 */
	flex-shrink: 0;
	
	&:active {
		opacity: 0.9;
		transform: scale(0.98);
	}

	&.disabled {
		background: #ccc;
		color: #999;
		transform: none;
		
		&:active {
			opacity: 1;
			transform: none;
		}
	}
}

.debug-btn {
	width: 100%;
	height: 70rpx;
	background-color: #999;
	color: #fff;
	font-size: 28rpx;
	border-radius: 35rpx;
	border: none;
	margin-top: 20rpx;
	opacity: 0.7;
	flex-shrink: 0;
	
	&:active {
		opacity: 0.9;
	}
}

/* 响应式调整 */
@media (max-width: 750px) {
	.content {
		padding: 30rpx 20rpx;
	}
	
	.status-card {
		padding: 24rpx;
		margin-bottom: 30rpx;
		
		.status-text {
			font-size: 26rpx;
		}
	}
	
	.input-container {
		padding: 24rpx;
		margin-bottom: 30rpx;
		
		.input-label {
			font-size: 30rpx;
			margin-bottom: 16rpx;
		}
		
		.email-input {
			height: 80rpx;
			font-size: 30rpx;
		}
		
		.input-hint, .input-tip {
			font-size: 24rpx;
			padding: 10rpx 14rpx;
		}
		
		.email-format-guide {
			.guide-title {
				.title-text {
					font-size: 26rpx;
				}
			}
			
			.format-examples {
				.example-section {
					.section-title {
						font-size: 24rpx;
					}
					
					.example-item {
						.example-text {
							font-size: 22rpx;
						}
					}
				}
				
				.format-tips {
					.tip-item {
						font-size: 22rpx;
					}
				}
			}
		}
	}
	
	.submit-btn {
		height: 80rpx;
		font-size: 32rpx;
	}
	
	.debug-btn {
		height: 65rpx;
		font-size: 26rpx;
	}
}
</style>