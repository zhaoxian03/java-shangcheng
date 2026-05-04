// 反馈相关API
const API_BASE = 'http://localhost:8089/api'; // 确保端口匹配你的后端端口

export const feedbackApi = {
	// 提交反馈
	async submitFeedback(data) {
		const token = uni.getStorageSync('userToken') || '';
		
		return uni.request({
			url: `${API_BASE}/feedback/submit`,
			method: 'POST',
			header: {
				'Content-Type': 'application/json',
				'Authorization': `Bearer ${token}`
			},
			data: {
				type: data.type,
				content: data.content,
				contact: data.contact || ''
				// 删除图片字段
			}
		}).then(res => res.data);
	},
	
	// 获取我的反馈列表（分页）
	async getMyFeedbacks(page = 1, pageSize = 10) {
		const token = uni.getStorageSync('userToken') || '';
		
		return uni.request({
			url: `${API_BASE}/feedback/my`,
			method: 'GET',
			header: {
				'Authorization': `Bearer ${token}`
			},
			data: {
				page: page,
				pageSize: pageSize
			}
		}).then(res => res.data);
	},
	
	// 获取反馈详情
	async getFeedbackDetail(id) {
		const token = uni.getStorageSync('userToken') || '';
		
		return uni.request({
			url: `${API_BASE}/feedback/detail/${id}`,
			method: 'GET',
			header: {
				'Authorization': `Bearer ${token}`
			}
		}).then(res => res.data);
	},
	
	// 删除反馈
	async deleteFeedback(id) {
		const token = uni.getStorageSync('userToken') || '';
		
		return uni.request({
			url: `${API_BASE}/feedback/delete/${id}`,
			method: 'DELETE',
			header: {
				'Authorization': `Bearer ${token}`
			}
		}).then(res => res.data);
	}
};