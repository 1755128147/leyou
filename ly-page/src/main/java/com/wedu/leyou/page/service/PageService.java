package com.wedu.leyou.page.service;

import com.wedu.leyou.item.pojo.*;
import com.wedu.leyou.page.client.BrandClient;
import com.wedu.leyou.page.client.CategoryClient;
import com.wedu.leyou.page.client.GoodsClient;
import com.wedu.leyou.page.client.SpecificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PageService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecificationClient specClient;

    @Autowired
    private TemplateEngine templateEngine;

    public Map<String,Object> loadModel(Long spuId) {
        Map<String,Object> model = new HashMap<>();
        //查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        //查询skus
        List<Sku> skus = spu.getSkus();
        // 查询detail
        SpuDetail spuDetail = goodsClient.queryDetailById(spuId);
        // 查询brand
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        // 查询商品分类
        List<Category> categories = categoryClient.queryCategoryByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        // 查询规格参数组，及组内参数
        List<SpecGroup> groups = specClient.queryListByCid(spu.getCid3());
        // 查询特殊规格参数
        List<SpecParam> specParams = specClient.queryParamList(null, spu.getCid3(), false, null);
        Map<Long,String> paramMap = new HashMap<>();
        specParams.forEach(specParam -> {
            paramMap.put(specParam.getId(),specParam.getName());
        });

        model.put("spu",spu);
        model.put("skus",skus);
        model.put("spuDetail",spuDetail);
        model.put("brand",brand);
        model.put("categories",categories);
        model.put("groups",groups);
        model.put("paramMap",paramMap);

        return model;
    }

    public void createHtml(Long spuId) {
        // 运行上下文
        Context context = new Context();
        context.setVariables(loadModel(spuId));
        // 输出流
        File dest = new File("E:/project/leyou/html",spuId + ".html");
        // 判断文件是否存在，如果存在删除重新生成，不存在直接生成
        if(dest.exists()) {
            dest.delete();
        }
        try(PrintWriter writer = new PrintWriter(dest, "UTF-8")){
            // 生成Html
            templateEngine.process("item",context,writer);
        }catch (Exception e) {
            log.error("[静态页服务] 生成静态页异常！",e);
        }
    }

    public void deleteHtml(Long spuId) {
        File dest = new File("E:/project/leyou/html",spuId + ".html");
        if(dest.exists()) {
            dest.delete();
        }
    }
}
