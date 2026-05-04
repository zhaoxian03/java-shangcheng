// api/collect.js
// 收藏相关API接口

// 配置基础URL - 请根据您的实际情况修改
const BASE_URL = 'http://localhost:8089/api/collect/'; // 开发环境
// const BASE_URL = 'https://yourdomain.com/api/collect/'; // 生产环境

// 获取应用实例
const app = getApp();

/**
 * 收藏API服务
 */
export const collectApi = {
  
  /**
   * 添加商品收藏
   * @param {Object} data 收藏数据
   * @returns {Promise}
   */
  addGoods(data) {
    return new Promise((resolve, reject) => {
      uni.request({
        url: BASE_URL + 'addGoods',
        method: 'POST',
        data: data,
        header: {
          'content-type': 'application/json',
          'Authorization': uni.getStorageSync('userToken') || '' // 添加认证token
        },
        success: (res) => {
          console.log('添加收藏成功:', res.data);
          resolve(res.data);
        },
        fail: (err) => {
          console.error('添加收藏失败:', err);
          reject(err);
        }
      });
    });
  },
  
  /**
   * 取消商品收藏
   * @param {string} userId 用户ID
   * @param {number|string} goodsId 商品ID
   * @returns {Promise}
   */
  cancelGoods(userId, goodsId) {
    return new Promise((resolve, reject) => {
      uni.request({
        url: BASE_URL + 'cancelGoods',
        method: 'POST',
        data: { userId, goodsId },
        header: {
          'content-type': 'application/json',
          'Authorization': uni.getStorageSync('userToken') || ''
        },
        success: (res) => {
          console.log('取消收藏成功:', res.data);
          resolve(res.data);
        },
        fail: (err) => {
          console.error('取消收藏失败:', err);
          reject(err);
        }
      });
    });
  },
  
  /**
   * 批量删除收藏
   * @param {string} userId 用户ID
   * @param {Array} goodsIds 商品ID数组
   * @returns {Promise}
   */
  batchDeleteGoods(userId, goodsIds) {
    return new Promise((resolve, reject) => {
      uni.request({
        url: BASE_URL + 'batchDeleteGoods',
        method: 'POST',
        data: { userId, goodsIds },
        header: {
          'content-type': 'application/json',
          'Authorization': uni.getStorageSync('userToken') || ''
        },
        success: (res) => {
          console.log('批量删除成功:', res.data);
          resolve(res.data);
        },
        fail: (err) => {
          console.error('批量删除失败:', err);
          reject(err);
        }
      });
    });
  },
  
  /**
   * 获取商品收藏列表
   * @param {string} userId 用户ID
   * @param {number} page 页码
   * @param {number} pageSize 每页数量
   * @returns {Promise}
   */
  getGoodsList(userId, page = 1, pageSize = 20) {
    return new Promise((resolve, reject) => {
      uni.request({
        url: BASE_URL + 'getGoodsList',
        method: 'GET',
        data: { 
          userId, 
          page, 
          pageSize 
        },
        header: {
          'Authorization': uni.getStorageSync('userToken') || ''
        },
        success: (res) => {
          console.log('获取收藏列表成功:', res.data);
          resolve(res.data);
        },
        fail: (err) => {
          console.error('获取收藏列表失败:', err);
          reject(err);
        }
      });
    });
  },
  
  /**
   * 检查商品是否已收藏
   * @param {string} userId 用户ID
   * @param {number|string} goodsId 商品ID
   * @returns {Promise}
   */
  checkGoods(userId, goodsId) {
    return new Promise((resolve, reject) => {
      uni.request({
        url: BASE_URL + 'checkGoods',
        method: 'GET',
        data: { userId, goodsId },
        header: {
          'Authorization': uni.getStorageSync('userToken') || ''
        },
        success: (res) => {
          console.log('检查收藏状态成功:', res.data);
          resolve(res.data);
        },
        fail: (err) => {
          console.error('检查收藏状态失败:', err);
          reject(err);
        }
      });
    });
  },
  
  /**
   * 添加店铺收藏
   * @param {Object} data 店铺收藏数据
   * @returns {Promise}
   */
  addShop(data) {
    return new Promise((resolve, reject) => {
      uni.request({
        url: BASE_URL + 'addShop',
        method: 'POST',
        data: data,
        header: {
          'content-type': 'application/json',
          'Authorization': uni.getStorageSync('userToken') || ''
        },
        success: (res) => {
          console.log('添加店铺收藏成功:', res.data);
          resolve(res.data);
        },
        fail: (err) => {
          console.error('添加店铺收藏失败:', err);
          reject(err);
        }
      });
    });
  },
  
  /**
   * 取消店铺收藏
   * @param {string} userId 用户ID
   * @param {number|string} shopId 店铺ID
   * @returns {Promise}
   */
  cancelShop(userId, shopId) {
    return new Promise((resolve, reject) => {
      uni.request({
        url: BASE_URL + 'cancelShop',
        method: 'POST',
        data: { userId, shopId },
        header: {
          'content-type': 'application/json',
          'Authorization': uni.getStorageSync('userToken') || ''
        },
        success: (res) => {
          console.log('取消店铺收藏成功:', res.data);
          resolve(res.data);
        },
        fail: (err) => {
          console.error('取消店铺收藏失败:', err);
          reject(err);
        }
      });
    });
  },
  
  /**
   * 获取店铺收藏列表
   * @param {string} userId 用户ID
   * @param {number} page 页码
   * @param {number} pageSize 每页数量
   * @returns {Promise}
   */
  getShopList(userId, page = 1, pageSize = 20) {
    return new Promise((resolve, reject) => {
      uni.request({
        url: 'http://localhost:8089/api/shop/fans/followed-shops',
        method: 'GET',
        header: {
          'Authorization': uni.getStorageSync('userToken') ? `Bearer ${uni.getStorageSync('userToken')}` : ''
        },
        success: (res) => {
          console.log('获取店铺收藏列表成功:', res.data);
          resolve(res.data);
        },
        fail: (err) => {
          console.error('获取店铺收藏列表失败:', err);
          reject(err);
        }
      });
    });
  },
  
  /**
   * 检查店铺是否已收藏
   * @param {string} userId 用户ID
   * @param {number|string} shopId 店铺ID
   * @returns {Promise}
   */
  checkShop(userId, shopId) {
    return new Promise((resolve, reject) => {
      uni.request({
        url: BASE_URL + 'checkShop',
        method: 'GET',
        data: { userId, shopId },
        header: {
          'Authorization': uni.getStorageSync('userToken') || ''
        },
        success: (res) => {
          console.log('检查店铺收藏状态成功:', res.data);
          resolve(res.data);
        },
        fail: (err) => {
          console.error('检查店铺收藏状态失败:', err);
          reject(err);
        }
      });
    });
  }
};

/**
 * 全局收藏管理器
 */
export const collectManager = {
  
  /**
   * 检查登录状态
   * @returns {boolean}
   */
  checkLogin() {
    const userId = uni.getStorageSync('userId');
    const token = uni.getStorageSync('userToken');
    
    if (!userId || !token) {
      return false;
    }
    
    return true;
  },
  
  /**
   * 获取当前用户ID
   * @returns {string}
   */
  getUserId() {
    return uni.getStorageSync('userId') || app.globalData.userId || '';
  },
  
  /**
   * 获取用户Token
   * @returns {string}
   */
  getUserToken() {
    return uni.getStorageSync('userToken') || app.globalData.userToken || '';
  },
  
  /**
   * 同步收藏状态到全局
   * @param {string} goodsId 商品ID
   * @param {boolean} isCollected 是否收藏
   */
  syncCollectStatus(goodsId, isCollected) {
    // 发送全局事件
    uni.$emit('collectStatusChanged', {
      goodsId: goodsId,
      isCollected: isCollected,
      timestamp: new Date().getTime()
    });
    
    // 更新全局数据
    if (app.globalData.collectStatus) {
      app.globalData.collectStatus[goodsId] = isCollected;
    } else {
      app.globalData.collectStatus = { [goodsId]: isCollected };
    }
    
    console.log('同步收藏状态:', goodsId, isCollected);
  },
  
  /**
   * 初始化收藏管理器
   */
  init() {
    // 监听登录状态变化
    uni.$on('loginStatusChanged', (data) => {
      console.log('登录状态变化，重新加载收藏数据:', data);
      this.loadCollectData();
    });
  },
  
  /**
   * 加载用户收藏数据
   */
  async loadCollectData() {
    const userId = this.getUserId();
    if (!userId) {
      console.log('用户未登录，跳过加载收藏数据');
      return;
    }
    
    try {
      // 加载商品收藏数据
      const goodsResult = await collectApi.getGoodsList(userId, 1, 100);
      if (goodsResult.code === 200) {
        const collectedGoodsIds = goodsResult.data.list.map(item => item.goods_id);
        app.globalData.collectedGoodsIds = collectedGoodsIds;
        console.log('加载收藏的商品ID列表:', collectedGoodsIds);
      }
      
      // 加载店铺收藏数据
      const shopResult = await collectApi.getShopList(userId, 1, 50);
      if (shopResult.code === 200) {
        const collectedShopIds = shopResult.data.list.map(item => item.shop_id);
        app.globalData.collectedShopIds = collectedShopIds;
        console.log('加载收藏的店铺ID列表:', collectedShopIds);
      }
    } catch (error) {
      console.error('加载收藏数据失败:', error);
    }
  }
};

// 导出默认配置
export default {
  BASE_URL,
  collectApi,
  collectManager
};