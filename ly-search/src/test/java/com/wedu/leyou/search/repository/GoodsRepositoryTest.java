package com.wedu.leyou.search.repository;

import com.wedu.leyou.common.vo.PageResult;
import com.wedu.leyou.item.pojo.Spu;
import com.wedu.leyou.search.client.GoodsClient;
import com.wedu.leyou.search.pojo.Goods;
import com.wedu.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    @Test
    public void testCreateIndex() {
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }

    @Test
    public void loadData() {
        int page = 1;
        int rows = 100;
        int size = 0;
        do {
            // 查询spu信息
            PageResult<Spu> result = this.goodsClient.querySpuByPage(page, rows, true, null);
            List<Spu> spus = result.getItems();

            // spu转为goods
            List<Goods> goods = spus.stream().map(searchService::bulidGoods)
                    .collect(Collectors.toList());

            // 把goods存入索引库
            this.goodsRepository.saveAll(goods);
            //翻页
            size = spus.size();
            page++;
        }while (size == 100);
    }
}