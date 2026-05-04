<template>
  <view class="goods-edit-container">
    <uni-forms ref="form" :model="form" :rules="rules">
      <!-- 商品基本信息 -->
      <uni-section title="商品基本信息" type="line">
        <uni-forms-item label="商品名称" name="gname" required>
          <uni-easyinput
            v-model="form.gname"
            placeholder="请输入商品名称"
          />
        </uni-forms-item>

        <uni-forms-item label="商品价格" name="price" required>
          <uni-easyinput
            v-model.number="form.price"
            placeholder="请输入商品价格"
            type="number"
          />
        </uni-forms-item>

        <uni-forms-item label="商品主图">
          <uni-file-picker
            v-model="form.images"
            mode="grid"
            :limit="5"
            title="点击选择图片"
            auto-upload="false"
            @select="onFileSelect"
          ></uni-file-picker>
        </uni-forms-item>

        <uni-forms-item label="商品状态" name="gstatus">
          <uni-data-select
            v-model="form.gstatus"
            :localdata="statusOptions"
          ></uni-data-select>
        </uni-forms-item>

        <uni-forms-item label="邮费">
          <uni-easyinput
            v-model.number="form.postprice"
            placeholder="请输入邮费"
            type="number"
          />
        </uni-forms-item>

        <uni-forms-item label="每人限购数量">
          <uni-easyinput
            v-model.number="form.restBuyNum"
            placeholder="请输入每人限购数量"
            type="number"
          />
        </uni-forms-item>

        <uni-forms-item label="选择类型">
          <uni-data-select
            v-model="form.selectType"
            :localdata="selectTypeOptions"
          ></uni-data-select>
        </uni-forms-item>

        <uni-forms-item label="选择类型文本">
          <uni-easyinput
            v-model="form.selectTypeText"
            placeholder="例如：共3种规格可选"
          />
        </uni-forms-item>

        <uni-forms-item label="商品分类ID">
          <uni-easyinput
            v-model.number="form.cgid"
            placeholder="请输入商品分类ID"
            type="number"
          />
        </uni-forms-item>

        <uni-forms-item label="店铺ID" disabled>
          <uni-easyinput
            v-model.number="form.sid"
            placeholder="店铺ID"
            type="number"
            :disabled="true"
          />
        </uni-forms-item>

        <uni-forms-item label="总库存" disabled>
          <uni-easyinput
            v-model.number="form.stockTotalNum"
            placeholder="总库存"
            type="number"
            :disabled="true"
          />
        </uni-forms-item>

        <uni-forms-item label="总销量" disabled>
          <uni-easyinput
            v-model.number="form.sellTotalNum"
            placeholder="总销量"
            type="number"
            :disabled="true"
          />
        </uni-forms-item>
      </uni-section>

      <!-- 商品详情 -->
      <uni-section title="商品详情" type="line">
        <uni-forms-item label="商品描述">
          <textarea
            v-model="form.content"
            placeholder="请输入商品描述"
            style="width: 100%; height: 200rpx; border: 1rpx solid #eee; border-radius: 8rpx; padding: 20rpx;"
          ></textarea>
        </uni-forms-item>
      </uni-section>

      <!-- 商品详情管理 -->
      <uni-section title="商品详情管理" type="line">
        <!-- 添加商品详情按钮 -->
        <button class="add-detail-btn" @click="showDetailForm">
          <uni-icons type="plus" size="20" color="#fff"></uni-icons>
          添加商品详情
        </button>

        <!-- 商品详情列表 -->
        <view class="detail-list" v-if="goodsDetailList.length > 0">
          <uni-card
            v-for="(detail, index) in goodsDetailList"
            :key="detail.gdid"
            title="商品详情"
          >
            <view class="detail-info">
              <view class="detail-attribute">
                <text class="attribute-label">{{ detail.attributeName1 }}:</text>
                <text class="attribute-value">{{ detail.attributeValue1 }}</text>
              </view>
              <view class="detail-attribute" v-if="detail.attributeName2">
                <text class="attribute-label">{{ detail.attributeName2 }}:</text>
                <text class="attribute-value">{{ detail.attributeValue2 }}</text>
              </view>
              <view class="detail-attribute" v-if="detail.attributeName3">
                <text class="attribute-label">{{ detail.attributeName3 }}:</text>
                <text class="attribute-value">{{ detail.attributeValue3 }}</text>
              </view>
              <view class="detail-attribute" v-if="detail.attributeName4">
                <text class="attribute-label">{{ detail.attributeName4 }}:</text>
                <text class="attribute-value">{{ detail.attributeValue4 }}</text>
              </view>
              <view class="detail-attribute" v-if="detail.attributeName5">
                <text class="attribute-label">{{ detail.attributeName5 }}:</text>
                <text class="attribute-value">{{ detail.attributeValue5 }}</text>
              </view>
              <view class="detail-stock">
                <text class="stock-label">库存:</text>
                <text class="stock-value">{{ detail.stockNum }}</text>
              </view>
              <view class="detail-price">
                <text class="price-label">售价:</text>
                <text class="price-value">¥{{ detail.specialPrice || detail.originalPrice }}</text>
              </view>
            </view>
            <view class="detail-actions">
              <button class="detail-action-btn edit-btn" @click="editDetail(detail)">
                <uni-icons type="compose" size="18" color="#fff"></uni-icons>
                编辑
              </button>
              <button class="detail-action-btn delete-btn" @click="deleteDetail(detail.gdid)">
                <uni-icons type="trash" size="18" color="#fff"></uni-icons>
                删除
              </button>
            </view>
          </uni-card>
        </view>

        <!-- 空状态 -->
        <view class="empty-detail" v-else>
          <uni-icons type="info" size="60" color="#ccc"></uni-icons>
          <text class="empty-text">暂无商品详情，请添加</text>
        </view>
      </uni-section>

      <!-- 商品详情表单弹窗 -->
      <uni-popup ref="detailPopup" type="bottom">
        <view class="popup-content">
          <view class="popup-header">
            <text class="popup-title">{{ isEditDetail ? '编辑商品详情' : '添加商品详情' }}</text>
            <button class="close-btn" @click="closeDetailForm">
              <uni-icons type="close" size="24" color="#333"></uni-icons>
            </button>
          </view>
          <view class="popup-body">
            <view class="input-item">
              <text class="input-label">属性名1</text>
              <text class="input-desc">用于区分商品的主要属性，如：尺码、颜色等</text>
              <uni-easyinput
                v-model="detailForm.attributeName1"
                placeholder="例如：尺码"
                style="margin-bottom: 20rpx;"
              ></uni-easyinput>
            </view>
            <view class="input-item">
              <text class="input-label">属性值1</text>
              <text class="input-desc">对应属性名1的值，如：S、M、L等</text>
              <uni-easyinput
                v-model="detailForm.attributeValue1"
                placeholder="例如：S"
                style="margin-bottom: 20rpx;"
              ></uni-easyinput>
            </view>
            <view class="input-item">
              <text class="input-label">属性名2</text>
              <text class="input-desc">（可选）用于区分商品的次要属性，如：颜色、材质等</text>
              <uni-easyinput
                v-model="detailForm.attributeName2"
                placeholder="例如：颜色"
                style="margin-bottom: 20rpx;"
              ></uni-easyinput>
            </view>
            <view class="input-item">
              <text class="input-label">属性值2</text>
              <text class="input-desc">对应属性名2的值，如：红色、蓝色等</text>
              <uni-easyinput
                v-model="detailForm.attributeValue2"
                placeholder="例如：红色"
                style="margin-bottom: 20rpx;"
              ></uni-easyinput>
            </view>
            <view class="input-item">
              <text class="input-label">属性名3</text>
              <text class="input-desc">（可选）用于区分商品的第三属性，如：材质、款式等</text>
              <uni-easyinput
                v-model="detailForm.attributeName3"
                placeholder="例如：材质"
                style="margin-bottom: 20rpx;"
              ></uni-easyinput>
            </view>
            <view class="input-item">
              <text class="input-label">属性值3</text>
              <text class="input-desc">对应属性名3的值，如：棉、涤纶等</text>
              <uni-easyinput
                v-model="detailForm.attributeValue3"
                placeholder="例如：棉"
                style="margin-bottom: 20rpx;"
              ></uni-easyinput>
            </view>
            <view class="input-item">
              <text class="input-label">库存数量</text>
              <text class="input-desc">该商品详情的当前库存数量，必须大于等于0</text>
              <uni-easyinput
                v-model.number="detailForm.stockNum"
                placeholder="例如：100"
                type="number"
                style="margin-bottom: 20rpx;"
              ></uni-easyinput>
            </view>
            <view class="input-item">
              <text class="input-label">原价</text>
              <text class="input-desc">商品的原始价格，用于显示折扣信息</text>
              <uni-easyinput
                v-model.number="detailForm.originalPrice"
                placeholder="例如：99.99"
                type="number"
                style="margin-bottom: 20rpx;"
              ></uni-easyinput>
            </view>
            <view class="input-item">
              <text class="input-label">售价</text>
              <text class="input-desc">商品的实际销售价格，用户将以该价格购买</text>
              <uni-easyinput
                v-model.number="detailForm.specialPrice"
                placeholder="例如：89.99"
                type="number"
                style="margin-bottom: 20rpx;"
              ></uni-easyinput>
            </view>
            
            <view class="input-item">
              <text class="input-label">商品详情图片</text>
              <text class="input-desc">上传该规格商品的详情图片</text>
              <uni-file-picker
                v-model="detailForm.images"
                mode="grid"
                :limit="1"
                title="点击选择图片"
                auto-upload="false"
                @select="onDetailFileSelect"
              ></uni-file-picker>
            </view>
          </view>
          <view class="popup-footer">
            <button class="cancel-btn" @click="closeDetailForm">取消</button>
            <button class="submit-btn" @click="submitDetailForm">{{ isEditDetail ? '保存修改' : '添加' }}</button>
          </view>
        </view>
      </uni-popup>

      <!-- 提交按钮 -->
      <view class="form-actions">
        <button class="cancel-btn" @click="cancel">取消</button>
        <button class="submit-btn" @click="submit">保存修改</button>
      </view>
    </uni-forms>
  </view>
</template>

<script>
import request from '../../utils/request.js'

export default {
  data() {
      return {
        gid: null,
        sellerInfo: null,
        // 表单数据
        form: {
          gname: '',
          price: 0,
          stockTotalNum: 0,
          sellTotalNum: 0,
          icon: '',
          content: '',
          gstatus: 1,
          postprice: 0,
          restBuyNum: 0,
          selectType: 1,
          selectTypeText: '',
          cgid: 0,
          sid: 0,
          images: []
        },
        // 状态选项
        statusOptions: [
          { text: '已上架', value: 1 },
          { text: '已下架', value: 0 }
        ],
        // 选择类型选项
        selectTypeOptions: [
          { text: '有多种规格可选', value: 1 },
          { text: '有多种分类可选', value: 2 }
        ],
        // 表单验证规则
        rules: {
          gname: {
            rules: [
              { required: true, errorMessage: '请输入商品名称' }
            ]
          },
          price: {
            rules: [
              { required: true, errorMessage: '请输入商品价格' },
              { type: 'number', min: 0.01, errorMessage: '价格必须大于0' }
            ]
          },
          stockTotalNum: {
            rules: [
              { required: true, errorMessage: '请输入库存数量' },
              { type: 'number', min: 0, errorMessage: '库存数量不能为负数' }
            ]
          }
        },
        // 商品详情相关数据
        goodsDetailList: [], // 商品详情列表
        detailForm: {
          gdid: null,
          gid: null,
          stockNum: 0,
          sellNum: 0,
          originalPrice: 0,
          specialPrice: 0,
          gdpic: '',
          images: [],
          attributeName1: '',
          attributeValue1: '',
          attributeName2: '',
          attributeValue2: '',
          attributeName3: '',
          attributeValue3: '',
          attributeName4: '',
          attributeValue4: '',
          attributeName5: '',
          attributeValue5: ''
        },
        isEditDetail: false // 是否处于编辑状态
      }
    },
  onLoad(options) {
        // 获取商品ID
        this.gid = options.gid
        // 检查登录状态
        this.checkLoginStatus()
        // 加载商品信息
        this.loadGoodsInfo()
        // 加载商品详情列表
        this.loadGoodsDetailList()
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
    
    // 加载商品信息
    async loadGoodsInfo() {
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
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
          return
        }
        
        // 调用接口获取商品信息
        const res = await request.get(`/seller/goods/${this.gid}`, { sid })
        const goods = res.data
        
        uni.hideLoading()
        
        // 填充表单数据
        this.form = {
          ...goods,
          images: goods.icon ? goods.icon.split(';').map(url => ({ url: url.trim() })) : []
        }
      } catch (error) {
        uni.hideLoading()
        uni.showToast({
          title: '加载商品信息失败',
          icon: 'none'
        })
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      }
    },

    // 文件选择成功处理
    async onFileSelect(e) {
      uni.showLoading({
        title: '上传中...'
      })
      
      try {
        // 获取选择的文件列表
        const fileList = e.tempFilePaths
        const fileUrls = []
        
        // 上传每张图片
        for (const filePath of fileList) {
          const uploadResult = await this.uploadImage(filePath)
          fileUrls.push(uploadResult)
        }
        
        // 将上传后的URL保存到表单中
        this.form.icon = fileUrls.join(';')
        this.form.images = fileUrls.map(url => ({ url: url.trim() }))
        
        uni.hideLoading()
      } catch (error) {
        uni.hideLoading()
        uni.showToast({
          title: '上传失败',
          icon: 'none'
        })
      }
    },
    
    // 上传单张图片
    uploadImage(filePath) {
      return new Promise((resolve, reject) => {
        uni.uploadFile({
          url: 'http://localhost:8089/api/file/upload',
          filePath: filePath,
          name: 'file',
          success: (res) => {
            const data = JSON.parse(res.data)
            if (data.code === 200) {
              resolve(data.data)
            } else {
              reject(new Error(data.msg || '上传失败'))
            }
          },
          fail: (err) => {
            reject(err)
          }
        })
      })
    },
    
    // 商品详情文件选择成功处理
    async onDetailFileSelect(e) {
      uni.showLoading({
        title: '上传中...'
      })
      
      try {
        // 获取选择的文件列表
        const fileList = e.tempFilePaths
        const fileUrls = []
        
        // 上传每张图片
        for (const filePath of fileList) {
          const uploadResult = await this.uploadImage(filePath)
          fileUrls.push(uploadResult)
        }
        
        // 将上传后的URL保存到表单中
        this.detailForm.gdpic = fileUrls.join(';')
        this.detailForm.images = fileUrls.map(url => ({ url: url.trim() }))
        
        uni.hideLoading()
      } catch (error) {
        uni.hideLoading()
        uni.showToast({
          title: '上传失败',
          icon: 'none'
        })
      }
    },

    // 提交表单
    async submit() {
      this.$refs.form.validate().then(async () => {
        // 表单验证通过
        uni.showLoading({
          title: '提交中...'
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
          
          // 调用接口提交数据，添加sid到表单数据
          const formData = {
            ...this.form,
            sid: sid
          }
          await request.put(`/seller/goods/${this.gid}`, formData)
          
          uni.hideLoading()
          uni.showToast({
            title: '商品修改成功',
            icon: 'success'
          })
          
          // 返回商品列表
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        } catch (error) {
          uni.hideLoading()
          // 错误已在request工具中处理
        }
      }).catch((errors) => {
        console.log('表单验证失败：', errors)
      })
    },

    // 取消提交
    cancel() {
      uni.navigateBack()
    },

    // 加载商品详情列表
    async loadGoodsDetailList() {
      try {
        const res = await request.get(`/seller/goods/detail/list`, { gid: this.gid })
        this.goodsDetailList = res.data
      } catch (error) {
        console.error('加载商品详情列表失败:', error)
      }
    },

    // 显示商品详情表单
    showDetailForm() {
      // 重置表单
      this.detailForm = {
        gdid: null,
        gid: this.gid,
        stockNum: 0,
        sellNum: 0,
        originalPrice: 0,
        specialPrice: 0,
        gdpic: '',
        images: [],
        attributeName1: '',
        attributeValue1: '',
        attributeName2: '',
        attributeValue2: '',
        attributeName3: '',
        attributeValue3: '',
        attributeName4: '',
        attributeValue4: '',
        attributeName5: '',
        attributeValue5: ''
      }
      this.isEditDetail = false
      this.$refs.detailPopup.open()
    },

    // 关闭商品详情表单
    closeDetailForm() {
      this.$refs.detailPopup.close()
    },

    // 编辑商品详情
    editDetail(detail) {
      // 处理图片，将gdpic转换为images数组
      const images = detail.gdpic ? detail.gdpic.split(';').map(url => ({ url: url.trim() })) : []
      this.detailForm = {
        ...detail,
        images: images
      }
      this.isEditDetail = true
      this.$refs.detailPopup.open()
    },

    // 提交商品详情表单
    async submitDetailForm() {
      uni.showLoading({
        title: '提交中...'
      })

      try {
        if (this.isEditDetail) {
          // 更新商品详情
          await request.put('/seller/goods/detail/update', this.detailForm)
          uni.showToast({
            title: '商品详情更新成功',
            icon: 'success'
          })
        } else {
          // 添加商品详情
          await request.post('/seller/goods/detail/add', this.detailForm)
          uni.showToast({
            title: '商品详情添加成功',
            icon: 'success'
          })
        }

        // 关闭表单
        this.closeDetailForm()
        // 重新加载商品详情列表
        this.loadGoodsDetailList()
      } catch (error) {
        uni.showToast({
          title: '操作失败',
          icon: 'none'
        })
      } finally {
        uni.hideLoading()
      }
    },

    // 删除商品详情
    async deleteDetail(gdid) {
      uni.showModal({
        title: '提示',
        content: '确定要删除该商品详情吗？',
        success: async (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '删除中...'
            })

            try {
              await request.delete(`/seller/goods/detail/${gdid}`)
              uni.showToast({
                title: '商品详情删除成功',
                icon: 'success'
              })
              // 重新加载商品详情列表
              this.loadGoodsDetailList()
            } catch (error) {
              uni.showToast({
                title: '删除失败',
                icon: 'none'
              })
            } finally {
              uni.hideLoading()
            }
          }
        }
      })
    }
  }
}
</script>

<style lang="scss">
.goods-edit-container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.form-actions {
  display: flex;
  gap: 20rpx;
  margin-top: 40rpx;
  padding: 20rpx 0;
}

.cancel-btn {
  flex: 1;
  background-color: #fff;
  color: #333;
  border: 1rpx solid #ddd;
  border-radius: 8rpx;
  height: 80rpx;
  line-height: 80rpx;
  font-size: 32rpx;
}

.submit-btn {
  flex: 1;
  background-color: #007aff;
  color: #fff;
  border-radius: 8rpx;
  height: 80rpx;
  line-height: 80rpx;
  font-size: 32rpx;
}

/* 商品详情管理样式 */
.add-detail-btn {
  background-color: #007aff;
  color: #fff;
  border-radius: 8rpx;
  height: 60rpx;
  line-height: 60rpx;
  font-size: 28rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20rpx;
  border: none;
}

.detail-list {
  margin-top: 20rpx;
}

.detail-info {
  margin-bottom: 20rpx;
}

.detail-attribute {
  display: flex;
  margin-bottom: 10rpx;
}

.attribute-label {
  font-weight: bold;
  margin-right: 10rpx;
  color: #333;
}

.attribute-value {
  color: #666;
}

.detail-stock {
  display: flex;
  margin-bottom: 10rpx;
  color: #333;
}

.stock-label {
  font-weight: bold;
  margin-right: 10rpx;
}

.stock-value {
  color: #007aff;
  font-weight: bold;
}

.detail-price {
  display: flex;
  margin-bottom: 10rpx;
  color: #333;
}

.price-label {
  font-weight: bold;
  margin-right: 10rpx;
}

.price-value {
  color: #ff3b30;
  font-weight: bold;
}

.detail-actions {
  display: flex;
  gap: 10rpx;
  margin-top: 20rpx;
}

.detail-action-btn {
  flex: 1;
  height: 50rpx;
  line-height: 50rpx;
  font-size: 24rpx;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  border: none;
}

.edit-btn {
  background-color: #007aff;
}

.delete-btn {
  background-color: #ff3b30;
}

.empty-detail {
  text-align: center;
  padding: 50rpx 0;
  color: #999;
}

.empty-text {
  display: block;
  margin-top: 20rpx;
  font-size: 28rpx;
}

/* 弹窗样式 */
.popup-content {
  background-color: #fff;
  border-radius: 16rpx 16rpx 0 0;
  padding: 20rpx;
  max-height: 80vh;
  overflow-y: auto;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.popup-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.close-btn {
  background-color: transparent;
  border: none;
  padding: 0;
  margin: 0;
  font-size: 28rpx;
  color: #666;
}

.popup-body {
  margin-bottom: 30rpx;
}

/* 输入项样式 */
.input-item {
  margin-bottom: 30rpx;
}

.input-label {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.input-desc {
  display: block;
  font-size: 22rpx;
  color: #999;
  margin-bottom: 12rpx;
  line-height: 1.4;
}

.popup-footer {
  display: flex;
  gap: 20rpx;
  margin-top: 20rpx;
}

.popup-footer .cancel-btn,
.popup-footer .submit-btn {
  flex: 1;
  height: 70rpx;
  line-height: 70rpx;
  font-size: 32rpx;
  border-radius: 8rpx;
  border: none;
  color: #fff;
}

.popup-footer .cancel-btn {
  background-color: #f5f5f5;
  color: #333;
}

.popup-footer .submit-btn {
  background-color: #007aff;
}
</style>