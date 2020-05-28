package com.wedu.leyou.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "tb_spu")
public class Spu {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long brandId;
    private Long cid1;// 1级类目
    private Long cid2;// 2级类目
    private Long cid3;// 3级类目
    private String title;// 标题
    private String subTitle;// 子标题
    private Boolean saleable;// 是否上架
    @JsonIgnore
    private Boolean valid;// 是否有效，逻辑删除用
    private Date createTime;// 创建时间
    @JsonIgnore //json序列化时将java bean中的一些属性忽略掉,序列化和反序列化都受影响。
    private Date lastUpdateTime;// 最后修改时间

    @Transient
    private String cname;//分类名称
    @Transient
    private String bname;//品牌名称
    @Transient
    private List<Sku> skus;//sku集合
    @Transient
    private SpuDetail spuDetail;//spu详情
}
