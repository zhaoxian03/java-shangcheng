<template>
   <!-- 轮播组件 -->
   <swiper class="bannerimg" autoplay="true" interval="3000">
	   <swiper-item>
		       <image src="/static/lunbo1.png" class="bannerimg"></image>
	   </swiper-item>
	   <swiper-item>
	   		   <image src="/static/lunbo2.png" class="bannerimg"></image>
	   </swiper-item>
	   <swiper-item>
	   		   <image src="/static/lunbo3.png" class="bannerimg"></image>
	   </swiper-item>
   </swiper>
   <button @click="dumpPage" class="dumpbtn">跳过</button>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		onLoad() {
			// 定时器
			setTimeout(this.dumpPage,9000)
		},
		methods: {
			dumpPage(){
				uni.switchTab({
					url:"/pages/index/index"
				})
			}
		}
	}
</script>

<style>
.bannerimg{
	/* vw(view width)   vh(view height) */
	width: 100vw;
	height: 100vh;
}
.dumpbtn{
	position: absolute;
	bottom: 50px;
	right: 20px;
	background-color: green;
	color: #FFF;
}
</style>