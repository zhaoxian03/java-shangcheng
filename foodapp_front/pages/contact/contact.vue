<template>
	<view class="container">
		<view class="header">
			<view class="search-box">
				<input 
					type="text" 
					v-model="searchKeyword" 
					@input="searchContacts" 
					placeholder="搜索联系人..." 
					class="search-input"
				/>
			</view>
		</view>
		
		<view class="content">
			<scroll-view class="contact-list" scroll-y="true">
				<view 
					v-for="contact in contacts" 
					:key="contact.contactId"
					class="contact-item"
					:class="{ active: selectedContact && selectedContact.contactId === contact.contactId }"
					@click="selectContact(contact)"
				>
					<view class="contact-info">
						<text class="contact-name">{{ contact.contactName }}</text>
						<text class="contact-phone">{{ contact.phone }}</text>
					</view>
				</view>
			</scroll-view>
			
			<view class="contact-form">
				<view class="form-group">
					<text class="label">用户ID:</text>
					<input type="number" v-model="currentContact.userId" placeholder="请输入用户ID" class="input" />
				</view>
				<view class="form-group">
					<text class="label">姓名:</text>
					<input type="text" v-model="currentContact.contactName" placeholder="请输入姓名" class="input" />
				</view>
				<view class="form-group">
					<text class="label">电话:</text>
					<input type="text" v-model="currentContact.phone" placeholder="请输入电话" class="input" />
				</view>
				<view class="form-group">
					<text class="label">邮箱:</text>
					<input type="email" v-model="currentContact.contactEmail" placeholder="请输入邮箱" class="input" />
				</view>
				<view class="form-group">
					<text class="label">分组:</text>
					<picker @change="onGroupChange" :value="groupIndex" :range="groupNames" range-key="groupName">
						<view class="picker">{{ currentContact.groupId ? groups[groupIndex].groupName : '请选择分组' }}</view>
					</picker>
				</view>
				
				<view class="button-group" v-if="!isEditing">
					<button class="btn btn-add" @click="addContact">添加联系人</button>
				</view>
				<view class="button-group" v-else>
					<button class="btn btn-update" @click="updateContact">更新联系人</button>
					<button class="btn btn-delete" @click="deleteContact">删除联系人</button>
					<button class="btn btn-cancel" @click="cancelEdit">取消</button>
				</view>
			</view>
		</view>
		
		<uni-popup v-model="showMessage" :type="messageType" v-if="message">
			<text>{{ message }}</text>
		</uni-popup>
	</view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// 响应式数据
const contacts = ref([])
const groups = ref([])
const currentContact = ref({
    contactId: null,
    userId: 1,
    contactName: '',
    phone: '',
    contactEmail: '',
    groupId: null
})
const selectedContact = ref(null)
const isEditing = ref(false)
const message = ref('')
const messageType = ref('success')
const searchKeyword = ref('')
const groupIndex = ref(0)

// 计算属性
const groupNames = computed(() => {
    return groups.value.map(group => ({ groupName: group.groupName }))
})

// 方法定义
const loadContacts = async () => {
    try {
        const response = await uni.request({
            url: 'http://localhost:8089/api/contact/listByUserId',
            method: 'GET',
            data: { userId: 1 }
        })
        
        if (response.data.code === 200) {
            contacts.value = response.data.data || []
        } else {
            showMessage('加载联系人失败: ' + response.data.msg, 'error')
        }
    } catch (error) {
        console.error('加载联系人出错:', error)
        showMessage('加载联系人出错: ' + error.message, 'error')
    }
}

const loadGroups = async () => {
    try {
        const response = await uni.request({
            url: 'http://localhost:8089/api/contact/group/list',
            method: 'GET',
            data: { userId: 1 }
        })
        
        if (response.data.code === 200) {
            groups.value = response.data.data || []
        } else {
            showMessage('加载分组失败: ' + response.data.msg, 'error')
        }
    } catch (error) {
        console.error('加载分组出错:', error)
        showMessage('加载分组出错: ' + error.message, 'error')
    }
}

const addContact = async () => {
    try {
        const response = await uni.request({
            url: 'http://localhost:8089/api/contact/add',
            method: 'POST',
            header: {
                'Content-Type': 'application/json'
            },
            data: currentContact.value
        })
        
        if (response.data.code === 200) {
            showMessage('联系人添加成功', 'success')
            resetForm()
            loadContacts() // 重新加载联系人列表
        } else {
            showMessage('添加失败: ' + response.data.msg, 'error')
        }
    } catch (error) {
        console.error('添加联系人出错:', error)
        showMessage('添加联系人出错: ' + error.message, 'error')
    }
}

const updateContact = async () => {
    try {
        const response = await uni.request({
            url: 'http://localhost:8089/api/contact/update',
            method: 'PUT',
            header: {
                'Content-Type': 'application/json'
            },
            data: currentContact.value
        })
        
        if (response.data.code === 200) {
            showMessage('联系人更新成功', 'success')
            resetForm()
            loadContacts() // 重新加载联系人列表
        } else {
            showMessage('更新失败: ' + response.data.msg, 'error')
        }
    } catch (error) {
        console.error('更新联系人出错:', error)
        showMessage('更新联系人出错: ' + error.message, 'error')
    }
}

const deleteContact = async () => {
    if (!currentContact.value.contactId) {
        showMessage('请选择要删除的联系人', 'error')
        return
    }
    
    uni.showModal({
        title: '确认删除',
        content: '确定要删除这个联系人吗？',
        success: async (res) => {
            if (res.confirm) {
                try {
                    const response = await uni.request({
                        url: `http://localhost:8089/api/contact/delete/${currentContact.value.contactId}`,
                        method: 'DELETE'
                    })
                    
                    if (response.data.code === 200) {
                        showMessage('联系人删除成功', 'success')
                        resetForm()
                        loadContacts() // 重新加载联系人列表
                    } else {
                        showMessage('删除失败: ' + response.data.msg, 'error')
                    }
                } catch (error) {
                    console.error('删除联系人出错:', error)
                    showMessage('删除联系人出错: ' + error.message, 'error')
                }
            }
        }
    })
}

const selectContact = (contact) => {
    selectedContact.value = contact
    currentContact.value = { ...contact }
    isEditing.value = true
}

const searchContacts = async () => {
    if (searchKeyword.value.trim() === '') {
        loadContacts() // 如果搜索关键词为空，加载所有联系人
        return
    }
    
    try {
        const response = await uni.request({
            url: `http://localhost:8089/api/contact/search`,
            method: 'GET',
            data: {
                userId: 1,
                keyword: searchKeyword.value
            }
        })
        
        if (response.data.code === 200) {
            contacts.value = response.data.data || []
        } else {
            showMessage('搜索失败: ' + response.data.msg, 'error')
        }
    } catch (error) {
        console.error('搜索联系人出错:', error)
        showMessage('搜索联系人出错: ' + error.message, 'error')
    }
}

const resetForm = () => {
    currentContact.value = {
        contactId: null,
        userId: 1,
        contactName: '',
        phone: '',
        contactEmail: '',
        groupId: null
    }
    selectedContact.value = null
    isEditing.value = false
}

const cancelEdit = () => {
    resetForm()
}

const onGroupChange = (e) => {
    groupIndex.value = e.detail.value
    currentContact.value.groupId = groups.value[e.detail.value].groupId
}

const showMessage = (msg, type) => {
    message.value = msg
    messageType.value = type
    setTimeout(() => {
        message.value = ''
    }, 3000)
}

// 组件挂载时加载数据
onMounted(() => {
    loadContacts()
    loadGroups()
})
</script>

<style>
.container {
    height: 100vh;
    display: flex;
    flex-direction: column;
    background-color: #f5f5f5;
}

.header {
    padding: 20rpx;
    background-color: #4CAF50;
    color: white;
}

.search-box {
    margin: 20rpx;
}

.search-input {
    width: 100%;
    padding: 20rpx;
    border-radius: 10rpx;
    background-color: white;
    box-sizing: border-box;
}

.content {
    flex: 1;
    display: flex;
    flex-direction: row;
    padding: 20rpx;
}

.contact-list {
    width: 40%;
    max-width: 400rpx;
    background-color: white;
    border-radius: 10rpx;
    padding: 20rpx;
    margin-right: 20rpx;
    height: 80vh;
}

.contact-item {
    padding: 20rpx;
    border-bottom: 1rpx solid #eee;
    cursor: pointer;
}

.contact-item.active {
    background-color: #e3f2fd;
    border-left: 8rpx solid #2196F3;
}

.contact-info {
    display: flex;
    flex-direction: column;
}

.contact-name {
    font-weight: bold;
    font-size: 32rpx;
}

.contact-phone {
    font-size: 28rpx;
    color: #666;
}

.contact-form {
    flex: 1;
    background-color: white;
    border-radius: 10rpx;
    padding: 30rpx;
}

.form-group {
    margin-bottom: 30rpx;
}

.label {
    display: block;
    margin-bottom: 10rpx;
    font-weight: bold;
    color: #333;
}

.input {
    width: 100%;
    padding: 15rpx;
    border: 1rpx solid #ddd;
    border-radius: 8rpx;
    box-sizing: border-box;
}

.picker {
    width: 100%;
    padding: 15rpx;
    border: 1rpx solid #ddd;
    border-radius: 8rpx;
    box-sizing: border-box;
    background-color: #fff;
}

.button-group {
    display: flex;
    flex-wrap: wrap;
    gap: 20rpx;
    margin-top: 30rpx;
}

.btn {
    flex: 1;
    padding: 20rpx;
    border: none;
    border-radius: 8rpx;
    color: white;
    font-size: 32rpx;
    cursor: pointer;
}

.btn-add {
    background-color: #4CAF50;
}

.btn-update {
    background-color: #2196F3;
}

.btn-delete {
    background-color: #f44336;
}

.btn-cancel {
    background-color: #999;
}
</style>