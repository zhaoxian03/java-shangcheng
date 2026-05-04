package com.xuejiaao.foodapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xuejiaao.foodapp.entity.Collect;
import com.xuejiaao.foodapp.service.CollectService;
import com.xuejiaao.foodapp.utils.Result;

import lombok.RequiredArgsConstructor;

/**
 * 收藏Controller
 */
@RestController
@RequestMapping("/api/collect")
@RequiredArgsConstructor
public class CollectController {
    
    private final CollectService collectService;
    
    /**
     * 添加商品收藏
     * @param collect 收藏数据
     * @return 操作结果
     */
    @PostMapping("/addGoods")
    public Result addGoods(@RequestBody Collect collect) {
        try {
            boolean success = collectService.addGoods(collect);
            if (success) {
                return Result.success("添加收藏成功");
            } else {
                return Result.error("添加收藏失败");
            }
        } catch (Exception e) {
            return Result.error("添加收藏失败: " + e.getMessage());
        }
    }
    
    /**
     * 取消商品收藏
     * @param params 包含userId和goodsId
     * @return 操作结果
     */
    @PostMapping("/cancelGoods")
    public Result cancelGoods(@RequestBody Map<String, Object> params) {
        try {
            String userId = (String) params.get("userId");
            Integer goodsId = Integer.parseInt(params.get("goodsId").toString());
            
            boolean success = collectService.cancelGoods(userId, goodsId);
            if (success) {
                return Result.success("取消收藏成功");
            } else {
                return Result.error("取消收藏失败");
            }
        } catch (Exception e) {
            return Result.error("取消收藏失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量删除收藏
     * @param params 包含userId和goodsIds列表
     * @return 操作结果
     */
    @PostMapping("/batchDeleteGoods")
    public Result batchDeleteGoods(@RequestBody Map<String, Object> params) {
        try {
            String userId = (String) params.get("userId");
            List<Integer> goodsIds = (List<Integer>) params.get("goodsIds");
            
            boolean success = collectService.batchDeleteGoods(userId, goodsIds);
            if (success) {
                return Result.success("批量删除成功");
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取商品收藏列表
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return 收藏列表
     */
    @GetMapping("/getGoodsList")
    public Result getGoodsList(@RequestParam String userId, 
                             @RequestParam(defaultValue = "1") Integer page, 
                             @RequestParam(defaultValue = "20") Integer pageSize) {
        try {
            Map<String, Object> result = collectService.getGoodsList(userId, page, pageSize);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("获取收藏列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查商品是否已收藏
     * @param userId 用户ID
     * @param goodsId 商品ID
     * @return 收藏状态
     */
    @GetMapping("/checkGoods")
    public Result checkGoods(@RequestParam String userId, @RequestParam Integer goodsId) {
        try {
            boolean isCollected = collectService.checkGoods(userId, goodsId);
            return Result.success(isCollected);
        } catch (Exception e) {
            return Result.error("检查收藏状态失败: " + e.getMessage());
        }
    }
}
