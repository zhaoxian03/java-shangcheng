// 直接使用uni.request连接你的Java后端
const baseUrl = 'http://localhost:8089/api/signin'; // 改成你的后端地址

export const signinApi = {
    // 获取签到信息
    getInfo(userId) {
        return new Promise((resolve) => {
            uni.request({
                url: `${baseUrl}/info/${userId}`,
                method: 'GET',
                success: (res) => {
                    resolve(res.data);
                },
                fail: (err) => {
                    resolve({
                        code: 500,
                        message: '网络请求失败',
                        data: {
                            todaySigned: false,
                            totalDays: 0,
                            continuousDays: 0,
                            monthSigninCount: 0
                        }
                    });
                }
            });
        });
    },

    // 执行签到
    doSignin(userId) {
        return new Promise((resolve) => {
            uni.request({
                url: baseUrl,
                method: 'POST',
                data: { userId },
                header: {
                    'content-type': 'application/json'
                },
                success: (res) => {
                    resolve(res.data);
                },
                fail: (err) => {
                    resolve({
                        code: 500,
                        message: '签到失败',
                        data: {}
                    });
                }
            });
        });
    }
};