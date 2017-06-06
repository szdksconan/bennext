package com.unipay.benext.mapper;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/2/11.
 */
@Transactional
public interface MybatisTestMapper {
    public List test();
}
