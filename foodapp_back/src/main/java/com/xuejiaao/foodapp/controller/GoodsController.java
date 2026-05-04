package com.xuejiaao.foodapp.controller;

import com.xuejiaao.foodapp.entity.Goods;
import com.xuejiaao.foodapp.entity.vo.CurGoodsVo;
import com.xuejiaao.foodapp.entity.vo.MyResult;
import com.xuejiaao.foodapp.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/goods")
@CrossOrigin
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping({"/findbycgid/{cgid}","/findbycgid"})
    public MyResult findByCgid(@PathVariable(value = "cgid",required = false) Integer cgid,
                              @RequestParam(value = "sid",required = false) Integer sid,
                              @RequestParam(value = "page",required = false) Integer page,
                              @RequestParam(value = "pageSize",required = false) Integer pageSize){
       MyResult myResult = new MyResult();
        List<Goods> list = goodsService.findByCgid(cgid, sid, page, pageSize);
        if(list!=null && list.size()>0){
            myResult.setCode(200);
            myResult.setMsg("已成功查询到商品列表");
            myResult.setData(list);
        }
        else{
            myResult.setCode(404);
            myResult.setMsg("未查询到商品列表");
            myResult.setData(null);
        }
        return myResult;
    }
    @GetMapping({"/findbykeyword/{keyword}"})
    public MyResult findByKeyWord(@PathVariable(value = "keyword") String keyword){
        MyResult myResult = new MyResult();
        List<Goods> list = goodsService.findByKeyWord(keyword);
        if(list!=null && list.size()>0){
            myResult.setCode(200);
            myResult.setMsg("已成功查询到商品列表");
            myResult.setData(list);
        }
        else{
            myResult.setCode(404);
            myResult.setMsg("未查询到商品列表");
            myResult.setData(null);
        }
        return myResult;
    }

    @GetMapping({"/getcurgoodsinfo/{gid}"})
    public MyResult getCurGoodsInfo(@PathVariable(value = "gid") Long gid){
        MyResult myResult = new MyResult();
        CurGoodsVo curGoodsVo = goodsService.getCurGoodsByGid(gid);
        if(curGoodsVo!=null){
            myResult.setCode(200);
            myResult.setMsg("已成功查询到商品信息");
            myResult.setData(curGoodsVo);
        }
        else{
            myResult.setCode(404);
            myResult.setMsg("未查询到商品信息");
            myResult.setData(null);
        }
        return myResult;
    }
}
