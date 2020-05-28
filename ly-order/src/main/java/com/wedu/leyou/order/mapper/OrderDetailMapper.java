package com.wedu.leyou.order.mapper;

import com.wedu.leyou.order.pojo.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;


@Mapper
public interface OrderDetailMapper extends tk.mybatis.mapper.common.Mapper<OrderDetail>, InsertListMapper<OrderDetail> {
}
