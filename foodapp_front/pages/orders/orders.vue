<template>
	<view>
		<!-- 固定顶部区域（搜索 + 分类） -->
		<view class="top-fixed">
			<!-- 头部 -->
		<view class="header-search">
			<view class="header-search-back">
				<uni-icons type="back" size="24" color="#999999" @click="back"></uni-icons>
				<view class="header-search-box">
					<uni-icons type="search" size="24" color="#EEEEEE" style="margin-right: 15rpx;"></uni-icons>
					<input
						type="text"
						class="header-search-input"
						v-model="searchKeyword"
						placeholder="搜索商品名称"
						@input="onSearchInput"
						@confirm="applyFilter"
						confirm-type="search"
					/>
				</view>
			</view>
		</view>
		<!-- 头部 -->

			<!-- 导航 -->
			<view class="nav">
				<text :class="{active: currentTab === 'all'}" @click="switchTab('all')">全部</text>
				<text :class="{active: currentTab === 'waitPay'}" @click="switchTab('waitPay')">待支付</text>
				<text :class="{active: currentTab === 'waitSend'}" @click="switchTab('waitSend')">待发货</text>
				<text :class="{active: currentTab === 'waitReceive'}" @click="switchTab('waitReceive')">待收货</text>
				<text :class="{active: currentTab === 'evaluate'}" @click="switchTab('evaluate')">评价</text>
				<text :class="{active: currentTab === 'afterSale'}" @click="switchTab('afterSale')">售后</text>
			</view>
			<!-- 导航 -->
		</view>

		<!-- 顶部占位，避免内容被固定区域遮挡 -->
		<view class="top-spacer"></view>

		<!-- 订单列表 -->
		<view class="buygoodslist" v-for="(item,index) in orderlist" :key="item.orderid">
			<view class="buygoodslist-shop">
				<view class="shop-info">
					<image class="shop-logo" :src="item.shop.slogo" v-if="item.shop.slogo!=''  && item.shop.slogo!=null" @error="onImageError($event, item.shop, 'slogo')"></image>
					<uni-icons class="shop-logo-placeholder" type="shop" size="28" v-else></uni-icons>
					<text class="shop-name">{{item.shop.sname}}</text>
				</view>
				<view
					class="status"
					:class="{
						'status-paid': item.orderstatus==1, 
						'status-cancelled': item.orderstatus==3,
						'status-success': item.orderstatus==2
					}"
				>
					{{item.orderstatusStr}}
				</view>
			</view>
			<!-- 收货地址信息 -->
			<view class="order-address" v-if="item.ordersDetail && item.ordersDetail.length > 0">
				<view class="address-info">
					<text class="address-name">{{item.ordersDetail[0].receiveName || '暂无收货人'}}</text>
					<text class="address-phone">{{item.ordersDetail[0].tel || '暂无联系电话'}}</text>
				</view>
				<view class="address-detail">
					{{item.ordersDetail[0].province || ''}}{{item.ordersDetail[0].city || ''}}{{item.ordersDetail[0].district || ''}}{{item.ordersDetail[0].detailAddress || '暂无详细地址'}}
				</view>
			</view>
			<view class="buygoodslist-curgoods" v-for="(ditem,dindex) in item.ordersDetail" :key="ditem.odid || dindex">
				<image :src="ditem.icon" class="curgoods-img" @error="onImageError($event, ditem)"></image>
				<view class="buygoodslist-curgoods-content">
					<view class="curgoods-content-gname">
					{{ditem.gname || '未知商品'}}
				</view>
					<view class="curgoods-content-text">
						{{ditem.bcontent}}
					</view>
				</view>
				<view class="curgoods-content-price">
					<view>
						<text class="danwei">￥</text>
						<text class="price">{{ditem.buyPrice}}</text>
					</view>
					<view>
						<text class="danwei">X</text>
						<text class="buynums">{{ditem.buyNums}}</text>
					</view>
				</view>
			</view>

			<view class="fahuo">
				<text class="fahuo-xianhuo">现货</text> 48小时内发货
			</view>

			<view class="totalprice">
				<text v-if="item.orderstatus==0">需</text>
				<text v-else-if="item.orderstatus==1||item.orderstatus==2">实</text>
				<text v-else>应</text>
				<text>付款￥{{(Number(item.buyTotalPrice || 0) + Number(item.postprice || 0)).toFixed(2)}}</text>
			</view>

			<!-- 操作按钮 -->
			<view class="contrllerbtn" v-if="item.orderstatus==0">
				<!-- 待支付 -->
				<view class="btn" @click="clickCancelorders(item)">取消订单</view>
							<view class="btn" @click="openAddressModal(item)">修改地址</view>
							<view class="btn weizhifubtn" @click="clickPayOrders(item)">
					去支付
					<uni-countdown
						:font-size="12"
						:show-day="false"
						:hour="0"
						:minute="4"
						:second="0"
						:show-colon="true"
						@timeup="timeup(item)"
						splitorColor="#FFFFFF"
						color="#FFFFFF"
					></uni-countdown>
				</view>
			</view>

			<view class="contrllerbtn" v-if="item.orderstatus==1">
				<!-- 已支付 -->
				<view class="btn" @click="addcart(item)">加入购物车</view>
				<view class="btn" @click="viewLogistics(item)">查看物流</view>
				<view class="btn pingjiabtn" @click="toEvaluatePage(item)">评价</view>
			</view>

			<view class="contrllerbtn" v-if="item.orderstatus==2">
				<!-- 交易成功 -->
				<view class="btn" @click="addcart(item)">加入购物车</view>
				<view class="btn" @click="viewLogistics(item)">查看物流</view>
				<view class="btn pingjiabtn" @click="toEvaluatePage(item, true)">追加评价</view>
			</view>

			<view class="contrllerbtn" v-if="item.orderstatus==3 || item.orderstatus==4">
				<!-- 已取消 / 退单 -->
				<view class="btn" @click="clickDelorders(item)">删除订单</view>
				<view class="btn" @click="addcart(item)">加入购物车</view>
			</view>
			<!-- 操作按钮 -->
		</view>
		<!-- 订单列表 -->

		<!-- 空状态提示 -->
		<view class="empty-state" v-if="orderlist.length === 0">
			<image src="/static/empty-order.png" class="empty-img"></image>
			<text class="empty-text">暂无订单</text>
			<view class="empty-btn" @click="goShopping">去逛逛</view>
		</view>

		<!-- 加载更多提示 -->
		<view class="load-more" v-if="orderlist.length > 0">
			<text v-if="isLoading">加载中...</text>
			<text v-else-if="!hasMore">没有更多了</text>
			<text v-else @click="loadMore">上拉加载更多</text>
		</view>

		<!-- 物流信息弹窗（保持原逻辑） -->
		<view class="logistics-modal" v-if="showLogisticsModal" @click="hideLogisticsModal">
			<view class="modal-content" @click.stop>
				<view class="modal-header">
					<text class="modal-title">物流信息</text>
					<uni-icons type="close" size="25" @click="hideLogisticsModal"></uni-icons>
				</view>

				<scroll-view scroll-y class="modal-body">
					<!-- 订单信息 -->
					<view class="order-info" v-if="currentLogistics.goodsName">
						<view class="info-row">
							<text class="label">订单编号：</text>
							<text class="value">{{ currentLogistics.orderId }}</text>
						</view>
						<view class="info-row">
							<text class="label">商品名称：</text>
							<text class="value">{{ currentLogistics.goodsName }}</text>
						</view>
						<view class="info-row" v-if="currentLogistics.totalPrice">
							<text class="label">订单金额：</text>
							<text class="value">￥{{ currentLogistics.totalPrice.toFixed(2) }}</text>
						</view>
						<view class="info-row" v-if="currentLogistics.note">
							<text class="label">配送备注：</text>
							<text class="value note">{{ currentLogistics.note }}</text>
						</view>
					</view>

					<!-- 快递信息 -->
					<view class="logistics-info">
						<view class="info-row">
							<text class="label">快递公司：</text>
							<text class="value">{{ currentLogistics.company }}</text>
						</view>
						<view class="info-row">
							<text class="label">运单号码：</text>
							<text class="value">{{ currentLogistics.trackingNumber }}</text>
							<text class="copy-btn" @click="copyTrackingNumber">复制</text>
						</view>
						<view class="info-row">
							<text class="label">收货人：</text>
							<text class="value">{{ currentLogistics.receiver }}</text>
						</view>
						<view class="info-row">
							<text class="label">联系电话：</text>
							<text class="value">{{ currentLogistics.phone }}</text>
						</view>
						<view class="info-row">
							<text class="label">收货地址：</text>
							<text class="value">{{ currentLogistics.address }}</text>
						</view>
					</view>

					<!-- 物流轨迹 -->
					<view class="tracking-section" v-if="trackingList.length > 0">
						<view class="section-title">物流轨迹</view>
						<view class="timeline">
							<view class="timeline-item" v-for="(item, index) in trackingList" :key="index"
								:class="{ 'active': index === 0 }">
								<view class="timeline-dot"></view>
								<view class="timeline-content">
									<view class="tracking-time">{{ item.time }}</view>
									<view class="tracking-status">{{ item.status }}</view>
									<view class="tracking-location" v-if="item.location">
										<uni-icons type="location-filled" size="16" color="#999"></uni-icons>
										{{ item.location }}
									</view>
								</view>
							</view>
						</view>
					</view>

					<!-- 没有物流信息 -->
					<view class="empty-tips" v-else>
						<uni-icons type="info" size="40" color="#999"></uni-icons>
						<text>暂无物流信息</text>
					</view>
				</scroll-view>

				<view class="modal-footer">
					<button class="confirm-btn" @click="hideLogisticsModal">确定</button>
				</view>
			</view>
		</view>
		<!-- 物流信息弹窗 -->
		
		<!-- 地址选择弹窗 -->
		<view class="address-modal" v-if="showAddressModal">
			<!-- 遮罩层 -->
			<view class="modal-mask" @click="closeAddressModal"></view>
			
			<!-- 弹窗内容 -->
			<view class="modal-content">
				<!-- 弹窗头部 -->
				<view class="modal-header">
					<text class="modal-title">选择收货地址</text>
					<view class="modal-close" @click="closeAddressModal">
						<uni-icons type="close" size="24" color="#999"></uni-icons>
					</view>
				</view>
				
				<!-- 地址列表 -->
				<scroll-view class="modal-address-list" scroll-y>
					<view v-if="loadingAddresses" class="loading">加载中...</view>
					
					<view v-else-if="addressList.length === 0" class="empty-address">
						<text>暂无收货地址</text>
						<view class="add-address-btn" @click="goToAddAddress">
							<uni-icons type="plus" size="18" color="#4a90e2"></uni-icons>
							<text>添加地址</text>
						</view>
					</view>
					
					<view 
						class="modal-address-item" 
						v-for="item in addressList" 
						:key="item.id"
						@click="selectAddressFromModal(item)"
					>
						<view class="address-header">
							<text class="address-name">{{ item.name || item.receiveName }}</text>
							<text class="address-phone">{{ item.phone || item.tel }}</text>
							<text class="default-tag" v-if="item.isDefault === 1">默认</text>
						</view>
						<view class="address-detail">
							{{ item.province || '' }}{{ item.city || '' }}{{ item.district || '' }}{{ item.detail || item.detailAddress || '' }}
						</view>
					</view>
				</scroll-view>
				
				<!-- 底部添加按钮 -->
				<view class="modal-footer">
					<view class="add-address-btn" @click="goToAddAddress">
						<uni-icons type="plus" size="24" color="#4a90e2"></uni-icons>
						<text>添加新地址</text>
					</view>
				</view>
			</view>
		</view>
		<!-- 地址选择弹窗 -->
	</view>
</template>

<script>
import fDate from "@/common/utils/formatDateUtil.js"
import { addressApi } from '../../api/address.js'
const app = getApp({
	allowDefault: true
})
export default {
	data() {
		return {
			// 当前用于展示的订单列表（根据标签筛选后）
			orderlist: [],
			// 原始订单列表（全部）
			allOrders: [],
			// 搜索关键词（商品名称）
			searchKeyword: '',
			// 分页相关数据
			currentPage: 1,
			hasMore: true,
			isLoading: false,
			// 物流相关数据
			showLogisticsModal: false,
			currentLogistics: {},
			trackingList: [],
			// 订单状态映射
			orderStatusMap: {
				0: '待支付',
				1: '已支付',
				2: '交易成功',
				3: '已取消',
				4: '退单'
			},
			// 当前选中标签
			currentTab: 'all',
			// 地址选择弹窗相关
			showAddressModal: false,
			addressList: [],
			loadingAddresses: false,
			currentOrder: null
		}
	},
	onShow() {
		// 检查登录状态
		if (!app.globalData.isLogin) {
			this.orderlist = []
			this.allOrders = []
			uni.showToast({
				title: '请先登录',
				icon: 'none'
			})
			return
		}
		this.refreshOrders()
	},
	onReachBottom() {
		// 检查登录状态
		if (!app.globalData.isLogin) return
		this.loadMore()
	},
	onPullDownRefresh() {
		// 检查登录状态
		if (!app.globalData.isLogin) {
			uni.stopPullDownRefresh()
			return
		}
		this.refreshOrders()
		setTimeout(() => {
			uni.stopPullDownRefresh()
		}, 1000)
	},
	methods: {
		back() {
			uni.switchTab({
				url: '../index/index'
			})
		},

		// 切换订单标签（前端筛选）
		switchTab(tab) {
			this.currentTab = tab
			this.applyFilter()
		},

		// 根据当前标签和搜索关键词筛选订单
		applyFilter() {
			if (!this.allOrders || this.allOrders.length === 0) {
				this.orderlist = []
				return
			}
			let statusFilter = null
			switch (this.currentTab) {
				case 'waitPay':
					statusFilter = [0]
					break
				case 'waitSend':
					// 简单约定：已支付视为待发货
					statusFilter = [1]
					break
				case 'waitReceive':
					// 简单约定：交易成功视为已收货，待收货可以按 1/2 扩展
					statusFilter = [2]
					break
				case 'evaluate':
					// 需要评价：已收货/交易成功
					statusFilter = [2]
					break
				case 'afterSale':
					statusFilter = [4]
					break
				case 'all':
				default:
					statusFilter = null
			}
			// 1. 先按状态筛选
			let result = statusFilter
				? this.allOrders.filter(o => statusFilter.includes(o.orderstatus))
				: [...this.allOrders]

			// 2. 再按商品名称关键字筛选
			const kw = (this.searchKeyword || '').trim().toLowerCase()
			if (kw) {
				result = result.filter(order => {
					if (!order.ordersDetail || order.ordersDetail.length === 0) return false
					return order.ordersDetail.some(d => {
						const name = (d.gname || '').toLowerCase()
						return name.includes(kw)
					})
				})
			}

			this.orderlist = result
		},

		// 搜索输入事件
		onSearchInput(e) {
			this.searchKeyword = e.detail.value
			this.applyFilter()
		},

		// 查看物流（原逻辑不变）
		viewLogistics(order) {
			this.getLogisticsInfo(order.orderid, order, (logisticsData) => {
				this.currentLogistics = logisticsData
				this.trackingList = this.generateTrackingList(order, logisticsData)
				this.showLogisticsModal = true
			})
		},

		getLogisticsInfo(orderId, orderData, callback) {
			const cachedData = uni.getStorageSync(`logistics_${orderId}`);
			if (cachedData) {
				setTimeout(() => {
					callback && callback(cachedData);
				}, 100);
				return;
			}
			let logisticsData;
			if (orderData.ordersDetail && orderData.ordersDetail.length > 0) {
				const firstItem = orderData.ordersDetail[0];
				const goodsName = firstItem.gname || '';
				if (goodsName.includes('碧根果') || goodsName.includes('坚果') || goodsName.includes('零食') || goodsName.includes('花生')) {
					logisticsData = {
						orderId: orderId,
						trackingNumber: `SF${Date.now().toString().slice(-12)}`,
						company: '顺丰速运',
						receiver: '红红儿',
						phone: '13888888888',
						address: '北京市海淀区中关村大街',
						goodsName: goodsName,
						totalPrice: orderData.buyTotalPrice,
						note: '食品类商品，优先配送'
					};
				} else if (goodsName.includes('衬衫') || goodsName.includes('服装') || goodsName.includes('衣服') || goodsName.includes('工作服')) {
					logisticsData = {
						orderId: orderId,
						trackingNumber: `YTO${Date.now().toString().slice(-11)}`,
						company: '圆通速递',
						receiver: '红红儿',
						phone: '13888888888',
						address: '上海市浦东新区陆家嘴',
						goodsName: goodsName,
						totalPrice: orderData.buyTotalPrice,
						note: '服装类商品'
					};
				} else if (goodsName.includes('被子') || goodsName.includes('家纺') || goodsName.includes('被芯') || goodsName.includes('保暖')) {
					logisticsData = {
						orderId: orderId,
						trackingNumber: `ZTO${Date.now().toString().slice(-11)}`,
						company: '中通快递',
						receiver: '红红儿',
						phone: '13888888888',
						address: '广州市天河区珠江新城',
						goodsName: goodsName,
						totalPrice: orderData.buyTotalPrice,
						note: '大件商品，特殊包装'
					};
				} else if (goodsName.includes('大豆被') || goodsName.includes('冬被') || goodsName.includes('棉被')) {
					logisticsData = {
						orderId: orderId,
						trackingNumber: `STO${Date.now().toString().slice(-11)}`,
						company: '申通快递',
						receiver: '红红儿',
						phone: '13888888888',
						address: '深圳市南山区科技园',
						goodsName: goodsName,
						totalPrice: orderData.buyTotalPrice,
						note: '家纺类商品'
					};
				} else if (goodsName.includes('香辣') || goodsName.includes('辣椒') || goodsName.includes('小吃')) {
					logisticsData = {
						orderId: orderId,
						trackingNumber: `YD${Date.now().toString().slice(-11)}`,
						company: '韵达速递',
						receiver: '红红儿',
						phone: '13888888888',
						address: '成都市锦江区春熙路',
						goodsName: goodsName,
						totalPrice: orderData.buyTotalPrice,
						note: '零食类商品'
					};
				} else {
					logisticsData = {
						orderId: orderId,
						trackingNumber: `JD${Date.now().toString().slice(-12)}`,
						company: '京东物流',
						receiver: '红红儿',
						phone: '13888888888',
						address: '杭州市西湖区文三路',
						goodsName: goodsName,
						totalPrice: orderData.buyTotalPrice,
						note: '普通商品'
					};
				}
			} else {
				logisticsData = {
					orderId: orderId,
					trackingNumber: `EMS${Date.now().toString().slice(-12)}`,
					company: 'EMS',
					receiver: '红红儿',
					phone: '13888888888',
					address: '西安市雁塔区小寨',
					goodsName: '商品',
					totalPrice: orderData.buyTotalPrice || 0,
					note: ''
				};
			}
			if (orderData.buyTotalPrice > 500) {
				logisticsData.company = '顺丰速运';
				logisticsData.note = '高价商品，优先配送';
				logisticsData.trackingNumber = `SF${Date.now().toString().slice(-12)}`;
			} else if (orderData.buyTotalPrice > 200) {
				logisticsData.company = '京东物流';
				logisticsData.note = '中等价位商品';
				logisticsData.trackingNumber = `JD${Date.now().toString().slice(-12)}`;
			}
			if (orderData.orderstatus === 3) {
				logisticsData.note = '订单已取消，物流信息仅供参考';
			}
			uni.setStorageSync(`logistics_${orderId}`, logisticsData);
			setTimeout(() => {
				callback && callback(logisticsData);
			}, 300);
		},

		generateTrackingList(orderData, logisticsData) {
			const trackingList = [];
			const now = new Date();
			const orderTime = new Date(now.getTime() - (2 + Math.random() * 2) * 24 * 60 * 60 * 1000);
			const goodsName = logisticsData.goodsName || '';
			const isFreshFood = goodsName.includes('碧根果') || goodsName.includes('坚果') || goodsName.includes('花生');
			const isClothing = goodsName.includes('衬衫') || goodsName.includes('服装') || goodsName.includes('衣服');
			const isBedding = goodsName.includes('被子') || goodsName.includes('被芯') || goodsName.includes('大豆被');
			const isSnack = goodsName.includes('香辣') || goodsName.includes('零食') || goodsName.includes('小吃');

			const add = (h, m) => this.formatRelativeTime(orderTime, h, m);

			if (isFreshFood) {
				trackingList.push(
					{ time: add(-24, 0), status: '订单创建成功', location: '系统' },
					{ time: add(-23, 30), status: '商家已接单，开始拣货', location: '商家仓库' },
					{ time: add(-23, 0), status: '【顺丰冷链】已揽收', location: '商家仓库' },
					{ time: add(-22, 0), status: '【顺丰冷链】快件到达生鲜转运中心', location: '生鲜转运中心' },
					{ time: add(-20, 0), status: '【顺丰冷链】快件装车，发往目的地', location: '生鲜转运中心' },
					{ time: add(-18, 0), status: '【顺丰冷链】快件到达目的地转运中心', location: '目的地转运中心' },
					{ time: add(-17, 0), status: '【顺丰冷链】开始配送，生鲜优先', location: '配送站' },
					{ time: add(-16, 30), status: '已签收（生鲜食品请尽快食用）', location: logisticsData.address }
				);
			} else if (isClothing) {
				trackingList.push(
					{ time: add(-48, 0), status: '订单创建成功', location: '系统' },
					{ time: add(-47, 30), status: '商家已接单', location: '商家仓库' },
					{ time: add(-46, 0), status: `【${logisticsData.company}】已揽收`, location: '商家仓库' },
					{ time: add(-44, 0), status: `【${logisticsData.company}】快件到达转运中心`, location: '转运中心' },
					{ time: add(-42, 0), status: `【${logisticsData.company}】快件发往目的地`, location: '转运中心' },
					{ time: add(-40, 0), status: `【${logisticsData.company}】快件到达配送站`, location: '配送站' },
					{ time: add(-39, 30), status: `【${logisticsData.company}】正在派送中`, location: '配送站' },
					{ time: add(-39, 0), status: '已签收', location: logisticsData.address }
				);
			} else if (isBedding) {
				trackingList.push(
					{ time: add(-72, 0), status: '订单创建成功', location: '系统' },
					{ time: add(-71, 30), status: '商家已接单（大件商品需特殊包装）', location: '商家仓库' },
					{ time: add(-70, 0), status: '商品已打包完成', location: '商家仓库' },
					{ time: add(-68, 0), status: `【${logisticsData.company}】大件物流已揽收`, location: '商家仓库' },
					{ time: add(-65, 0), status: `【${logisticsData.company}】快件到达大件物流中心`, location: '大件物流中心' },
					{ time: add(-62, 0), status: `【${logisticsData.company}】大件商品装车完成`, location: '大件物流中心' },
					{ time: add(-58, 0), status: `【${logisticsData.company}】快件到达目的地大件物流中心`, location: '目的地大件物流中心' },
					{ time: add(-56, 0), status: '配送员联系收件人预约配送时间', location: '配送站' },
					{ time: add(-54, 0), status: '已签收（大件商品配送完成）', location: logisticsData.address }
				);
			} else if (isSnack) {
				trackingList.push(
					{ time: add(-36, 0), status: '订单创建成功', location: '系统' },
					{ time: add(-35, 30), status: '商家已接单', location: '商家仓库' },
					{ time: add(-34, 0), status: `【${logisticsData.company}】已揽收`, location: '商家仓库' },
					{ time: add(-32, 0), status: `【${logisticsData.company}】快件到达转运中心`, location: '转运中心' },
					{ time: add(-30, 0), status: `【${logisticsData.company}】快件发往目的地`, location: '转运中心' },
					{ time: add(-28, 0), status: `【${logisticsData.company}】快件到达目的地转运中心`, location: '目的地转运中心' },
					{ time: add(-26, 0), status: `【${logisticsData.company}】快件到达配送站`, location: '配送站' },
					{ time: add(-25, 30), status: `【${logisticsData.company}】正在派送中`, location: '配送站' },
					{ time: add(-25, 0), status: '已签收（零食请尽快食用）', location: logisticsData.address }
				);
			} else {
				trackingList.push(
					{ time: add(-36, 0), status: '订单创建成功', location: '系统' },
					{ time: add(-35, 30), status: '商家已接单', location: '商家仓库' },
					{ time: add(-34, 0), status: `【${logisticsData.company}】已揽收`, location: '商家仓库' },
					{ time: add(-32, 0), status: `【${logisticsData.company}】快件到达转运中心`, location: '转运中心' },
					{ time: add(-30, 0), status: `【${logisticsData.company}】快件发往目的地`, location: '转运中心' },
					{ time: add(-28, 0), status: `【${logisticsData.company}】快件到达目的地转运中心`, location: '目的地转运中心' },
					{ time: add(-26, 0), status: `【${logisticsData.company}】快件到达配送站`, location: '配送站' },
					{ time: add(-25, 30), status: `【${logisticsData.company}】正在派送中`, location: '配送站' },
					{ time: add(-25, 0), status: '已签收', location: logisticsData.address }
				);
			}
			if (orderData.orderstatus === 3) {
				trackingList.unshift({
					time: add(0, 0),
					status: '订单已取消，物流信息仅供参考',
					location: '系统'
				});
			}
			return trackingList.sort((a, b) => new Date(b.time) - new Date(a.time));
		},

		formatRelativeTime(baseTime, addHours, addMinutes) {
			const time = new Date(baseTime.getTime());
			time.setHours(time.getHours() + addHours);
			time.setMinutes(time.getMinutes() + addMinutes);

			return time.toLocaleString('zh-CN', {
				year: 'numeric',
				month: '2-digit',
				day: '2-digit',
				hour: '2-digit',
				minute: '2-digit',
				second: '2-digit',
				hour12: false
			}).replace(/\//g, '-');
		},

		copyTrackingNumber() {
			if (!this.currentLogistics.trackingNumber) return;
			uni.setClipboardData({
				data: this.currentLogistics.trackingNumber,
				success: () => {
					uni.showToast({
						title: '运单号已复制',
						icon: 'success'
					})
				}
			})
		},

		hideLogisticsModal() {
			this.showLogisticsModal = false
		},

		// 刷新订单
		refreshOrders() {
			this.currentPage = 1
			this.hasMore = true
			this.allOrders = []
			this.orderlist = []
			this.loadOrders(1)
		},

		// 加载更多
		loadMore() {
			if (this.isLoading || !this.hasMore) return
			this.isLoading = true
			const nextPage = this.currentPage + 1

			uni.showLoading({
				title: '加载更多...'
			})

			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			uni.request({
				url: app.globalData.baseUrl + 'api/orders/getorderslist/' + nextPage,
				method: 'GET',
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				success: (res) => {
					uni.hideLoading()
					this.isLoading = false

					if (res.data.code == 200 && res.data.data.length > 0) {
						const newOrders = res.data.data.map(order => {
							if (!order.orderstatusStr) {
								order.orderstatusStr = this.orderStatusMap[order.orderstatus] || '未知状态'
							}
							return order
						})

						this.currentPage = nextPage
						this.allOrders = [...this.allOrders, ...newOrders]
						this.applyFilter()

						if (res.data.data.length < 10) {
							this.hasMore = false
						}
					} else {
						this.hasMore = false
						uni.showToast({
							title: '没有更多了',
							icon: 'none'
						})
					}
				},
				fail: (err) => {
					uni.hideLoading()
					this.isLoading = false
					console.error('加载更多失败:', err)
					uni.showToast({
						title: '加载失败',
						icon: 'none'
					})
				}
			})
		},

		goShopping() {
			uni.switchTab({
				url: '../index/index'
			})
		},

		loadOrders(pageindex) {
			uni.showLoading({
				title: '加载中...'
			})

			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			uni.request({
				url: app.globalData.baseUrl + 'api/orders/getorderslist/' + pageindex,
				method: 'GET',
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				success: (res) => {
					uni.hideLoading()

					if (res.data.code == 200) {
						const list = res.data.data.map(order => {
							if (!order.orderstatusStr) {
								order.orderstatusStr = this.orderStatusMap[order.orderstatus] || '未知状态'
							}
							return order
						})
						this.allOrders = list
						// 如果首屏少于一页，说明没有更多
						if (list.length < 10) {
							this.hasMore = false
						}
						this.applyFilter()
					} else {
						uni.showToast({
							title: res.data.msg || '加载失败',
							icon: 'none'
						})
					}
				},
				fail: (err) => {
					uni.hideLoading()
					console.error('加载订单失败:', err)
					uni.showToast({
						title: '网络错误，请重试',
						icon: 'none'
					})
				}
			})
		},

		// 去支付：直接标记为已支付并同步后端
		clickPayOrders(curOrder) {
			// 本地先更新（直接支付成功）
			this.allOrders.forEach(item => {
				if (item.orderid == curOrder.orderid) {
					item.orderstatus = 1
					item.orderstatusStr = this.orderStatusMap[1]
					item.paytime = fDate.formatTime(new Date())
				}
			})
			this.applyFilter()

			// 远程更新订单
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			uni.request({
				url: app.globalData.baseUrl + 'api/orders/updateorders',
				method: 'POST',
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				data: {
					...curOrder,
					orderstatus: 1,
					orderstatusStr: this.orderStatusMap[1],
					paytime: fDate.formatTime(new Date())
				},
				success: () => {
					uni.showToast({
						title: '支付成功',
						icon: 'success'
					})
				},
				fail: () => {
					uni.showToast({
						title: '同步订单失败',
						icon: 'none'
					})
				}
			})
		},

		addcart(curOrder) {
			// 远程获得原来的购物车
			this.loadCartList(curOrder)
		},

		loadCartList(curOrder) {
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			uni.request({
				url: app.globalData.baseUrl + 'api/cart/getcartlist',
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				success: (res) => {
					let cartlist = []
					if (res.data.code == 200) {
						cartlist = res.data.data || []
					}
					this.addNewCart(cartlist, curOrder)
				},
				fail: () => {
					uni.showToast({
						title: '加载购物车失败',
						icon: 'none'
					})
				}
			})
		},

		addNewCart(cartlist, curOrder) {
			let isFind = false;
			let curcartitem = null;

			// 标记已存在的订单商品
			curOrder.ordersDetail.forEach(orderitem => {
				orderitem.isFind = false;
			});

			cartlist.forEach(cartitem => {
				if (cartitem.sid == curOrder.sid) {
					isFind = true;
					curcartitem = cartitem;
					cartitem.goodsDetail.forEach(gitem => {
						curOrder.ordersDetail.forEach(orderitem => {
							if (gitem.gdid == orderitem.gdid) {
								// 确保数字类型转换
								gitem.buyNums = Number(gitem.buyNums || 0) + Number(orderitem.buyNums || 0);
								orderitem.isFind = true;
							}
						});
					});
				}
			});
			if (isFind && curcartitem) {
				curOrder.ordersDetail.forEach(orderitem => {
					if (!orderitem.isFind) {
						curcartitem.goodsDetail.push(orderitem);
					}
				});
			} else {
				// 确保所有数值字段都是数字类型
				let cart = {
					beizhu: curOrder.beizhu || '',
					buyTotalNums: Number(curOrder.buyTotalNums || 0),
					buyTotalPrice: Number(curOrder.buyTotalPrice || 0),
					cartid: null,
					cid: curOrder.cid,
					goodsDetail: curOrder.ordersDetail,
					postprice: Number(curOrder.postprice || 0),
					shop: curOrder.shop || {},
					sid: curOrder.sid
				};
				cartlist.push(cart);
			}

			this.compterCartList(cartlist);

			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			uni.request({
				url: app.globalData.baseUrl + 'api/cart/updatecartlist',
				method: "POST",
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				data: cartlist,
				dataType: 'json',
				success: () => {
					uni.switchTab({
						url: "../cart/cart"
					});
				},
				fail: () => {
					uni.showToast({
						title: '加入购物车失败',
						icon: 'none'
					})
				}
			});
		},

		compterCartList(cartlist) {
			cartlist.forEach(curCart => {
				let priceSum = 0;
				let numSum = 0;
				curCart.goodsDetail.forEach(ditem => {
					priceSum += Number(ditem.buyNums || 0) * Number(ditem.buyPrice || 0);
					numSum += Number(ditem.buyNums || 0);
				});
				curCart.buyTotalPrice = priceSum;
				curCart.buyTotalNums = numSum;
			});
		},

		clickDelorders(curOrder) {
			let that = this;
			uni.showModal({
				title: "温馨提示",
				content: '你确定要删除订单吗？',
				showCancel: true,
				success: function(res) {
					if (res.confirm) {
						that.delorders(curOrder);
					}
				}
			});
		},

		delorders(curOrder) {
			// 本地删除
			this.allOrders = this.allOrders.filter(item => item.orderid != curOrder.orderid)
			this.applyFilter()

			// 远程删除
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			uni.request({
				url: app.globalData.baseUrl + "api/orders/delorders/" + curOrder.orderid,
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				success: () => {},
				fail: () => {
					uni.showToast({
						title: '删除失败',
						icon: 'none'
					})
				}
			});
		},

		timeup(curOrder) {
			this.cancelorders(curOrder);
			uni.showToast({
				title: "支付时间超时，订单已取消",
				duration: 4000
			});
		},
		// 打开地址选择弹窗
		openAddressModal(order) {
			const userId = app.globalData.currentUserId || app.globalData.cid || ''
			if (!userId) {
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				})
				return
			}
			
			this.currentOrder = order
			this.showAddressModal = true
			this.loadAddressList()
		},
		
		// 关闭地址选择弹窗
		closeAddressModal() {
			this.showAddressModal = false
			this.currentOrder = null
		},
		
		// 加载地址列表
		async loadAddressList() {
			const userId = app.globalData.currentUserId || app.globalData.cid || ''
			if (!userId) return
			
			this.loadingAddresses = true
			try {
				const addresses = await addressApi.getList(userId)
				this.addressList = addresses || []
				console.log('加载地址列表成功:', this.addressList)
			} catch (error) {
				console.error('加载地址列表失败:', error)
				uni.showToast({
					title: '加载地址失败',
					icon: 'none'
				})
				this.addressList = []
			} finally {
				this.loadingAddresses = false
			}
		},
		
		// 从弹窗选择地址
		selectAddressFromModal(item) {
			console.log('从弹窗选择地址:', item)
			if (!this.currentOrder) return
			
			// 更新订单地址
			// 使用与其他订单更新相同的API端点
			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token')
			
			// 构建完整的订单对象，包含更新后的地址信息
				const updatedOrder = {
					...this.currentOrder,
					receiveName: item.name || item.receiveName,
					tel: item.phone || item.tel,
					address: `${item.province || ''}${item.city || ''}${item.district || ''}${item.detail || item.detailAddress || ''}`,
					province: item.province || '',
					city: item.city || '',
					district: item.district || '',
					detailAddress: item.detail || item.detailAddress || '',
					// 包含订单详情，更新地址信息
					ordersDetail: this.currentOrder.ordersDetail.map(detail => ({
						...detail,
						receiveName: item.name || item.receiveName,
						tel: item.phone || item.tel,
						province: item.province || '',
						city: item.city || '',
						district: item.district || '',
						detailAddress: item.detail || item.detailAddress || ''
					}))
				}
			
			uni.request({
				url: app.globalData.baseUrl + 'api/orders/updateorders',
				method: 'POST',
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				data: updatedOrder,
				success: (res) => {
					console.log('更新订单地址成功:', res.data)
					if (res.data.code === 200 || res.statusCode === 200) {
						// 保存当前订单ID，因为我们稍后会清空currentOrder
							const currentOrderId = this.currentOrder.orderid
							// 本地更新订单地址信息
							this.allOrders.forEach(order => {
								if (order.orderid === currentOrderId) {
									// 更新订单的地址信息
									order.receiveName = item.name || item.receiveName
									order.tel = item.phone || item.tel
									order.address = `${item.province || ''}${item.city || ''}${item.district || ''}${item.detail || item.detailAddress || ''}`
									order.province = item.province || ''
									order.city = item.city || ''
									order.district = item.district || ''
									order.detailAddress = item.detail || item.detailAddress || ''
									
									// 同时更新订单详情中的地址信息，因为模板中是从ordersDetail[0]获取地址的
									if (order.ordersDetail && order.ordersDetail.length > 0) {
										order.ordersDetail.forEach(detail => {
											detail.receiveName = item.name || item.receiveName
											detail.tel = item.phone || item.tel
											detail.province = item.province || ''
											detail.city = item.city || ''
											detail.district = item.district || ''
											detail.detailAddress = item.detail || item.detailAddress || ''
										})
									}
								}
							})
						this.applyFilter()
						uni.showToast({
							title: '地址更新成功',
							icon: 'success'
						})
					} else {
						uni.showToast({
							title: res.data.msg || '更新失败',
							icon: 'none'
						})
					}
					// 关闭弹窗并清空当前订单
					this.showAddressModal = false
					this.currentOrder = null
				},
				fail: (error) => {
					console.error('更新订单地址失败:', error)
					uni.showToast({
						title: '更新失败，请重试',
						icon: 'none'
					})
					// 关闭弹窗并清空当前订单
					this.showAddressModal = false
					this.currentOrder = null
				}
			})
		},
		
		// 跳转到添加地址页面
		goToAddAddress() {
			const userId = app.globalData.currentUserId || app.globalData.cid || ''
			if (!userId) {
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				})
				return
			}
			
			// 关闭弹窗
			this.showAddressModal = false
			this.currentOrder = null
			
			// 跳转到添加地址页面
			uni.navigateTo({
				url: `/pages/address/edit?userId=${userId}&from=orders`
			})
		},

		clickCancelorders(curOrder) {
			let that = this;
			uni.showModal({
				title: "温馨提示",
				content: '你确定要取消订单吗？',
				showCancel: true,
				success: function(res) {
					if (res.confirm) {
						that.cancelorders(curOrder);
					}
				}
			});
		},

		cancelorders(curOrder) {
			this.allOrders.forEach(item => {
				if (item.orderid == curOrder.orderid) {
					item.orderstatus = 3;
					item.orderstatusStr = this.orderStatusMap[3];
					item.paytime = "";
				}
			});
			this.applyFilter()

			const token = uni.getStorageSync('userToken') || uni.getStorageSync('token');
			uni.request({
				url: app.globalData.baseUrl + 'api/orders/updateorders',
				method: 'POST',
				header: {
					'Authorization': token ? `Bearer ${token}` : ''
				},
				data: {
					...curOrder,
					orderstatus: 3,
					orderstatusStr: this.orderStatusMap[3],
					paytime: ''
				},
				success: () => {},
				fail: () => {
					uni.showToast({
						title: '取消失败',
						icon: 'none'
					})
				}
			});
		},

		// 图片加载失败处理
		onImageError(e, imgItem, imgField = 'icon') {
			// 设置默认图片路径
			const defaultImage = '/static/goodsbanner01.png';
			// 更新当前图片为默认图片
			if (imgItem && imgItem[imgField]) {
				imgItem[imgField] = defaultImage;
			}
			// 防止重复触发错误
			const imgElement = e.target;
			if (imgElement) {
				imgElement.onerror = null;
			}
		},
			toEvaluatePage(curOrder, isAppend = false) {
				try {
					const orderStr = encodeURIComponent(JSON.stringify(curOrder));
					uni.navigateTo({
						url: `/pages/evaluate/evaluate?order=${orderStr}&isAppend=${isAppend}`
					});
				} catch (e) {
					uni.showToast({
						title: '参数传递失败',
						icon: 'none'
					});
				}
			}
	}
}
</script>

<style lang="scss">
	.top-fixed {
		position: fixed;
		top: 0; // 回到屏幕顶部
		left: 0;
		right: 0;
		z-index: 20;
		background-color: #fff;
		padding: calc(var(--status-bar-height) + 80rpx) 0 0rpx; // 增加顶部内边距，使搜索框向下移动
		box-sizing: border-box;
		box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.06);
		min-height: 200rpx; // 减小最小高度
		// 解决滚动时留白问题：背景色延伸到顶部
		&::before {
			content: '';
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			height: calc(var(--status-bar-height) + 80rpx);
			background-color: #fff;
			z-index: -1;
		}
	}

	.top-spacer {
		height: calc(var(--status-bar-height) + 240rpx); // 减小占位符高度，减小订单分类和订单之间的间距
	}

	// 搜索框区域：包含返回按钮、搜索输入框和更多选项
	.header-search {
		width: 95%;          // 占屏幕宽度95%
		margin: 0 auto;      // 水平居中
		box-sizing: border-box; // 盒模型计算方式
		display: flex;       // Flex布局
		justify-content: space-between; // 子元素两端对齐
		align-items: center; // 垂直居中
		height: 70rpx;       // 明确设置高度
		margin-bottom: 20rpx; // 增加与分类导航的间距

		// 返回按钮与搜索框容器
		.header-search-back {
			display: flex;       // Flex布局
			flex: 1;             // 占据剩余空间
			justify-content: flex-start; // 子元素左对齐
			align-items: center; // 垂直居中

			// 搜索框样式：浅灰色圆角输入框
			.header-search-box {
				flex: 1;             // 占据剩余空间
				height: 66rpx;       // 搜索框高度（rpx是uni-app适配单位）
				background-color: #F0F0F0; // 改为明显的浅灰色背景
				border-radius: 10rpx; // 圆角边框
				padding: 0rpx 20rpx; // 左右内边距
				display: flex;       // Flex布局
				align-items: center; // 内容垂直居中
				margin-left: 20rpx;  // 与返回按钮的间距

				// 搜索图标样式
				> uni-icons[type="search"] {
					color: #999; // 搜索图标改为灰色，更明显
				}

				// 搜索输入框样式
				.header-search-input {
					flex: 1;             // 占据剩余空间
					height: 100%;        // 填满搜索框高度
					font-size: 28rpx;    // 字体大小
					color: #333;         // 文字颜色
					background-color: transparent; // 透明背景
					border: none;        // 无边框
					outline: none;       // 取消聚焦边框
				}
				
				// 搜索输入框占位符样式
				.header-search-input::placeholder {
					color: #999;         // 占位符颜色
					font-size: 26rpx;    // 占位符字体大小
				}
			}
		}

		// 更多选项按钮（显示"..."）
		.dian {
			font-size: 40rpx;    // 字体大小
			margin-left: 20rpx;  // 与搜索框的间距
			color: #999;         // 设置明显的颜色
		}
	}

	// 订单分类导航：包含全部、待支付、待发货等分类标签
	.nav {
		width: 95%;          // 占屏幕宽度95%
		height: 70rpx;       // 导航栏高度
		margin: 20rpx auto;  // 20rpx上边距，水平居中
		display: flex;       // Flex布局
		justify-content: space-between; // 子元素均匀分布
		align-items: center; // 垂直居中
		font-size: 30rpx;    // 默认字体大小

		// 分类标签激活状态样式
		.active {
			font-size: 36rpx;    // 激活状态字体更大
			font-weight: bold;   // 字体加粗
			line-height: 70rpx;  // 行高与导航栏高度一致
			color: #F36C8A;       // 红色文字
		}

		// 激活状态下划线：红色指示线
		.active::after {
			content: "";         // 伪元素内容为空
			width: 80rpx;        // 下划线宽度
			height: 4rpx;        // 下划线高度
			background-color: #F36C8A; // 红色下划线
			display: block;      // 块级元素
			margin: 0rpx auto;   // 水平居中
		}
	}

	.buygoodslist {
		width: 100%;
		margin-bottom: 20rpx;
		padding: 30rpx;
		box-sizing: border-box;
		background-color: #FFF;
		border-radius: 20rpx;

		.buygoodslist-shop {
			font-size: 30rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.shop-info {
				display: flex;
				align-items: center;
			}

			.shop-logo {
				width: 50rpx;
				height: 50rpx;
				border-radius: 50%;
				object-fit: cover;
				margin-right: 10rpx;
			}

			.shop-logo-placeholder {
				margin-right: 10rpx;
			}

			.status {
				color: #F36C8A;
				font-weight: bold;

				&.status-paid {
					color: #07c160;
				}

				&.status-cancelled {
					color: #999;
				}

				&.status-success {
					color: #1890ff;
				}
			}
		}
		
		/* 订单地址样式 */
		.order-address {
			padding: 20rpx 0;
			margin: 20rpx 0;
			border-top: 1rpx solid #f0f0f0;
			border-bottom: 1rpx solid #f0f0f0;
			
			.address-info {
				display: flex;
				align-items: center;
				margin-bottom: 10rpx;
				
				.address-name {
					font-size: 32rpx;
					font-weight: bold;
					color: #333;
					margin-right: 20rpx;
				}
				
				.address-phone {
					font-size: 30rpx;
					color: #666;
				}
			}
			
			.address-detail {
				font-size: 28rpx;
				color: #666;
				line-height: 1.5;
				word-break: break-all;
			}
		}

		.buygoodslist-curgoods {
			display: flex;
			padding: 30rpx 0;

			.curgoods-img {
				width: 230rpx;
				height: 230rpx;
				border-radius: 5rpx;
				flex-shrink: 0;
			}

			.buygoodslist-curgoods-content {
				padding-left: 20rpx;
				box-sizing: border-box;
				height: 230rpx;
				display: flex;
				flex-direction: column;
				justify-content: space-between;

				.curgoods-content-gname {
					font-size: 30rpx;
					color: #555;
					font-weight: bold;
				}

				.curgoods-content-text {
					color: #777;
				}

			}

			.curgoods-content-price {
				display: flex;
				justify-content: flex-start;
				flex-direction: column;
				font-weight: bold;
				text-align: right;

				.danwei {
					font-size: 24rpx;
				}

				.price {
					font-size: 34rpx;
					color: #333;
				}

				.buynums {
					font-size: 30rpx;
					color: #333;
				}
			}
		}

		.fahuo {
			width: 100%;
			height: 70rpx;
			background-color: #fAfAfA;
			line-height: 70rpx;
			padding-left: 20rpx;

			.fahuo-xianhuo {
				font-weight: bold;
				font-size: 28rpx;
			}
		}

		.totalprice {
			height: 120rpx;
			text-align: right;
			line-height: 120rpx;
			font-size: 30rpx;
			font-weight: bold;
			color: #333;
		}

		.contrllerbtn {
			width: 100%;
			height: 70rpx;
			display: flex;
			justify-content: flex-end;
			align-items: center;

			.btn {
				min-width: 170rpx;
				height: 66rpx;
				padding: 0 10rpx;
				border: 2rpx solid #CCC;
				border-radius: 10rpx;
				margin-left: 10rpx;
				font-size: 26rpx;
				display: flex;
				justify-content: center;
				align-items: center;
				flex-wrap: nowrap;
			}

			.weizhifubtn {
				background-color: #F36C8A;
				color: #FFF;
			}

			.pingjiabtn {
				border: 2rpx solid orange;
				color: orange;
			}
		}
	}

	.empty-state {
		padding: 100rpx 0;
		text-align: center;

		.empty-img {
			width: 200rpx;
			height: 200rpx;
			margin-bottom: 30rpx;
		}

		.empty-text {
			display: block;
			color: #999;
			font-size: 30rpx;
			margin-bottom: 40rpx;
		}

		.empty-btn {
			width: 200rpx;
			height: 70rpx;
			background-color: #F36C8A;
			color: #fff;
			line-height: 70rpx;
			border-radius: 35rpx;
			margin: 0 auto;
		}
	}

	.load-more {
		text-align: center;
		padding: 30rpx;
		color: #999;
		font-size: 28rpx;
	}

	/* 物流弹窗样式（原样） */
	.logistics-modal {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background-color: rgba(0, 0, 0, 0.5);
		z-index: 1000;
		display: flex;
		justify-content: center;
		align-items: center;

		.modal-content {
			width: 90%;
			max-height: 80%;
			background-color: #fff;
			border-radius: 20rpx;
			overflow: hidden;
			display: flex;
			flex-direction: column;

			.modal-header {
				height: 100rpx;
				padding: 0 30rpx;
				display: flex;
				justify-content: space-between;
				align-items: center;
				border-bottom: 1rpx solid #f0f0f0;

				.modal-title {
					font-size: 34rpx;
					font-weight: bold;
					color: #333;
				}
			}

			.modal-body {
				flex: 1;
				padding: 30rpx;
				max-height: 60vh;

				.order-info {
					background-color: #f9f9f9;
					padding: 20rpx;
					border-radius: 10rpx;
					margin-bottom: 30rpx;

					.info-row {
						display: flex;
						align-items: center;
						margin-bottom: 15rpx;
						font-size: 28rpx;

						&:last-child {
							margin-bottom: 0;
						}

						.label {
							width: 140rpx;
							color: #666;
							flex-shrink: 0;
						}

						.value {
							flex: 1;
							color: #333;
							word-break: break-all;

							&.note {
								color: #F36C8A;
								font-weight: bold;
							}
						}
					}
				}

				.logistics-info {
					.info-row {
						display: flex;
						align-items: center;
						margin-bottom: 20rpx;
						font-size: 28rpx;

						.label {
							width: 140rpx;
							color: #666;
							flex-shrink: 0;
						}

						.value {
							flex: 1;
							color: #333;
							word-break: break-all;
						}

						.copy-btn {
							margin-left: 20rpx;
							color: #F36C8A;
							font-size: 24rpx;
							padding: 8rpx 16rpx;
							border: 1rpx solid #F36C8A;
							border-radius: 8rpx;
						}
					}
				}

				.tracking-section {
					margin-top: 30rpx;

					.section-title {
						font-size: 32rpx;
						font-weight: bold;
						color: #333;
						margin-bottom: 20rpx;
					}

					.timeline {
						.timeline-item {
							display: flex;
							margin-bottom: 30rpx;
							position: relative;
							padding-left: 40rpx;

							&:last-child {
								margin-bottom: 0;
							}

							&::before {
								content: '';
								position: absolute;
								left: 15rpx;
								top: 10rpx;
								width: 12rpx;
								height: 12rpx;
								border-radius: 50%;
								background-color: #ccc;
								border: 2rpx solid #fff;
								z-index: 2;
							}

							&::after {
								content: '';
								position: absolute;
								left: 20rpx;
								top: 22rpx;
								width: 2rpx;
								height: calc(100% + 30rpx);
								background-color: #e0e0e0;
							}

							&:last-child::after {
								display: none;
							}

							&.active::before {
								background-color: #07c160;
							}

							.timeline-content {
								flex: 1;

								.tracking-time {
									font-size: 26rpx;
									color: #999;
									margin-bottom: 8rpx;
								}

								.tracking-status {
									font-size: 28rpx;
									color: #333;
									margin-bottom: 5rpx;
									line-height: 1.4;
								}

								.tracking-location {
									font-size: 26rpx;
									color: #666;
									display: flex;
									align-items: center;

									uni-icons {
										margin-right: 8rpx;
									}
								}
							}
						}
					}
				}

				.empty-tips {
					text-align: center;
					padding: 60rpx 0;
					color: #999;

					uni-icons {
						display: block;
						margin-bottom: 20rpx;
					}
				}
			}

			.modal-footer {
				padding: 20rpx 30rpx;
				border-top: 1rpx solid #f0f0f0;

				.confirm-btn {
					width: 100%;
					height: 80rpx;
					background-color: #F36C8A;
					color: #fff;
					border: none;
					border-radius: 40rpx;
					font-size: 32rpx;
					font-weight: bold;

					&:active {
						opacity: 0.8;
					}
				}
			}
		}
	}
	
	/* 地址选择弹窗样式 */
	.address-modal {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		z-index: 1000;
	}
	
	.modal-mask {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background-color: rgba(0, 0, 0, 0.5);
	}
	
	.address-modal .modal-content {
		position: absolute;
		bottom: 0;
		left: 0;
		width: 100%;
		max-height: 70vh;
		background-color: #FFF;
		border-top-left-radius: 30rpx;
		border-top-right-radius: 30rpx;
		padding-bottom: 20rpx;
		box-sizing: border-box;
	}
	
	.address-modal .modal-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 30rpx;
		border-bottom: 1rpx solid #f0f0f0;
		height: 80rpx;
		box-sizing: border-box;
		
		.modal-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333;
		}
		
		.modal-close {
			width: 60rpx;
			height: 60rpx;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}
	
	.address-modal .modal-address-list {
		height: calc(70vh - 80rpx - 120rpx);
		padding: 20rpx;
	}
	
	.loading {
		text-align: center;
		padding: 40rpx 0;
		color: #999;
		font-size: 28rpx;
	}
	
	.empty-address {
		text-align: center;
		padding: 60rpx 0;
		color: #999;
		font-size: 28rpx;
		
		.add-address-btn {
			margin-top: 30rpx;
			display: inline-flex;
			align-items: center;
			color: #4a90e2;
			font-size: 28rpx;
			
			uni-icons {
				margin-right: 8rpx;
			}
		}
	}
	
	.modal-address-item {
		background-color: #f8f8f8;
		border-radius: 16rpx;
		padding: 25rpx;
		margin-bottom: 20rpx;
		transition: all 0.3s ease;
		border: 2rpx solid transparent;
		
		&.active {
			border-color: #4a90e2;
			background-color: rgba(74, 144, 226, 0.05);
		}
		
		.address-header {
			display: flex;
			align-items: center;
			margin-bottom: 15rpx;
			flex-wrap: wrap;
			
			.address-name {
				font-size: 32rpx;
				font-weight: bold;
				color: #333;
				margin-right: 20rpx;
			}
			
			.address-phone {
				font-size: 28rpx;
				color: #666;
				margin-right: 20rpx;
				margin-bottom: 10rpx;
			}
			
			.default-tag {
				background-color: #4a90e2;
				color: #fff;
				font-size: 22rpx;
				padding: 4rpx 12rpx;
				border-radius: 15rpx;
				margin-bottom: 10rpx;
			}
		}
		
		.address-detail {
			font-size: 28rpx;
			color: #333;
			line-height: 1.5;
			word-break: break-all;
			white-space: normal;
			width: 100%;
			flex-wrap: wrap;
		}
	}
	
	/* 确保新增按钮在弹窗底部 */
	.address-modal .add-address-btn {
		position: relative;
		z-index: 20;
		margin-bottom: 0;
	}
	
	.address-modal .modal-footer {
		padding: 20rpx;
		border-top: 1rpx solid #f0f0f0;
	}
	
	.address-modal .add-address-btn {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
		height: 80rpx;
		background-color: #fff;
		color: #4a90e2;
		border: 2rpx solid #4a90e2;
		border-radius: 40rpx;
		font-size: 30rpx;
		font-weight: 500;
		
		uni-icons {
			margin-right: 10rpx;
		}
	}
	
	.address-modal .modal-footer {
		padding: 20rpx;
		border-top: 1rpx solid #f0f0f0;
		height: 120rpx;
		box-sizing: border-box;
		position: relative;
		z-index: 10;
	}
	
	.add-address-btn {
		position: relative;
		z-index: 20;
		margin-bottom: 0;
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
		height: 80rpx;
		background-color: #fff;
		color: #4a90e2;
		border: 2rpx solid #4a90e2;
		border-radius: 40rpx;
		font-size: 30rpx;
		font-weight: 500;
		
		uni-icons {
			margin-right: 10rpx;
		}
	}
</style>

