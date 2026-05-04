<template>
  <view class="shop-setting-container">
    <uni-forms ref="form" :model="shopInfo" :rules="rules">
      <!-- 页面标题 -->
      <view class="page-header">
        <view class="header-left" @click="goBack">
          <uni-icons type="back" size="28"></uni-icons>
          <text>返回</text>
        </view>
        <view class="header-title">店铺设置</view>
        <view class="header-right" @click="saveShopInfo">
          <text>保存</text>
        </view>
      </view>

      <!-- 店铺基本信息 -->
      <uni-section title="基本信息" type="line">
        <!-- 店铺ID - 只读 -->
        <uni-forms-item label="店铺ID" disabled>
          <uni-easyinput
            v-model="shopInfo.sid"
            disabled
          />
        </uni-forms-item>

        <!-- 店铺名称 - 可编辑 -->
        <uni-forms-item label="店铺名称" name="sname" required>
          <uni-easyinput
            v-model="shopInfo.sname"
            placeholder="请输入店铺名称"
          />
        </uni-forms-item>

        <!-- 店铺状态 - 可编辑 -->
        <uni-forms-item label="店铺状态" name="sstatus">
          <uni-data-select
            v-model="shopInfo.sstatus"
            :localdata="statusOptions"
          ></uni-data-select>
        </uni-forms-item>

        <!-- 信誉等级 - 只读 -->
        <uni-forms-item label="信誉等级" disabled>
          <uni-easyinput
            v-model="shopInfo.xgade"
            disabled
          />
        </uni-forms-item>
      </uni-section>

      <!-- 联系方式 -->
      <uni-section title="联系方式" type="line">
        <!-- 联系电话 - 可编辑 -->
        <uni-forms-item label="联系电话" name="phone" required>
          <uni-easyinput
            v-model="shopInfo.phone"
            placeholder="请输入联系电话"
            type="number"
          />
        </uni-forms-item>

        <!-- 所在地区 - 可编辑 -->
        <uni-forms-item label="所在地区" name="province" required>
          <uni-easyinput
            v-model="shopInfo.province"
            placeholder="请输入所在省市区（县）"
          />
        </uni-forms-item>

        <!-- 详细地址 - 可编辑 -->
        <uni-forms-item label="详细地址" name="saddress" required>
          <uni-easyinput
            v-model="shopInfo.saddress"
            placeholder="请输入详细地址"
          />
        </uni-forms-item>
      </uni-section>

      <!-- 店铺图片 -->
      <uni-section title="店铺图片" type="line">
        <!-- 店铺Logo - 可编辑 -->
        <uni-forms-item label="店铺Logo">
          <uni-file-picker
            v-model="shopInfo.logoFiles"
            mode="grid"
            :limit="1"
            title="点击选择Logo"
            auto-upload="false"
            @select="onLogoSelect"
          ></uni-file-picker>
        </uni-forms-item>
      </uni-section>

      <!-- 商家资质 -->
      <uni-section title="商家资质" type="line">
        <!-- 营业执照 - 可编辑 -->
        <uni-forms-item label="营业执照">
          <uni-file-picker
            v-model="shopInfo.businessLicenseFiles"
            mode="grid"
            :limit="1"
            title="点击上传营业执照"
            auto-upload="false"
            @select="onBusinessLicenseSelect"
          ></uni-file-picker>
        </uni-forms-item>

        <!-- 食品经营许可证 - 可编辑 -->
        <uni-forms-item label="食品经营许可证">
          <uni-file-picker
            v-model="shopInfo.foodLicenseFiles"
            mode="grid"
            :limit="1"
            title="点击上传食品经营许可证"
            auto-upload="false"
            @select="onFoodLicenseSelect"
          ></uni-file-picker>
        </uni-forms-item>

        <!-- 其他资质 - 可编辑 -->
        <uni-forms-item label="其他资质">
          <uni-file-picker
            v-model="shopInfo.otherLicenseFiles"
            mode="grid"
            :limit="3"
            title="点击上传其他资质图片"
            auto-upload="false"
            @select="onOtherLicenseSelect"
          ></uni-file-picker>
        </uni-forms-item>
      </uni-section>

      <!-- 店铺评分 - 只读 -->
      <uni-section title="店铺评分" type="line">
        <uni-forms-item label="综合体验" disabled>
          <uni-easyinput
            v-model="shopInfo.overallExperience"
            disabled
          />
        </uni-forms-item>

        <uni-forms-item label="商品体验" disabled>
          <uni-easyinput
            v-model="shopInfo.goodsExperience"
            disabled
          />
        </uni-forms-item>

        <uni-forms-item label="服务体验" disabled>
          <uni-easyinput
            v-model="shopInfo.serviceExperience"
            disabled
          />
        </uni-forms-item>

        <uni-forms-item label="物流体验" disabled>
          <uni-easyinput
            v-model="shopInfo.logisticsExperience"
            disabled
          />
        </uni-forms-item>

        <uni-forms-item label="上线时间" disabled>
          <uni-easyinput
            v-model="shopInfo.ontime"
            disabled
          />
        </uni-forms-item>
      </uni-section>
    </uni-forms>
  </view>
</template>

<script>
import request from '../../utils/request.js'

export default {
  data() {
    return {
      sellerInfo: null,
      // 店铺信息
      shopInfo: {
        sid: '',
        sname: '',
        slogo: '',
        province: '',
        saddress: '',
        phone: '',
        xgade: '',
        businesslicense: '',
        foodBusinessLicense: '',
        otherBusinessLicense: '',
        overallExperience: 0,
        goodsExperience: 0,
        serviceExperience: 0,
        logisticsExperience: 0,
        ontime: '',
        sstatus: 1,
        // 文件上传相关
        logoFiles: [],
        businessLicenseFiles: [],
        foodLicenseFiles: [],
        otherLicenseFiles: []
      },
      // 店铺状态选项
      statusOptions: [
        { text: '正常营业', value: 1 },
        { text: '休息中', value: 2 },
        { text: '已禁用', value: 0 }
      ],
      // 表单验证规则
      rules: {
        sname: {
          rules: [
            { required: true, errorMessage: '请输入店铺名称' },
            { minLength: 2, maxLength: 50, errorMessage: '店铺名称长度在2-50个字符之间' }
          ]
        },
        phone: {
          rules: [
            { required: true, errorMessage: '请输入联系电话' },
            { pattern: /^1[3-9]\d{9}$/, errorMessage: '请输入正确的手机号码' }
          ]
        },
        province: {
          rules: [
            { required: true, errorMessage: '请输入所在地区' }
          ]
        },
        saddress: {
          rules: [
            { required: true, errorMessage: '请输入详细地址' }
          ]
        },
        sstatus: {
          rules: [
            { required: true, errorMessage: '请选择店铺状态' }
          ]
        }
      }
    }
  },
  onShow() {
    // 检查登录状态
    this.checkLoginStatus()
    // 加载店铺信息
    this.loadShopInfo()
  },
  methods: {
    // 检查登录状态
    checkLoginStatus() {
      const sellerInfo = uni.getStorageSync('sellerInfo')
      const token = uni.getStorageSync('token')
      // 同时检查sellerInfo和token
      if (sellerInfo && token) {
        this.sellerInfo = sellerInfo
        return true
      } else {
        // 清除可能的残留数据
        uni.removeStorageSync('sellerInfo')
        uni.removeStorageSync('token')
        // 检查是否已经在登录页面
        const pages = getCurrentPages()
        const currentPage = pages[pages.length - 1]
        if (currentPage.route !== 'pages/login/login') {
          // 未登录且不在登录页面，跳转到登录页面
          uni.redirectTo({
            url: '/pages/login/login'
          })
        }
        return false
      }
    },

    // 加载店铺信息
    async loadShopInfo() {
      uni.showLoading({
        title: '加载中...'
      })

      try {
        // 获取店铺ID
        const sid = this.sellerInfo.shopInfo?.sid
        if (!sid) {
          uni.hideLoading()
          uni.showToast({
            title: '店铺信息错误',
            icon: 'none'
          })
          return
        }

        // 调用接口获取店铺信息
        const res = await request.get(`/seller/shop/${sid}`)
        const shopData = res.data

        // 格式化时间，确保日期显示正常
        if (shopData.ontime) {
          // 检查是否是字符串，如果是，直接使用
          if (typeof shopData.ontime === 'string') {
            // 保留原始字符串，避免日期解析错误
          } else {
            // 如果是Date对象，格式化显示
            shopData.ontime = this.formatTime(shopData.ontime)
          }
        }

        // 初始化文件列表
        if (shopData.slogo) {
          shopData.logoFiles = [{ url: shopData.slogo }]
        }
        if (shopData.businesslicense) {
          shopData.businessLicenseFiles = [{ url: shopData.businesslicense }]
        }
        if (shopData.foodBusinessLicense) {
          shopData.foodLicenseFiles = [{ url: shopData.foodBusinessLicense }]
        }
        if (shopData.otherBusinessLicense) {
          shopData.otherLicenseFiles = shopData.otherBusinessLicense.split(';').map(url => ({ url: url.trim() }))
        }

        this.shopInfo = shopData
      } catch (error) {
        uni.hideLoading()
        uni.showToast({
          title: '加载店铺信息失败',
          icon: 'none'
        })
      } finally {
        uni.hideLoading()
      }
    },

    // 保存店铺信息
    async saveShopInfo() {
      this.$refs.form.validate().then(async () => {
        // 表单验证通过
        uni.showLoading({
          title: '保存中...'
        })

        try {
          // 构建表单数据，只包含允许修改的字段
          const formData = {
            sid: this.shopInfo.sid,
            sname: this.shopInfo.sname,
            slogo: this.shopInfo.slogo,
            province: this.shopInfo.province,
            saddress: this.shopInfo.saddress,
            phone: this.shopInfo.phone,
            businesslicense: this.shopInfo.businesslicense,
            foodBusinessLicense: this.shopInfo.foodBusinessLicense,
            otherBusinessLicense: this.shopInfo.otherBusinessLicense,
            sstatus: this.shopInfo.sstatus
          }

          // 上传图片（这里需要根据实际情况实现图片上传逻辑）
          // 暂时直接使用现有图片URL

          // 调用接口保存店铺信息
          await request.put('/seller/shop/update', formData)

          uni.hideLoading()
          uni.showToast({
            title: '保存成功',
            icon: 'success'
          })
          
          // 延迟1秒后返回主页
          setTimeout(() => {
            // 使用redirectTo跳转到主页，确保总是返回主页
            uni.redirectTo({
              url: '/pages/index/index'
            })
          }, 1000)
        } catch (error) {
          uni.hideLoading()
          uni.showToast({
            title: '保存失败',
            icon: 'none'
          })
        }
      }).catch((errors) => {
        console.log('表单验证失败：', errors)
        // 表单验证失败时也需要调用hideLoading
        uni.hideLoading()
        uni.showToast({
          title: '表单验证失败',
          icon: 'none'
        })
      })
    },

    // 通用图片上传方法
    async uploadImage(file) {
      return new Promise((resolve, reject) => {
        uni.showLoading({
          title: '上传中...'
        })

        // 获取token
        const token = uni.getStorageSync('token')

        uni.uploadFile({
          url: 'http://localhost:8089/api/file/upload',
          filePath: file.path,
          name: 'file',
          header: {
            'Authorization': token ? `Bearer ${token}` : ''
          },
          success: (res) => {
            uni.hideLoading()
            try {
              const data = JSON.parse(res.data)
              if (data.code === 200) {
                resolve(data.data)
              } else {
                uni.showToast({
                  title: data.msg || '上传失败',
                  icon: 'none'
                })
                reject(data)
              }
            } catch (parseError) {
              uni.showToast({
                title: '上传失败，服务器返回格式错误',
                icon: 'none'
              })
              reject(parseError)
            }
          },
          fail: (error) => {
            uni.hideLoading()
            uni.showToast({
              title: '上传失败，请稍后重试',
              icon: 'none'
            })
            console.error('图片上传失败:', error)
            reject(error)
          }
        })
      })
    },

    // Logo文件选择
    async onLogoSelect(e) {
      if (e.tempFiles && e.tempFiles.length > 0) {
        try {
          const file = e.tempFiles[0]
          const imgUrl = await this.uploadImage(file)
          this.shopInfo.slogo = imgUrl
          this.shopInfo.logoFiles = [{ url: imgUrl }]
        } catch (error) {
          console.error('Logo上传失败:', error)
        }
      }
    },

    // 营业执照选择
    async onBusinessLicenseSelect(e) {
      if (e.tempFiles && e.tempFiles.length > 0) {
        try {
          const file = e.tempFiles[0]
          const imgUrl = await this.uploadImage(file)
          this.shopInfo.businesslicense = imgUrl
          this.shopInfo.businessLicenseFiles = [{ url: imgUrl }]
        } catch (error) {
          console.error('营业执照上传失败:', error)
        }
      }
    },

    // 食品经营许可证选择
    async onFoodLicenseSelect(e) {
      if (e.tempFiles && e.tempFiles.length > 0) {
        try {
          const file = e.tempFiles[0]
          const imgUrl = await this.uploadImage(file)
          this.shopInfo.foodBusinessLicense = imgUrl
          this.shopInfo.foodLicenseFiles = [{ url: imgUrl }]
        } catch (error) {
          console.error('食品经营许可证上传失败:', error)
        }
      }
    },

    // 其他资质选择
    async onOtherLicenseSelect(e) {
      if (e.tempFiles && e.tempFiles.length > 0) {
        try {
          // 上传多张图片
          const uploadPromises = e.tempFiles.map(file => this.uploadImage(file))
          const imgUrls = await Promise.all(uploadPromises)
          this.shopInfo.otherBusinessLicense = imgUrls.join(';')
          this.shopInfo.otherLicenseFiles = imgUrls.map(url => ({ url }))
        } catch (error) {
          console.error('其他资质上传失败:', error)
        }
      }
    },

    // 格式化时间
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      return date.toLocaleString()
    },

    // 返回上一页
    goBack() {
      uni.navigateBack()
    }
  }
}
</script>

<style lang="scss">
.shop-setting-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 0;
  margin-bottom: 20rpx;
  background-color: #fff;
  padding: 20rpx;
  border-radius: 8rpx;
}

.header-left,
.header-right {
  display: flex;
  align-items: center;
  font-size: 28rpx;
  color: #007aff;
}

.header-left uni-icons {
  margin-right: 10rpx;
}

.header-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.header-right {
  font-weight: bold;
}

/* 表单样式 */
uni-section {
  margin-bottom: 20rpx;
  background-color: #fff;
  border-radius: 8rpx;
  padding: 0 20rpx;
}

uni-forms-item {
  margin-bottom: 20rpx;
}

/* 文件上传样式 */
uni-file-picker {
  margin-top: 10rpx;
}
</style>