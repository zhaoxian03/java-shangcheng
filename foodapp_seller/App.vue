<script>
	export default {
		onLaunch: function() {
			console.warn('当前组件仅支持 uni_modules 目录结构 ，请升级 HBuilderX 到 3.1.0 版本以上！')
			console.log('App Launch')
			// 全局登录状态检查
			this.checkGlobalLoginStatus()
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		},
		methods: {
			// 全局登录状态检查
			checkGlobalLoginStatus() {
				const sellerInfo = uni.getStorageSync('sellerInfo')
				const token = uni.getStorageSync('token')
				const pages = getCurrentPages()
				const currentPage = pages[pages.length - 1]
				
				// 如果用户已登录，且当前页面是登录页面，直接跳转到首页
				if (sellerInfo && token && currentPage && currentPage.route === 'pages/login/login') {
					uni.redirectTo({
						url: '/pages/index/index'
					})
				}
			}
		}
	}
</script>

<style lang="scss">
	/*每个页面公共css */
	@import '@/uni_modules/uni-scss/index.scss';
	/* #ifndef APP-NVUE */
	@import '@/static/customicons.css';
	// 设置整个项目的背景色
	page {
		background-color: #f5f5f5;
	}

	/* #endif */
	.example-info {
		font-size: 14px;
		color: #333;
		padding: 10px;
	}
</style>
