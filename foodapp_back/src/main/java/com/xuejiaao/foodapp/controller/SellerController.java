package com.xuejiaao.foodapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuejiaao.foodapp.entity.Goods;
import com.xuejiaao.foodapp.entity.GoodsDetail;
import com.xuejiaao.foodapp.entity.Orders;
import com.xuejiaao.foodapp.entity.SellerUser;
import com.xuejiaao.foodapp.entity.Shops;
import com.xuejiaao.foodapp.entity.Evaluate;
import com.xuejiaao.foodapp.entity.vo.MyResult;
import com.xuejiaao.foodapp.mapper.GoodsMapper;
import com.xuejiaao.foodapp.mapper.SellerUserMapper;
import com.xuejiaao.foodapp.mapper.ShopsMapper;
import com.xuejiaao.foodapp.service.GoodsDetailService;
import com.xuejiaao.foodapp.service.OrdersService;
import com.xuejiaao.foodapp.service.EvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/seller")
public class SellerController {
    @Autowired
    private SellerUserMapper sellerUserMapper;
    
    @Autowired
    private ShopsMapper shopsMapper;
    
    @Autowired
    private GoodsMapper goodsMapper;
    
    @Autowired
    private GoodsDetailService goodsDetailService;
    
    @Autowired
    private OrdersService ordersService;
    
    @Autowired
    private EvaluateService evaluateService;
    
    /**
     * 商家登录
     * @param loginData 登录数据
     * @return 登录结果
     */
    @PostMapping("/login")
    public MyResult sellerLogin(@RequestBody Map<String, String> loginData) {
        MyResult myResult = new MyResult();
        
        try {
            String username = loginData.get("username");
            String password = loginData.get("password");
            
            // 根据用户名和密码查询商家用户
            QueryWrapper<SellerUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username)
                       .eq("userpwd", password)
                       .eq("role", 1); // 1表示商家角色
            
            // 使用selectList查询，然后取第一个结果
            List<SellerUser> sellerUserList = sellerUserMapper.selectList(queryWrapper);
            SellerUser sellerUser = sellerUserList != null && sellerUserList.size() > 0 ? sellerUserList.get(0) : null;
            
            if (sellerUser != null) {
                // 查询商家对应的店铺信息
                Shops shop = shopsMapper.selectOne(new QueryWrapper<Shops>().eq("sid", sellerUser.getSid()));
                
                // 构建登录成功返回数据
                Map<String, Object> userData = new HashMap<>();
                userData.put("uid", sellerUser.getUserid());
                userData.put("username", sellerUser.getUsername());
                userData.put("nickName", sellerUser.getShopName());
                userData.put("shopInfo", shop);
                userData.put("token", "dummy-token-" + System.currentTimeMillis()); // 实际项目中应该生成真实的JWT token
                
                myResult.setCode(200);
                myResult.setMsg("登录成功");
                myResult.setData(userData);
            } else {
                myResult.setCode(401);
                myResult.setMsg("用户名或密码错误");
                myResult.setData(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("登录失败，请稍后重试");
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 商家注册
     * @param registerData 注册数据
     * @return 注册结果
     */
    @PostMapping("/register")
    public MyResult sellerRegister(@RequestBody Map<String, String> registerData) {
        MyResult myResult = new MyResult();
        
        try {
            String username = registerData.get("username");
            String password = registerData.get("password");
            String shopName = registerData.get("shopName");
            
            // 检查用户名是否已存在
            QueryWrapper<SellerUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            
            if (sellerUserMapper.selectOne(queryWrapper) != null) {
                myResult.setCode(400);
                myResult.setMsg("用户名已存在");
                myResult.setData(null);
                return myResult;
            }
            
            // 创建店铺
            Shops shop = new Shops();
            shop.setSname(shopName);
            shop.setSstatus(1); // 默认正常状态
            shopsMapper.insert(shop);
            
            // 创建商家用户
            SellerUser sellerUser = new SellerUser();
            sellerUser.setUsername(username);
            sellerUser.setUserpwd(password);
            sellerUser.setShopName(shopName); // 使用shopName字段而不是nickName
            sellerUser.setRole(1); // 1表示商家角色
            sellerUser.setSid(shop.getSid()); // 关联店铺ID
            sellerUser.setUstatus(1); // 默认正常状态
            sellerUser.setLastLoginTime(new Date());
            sellerUserMapper.insert(sellerUser);
            
            myResult.setCode(200);
            myResult.setMsg("注册成功");
            myResult.setData(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("注册失败，请稍后重试");
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 获取商品列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param keyword 搜索关键词
     * @return 商品列表
     */
    @GetMapping("/goods/list")
    public MyResult getGoodsList(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false) String keyword,
                                 @RequestParam Integer sid) {
        MyResult myResult = new MyResult();
        
        try {
            Page<Goods> goodsPage = new Page<>(page, pageSize);
            QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
            
            if (keyword != null && !keyword.isEmpty()) {
                queryWrapper.like("gname", keyword);
            }
            
            // 根据当前登录商家的店铺ID过滤商品
            queryWrapper.eq("sid", sid);
            
            goodsMapper.selectPage(goodsPage, queryWrapper);
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", goodsPage.getRecords());
            data.put("total", goodsPage.getTotal());
            data.put("totalPages", goodsPage.getPages());
            data.put("currentPage", page);
            data.put("pageSize", pageSize);
            
            myResult.setCode(200);
            myResult.setMsg("获取商品列表成功");
            myResult.setData(data);
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("获取商品列表失败，请稍后重试");
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 切换商品状态
     * @param statusData 状态数据
     * @return 操作结果
     */
    @PutMapping("/goods/status")
    public MyResult toggleGoodsStatus(@RequestBody Map<String, Object> statusData) {
        MyResult myResult = new MyResult();
        
        try {
            Long gid = Long.valueOf(statusData.get("gid").toString());
            Integer gstatus = Integer.valueOf(statusData.get("gstatus").toString());
            Integer sid = Integer.valueOf(statusData.get("sid").toString());
            
            // 验证商品是否属于当前店铺
            Goods goods = goodsMapper.selectById(gid);
            if (goods == null || !goods.getSid().equals(sid)) {
                myResult.setCode(403);
                myResult.setMsg("无权操作该商品");
                myResult.setData(null);
                return myResult;
            }
            
            goods.setGstatus(gstatus);
            int rows = goodsMapper.updateById(goods);
            
            if (rows > 0) {
                myResult.setCode(200);
                myResult.setMsg("商品状态更新成功");
                myResult.setData(null);
            } else {
                myResult.setCode(400);
                myResult.setMsg("商品状态更新失败");
                myResult.setData(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("商品状态更新失败，请稍后重试");
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 删除商品
     * @param gid 商品ID
     * @param sid 店铺ID
     * @return 操作结果
     */
    @DeleteMapping("/goods/{gid}")
    public MyResult deleteGoods(@PathVariable Long gid, @RequestParam Integer sid) {
        MyResult myResult = new MyResult();
        
        try {
            
            // 验证商品是否属于当前店铺
            Goods goods = goodsMapper.selectById(gid);
            if (goods == null || !goods.getSid().equals(sid)) {
                myResult.setCode(403);
                myResult.setMsg("无权操作该商品");
                myResult.setData(null);
                return myResult;
            }
            
            int rows = goodsMapper.deleteById(gid);
            
            if (rows > 0) {
                myResult.setCode(200);
                myResult.setMsg("商品删除成功");
                myResult.setData(null);
            } else {
                myResult.setCode(400);
                myResult.setMsg("商品删除失败");
                myResult.setData(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("商品删除失败，请稍后重试");
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 新增商品
     * @param goods 商品信息
     * @return 操作结果
     */
    @PostMapping("/goods/add")
    public MyResult addGoods(@RequestBody Goods goods) {
        MyResult myResult = new MyResult();
        
        try {
            // 确保商品属于当前店铺，sid从请求中获取或从登录信息中获取
            // 注意：实际项目中应该从JWT token中获取当前登录用户的店铺ID
            if (goods.getSid() == null) {
                myResult.setCode(400);
                myResult.setMsg("店铺ID不能为空");
                myResult.setData(null);
                return myResult;
            }
            
            goodsMapper.insert(goods);
            
            myResult.setCode(200);
            myResult.setMsg("商品新增成功");
            myResult.setData(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("商品新增失败，请稍后重试");
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 编辑商品
     * @param gid 商品ID
     * @param goods 商品信息
     * @return 操作结果
     */
    @PutMapping("/goods/{gid}")
    public MyResult editGoods(@PathVariable Long gid, @RequestBody Goods goods) {
        MyResult myResult = new MyResult();
        
        try {
            goods.setGid(gid);
            
            // 验证商品是否属于当前店铺
            Goods existingGoods = goodsMapper.selectById(gid);
            if (existingGoods == null || !existingGoods.getSid().equals(goods.getSid())) {
                myResult.setCode(403);
                myResult.setMsg("无权操作该商品");
                myResult.setData(null);
                return myResult;
            }
            
            // 确保编辑后商品仍然属于原店铺，不允许修改店铺ID
            goods.setSid(existingGoods.getSid());
            
            int rows = goodsMapper.updateById(goods);
            
            if (rows > 0) {
                myResult.setCode(200);
                myResult.setMsg("商品编辑成功");
                myResult.setData(null);
            } else {
                myResult.setCode(400);
                myResult.setMsg("商品编辑失败");
                myResult.setData(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("商品编辑失败，请稍后重试");
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 获取商品详情
     * @param gid 商品ID
     * @return 商品详情
     */
    @GetMapping("/goods/{gid}")
    public MyResult getGoodsDetail(@PathVariable Long gid, @RequestParam Integer sid) {
        MyResult myResult = new MyResult();
        
        try {
            Goods goods = goodsMapper.selectById(gid);
            
            if (goods != null) {
                // 验证商品是否属于当前店铺
                if (!goods.getSid().equals(sid)) {
                    myResult.setCode(403);
                    myResult.setMsg("无权查看该商品");
                    myResult.setData(null);
                    return myResult;
                }
                
                myResult.setCode(200);
                myResult.setMsg("获取商品详情成功");
                myResult.setData(goods);
            } else {
                myResult.setCode(404);
                myResult.setMsg("商品不存在");
                myResult.setData(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("获取商品详情失败，请稍后重试");
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 获取数据概览
     * @return 数据概览
     */
    @GetMapping("/stats")
    public MyResult getStats(@RequestParam Integer sid) {
        MyResult myResult = new MyResult();
        
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // 根据当前登录商家的店铺ID过滤数据
            QueryWrapper<Goods> shopQueryWrapper = new QueryWrapper<>();
            shopQueryWrapper.eq("sid", sid);
            
            QueryWrapper<Goods> allQueryWrapper = new QueryWrapper<>();
            allQueryWrapper.eq("sid", sid);
            long goodsCount = goodsMapper.selectCount(allQueryWrapper);
            
            QueryWrapper<Goods> onlineQueryWrapper = new QueryWrapper<>();
            onlineQueryWrapper.eq("sid", sid)
                            .eq("gstatus", 1);
            long onlineCount = goodsMapper.selectCount(onlineQueryWrapper);
            
            // 查询今日订单数量
            Long orderCount = ordersService.getTodayOrdersCount(sid);
            
            stats.put("goodsCount", goodsCount);
            stats.put("onlineCount", onlineCount);
            stats.put("orderCount", orderCount);
            
            myResult.setCode(200);
            myResult.setMsg("获取数据概览成功");
            myResult.setData(stats);
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("获取数据概览失败，请稍后重试");
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 获取店铺信息
     * @param sid 店铺ID
     * @return 店铺信息
     */
    @GetMapping("/shop/{sid}")
    public MyResult getShopInfo(@PathVariable Integer sid) {
        MyResult myResult = new MyResult();
        
        try {
            // 查询店铺信息
            Shops shop = shopsMapper.selectById(sid);
            
            if (shop == null) {
                myResult.setCode(404);
                myResult.setMsg("店铺不存在");
                myResult.setData(null);
                return myResult;
            }
            
            myResult.setCode(200);
            myResult.setMsg("获取店铺信息成功");
            myResult.setData(shop);
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("获取店铺信息失败，请稍后重试");
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 更新店铺信息
     * @param shop 店铺信息
     * @return 更新结果
     */
    @PutMapping("/shop/update")
    public MyResult updateShopInfo(@RequestBody Shops shop) {
        MyResult myResult = new MyResult();
        
        try {
            // 验证店铺是否存在
            Shops existingShop = shopsMapper.selectById(shop.getSid());
            if (existingShop == null) {
                myResult.setCode(404);
                myResult.setMsg("店铺不存在");
                myResult.setData(null);
                return myResult;
            }
            
            // 更新店铺信息
            int rows = shopsMapper.updateById(shop);
            
            if (rows > 0) {
                myResult.setCode(200);
                myResult.setMsg("店铺信息更新成功");
                myResult.setData(shop);
            } else {
                myResult.setCode(400);
                myResult.setMsg("店铺信息更新失败");
                myResult.setData(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("更新店铺信息失败，请稍后重试");
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 获取商品详情列表
     * @param gid 商品ID
     * @return 商品详情列表
     */
    @GetMapping("/goods/detail/list")
    public MyResult getGoodsDetailList(@RequestParam Long gid) {
        MyResult myResult = new MyResult();
        
        try {
            List<GoodsDetail> goodsDetailList = goodsDetailService.findByGid(gid);
            myResult.setCode(200);
            myResult.setMsg("获取商品详情列表成功");
            myResult.setData(goodsDetailList);
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("获取商品详情列表失败，请稍后重试");
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 添加商品详情
     * @param goodsDetail 商品详情信息
     * @return 操作结果
     */
    @PostMapping("/goods/detail/add")
    public MyResult addGoodsDetail(@RequestBody GoodsDetail goodsDetail) {
        MyResult myResult = new MyResult();
        
        try {
            // 验证商品是否存在
            Goods goods = goodsMapper.selectById(goodsDetail.getGid());
            if (goods == null) {
                myResult.setCode(404);
                myResult.setMsg("商品不存在");
                myResult.setData(null);
                return myResult;
            }
            
            boolean success = goodsDetailService.addGoodsDetail(goodsDetail);
            if (success) {
                myResult.setCode(200);
                myResult.setMsg("商品详情添加成功");
                myResult.setData(null);
            } else {
                myResult.setCode(400);
                myResult.setMsg("商品详情添加失败");
                myResult.setData(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("商品详情添加失败，请稍后重试");
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 更新商品详情
     * @param goodsDetail 商品详情信息
     * @return 操作结果
     */
    @PutMapping("/goods/detail/update")
    public MyResult updateGoodsDetail(@RequestBody GoodsDetail goodsDetail) {
        MyResult myResult = new MyResult();
        
        try {
            // 验证商品详情是否存在
            GoodsDetail existingDetail = goodsDetailService.findByGdid(goodsDetail.getGdid());
            if (existingDetail == null) {
                myResult.setCode(404);
                myResult.setMsg("商品详情不存在");
                myResult.setData(null);
                return myResult;
            }
            
            boolean success = goodsDetailService.updateGoodsDetail(goodsDetail);
            if (success) {
                myResult.setCode(200);
                myResult.setMsg("商品详情更新成功");
                myResult.setData(null);
            } else {
                myResult.setCode(400);
                myResult.setMsg("商品详情更新失败");
                myResult.setData(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("商品详情更新失败，请稍后重试");
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 删除商品详情
     * @param gdid 商品详情ID
     * @return 操作结果
     */
    @DeleteMapping("/goods/detail/{gdid}")
    public MyResult deleteGoodsDetail(@PathVariable Long gdid) {
        MyResult myResult = new MyResult();
        
        try {
            // 验证商品详情是否存在
            GoodsDetail existingDetail = goodsDetailService.findByGdid(gdid);
            if (existingDetail == null) {
                myResult.setCode(404);
                myResult.setMsg("商品详情不存在");
                myResult.setData(null);
                return myResult;
            }
            
            boolean success = goodsDetailService.deleteGoodsDetail(gdid);
            if (success) {
                myResult.setCode(200);
                myResult.setMsg("商品详情删除成功");
                myResult.setData(null);
            } else {
                myResult.setCode(400);
                myResult.setMsg("商品详情删除失败");
                myResult.setData(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("商品详情删除失败，请稍后重试");
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 获取商家订单列表
     * @param sid 店铺ID
     * @param pageIndex 页码
     * @return 订单列表
     */
    @GetMapping("/orders/list")
    public MyResult getOrdersList(@RequestParam Integer sid, @RequestParam(required = false, defaultValue = "1") Integer pageIndex) {
        MyResult myResult = new MyResult();
        
        try {
            // 验证店铺ID
            if (sid == null) {
                myResult.setCode(400);
                myResult.setMsg("店铺ID不能为空");
                myResult.setData(null);
                return myResult;
            }
            
            // 查询订单列表
            List<Orders> ordersList = ordersService.getOrdersListBySid(sid, pageIndex);
            myResult.setCode(200);
            myResult.setMsg("获取订单列表成功");
            myResult.setData(ordersList);
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("获取订单列表失败: " + ex.getMessage());
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 获取订单详情
     * @param sid 店铺ID
     * @param orderid 订单ID
     * @return 订单详情
     */
    @GetMapping("/orders/{orderid}")
    public MyResult getOrderDetail(@RequestParam Integer sid, @PathVariable Long orderid) {
        MyResult myResult = new MyResult();
        
        try {
            // 验证参数
            if (sid == null || orderid == null) {
                myResult.setCode(400);
                myResult.setMsg("参数不能为空");
                myResult.setData(null);
                return myResult;
            }
            
            // 查询订单详情
            Orders order = ordersService.getOrderBySidAndOrderid(sid, orderid);
            if (order == null) {
                myResult.setCode(404);
                myResult.setMsg("订单不存在或不属于当前店铺");
                myResult.setData(null);
                return myResult;
            }
            
            myResult.setCode(200);
            myResult.setMsg("获取订单详情成功");
            myResult.setData(order);
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("获取订单详情失败: " + ex.getMessage());
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 搜索订单
     * @param sid 店铺ID
     * @param keyword 搜索关键字
     * @param pageIndex 页码
     * @return 搜索结果
     */
    @GetMapping("/orders/search")
    public MyResult searchOrders(@RequestParam Integer sid, @RequestParam String keyword, @RequestParam(required = false, defaultValue = "1") Integer pageIndex) {
        MyResult myResult = new MyResult();
        
        try {
            // 验证参数
            if (sid == null || keyword == null || keyword.trim().isEmpty()) {
                myResult.setCode(400);
                myResult.setMsg("参数不能为空");
                myResult.setData(null);
                return myResult;
            }
            
            // 搜索订单
            List<Orders> ordersList = ordersService.searchOrdersBySid(sid, keyword, pageIndex);
            myResult.setCode(200);
            myResult.setMsg("搜索订单成功");
            myResult.setData(ordersList);
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("搜索订单失败: " + ex.getMessage());
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 更新订单状态
     * @param updateParams 更新参数
     * @return 更新结果
     */
    @PutMapping("/orders/status")
    public MyResult updateOrderStatus(@RequestBody Map<String, Object> updateParams) {
        MyResult myResult = new MyResult();
        
        try {
            // 获取参数
            Integer sid = (Integer) updateParams.get("sid");
            Long orderid = Long.parseLong(updateParams.get("orderid").toString());
            Integer status = (Integer) updateParams.get("status");
            
            // 验证参数
            if (sid == null || orderid == null || status == null) {
                myResult.setCode(400);
                myResult.setMsg("参数不能为空");
                myResult.setData(null);
                return myResult;
            }
            
            // 验证订单是否属于当前店铺
            Orders order = ordersService.getOrderBySidAndOrderid(sid, orderid);
            if (order == null) {
                myResult.setCode(404);
                myResult.setMsg("订单不存在或不属于当前店铺");
                myResult.setData(null);
                return myResult;
            }
            
            // 更新订单状态
            order.setOrderstatus(status); // 更新数据库字段orderstatus
            order.setStatus(status); // 同时更新前端使用的status字段
            
            // 同步更新订单状态文字描述
            String statusStr;
            switch (status) {
                case 0: statusStr = "待支付"; break;
                case 1: statusStr = "已支付"; break;
                case 2: statusStr = "已完成"; break;
                case 3: statusStr = "已取消"; break;
                case 4: statusStr = "退单"; break;
                default: statusStr = "未知状态"; break;
            }
            order.setOrderstatusStr(statusStr);
            
            ordersService.updateOrders(order);
            
            myResult.setCode(200);
            myResult.setMsg("订单状态更新成功");
            myResult.setData(order);
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("更新订单状态失败: " + ex.getMessage());
            myResult.setData(null);
        }
        
        return myResult;
    }
    
    /**
     * 获取订单评价
     * @param sid 店铺ID
     * @param orderid 订单ID
     * @return 评价列表
     */
    @GetMapping("/orders/{orderid}/evaluates")
    public MyResult getOrderEvaluates(@RequestParam Integer sid, @PathVariable Long orderid) {
        MyResult myResult = new MyResult();
        
        try {
            // 验证参数
            if (sid == null || orderid == null) {
                myResult.setCode(400);
                myResult.setMsg("参数不能为空");
                myResult.setData(null);
                return myResult;
            }
            
            // 验证订单是否属于当前店铺
            Orders order = ordersService.getOrderBySidAndOrderid(sid, orderid);
            if (order == null) {
                myResult.setCode(404);
                myResult.setMsg("订单不存在或不属于当前店铺");
                myResult.setData(null);
                return myResult;
            }
            
            // 查询订单评价
            List<Evaluate> evaluates = evaluateService.getEvaluatesByOrderId(orderid);
            
            myResult.setCode(200);
            myResult.setMsg("获取订单评价成功");
            myResult.setData(evaluates);
        } catch (Exception ex) {
            ex.printStackTrace();
            myResult.setCode(500);
            myResult.setMsg("获取订单评价失败: " + ex.getMessage());
            myResult.setData(null);
        }
        
        return myResult;
    }
}