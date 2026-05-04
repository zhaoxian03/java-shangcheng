<template>
	<view class="cbcontainer">
		<view class="btn" :class="mynum>1?'active':''" @click="sub">-</view>{{mynum}}<view class="btn" :class="mynum<max?'active':''" @click="add">+</view>
	</view>
</template>

<script>
	export default {
		name:"ControllerBuyNumBtn",
		props:{
			max:{
				type:Number,
				default:99999999999
			},
			num:{
				type:Number,
				default:1
			},
			myid:{
				type:[Number,String],
				required:false
			}
		},
		data() {
			return {
				mynum:this.num
			};
		},
		methods:{
			sub(){
				if(this.mynum>1){
					this.mynum--
					this.setBuyNum()
				}
			},
			add(){
				if(this.mynum<this.max){
					this.mynum++
					this.setBuyNum()
				}
			},
			setBuyNum(){
				this.$emit('toBuyNum',this.mynum)
				this.$emit('toBuyId',this.myid)
			}
		}
	}
</script>

<style lang="scss">
.cbcontainer{
	width: 100%;
	height: 50rpx;
	display: flex;
	justify-content: space-between;
	font-size: 32rpx;
	text-align: center;
	line-height: 45rpx;
	
	.btn{
		width: 45rpx;
		height: 45rpx;
		background-color: #F8F8F8;
		color: #AFAFAF;
	}
	
	.active{
		background-color: #ECECEC;
		color: #303030;
	}
}
</style>