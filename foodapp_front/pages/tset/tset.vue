<template>
    <view class="test-page">
        <view class="title">接口测试页面</view>
        
        <view class="input-group">
            <text>用户ID: {{ userId }}</text>
            <text>Token预览: {{ tokenPreview }}</text>
        </view>
        
        <button @click="getProfile">测试获取用户信息</button>
        <button @click="updatePhone">测试更新手机号</button>
        <button @click="unbindPhone">测试解绑手机号</button>
        
        <!-- 这里添加结果显示区域 -->
        <view class="result" v-if="testResult">
            <text class="result-title">测试结果：</text>
            <view class="result-content">{{ testResult }}</view>
        </view>
        
        <!-- 这里添加控制台查看提示 -->
        <view class="tips">
            <text class="tip-text">提示：同时请查看浏览器控制台（F12 → Console）</text>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            userId: '',
            userToken: '',
            testResult: ''  // 存储测试结果
        };
    },
    
    computed: {
        tokenPreview() {
            return this.userToken ? this.userToken.substring(0, 20) + '...' : '无';
        }
    },
    
    onLoad() {
        this.userId = uni.getStorageSync('userId');
        this.userToken = uni.getStorageSync('userToken');
        console.log('用户ID:', this.userId);
        console.log('Token:', this.tokenPreview);
    },
    
    methods: {
        async getProfile() {
            console.log('=== 开始测试获取用户信息 ===');
            this.testResult = '测试中...';
            
            try {
                const result = await uni.request({
                    url: 'http://localhost:8089/api/user/profile',
                    method: 'GET',
                    header: {
                        'Authorization': 'Bearer ' + this.userToken
                    }
                });
                
                console.log('获取用户信息响应:', result);
                this.testResult = `✅ 获取成功！
状态码: ${result.statusCode}
响应数据: ${JSON.stringify(result.data, null, 2)}`;
                
            } catch (error) {
                console.error('获取失败:', error);
                this.testResult = `❌ 获取失败: ${error.errMsg || error.message}`;
            }
        },
        
        async updatePhone() {
            console.log('=== 开始测试更新手机号 ===');
            this.testResult = '测试中...';
            
            const testPhone = '13800138000'; // 测试手机号
            
            try {
                const result = await uni.request({
                    url: 'http://localhost:8089/api/user/update',
                    method: 'POST',
                    header: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + this.userToken
                    },
                    data: {
                        userId: parseInt(this.userId),
                        phone: testPhone
                    }
                });
                
                console.log('更新手机号响应:', result);
                this.testResult = `✅ 更新请求完成！
状态码: ${result.statusCode}
测试手机号: ${testPhone}
响应数据: ${JSON.stringify(result.data, null, 2)}`;
                
            } catch (error) {
                console.error('更新失败:', error);
                this.testResult = `❌ 更新失败: ${error.errMsg || error.message}`;
            }
        },
        
        async unbindPhone() {
            console.log('=== 开始测试解绑手机号 ===');
            this.testResult = '测试中...';
            
            try {
                const result = await uni.request({
                    url: 'http://localhost:8089/api/user/update',
                    method: 'POST',
                    header: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + this.userToken
                    },
                    data: {
                        userId: parseInt(this.userId),
                        phone: null  // 解绑手机号
                    }
                });
                
                console.log('解绑手机号响应:', result);
                this.testResult = `✅ 解绑请求完成！
状态码: ${result.statusCode}
响应数据: ${JSON.stringify(result.data, null, 2)}`;
                
            } catch (error) {
                console.error('解绑失败:', error);
                this.testResult = `❌ 解绑失败: ${error.errMsg || error.message}`;
            }
        }
    }
};
</script>

<style scoped>
.test-page {
    padding: 40rpx;
    background-color: #f5f5f5;
    min-height: 100vh;
}

.title {
    font-size: 44rpx;
    font-weight: bold;
    text-align: center;
    margin-bottom: 50rpx;
    color: #333;
}

.input-group {
    background: white;
    padding: 30rpx;
    border-radius: 16rpx;
    margin-bottom: 40rpx;
    box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.05);
}

.input-group text {
    display: block;
    font-size: 32rpx;
    color: #333;
    margin-bottom: 20rpx;
    line-height: 1.5;
}

.input-group text:last-child {
    margin-bottom: 0;
}

button {
    width: 100%;
    height: 90rpx;
    background: linear-gradient(135deg, #4a90e2, #5a8bff);
    color: white;
    border-radius: 45rpx;
    margin-bottom: 30rpx;
    font-size: 34rpx;
    font-weight: bold;
    border: none;
}

button:active {
    opacity: 0.9;
}

.result {
    background: white;
    padding: 30rpx;
    border-radius: 16rpx;
    margin-top: 40rpx;
    box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.05);
}

.result-title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
    display: block;
}

.result-content {
    font-size: 28rpx;
    color: #666;
    line-height: 1.6;
    white-space: pre-wrap;
    word-break: break-all;
}

.tips {
    margin-top: 30rpx;
    padding: 20rpx;
    background: #fff8e8;
    border-radius: 8rpx;
}

.tip-text {
    font-size: 26rpx;
    color: #ff9900;
    display: block;
}
</style>