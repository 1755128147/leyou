package com.wedu.leyou.cart.service;

import com.wedu.leyou.auth.pojo.UserInfo;
import com.wedu.leyou.cart.client.GoodsClient;
import com.wedu.leyou.cart.interceptor.LoginInterceptor;
import com.wedu.leyou.cart.pojo.Cart;
import com.wedu.leyou.common.enums.ExceptionEnum;
import com.wedu.leyou.common.exception.LyException;
import com.wedu.leyou.common.utils.JsonUtils;
import com.wedu.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    private static final String KEY_PREFIX = "user:cart:";

    public void addCart(Cart cart) {
        // 获取用信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        // 查询购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        String key = cart.getSkuId().toString();
        Integer num = cart.getNum();

        // 判断当前商品是否在购物车中
        if(hashOperations.hasKey(key)) {
            // 在，更新数量
            String cartJson = hashOperations.get(key).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            cart.setNum(cart.getNum() + num);
        } else {
            // 不在，新增购物车
            Sku sku = goodsClient.querySkuBySkuId(cart.getSkuId());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(),",")[0]);
            cart.setPrice(sku.getPrice());

            cart.setUserId(userInfo.getId());
        }
        hashOperations.put(key,JsonUtils.serialize(cart));
    }

    public List<Cart> queryCart() {
        // 获取用信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        // 判断用户是否有购物车记录
        String key = KEY_PREFIX + userInfo.getId();
        if(!redisTemplate.hasKey(key)) {
            throw new LyException(ExceptionEnum.CART_RECORD_NOT_FOUND);
        }
        // 获取购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(key);
        // 获取购物车Map中所有Cart值集合
        List<Object> cartsJson = hashOperations.values();
        // 购物车集合为空，抛出异常信息
        if(CollectionUtils.isEmpty(cartsJson)) {
            throw new LyException(ExceptionEnum.CART_RECORD_NOT_FOUND);
        }
        // 购物车记录序列化
        List<Cart> carts = cartsJson.stream().map(
                cartJson -> JsonUtils.parse(cartJson.toString(), Cart.class)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(carts)) {
            throw new LyException(ExceptionEnum.CART_SERIALIZE_ERROR);
        }
        return carts;
    }

    public void updateNum(Cart cart) {
        // 获取用信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        // 判断用户是否有购物车记录
        String key = KEY_PREFIX + userInfo.getId();
        if(!redisTemplate.hasKey(key)) {
            throw new LyException(ExceptionEnum.CART_RECORD_NOT_FOUND);
        }
        Integer num = cart.getNum();
        // 获取购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(key);
        String cartJson = hashOperations.get(cart.getSkuId().toString()).toString();
        cart = JsonUtils.parse(cartJson, Cart.class);
        cart.setNum(num);
        hashOperations.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }

    public void deleteCart(String skuId) {
        // 获取用信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        // 判断用户是否有购物车记录
        String key = KEY_PREFIX + userInfo.getId();
        if(!redisTemplate.hasKey(key)) {
            throw new LyException(ExceptionEnum.CART_RECORD_NOT_FOUND);
        }
        // 获取购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(key);
        hashOperations.delete(skuId);
    }
}
