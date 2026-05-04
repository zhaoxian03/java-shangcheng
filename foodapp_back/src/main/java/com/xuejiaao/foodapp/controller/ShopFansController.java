package com.xuejiaao.foodapp.controller;

import com.xuejiaao.foodapp.common.Result;
import com.xuejiaao.foodapp.service.ShopFansService;
import com.xuejiaao.foodapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺粉丝表 前端控制器
 *
 * @author xuejiaao
 * @since 2026-01-13
 */
@CrossOrigin
@RestController
@RequestMapping("/api/shop/fans")
public class ShopFansController {

    @Autowired
    private ShopFansService shopFansService;

    @Autowired
    private UserService userService;

    /**
     * 关注店铺
     *
     * @param sid 店铺ID
     * @param token 用户令牌
     * @return 操作结果
     */
    @PostMapping("/follow/{sid}")
    public Result followShop(@PathVariable Integer sid, @RequestHeader(value = "Authorization", required = false) String token) {
        // 验证token并获取用户ID
        Integer cid = userService.validateToken(token);
        if (cid == null) {
            return Result.error(401, "未授权访问");
        }

        // 执行关注操作
        boolean result = shopFansService.followShop(cid, sid);
        if (result) {
            return Result.success("关注成功", null);
        } else {
            return Result.error("关注失败");
        }
    }

    /**
     * 取消关注店铺
     *
     * @param sid 店铺ID
     * @param token 用户令牌
     * @return 操作结果
     */
    @PostMapping("/unfollow/{sid}")
    public Result unfollowShop(@PathVariable Integer sid, @RequestHeader(value = "Authorization", required = false) String token) {
        // 验证token并获取用户ID
        Integer cid = userService.validateToken(token);
        if (cid == null) {
            return Result.error(401, "未授权访问");
        }

        // 执行取消关注操作
        boolean result = shopFansService.unfollowShop(cid, sid);
        if (result) {
            return Result.success("取消关注成功", null);
        } else {
            return Result.error("取消关注失败");
        }
    }

    /**
     * 检查关注状态
     *
     * @param sid 店铺ID
     * @param token 用户令牌
     * @return 关注状态
     */
    @GetMapping("/check/{sid}")
    public Result checkFollowStatus(@PathVariable Integer sid, @RequestHeader(value = "Authorization", required = false) String token) {
        // 验证token并获取用户ID
        Integer cid = userService.validateToken(token);
        if (cid == null) {
            return Result.success(false); // 未登录用户默认未关注
        }

        // 检查关注状态
        boolean isFollowed = shopFansService.checkFollowStatus(cid, sid);
        return Result.success(isFollowed);
    }

    /**
     * 获取店铺粉丝数量
     *
     * @param sid 店铺ID
     * @return 粉丝数量
     */
    @GetMapping("/count/{sid}")
    public Result getShopFansCount(@PathVariable Integer sid) {
        // 获取粉丝数量
        Integer count = shopFansService.getShopFansCount(sid);
        return Result.success(count);
    }
    
    /**
     * 获取用户关注的店铺列表
     *
     * @param token 用户令牌
     * @return 关注的店铺列表
     */
    @GetMapping("/followed-shops")
    public Result getFollowedShops(@RequestHeader(value = "Authorization", required = false) String token) {
        // 验证token并获取用户ID
        Integer cid = userService.validateToken(token);
        if (cid == null) {
            return Result.error(401, "未授权访问");
        }
        
        // 获取用户关注的店铺列表
        java.util.List<java.util.Map<String, Object>> followedShops = shopFansService.getFollowedShops(cid);
        return Result.success(followedShops);
    }
}