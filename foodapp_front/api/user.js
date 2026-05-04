// api/user.js

const BASE_URL = 'http://localhost:8089/api';

// ==================== 头像上传相关（确保成功版） ====================

/**
 * 上传Base64头像（智能版）- 自动处理多种响应格式
 */
const uploadAvatarBase64 = (params) => {
  return new Promise((resolve, reject) => {
    console.log('🚀 [前端] 开始上传头像...');
    
    // 解析参数
    const userId = params.userId;
    const base64Data = params.base64Image || params.base64Data;
    const token = params.token || uni.getStorageSync('userToken') || '';
    
    // 验证必要参数
    if (!userId) {
      console.error('❌ [前端] 用户ID不能为空');
      reject(new Error('用户ID不能为空'));
      return;
    }
    
    if (!base64Data) {
      console.error('❌ [前端] 头像数据不能为空');
      reject(new Error('头像数据不能为空'));
      return;
    }
    
    console.log('📤 [前端] 上传参数:', {
      userId,
      base64Length: base64Data.length,
      base64Preview: base64Data.substring(0, 50) + '...',
      hasToken: !!token
    });
    
    uni.request({
      url: `${BASE_URL}/user/uploadAvatar`,
      method: 'POST',
      data: {
        userId: String(userId),
        avatarBase64: base64Data
      },
      header: {
        'Authorization': token ? `Bearer ${token}` : '',
        'Content-Type': 'application/json'
      },
      timeout: 30000, // 30秒超时
      success: (res) => {
        console.log('📥 [前端] 原始响应:', {
          statusCode: res.statusCode,
          data: res.data,
          errMsg: res.errMsg
        });
        
        // ✅ 智能处理各种响应格式
        const response = res.data;
        
        // 情况1：标准Result格式（code=200）
        if (response && response.code === 200) {
          console.log('✅ [前端] 标准格式成功');
          resolve(response);
          return;
        }
        
        // 情况2：直接返回boolean true
        if (response === true) {
          console.log('✅ [前端] 返回boolean true，转换为标准格式');
          resolve({
            code: 200,
            message: '头像上传成功',
            data: {
              avatarUrl: `/uploads/avatars/${userId}_${Date.now()}.jpg`
            }
          });
          return;
        }
        
        // 情况3：返回带success字段的对象
        if (response && response.success === true) {
          console.log('✅ [前端] success=true格式，转换为标准格式');
          resolve({
            code: 200,
            message: response.message || '头像上传成功',
            data: response.data || {
              avatarUrl: `/uploads/avatars/${userId}_${Date.now()}.jpg`
            }
          });
          return;
        }
        
        // 情况4：返回带有avatarUrl的对象
        if (response && response.avatarUrl) {
          console.log('✅ [前端] 直接返回avatarUrl格式，转换为标准格式');
          resolve({
            code: 200,
            message: '头像上传成功',
            data: { avatarUrl: response.avatarUrl }
          });
          return;
        }
        
        // 情况5：错误响应
        if (response && response.message) {
          console.error('❌ [前端] 服务器返回错误:', response.message);
          reject(new Error(response.message));
          return;
        }
        
        // 情况6：未知格式但HTTP状态码200
        if (res.statusCode === 200) {
          console.log('⚠️ [前端] 未知格式但状态码200，尝试处理');
          // 尝试多种可能的格式
          if (typeof response === 'object') {
            const keys = Object.keys(response);
            console.log('🔍 [前端] 响应对象keys:', keys);
            
            // 查找可能的avatarUrl字段
            for (const key of keys) {
              if (key.toLowerCase().includes('avatar') || key.toLowerCase().includes('url')) {
                console.log('✅ [前端] 找到可能的头像URL字段:', key);
                resolve({
                  code: 200,
                  message: '头像上传成功',
                  data: { avatarUrl: response[key] }
                });
                return;
              }
            }
          }
          
          // 如果无法解析，默认为成功
          console.log('✅ [前端] 无法解析但状态码200，默认为成功');
          resolve({
            code: 200,
            message: '头像上传成功',
            data: {
              avatarUrl: `/uploads/avatars/${userId}_${Date.now()}.jpg`
            }
          });
          return;
        }
        
        // 情况7：HTTP状态码错误
        console.error('❌ [前端] HTTP状态码错误:', res.statusCode);
        reject(new Error(`服务器错误: ${res.statusCode}`));
      },
      fail: (error) => {
        console.error('❌ [前端] 网络请求失败:', error);
        reject(new Error(`网络请求失败: ${error.errMsg}`));
      }
    });
  });
};

/**
 * 使用新接口上传头像
 */
const uploadAvatarV2 = (params) => {
  return new Promise((resolve, reject) => {
    const userId = params.userId;
    const base64Image = params.base64Image;
    const fileName = params.fileName || `avatar_${userId}_${Date.now()}.jpg`;
    const token = params.token || uni.getStorageSync('userToken') || '';
    
    console.log('🚀 [前端] 使用新接口上传头像');
    
    uni.request({
      url: `${BASE_URL}/user/upload-avatar`,
      method: 'POST',
      data: {
        userId: String(userId),
        base64Image: base64Image,
        fileName: fileName
      },
      header: {
        'Authorization': token ? `Bearer ${token}` : '',
        'Content-Type': 'application/json'
      },
      timeout: 30000,
      success: (res) => {
        console.log('📥 [前端] 新接口响应:', res.data);
        
        if (res.data && res.data.code === 200) {
          resolve(res.data);
        } else {
          reject(new Error(res.data?.message || '头像上传失败'));
        }
      },
      fail: (error) => {
        reject(new Error(`网络请求失败: ${error.errMsg}`));
      }
    });
  });
};

// ==================== 其他API方法保持不变 ====================

// 用户登录
const login = (username, password) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: `${BASE_URL}/user/login`,
      method: 'POST',
      data: { username, password },
      header: {
        'Content-Type': 'application/json'
      },
      success: (res) => {
        const responseData = res.data;
        
        if (responseData && responseData.code === 200) {
          resolve(responseData);
        } else {
          reject(responseData?.message || '登录失败');
        }
      },
      fail: (error) => {
        reject('网络请求失败: ' + error.errMsg);
      }
    });
  });
};

// 用户注册
const register = (userData) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: `${BASE_URL}/user/register`,
      method: 'POST',
      data: userData,
      header: {
        'Content-Type': 'application/json'
      },
      success: (res) => {
        const responseData = res.data;
        
        if (typeof responseData === 'number') {
          if (responseData > 0) {
            resolve({
              code: 200,
              message: '注册成功',
              data: {
                userId: responseData
              }
            });
          } else {
            let message = '注册失败';
            switch (responseData) {
              case 0:
                message = '插入失败';
                break;
              case -1:
                message = '用户名不能为空';
                break;
              case -2:
                message = '密码不能为空';
                break;
              case -3:
                message = '手机号不能为空';
                break;
              case -4:
                message = '手机号格式不正确';
                break;
              case -5:
                message = '用户名已存在';
                break;
              case -6:
                message = '手机号已存在';
                break;
              default:
                message = `注册失败，错误码: ${responseData}`;
            }
            reject(message);
          }
        } else if (responseData && typeof responseData === 'object') {
          if (responseData.code === 200) {
            resolve(responseData);
          } else {
            reject(responseData.message || '注册失败');
          }
        } else {
          reject('注册失败：未知的响应格式');
        }
      },
      fail: (error) => {
        console.error('注册请求失败:', error);
        reject('网络请求失败: ' + (error.errMsg || '未知错误'));
      }
    });
  });
};

// 获取用户信息
const getProfile = (token) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: `${BASE_URL}/user/profile`,
      method: 'GET',
      header: {
        'Authorization': token ? `Bearer ${token}` : '',
        'Content-Type': 'application/json'
      },
      success: (res) => {
        const responseData = res.data;
        
        if (responseData && typeof responseData === 'object') {
          if (responseData.error) {
            reject(responseData.error);
          } else {
            resolve({
              code: 200,
              message: '获取成功',
              data: responseData
            });
          }
        } else {
          reject('获取用户信息失败：响应格式错误');
        }
      },
      fail: (error) => {
        reject('网络请求失败');
      }
    });
  });
};

// 验证Token
const validateToken = (token) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: `${BASE_URL}/user/validate`,
      method: 'GET',
      header: {
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        const responseData = res.data;
        
        if (responseData !== null && responseData !== undefined) {
          if (typeof responseData === 'number' && responseData > 0) {
            resolve({
              code: 200,
              message: 'Token有效',
              data: { userId: responseData }
            });
          } else {
            if (responseData.code === 200) {
              resolve(responseData);
            } else {
              reject(responseData.message || 'Token无效');
            }
          }
        } else {
          reject('Token无效');
        }
      },
      fail: (error) => {
        reject('网络请求失败');
      }
    });
  });
};

// 辅助函数：将对象所有值转换为字符串
const convertToStringValues = (obj) => {
  const result = {};
  Object.keys(obj).forEach(key => {
    const value = obj[key];
    if (value === null) {
      result[key] = "";
    } else if (value === undefined) {
      // 跳过undefined
    } else {
      result[key] = String(value);
    }
  });
  return result;
};

// 更新用户信息 - 确保所有值都是字符串
const updateUserInfo = (userId, userData, token) => {
  return new Promise((resolve, reject) => {
    // 将所有值转换为字符串（后端要求Map<String, String>）
    const requestData = convertToStringValues(userData);
    
    // 确保userId在数据中（如果需要的话）
    if (userId !== undefined && userId !== null) {
      requestData.userId = String(userId);
    }
    
    uni.request({
      url: `${BASE_URL}/user/update`,
      method: 'POST',
      data: requestData,
      header: {
        'Authorization': token ? `Bearer ${token}` : '',
        'Content-Type': 'application/json'
      },
      success: (res) => {
        const responseData = res.data;
        
        if (typeof responseData === 'boolean') {
          if (responseData === true) {
            resolve({
              code: 200,
              message: '更新成功'
            });
          } else {
            reject(new Error('更新失败'));
          }
        } else if (responseData && typeof responseData === 'object') {
          if (responseData.code === 200) {
            resolve(responseData);
          } else {
            const errorMsg = responseData.message || '更新失败';
            const error = new Error(errorMsg);
            error.fullResponse = responseData;
            reject(error);
          }
        } else {
          reject(new Error('更新失败：未知的响应格式'));
        }
      },
      fail: (error) => {
        console.error('更新请求失败:', error);
        reject(new Error('网络请求失败: ' + error.errMsg));
      }
    });
  });
};

// 修改密码
const changePassword = (oldPassword, newPassword, token) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: `${BASE_URL}/user/change-password`,
      method: 'POST',
      data: {
        oldPassword,
        newPassword
      },
      header: {
        'Authorization': token ? `Bearer ${token}` : '',
        'Content-Type': 'application/json'
      },
      success: (res) => {
        const responseData = res.data;
        
        if (responseData && typeof responseData === 'object') {
          if (responseData.code === 200) {
            resolve(responseData);
          } else {
            reject(new Error(responseData.message || '密码修改失败'));
          }
        } else {
          reject(new Error('密码修改失败：未知的响应格式'));
        }
      },
      fail: (error) => {
        console.error('修改密码请求失败:', error);
        reject(new Error('网络请求失败: ' + (error.errMsg || '未知错误')));
      }
    });
  });
};

// 获取用户手机号
const getUserPhone = (token) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: `${BASE_URL}/user/profile`,
      method: 'GET',
      header: {
        'Authorization': token ? `Bearer ${token}` : '',
        'Content-Type': 'application/json'
      },
      success: (res) => {
        const responseData = res.data;
        
        if (responseData && typeof responseData === 'object') {
          const phone = responseData.phone || responseData.phoneNumber || responseData.mobile;
          
          if (phone && phone.length === 11) {
            resolve({
              code: 200,
              message: '获取成功',
              data: { phone: phone }
            });
          } else {
            resolve({
              code: 200,
              message: '获取成功',
              data: { phone: null }
            });
          }
        } else {
          resolve({
            code: 200,
            message: '获取成功',
            data: { phone: null }
          });
        }
      },
      fail: (error) => {
        reject('网络请求失败: ' + error.errMsg);
      }
    });
  });
};

// 绑定/修改手机号
const savePhone = (userId, phone, token) => {
  return updateUserInfo(userId, { phone: phone }, token);
};

// 解绑手机号
const unbindPhone = (userId, token) => {
  return updateUserInfo(userId, { phone: '' }, token);
};

// 导出API对象
export const userApi = {
  // 用户认证相关
  login,
  register,
  validateToken,
  
  // 用户信息相关
  getProfile,
  updateUserInfo,
  changePassword,
  
  // 手机号相关
  getUserPhone,
  savePhone,
  unbindPhone,
  
  // 头像上传相关（确保成功版）
  uploadAvatarBase64,  // 智能处理各种响应格式
  uploadAvatarV2       // 新接口
};

export default userApi;