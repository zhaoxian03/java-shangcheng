// 测试API路径的脚本
const testApiPaths = async () => {
  console.log('🔍 开始测试API路径...');
  
  // 测试不同的API路径
  const pathsToTest = [
    '/api/address/add',
    '/api/address/update',
    '/address/add',
    '/address/update',
    '/update',
    '/add'
  ];
  
  for (const path of pathsToTest) {
    console.log(`\n📡 测试路径: ${path}`);
    
    try {
      const res = await uni.request({
        url: path,
        method: 'POST',
        data: {
          userId: 1,
          name: '测试用户',
          phone: '13800138000',
          province: '北京',
          city: '北京',
          district: '朝阳区',
          detail: '测试地址',
          isDefault: 0
        },
        header: {
          'Content-Type': 'application/json'
        }
      });
      
      console.log(`✅ 响应状态: ${res.statusCode}`);
      console.log(`   响应数据: ${JSON.stringify(res.data)}`);
      
      if (res.statusCode === 200) {
        console.log(`🎉 路径 ${path} 可能有效!`);
      } else {
        console.log(`❌ 路径 ${path} 返回错误状态: ${res.statusCode}`);
      }
    } catch (error) {
      console.log(`❌ 路径 ${path} 请求失败: ${error.message}`);
    }
  }
  
  console.log('\n🔍 API路径测试完成!');
};

export { testApiPaths };