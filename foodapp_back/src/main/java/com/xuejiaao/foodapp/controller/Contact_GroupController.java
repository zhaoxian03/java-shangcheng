package com.xuejiaao.foodapp.controller;

import com.xuejiaao.foodapp.entity.Contact_Group;
import com.xuejiaao.foodapp.service.Contact_GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 联系人分组管理接口
 */
@RestController
@RequestMapping("/api/contact/group")
@CrossOrigin
public class Contact_GroupController {

    @Autowired
    private Contact_GroupService contactGroupService;

    // 查询联系人分组列表
    @GetMapping("/list")
    public Map<String, Object> getContactGroupList(@RequestParam Integer userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Contact_Group> groups = contactGroupService.getContactGroupsByUserId(userId);
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", groups);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询失败：" + e.getMessage());
            result.put("data", null);
        }
        return result;
    }

    // 添加联系人分组
    @PostMapping("/add")
    public Map<String, Object> addContactGroup(@RequestBody Contact_Group contactGroup) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = contactGroupService.addContactGroup(contactGroup);
            if (success) {
                result.put("code", 200);
                result.put("msg", "添加成功");
                result.put("data", contactGroup);
            } else {
                result.put("code", 400);
                result.put("msg", "添加失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "系统错误：" + e.getMessage());
        }
        return result;
    }

    // 修改联系人分组
    @PutMapping("/update")
    public Map<String, Object> updateContactGroup(@RequestBody Contact_Group contactGroup) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = contactGroupService.updateContactGroup(contactGroup);
            if (success) {
                result.put("code", 200);
                result.put("msg", "修改成功");
                result.put("data", contactGroup);
            } else {
                result.put("code", 400);
                result.put("msg", "修改失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "系统错误：" + e.getMessage());
        }
        return result;
    }

    // 删除联系人分组
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteContactGroup(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = contactGroupService.deleteContactGroup(id);
            if (success) {
                result.put("code", 200);
                result.put("msg", "删除成功");
            } else {
                result.put("code", 400);
                result.put("msg", "删除失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "系统错误：" + e.getMessage());
        }
        return result;
    }

    // 根据ID查询联系人分组
    @GetMapping("/detail/{id}")
    public Map<String, Object> getContactGroupDetail(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Contact_Group group = contactGroupService.getContactGroupById(id);
            if (group != null) {
                result.put("code", 200);
                result.put("msg", "查询成功");
                result.put("data", group);
            } else {
                result.put("code", 404);
                result.put("msg", "分组不存在");
                result.put("data", null);
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "系统错误：" + e.getMessage());
            result.put("data", null);
        }
        return result;
    }
}