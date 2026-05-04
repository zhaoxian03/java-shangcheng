// 封装uni.request请求
const BASE_URL = 'http://localhost:8089/api'

const request = (options) => {
  return new Promise((resolve, reject) => {
    // 获取token
    const token = uni.getStorageSync('token')
    
    // 合并header
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : '',
      ...options.header
    }
    
    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: headers,
      success: (res) => {
        // 统一处理响应
        if (res.statusCode === 200) {
          // 业务成功
          if (res.data.code === 200) {
            resolve(res.data)
          } else {
            // 业务失败
            uni.showToast({
              title: res.data.msg || '请求失败',
              icon: 'none'
            })
            reject(res.data)
          }
        } else if (res.statusCode === 401) {
          // 未授权，检查是否已经在登录页面
          const pages = getCurrentPages()
          const currentPage = pages[pages.length - 1]
          if (currentPage.route !== 'pages/login/login') {
            // 不在登录页面才跳转到登录页
            uni.showToast({
              title: '登录已过期，请重新登录',
              icon: 'none'
            })
            setTimeout(() => {
              // 使用redirectTo替换当前页面，清除之前的页面历史
              uni.redirectTo({
                url: '/pages/login/login'
              })
            }, 1500)
          }
          reject(res.data)
        } else {
          // 其他错误
          uni.showToast({
            title: '网络错误，请稍后重试',
            icon: 'none'
          })
          reject(res.data)
        }
      },
      fail: (err) => {
        uni.showToast({
          title: '网络错误，请稍后重试',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

// 导出常用请求方法
export default {
  get(url, data, config) {
    return request({
      url,
      method: 'GET',
      data,
      ...config
    })
  },
  
  post(url, data, config) {
    return request({
      url,
      method: 'POST',
      data,
      ...config
    })
  },
  
  put(url, data, config) {
    return request({
      url,
      method: 'PUT',
      data,
      ...config
    })
  },
  
  delete(url, data, config) {
    return request({
      url,
      method: 'DELETE',
      data,
      ...config
    })
  }
}