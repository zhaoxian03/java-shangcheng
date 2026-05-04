// api/address.js

//五一快乐

const BASE_URL = 'http://localhost:8089/api';

// 获取token
const getToken = () => {
  const token = uni.getStorageSync('userToken') || '';
  console.log('🔐 从存储获取token:', token ? `有token(${token.substring(0, 10)}...)` : '无token');
  return token;
};

// 获取地址列表 - 修复版
const getList = (userId) => {
  return new Promise((resolve, reject) => {
    const token = getToken();
    
    // 验证userId
    if (!userId || userId === '') {
      console.error('❌ 请求参数错误: userId不能为空');
      reject('请求参数错误: userId不能为空');
      return;
    }
    
    const requestData = { userId };
    
    console.log('📤 发送地址列表请求:');
    console.log('  URL:', `${BASE_URL}/address/list`);
    console.log('  Method: GET');
    console.log('  Data:', requestData);
    
    uni.request({
      url: `${BASE_URL}/address/list`,
      method: 'GET',
      data: requestData,
      header: {
        'Authorization': token ? `Bearer ${token}` : '',
        'Content-Type': 'application/json'
      },
      success: (res) => {
        console.log('📥 地址列表响应状态:', res.statusCode);
        console.log('📥 地址列表响应数据:', res.data);
        
        if (res.statusCode === 200) {
          if (res.data && res.data.code === 200) {
            // 成功：返回data数组，并转换字段名
            const data = res.data.data || [];
            console.log(`✅ 获取到 ${data.length} 条地址数据`);
            
            // 将后端的is_default字段转换为前端的isDefault字段
            const formattedData = data.map(item => {
              // 确保isDefault字段是数字类型，并且正确处理后端返回的is_default字段
              const isDefaultValue = item.is_default !== undefined ? item.is_default : (item.isDefault || 0);
              console.log(`🔄 地址ID ${item.id} 的is_default值:`, item.is_default, '转换后isDefault值:', isDefaultValue);
              return {
                ...item,
                isDefault: parseInt(isDefaultValue, 10) // 确保是数字类型
              };
            });
            
            resolve(formattedData);
          } else {
            // 业务错误：使用 msg 字段（后端返回的是 msg）
            const errorMsg = res.data?.msg || res.data?.message || `请求失败: ${res.data?.code || '未知错误'}`;
            console.error('❌ 业务逻辑失败:', errorMsg);
            reject(errorMsg);
          }
        } else {
          // HTTP错误
          console.error(`❌ HTTP错误: ${res.statusCode}`);
          reject(`服务器错误: ${res.statusCode}`);
        }
      },
      fail: (error) => {
        console.error('❌ 网络请求失败:', error);
        console.error('  错误详情:', error.errMsg);
        
        if (error.errMsg && error.errMsg.includes('request:fail')) {
          reject('无法连接到服务器，请检查：\n1. 后端服务是否启动\n2. 是否有跨域问题\n3. 网络是否正常');
        } else {
          reject('网络请求失败: ' + error.errMsg);
        }
      }
    });
  });
};

// 获取地址详情
const getDetail = (id, userId) => {
  return new Promise((resolve, reject) => {
    const token = getToken();
    
    console.log('📤 获取地址详情:', { id, userId });
    
    uni.request({
      url: `${BASE_URL}/address/detail`,
      method: 'GET',
      data: { id, userId },
      header: {
        'Authorization': token ? `Bearer ${token}` : '',
        'Content-Type': 'application/json'
      },
      success: (res) => {
        console.log('📥 地址详情响应:', res.data);
        
        if (res.statusCode === 200) {
          if (res.data && res.data.code === 200) {
            const data = res.data.data || {};
            // 将后端的is_default字段转换为前端的isDefault字段
            const formattedData = {
              ...data,
              isDefault: data.is_default || data.isDefault || 0
            };
            resolve(formattedData);
          } else {
            reject(res.data?.msg || res.data?.message || '获取详情失败');
          }
        } else {
          reject(`服务器错误: ${res.statusCode}`);
        }
      },
      fail: (error) => {
        console.error('获取详情请求失败:', error);
        reject('网络请求失败: ' + error.errMsg);
      }
    });
  });
};

// 添加地址
const addAddress = (addressData) => {
  return new Promise((resolve, reject) => {
    const token = getToken();
    
    console.log('📤 添加地址数据:', addressData);
    
    uni.request({
      url: `${BASE_URL}/address/add`,
      method: 'POST',
      data: addressData,
      header: {
        'Authorization': token ? `Bearer ${token}` : '',
        'Content-Type': 'application/json'
      },
      success: (res) => {
        console.log('📥 添加地址响应:', res.data);
        
        if (res.statusCode === 200) {
          if (res.data && res.data.code === 200) {
            resolve(res.data.data || {});
          } else {
            reject(res.data?.msg || res.data?.message || '添加失败');
          }
        } else {
          reject(`服务器错误: ${res.statusCode}`);
        }
      },
      fail: (error) => {
        console.error('添加地址请求失败:', error);
        reject('网络请求失败: ' + error.errMsg);
      }
    });
  });
};

// 更新地址
const updateAddress = (addressData) => {
  return new Promise((resolve, reject) => {
    const token = getToken();
    
    console.log('📤 更新地址数据:', addressData);
    
    uni.request({
      url: `${BASE_URL}/address/update`,
      method: 'POST',
      data: addressData,
      header: {
        'Authorization': token ? `Bearer ${token}` : '',
        'Content-Type': 'application/json'
      },
      success: (res) => {
        console.log('📥 更新地址响应:', res.data);
        
        if (res.statusCode === 200) {
          if (res.data && res.data.code === 200) {
            resolve(res.data.data || {});
          } else {
            reject(res.data?.msg || res.data?.message || '更新失败');
          }
        } else {
          reject(`服务器错误: ${res.statusCode}`);
        }
      },
      fail: (error) => {
        console.error('更新地址请求失败:', error);
        reject('网络请求失败: ' + error.errMsg);
      }
    });
  });
};

// 删除地址
const deleteAddress = (id, userId) => {
  return new Promise((resolve, reject) => {
    const token = getToken();
    
    console.log('📤 删除地址:', { id, userId });
    
    uni.request({
      url: `${BASE_URL}/address/delete`,
      method: 'POST',
      data: { id, userId },
      header: {
        'Authorization': token ? `Bearer ${token}` : '',
        'Content-Type': 'application/json'
      },
      success: (res) => {
        console.log('📥 删除地址响应:', res.data);
        
        if (res.statusCode === 200) {
          if (res.data && res.data.code === 200) {
            resolve(res.data.data || {});
          } else {
            reject(res.data?.msg || res.data?.message || '删除失败');
          }
        } else {
          reject(`服务器错误: ${res.statusCode}`);
        }
      },
      fail: (error) => {
        console.error('删除地址请求失败:', error);
        reject('网络请求失败: ' + error.errMsg);
      }
    });
  });
};

// 设置默认地址
const setDefault = (id, userId) => {
  return new Promise((resolve, reject) => {
    const token = getToken();
    
    console.log('📤 设置默认地址:', { id, userId });
    
    uni.request({
      url: `${BASE_URL}/address/set-default`,
      method: 'POST',
      data: { id, userId },
      header: {
        'Authorization': token ? `Bearer ${token}` : '',
        'Content-Type': 'application/json'
      },
      success: (res) => {
        console.log('📥 设置默认地址响应:', res.data);
        
        if (res.statusCode === 200) {
          if (res.data && res.data.code === 200) {
            resolve(res.data.data || {});
          } else {
            reject(res.data?.msg || res.data?.message || '设置失败');
          }
        } else {
          reject(`服务器错误: ${res.statusCode}`);
        }
      },
      fail: (error) => {
        console.error('设置默认地址请求失败:', error);
        reject('网络请求失败: ' + error.errMsg);
      }
    });
  });
};

// 获取默认地址
const getDefaultAddress = (userId) => {
  return new Promise((resolve, reject) => {
    const token = getToken();
    
    console.log('📤 获取默认地址:', { userId });
    
    uni.request({
      url: `${BASE_URL}/address/default`,
      method: 'GET',
      data: { userId },
      header: {
        'Authorization': token ? `Bearer ${token}` : '',
        'Content-Type': 'application/json'
      },
      success: (res) => {
        console.log('📥 获取默认地址响应:', res.data);
        
        if (res.statusCode === 200) {
          if (res.data && res.data.code === 200) {
            const data = res.data.data || null;
            if (data) {
              // 将后端的is_default字段转换为前端的isDefault字段
              const formattedData = {
                ...data,
                isDefault: data.is_default || data.isDefault || 0
              };
              resolve(formattedData);
            } else {
              resolve(null);
            }
          } else {
            // 如果没有默认地址，可能返回404或其他状态，这里不视为错误
            resolve(null);
          }
        } else if (res.statusCode === 404) {
          // 没有默认地址
          resolve(null);
        } else {
          reject(`服务器错误: ${res.statusCode}`);
        }
      },
      fail: (error) => {
        console.error('获取默认地址请求失败:', error);
        reject('网络请求失败: ' + error.errMsg);
      }
    });
  });
};

// 省市区三级联动数据（示例，实际可能需要从后端获取）
const getRegionData = () => {
  return new Promise((resolve, reject) => {
    // 这里可以调用后端接口获取省市区数据
    // 或者使用本地静态数据
    const staticRegionData = {
      provinces: [
        { code: '110000', name: '北京市' },
        { code: '310000', name: '上海市' },
        { code: '440000', name: '广东省' },
        // ... 其他省份
      ],
      // 实际应用中应该有完整的省市区数据
    };
    
    resolve(staticRegionData);
  });
};

export const addressApi = {
  getList,
  getDetail,
  add: addAddress,
  update: updateAddress,
  delete: deleteAddress,
  setDefault,
  getDefaultAddress,
  getRegionData
};

// 导出默认对象，方便按需导入
export default addressApi;