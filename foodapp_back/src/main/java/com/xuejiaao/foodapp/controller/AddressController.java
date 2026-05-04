package com.xuejiaao.foodapp.controller;

//五一快乐

import com.xuejiaao.foodapp.entity.Address;
import com.xuejiaao.foodapp.service.AddressService;
import com.xuejiaao.foodapp.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/address")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * 获取地址列表
     */
    @GetMapping("/list")
    public Result getAddressList(@RequestParam Long userId) {
        try {
            List<Address> addresses = addressService.getAddressList(userId);
            return Result.success(addresses);
        } catch (Exception e) {
            return Result.error("获取地址列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取地址详情
     */
    @GetMapping("/detail")
    public Result getAddressDetail(@RequestParam Long id, @RequestParam Long userId) {
        try {
            Address address = addressService.getAddressDetail(id, userId);
            if (address == null) {
                return Result.error("地址不存在");
            }
            return Result.success(address);
        } catch (Exception e) {
            return Result.error("获取地址详情失败: " + e.getMessage());
        }
    }

    /**
     * 获取默认地址
     */
    @GetMapping("/default")
    public Result getDefaultAddress(@RequestParam Long userId) {
        try {
            Address address = addressService.getDefaultAddress(userId);
            return Result.success(address);
        } catch (Exception e) {
            return Result.error("获取默认地址失败: " + e.getMessage());
        }
    }

    /**
     * 添加地址
     */
    @PostMapping("/add")
    public Result addAddress(@RequestBody Map<String, Object> params) {
        try {
            Long userId = Long.parseLong(params.get("userId").toString());

            // 必填字段验证
            if (!params.containsKey("name") || !params.containsKey("phone") ||
                    !params.containsKey("province") || !params.containsKey("city") ||
                    !params.containsKey("district") || !params.containsKey("detail")) {
                return Result.error("缺少必要参数");
            }

            // 手机号格式验证
            String phone = params.get("phone").toString();
            if (!phone.matches("^1[3-9]\\d{9}$")) {
                return Result.error("手机号格式不正确");
            }

            Long addressId = addressService.addAddress(params, userId);
            Map<String, Object> result = new HashMap<>();
            result.put("addressId", addressId);
            return Result.success("添加地址成功", result);
        } catch (Exception e) {
            return Result.error("添加地址失败: " + e.getMessage());
        }
    }

    /**
     * 更新地址
     */
    @PostMapping("/update")
    public Result updateAddress(@RequestBody Map<String, Object> params) {
        try {
            Long userId = Long.parseLong(params.get("userId").toString());

            if (!params.containsKey("id")) {
                return Result.error("地址ID不能为空");
            }

            boolean success = addressService.updateAddress(params, userId);
            if (success) {
                return Result.success("更新地址成功");
            } else {
                return Result.error("地址不存在或无权修改");
            }
        } catch (Exception e) {
            return Result.error("更新地址失败: " + e.getMessage());
        }
    }

    /**
     * 删除地址
     */
    @PostMapping("/delete")
    public Result deleteAddress(@RequestBody Map<String, Object> params) {
        try {
            Long id = Long.parseLong(params.get("id").toString());
            Long userId = Long.parseLong(params.get("userId").toString());

            boolean success = addressService.deleteAddress(id, userId);
            if (success) {
                return Result.success("删除地址成功");
            } else {
                return Result.error("地址不存在或无权删除");
            }
        } catch (Exception e) {
            return Result.error("删除地址失败: " + e.getMessage());
        }
    }

    /**
     * 设置默认地址
     */
    @PostMapping("/set-default")
    public Result setDefaultAddress(@RequestBody Map<String, Object> params) {
        try {
            Long id = Long.parseLong(params.get("id").toString());
            Long userId = Long.parseLong(params.get("userId").toString());

            boolean success = addressService.setDefaultAddress(id, userId);
            if (success) {
                return Result.success("设置默认地址成功");
            } else {
                return Result.error("地址不存在");
            }
        } catch (Exception e) {
            return Result.error("设置默认地址失败: " + e.getMessage());
        }
    }
}