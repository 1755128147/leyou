package com.wedu.leyou.item.api;

import com.wedu.leyou.common.vo.PageResult;
import com.wedu.leyou.item.pojo.Sku;
import com.wedu.leyou.item.pojo.Spu;
import com.wedu.leyou.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsApi {

    /**
     * 根据spu的id查询详情detail
     * @param spuId
     * @return
     */
    @GetMapping("goods/spu/detail/{id}")
    SpuDetail queryDetailById(@PathVariable("id") Long spuId);

    /**
     * 根据spu查询对应的所有sku
     * @param spuId
     * @return
     */
    @GetMapping("goods/sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("id") Long spuId);

    /**
     * 分页查询SPU
     * @param page 当前页码
     * @param rows 每页的数量
     * @param saleable 上架或下架
     * @param key   过滤条件
     * @return
     */
    @GetMapping("goods/spu/page")
    PageResult<Spu> querySpuByPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable",required = false) Boolean saleable,
            @RequestParam(value = "key",required = false) String key);

    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @GetMapping("goods/spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);

    @GetMapping("goods/sku/{skuId}")
    Sku querySkuBySkuId(@PathVariable("skuId") Long skuId);
}
