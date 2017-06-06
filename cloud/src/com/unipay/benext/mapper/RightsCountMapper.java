package com.unipay.benext.mapper;

import com.unipay.benext.model.cloud.RightsCount;

import java.util.List;

/**
 * Created by Administrator on 2017/2/23.
 */
public interface RightsCountMapper {
    public List<RightsCount> getList();

    void deleteAllRightsCount();

    void saveAllRightsCount(List<RightsCount> list);
}
