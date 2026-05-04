package com.xuejiaao.foodapp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuejiaao.foodapp.entity.Contact;
import com.xuejiaao.foodapp.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 联系人管理接口
 */
@RestController
@RequestMapping("/api/contact")
@CrossOrigin
public class ContactController {

    @Autowired
    private ContactService contactService;

    // 查询联系人列表（分页）
    @GetMapping("/list")
    public Map<String, Object> getContactList(@RequestParam Integer userId,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<Contact> contacts = contactService.getContacts(page - 1, size, userId);
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", contacts);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询失败：" + e.getMessage());
            result.put("data", null);
        }
        return result;
    }

    // 根据用户ID查询联系人列表
    @GetMapping("/listByUserId")
    public Map<String, Object> getContactListByUserId(@RequestParam Integer userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Contact> contacts = contactService.getContactsByUserId(userId);
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", contacts);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询失败：" + e.getMessage());
            result.put("data", null);
        }
        return result;
    }

    // 添加联系人
    @PostMapping("/add")
    public Map<String, Object> addContact(@RequestBody Contact contact) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = contactService.addContact(contact);
            if (success) {
                result.put("code", 200);
                result.put("msg", "添加成功");
                result.put("data", contact);
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

    // 修改联系人
    @PutMapping("/update")
    public Map<String, Object> updateContact(@RequestBody Contact contact) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = contactService.updateContact(contact);
            if (success) {
                result.put("code", 200);
                result.put("msg", "修改成功");
                result.put("data", contact);
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

    // 删除联系人
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteContact(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = contactService.deleteContact(id);
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

    // 根据ID查询联系人
    @GetMapping("/detail/{id}")
    public Map<String, Object> getContactDetail(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Contact contact = contactService.getContactById(id);
            if (contact != null) {
                result.put("code", 200);
                result.put("msg", "查询成功");
                result.put("data", contact);
            } else {
                result.put("code", 404);
                result.put("msg", "联系人不存在");
                result.put("data", null);
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "系统错误：" + e.getMessage());
            result.put("data", null);
        }
        return result;
    }

    // 搜索联系人
    @GetMapping("/search")
    public Map<String, Object> searchContacts(@RequestParam Integer userId, 
                                              @RequestParam String keyword) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Contact> contacts = contactService.searchContacts(userId, keyword);
            result.put("code", 200);
            result.put("msg", "搜索成功");
            result.put("data", contacts);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "搜索失败：" + e.getMessage());
            result.put("data", null);
        }
        return result;
    }
}