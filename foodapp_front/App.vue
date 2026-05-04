<script>
	// 最大父组件
	export default {
		
		globalData:{
	baseUrl:'http://localhost:8089/',
	cid:1,
	curGoods:{},
	cartAllTotalNums:0,
	// 登录状态相关
	isLogin: false,
	userInfo: null,
	currentUserId: ''
},
	onLaunch: function() {
		console.warn('当前组件仅支持 uni_modules 目录结构 ，请升级 HBuilderX 到 3.1.0 版本以上！')
		console.log('App Launch')
		// 从本地存储恢复登录状态
		this.restoreLoginState()
	},
	methods: {
		// 从本地存储恢复登录状态
		restoreLoginState() {
			try {
				const token = uni.getStorageSync('userToken') || uni.getStorageSync('token')
				const userId = uni.getStorageSync('userId')
				const username = uni.getStorageSync('username')
				const userInfo = uni.getStorageSync('userInfo')
				
				if (token && userId && username) {
					this.globalData.isLogin = true
					this.globalData.userInfo = userInfo
					this.globalData.currentUserId = userId
					this.globalData.cid = userId // 更新全局cid为当前用户ID
					this.globalData.userId = userId
					this.globalData.userToken = token
					this.globalData.username = username
				}
			} catch (e) {
				console.error('恢复登录状态失败:', e)
			}
		}
	},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		},
		// // 应用保存的主题色
		//     const savedColor = uni.getStorageSync('themeColor')
		//     if (savedColor) {
		//       document.documentElement.style.setProperty('--theme-color', savedColor)
		//     }

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
