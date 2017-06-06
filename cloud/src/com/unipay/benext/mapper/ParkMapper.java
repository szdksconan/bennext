package com.unipay.benext.mapper;

import com.unipay.benext.model.cloud.Park;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/4.
 */
public interface ParkMapper {
    void deleteAllPark();

    void saveAllPartInfo(List<Park> list);

    Map getBenefitUrl();

    void saveBenefitUrl(Map map);
}
