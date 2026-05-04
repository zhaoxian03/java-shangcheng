/*
 Navicat Premium Data Transfer

 Source Server         : dashuju
 Source Server Type    : MySQL
 Source Server Version : 80100
 Source Host           : localhost:3306
 Source Schema         : douyinshops

 Target Server Type    : MySQL
 Target Server Version : 80100
 File Encoding         : 65001

 Date: 14/01/2026 15:33:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `cartid` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '购物车id',
  `cid` bigint(0) DEFAULT NULL COMMENT '客户id',
  `sid` int(0) DEFAULT NULL COMMENT '店铺id',
  `buyTotalNums` int(0) DEFAULT NULL COMMENT '购买总数量',
  `buyTotalPrice` decimal(16, 2) DEFAULT NULL COMMENT '购买总价格',
  `beizhu` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`cartid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 252 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '购物车' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (217, 2, 2, 4, 1029.00, '');
INSERT INTO `cart` VALUES (218, 2, 1, 1, 0.00, '');
INSERT INTO `cart` VALUES (223, 5, 2, 4, 1192.00, '');

-- ----------------------------
-- Table structure for cart_detail
-- ----------------------------
DROP TABLE IF EXISTS `cart_detail`;
CREATE TABLE `cart_detail`  (
  `cdid` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '购物车详细id',
  `gid` bigint(0) DEFAULT NULL COMMENT '商品id',
  `gdid` bigint(0) DEFAULT NULL COMMENT '商品详细id',
  `cartid` bigint(0) DEFAULT NULL COMMENT '购物车id',
  `gname` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品名称',
  `icon` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品图片',
  `bcontent` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品内容',
  `buyPrice` decimal(16, 2) DEFAULT NULL COMMENT '商品价格',
  `buyNums` int(0) DEFAULT NULL COMMENT '商品数量',
  PRIMARY KEY (`cdid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 496 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '购物车详细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart_detail
-- ----------------------------
INSERT INTO `cart_detail` VALUES (218, 1, 1, 110, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', '/static/images/goods/780.jpg', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 3);
INSERT INTO `cart_detail` VALUES (219, 6, 20, 111, '大豆纤维冬被被子保暖儿童丝棉被秋冬学生宿舍被芯春秋被四季通用', '/static/images/goods/780.jpg', '180*220cm【4斤 春秋被】不结团 不跑棉/【绅士灰】抑菌大豆被', 50.80, 2);
INSERT INTO `cart_detail` VALUES (392, 2, 4, 184, '碧根果新货500g250g新鲜坚果零食奶油味奶香浓郁长寿干果年货5斤', '/static/images/goods/780.jpg', '250g奶香味', 18.00, 1);
INSERT INTO `cart_detail` VALUES (393, 1, 1, 184, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', '/static/images/goods/780.jpg', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 6);
INSERT INTO `cart_detail` VALUES (394, 2, 5, 184, '碧根果新货500g250g新鲜坚果零食奶油味奶香浓郁长寿干果年货5斤', '/static/images/goods/780.jpg', '500g奶香味', 34.80, 1);
INSERT INTO `cart_detail` VALUES (395, 6, 20, 185, '大豆纤维冬被被子保暖儿童丝棉被秋冬学生宿舍被芯春秋被四季通用', '/static/images/goods/780.jpg', '180*220cm【4斤 春秋被】不结团 不跑棉/【绅士灰】抑菌大豆被', 50.80, 2);
INSERT INTO `cart_detail` VALUES (396, 8, 26, 186, '乐乐逗逗童装针织儿童开衫中大童2022秋装新款休闲百搭男童女童毛衣潮', '/static/images/goods/780.jpg', '藏青色/120cm', 99.00, 1);
INSERT INTO `cart_detail` VALUES (397, 6, 21, 187, '大豆纤维冬被被子保暖儿童丝棉被秋冬学生宿舍被芯春秋被四季通用', '/static/images/goods/780.jpg', '180*220cm【4斤 春秋被】不结团 不跑棉/【香妃玉】抑菌大豆被', 50.80, 1);
INSERT INTO `cart_detail` VALUES (398, 1, 1, 188, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', '/static/images/goods/780.jpg', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 3);
INSERT INTO `cart_detail` VALUES (399, 7, 22, 189, '白衬衫女长袖职业春秋夏季短袖宽松工作服正装大码工装女装白衬衣', '/static/images/goods/780.jpg', 'F11【短袖】纯白翻领/XL', 16.90, 3);
INSERT INTO `cart_detail` VALUES (400, 1, 1, 190, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', '/static/images/goods/780.jpg', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 3);
INSERT INTO `cart_detail` VALUES (401, 7, 22, 191, '白衬衫女长袖职业春秋夏季短袖宽松工作服正装大码工装女装白衬衣', '/static/images/goods/780.jpg', 'F11【短袖】纯白翻领/XL', 16.90, 3);
INSERT INTO `cart_detail` VALUES (402, 7, 23, 192, '白衬衫女长袖职业春秋夏季短袖宽松工作服正装大码工装女装白衬衣', '/static/images/goods/780.jpg', '【长袖】纯白V领/2XL', 18.90, 4);
INSERT INTO `cart_detail` VALUES (447, 4, 10, 217, '高档纯棉四件套轻奢高级感100全棉南通家纺床单被套床上用品床笠', '/static/images/goods/780.jpg', '1.2m床单款三件套(被套1.5X2m)/莫兰迪高贵紫', 298.00, 3);
INSERT INTO `cart_detail` VALUES (448, 5, 15, 217, '简约纯棉四件套全棉100家纺床上用品床单被套1.8米被罩宿舍三件套', '/static/images/goods/780.jpg', '1.5米床【被套180*220】四件套/快乐空间', 135.00, 1);
INSERT INTO `cart_detail` VALUES (449, 11, NULL, 218, '拖鞋女夏季室内居家用浴室洗澡防滑宾馆酒店EVA防臭男士拖鞋软底', '/static/images/goods/780.jpg', NULL, NULL, 1);
INSERT INTO `cart_detail` VALUES (458, 4, 10, 223, '高档纯棉四件套轻奢高级感100全棉南通家纺床单被套床上用品床笠', '/static/images/goods/780.jpg', '1.2m床单款三件套(被套1.5X2m)/莫兰迪高贵紫', 298.00, 4);
INSERT INTO `cart_detail` VALUES (462, 11, 36, 226, '拖鞋女夏季室内居家用浴室洗澡防滑宾馆酒店EVA防臭男士拖鞋软底', '/static/images/goods/1.jpg', '42码/26cm', 9.80, 3);
INSERT INTO `cart_detail` VALUES (468, 17, 40, 231, '牛奶', '', '60瓶/', 49.90, 3);
INSERT INTO `cart_detail` VALUES (469, 16, 38, 231, '凉鞋', '', '44码/26cm', 20.00, 3);

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `cgid` int(0) NOT NULL AUTO_INCREMENT COMMENT '类别id',
  `cgname` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '类别名称',
  `ctag` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '副标题',
  `cgstatus` int(0) DEFAULT NULL COMMENT '类别状态: 1正常 2.下线',
  `position` int(0) DEFAULT NULL COMMENT '排序位置',
  PRIMARY KEY (`cgid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '商品类别表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '食品饮料', NULL, NULL, 1);
INSERT INTO `category` VALUES (2, '家纺', NULL, NULL, 2);
INSERT INTO `category` VALUES (3, '童装', NULL, NULL, 3);
INSERT INTO `category` VALUES (4, '生鲜', NULL, NULL, 4);
INSERT INTO `category` VALUES (5, '百货', NULL, NULL, 5);
INSERT INTO `category` VALUES (6, '图书', NULL, NULL, 6);
INSERT INTO `category` VALUES (7, '数码办公', NULL, NULL, 7);
INSERT INTO `category` VALUES (8, '洗护', NULL, NULL, 8);
INSERT INTO `category` VALUES (9, '男装', NULL, NULL, 9);
INSERT INTO `category` VALUES (10, '运动', NULL, NULL, 10);
INSERT INTO `category` VALUES (11, '配饰', NULL, NULL, 11);
INSERT INTO `category` VALUES (12, '鞋靴', NULL, NULL, 12);
INSERT INTO `category` VALUES (13, '家装', NULL, NULL, 13);
INSERT INTO `category` VALUES (14, '珠宝文玩', NULL, NULL, 14);
INSERT INTO `category` VALUES (15, '女装', NULL, NULL, 15);
INSERT INTO `category` VALUES (16, '母婴', NULL, NULL, 16);
INSERT INTO `category` VALUES (17, '护肤', NULL, NULL, 17);
INSERT INTO `category` VALUES (18, '车品', NULL, NULL, 18);
INSERT INTO `category` VALUES (19, '彩妆', NULL, NULL, 19);
INSERT INTO `category` VALUES (20, '家电', NULL, NULL, 20);
INSERT INTO `category` VALUES (21, '绿植农资', NULL, NULL, 21);
INSERT INTO `category` VALUES (22, '厨具', NULL, NULL, 22);
INSERT INTO `category` VALUES (24, '推荐', NULL, NULL, 0);

-- ----------------------------
-- Table structure for contact
-- ----------------------------
DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact`  (
  `contactid` int(0) NOT NULL AUTO_INCREMENT COMMENT '联系人ID',
  `cid` bigint(0) NOT NULL COMMENT '客户ID',
  `contact_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系人电话',
  `contact_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系人地址',
  `is_default` int(0) DEFAULT 0 COMMENT '是否默认：0否，1是',
  PRIMARY KEY (`contactid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '联系人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for contact_group
-- ----------------------------
DROP TABLE IF EXISTS `contact_group`;
CREATE TABLE `contact_group`  (
  `groupid` int(0) NOT NULL AUTO_INCREMENT COMMENT '分组ID',
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分组名称',
  `cid` bigint(0) NOT NULL COMMENT '客户ID',
  PRIMARY KEY (`groupid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '联系人分组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `cid` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '客户id',
  `douyinCode` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '抖音号(最多16个字，只允许字母、数学、下划线和点)',
  `nickName` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '客户昵称',
  `headerimg` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '头像',
  `gender` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '性别',
  `custTel` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '客户电话',
  `birth` date DEFAULT NULL COMMENT '生日',
  `address` varchar(300) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '地址',
  `resume` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '简介',
  `lastLoginTime` datetime(0) DEFAULT NULL COMMENT '最后一次登录的时间',
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `userpwd` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`cid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '客户表（用户表）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES (1, 'xiaoye9689745', '小叶', NULL, '女', '13572251106', NULL, '陕西省西安市', NULL, '2026-01-07 21:51:50', '1', '1');
INSERT INTO `customer` VALUES (2, 'hongjiuyou98', '红酒哟', NULL, '男', '13289674503', NULL, '河南省郑州市', NULL, '2026-01-08 18:24:03', '2', '2');
INSERT INTO `customer` VALUES (3, 'liuliule78', '刘溜了', NULL, '女', '17890563306', NULL, '河南省郑州市', NULL, '2026-01-08 18:24:06', '3', '3');
INSERT INTO `customer` VALUES (4, 'lele605', '乐乐', NULL, '女', '12345897705', NULL, '陕西省西安市', NULL, '2026-01-08 18:24:08', '4', '4');
INSERT INTO `customer` VALUES (5, 'tutu675', '兔兔宝', NULL, '女', '13689440003', NULL, '河南省郑州市', NULL, '2026-01-08 18:24:11', '5', '5');
INSERT INTO `customer` VALUES (6, 'yingying89', '英英吆', NULL, '男', '13578665544', NULL, '河南省郑州市', NULL, '2026-01-08 18:24:14', '6', '6');
INSERT INTO `customer` VALUES (7, 'meiyou33', '美美嘞', NULL, '女', '13267559908', NULL, '河南省郑州市', NULL, '2026-01-08 18:24:16', '7', '7');
INSERT INTO `customer` VALUES (8, 'dudu4523', '嘟嘟', NULL, '男', '14456785555', NULL, '河南省郑州市', NULL, '2026-01-08 18:24:19', '8', '8');

-- ----------------------------
-- Table structure for discounts
-- ----------------------------
DROP TABLE IF EXISTS `discounts`;
CREATE TABLE `discounts`  (
  `disid` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '优惠劵id',
  `gid` bigint(0) DEFAULT NULL COMMENT '商品id',
  `dtype` int(0) DEFAULT NULL COMMENT '优惠类型：1.劵后价  2.限时价 3.立减',
  `couponSatisfyPrice` int(0) DEFAULT NULL COMMENT '满足价格',
  `subPrice` int(0) DEFAULT NULL COMMENT '减少价格',
  `endTime` datetime(0) DEFAULT NULL COMMENT '限时结束时间',
  `timeLimitPrice` decimal(16, 2) DEFAULT NULL COMMENT '限时价格',
  PRIMARY KEY (`disid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '优惠表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of discounts
-- ----------------------------
INSERT INTO `discounts` VALUES (1, 1, 2, NULL, NULL, '2025-08-20 00:00:00', 3.00);
INSERT INTO `discounts` VALUES (2, 2, 1, 30, 3, NULL, NULL);
INSERT INTO `discounts` VALUES (3, 3, 2, NULL, NULL, '2025-08-25 17:36:39', 43.90);
INSERT INTO `discounts` VALUES (4, 4, 1, 50, 5, NULL, NULL);
INSERT INTO `discounts` VALUES (5, 5, 3, NULL, 10, NULL, NULL);
INSERT INTO `discounts` VALUES (6, 6, 1, 49, 6, NULL, NULL);
INSERT INTO `discounts` VALUES (7, 7, 1, 20, 3, NULL, NULL);
INSERT INTO `discounts` VALUES (8, 8, 2, NULL, NULL, '2025-08-24 17:40:25', 41.31);

-- ----------------------------
-- Table structure for evaluate
-- ----------------------------
DROP TABLE IF EXISTS `evaluate`;
CREATE TABLE `evaluate`  (
  `evid` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '评价id',
  `gid` bigint(0) DEFAULT NULL COMMENT '商品id',
  `gdid` bigint(0) DEFAULT NULL COMMENT '商品详细id',
  `etype` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '评价类型逗号分隔：1.回头客 2.是正品 3.口感好 4.会回购 ',
  `content` varchar(300) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '评价内容',
  `pic` varchar(800) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '评价的图片，多个以;分隔',
  `cid` bigint(0) DEFAULT NULL COMMENT '客户id',
  `evidp` bigint(0) DEFAULT NULL COMMENT '父id',
  PRIMARY KEY (`evid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '平价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of evaluate
-- ----------------------------
INSERT INTO `evaluate` VALUES (1, 1, 1, '1,3', '好吃，香辣酥脆，下次还会买。', '/static/images/goods/780.jpg', 3, 0);
INSERT INTO `evaluate` VALUES (2, 1, 2, '3,4', '太好吃了，老公爱吃的不得了。', '/static/images/goods/780.jpg', 4, 0);
INSERT INTO `evaluate` VALUES (3, 1, 1, '1,3', '真的是太好了，好好吃。', '/static/images/goods/780.jpg', 2, 0);
INSERT INTO `evaluate` VALUES (4, 1, 3, '1,3,4', '嗯嗯，真的是太好吃了，下次再买。', '/static/images/goods/780.jpg', 2, 0);
INSERT INTO `evaluate` VALUES (5, 2, 4, '4', '好，下次再买。', '/static/images/goods/780.jpg', 5, 0);
INSERT INTO `evaluate` VALUES (6, 5, 15, '1,4', '不错不错。', '/static/images/goods/780.jpg', 6, 0);
INSERT INTO `evaluate` VALUES (7, 9, 30, '4', '还好。', '/static/images/goods/780.jpg', 7, 0);
INSERT INTO `evaluate` VALUES (8, 9, 31, '1,4', '下次回购。', '/static/images/goods/780.jpg', 8, 0);
INSERT INTO `evaluate` VALUES (9, 10, NULL, NULL, '很好', NULL, 1, NULL);
INSERT INTO `evaluate` VALUES (10, 10, NULL, NULL, '很好看', NULL, 1, NULL);
INSERT INTO `evaluate` VALUES (11, 7, NULL, NULL, '很好用AAAA', NULL, 1, NULL);
INSERT INTO `evaluate` VALUES (12, 10, NULL, '5', 'good', NULL, 1, NULL);
INSERT INTO `evaluate` VALUES (13, 10, NULL, '5', '你好', NULL, 1, NULL);
INSERT INTO `evaluate` VALUES (14, 10, 0, '5', '可以可以', '', 1, 0);
INSERT INTO `evaluate` VALUES (15, 11, 35, '5', '111', '', 1, 0);
INSERT INTO `evaluate` VALUES (16, 1, 2, '5', '222', '', 1, 0);
INSERT INTO `evaluate` VALUES (17, 17, 40, '5', '这是一个牛奶', '', 1, 0);
INSERT INTO `evaluate` VALUES (18, 9, 29, '5', '6666', '', 1, 0);
INSERT INTO `evaluate` VALUES (19, 16, 39, '5', '1233444', '', 1, 0);
INSERT INTO `evaluate` VALUES (20, 16, 39, '5', '1111', 'http://localhost:8089//uploads/OIP-C.jpg', 1, 0);
INSERT INTO `evaluate` VALUES (21, 11, 35, '5', '111', 'http://localhost:8089//uploads/780.jpg', 1, 0);

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `type` tinyint(0) NOT NULL DEFAULT 1 COMMENT '反馈类型：1-功能建议, 2-问题反馈, 3-其他',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '反馈内容',
  `contact` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系方式',
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '图片地址，多个用逗号分隔',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '处理状态：0-待处理, 1-处理中, 2-已解决, 3-已关闭',
  `reply_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '回复内容',
  `reply_time` datetime(0) DEFAULT NULL COMMENT '回复时间',
  `admin_id` bigint(0) DEFAULT NULL COMMENT '处理管理员ID',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户反馈表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of feedback
-- ----------------------------
INSERT INTO `feedback` VALUES (1, 1, 1, '哈哈哈000000000000', '', '', 0, NULL, NULL, NULL, '2026-01-08 00:19:11', '2026-01-08 00:19:11');
INSERT INTO `feedback` VALUES (2, 1, 1, '哈哈哈1111111111', '15233659985', '', 0, NULL, NULL, NULL, '2026-01-08 00:25:43', '2026-01-08 00:25:43');
INSERT INTO `feedback` VALUES (3, 1, 1, '111111111111111', '', NULL, 0, NULL, NULL, NULL, '2026-01-08 00:33:47', '2026-01-08 00:33:47');
INSERT INTO `feedback` VALUES (4, 1, 3, '我感觉的话，就是如果，嗯嗯，就是', '', NULL, 0, NULL, NULL, NULL, '2026-01-08 15:11:56', '2026-01-08 15:11:56');
INSERT INTO `feedback` VALUES (5, 1, 2, '我感觉这个软件有时候很卡', '', NULL, 0, NULL, NULL, NULL, '2026-01-08 21:27:53', '2026-01-08 21:27:53');
INSERT INTO `feedback` VALUES (6, 1, 1, 'gggggggggggggggggggg', '', NULL, 0, NULL, NULL, NULL, '2026-01-10 17:10:25', '2026-01-10 17:10:25');
INSERT INTO `feedback` VALUES (7, 1, 1, '00000000000000', '', NULL, 0, NULL, NULL, NULL, '2026-01-10 18:31:43', '2026-01-10 18:31:43');
INSERT INTO `feedback` VALUES (8, 1, 1, 'goods11dwdqq', '15120693636', NULL, 0, NULL, NULL, NULL, '2026-01-12 14:45:42', '2026-01-12 14:45:42');
INSERT INTO `feedback` VALUES (9, 1, 2, '你好，年号ssassxx', '', NULL, 0, NULL, NULL, NULL, '2026-01-12 17:15:45', '2026-01-12 17:15:45');
INSERT INTO `feedback` VALUES (10, 1, 1, '11111111111', '', NULL, 0, NULL, NULL, NULL, '2026-01-14 15:20:03', '2026-01-14 15:20:03');

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `gid` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `gname` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品名称',
  `icon` varchar(5000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品图片多个用;分号分隔',
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '商品内容',
  `price` decimal(16, 2) DEFAULT NULL COMMENT '商品价格（无类型）',
  `gstatus` int(0) DEFAULT NULL COMMENT '商品状态：0,下架  1,上架',
  `stockTotalNum` int(0) DEFAULT NULL COMMENT '商品库存总量',
  `sellTotalNum` int(0) DEFAULT NULL COMMENT '商品总销售量',
  `selectType` int(0) DEFAULT NULL COMMENT '用户未选择商品时显示的类型：1.商品标题 2.图片缩略图',
  `selectTypeText` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '提示用户有多少类型可选择',
  `restBuyNum` int(0) DEFAULT NULL COMMENT '限购数量',
  `postprice` decimal(10, 2) DEFAULT NULL COMMENT '邮费',
  `cgid` int(0) DEFAULT NULL COMMENT '商品类型id',
  `sid` int(0) DEFAULT NULL COMMENT '所属店铺id',
  PRIMARY KEY (`gid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (1, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', 'http://localhost:8089/0391dda5-e93e-43ed-a792-4b8c0e3a2ac2.png', NULL, 10.00, 1, 156, 56, 2, '有3种规格可选', 10, 5.00, 1, 1);
INSERT INTO `goods` VALUES (2, '碧根果新货500g250g新鲜坚果零食奶油味奶香浓郁长寿干果年货5斤', 'http://localhost:8089/28d35d47-e1f9-4f46-8d94-1bb9290afa36.png', NULL, 18.00, 1, 30, 23, 1, '有2种规格可选', 5, 0.00, 1, 1);
INSERT INTO `goods` VALUES (3, '自然开口盐焗开心果原味坚果炒货无漂白散装500g袋装孕妇儿童零食', 'http://localhost:8089/5a047dd3-fbae-44ec-9d8e-9dd642a61e9a.png', NULL, 43.90, 1, 55, 25, 1, '共4种口味可选', 10, 0.00, 1, 1);
INSERT INTO `goods` VALUES (4, '高档纯棉四件套轻奢高级感100全棉南通家纺床单被套床上用品床笠', 'http://localhost:8089/a18a47fe-82d4-4db1-8d25-da41aa5eee21.jpg', NULL, 298.00, 1, 199, 66, 1, '共5种尺寸可选', 12, 0.00, 2, 2);
INSERT INTO `goods` VALUES (5, '简约纯棉四件套全棉100家纺床上用品床单被套1.8米被罩宿舍三件套', 'http://localhost:8089/bd6668a2-8814-48c5-a0d1-3e4966e40c26.jpg', NULL, 98.00, 1, 99, 21, 1, '共4种尺寸可选', 5, 0.00, 2, 2);
INSERT INTO `goods` VALUES (6, '大豆纤维冬被被子保暖儿童丝棉被秋冬学生宿舍被芯春秋被四季通用', 'http://localhost:8089/1366a84e-020c-477f-bc09-6b05f0d8501f.webp', NULL, 43.80, 1, 108, 42, 2, '共3种尺寸可选', 20, 0.00, 2, 2);
INSERT INTO `goods` VALUES (7, '白衬衫女长袖职业春秋夏季短袖宽松工作服正装大码工装女装白衬衣', 'http://localhost:8089/2f848fc6-f9ec-4472-8c90-932587479fc0.jpg', NULL, 16.90, 1, 184, 52, 1, '共3种尺寸可选', 30, 0.00, 15, 2);
INSERT INTO `goods` VALUES (8, '乐乐逗逗童装针织儿童开衫中大童2022秋装新款休闲百搭男童女童毛衣潮', 'http://localhost:8089/51687b24-b909-425f-ab85-ceda0676561c.webp', NULL, 99.00, 1, 117, 59, 2, '共4种颜色可选', 24, 0.00, 3, 3);
INSERT INTO `goods` VALUES (9, '宝宝长袖T恤春秋纯棉衣服小婴儿打底衫男童上衣女童春装儿童童装', 'http://localhost:8089/1479eb7e-3066-44d5-a5e3-560b547682e6.jpg', NULL, 41.31, 1, 145, 50, 2, '共3种颜色可选', 10, 0.00, 3, 3);
INSERT INTO `goods` VALUES (10, '品牌散装碎海苔500g拌饭即食切丝海苔条250克章鱼小丸子 零食', 'http://localhost:8089/af0b23a6-3110-4586-a331-dc20b456e71f.png', '<p><br></p><p style=\"text-align: left;\"><span style=\"background-color: rgb(255, 255, 255); font-size: 48px;\">为了避免必要的纠纷，请亲们看好详情再下单！</span><span style=\"color: rgb(255, 0, 0); background-color: rgb(255, 255, 255); font-size: 48px;\">有特殊要求请提前联系客服！</span></p><p> </p><p>下单赠送运费险，签收前请验货，任何不满意包退包运费！</p><p><span style=\"color: rgb(231, 95, 51);\">我们卖的是边角料，每一箱每一包都可能有区别，不保证宽度和长度，介意的，要求比较高的，请慎拍！</span></p><p><span style=\"color: rgb(255, 0, 0);\">小丸子细条口感较差，不适合做零食，慎拍！</span></p><p style=\"text-align: left;\"><span style=\"color: rgb(0, 0, 255);\">此款海苔为品牌海苔生产时所切下的边角料，不仅实惠而且口味好，我们卖的是性价比。此款产品20斤整箱到货，小店经过分拣，比较碎的我们挑出来做章鱼小丸子，细条状的留下来分装称重,质量口感保证，请大家不用怀疑不是正品。每次厂家到货都是几十箱，为了避免发货快递途中出现坏包的情况，我们分装的时候会适量排除袋里的空气；但是快递有多暴力我想大家都明白，海苔本身是比较脆的，所以在分装以及快递运输途中会出现有断裂以及一点粉末出现，但是绝不影响食用。粉末做饭团或者直接拌饭都是很美味的哦。本产品为每包毛重250克，拍500克的一份发两包，为了降低客单价，每包含干燥剂50克，需要净含量或者大批进货的亲请联系客服。希望亲们能理解和支持。</span></p><p> </p><p><img src=\"https://img.alicdn.com/imgextra/i2/2115358365/TB20kfRqBjTBKNjSZFwXXcG4XXa_!!2115358365.jpg\" alt=\"\" data-href=\"\" style=\"\"/><img src=\"https://img.alicdn.com/imgextra/i1/2115358365/TB2C5f7qrZnBKNjSZFhXXc.oXXa_!!2115358365.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p><img src=\"https://img.alicdn.com/imgextra/i1/2115358365/O1CN01SG7eO82BfDb79DjRD_!!2115358365.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p><img src=\"https://img.alicdn.com/imgextra/i4/2115358365/O1CN01DLyc6n2BfDb8GZIPm_!!2115358365.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p> </p><p> </p><p><img src=\"https://gw.alicdn.com/tfs/TB1d0h2qVYqK1RjSZLeXXbXppXa-1125-960.png?getAvatar=avatar\" alt=\"\" data-href=\"\" style=\"width: 750px;\"/></p>', 20.00, 1, 98, 52, 2, '共3种类别可选', 5, 0.00, 1, 1);
INSERT INTO `goods` VALUES (11, '拖鞋女夏季室内居家用浴室洗澡防滑宾馆酒店EVA防臭男士拖鞋软底', 'http://localhost:8089/11a842e3-db94-4f95-8718-9497d521c25a.jpg', '<p style=\"text-align: left;\">品牌：CLEOTK</p><p style=\"text-align: left;\">风格：简约</p><p style=\"text-align: left;\">帮面内里材质：EVA</p><p style=\"text-align: left;\">鞋码：36-37,38-39,40-41,42-43,44-45</p><p style=\"text-align: left;\">适用对象：情侣</p><p style=\"text-align: left;\">省份：福建省</p><p style=\"text-align: left;\">地市：三明市</p><p style=\"text-align: left;\">帮面材质：EVA</p><p style=\"text-align: left;\">货号：米亚</p><p style=\"text-align: left;\">填充物：EVA</p><p style=\"text-align: left;\">适用季节：夏季</p><p style=\"text-align: left;\">中底材质：EVA</p><p style=\"text-align: left;\">款式：一字型</p><p style=\"text-align: left;\">产地：中国大陆</p><p style=\"text-align: left;\">适用场地：居家拖鞋</p><p style=\"text-align: left;\">鞋底厚度：1-3cm</p><p style=\"text-align: left;\">颜色分类：黑色,黄色,蓝色,灰色,桔色,浅绿,粉色</p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i4/1680982267/O1CN018HOAan1ScKhSOQ8eO_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i3/1680982267/O1CN01WlTuYT1ScKhRiALkP_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><br></p><p style=\"text-align: left;\"><br></p><p style=\"text-align: left;\"><br></p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i1/1680982267/O1CN01x4leDF1ScKhmUOvwO_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i4/1680982267/O1CN01yHDydU1ScKhDF9FP5_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i3/1680982267/O1CN01euKx2O1ScKhzdKqL9_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><br></p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i2/1680982267/O1CN01Mg7SHM1ScKhmUPo09_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><br></p><p style=\"text-align: left;\"><br></p><p style=\"text-align: left;\"><br></p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i1/1680982267/O1CN01HIv8Zc1ScKhM7txGf_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i3/1680982267/O1CN01Wg7EYg1ScKhJHd4kQ_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i3/1680982267/O1CN01pGVmqV1ScKhUIfYaK_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i2/1680982267/O1CN01NWHjb21ScKhM7ugzO_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i1/1680982267/O1CN01IceddT1ScKhUIfgto_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><br></p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i4/1680982267/O1CN01Hp3p951ScKhwkNWjs_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><br></p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i1/1680982267/O1CN01xv2TP11ScKhRiB5bK_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i4/1680982267/O1CN01zCiGHv1ScKhPF4Xs4_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><br></p><p style=\"text-align: left;\"><br></p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i2/1680982267/O1CN01GtDS0g1ScKhDF6trg_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i3/1680982267/O1CN01xK4ZWS1ScKi1G7eIi_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><br></p><p style=\"text-align: left;\"><img src=\"https://img.alicdn.com/imgextra/i4/1680982267/O1CN01EvM4BM1ScKhHvcBK4_!!1680982267.jpg\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p><p style=\"text-align: left;\"><img src=\"https://gw.alicdn.com/tfs/TB1d0h2qVYqK1RjSZLeXXbXppXa-1125-960.png?getAvatar=avatar\" alt=\"\" data-href=\"\" style=\"width: 750px;\"></p>', 9.80, 1, 95, 55, 2, '共3种尺寸可选', 5, 0.00, 12, 1);
INSERT INTO `goods` VALUES (16, '凉鞋', 'http://localhost:8089/e5237920-03c8-4161-9dd9-0435932daa5c.png', '假设这是一个凉鞋', 33.30, 1, 59, 7, 1, '共两种类型可选', 66, 2.00, 12, 1);
INSERT INTO `goods` VALUES (17, '牛奶', 'http://localhost:8089/a22cbc7e-2350-4c4b-9f2b-d4042a491c94.webp', '这是一种牛奶', 66.00, 1, 45, 15, 1, '2种规格', 60, 5.00, 0, 1);
INSERT INTO `goods` VALUES (18, '商品二', 'http://localhost:8089/c5b829e8-2534-43ca-8143-2a273de3de58.webp', '1111', 20.00, 0, 18, 2, 1, '', 5, 20.00, 24, 1);

-- ----------------------------
-- Table structure for goods_collect
-- ----------------------------
DROP TABLE IF EXISTS `goods_collect`;
CREATE TABLE `goods_collect`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(0) NOT NULL COMMENT '用户ID',
  `goods_id` int(0) NOT NULL COMMENT '商品ID',
  `goods_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `goods_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品图片',
  `goods_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '商品价格',
  `collect_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  `status` tinyint(0) NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_goods`(`user_id`, `goods_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_goods_id`(`goods_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 355586059 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_collect
-- ----------------------------
INSERT INTO `goods_collect` VALUES (1, 30, 1, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', '/static/images/goods/780.jpg', 10.00, '2026-01-12 22:31:15', 1);
INSERT INTO `goods_collect` VALUES (2, 1, 17, '牛奶', 'http://localhost:8089/4acac3ae-0670-4b4c-bb5c-afa12cf473a2.png', 66.00, '2026-01-12 22:16:03', 1);
INSERT INTO `goods_collect` VALUES (4, 30, 2, '碧根果新货500g250g新鲜坚果零食奶油味奶香浓郁长寿干果年货5斤', '/static/images/goods/780.jpg', 18.00, '2026-01-12 22:41:32', 1);
INSERT INTO `goods_collect` VALUES (5, 30, 16, '凉鞋', 'http://localhost:8089/e5237920-03c8-4161-9dd9-0435932daa5c.png', 33.30, '2026-01-12 22:42:20', 1);
INSERT INTO `goods_collect` VALUES (355586054, 30, 18, '商品二', 'http://localhost:8089/c5b829e8-2534-43ca-8143-2a273de3de58.webp', 20.00, '2026-01-12 22:43:32', 1);
INSERT INTO `goods_collect` VALUES (355586055, 31, 17, '牛奶', 'http://localhost:8089/4acac3ae-0670-4b4c-bb5c-afa12cf473a2.png', 66.00, '2026-01-13 01:00:07', 1);
INSERT INTO `goods_collect` VALUES (355586056, 31, 18, '商品二', 'http://localhost:8089/c5b829e8-2534-43ca-8143-2a273de3de58.webp', 20.00, '2026-01-13 14:16:49', 1);
INSERT INTO `goods_collect` VALUES (355586057, 31, 1, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', '/static/images/goods/780.jpg', 10.00, '2026-01-13 14:17:12', 1);
INSERT INTO `goods_collect` VALUES (355586058, 31, 7, '白衬衫女长袖职业春秋夏季短袖宽松工作服正装大码工装女装白衬衣', '/static/images/goods/780.jpg', 16.90, '2026-01-13 14:17:25', 1);
INSERT INTO `goods_collect` VALUES (355586059, 1, 9, '宝宝长袖T恤春秋纯棉衣服小婴儿打底衫男童上衣女童春装儿童童装', '/static/images/goods/780.jpg', 41.31, '2026-01-14 00:42:11', 1);

-- ----------------------------
-- Table structure for goods_detail
-- ----------------------------
DROP TABLE IF EXISTS `goods_detail`;
CREATE TABLE `goods_detail`  (
  `gdid` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '商品详细id',
  `gid` bigint(0) DEFAULT NULL COMMENT '商品id(父id)',
  `stockNum` int(0) DEFAULT NULL COMMENT '库存量',
  `sellNum` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '售卖量',
  `originalPrice` decimal(16, 2) DEFAULT NULL COMMENT '原价',
  `specialPrice` decimal(16, 2) DEFAULT NULL COMMENT '特价',
  `gdpic` varchar(5120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品图片',
  `attributeName1` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '属性名称1',
  `attributeValue1` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '属性值1',
  `attributeName2` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '属性名称2',
  `attributeValue2` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '属性值2',
  `attributeName3` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '属性名称3',
  `attributeValue3` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '属性值3',
  `attributeName4` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '属性名称4',
  `attributeValue4` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '属性值4',
  `attributeName5` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '属性名称5',
  `attributeValue5` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '属性值5',
  PRIMARY KEY (`gdid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '商品详细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_detail
-- ----------------------------
INSERT INTO `goods_detail` VALUES (1, 1, 72, '34', 16.76, 8.76, 'http://localhost:8089/4c7a323b-11e6-4946-b368-79a2679f0845.png', '规格', '【精品大罐】正宗香辣脆500g*1罐', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (2, 1, 38, '14', 32.36, 24.36, 'http://localhost:8089/648316a2-3ed6-410e-9e10-296efc7b750c.png', '规格', '【精品大罐】正宗香辣脆500g*2罐', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (3, 1, 46, '8', 9.56, 2.56, 'http://localhost:8089/0589405f-1edd-4b43-8b4c-e112980ff31e.png', '规格', '【尝鲜半斤】正宗香辣脆250g*1罐', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (4, 2, 12, '8', 45.00, 18.00, 'http://localhost:8089/25ba57f1-75c1-4671-8ec5-7475f13569a1.png', '规格', '250g奶香味', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (5, 2, 18, '15', 90.00, 34.80, 'http://localhost:8089/041a949a-b722-43e2-b09e-29978112ffea.png', '规格', '500g奶香味', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (6, 3, 13, '8', 60.00, 43.90, 'http://localhost:8089/9fc378da-1b84-4414-ba62-c5ae8b2f2dd3.jpg', '食品口味', '盐焗开心果一斤', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (7, 3, 20, '5', 69.00, 52.90, 'http://localhost:8089/d68223f1-46fe-4661-97b4-1e375672c633.jpg', '食品口味', '真原味(特级)开心果一斤', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (8, 3, 7, '7', 231.00, 214.90, 'http://localhost:8089/e05961cd-b805-441b-8b3d-1feb668422c1.jpg', '食品口味', '盐焗开心果特惠5斤', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (9, 3, 15, '5', 271.00, 254.90, 'http://localhost:8089/fe454631-6c9f-4289-b56f-bbe04931f6b6.jpg', '食品口味', '真原味(特级)开心果特惠5斤', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (10, 4, 20, '4', 328.00, 298.00, 'http://localhost:8089/a6dc641b-553f-466e-a46e-8e80881d1b35.png', '适合床尺寸', '1.2m床单款三件套(被套1.5X2m)', '颜色分类', '莫兰迪高贵紫', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (11, 4, 50, '30', 388.00, 358.00, 'http://localhost:8089/4abf275a-7ae7-4671-a764-4c0306e0c9df.webp', '适合床尺寸', '1.5m床单款四件套(被套2x2.3m)', '颜色分类', '莫兰迪高贵紫', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (12, 4, 49, '21', 388.00, 358.00, 'http://localhost:8089/86be23bc-dcfb-4842-9c34-da2d4755daea.webp', '适合床尺寸', '1.5m床单款四件套(被套2x2.3m)', '颜色分类', '莫兰迪高贵绿', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (13, 4, 40, '6', 388.00, 358.00, 'http://localhost:8089/5096ca52-eef0-4903-bf3f-d40e3b8bc57f.webp', '适合床尺寸', '1.5m床单款四件套(被套2x2.3m)', '颜色分类', '莫兰迪高贵粉', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (14, 4, 40, '5', 388.00, 358.00, 'http://localhost:8089/a4a87586-19c2-4347-a70a-efefa964d1bc.webp', '适合床尺寸', '1.5m床单款四件套(被套2x2.3m)', '颜色分类', '莫兰迪高贵黄', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (15, 5, 25, '5', 270.00, 135.00, 'http://localhost:8089/80183832-ca11-427e-8307-2b9d354379fe.webp', '适合床尺寸', '1.5米床【被套180*220】四件套', '颜色分类', '快乐空间', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (16, 5, 24, '5', 270.00, 135.00, 'http://localhost:8089/6a60fce2-72c5-4486-bd8c-503df3b0bc2e.webp', '适合床尺寸', '1.5米床【被套180*220】四件套', '颜色分类', '蜜糖甜心', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (17, 5, 20, '6', 276.00, 138.00, 'http://localhost:8089/621f36b4-9c25-4931-ae0a-e42773f1f7ac.webp', '适合床尺寸', '1.8米床【被套200*230】四件套', '颜色分类', '栗色罗格', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (18, 5, 30, '5', 276.00, 138.00, 'http://localhost:8089/3a592d5b-a5b5-42e8-bdc6-e060fa968027.jpg', '适合床尺寸', '1.8米床【被套200*230】四件套', '颜色分类', '花昔粉', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (19, 6, 36, '14', 48.80, 43.80, 'http://localhost:8089/7fa477ab-3812-4144-88fb-8d1816804ddb.jpg', '尺寸', '150*200cm【夏凉被 空调被】不结团 不跑棉', '颜色分类', '【珍珠白】抑菌大豆被', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (20, 6, 35, '11', 55.80, 50.80, 'http://localhost:8089/7027a3a4-7019-4cf1-980b-2d9670f16158.webp', '尺寸', '180*220cm【4斤 春秋被】不结团 不跑棉', '颜色分类', '【绅士灰】抑菌大豆被', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (21, 6, 37, '17', 55.80, 50.80, 'http://localhost:8089/21dac47f-27d7-49e5-a966-a7f87c640ec3.webp', '尺寸', '180*220cm【4斤 春秋被】不结团 不跑棉', '颜色分类', '【香妃玉】抑菌大豆被', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (22, 7, 88, '18', 16.90, 16.90, 'http://localhost:8089/036b9d75-b1b7-4e82-acb6-2d1dfaad267f.jpg', '颜色分类', 'F11【短袖】纯白翻领', '尺码', 'XL', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (23, 7, 37, '23', 18.90, 18.90, 'http://localhost:8089/3c5b83f4-307e-4c81-a0e6-f21555fa37ce.webp', '颜色分类', '【长袖】纯白V领', '尺码', '2XL', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (24, 7, 59, '11', 18.90, 18.90, 'http://localhost:8089/90d0edaa-56e5-43ba-bc72-4fd95a41a2be.webp', '颜色分类', '【长袖】纯白翻领', '尺码', 'XL', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (25, 8, 27, '29', 139.00, 99.00, 'http://localhost:8089/ab75740b-5c63-4365-8626-0a1e5520d71e.webp', '颜色分类', '橄榄绿（80%妈妈的选择)', '参考身高', '110cm', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (26, 8, 30, '10', 139.00, 99.00, 'http://localhost:8089/594cfb41-a420-4a15-8a4d-a04d2914beee.webp', '颜色分类', '藏青色', '参考身高', '120cm', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (27, 8, 30, '12', 129.00, 89.00, 'http://localhost:8089/6430961d-1979-400e-8740-69096654801d.webp', '颜色分类', '雾霾蓝', '参考身高', '120cm', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (28, 8, 30, '8', 129.00, 89.00, 'http://localhost:8089/327537de-a244-42ae-bdec-8cded326ad1f.webp', '颜色分类', '驼色', '参考身高', '130cm', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (29, 9, 47, '13', 45.90, 41.31, 'http://localhost:8089/11cf9f09-6a9d-465e-8f0e-c4db465de8ca.webp', '颜色分类', '彩虹条', '参考身高', '110cm', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (30, 9, 50, '15', 45.90, 41.31, 'http://localhost:8089/51f80311-2589-438d-ae3c-aee7036f359c.jpg', '颜色分类', '黄色', '参考身高', '120cm', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (31, 9, 48, '22', 45.90, 41.31, 'http://localhost:8089/c8b2fe64-27f4-4fc8-b163-ff4cde0911e9.jpg', '颜色分类', '白色', '参考身高', '120cm', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (32, 10, 33, '10', 20.00, 19.90, 'http://localhost:8089/3739e2aa-f64a-47e3-8ee6-02c5bb581b14.jpg', '类别', '300g', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (33, 10, 33, '15', 18.00, 15.00, 'http://localhost:8089/a9db9627-7567-44da-80dc-2400f358f328.jpg', '类别', '200g', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (34, 10, 32, '27', 15.00, 11.10, 'http://localhost:8089/e39be713-77dc-464e-be7f-da36d8178f29.jpg', '类别', '250g', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (35, 11, 31, '17', 15.00, 9.80, 'http://localhost:8089/bf5e6213-51ab-4ad7-9629-a278cf5e80a2.png', '尺寸', '43码', '参考脚长', '26.5cm', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (36, 11, 30, '18', 15.00, 9.80, 'http://localhost:8089/5e2bc737-0e5e-4009-b76f-a4f30338baf2.jpg', '尺寸', '42码', '参考脚长', '26cm', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (37, 11, 34, '20', 15.00, 9.80, 'http://localhost:8089/e3762c9e-48f8-423e-b3d0-4fd8b5efd5d3.png', '尺寸', '41码', '参考脚长', '5.5cm', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `goods_detail` VALUES (38, 16, 27, '6', 33.30, 20.00, 'http://localhost:8089/72e0eefc-dd7b-4d47-b2e7-3550e29cd2e7.png', '大号凉鞋', '44码', '参考脚长', '26cm', '', '', '', '', '', '');
INSERT INTO `goods_detail` VALUES (39, 16, 32, '1', 30.00, 20.00, 'http://localhost:8089/280c4620-a92b-4a38-a694-7472360b8142.png', '小号凉鞋', '40码', '参考脚长', '25cm', '', '', '', '', '', '');
INSERT INTO `goods_detail` VALUES (40, 17, 5, '15', 60.00, 49.90, 'http://localhost:8089/a0351831-d4d8-4c02-9db9-1e4610ee0f70.png', '规格', '60瓶', '', '', '', '', '', '', '', '');
INSERT INTO `goods_detail` VALUES (41, 17, 40, '0', 50.00, 39.90, 'http://localhost:8089/94d7a093-2a68-4dbe-9ff5-f27be7451548.webp', '规格', '50瓶', '', '', '', '', '', '', '', '');
INSERT INTO `goods_detail` VALUES (42, 18, 16, '4', 20.00, 15.50, '', '1', '1', '', '', '', '', '', '', '', '');
INSERT INTO `goods_detail` VALUES (43, 19, 8, '2', 15.00, 10.00, 'http://localhost:8089/47fd1d20-6e13-4afc-b98b-eff7707d9e46.png', '9-1', '9-2', '', '', '', '', '', '', '', '');
INSERT INTO `goods_detail` VALUES (44, 19, 10, '0', 15.00, 9.90, 'http://localhost:8089/18e3183e-2fae-4e68-a773-4edacedae3f5.png', '9-2', '9-2', '', '', '', '', '', '', '', '');
INSERT INTO `goods_detail` VALUES (45, 20, 66, '0', 15.00, 9.90, 'http://localhost:8089/bbe4ca2c-3f05-4f61-a4e8-06f1c14de6d8.png', '1', '', '', '', '', '', '', '', '', '');
INSERT INTO `goods_detail` VALUES (46, 20, 55, '0', 15.00, 10.00, 'http://localhost:8089/0553408c-f0e6-4817-8aef-57540658447c.jpg', '1', '', '', '', '', '', '', '', '', '');

-- ----------------------------
-- Table structure for goods_safeguard
-- ----------------------------
DROP TABLE IF EXISTS `goods_safeguard`;
CREATE TABLE `goods_safeguard`  (
  `gid` bigint(0) DEFAULT NULL COMMENT '商品id',
  `sgid` int(0) DEFAULT NULL COMMENT '保障id'
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '商品关联保障表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_safeguard
-- ----------------------------
INSERT INTO `goods_safeguard` VALUES (1, 1);
INSERT INTO `goods_safeguard` VALUES (1, 3);
INSERT INTO `goods_safeguard` VALUES (1, 4);
INSERT INTO `goods_safeguard` VALUES (1, 5);
INSERT INTO `goods_safeguard` VALUES (1, 7);
INSERT INTO `goods_safeguard` VALUES (2, 1);
INSERT INTO `goods_safeguard` VALUES (2, 2);
INSERT INTO `goods_safeguard` VALUES (2, 3);
INSERT INTO `goods_safeguard` VALUES (2, 4);
INSERT INTO `goods_safeguard` VALUES (2, 6);
INSERT INTO `goods_safeguard` VALUES (3, 2);
INSERT INTO `goods_safeguard` VALUES (3, 3);
INSERT INTO `goods_safeguard` VALUES (3, 4);
INSERT INTO `goods_safeguard` VALUES (3, 6);
INSERT INTO `goods_safeguard` VALUES (3, 7);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `orderid` bigint(0) NOT NULL COMMENT '订单id（时间毫秒数+cid）',
  `cid` bigint(0) DEFAULT NULL COMMENT '客户id',
  `sid` int(0) DEFAULT NULL COMMENT '门店id',
  `ordertime` datetime(0) DEFAULT NULL COMMENT '订单时间',
  `paytime` datetime(0) DEFAULT NULL COMMENT '支付时间',
  `orderstatus` int(0) DEFAULT NULL COMMENT '订单状态： 0.待支付 1.已支付 2.交易成功 3.已取消 4.退单',
  `orderstatusStr` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '订单状态： 0.待支付 1.已支付  2.已完成 3.已取消 4.退单',
  `buyTotalNums` int(0) DEFAULT NULL COMMENT '购物数量',
  `buyTotalPrice` decimal(16, 2) DEFAULT NULL COMMENT '购买总价格',
  `postprice` decimal(10, 2) DEFAULT NULL COMMENT '邮费',
  `beizhu` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`orderid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1679477844413, 1, 1, '2023-03-22 17:37:24', '2023-03-23 09:19:57', 1, '已支付', 3, 45.33, 5.00, '');
INSERT INTO `orders` VALUES (1679477844414, 1, 2, '2023-03-22 17:37:24', NULL, 3, '已取消', 1, 43.80, 0.00, '');
INSERT INTO `orders` VALUES (1679530160240, 1, 1, '2023-03-23 08:09:20', NULL, 3, '已取消', 3, 45.33, 5.00, '');
INSERT INTO `orders` VALUES (1679538169377, 1, 2, '2023-03-23 10:22:49', NULL, 3, '已取消', 1, 43.80, 0.00, '');
INSERT INTO `orders` VALUES (1679538169378, 1, 3, '2023-03-23 10:22:49', NULL, 3, '已取消', 2, 181.00, 0.00, '');
INSERT INTO `orders` VALUES (1679538797429, 1, 1, '2023-03-23 10:33:17', NULL, 3, '已取消', 2, 4.32, 10.00, '');
INSERT INTO `orders` VALUES (1679538856306, 1, 1, '2023-03-23 10:34:16', NULL, 3, '已取消', 1, -4.44, 5.00, '');
INSERT INTO `orders` VALUES (1679539791748, 1, 2, '2023-03-23 10:49:51', NULL, 3, '已取消', 3, 111.50, 0.00, '');
INSERT INTO `orders` VALUES (1679542547005, 1, 2, '2023-03-23 11:35:47', NULL, 3, '已取消', 3, 77.60, 0.00, '');
INSERT INTO `orders` VALUES (1679542774186, 1, 2, '2023-03-23 11:39:34', '2023-03-23 17:22:02', 1, '已支付', 2, 94.60, 0.00, '');
INSERT INTO `orders` VALUES (1686475307793, 1, 1, '2023-06-11 17:21:47', '2023-06-11 17:22:10', 1, '已支付', 1, 10.56, 15.00, '');
INSERT INTO `orders` VALUES (1686482638070, 1, 1, '2023-06-11 19:23:58', NULL, 3, '已取消', 2, 437.80, 15.00, '');
INSERT INTO `orders` VALUES (1686482668546, 1, 1, '2023-06-11 19:24:28', '2023-06-11 19:24:31', 1, '已支付', 1, 222.90, 15.00, '');
INSERT INTO `orders` VALUES (1687253519826, 1, 1, '2023-06-20 17:31:59', '2023-06-20 17:49:39', 1, '已支付', 4, 69.24, 10.00, '');
INSERT INTO `orders` VALUES (1687260344813, 1, 3, '2023-06-20 19:25:44', '2023-06-20 19:25:53', 1, '已支付', 3, 116.93, 0.00, '');
INSERT INTO `orders` VALUES (1687305111200, 1, 1, '2023-06-21 07:51:51', '2023-06-21 07:51:58', 1, '已支付', 1, 22.36, 5.00, '');
INSERT INTO `orders` VALUES (1687305912775, 1, 2, '2023-06-21 08:05:12', '2023-06-21 08:05:14', 1, '已支付', 2, 30.80, 0.00, '');
INSERT INTO `orders` VALUES (1688342829870, 1, 1, '2023-07-03 08:07:09', NULL, 3, '已取消', 2, 15.52, 5.00, '');
INSERT INTO `orders` VALUES (1688348594596, 1, 3, '2023-07-03 09:43:14', NULL, 3, '已取消', 2, 191.00, 0.00, '');
INSERT INTO `orders` VALUES (1694682477093, 1, 1, '2023-09-14 17:07:57', NULL, 3, '已取消', 1, 6.76, 5.00, '');
INSERT INTO `orders` VALUES (1694689885633, 1, 1, '2023-09-14 19:11:25', NULL, 3, '已取消', 1, 22.36, 5.00, '');
INSERT INTO `orders` VALUES (1711953695586, 1, 1, '2024-04-01 14:41:33', NULL, 3, '已取消', 1, 24.36, 5.00, '');
INSERT INTO `orders` VALUES (1711978854875, 1, 3, '2024-04-01 21:40:54', NULL, 3, '已取消', 1, 41.31, 0.00, '');
INSERT INTO `orders` VALUES (1711978858376, 1, 2, '2024-04-01 21:40:54', NULL, 3, '已取消', 1, 43.80, 0.00, '');
INSERT INTO `orders` VALUES (1711979443585, 1, 2, '2024-04-01 21:50:42', '2024-04-01 21:50:57', 1, '已支付', 2, 35.80, 0.00, '');
INSERT INTO `orders` VALUES (1712059963990, 1, 1, '2024-04-02 20:12:40', '2024-04-02 20:12:42', 1, '已支付', 1, 34.80, 0.00, '');
INSERT INTO `orders` VALUES (1751501693131, 1, 1, '2025-07-03 08:14:53', NULL, 3, '已取消', 3, 73.08, 5.00, '');
INSERT INTO `orders` VALUES (1751790734626, 1, 2, '2025-07-06 16:32:12', NULL, 3, '已取消', 1, 50.80, NULL, '');
INSERT INTO `orders` VALUES (1751790794237, 1, 2, '2025-07-06 16:33:07', NULL, 3, '已取消', 3, 50.70, NULL, '');
INSERT INTO `orders` VALUES (1751790813003, 1, 1, '2025-07-06 16:33:27', '2025-07-06 16:38:42', 1, '已支付', 3, 26.28, NULL, '');
INSERT INTO `orders` VALUES (1751790905432, 1, 2, '2025-07-06 16:35:05', NULL, 3, '已取消', 4, 75.60, NULL, '');
INSERT INTO `orders` VALUES (1766578087472, 1, 1, '2025-12-24 20:08:06', NULL, 3, '已取消', 9, 136.08, 10.00, '');
INSERT INTO `orders` VALUES (1766659536061, 1, 1, '2025-12-25 18:45:35', '2025-12-25 18:45:39', 1, '已支付', 1, 34.80, 0.00, '');
INSERT INTO `orders` VALUES (1767785500063, 1, 2, '2026-01-07 19:31:39', '2026-01-07 19:32:24', 1, '已支付', 7, 221.00, 0.00, '');
INSERT INTO `orders` VALUES (1767785565664, 1, 1, '2026-01-07 19:32:45', NULL, 3, '已取消', 7, 88.80, 0.00, '');
INSERT INTO `orders` VALUES (1767791112679, 2, 1, '2026-01-07 21:05:07', '2026-01-07 21:45:55', 1, '已支付', 3, 26.28, 5.00, '');
INSERT INTO `orders` VALUES (1767792669924, 2, 2, '2026-01-07 21:31:06', '2026-01-07 22:11:05', 1, '已支付', 1, 16.90, 0.00, '');
INSERT INTO `orders` VALUES (1767793469471, 2, 3, '2026-01-07 21:44:25', '2026-01-07 22:11:07', 1, '已支付', 1, 99.00, 0.00, '');
INSERT INTO `orders` VALUES (1767793615732, 2, 1, '2026-01-07 21:46:48', '2026-01-07 22:11:06', 1, '已支付', 3, 26.28, 5.00, '');
INSERT INTO `orders` VALUES (1767794452398, 2, 1, '2026-01-07 22:00:51', '2026-01-07 22:11:06', 1, '已支付', 2, 0.00, 0.00, '');
INSERT INTO `orders` VALUES (1767794939383, 1, 1, '2026-01-07 22:08:54', '2026-01-07 22:08:55', 1, '已支付', 3, 0.00, 0.00, '');
INSERT INTO `orders` VALUES (1767795063874, 2, 2, '2026-01-07 22:10:58', '2026-01-07 22:11:06', 1, '已支付', 1, 358.00, 0.00, '');
INSERT INTO `orders` VALUES (1767796013858, 1, 1, '2026-01-07 22:26:50', NULL, 0, '待支付', 6, 8.76, 5.00, '');
INSERT INTO `orders` VALUES (1767796016064, 1, 1, '2026-01-07 22:26:56', NULL, 3, '已取消', 5, 0.00, 0.00, '');
INSERT INTO `orders` VALUES (1767796089814, 1, 1, '2026-01-07 22:28:08', NULL, 3, '已取消', 5, 0.00, 0.00, '');
INSERT INTO `orders` VALUES (1767796120778, 1, 1, '2026-01-07 22:28:36', NULL, 3, '已取消', 1, 8.76, 5.00, '');
INSERT INTO `orders` VALUES (1767797083112, 1, 1, '2026-01-07 22:44:41', '2026-01-07 23:28:59', 1, '已支付', 2, 0.00, 0.00, '');
INSERT INTO `orders` VALUES (1767799760151, 1, 1, '2026-01-07 23:29:16', NULL, 3, '已取消', 4, 87.80, 0.00, '');
INSERT INTO `orders` VALUES (1767802301880, 5, 1, '2026-01-08 00:11:34', '2026-01-08 00:14:03', 1, '已支付', 2, 36.00, 0.00, '');
INSERT INTO `orders` VALUES (1767859869321, 1, 1, '2026-01-08 16:11:09', NULL, 3, '已取消', 5, 26.28, 5.00, '');
INSERT INTO `orders` VALUES (1767859882712, 1, 1, '2026-01-08 16:11:17', NULL, 3, '已取消', 2, 0.00, 0.00, '');
INSERT INTO `orders` VALUES (1767863121666, 1, 1, '2026-01-08 17:05:20', NULL, 3, '已取消', 2, 22.20, 0.00, '');
INSERT INTO `orders` VALUES (1767863148990, 1, 1, '2026-01-08 17:05:47', '2026-01-08 18:02:51', 1, '已支付', 1, 9.80, 0.00, '');
INSERT INTO `orders` VALUES (1767863294634, 1, 1, '2026-01-08 17:08:13', '2026-01-08 18:02:50', 1, '已支付', 3, 29.40, 0.00, '');
INSERT INTO `orders` VALUES (1767884219755, 1, 1, '2026-01-08 22:56:59', '2026-01-08 22:57:25', 1, '已支付', 1, 20.00, 2.00, '');
INSERT INTO `orders` VALUES (1767885990946, 1, 1, '2026-01-08 23:26:30', '2026-01-08 23:26:41', 1, '已支付', 2, 99.80, 5.00, '');
INSERT INTO `orders` VALUES (1767943565991, 1, 1, '2026-01-09 15:26:04', '2026-01-09 15:26:24', 1, '已支付', 2, 31.00, 20.00, '');
INSERT INTO `orders` VALUES (1767963259458, 1, 1, '2026-01-09 20:54:18', '2026-01-09 20:54:38', 2, '已完成', 2, 48.72, 32.00, '');
INSERT INTO `orders` VALUES (1767965142958, 1, 1, '2026-01-09 21:25:41', '2026-01-09 21:26:40', 2, '已完成', 2, 69.60, 27.00, '');
INSERT INTO `orders` VALUES (1767966389450, 1, 1, '2026-01-09 21:46:27', '2026-01-09 21:46:47', 1, '已支付', 1, 49.90, 5.00, '');
INSERT INTO `orders` VALUES (1767971804946, 1, 2, '2026-01-09 23:16:37', NULL, 3, '已取消', 2, 87.60, 0.00, '');
INSERT INTO `orders` VALUES (1767972234145, 1, 1, '2026-01-09 23:23:53', NULL, 3, '已取消', 1, 20.00, 2.00, '');
INSERT INTO `orders` VALUES (1767973489982, 1, 1, '2026-01-09 23:44:49', NULL, 3, '已取消', 1, 49.90, 5.00, '');
INSERT INTO `orders` VALUES (1768031040604, 1, 1, '2026-01-10 15:43:59', NULL, 3, '已取消', 2, 99.80, 5.00, 'aaa');
INSERT INTO `orders` VALUES (1768039045668, 1, 1, '2026-01-10 17:57:18', NULL, 3, '已取消', 1, 20.00, 2.00, '这是一个订单');
INSERT INTO `orders` VALUES (1768039338918, 1, 1, '2026-01-10 18:02:16', NULL, 3, '已取消', 1, 20.00, 2.00, '这是一个订单');
INSERT INTO `orders` VALUES (1768040381709, 1, 1, '2026-01-10 18:19:40', '2026-01-10 18:30:43', 2, '已完成', 1, 49.90, 5.00, '');
INSERT INTO `orders` VALUES (1768238459425, 31, 1, '2026-01-13 01:20:55', '2026-01-13 01:21:15', 2, '已完成', 1, 20.00, 2.00, '');
INSERT INTO `orders` VALUES (1768283709684, 31, 2, '2026-01-13 13:55:07', NULL, 3, '已取消', 1, 16.90, 0.00, '');
INSERT INTO `orders` VALUES (1768287173146, 31, 1, '2026-01-13 14:52:52', '2026-01-13 14:53:06', 2, '已完成', 3, 80.90, 25.00, '');
INSERT INTO `orders` VALUES (1768292955430, 31, 1, '2026-01-13 16:29:13', NULL, 3, '已取消', 1, 49.90, 5.00, '');
INSERT INTO `orders` VALUES (1768318263234, 1, 6, '2026-01-13 23:31:02', NULL, 3, '已取消', 2, 20.00, 5.00, '');
INSERT INTO `orders` VALUES (1768319887209, 1, 1, '2026-01-13 23:58:06', NULL, 3, '已取消', 1, 49.90, 7.00, '');
INSERT INTO `orders` VALUES (1768319915712, 1, 1, '2026-01-13 23:58:35', NULL, 3, '已取消', 1, 20.00, 2.00, '');
INSERT INTO `orders` VALUES (1768321242138, 1, 1, '2026-01-14 00:20:35', NULL, 3, '已取消', 2, 99.80, 5.00, '这是一个订单备注');
INSERT INTO `orders` VALUES (1768322303050, 1, 1, '2026-01-14 00:38:21', NULL, 3, '已取消', 1, 49.90, 5.00, '');
INSERT INTO `orders` VALUES (1768322554041, 1, 3, '2026-01-14 00:42:32', '2026-01-14 00:43:34', 1, '已支付', 1, 41.31, 0.00, '1111');
INSERT INTO `orders` VALUES (1768322810852, 1, 1, '2026-01-14 00:46:48', '2026-01-14 00:47:12', 2, '已完成', 1, 49.90, 5.00, '');
INSERT INTO `orders` VALUES (1768369357045, 1, 1, '2026-01-14 13:42:36', '2026-01-14 13:43:00', 2, '已完成', 1, 20.00, 2.00, '你好');
INSERT INTO `orders` VALUES (1768369784633, 1, 2, '2026-01-14 13:49:40', NULL, 3, '已取消', 1, 18.90, 0.00, '');
INSERT INTO `orders` VALUES (1768370343829, 1, 1, '2026-01-14 13:59:01', NULL, 3, '已取消', 1, 18.00, 0.00, '');
INSERT INTO `orders` VALUES (1768370847769, 1, 2, '2026-01-14 14:07:27', NULL, 3, '已取消', 1, 16.90, 0.00, '');
INSERT INTO `orders` VALUES (1768375337859, 1, 1, '2026-01-14 15:22:16', '2026-01-14 15:22:28', 2, '已完成', 1, 9.80, 0.00, '222222');

-- ----------------------------
-- Table structure for orders_detail
-- ----------------------------
DROP TABLE IF EXISTS `orders_detail`;
CREATE TABLE `orders_detail`  (
  `odid` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '订单详细id',
  `orderid` bigint(0) DEFAULT NULL COMMENT '订单id',
  `gid` bigint(0) DEFAULT NULL COMMENT '商品id',
  `gdid` bigint(0) DEFAULT NULL COMMENT '商品详细id',
  `gname` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品名称',
  `icon` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品图片',
  `bcontent` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品内容',
  `buyPrice` decimal(16, 2) DEFAULT NULL COMMENT '购买价格',
  `buyNums` int(0) DEFAULT NULL COMMENT '购买数量',
  `receiveName` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '收货人姓名',
  `tel` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '收货人电话',
  `province` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '省份',
  `city` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '城市',
  `district` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '区县',
  `detailAddress` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '详细地址',
  PRIMARY KEY (`odid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '订单详细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders_detail
-- ----------------------------
INSERT INTO `orders_detail` VALUES (1, 1679477844413, 1, 1, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', 'http://8.152.205.127/douyin/goods/xianglasu02.png', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (2, 1679477844413, 2, 5, '碧根果新货500g250g新鲜坚果零食奶油味奶香浓郁长寿干果年货5斤', 'http://8.152.205.127/douyin/goods/bigenguo04.png', '500g奶香味', 34.80, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (3, 1679477844414, 6, 20, '大豆纤维冬被被子保暖儿童丝棉被秋冬学生宿舍被芯春秋被四季通用', 'http://8.152.205.127/douyin/goods/jiafangbei02.png', '180*220cm【4斤 春秋被】不结团 不跑棉/【绅士灰】抑菌大豆被', 50.80, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (4, 1679530160240, 1, 1, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', 'http://8.152.205.127/douyin/goods/xianglasu02.png', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (5, 1679530160240, 2, 5, '碧根果新货500g250g新鲜坚果零食奶油味奶香浓郁长寿干果年货5斤', 'http://8.152.205.127/douyin/goods/bigenguo04.png', '500g奶香味', 34.80, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (6, 1679538169377, 6, 20, '大豆纤维冬被被子保暖儿童丝棉被秋冬学生宿舍被芯春秋被四季通用', 'http://8.152.205.127/douyin/goods/jiafangbei02.png', '180*220cm【4斤 春秋被】不结团 不跑棉/【绅士灰】抑菌大豆被', 50.80, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (7, 1679538169378, 8, 28, '乐乐逗逗童装针织儿童开衫中大童2022秋装新款休闲百搭男童女童毛衣潮', 'http://8.152.205.127/douyin/goods/ledoudoutongzhuang04.png', '驼色/130cm', 89.00, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (8, 1679538797429, 1, 1, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', 'http://8.152.205.127/douyin/goods/xianglasu02.png', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (9, 1679538856306, 1, 3, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', 'http://8.152.205.127/douyin/goods/xianglasu03.png', '【尝鲜半斤】正宗香辣脆250g*1罐', 2.56, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (10, 1679539791748, 6, 21, '大豆纤维冬被被子保暖儿童丝棉被秋冬学生宿舍被芯春秋被四季通用', 'http://8.152.205.127/douyin/goods/jiafangbei03.png', '180*220cm【4斤 春秋被】不结团 不跑棉/【香妃玉】抑菌大豆被', 50.80, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (11, 1679542547005, 7, 22, '白衬衫女长袖职业春秋夏季短袖宽松工作服正装大码工装女装白衬衣', 'http://8.152.205.127/douyin/goods/chenyi01.png', 'F11【短袖】纯白翻领/XL', 16.90, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (12, 1679542774186, 6, 20, '大豆纤维冬被被子保暖儿童丝棉被秋冬学生宿舍被芯春秋被四季通用', 'http://8.152.205.127/douyin/goods/jiafangbei02.png', '180*220cm【4斤 春秋被】不结团 不跑棉/【绅士灰】抑菌大豆被', 50.80, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (21, 1686475307793, 1, 3, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', 'http://8.152.205.127/douyin/goods/xianglasu03.png', '【尝鲜半斤】正宗香辣脆250g*1罐', 2.56, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (24, 1686482638070, 3, 8, '自然开口盐焗开心果原味坚果炒货无漂白散装500g袋装孕妇儿童零食', 'http://8.152.205.127/douyin/goods/kaixinguo03.png', '盐焗开心果特惠5斤', 214.90, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (25, 1686482668546, 3, 8, '自然开口盐焗开心果原味坚果炒货无漂白散装500g袋装孕妇儿童零食', 'http://8.152.205.127/douyin/goods/kaixinguo03.png', '盐焗开心果特惠5斤', 214.90, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (26, 1687253519826, 1, 1, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', 'http://8.152.205.127/douyin/goods/xianglasu02.png', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (27, 1687253519826, 1, 2, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', 'http://8.152.205.127/douyin/goods/xianglasu01.png', '【精品大罐】正宗香辣脆500g*2罐', 24.36, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (29, 1687260344813, 9, 29, '宝宝长袖T恤春秋纯棉衣服小婴儿打底衫男童上衣女童春装儿童童装', 'http://8.152.205.127/douyin/goods/ledoudoutongzhuang05.png', '彩虹条/110cm', 41.31, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (30, 1687260344813, 9, 31, '宝宝长袖T恤春秋纯棉衣服小婴儿打底衫男童上衣女童春装儿童童装', 'http://8.152.205.127/douyin/goods/ledoudoutongzhuang07.png', '白色/120cm', 41.31, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (31, 1687305111200, 1, 2, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', 'http://8.152.205.127/douyin/goods/xianglasu01.png', '【精品大罐】正宗香辣脆500g*2罐', 24.36, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (33, 1687305912775, 7, 23, '白衬衫女长袖职业春秋夏季短袖宽松工作服正装大码工装女装白衬衣', 'http://8.152.205.127/douyin/goods/chenyi02.png', '【长袖】纯白V领/2XL', 18.90, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (34, 1688342829870, 1, 1, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', 'http://8.152.205.127/douyin/goods/xianglasu02.png', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (35, 1688348594596, 8, 25, '乐乐逗逗童装针织儿童开衫中大童2022秋装新款休闲百搭男童女童毛衣潮', 'http://8.152.205.127/douyin/goods/ledoudoutongzhuang01.png', '橄榄绿（80%妈妈的选择)/110cm', 99.00, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (37, 1694682477093, 1, 1, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', 'http://8.152.205.127/douyin/goods/xianglasu02.png', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (38, 1694689885633, 1, 2, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', 'http://8.152.205.127/douyin/goods/xianglasu01.png', '【精品大罐】正宗香辣脆500g*2罐', 24.36, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (39, 1711953695586, 1, 2, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', 'http://8.152.205.127/douyin/goods/xianglasu01.png', '【精品大罐】正宗香辣脆500g*2罐', 24.36, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (40, 1711978854875, 9, 29, '宝宝长袖T恤春秋纯棉衣服小婴儿打底衫男童上衣女童春装儿童童装', 'http://8.152.205.127/douyin/goods/ledoudoutongzhuang05.png', '彩虹条/110cm', 41.31, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (41, 1711978858376, 6, 19, '大豆纤维冬被被子保暖儿童丝棉被秋冬学生宿舍被芯春秋被四季通用', 'http://8.152.205.127/douyin/goods/jiafangbei01.png', '150*200cm【夏凉被 空调被】不结团 不跑棉/【珍珠白】抑菌大豆被', 43.80, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (42, 1711979443585, 7, 22, '白衬衫女长袖职业春秋夏季短袖宽松工作服正装大码工装女装白衬衣', '/static/images/goods/780.jpg', 'F11【短袖】纯白翻领/XL', 16.90, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (43, 1711979443585, 7, 23, '白衬衫女长袖职业春秋夏季短袖宽松工作服正装大码工装女装白衬衣', '/static/images/goods/780.jpg', '【长袖】纯白V领/2XL', 18.90, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (44, 1712059963990, 2, 5, '碧根果新货500g250g新鲜坚果零食奶油味奶香浓郁长寿干果年货5斤', '/static/images/goods/780.jpg', '500g奶香味', 34.80, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (45, 1751501693131, 1, 2, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', '/static/images/goods/780.jpg', '【精品大罐】正宗香辣脆500g*2罐', 24.36, 3, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (46, 1751790734626, 6, 21, '大豆纤维冬被被子保暖儿童丝棉被秋冬学生宿舍被芯春秋被四季通用', '/static/images/goods/780.jpg', '180*220cm【4斤 春秋被】不结团 不跑棉/【香妃玉】抑菌大豆被', 50.80, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (47, 1751790794237, 7, 22, '白衬衫女长袖职业春秋夏季短袖宽松工作服正装大码工装女装白衬衣', '/static/images/goods/780.jpg', 'F11【短袖】纯白翻领/XL', 16.90, 3, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (48, 1751790813003, 1, 1, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', '/static/images/goods/780.jpg', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 3, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (49, 1751790905432, 7, 23, '白衬衫女长袖职业春秋夏季短袖宽松工作服正装大码工装女装白衬衣', '/static/images/goods/780.jpg', '【长袖】纯白V领/2XL', 18.90, 4, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (52, 1766578087472, 1, 1, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', '/static/images/goods/780.jpg', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 6, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (53, 1766578087472, 2, 5, '碧根果新货500g250g新鲜坚果零食奶油味奶香浓郁长寿干果年货5斤', '/static/images/goods/780.jpg', '500g奶香味', 34.80, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (54, 1766578087472, 1, 2, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', '/static/images/goods/780.jpg', '【精品大罐】正宗香辣脆500g*2罐', 24.36, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (56, 1766659536061, 2, 5, '碧根果新货500g250g新鲜坚果零食奶油味奶香浓郁长寿干果年货5斤', '/static/images/goods/780.jpg', '500g奶香味', 34.80, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (57, 1767785500063, 7, 23, '白衬衫女长袖职业春秋夏季短袖宽松工作服正装大码工装女装白衬衣', '/static/images/goods/780.jpg', '【长袖】纯白V领/2XL', 18.90, 4, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (58, 1767785500063, 6, 21, '大豆纤维冬被被子保暖儿童丝棉被秋冬学生宿舍被芯春秋被四季通用', '/static/images/goods/780.jpg', '180*220cm【4斤 春秋被】不结团 不跑棉/【香妃玉】抑菌大豆被', 50.80, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (59, 1767785500063, 6, 19, '大豆纤维冬被被子保暖儿童丝棉被秋冬学生宿舍被芯春秋被四季通用', '/static/images/goods/780.jpg', '150*200cm【夏凉被 空调被】不结团 不跑棉/【珍珠白】抑菌大豆被', 43.80, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (60, 1767785565664, 2, 5, '碧根果新货500g250g新鲜坚果零食奶油味奶香浓郁长寿干果年货5斤', '/static/images/goods/780.jpg', '500g奶香味', 34.80, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (61, 1767785565664, 2, 4, '碧根果新货500g250g新鲜坚果零食奶油味奶香浓郁长寿干果年货5斤', '/static/images/goods/780.jpg', '250g奶香味', 18.00, 3, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (62, 1767785565664, 11, NULL, '拖鞋女夏季室内居家用浴室洗澡防滑宾馆酒店EVA防臭男士拖鞋软底', '/static/images/goods/780.jpg', NULL, NULL, 3, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (63, 1767791112679, 1, 1, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', '/static/images/goods/780.jpg', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 3, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (64, 1767792669924, 7, 22, '白衬衫女长袖职业春秋夏季短袖宽松工作服正装大码工装女装白衬衣', '/static/images/goods/780.jpg', 'F11【短袖】纯白翻领/XL', 16.90, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (65, 1767793469471, 8, 25, '乐乐逗逗童装针织儿童开衫中大童2022秋装新款休闲百搭男童女童毛衣潮', '/static/images/goods/780.jpg', '橄榄绿（80%妈妈的选择)/110cm', 99.00, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (66, 1767793615732, 1, 1, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', '/static/images/goods/780.jpg', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 3, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (67, 1767794452398, 11, NULL, '拖鞋女夏季室内居家用浴室洗澡防滑宾馆酒店EVA防臭男士拖鞋软底', '/static/images/goods/780.jpg', NULL, NULL, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (68, 1767794939383, 11, NULL, '拖鞋女夏季室内居家用浴室洗澡防滑宾馆酒店EVA防臭男士拖鞋软底', '/static/images/goods/780.jpg', NULL, NULL, 3, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (69, 1767795063874, 4, 12, '高档纯棉四件套轻奢高级感100全棉南通家纺床单被套床上用品床笠', '/static/images/goods/780.jpg', '1.5m床单款四件套(被套2x2.3m)/莫兰迪高贵绿', 358.00, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (70, 1767796013858, 11, NULL, '拖鞋女夏季室内居家用浴室洗澡防滑宾馆酒店EVA防臭男士拖鞋软底', '/static/images/goods/780.jpg', NULL, NULL, 5, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (71, 1767796013858, 1, 1, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', '/static/images/goods/780.jpg', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (72, 1767796016064, 11, NULL, '拖鞋女夏季室内居家用浴室洗澡防滑宾馆酒店EVA防臭男士拖鞋软底', '/static/images/goods/780.jpg', NULL, NULL, 5, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (73, 1767796089814, 11, NULL, '拖鞋女夏季室内居家用浴室洗澡防滑宾馆酒店EVA防臭男士拖鞋软底', '/static/images/goods/780.jpg', NULL, NULL, 5, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (74, 1767796120778, 1, 1, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', '/static/images/goods/780.jpg', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (76, 1767797083112, 10, NULL, '品牌散装碎海苔500g拌饭即食切丝海苔条250克章鱼小丸子 零食', '/static/images/goods/780.jpg', NULL, NULL, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (77, 1767799760151, 10, NULL, '品牌散装碎海苔500g拌饭即食切丝海苔条250克章鱼小丸子 零食', '/static/images/goods/780.jpg', NULL, NULL, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (78, 1767799760151, 3, 6, '自然开口盐焗开心果原味坚果炒货无漂白散装500g袋装孕妇儿童零食', '/static/images/goods/780.jpg', '盐焗开心果一斤', 43.90, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (79, 1767802301880, 2, 4, '碧根果新货500g250g新鲜坚果零食奶油味奶香浓郁长寿干果年货5斤', '/static/images/goods/780.jpg', '250g奶香味', 18.00, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (80, 1767859869321, 10, NULL, '品牌散装碎海苔500g拌饭即食切丝海苔条250克章鱼小丸子 零食', '/static/images/goods/780.jpg', NULL, NULL, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (81, 1767859869321, 1, 1, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', '/static/images/goods/780.jpg', '【精品大罐】正宗香辣脆500g*1罐', 8.76, 3, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (82, 1767859882712, 10, NULL, '品牌散装碎海苔500g拌饭即食切丝海苔条250克章鱼小丸子 零食', '/static/images/goods/780.jpg', NULL, NULL, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (83, 1767863121666, 10, 34, '品牌散装碎海苔500g拌饭即食切丝海苔条250克章鱼小丸子 零食', '/static/images/goods/1.jpg', '250g', 11.10, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (84, 1767863148990, 11, 35, '拖鞋女夏季室内居家用浴室洗澡防滑宾馆酒店EVA防臭男士拖鞋软底', '/static/images/goods/1.jpg', '43码/26.5cm', 9.80, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (85, 1767863294634, 11, 36, '拖鞋女夏季室内居家用浴室洗澡防滑宾馆酒店EVA防臭男士拖鞋软底', '/static/images/goods/1.jpg', '42码/26cm', 9.80, 3, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (86, 1767884219755, 16, 38, '凉鞋', '', '44码/', 20.00, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (87, 1767885990946, 17, 40, '牛奶', '', '60瓶/', 49.90, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (88, 1767943565991, 18, 42, '商品二', '', '1/', 15.50, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (89, 1767963259458, 1, 2, '香辣酥花生500g罐装香辣脆下酒零食小吃干吃红辣椒段芝麻香脆椒', '/static/images/goods/780.jpg', '【精品大罐】正宗香辣脆500g*2罐', 24.36, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (90, 1767965142958, 2, 5, '碧根果新货500g250g新鲜坚果零食奶油味奶香浓郁长寿干果年货5斤', '/static/images/goods/780.jpg', '500g奶香味', 34.80, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (91, 1767966389450, 17, 40, '牛奶', 'http://localhost:8089/9fcafe07-3a4f-4133-af99-67456c24c560.png', '60瓶/', 49.90, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (92, 1767971804946, 6, 19, '大豆纤维冬被被子保暖儿童丝棉被秋冬学生宿舍被芯春秋被四季通用', '/static/images/goods/780.jpg', '150*200cm【夏凉被 空调被】不结团 不跑棉/【珍珠白】抑菌大豆被', 43.80, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (93, 1767972234145, 16, 38, '凉鞋', 'http://localhost:8089/72e0eefc-dd7b-4d47-b2e7-3550e29cd2e7.png', '44码/26cm', 20.00, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (94, 1767973489982, 17, 40, '牛奶', 'http://localhost:8089/a0351831-d4d8-4c02-9db9-1e4610ee0f70.png', '60瓶/', 49.90, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (95, 1768031040604, 17, 40, '牛奶', 'http://localhost:8089/a0351831-d4d8-4c02-9db9-1e4610ee0f70.png', '60瓶/', 49.90, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (96, 1768039045668, 16, 38, '凉鞋', 'http://localhost:8089/72e0eefc-dd7b-4d47-b2e7-3550e29cd2e7.png', '44码/26cm', 20.00, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (97, 1768039338918, 16, 38, '凉鞋', 'http://localhost:8089/72e0eefc-dd7b-4d47-b2e7-3550e29cd2e7.png', '44码/26cm', 20.00, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (98, 1768040381709, 17, 40, '牛奶', 'http://localhost:8089/a0351831-d4d8-4c02-9db9-1e4610ee0f70.png', '60瓶/', 49.90, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (100, 1768238459425, 16, 38, '凉鞋', 'http://localhost:8089/72e0eefc-dd7b-4d47-b2e7-3550e29cd2e7.png', '44码/26cm', 20.00, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (101, 1768283709684, 7, 22, '白衬衫女长袖职业春秋夏季短袖宽松工作服正装大码工装女装白衬衣', '/static/images/goods/780.jpg', 'F11【短袖】纯白翻领/XL', 16.90, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (102, 1768287173146, 18, 42, '商品二', '', '1/', 15.50, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (103, 1768287173146, 17, 40, '牛奶', 'http://localhost:8089/a0351831-d4d8-4c02-9db9-1e4610ee0f70.png', '60瓶/', 49.90, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (104, 1768292955430, 17, 40, '牛奶', 'http://localhost:8089/a0351831-d4d8-4c02-9db9-1e4610ee0f70.png', '60瓶/', 49.90, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (105, 1768318263234, 19, 43, '商品9', 'http://localhost:8089/47fd1d20-6e13-4afc-b98b-eff7707d9e46.png', '9-2/', 10.00, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (106, 1768319887209, 17, 40, '牛奶', 'http://localhost:8089/a0351831-d4d8-4c02-9db9-1e4610ee0f70.png', '60瓶/', 49.90, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (107, 1768319915712, 16, 38, '凉鞋', 'http://localhost:8089/72e0eefc-dd7b-4d47-b2e7-3550e29cd2e7.png', '44码/26cm', 20.00, 1, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (108, 1768321242138, 17, 40, '牛奶', 'http://localhost:8089/a0351831-d4d8-4c02-9db9-1e4610ee0f70.png', '60瓶/', 49.90, 2, '', '', '', '', '', '');
INSERT INTO `orders_detail` VALUES (109, 1768322303050, 17, 40, '牛奶', 'http://localhost:8089/a0351831-d4d8-4c02-9db9-1e4610ee0f70.png', '60瓶/', 49.90, 1, '贤', '15120693132', 'c', 'c', 'c', 'c');
INSERT INTO `orders_detail` VALUES (110, 1768322554041, 9, 29, '宝宝长袖T恤春秋纯棉衣服小婴儿打底衫男童上衣女童春装儿童童装', '/static/images/goods/780.jpg', '彩虹条/110cm', 41.31, 1, '韩信', '15632589985', '内蒙古', '画的', '哈哈', '111');
INSERT INTO `orders_detail` VALUES (111, 1768322810852, 17, 40, '牛奶', 'http://localhost:8089/a0351831-d4d8-4c02-9db9-1e4610ee0f70.png', '60瓶/', 49.90, 1, '韩信', '15632589985', '内蒙古', '画的', '哈哈', '111');
INSERT INTO `orders_detail` VALUES (112, 1768369357045, 16, 39, '凉鞋', 'http://localhost:8089/280c4620-a92b-4a38-a694-7472360b8142.png', '40码/25cm', 20.00, 1, '5555', '18522659845', '内蒙古', '乌兰察布市', '集宁区', '仁德小区六单元二号楼203');
INSERT INTO `orders_detail` VALUES (113, 1768369784633, 7, 24, '白衬衫女长袖职业春秋夏季短袖宽松工作服正装大码工装女装白衬衣', 'http://localhost:8089/90d0edaa-56e5-43ba-bc72-4fd95a41a2be.webp', '【长袖】纯白翻领/XL', 18.90, 1, '韩信', '15632589985', '内蒙古', '画的', '哈哈', '111');
INSERT INTO `orders_detail` VALUES (114, 1768370343829, 2, 4, '碧根果新货500g250g新鲜坚果零食奶油味奶香浓郁长寿干果年货5斤', 'http://localhost:8089/25ba57f1-75c1-4671-8ec5-7475f13569a1.png', '250g奶香味', 18.00, 1, '韩信', '15632589985', '内蒙古', '画的', '哈哈', '111');
INSERT INTO `orders_detail` VALUES (115, 1768370847769, 7, 22, '白衬衫女长袖职业春秋夏季短袖宽松工作服正装大码工装女装白衬衣', 'http://localhost:8089/036b9d75-b1b7-4e82-acb6-2d1dfaad267f.jpg', 'F11【短袖】纯白翻领/XL', 16.90, 1, '5555', '18522659845', '内蒙古', '乌兰察布市', '集宁区', '仁德小区六单元二号楼203');
INSERT INTO `orders_detail` VALUES (116, 1768375337859, 11, 35, '拖鞋女夏季室内居家用浴室洗澡防滑宾馆酒店EVA防臭男士拖鞋软底', 'http://localhost:8089/bf5e6213-51ab-4ad7-9629-a278cf5e80a2.png', '43码/26.5cm', 9.80, 1, '韩信', '15632589985', '内蒙古', '画的', '哈哈', '111');

-- ----------------------------
-- Table structure for receiveaddress
-- ----------------------------
DROP TABLE IF EXISTS `receiveaddress`;
CREATE TABLE `receiveaddress`  (
  `raid` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '收货地址id',
  `receiveName` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '收货人姓名',
  `tel` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '电话',
  `province` varchar(127) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '所在地区(省市区街道)',
  `detailaddress` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '详细地址',
  `cid` bigint(0) DEFAULT NULL COMMENT '客户id',
  `isdefault` int(0) DEFAULT NULL COMMENT '是否为默认地址：0.否  1.是',
  PRIMARY KEY (`raid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '收货地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for safeguard
-- ----------------------------
DROP TABLE IF EXISTS `safeguard`;
CREATE TABLE `safeguard`  (
  `sgid` int(0) NOT NULL AUTO_INCREMENT COMMENT '商品保障id',
  `sgname` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '保障条款名称',
  `sgcontent` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '保障条款内容',
  `sgicon` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '保障条款图标',
  PRIMARY KEY (`sgid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '商品保障' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of safeguard
-- ----------------------------
INSERT INTO `safeguard` VALUES (1, '晚发即赔', '若发货超时或物流异常，将为你补贴至少3元无门槛优惠劵（特殊商品、大促活动及不可抗力因素除外）', '/static/images/other/OIP-C.jpg');
INSERT INTO `safeguard` VALUES (2, '假一赔四', '若商品鉴定为假货，商家支持假一赔四，且承担商品的鉴定费和退回的运费', '/static/images/other/OIP-C.jpg');
INSERT INTO `safeguard` VALUES (3, '坏损包退', '签收后72小时内发现商品破损、腐烂、变质 (食品酒饮、奶粉、生鲜绿植为24小时)，申请退款，商家将在24小时内处理', '/static/images/other/OIP-C.jpg');
INSERT INTO `safeguard` VALUES (4, '运费险', '退货或换货，均可享受一定金额的运费补贴 (全球购商品不支持换货)', '/static/images/other/OIP-C.jpg');
INSERT INTO `safeguard` VALUES (5, '极速退款', '满足相应条件时，待发货状态下，提交退款申请将免审核立即退款', '/static/images/other/OIP-C.jpg');
INSERT INTO `safeguard` VALUES (6, '7天无理由退货', '7天无理由退货\n符合相应条件时，消费者可申请7天无理由退货', '/static/images/other/OIP-C.jpg');
INSERT INTO `safeguard` VALUES (7, '消费者保障服务', '如商品有质量问题、描述不符等情况，你有权申请退货退款', '/static/images/other/OIP-C.jpg');
INSERT INTO `safeguard` VALUES (8, '商家资质', '查看商家详细资质信息', '/static/images/other/OIP-C.jpg');

-- ----------------------------
-- Table structure for shop_fans
-- ----------------------------
DROP TABLE IF EXISTS `shop_fans`;
CREATE TABLE `shop_fans`  (
  `fid` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `cid` int(0) NOT NULL COMMENT '客户ID',
  `sid` int(0) NOT NULL COMMENT '店铺ID',
  `focus_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
  `status` tinyint(0) NOT NULL DEFAULT 1 COMMENT '关注状态：1=已关注，0=已取消',
  PRIMARY KEY (`fid`) USING BTREE,
  UNIQUE INDEX `uk_cid_sid`(`cid`, `sid`) USING BTREE,
  INDEX `idx_sid`(`sid`) USING BTREE,
  INDEX `idx_cid`(`cid`) USING BTREE,
  INDEX `idx_sid_status`(`sid`, `status`) USING BTREE,
  CONSTRAINT `fk_shop_fans_cid` FOREIGN KEY (`cid`) REFERENCES `users` (`cid`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_shop_fans_sid` FOREIGN KEY (`sid`) REFERENCES `shops` (`sid`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '店铺粉丝表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shop_fans
-- ----------------------------
INSERT INTO `shop_fans` VALUES (1, 1, 1, '2026-01-13 21:39:53', 1);
INSERT INTO `shop_fans` VALUES (2, 1, 2, '2026-01-13 21:40:27', 1);

-- ----------------------------
-- Table structure for shops
-- ----------------------------
DROP TABLE IF EXISTS `shops`;
CREATE TABLE `shops`  (
  `sid` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sname` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '门店名称',
  `slogo` varchar(300) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '店铺logo',
  `province` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '所在省市区（县）',
  `saddress` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '详细地址',
  `phone` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '联系电话',
  `xgade` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '信誉等级(每月16号更新)：青铜(Lv1---Lv5)、白银(Lv1---Lv5)、黄金(Lv1---Lv5)、抖钻(Lv1---Lv5)',
  `businesslicense` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商家资质图片--营业执照',
  `foodBusinessLicense` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商家资质图片--食品经营许可证',
  `otherBusinessLicense` varchar(600) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '其他商家资质图片,多个用;分号分隔',
  `overallExperience` decimal(4, 2) DEFAULT NULL COMMENT '综合体验',
  `goodsExperience` decimal(4, 2) DEFAULT NULL COMMENT '商品体验',
  `serviceExperience` decimal(4, 2) DEFAULT NULL COMMENT '服务体验',
  `logisticsExperience` decimal(4, 2) DEFAULT NULL COMMENT '物流体验',
  `ontime` datetime(0) DEFAULT NULL COMMENT '上线时间',
  `sstatus` int(0) DEFAULT NULL COMMENT '状态：0.禁用 1正常 2.休息 ',
  PRIMARY KEY (`sid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '门店表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shops
-- ----------------------------
INSERT INTO `shops` VALUES (1, '美滋滋店铺1', 'http://localhost:8089/68c91f55-0c5f-4215-977c-57ab29e9fd9c.png', '河北', '河北', '15120693132', '青铜Lv2', 'http://localhost:8089/ea2d5509-ade4-4877-9fe5-9c1ffd58e6aa.png', 'http://localhost:8089/2708113d-7a8d-4e94-84bd-72468f34ec8e.jpg', 'http://localhost:8089/6a4ed253-a48a-4df4-a8ab-fb868a0844ce.png', NULL, NULL, NULL, NULL, '2022-09-23 00:00:00', 1);
INSERT INTO `shops` VALUES (2, '红红儿', 'http://localhost:8089/15fa25d4-ea00-43ca-bb4b-76c58bb20cda.png', '海南', '海南', '14785236954', '白银Lv1', 'http://localhost:8089/3573ebd3-5946-494a-8dac-bf71fa2f2c0e.png', 'http://localhost:8089/9bd5c12f-9579-412c-9bb2-051c5c656617.png', 'http://localhost:8089/76b941e0-0eee-4b46-86cb-e39d715ed659.png', NULL, NULL, NULL, NULL, '2020-01-12 00:00:00', 1);
INSERT INTO `shops` VALUES (3, '乐乐逗逗', NULL, NULL, NULL, NULL, '白银Lv2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2021-03-21 00:00:00', 1);
INSERT INTO `shops` VALUES (4, '好好吃零食铺', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `shops` VALUES (5, '贤的店铺', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `shops` VALUES (6, '美滋滋店铺', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `userid` int(0) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '用户名',
  `userpwd` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '密码',
  `email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'Email',
  `phone` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '电话',
  `address` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '地址',
  `sid` int(0) DEFAULT NULL COMMENT '所属门店id',
  `ustatus` int(0) DEFAULT NULL COMMENT '状态：0.禁用 1.正常 10.超级管理员',
  `lastlogintime` datetime(0) DEFAULT NULL COMMENT '最后一次登录时间',
  `role` int(0) DEFAULT NULL,
  `shopName` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`userid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, '1', '1', NULL, NULL, NULL, 1, 1, NULL, 1, NULL);
INSERT INTO `t_user` VALUES (2, '2', '2', NULL, '15120693132', '邯郸', 2, 1, '2026-01-07 21:08:14', 1, NULL);
INSERT INTO `t_user` VALUES (3, '3', '3', NULL, NULL, NULL, 3, 1, NULL, 1, NULL);
INSERT INTO `t_user` VALUES (4, '5', '5', NULL, NULL, NULL, 4, 1, '2026-01-08 20:19:11', 1, '好好吃零食铺');
INSERT INTO `t_user` VALUES (5, '6', '6', NULL, NULL, NULL, 5, 1, '2026-01-13 23:25:06', 1, '贤的店铺');
INSERT INTO `t_user` VALUES (6, '9', '9', NULL, NULL, NULL, 6, 1, '2026-01-13 23:25:39', 1, '美滋滋店铺');

-- ----------------------------
-- Table structure for user_address
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人电话',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '省',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '市',
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '区/县',
  `detail` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详细地址',
  `is_default` tinyint(1) DEFAULT 0 COMMENT '是否默认地址 0否 1是',
  `create_time` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户收货地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_address
-- ----------------------------
INSERT INTO `user_address` VALUES (3, 1, '5555', '18522659845', '内蒙古', '乌兰察布市', '集宁区', '仁德小区六单元二号楼203', 0, '2026-01-06 17:17:21', '2026-01-13 23:37:32');
INSERT INTO `user_address` VALUES (4, 1, '44', '15633256988', '江苏省', 'a市', '邯郸县', '六单元', 0, '2026-01-06 18:12:31', '2026-01-13 23:44:11');
INSERT INTO `user_address` VALUES (5, 1, '韩信', '15632589985', '内蒙古', '画的', '哈哈', '111', 1, '2026-01-06 19:16:39', '2026-01-13 23:44:55');
INSERT INTO `user_address` VALUES (6, 1, '1', '15698471128', 'li', 'l', 'k', '鸡你太美ll', 0, '2026-01-06 19:36:24', '2026-01-12 19:36:32');
INSERT INTO `user_address` VALUES (7, 8, 'sun', '15632559815', '内蒙古 ', '乌兰察布', '集宁', '仁德小区', 0, '2026-01-06 22:21:28', '2026-01-06 22:21:28');
INSERT INTO `user_address` VALUES (8, 8, '宁', '14456225885', '浙江', '温州', '皮革厂', '长江', 1, '2026-01-06 22:22:05', '2026-01-06 22:22:08');
INSERT INTO `user_address` VALUES (9, 16, 'sun', '15266957784', 'beijin', 'handan', 'daming', 'aaa', 1, '2026-01-09 20:56:00', '2026-01-09 20:56:06');
INSERT INTO `user_address` VALUES (11, 1, '1', '15120693132', 'a', 'a', 'a', 'a', 0, '2026-01-12 18:13:29', '2026-01-12 19:42:54');
INSERT INTO `user_address` VALUES (12, 1, '2', '15120393132', 'b', 'b', 'b', 'b', 0, '2026-01-12 18:15:07', '2026-01-13 23:44:55');
INSERT INTO `user_address` VALUES (13, 31, 'a', '15120693132', 'a', 'a', 'a', 'a', 1, '2026-01-13 01:21:51', '2026-01-13 01:21:55');
INSERT INTO `user_address` VALUES (14, 1, '贤', '15120693132', 'c', 'c', 'c', 'c', 0, '2026-01-13 23:58:27', '2026-01-13 23:58:27');

-- ----------------------------
-- Table structure for user_signin
-- ----------------------------
DROP TABLE IF EXISTS `user_signin`;
CREATE TABLE `user_signin`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `signin_date` date NOT NULL COMMENT '签到日期',
  `signin_time` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  `continuous_days` int(0) DEFAULT 1 COMMENT '连续签到天数',
  `total_days` int(0) DEFAULT 1 COMMENT '累计签到天数',
  `created_at` datetime(0) DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_date`(`user_id`, `signin_date`) USING BTREE COMMENT '用户每日签到唯一索引',
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_signin_date`(`signin_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户签到记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_signin
-- ----------------------------
INSERT INTO `user_signin` VALUES (1, 1, '2026-01-10', '2026-01-10 16:04:14', 99, 10, NULL);
INSERT INTO `user_signin` VALUES (2, 17, '2026-01-10', '2026-01-10 17:03:15', 1, 3, NULL);
INSERT INTO `user_signin` VALUES (3, 25, '2026-01-10', '2026-01-10 17:20:45', 20, 20, NULL);
INSERT INTO `user_signin` VALUES (4, 18, '2026-01-10', '2026-01-10 17:22:20', 1, 30, NULL);
INSERT INTO `user_signin` VALUES (5, 11, '2026-01-10', '2026-01-10 19:22:34', 1, 1, NULL);
INSERT INTO `user_signin` VALUES (6, 2, '2026-01-10', '2026-01-10 20:58:04', 1, 1, '2026-01-10 20:58:04');
INSERT INTO `user_signin` VALUES (7, 4, '2026-01-10', '2026-01-10 21:04:38', 1, 1, '2026-01-10 21:04:37');
INSERT INTO `user_signin` VALUES (8, 26, '2026-01-10', '2026-01-10 21:21:37', 1, 1, '2026-01-10 21:21:37');
INSERT INTO `user_signin` VALUES (9, 28, '2026-01-10', '2026-01-10 21:27:11', 1, 1, '2026-01-10 21:27:10');
INSERT INTO `user_signin` VALUES (10, 1, '2026-01-13', '2026-01-13 23:31:46', 1, 11, '2026-01-13 23:31:45');
INSERT INTO `user_signin` VALUES (11, 1, '2026-01-14', '2026-01-14 00:45:09', 2, 12, '2026-01-14 00:45:09');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `cid` int(0) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `avatar_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `register_time` datetime(0) DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime(0) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `last_signin_date` date DEFAULT NULL COMMENT '最后签到日期',
  `continuous_signin_days` int(0) DEFAULT 0 COMMENT '连续签到天数',
  `total_signin_days` int(0) DEFAULT 0 COMMENT '累计签到天数',
  PRIMARY KEY (`cid`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `phone`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, '1', '111aaa', '15120693136', '366aa@163.com', '1', '/uploads/avatars/图 3-1 功能结构设计图.jpg', '2026-01-06 18:54:37', '2026-01-12 17:15:20', '2026-01-10', 0, 0);
INSERT INTO `users` VALUES (2, '33', '1', '15366598825', '1', '1', NULL, '2026-01-06 18:59:32', '2026-01-06 18:59:32', NULL, 0, 0);
INSERT INTO `users` VALUES (3, 'test_1767707215082', '123456', '13800138000', '1256384957@qq.com', NULL, NULL, '2026-01-06 21:46:58', '2026-01-08 23:06:59', NULL, 0, 0);
INSERT INTO `users` VALUES (4, '111', '111111', '15233695589', '', NULL, '/uploads/avatars/收货地址.png', '2026-01-06 21:47:32', '2026-01-10 21:12:32', NULL, 0, 0);
INSERT INTO `users` VALUES (5, '333', '333333', '15633956625', '', NULL, NULL, '2026-01-06 21:49:50', '2026-01-06 21:49:50', NULL, 0, 0);
INSERT INTO `users` VALUES (6, '999', '999999', '15633254485', '', NULL, NULL, '2026-01-06 21:50:53', '2026-01-06 21:50:53', NULL, 0, 0);
INSERT INTO `users` VALUES (7, '155', '000000', '15233659958', '', NULL, NULL, '2026-01-06 22:05:19', '2026-01-06 22:05:19', NULL, 0, 0);
INSERT INTO `users` VALUES (8, '197', '111111', '15233669985', '', NULL, NULL, '2026-01-06 22:11:41', '2026-01-06 22:11:41', NULL, 0, 0);
INSERT INTO `users` VALUES (9, '200', '000000', '15233659981', '', NULL, NULL, '2026-01-06 22:12:29', '2026-01-06 22:12:29', NULL, 0, 0);
INSERT INTO `users` VALUES (10, '565', '333333', '15233698852', '', NULL, NULL, '2026-01-06 22:16:15', '2026-01-06 22:16:15', NULL, 0, 0);
INSERT INTO `users` VALUES (11, '张飞哥', '1', '15266589952', '', NULL, '/uploads/avatars/微信图片_20260107000015.jpg', '2026-01-06 22:23:47', '2026-01-10 19:15:48', NULL, 0, 0);
INSERT INTO `users` VALUES (12, '3', '3', '15233639985', '1526983320@qq.com', NULL, '/uploads/avatars/2025-12-24-18-45-08.045西红柿炒土豆.jpg', '2026-01-08 19:50:20', '2026-01-09 17:48:15', NULL, 0, 0);
INSERT INTO `users` VALUES (13, '559', '111111kk', '14578897765', '178@qq.com', '', '/uploads/avatars/微信图片_20260107000015.jpg', '2026-01-09 17:50:05', '2026-01-09 18:06:06', NULL, 0, 0);
INSERT INTO `users` VALUES (14, 'www', 'wwwwww', '17899087765', '', NULL, NULL, '2026-01-09 18:31:57', '2026-01-09 18:31:57', NULL, 0, 0);
INSERT INTO `users` VALUES (15, 'www1', 'qqqqqqq', '15566359585', '', NULL, NULL, '2026-01-09 20:07:43', '2026-01-09 20:07:43', NULL, 0, 0);
INSERT INTO `users` VALUES (16, 'wwww', '1', '15233695845', '', NULL, NULL, '2026-01-09 20:51:20', '2026-01-09 20:56:46', NULL, 0, 0);
INSERT INTO `users` VALUES (17, 'q', 'q', '15233652258', '', NULL, NULL, '2026-01-09 20:58:11', '2026-01-10 16:59:31', NULL, 0, 0);
INSERT INTO `users` VALUES (18, '0', 'w', '15233695586', '1254@qq.com', '', '', '2026-01-09 20:59:23', '2026-01-10 17:44:55', NULL, 0, 0);
INSERT INTO `users` VALUES (19, '000000', '111111pp', '15244785633', '', NULL, NULL, '2026-01-09 21:10:06', '2026-01-09 21:10:06', NULL, 0, 0);
INSERT INTO `users` VALUES (22, '1111111', '111111www', '15863695221', '', NULL, NULL, '2026-01-10 16:53:05', '2026-01-10 16:53:05', NULL, 0, 0);
INSERT INTO `users` VALUES (23, '1111100', '111111ppp', '15636695521', '', NULL, NULL, '2026-01-10 16:55:07', '2026-01-10 16:55:07', NULL, 0, 0);
INSERT INTO `users` VALUES (24, '2', '2', '15120693139', '', NULL, NULL, '2026-01-10 16:57:16', '2026-01-10 18:45:28', NULL, 0, 0);
INSERT INTO `users` VALUES (25, '333000', '333000aa', '15420033652', '', NULL, NULL, '2026-01-10 17:19:40', '2026-01-10 17:19:40', NULL, 0, 0);
INSERT INTO `users` VALUES (26, 'qwq', '111222qq', '15233695584', '', NULL, NULL, '2026-01-10 21:21:28', '2026-01-10 21:21:28', NULL, 0, 0);
INSERT INTO `users` VALUES (27, 'wew', '111111pp', '15233655589', '', NULL, NULL, '2026-01-10 21:24:29', '2026-01-10 21:24:29', NULL, 0, 0);
INSERT INTO `users` VALUES (28, '100', '111222pp', '17852263325', '11111@qq.com', '', '/uploads/avatars/logo.jpg', '2026-01-10 21:26:53', '2026-01-10 21:29:37', NULL, 0, 0);
INSERT INTO `users` VALUES (29, 'newuser123', '123456', '13800138001', 'test@example.com', '??????', NULL, '2026-01-12 14:18:27', '2026-01-12 14:19:51', NULL, 0, 0);
INSERT INTO `users` VALUES (30, 'xian', '12345a', '15120693132', '', NULL, '/uploads/avatars/WebP 是什么及优势.png', '2026-01-12 22:30:21', '2026-01-12 22:30:49', NULL, 0, 0);
INSERT INTO `users` VALUES (31, 'aaa', '111aaa', '15236945223', '', NULL, '/uploads/avatars/WebP 是什么及优势.png', '2026-01-12 22:46:16', '2026-01-13 00:35:24', NULL, 0, 0);

SET FOREIGN_KEY_CHECKS = 1;
