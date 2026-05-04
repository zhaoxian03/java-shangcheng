package com.xuejiaao.foodapp.controller;

import com.xuejiaao.foodapp.entity.Category;
import com.xuejiaao.foodapp.entity.vo.MyResult;
import com.xuejiaao.foodapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
//允许跨域请求
@CrossOrigin
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 接受查询所有商品分类的请求，并返回响应数据
     * @return
     */
    @RequestMapping("/findall")
    public MyResult findAll(){
        MyResult myResult = new MyResult();
        List<Category> list = categoryService.findAll();
        if(list!=null && list.size()>0){
            myResult.setCode(200);
            myResult.setMsg("已成功查询到所有的商品分类");
            myResult.setData(list);
        }
        else{
            myResult.setCode(404);
            myResult.setMsg("未查询到所有的商品分类");
            myResult.setData(null);
        }
       return myResult;
    }
}
