package com.xuejiaao.foodapp.controller;

import com.xuejiaao.foodapp.entity.Evaluate;
import com.xuejiaao.foodapp.entity.vo.MyResult;
import com.xuejiaao.foodapp.service.EvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluate")
public class EvaluateController {

    @Autowired
    private EvaluateService evaluateService;

    /**
     * 添加评价
     * @param evaluate 评价对象
     * @return 操作结果
     */
    @PostMapping("/add")
    public MyResult addEvaluate(@RequestBody Evaluate evaluate) {
        MyResult result = new MyResult();
        try {
            // 保存评价
            boolean saveResult = evaluateService.save(evaluate);
            if (saveResult) {
                result.setCode(200);
                result.setMsg("评价提交成功");
                result.setData(null);
            } else {
                result.setCode(500);
                result.setMsg("评价提交失败");
                result.setData(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMsg("评价提交异常：" + e.getMessage());
            result.setData(null);
        }
        return result;
    }
}
