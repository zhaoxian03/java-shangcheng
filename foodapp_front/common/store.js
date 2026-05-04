// common/store.js

class Store {
  constructor() {
    this.state = {
      isLogin: false,
      userId: null,
      userToken: null,
      username: null,
      userInfo: null
    };
    
    // 尝试从本地存储恢复状态
    this.restoreFromStorage();
  }
  
  // 从本地存储恢复状态
  restoreFromStorage() {
    try {
      const token = uni.getStorageSync('userToken');
      const userId = uni.getStorageSync('userId');
      const username = uni.getStorageSync('username');
      const userInfo = uni.getStorageSync('userInfo');
      
      if (token && userId) {
        this.state.isLogin = true;
        this.state.userId = userId;
        this.state.userToken = token;
        this.state.username = username;
        this.state.userInfo = userInfo || {};
      }
    } catch (error) {
      console.error('从存储恢复状态失败:', error);
    }
  }
  
  // 设置登录状态
  setLogin(data) {
    const { token, userId, username, userInfo } = data;
    
    this.state.isLogin = true;
    this.state.userId = userId;
    this.state.userToken = token;
    this.state.username = username;
    this.state.userInfo = userInfo || {};
    
    // 保存到本地存储
    uni.setStorageSync('userToken', token);
    uni.setStorageSync('userId', userId);
    uni.setStorageSync('username', username);
    uni.setStorageSync('userInfo', userInfo || {});
    
    console.log('设置登录状态:', this.state);
  }
  
  // 清除登录状态
  clearLogin() {
    this.state.isLogin = false;
    this.state.userId = null;
    this.state.userToken = null;
    this.state.username = null;
    this.state.userInfo = null;
    
    // 清除本地存储
    uni.removeStorageSync('userToken');
    uni.removeStorageSync('userId');
    uni.removeStorageSync('username');
    uni.removeStorageSync('userInfo');
  }
  
  // 获取状态
  getState() {
    return { ...this.state };
  }
  
  // 获取用户ID
  getUserId() {
    return this.state.userId;
  }
  
  // 获取token
  getToken() {
    return this.state.userToken;
  }
  
  // 是否已登录
  isLoggedIn() {
    return this.state.isLogin && this.state.userId && this.state.userToken;
  }
}

// 创建单例
const store = new Store();

// 导出
export default store;