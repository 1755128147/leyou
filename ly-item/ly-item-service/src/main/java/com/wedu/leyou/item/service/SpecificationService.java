package com.wedu.leyou.item.service;

import com.wedu.leyou.common.enums.ExceptionEnum;
import com.wedu.leyou.common.exception.LyException;
import com.wedu.leyou.item.mapper.SpecGroupMapper;
import com.wedu.leyou.item.mapper.SpecParamMapper;
import com.wedu.leyou.item.mapper.SpecificationMapper;
import com.wedu.leyou.item.pojo.SpecGroup;
import com.wedu.leyou.item.pojo.SpecParam;
import com.wedu.leyou.item.pojo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper groupMapper;

    @Autowired
    private SpecParamMapper paramMapper;

    /*@Autowired
    private SpecificationMapper specificationMapper;

    public Specification queryById(Long id) {
        Specification specification = specificationMapper.selectByPrimaryKey(id);
        if(specification == null) {
            throw new LyException(ExceptionEnum.SPEC_NOT_FOUND);
        }
        return specification;
    }*/

    public List<SpecGroup> queryGroupByCid(Long cid) {
        SpecGroup group = new SpecGroup();
        group.setCid(cid);
        List<SpecGroup> groups = groupMapper.select(group);
        if(CollectionUtils.isEmpty(groups)) {
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return groups;
    }

    public List<SpecParam> queryParamList(Long gid,Long cid,Boolean generic,Boolean searching) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setGeneric(generic);
        param.setSearching(searching);
        List<SpecParam> params = paramMapper.select(param);
        if(param == null) {
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return params;
    }

    public List<SpecGroup> queryListByCid(Long cid) {
        // 查询规格组
        List<SpecGroup> specGroups = queryGroupByCid(cid);
        // 查询当前分类下的参数
        // 方式一：
        /*List<SpecParam> specParams = queryParamList(null, cid, null,null);
        // 先把规格参数变成map，map的key是规格组id，map的值是组下的所有参数
        Map<Long,List<SpecParam>> map = new HashMap<>();
        for (SpecParam param : specParams) {
            if(!map.containsKey(param.getGroupId())) {
                // 这个组id在map中不存在，新增一个list
                map.put(param.getGroupId(),new ArrayList<>());
            }
            map.get(param.getGroupId()).add(param);
        }
        // 填充param到group
        for (SpecGroup specGroup : specGroups) {
            specGroup.setParams(map.get(specGroup.getId()));
        }*/
        // 方式二:
        specGroups.forEach(specGroup -> {
            List<SpecParam> specParams = queryParamList(specGroup.getId(),null,null,null);
            specGroup.setParams(specParams);
        });

        return specGroups;
    }
}
