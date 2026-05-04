package com.xuejiaao.foodapp.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuejiaao.foodapp.entity.Contact;
import com.xuejiaao.foodapp.mapper.ContactMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 联系人Service（基于MyBatis-Plus）
 */
@Service
public class ContactService {
    @Autowired
    private ContactMapper contactMapper;

    /**
     * 分页查询联系人列表
     */
    public Page<Contact> getContacts(int page, int size, Integer userId) {
        Page<Contact> contactPage = new Page<>(page, size);
        QueryWrapper<Contact> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("status", true) // 只查询未删除的联系人
                .orderByDesc("created_time"); // 按创建时间倒序排列
        return contactMapper.selectPage(contactPage, queryWrapper);
    }

    /**
     * 根据用户ID查询联系人列表
     */
    public List<Contact> getContactsByUserId(Integer userId) {
        QueryWrapper<Contact> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("status", true); // 只查询未删除的联系人
        return contactMapper.selectList(queryWrapper);
    }

    /**
     * 添加联系人
     */
    public boolean addContact(Contact contact) {
        contact.setCreatedTime(new Date());
        contact.setUpdatedTime(new Date());
        contact.setStatus(true); // 默认状态为正常
        int result = contactMapper.insert(contact);
        return result > 0;
    }

    /**
     * 更新联系人
     */
    public boolean updateContact(Contact contact) {
        contact.setUpdatedTime(new Date());
        int result = contactMapper.updateById(contact);
        return result > 0;
    }

    /**
     * 根据ID删除联系人（软删除）
     */
    public boolean deleteContact(Integer id) {
        Contact contact = new Contact();
        contact.setContactId(id);
        contact.setStatus(false); // 设置为删除状态
        contact.setUpdatedTime(new Date());
        int result = contactMapper.updateById(contact);
        return result > 0;
    }

    /**
     * 根据ID查询联系人
     */
    public Contact getContactById(Integer id) {
        return contactMapper.selectById(id);
    }

    /**
     * 根据用户ID和关键词搜索联系人
     */
    public List<Contact> searchContacts(Integer userId, String keyword) {
        QueryWrapper<Contact> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("status", true) // 只查询未删除的联系人
                .and(wrapper -> 
                    wrapper.like("contact_name", keyword)
                        .or()
                        .like("phone", keyword)
                        .or()
                        .like("remark", keyword)
                );
        return contactMapper.selectList(queryWrapper);
    }
}