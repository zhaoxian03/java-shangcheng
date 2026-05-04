package com.xuejiaao.foodapp.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuejiaao.foodapp.entity.Contact_Group;
import com.xuejiaao.foodapp.mapper.Contact_GroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 联系人分组Service
 */
@Service
public class Contact_GroupService {
    @Autowired
    private Contact_GroupMapper contactGroupMapper;

    /**
     * 根据用户ID查询联系人分组列表
     */
    public List<Contact_Group> getContactGroupsByUserId(Integer userId) {
        QueryWrapper<Contact_Group> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return contactGroupMapper.selectList(queryWrapper);
    }

    /**
     * 添加联系人分组
     */
    public boolean addContactGroup(Contact_Group contactGroup) {
        contactGroup.setCreatedTime(new Date());
        contactGroup.setUpdatedTime(new Date());
        int result = contactGroupMapper.insert(contactGroup);
        return result > 0;
    }

    /**
     * 更新联系人分组
     */
    public boolean updateContactGroup(Contact_Group contactGroup) {
        contactGroup.setUpdatedTime(new Date());
        int result = contactGroupMapper.updateById(contactGroup);
        return result > 0;
    }

    /**
     * 根据ID删除联系人分组
     */
    public boolean deleteContactGroup(Integer id) {
        int result = contactGroupMapper.deleteById(id);
        return result > 0;
    }

    /**
     * 根据ID查询联系人分组
     */
    public Contact_Group getContactGroupById(Integer id) {
        return contactGroupMapper.selectById(id);
    }
}