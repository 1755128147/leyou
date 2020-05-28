package com.wedu.leyou.order.service.api;

import com.wedu.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value = "api-gateway", path = "/api/item")
public interface GoodsService extends GoodsApi {
}
