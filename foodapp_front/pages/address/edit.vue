<template>
  <view class="edit-address-page">
    <uni-nav-bar :title="isEdit ? '编辑地址' : '添加地址'" left-icon="back" @clickLeft="goBack" />
    
    <scroll-view class="form-container" scroll-y>
      <view class="form-item">
        <text class="label">收货人</text>
        <input 
          class="input" 
          v-model="formData.name" 
          placeholder="请输入收货人姓名"
          maxlength="10"
        />
      </view>
      
      <view class="form-item">
        <text class="label">手机号</text>
        <input 
          class="input" 
          v-model="formData.phone" 
          placeholder="请输入手机号码"
          type="number"
          maxlength="11"
        />
      </view>
      
      <view class="form-item">
        <text class="label">省份</text>
        <input 
          class="input" 
          v-model="formData.province" 
          placeholder="请输入省份"
          maxlength="20"
        />
      </view>
      
      <view class="form-item">
        <text class="label">城市</text>
        <input 
          class="input" 
          v-model="formData.city" 
          placeholder="请输入城市"
          maxlength="20"
        />
      </view>
      
      <view class="form-item">
        <text class="label">区县</text>
        <input 
          class="input" 
          v-model="formData.district" 
          placeholder="请输入区县"
          maxlength="20"
        />
      </view>
      
      <view class="form-item">
        <text class="label">详细地址</text>
        <textarea 
          class="textarea" 
          v-model="formData.detail" 
          placeholder="请输入详细地址（如街道、楼牌号等）"
          maxlength="100"
          auto-height
        />
      </view>
      
      <view class="form-item default-item">
        <text class="label">设为默认地址</text>
        <switch 
          v-model="formData.isDefault" 
          :checked="formData.isDefault" 
          color="#4a90e2"
        />
      </view>
    </scroll-view>
    
    <!-- 保存按钮 -->
    <view class="save-btn-wrapper">
      <view class="save-btn" @click="saveAddress">
        <text>{{ isEdit ? '保存修改' : '保存地址' }}</text>
      </view>
    </view>
  </view>
</template>

<script>
import { addressApi } from '@/api/address.js';

export default {
  data() {
    return {
      isEdit: false,
      addressId: null,
      userId: null,  // 从登录信息获取
      
      formData: {
        name: '',
        phone: '',
        province: '',
        city: '',
        district: '',
        detail: '',
        isDefault: false
      }
    };
  },
  
  onLoad(options) {
    console.log('编辑地址页面参数:', options);
    
    // 获取用户ID
    this.getUserInfo();
    
    // 优先使用参数中的userId
    if (options.userId) {
      this.userId = options.userId;
    }
    
    console.log('当前用户ID:', this.userId);
    
    if (!this.userId) {
      uni.showToast({
        title: '请先登录',
        icon: 'none'
      });
      
      // 返回上一页
      setTimeout(() => {
        uni.navigateBack();
      }, 1500);
      
      return;
    }
    
    if (options.id) {
      this.isEdit = true;
      this.addressId = options.id;
      this.loadAddressDetail();
    }
  },
  
  methods: {
    // 获取用户信息
    getUserInfo() {
      const app = getApp();
      
      // 先从全局获取
      this.userId = app.globalData.userId;
      
      // 如果全局没有，从本地存储获取
      if (!this.userId) {
        this.userId = uni.getStorageSync('userId');
        
        // 更新到全局
        app.globalData.userId = this.userId;
      }
    },
    
    goBack() {
      uni.navigateBack();
    },
    
    // 加载地址详情
    async loadAddressDetail() {
      try {
        const data = await addressApi.getDetail(this.addressId, this.userId);
        if (data) {
          this.formData = {
            name: data.name || '',
            phone: data.phone || '',
            province: data.province || '',
            city: data.city || '',
            district: data.district || '',
            detail: data.detail || '',
            isDefault: data.isDefault === 1
          };
        }
      } catch (error) {
        console.error('加载地址详情失败:', error);
        uni.showToast({
          title: error || '加载失败',
          icon: 'none'
        });
      }
    },
    
    // 表单验证
    validateForm() {
      if (!this.formData.name.trim()) {
        uni.showToast({ title: '请输入收货人姓名', icon: 'none' });
        return false;
      }
      
      if (!this.formData.phone.trim()) {
        uni.showToast({ title: '请输入手机号码', icon: 'none' });
        return false;
      }
      
      const phoneReg = /^1[3-9]\d{9}$/;
      if (!phoneReg.test(this.formData.phone)) {
        uni.showToast({ title: '手机号码格式不正确', icon: 'none' });
        return false;
      }
      
      if (!this.formData.province.trim()) {
        uni.showToast({ title: '请输入省份', icon: 'none' });
        return false;
      }
      
      if (!this.formData.city.trim()) {
        uni.showToast({ title: '请输入城市', icon: 'none' });
        return false;
      }
      
      if (!this.formData.district.trim()) {
        uni.showToast({ title: '请输入区县', icon: 'none' });
        return false;
      }
      
      if (!this.formData.detail.trim()) {
        uni.showToast({ title: '请输入详细地址', icon: 'none' });
        return false;
      }
      
      return true;
    },
    
    // 保存地址
    async saveAddress() {
      if (!this.validateForm()) {
        return;
      }
      
      const params = {
        userId: this.userId,
        name: this.formData.name,
        phone: this.formData.phone,
        province: this.formData.province,
        city: this.formData.city,
        district: this.formData.district,
        detail: this.formData.detail,
        isDefault: this.formData.isDefault ? 1 : 0 
      };
      
      if (this.isEdit) {
        params.id = this.addressId;
      }
      
      try {
        if (this.isEdit) {
          await addressApi.update(params);
          uni.showToast({
            title: '修改成功',
            icon: 'success'
          });
        } else {
          await addressApi.add(params);
          uni.showToast({
            title: '添加成功',
            icon: 'success'
          });
        }
        
        // 返回上一页
        setTimeout(() => {
          uni.navigateBack();
        }, 1500);
        
      } catch (error) {
        console.error('保存失败:', error);
        uni.showToast({
          title: error.toString(),
          icon: 'none',
          duration: 3000
        });
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.edit-address-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  
  .form-container {
    padding: 20rpx;
    height: calc(100vh - 200rpx);
    
    .form-item {
      background: #fff;
      margin-bottom: 20rpx;
      padding: 30rpx;
      border-radius: 12rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      
      .label {
        font-size: 32rpx;
        color: #333;
        width: 200rpx;
      }
      
      .input, .textarea {
        flex: 1;
        font-size: 32rpx;
        color: #333;
      }
      
      .input {
        text-align: right;
        height: 40rpx;
      }
      
      .textarea {
        min-height: 80rpx;
        line-height: 1.5;
        text-align: right;
      }
      
      &.default-item {
        justify-content: space-between;
      }
    }
  }
  
  .save-btn-wrapper {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 20rpx;
    background: #fff;
    border-top: 1rpx solid #f0f0f0;
    
    .save-btn {
      background: linear-gradient(135deg, #4a90e2, #5a8bff);
      color: #fff;
      height: 90rpx;
      border-radius: 45rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 32rpx;
      font-weight: 500;
    }
  }
}
</style>