package com.xuejiaao.foodapp.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuejiaao.foodapp.entity.SigninRecord;
import com.xuejiaao.foodapp.mapper.SigninRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class SigninService {

    @Autowired
    private SigninRecordMapper signinRecordMapper;

    // ================ 主要业务方法 ================

    /**
     * 获取用户签到信息
     */
    public Map<String, Object> getSigninInfo(Integer userId) {
        log.info("【获取签到信息】开始，用户ID: {}", userId);
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 检查今天是否已签到
            boolean todaySigned = checkTodaySignedInternal(userId);
            log.info("今日签到状态: {}", todaySigned ? "已签到" : "未签到");

            // 2. 获取最后一条签到记录
            SigninRecord lastRecord = signinRecordMapper.getLastSigninRecord(userId);

            // 3. 计算总天数（从记录的total_days字段读取）
            Integer totalDays = 0;
            if (lastRecord != null && lastRecord.getTotalDays() != null) {
                totalDays = lastRecord.getTotalDays();
            }
            log.info("总签到天数: {}", totalDays);

            // 4. 获取今日签到时间
            String signinTime = "";
            if (todaySigned) {
                SigninRecord todayRecord = signinRecordMapper.getTodaySigninRecord(userId);
                if (todayRecord != null && todayRecord.getSigninTime() != null) {
                    signinTime = todayRecord.getSigninTime()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
            }

            // 5. 获取最近7天签到记录
            List<SigninRecord> recentRecords = signinRecordMapper.getRecentSignins(userId, 7);
            List<String> recentSignins = new ArrayList<>();
            if (recentRecords != null) {
                for (SigninRecord record : recentRecords) {
                    if (record.getSigninDate() != null) {
                        recentSignins.add(record.getSigninDate().toString());
                    }
                }
            }

            // 6. 计算连续签到天数（从数据库查询）
            Integer continuousDays = calculateContinuousDays(userId);
            log.info("连续签到天数: {}", continuousDays);

            // 7. 获取最大连续签到天数
            Integer maxContinuousDays = signinRecordMapper.getMaxContinuousDays(userId);
            if (maxContinuousDays == null) maxContinuousDays = 0;

            // 8. 获取本月签到天数
            Integer monthSigninCount = signinRecordMapper.getCurrentMonthSigninCount(userId);
            if (monthSigninCount == null) monthSigninCount = 0;

            // 9. 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("todaySigned", todaySigned);
            data.put("totalDays", totalDays);
            data.put("continuousDays", continuousDays);
            data.put("maxContinuousDays", maxContinuousDays);
            data.put("monthSigninCount", monthSigninCount);
            data.put("signinTime", signinTime);
            data.put("recentSignins", recentSignins);
            data.put("userId", userId);

            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", data);

            log.info("【获取签到信息】成功，总天数: {}, 连续天数: {}, 今日已签: {}",
                    totalDays, continuousDays, todaySigned);

        } catch (Exception e) {
            log.error("【获取签到信息】异常，用户ID: {}", userId, e);
            result.put("code", 500);
            result.put("message", "获取签到信息失败: " + e.getMessage());
            result.put("data", createDefaultSigninData(userId));
        }

        return result;
    }

    /**
     * 执行签到 - 修复版（只插入数据库存在的字段）
     */
    @Transactional
    public Map<String, Object> signin(Integer userId) {
        log.info("【执行签到】开始，用户ID: {}", userId);
        Map<String, Object> result = new HashMap<>();

        try {
            LocalDate today = LocalDate.now();
            LocalDateTime now = LocalDateTime.now();

            // 1. 检查今天是否已签到
            Integer check = signinRecordMapper.checkTodaySigned(userId);
            if (check != null && check > 0) {
                log.warn("用户今日已签到，用户ID: {}", userId);
                result.put("code", 400);
                result.put("message", "今天已经签到过了");
                return result;
            }

            // 2. 获取最后一条签到记录
            SigninRecord lastRecord = signinRecordMapper.getLastSigninRecord(userId);

            // 3. 计算连续天数
            int continuousDays = 1;
            if (lastRecord != null) {
                LocalDate lastDate = lastRecord.getSigninDate();
                LocalDate yesterday = today.minusDays(1);

                if (lastDate.equals(yesterday)) {
                    continuousDays = lastRecord.getContinuousDays() + 1;
                }
            }

            // 4. 计算总天数
            int totalDays = 1;
            if (lastRecord != null && lastRecord.getTotalDays() != null) {
                totalDays = lastRecord.getTotalDays() + 1;
            }

            // 5. 创建签到记录（只设置数据库存在的字段）
            SigninRecord newRecord = new SigninRecord();
            newRecord.setUserId(userId);
            newRecord.setSigninDate(today);
            newRecord.setSigninTime(now);
            newRecord.setContinuousDays(continuousDays);
            newRecord.setTotalDays(totalDays);

            // 重要：不要设置created_at，让数据库自动处理（如果是TIMESTAMP DEFAULT CURRENT_TIMESTAMP）
            // 如果数据库没有默认值，这里设置为null
            newRecord.setCreatedAt(null);

            // 6. 插入记录
            int insertResult = signinRecordMapper.insertSimple(newRecord);
            if (insertResult <= 0) {
                throw new RuntimeException("插入签到记录失败");
            }

            // 7. 获取本月签到天数
            Integer monthSigninCount = signinRecordMapper.getCurrentMonthSigninCount(userId);
            if (monthSigninCount == null) monthSigninCount = 0;

            // 8. 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("todaySigned", true);
            data.put("totalDays", totalDays);
            data.put("continuousDays", continuousDays);
            data.put("monthSigninCount", monthSigninCount);
            data.put("signinTime", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            data.put("userId", userId);

            result.put("code", 200);
            result.put("message", "签到成功");
            result.put("data", data);

            log.info("【执行签到】成功，用户ID: {}, 总天数: {}, 连续天数: {}",
                    userId, totalDays, continuousDays);

        } catch (Exception e) {
            log.error("【执行签到】失败，用户ID: {}", userId, e);
            result.put("code", 500);
            result.put("message", "签到失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 获取用户签到历史
     */
    public Map<String, Object> getSigninHistory(Integer userId, Integer limit) {
        log.info("【获取签到历史】开始，用户ID: {}, 限制: {}", userId, limit);
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取签到记录
            List<SigninRecord> records = signinRecordMapper.getRecentSignins(userId, limit);

            // 转换格式
            List<Map<String, Object>> history = new ArrayList<>();
            for (SigninRecord record : records) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", record.getId());
                item.put("date", record.getSigninDate().toString());
                item.put("time", record.getSigninTime() != null ?
                        record.getSigninTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "");
                item.put("continuousDays", record.getContinuousDays());
                item.put("totalDays", record.getTotalDays());
                item.put("formattedDate", formatSigninDate(record.getSigninDate()));
                history.add(item);
            }

            // 获取统计数据
            SigninRecord lastRecord = signinRecordMapper.getLastSigninRecord(userId);
            Integer totalDays = lastRecord != null && lastRecord.getTotalDays() != null ?
                    lastRecord.getTotalDays() : 0;

            Integer continuousDays = calculateContinuousDays(userId);
            Integer maxContinuousDays = signinRecordMapper.getMaxContinuousDays(userId);
            if (maxContinuousDays == null) maxContinuousDays = 0;

            Map<String, Object> data = new HashMap<>();
            data.put("history", history);
            data.put("totalDays", totalDays);
            data.put("continuousDays", continuousDays);
            data.put("maxContinuousDays", maxContinuousDays);
            data.put("count", history.size());
            data.put("userId", userId);

            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", data);

            log.info("【获取签到历史】成功，记录数: {}", history.size());

        } catch (Exception e) {
            log.error("【获取签到历史】失败，用户ID: {}", userId, e);
            result.put("code", 500);
            result.put("message", "获取签到历史失败");
        }

        return result;
    }

    /**
     * 获取本月签到日历
     */
    public Map<String, Object> getMonthCalendar(Integer userId, Integer year, Integer month) {
        log.info("【获取月日历】开始，用户ID: {}, 年: {}, 月: {}", userId, year, month);
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取本月签到日期
            List<LocalDate> signinDates = signinRecordMapper.getCurrentMonthSigninDates(userId);
            Set<String> signinDateSet = new HashSet<>();
            for (LocalDate date : signinDates) {
                signinDateSet.add(date.toString());
            }

            // 获取本月签到统计
            Integer monthSigninCount = signinRecordMapper.getCurrentMonthSigninCount(userId);
            if (monthSigninCount == null) monthSigninCount = 0;

            // 计算本月已过天数
            LocalDate today = LocalDate.now();
            int currentYear = year != null ? year : today.getYear();
            int currentMonth = month != null ? month : today.getMonthValue();
            int daysInMonth = today.withMonth(currentMonth).lengthOfMonth();
            int passedDays = today.getDayOfMonth();

            Map<String, Object> data = new HashMap<>();
            data.put("signinDates", signinDateSet);
            data.put("count", monthSigninCount);
            data.put("year", currentYear);
            data.put("month", currentMonth);
            data.put("daysInMonth", daysInMonth);
            data.put("passedDays", passedDays);
            data.put("userId", userId);

            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", data);

            log.info("【获取月日历】成功，签到天数: {}", monthSigninCount);

        } catch (Exception e) {
            log.error("【获取月日历】失败", e);
            result.put("code", 500);
            result.put("message", "获取月日历失败");
        }

        return result;
    }

    /**
     * 获取用户签到统计数据
     */
    public Map<String, Object> getSigninStats(Integer userId) {
        log.info("【获取签到统计】开始，用户ID: {}", userId);
        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Object> stats = signinRecordMapper.getUserSigninStats(userId);
            if (stats == null) {
                stats = new HashMap<>();
                stats.put("total_days", 0);
                stats.put("max_continuous_days", 0);
                stats.put("last_signin_date", null);
                stats.put("signin_count", 0);
            }

            // 计算本月签到天数
            Integer monthSigninCount = signinRecordMapper.getCurrentMonthSigninCount(userId);
            if (monthSigninCount == null) monthSigninCount = 0;

            boolean todaySigned = checkTodaySignedInternal(userId);

            Map<String, Object> data = new HashMap<>();
            data.put("totalDays", stats.get("total_days") != null ? stats.get("total_days") : 0);
            data.put("maxContinuousDays", stats.get("max_continuous_days") != null ? stats.get("max_continuous_days") : 0);
            data.put("lastSigninDate", stats.get("last_signin_date"));
            data.put("monthSigninCount", monthSigninCount);
            data.put("todaySigned", todaySigned);
            data.put("userId", userId);

            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", data);

            log.info("【获取签到统计】成功");

        } catch (Exception e) {
            log.error("【获取签到统计】失败", e);
            result.put("code", 500);
            result.put("message", "获取签到统计失败");
        }

        return result;
    }

    /**
     * 检查今天是否已签到（对外接口）
     */
    public Map<String, Object> checkTodaySigned(Integer userId) {
        log.info("【检查今日签到】开始，用户ID: {}", userId);
        Map<String, Object> result = new HashMap<>();

        try {
            boolean todaySigned = checkTodaySignedInternal(userId);
            Map<String, Object> data = new HashMap<>();
            data.put("todaySigned", todaySigned);
            data.put("userId", userId);

            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", data);

            log.info("【检查今日签到】成功: {}", todaySigned);

        } catch (Exception e) {
            log.error("【检查今日签到】失败", e);
            result.put("code", 500);
            result.put("message", "检查失败");
            result.put("data", Map.of("todaySigned", false, "userId", userId));
        }

        return result;
    }

    /**
     * 手动更新用户签到统计数据
     */
    public Map<String, Object> updateUserSigninStats(Integer userId) {
        log.info("【手动更新统计】开始，用户ID: {}", userId);
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取用户所有签到记录
            List<SigninRecord> allRecords = signinRecordMapper.selectList(
                    new LambdaQueryWrapper<SigninRecord>()
                            .eq(SigninRecord::getUserId, userId)
                            .orderByAsc(SigninRecord::getSigninDate)
            );

            if (allRecords.isEmpty()) {
                result.put("code", 404);
                result.put("message", "用户没有签到记录");
                return result;
            }

            // 重新计算并更新每条记录的total_days
            int updatedCount = 0;
            for (int i = 0; i < allRecords.size(); i++) {
                SigninRecord record = allRecords.get(i);
                int correctTotalDays = i + 1;

                if (record.getTotalDays() == null || record.getTotalDays() != correctTotalDays) {
                    record.setTotalDays(correctTotalDays);
                    signinRecordMapper.updateById(record);
                    updatedCount++;
                }
            }

            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("updatedCount", updatedCount);
            data.put("totalRecords", allRecords.size());

            result.put("code", 200);
            result.put("message", String.format("更新成功，修复了%d条记录", updatedCount));
            result.put("data", data);

            log.info("【手动更新统计】成功，修复记录数: {}", updatedCount);

        } catch (Exception e) {
            log.error("【手动更新统计】失败", e);
            result.put("code", 500);
            result.put("message", "更新失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 数据库连接测试
     */
    public Map<String, Object> testDatabaseConnection() {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer testCount = signinRecordMapper.countUserSigninDays(1);
            boolean connected = testCount != null;

            result.put("code", 200);
            result.put("message", connected ? "数据库连接正常" : "数据库连接异常");
            result.put("data", Map.of(
                    "connected", connected,
                    "testCount", testCount
            ));

            log.info("【数据库测试】连接状态: {}", connected);
        } catch (Exception e) {
            log.error("【数据库测试】失败", e);
            result.put("code", 500);
            result.put("message", "数据库连接异常: " + e.getMessage());
        }
        return result;
    }

    // ================ 私有辅助方法 ================

    /**
     * 内部方法：检查今天是否已签到
     */
    private boolean checkTodaySignedInternal(Integer userId) {
        try {
            Integer check = signinRecordMapper.checkTodaySigned(userId);
            return check != null && check > 0;
        } catch (Exception e) {
            log.error("检查今日签到状态失败", e);
            return false;
        }
    }

    /**
     * 计算连续签到天数
     */
    private Integer calculateContinuousDays(Integer userId) {
        try {
            // 获取最后一条签到记录
            SigninRecord lastRecord = signinRecordMapper.getLastSigninRecord(userId);
            if (lastRecord == null) {
                return 0;
            }

            // 如果最后签到日期不是今天，连续天数为0
            if (!lastRecord.getSigninDate().equals(LocalDate.now())) {
                return 0;
            }

            // 如果最后记录有continuous_days字段，直接使用
            if (lastRecord.getContinuousDays() != null) {
                return lastRecord.getContinuousDays();
            }

            // 否则计算最近的连续天数
            return calculateContinuousDaysFromHistory(userId);

        } catch (Exception e) {
            log.error("计算连续签到天数异常", e);
            return 0;
        }
    }

    /**
     * 从历史记录计算连续天数
     */
    private Integer calculateContinuousDaysFromHistory(Integer userId) {
        try {
            List<SigninRecord> recentSignins = signinRecordMapper.getRecentSignins(userId, 30);
            if (recentSignins == null || recentSignins.isEmpty()) {
                return 0;
            }

            int continuousDays = 1;
            LocalDate currentDate = recentSignins.get(0).getSigninDate();

            for (int i = 1; i < recentSignins.size(); i++) {
                LocalDate previousDate = recentSignins.get(i).getSigninDate();
                if (currentDate.minusDays(1).equals(previousDate)) {
                    continuousDays++;
                    currentDate = previousDate;
                } else {
                    break;
                }
            }

            return continuousDays;
        } catch (Exception e) {
            log.error("从历史计算连续天数异常", e);
            return 1; // 至少1天
        }
    }

    /**
     * 格式化签到日期
     */
    private String formatSigninDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DateTimeFormatter.ofPattern("MM月dd日"));
    }

    /**
     * 创建默认的签到数据
     */
    private Map<String, Object> createDefaultSigninData(Integer userId) {
        Map<String, Object> defaultData = new HashMap<>();
        defaultData.put("todaySigned", false);
        defaultData.put("totalDays", 0);
        defaultData.put("signinTime", "");
        defaultData.put("recentSignins", new ArrayList<>());
        defaultData.put("continuousDays", 0);
        defaultData.put("maxContinuousDays", 0);
        defaultData.put("monthSigninCount", 0);
        defaultData.put("userId", userId != null ? userId : 0);
        return defaultData;
    }
}