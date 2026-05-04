<template>
	<view class="collect-page">
		<!-- 顶部导航 -->
		<view class="nav-header">
			<view class="nav-back" @click="goBack">
				<uni-icons type="back" size="24" color="#333"></uni-icons>
			</view>
			<text class="nav-title">我的收藏</text>
			<view class="nav-right">
				<text class="nav-edit" @click="toggleEditMode">{{ isEditMode ? '完成' : '编辑' }}</text>
			</view>
		</view>

		<!-- 收藏分类 -->
		<view class="category-tabs">
			<view 
				class="tab-item" 
				:class="{ 'active': activeTab === 1 }" 
				@click="switchTab(1)"
			>
				<text>商品</text>
			</view>
			<view 
				class="tab-item" 
				:class="{ 'active': activeTab === 2 }" 
				@click="switchTab(2)"
			>
				<text>店铺</text>
			</view>
		</view>

		<!-- 商品收藏列表 -->
		<view class="collect-list" v-if="activeTab === 1">
			<!-- 商品项 -->
			<view 
				class="collect-item" 
				v-for="(item, index) in goodsList" 
				:key="item.id"
				@click="goToGoodsDetail(item)"
			>
				<view class="item-left">
					<image :src="item.image" class="goods-image" mode="aspectFill"></image>
					<view class="select-checkbox" v-if="isEditMode" @click.stop="toggleSelect(index)">
						<uni-icons 
							:type="item.selected ? 'checkbox-filled' : 'circle'" 
							:color="item.selected ? '#f36c8a' : '#ccc'" 
							size="20"
						></uni-icons>
					</view>
				</view>
				
				<view class="item-content">
					<view class="goods-title">{{ item.title }}</view>
					<view class="goods-price">
						<text class="current-price">¥{{ item.price }}</text>
						<text class="original-price" v-if="item.originalPrice">¥{{ item.originalPrice }}</text>
					</view>
					<view class="goods-info">
						<text class="sales">已售{{ item.sales }}件</text>
						<view class="collect-time">{{ formatTime(item.collectTime) }}</view>
					</view>
					<view class="goods-tags">
						<text class="tag" v-for="(tag, tagIndex) in item.tags" :key="tagIndex">{{ tag }}</text>
					</view>
				</view>
				
				<view class="item-actions">
					<view class="action-btn cart-btn" @click.stop="addToCart(item)">
						<uni-icons type="cart" size="20" color="#f36c8a"></uni-icons>
						<text>加入购物车</text>
					</view>
					<view class="action-btn delete-btn" @click.stop="deleteItem(index)" v-if="!isEditMode">
						<uni-icons type="trash" size="20" color="#999"></uni-icons>
					</view>
				</view>
			</view>

			<!-- 空状态 -->
			<view class="empty-state" v-if="goodsList.length === 0">
				<image src="/static/empty-collect.png" class="empty-image"></image>
				<text class="empty-text">暂无收藏的商品</text>
				<view class="empty-btn" @click="goShopping">去逛逛</view>
			</view>
		</view>

		<!-- 店铺收藏列表 -->
		<view class="shop-list" v-if="activeTab === 2">
			<!-- 店铺项 -->
				<view 
					class="shop-item" 
					v-for="(shop, index) in shopList" 
					:key="shop.id"
					@click="goToShop(shop)"
				>
					<view class="shop-header">
						<image :src="shop.avatar" class="shop-logo" mode="aspectFill" @error="onImageError($event, shop)"></image>
						<view class="shop-info">
							<view class="shop-name">{{ shop.name }}</view>
							<view class="shop-rating">
								<uni-icons type="star" size="16" color="#FFD700"></uni-icons>
								<text>{{ Number(shop.rating).toFixed(1) }}</text>
								<text class="shop-sales">销量 {{ shop.sales || 0 }}</text>
							</view>
						</view>
						<view class="shop-actions">
							<view class="follow-btn" :class="{ 'followed': shop.isFollowed }" @click.stop="toggleFollow(shop, index)">
								{{ shop.isFollowed ? '已关注' : '+ 关注' }}
							</view>
							<uni-icons type="right" size="18" color="#999"></uni-icons>
						</view>
					</view>
					<view class="shop-stats">
						<view class="stat-item">
							<text class="stat-value">{{ shop.goodsCount || 0 }}</text>
							<text class="stat-label">商品</text>
						</view>
						<view class="stat-item">
							<text class="stat-value">{{ shop.fans || 0 }}</text>
							<text class="stat-label">粉丝</text>
						</view>
						<view class="stat-item">
							<text class="stat-value">{{ shop.openDays || 0 }}</text>
							<text class="stat-label">开店天数</text>
						</view>
					</view>
				</view>

			<!-- 空状态 -->
			<view class="empty-state" v-if="shopList.length === 0">
				<image src="/static/empty-shop.png" class="empty-image"></image>
				<text class="empty-text">暂无关注的店铺</text>
				<view class="empty-btn" @click="goShopping">去逛逛</view>
			</view>
		</view>

		<!-- 批量操作栏 -->
		<view class="batch-actions" v-if="isEditMode && activeTab === 1">
			<view class="select-all" @click="toggleSelectAll">
				<uni-icons 
					:type="isAllSelected ? 'checkbox-filled' : 'circle'" 
					:color="isAllSelected ? '#f36c8a' : '#ccc'" 
					size="20"
				></uni-icons>
				<text>全选</text>
			</view>
			<view class="batch-delete" @click="batchDelete">
				删除({{ selectedCount }})
			</view>
		</view>
	</view>
</template>

<script>
	import { collectApi } from '../../api/collect.js';
		export default {
		data() {
			return {
				activeTab: 1, // 1: 商品, 2: 店铺
				isEditMode: false,
				userId: '',
				// 商品收藏列表
				goodsList: [],
				// 店铺收藏列表
				shopList: []
			}
		},
		computed: {
			// 选中的商品数量
			selectedCount() {
				return this.goodsList.filter(item => item.selected).length;
			},
			// 是否全选
			isAllSelected() {
				if (this.goodsList.length === 0) return false;
				return this.goodsList.every(item => item.selected);
			}
		},
		onLoad(options) {
			// 获取用户ID
			this.userId = options.userId || uni.getStorageSync('userId') || '';
			console.log('收藏页面用户ID:', this.userId);
			
			// 如果是从my页面跳转过来的，可能会有userId参数
			if (options.userId) {
				this.loadCollectData(options.userId);
			} else {
				this.loadCollectData(this.userId);
			}
		},
		onShow() {
		// 页面显示时不需要重复加载，避免数据双倍显示
		// 只在onLoad中加载一次，或添加防抖机制
	},
		methods: {
			// 获取用户收藏数据
		async loadCollectData(userId) {
			if (!userId) {
				console.log('用户未登录，显示默认数据');
				return;
			}
			
			try {
				// 调用API获取用户收藏数据
				const goodsResult = await collectApi.getGoodsList(userId);
				if (goodsResult.code === 200) {
					// 转换API返回的数据格式为页面所需格式
					this.goodsList = goodsResult.data.list.map(item => ({
						id: item.goodsId, // 使用正确的商品ID字段名（驼峰命名）
						image: item.goodsImage,
						title: item.goodsName,
						price: item.goodsPrice,
						originalPrice: item.goodsPrice * 1.5, // 模拟原价
						sales: Math.floor(Math.random() * 1000) + 100, // 模拟销量
						collectTime: item.collectTime,
						tags: ['热销', '包邮'], // 模拟标签
						selected: false
					}));
				}
				
				// 调用API获取用户收藏的店铺数据
				const shopResult = await collectApi.getShopList(userId);
				let shopData = [];
				
				// 处理不同的API返回数据格式
				if (shopResult.code === 200) {
					// 情况1：API直接返回数组
					if (Array.isArray(shopResult.data)) {
						shopData = shopResult.data;
						console.log('API返回直接数组格式，店铺数据:', shopData);
					} 
					// 情况2：API返回包含list属性的对象
					else if (shopResult.data && Array.isArray(shopResult.data.list)) {
						shopData = shopResult.data.list;
						console.log('API返回带list属性格式，店铺数据:', shopData);
					} 
					// 情况3：API返回其他格式
					else {
						console.log('API返回格式未知，使用空数据');
						shopData = [];
					}
				}
				
				if (shopData.length > 0) {
					// 转换API返回的数据格式为页面所需格式
					this.shopList = [];
					for (const item of shopData) {
						// 为每个店铺调用API获取完整的店铺信息
						await this.loadShopDetail(item);
					}
				} else {
					// 如果API返回空数据，使用测试数据
					console.log('API返回空数据，使用测试数据');
					this.shopList = [
						{
							id: 'test1',
							name: '测试店铺1',
							avatar: 'http://localhost:8089/ea2d5509-ade4-4877-9fe5-9c1ffd58e6aa.png',
							rating: 4.5,
							goodsCount: 120,
							fans: 850,
							score: 4.5,
							isFollowed: true,
							goods: [
								{id: 'g1', image: '/static/other/logo.jpg', price: 19.9},
								{id: 'g2', image: '/static/other/logo.jpg', price: 29.9},
								{id: 'g3', image: '/static/other/logo.jpg', price: 39.9}
							]
						},
						{
							id: 'test2',
							name: '测试店铺2',
							avatar: '/static/other/moren_1.jpg',
							rating: 4.2,
							goodsCount: 85,
							fans: 420,
							score: 4.2,
							isFollowed: true,
							goods: [
								{id: 'g4', image: '/static/other/logo.jpg', price: 15.9},
								{id: 'g5', image: '/static/other/logo.jpg', price: 25.9}
							]
						}
					];
				}
			console.log('最终店铺列表:', this.shopList);
			} catch (error) {
				console.error('获取收藏列表失败:', error);
				uni.showToast({
					title: '获取收藏列表失败',
					icon: 'none'
				});
			}
			
			console.log('加载用户收藏数据，用户ID:', userId);
		},
		// 获取店铺详情信息
		async loadShopDetail(shopItem) {
			const app = getApp({allowDefault: true});
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			const shopId = shopItem.shopId || shopItem.sid || shopItem.id;
			
			if (!shopId) return;
			
			try {
				// 调用店铺详情API
				const shopDetailResult = await new Promise((resolve, reject) => {
					uni.request({
						url: app.globalData.baseUrl + 'api/seller/shop/' + shopId,
						header: {
							'Authorization': token ? `Bearer ${token}` : ''
						},
						success: resolve,
						fail: reject
					});
				});
				
				if (shopDetailResult.data.code === 200) {
					const shopData = shopDetailResult.data.data;
					// 获取商品数量
					const goodsCountResult = await new Promise((resolve, reject) => {
						uni.request({
							url: app.globalData.baseUrl + 'api/seller/stats?sid=' + shopId,
							header: {
								'Authorization': token ? `Bearer ${token}` : ''
							},
							success: resolve,
							fail: reject
						});
					});
					
					// 获取粉丝数量
					const fansCountResult = await new Promise((resolve, reject) => {
						uni.request({
							url: app.globalData.baseUrl + 'api/shop/fans/count/' + shopId,
							header: {
								'Authorization': token ? `Bearer ${token}` : ''
							},
							success: resolve,
							fail: reject
						});
					});
					
					// 计算开店天数
					let openDays = 0;
					if (shopData.ontime) {
						const now = new Date();
						const ontime = new Date(shopData.ontime);
						const diffTime = now - ontime;
						openDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
					}
					
					// 处理店铺logo
					let logoUrl = '/static/other/moren_1.jpg'; // 默认logo
					const logoFields = ['slogo', 'logo', 'shopLogo', 'image', 'shopImage'];
					for (const field of logoFields) {
						if (shopData[field]) {
							logoUrl = shopData[field];
							break;
						}
					}
					
					// 构建完整的店铺信息
					const shop = {
						id: shopId,
						name: shopData.sname || shopItem.shopName || shopItem.name || '店铺名称',
						avatar: logoUrl,
						rating: Number(shopData.overallExperience || shopItem.rating || 0),
						goodsCount: goodsCountResult.data.code === 200 ? Number(goodsCountResult.data.data.goodsCount || 0) : 0,
						fans: fansCountResult.data.code === 200 ? Number(fansCountResult.data.data || 0) : 0,
						score: Number(shopData.overallExperience || shopItem.score || 0),
						sales: Number(shopData.sales || shopItem.sales || 0),
						openDays: openDays,
						isFollowed: true
					};
					
					// 添加到店铺列表
					this.shopList.push(shop);
					console.log('店铺详情加载成功:', shop.name, shop);
				}
			} catch (error) {
				console.error('加载店铺详情失败:', error);
				// 如果加载失败，使用基本信息
				let logoUrl = '/static/other/moren_1.jpg';
				const logoFields = ['shopImage', 'slogo', 'logo', 'shopLogo', 'image'];
				for (const field of logoFields) {
					if (shopItem[field]) {
						logoUrl = shopItem[field];
						break;
					}
				}
				
				const shop = {
					id: shopId,
					name: shopItem.shopName || shopItem.sname || shopItem.name || '店铺名称',
					avatar: logoUrl,
					rating: Number(shopItem.overallExperience || shopItem.rating || 0),
					goodsCount: Number(shopItem.goodsCount || shopItem.goods || 0),
					fans: Number(shopItem.fansCount || shopItem.fans || 0),
					score: Number(shopItem.overallExperience || shopItem.score || 0),
					sales: Number(shopItem.sales || 0),
					openDays: Number(shopItem.openDays || 0),
					isFollowed: true
				};
				
				this.shopList.push(shop);
			}
		},
			
			// 返回我的界面
			goBack() {
				uni.switchTab({
					url: '/pages/my/my'
				});
			},
			
			// 切换编辑模式
			toggleEditMode() {
				this.isEditMode = !this.isEditMode;
				// 退出编辑模式时取消所有选中
				if (!this.isEditMode) {
					this.goodsList.forEach(item => {
						item.selected = false;
					});
				}
			},
			
			// 切换标签页
			switchTab(tab) {
				this.activeTab = tab;
				this.isEditMode = false; // 切换标签时退出编辑模式
			},
			
			// 格式化时间
			formatTime(timeStr) {
				if (!timeStr) return '';
				const time = new Date(timeStr);
				const now = new Date();
				const diff = now - time;
				
				// 1天内显示具体时间
				if (diff < 24 * 60 * 60 * 1000) {
					const hours = time.getHours().toString().padStart(2, '0');
					const minutes = time.getMinutes().toString().padStart(2, '0');
					return `${hours}:${minutes}`;
				}
				
				// 1周内显示几天前
				if (diff < 7 * 24 * 60 * 60 * 1000) {
					const days = Math.floor(diff / (24 * 60 * 60 * 1000));
					return `${days}天前`;
				}
				
				// 超过1周显示日期
				const month = time.getMonth() + 1;
				const day = time.getDate();
				return `${month}月${day}日`;
			},
			
			// 跳转到商品详情
			goToGoodsDetail(item) {
				if (this.isEditMode) return; // 编辑模式下不跳转
				
				uni.navigateTo({
					url: `/pages/goods/goods?id=${item.id}&title=${encodeURIComponent(item.title)}`
				});
			},
			
			// 跳转到店铺
			goToShop(shop) {
				uni.navigateTo({
					url: `/pages/shop/goods-list?shopId=${shop.id}&name=${encodeURIComponent(shop.name)}`
				});
			},
			
			// 进入店铺
			enterShop(shop) {
				uni.navigateTo({
					url: `/pages/shop/goods-list?shopId=${shop.id}&name=${encodeURIComponent(shop.name)}`
				});
			},
			
			// 联系客服
			contactService(shop) {
				uni.showModal({
					title: '联系客服',
					content: `是否联系${shop.name}的客服？`,
					success: (res) => {
						if (res.confirm) {
							// 这里可以跳转到客服聊天页面
							uni.navigateTo({
								url: `/pages/chat/chat?shopId=${shop.id}&shopName=${encodeURIComponent(shop.name)}`
							});
						}
					}
				});
			},
			
			// 切换商品选中状态
			toggleSelect(index) {
				this.goodsList[index].selected = !this.goodsList[index].selected;
			},
			
			// 全选/取消全选
			toggleSelectAll() {
				const allSelected = this.isAllSelected;
				this.goodsList.forEach(item => {
					item.selected = !allSelected;
				});
			},
			
			// 单个删除
			deleteItem(index) {
				uni.showModal({
					title: '提示',
					content: '确定要删除这个收藏吗？',
					success: (res) => {
						if (res.confirm) {
							const itemId = this.goodsList[index].id;
							this.goodsList.splice(index, 1);
							
							// 调用API删除
							this.deleteCollectFromServer(itemId);
							
							uni.showToast({
								title: '删除成功',
								icon: 'success'
							});
						}
					}
				});
			},
			
			// 批量删除
			batchDelete() {
				if (this.selectedCount === 0) {
					uni.showToast({
						title: '请先选择要删除的商品',
						icon: 'none'
					});
					return;
				}
				
				uni.showModal({
					title: '提示',
					content: `确定要删除选中的${this.selectedCount}个收藏吗？`,
					success: (res) => {
						if (res.confirm) {
							// 获取选中的商品ID
							const selectedIds = this.goodsList
								.filter(item => item.selected)
								.map(item => item.id);
							
							// 从列表中移除
							this.goodsList = this.goodsList.filter(item => !item.selected);
							
							// 调用API批量删除
							this.batchDeleteFromServer(selectedIds);
							
							// 退出编辑模式
							this.isEditMode = false;
							
							uni.showToast({
								title: '删除成功',
								icon: 'success'
							});
						}
					}
				});
			},
			
			// 从服务器删除收藏
			deleteCollectFromServer(itemId) {
				if (!this.userId) return;
				
				// 这里调用API删除收藏
				// uni.request({
				//   url: '你的API地址/collect/delete',
				//   method: 'POST',
				//   data: {
				//     userId: this.userId,
				//     itemId: itemId,
				//     type: 'goods' // 或 'shop'
				//   },
				//   success: (res) => {
				//     console.log('删除成功:', res);
				//   }
				// });
				
				console.log('删除收藏，用户ID:', this.userId, '商品ID:', itemId);
			},
			
			// 批量删除收藏
			batchDeleteFromServer(itemIds) {
				if (!this.userId || itemIds.length === 0) return;
				
				// 这里调用API批量删除
				// uni.request({
				//   url: '你的API地址/collect/batch-delete',
				//   method: 'POST',
				//   data: {
				//     userId: this.userId,
				//     itemIds: itemIds,
				//     type: 'goods'
				//   },
				//   success: (res) => {
				//     console.log('批量删除成功:', res);
				//   }
				// });
				
				console.log('批量删除收藏，用户ID:', this.userId, '商品IDs:', itemIds);
			},
			
			// 添加到购物车
			addToCart(item) {
				// 这里调用添加购物车API
				uni.showLoading({
					title: '添加中...'
				});
				
				setTimeout(() => {
					uni.hideLoading();
					uni.showToast({
						title: '已加入购物车',
						icon: 'success'
					});
				}, 1000);
			},
			
			// 切换店铺关注状态
			toggleFollow(shop, index) {
				uni.showModal({
					title: shop.isFollowed ? '取消关注' : '关注店铺',
					content: shop.isFollowed ? `确定不再关注${shop.name}吗？` : `确定要关注${shop.name}吗？`,
					success: (res) => {
						if (res.confirm) {
							shop.isFollowed = !shop.isFollowed;
							
							// 更新服务器
							this.updateFollowStatus(shop.id, shop.isFollowed);
							
							uni.showToast({
								title: shop.isFollowed ? '关注成功' : '已取消关注',
								icon: 'success'
							});
						}
					}
				});
			},
			
			// 更新关注状态到服务器
			updateFollowStatus(shopId, isFollowed) {
				if (!this.userId) return;
				
				// 这里调用API更新关注状态
				// uni.request({
				//   url: '你的API地址/shop/follow',
				//   method: 'POST',
				//   data: {
				//     userId: this.userId,
				//     shopId: shopId,
				//     isFollowed: isFollowed
				//   }
				// });
				
				console.log('更新关注状态，用户ID:', this.userId, '店铺ID:', shopId, '关注状态:', isFollowed);
			},
			
			// 图片加载失败处理
		onImageError(e, shop) {
			console.error('店铺logo加载失败:', shop.name, shop.avatar);
			console.error('错误事件:', e);
			// 设置默认图片
			if (e.target) {
				e.target.src = '/static/other/moren_1.jpg';
				console.log('已设置默认图片');
			}
		},
		// 图片加载成功处理
		onImageLoad(e, shop) {
			console.log('店铺logo加载成功:', shop.name, shop.avatar);
		},
		// 测试获取店铺数据
		testShopData() {
			console.log('=== 开始测试店铺数据获取 ===');
			const userId = this.userId || 'test';
			collectApi.getShopList(userId).then(res => {
				console.log('=== 测试API响应 ===');
				console.log('响应状态:', res.code);
				console.log('响应数据:', res.data);
				if (res.data && res.data.length > 0) {
					console.log('=== 第一个店铺详情 ===');
					const firstShop = res.data[0];
					console.log('店铺名称:', firstShop.shopName || firstShop.name);
					console.log('店铺ID:', firstShop.shopId || firstShop.id);
					console.log('所有字段:', Object.keys(firstShop));
					// 检查所有可能的logo字段 - 添加shopImage字段
				const possibleLogoFields = ['shopImage', 'slogo', 'logo', 'avatar', 'shopLogo', 'image'];
				possibleLogoFields.forEach(field => {
					console.log(`${field}字段值:`, firstShop[field]);
				});
				}
			}).catch(err => {
				console.error('=== 测试API失败 ===', err);
			});
		},
			
			// 去逛逛
			goShopping() {
				uni.switchTab({
					url: '/pages/index/index'
				});
			}
		}
	}
</script>

<style lang="scss" scoped>
.collect-page {
	min-height: 100vh;
	background-color: #f5f5f5;
	padding-bottom: 100rpx;
	box-sizing: border-box;
}

/* 导航栏样式 */
.nav-header {
	height: 88rpx;
	background-color: #fff;
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 0 30rpx;
	border-bottom: 1rpx solid #f0f0f0;
	position: sticky;
	top: 0;
	z-index: 100;
	
	.nav-back {
		width: 60rpx;
	}
	
	.nav-title {
		font-size: 36rpx;
		font-weight: bold;
		color: #333;
	}
	
	.nav-right {
		width: 60rpx;
		
		.nav-edit {
			font-size: 28rpx;
			color: #f36c8a;
		}
	}
}

/* 分类标签样式 */
.category-tabs {
	display: flex;
	background-color: #fff;
	border-bottom: 1rpx solid #f0f0f0;
	
	.tab-item {
		flex: 1;
		height: 80rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 30rpx;
		color: #666;
		
		&.active {
			color: #f36c8a;
			font-weight: bold;
			position: relative;
			
			&::after {
				content: '';
				position: absolute;
				bottom: 0;
				left: 50%;
				transform: translateX(-50%);
				width: 80rpx;
				height: 4rpx;
				background-color: #f36c8a;
				border-radius: 2rpx;
			}
		}
	}
}

/* 商品收藏列表样式 */
.collect-list {
	padding: 20rpx 30rpx;
}

.collect-item {
	display: flex;
	background-color: #fff;
	border-radius: 16rpx;
	padding: 30rpx;
	margin-bottom: 20rpx;
	box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
	
	.item-left {
		position: relative;
		margin-right: 30rpx;
		
		.goods-image {
			width: 180rpx;
			height: 180rpx;
			border-radius: 8rpx;
		}
		
		.select-checkbox {
			position: absolute;
			top: 10rpx;
			left: 10rpx;
			background-color: rgba(255, 255, 255, 0.9);
			border-radius: 50%;
			padding: 4rpx;
		}
	}
	
	.item-content {
		flex: 1;
		
		.goods-title {
			font-size: 28rpx;
			color: #333;
			line-height: 1.4;
			margin-bottom: 15rpx;
			display: -webkit-box;
			-webkit-box-orient: vertical;
			-webkit-line-clamp: 2;
			overflow: hidden;
		}
		
		.goods-price {
			display: flex;
			align-items: center;
			margin-bottom: 15rpx;
			
			.current-price {
				font-size: 34rpx;
				color: #f36c8a;
				font-weight: bold;
				margin-right: 15rpx;
			}
			
			.original-price {
				font-size: 24rpx;
				color: #999;
				text-decoration: line-through;
			}
		}
		
		.goods-info {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 15rpx;
			
			.sales {
				font-size: 24rpx;
				color: #999;
			}
			
			.collect-time {
				font-size: 24rpx;
				color: #999;
			}
		}
		
		.goods-tags {
			display: flex;
			flex-wrap: wrap;
			gap: 10rpx;
			
			.tag {
				font-size: 20rpx;
				color: #f36c8a;
				background-color: rgba(243, 108, 138, 0.1);
				padding: 4rpx 12rpx;
				border-radius: 4rpx;
			}
		}
	}
	
	.item-actions {
		display: flex;
		flex-direction: column;
		justify-content: flex-end;
		margin-left: 20rpx;
		
		.action-btn {
			display: flex;
			flex-direction: column;
			align-items: center;
			margin-bottom: 20rpx;
			
			&:last-child {
				margin-bottom: 0;
			}
			
			&.cart-btn {
				width: 140rpx;
				height: 60rpx;
				background-color: #fff;
				border: 1rpx solid #f36c8a;
				border-radius: 30rpx;
				justify-content: center;
				
				uni-icons {
					margin-bottom: 5rpx;
				}
				
				text {
					font-size: 22rpx;
					color: #f36c8a;
				}
			}
			
			&.delete-btn {
				width: 60rpx;
				height: 60rpx;
				background-color: #f9f9f9;
				border-radius: 50%;
				justify-content: center;
			}
		}
	}
}

/* 店铺收藏列表样式 */
.shop-list {
	padding: 20rpx 30rpx;
}

.shop-item {
	background-color: #fff;
	border-radius: 16rpx;
	padding: 20rpx;
	margin-bottom: 20rpx;
	box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
	
	.shop-header {
		display: flex;
		align-items: center;
		margin-bottom: 20rpx;
		cursor: pointer;
		
		.shop-logo {
			width: 80rpx;
			height: 80rpx;
			border-radius: 50%;
			margin-right: 15rpx;
			border: 1px solid #eee;
		}
		
		.shop-info {
			flex: 1;
			
			.shop-name {
				font-size: 32rpx;
				font-weight: bold;
				margin-bottom: 5rpx;
			}
			
			.shop-rating {
				display: flex;
				align-items: center;
				font-size: 24rpx;
				color: #666;
				
				.shop-sales {
					margin-left: 15rpx;
				}
			}
		}
		
		.shop-actions {
			display: flex;
			align-items: center;
			
			.follow-btn {
				width: 120rpx;
				height: 60rpx;
				background-color: #f36c8a;
				color: #fff;
				border-radius: 30rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				font-size: 24rpx;
				margin-right: 10rpx;
				
				&.followed {
					background-color: #f9f9f9;
					color: #999;
					border: 1rpx solid #e0e0e0;
				}
			}
		}
	}
	
	.shop-stats {
		display: flex;
		justify-content: space-around;
		padding-top: 20rpx;
		border-top: 1px solid #eee;
		
		.stat-item {
			display: flex;
			flex-direction: column;
			align-items: center;
			
			.stat-value {
				font-size: 32rpx;
				font-weight: bold;
				color: #EE425A;
				margin-bottom: 5rpx;
			}
			
			.stat-label {
				font-size: 24rpx;
				color: #999;
			}
		}
	}
}

/* 空状态样式 */
.empty-state {
	padding: 100rpx 0;
	text-align: center;
	
	.empty-image {
		width: 200rpx;
		height: 200rpx;
		margin-bottom: 30rpx;
		opacity: 0.6;
	}
	
	.empty-text {
		display: block;
		font-size: 28rpx;
		color: #999;
		margin-bottom: 40rpx;
	}
	
	.empty-btn {
		display: inline-block;
		width: 200rpx;
		height: 70rpx;
		background-color: #f36c8a;
		color: #fff;
		line-height: 70rpx;
		border-radius: 35rpx;
		font-size: 28rpx;
	}
}

/* 批量操作栏样式 */
.batch-actions {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	height: 100rpx;
	background-color: #fff;
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 0 30rpx;
	box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.1);
	
	.select-all {
		display: flex;
		align-items: center;
		
		uni-icons {
			margin-right: 15rpx;
		}
		
		text {
			font-size: 28rpx;
			color: #333;
		}
	}
	
	.batch-delete {
		width: 200rpx;
		height: 70rpx;
		background-color: #f36c8a;
		color: #fff;
		border-radius: 35rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
	}
}
</style>